package com.shopme.shoppingcart.controller;

import com.shopme.address.AddressService;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;
import com.shopme.customer.CustomerService;
import com.shopme.shipping.ShippingRateService;
import com.shopme.shoppingcart.ShoppingCartService;
import com.shopme.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ShoppingCartController {

  private ShoppingCartService shoppingCartService;

  private CustomerService customerService;

  private AddressService addressService;

  private ShippingRateService shippingRateService;

  @Autowired
  public ShoppingCartController(
      ShoppingCartService shoppingCartService,
      CustomerService customerService,
      AddressService addressService,
      ShippingRateService shippingRateService) {
    this.shoppingCartService = shoppingCartService;
    this.customerService = customerService;
    this.addressService = addressService;
    this.shippingRateService = shippingRateService;
  }

  @GetMapping("/cart")
  public String viewCart(Model model, HttpServletRequest servletRequest) {
    Customer customer = getAuthenticatedCustomer(servletRequest);
    List<CartItem> cartItems = shoppingCartService.listCartItems(customer);

    float estimatedTotal =
        (float)
            cartItems.stream()
                .map(CartItem::getSubtotal)
                .mapToDouble(item -> item.doubleValue())
                .sum();

    Address defaultAddress = addressService.getDefaultAddress(customer);
    ShippingRate shippingRate = null;
    boolean usePrimaryAddressAsDefault = false;

    if(defaultAddress != null){
      shippingRate = shippingRateService.getShippingRateForAddress(defaultAddress);
    } else {
      usePrimaryAddressAsDefault = true;
      shippingRate = shippingRateService.getShippingRateForCustomer(customer);
    }

    model.addAttribute("shippingSupported", shippingRate != null);
    model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);
    model.addAttribute("cartItems", cartItems);
    model.addAttribute("estimatedTotal", estimatedTotal);

    return "cart/shopping_cart";
  }

  private Customer getAuthenticatedCustomer(HttpServletRequest servletRequest) {
    String email = Utility.getEmailOfAuthenticatedCustomer(servletRequest);

    return customerService.getCustomerByEmail(email);
  }
}
