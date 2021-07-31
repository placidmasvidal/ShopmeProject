package com.shopme.checkout.controller;

import com.shopme.address.AddressService;
import com.shopme.checkout.CheckoutInfo;
import com.shopme.checkout.CheckoutService;
import com.shopme.checkout.paypal.PayPalApiException;
import com.shopme.checkout.paypal.PayPalService;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.PaymentMethod;
import com.shopme.customer.CustomerService;
import com.shopme.order.OrderService;
import com.shopme.setting.EmailSettingBag;
import com.shopme.setting.PaymentSettingBag;
import com.shopme.setting.SettingService;
import com.shopme.shipping.ShippingRateService;
import com.shopme.shoppingcart.ShoppingCartService;
import com.shopme.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class CheckoutController {

  private CheckoutService checkoutService;
  private CustomerService customerService;
  private AddressService addressService;
  private ShippingRateService shippingRateService;
  private ShoppingCartService shoppingCartService;
  private OrderService orderService;
  private SettingService settingService;
  private PayPalService payPalService;

  @Autowired
  public CheckoutController(
      CheckoutService checkoutService,
      CustomerService customerService,
      AddressService addressService,
      ShippingRateService shippingRateService,
      ShoppingCartService shoppingCartService,
      OrderService orderService,
      SettingService settingService,
      PayPalService payPalService) {
    this.checkoutService = checkoutService;
    this.customerService = customerService;
    this.addressService = addressService;
    this.shippingRateService = shippingRateService;
    this.shoppingCartService = shoppingCartService;
    this.orderService = orderService;
    this.settingService = settingService;
    this.payPalService = payPalService;
  }

  @GetMapping("/checkout")
  public String showCheckoutPage(Model model, HttpServletRequest servletRequest) {
    Customer customer = getAuthenticatedCustomer(servletRequest);

    Address defaultAddress = addressService.getDefaultAddress(customer);
    ShippingRate shippingRate = null;

    if (defaultAddress != null) {
      model.addAttribute("shippingAddress", defaultAddress.toString());
      shippingRate = shippingRateService.getShippingRateForAddress(defaultAddress);
    } else {
      model.addAttribute("shippingAddress", customer.toString());
      shippingRate = shippingRateService.getShippingRateForCustomer(customer);
    }

    if (shippingRate == null) {
      return "redirect:/cart";
    }

    List<CartItem> cartItems = shoppingCartService.listCartItems(customer);
    CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate);

    String currencyCode = settingService.getCurrencyCode();
    PaymentSettingBag paymentSettings = settingService.getPaymentSettings();
    String paypalClientId = paymentSettings.getClientID();

    model.addAttribute("paypalClientId", paypalClientId);
    model.addAttribute("currencyCode", currencyCode);
    model.addAttribute("customer", customer);
    model.addAttribute("checkoutInfo", checkoutInfo);
    model.addAttribute("cartItems", cartItems);

    return "checkout/checkout";
  }

  private Customer getAuthenticatedCustomer(HttpServletRequest servletRequest) {
    String email = Utility.getEmailOfAuthenticatedCustomer(servletRequest);

    return customerService.getCustomerByEmail(email);
  }

  @PostMapping("/place_order")
  public String placeOrder(HttpServletRequest servletRequest)
      throws UnsupportedEncodingException, MessagingException {
    String paymentType = servletRequest.getParameter("paymentMethod");
    PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentType);

    Customer customer = getAuthenticatedCustomer(servletRequest);

    Address defaultAddress = addressService.getDefaultAddress(customer);
    ShippingRate shippingRate = null;

    if (defaultAddress != null) {
      shippingRate = shippingRateService.getShippingRateForAddress(defaultAddress);
    } else {
      shippingRate = shippingRateService.getShippingRateForCustomer(customer);
    }

    List<CartItem> cartItems = shoppingCartService.listCartItems(customer);
    CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate);

    Order createdOrder =
        orderService.createOrder(customer, defaultAddress, cartItems, paymentMethod, checkoutInfo);
    shoppingCartService.deleteByCustomer(customer);
    sendOrderConfirmationEmail(servletRequest, createdOrder);

    return "checkout/order_completed";
  }

  private void sendOrderConfirmationEmail(HttpServletRequest servletRequest, Order order)
      throws UnsupportedEncodingException, MessagingException {
    EmailSettingBag emailSettings = settingService.getEmailSettings();
    JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
    mailSender.setDefaultEncoding("utf-8");

    String toAddress = order.getCustomer().getEmail();
    String subject = emailSettings.getOrderConfirmationSubject();
    String content = emailSettings.getOrderConfirmationContent();

    subject = subject.replace("[[orderId]]", String.valueOf(order.getId()));

    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);

    helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
    helper.setTo(toAddress);
    helper.setSubject(subject);

    DateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss E, dd MMM yyyy");
    String orderTime = dateFormatter.format(order.getOrderTime());

    String totalAmount =
        Utility.formatCurrency(order.getTotal(), settingService.getCurrencySettings());

    content = content.replace("[[name]]", order.getCustomer().getFullName());
    content = content.replace("[[orderId]]", String.valueOf(order.getId()));
    content = content.replace("[[orderTime]]", orderTime);
    content = content.replace("[[shippingAddress]]", order.getShippingAddress());
    content = content.replace("[[total]]", totalAmount);
    content = content.replace("[[paymentMethod]]", order.getPaymentMethod().toString());

    helper.setText(content, true);
    mailSender.send(message);
  }

  @PostMapping("/process_paypal_order")
  public String processPayPalOrder(HttpServletRequest servletRequest, Model model)
      throws UnsupportedEncodingException, MessagingException {
    String orderId = servletRequest.getParameter("orderId");

    try {
      payPalService.validateOrder(orderId);
      return placeOrder(servletRequest);
    } catch (PayPalApiException e) {
      model.addAttribute("title", "Checkout Failure");
      model.addAttribute(
          "message",
          "ERROR: Transaction could not be completed because order information is invalid");
      return e.getMessage();
    }
  }
}
