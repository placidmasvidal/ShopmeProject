package com.shopme.checkout.controller;

import com.shopme.address.AddressService;
import com.shopme.checkout.CheckoutInfo;
import com.shopme.checkout.CheckoutService;
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
public class CheckoutController {

    private CheckoutService checkoutService;
    private CustomerService customerService;
    private AddressService addressService;
    private ShippingRateService shippingRateService;
    private ShoppingCartService shoppingCartService;

    @Autowired
    public CheckoutController(CheckoutService checkoutService,
                              CustomerService customerService,
                              AddressService addressService,
                              ShippingRateService shippingRateService,
                              ShoppingCartService shoppingCartService) {
        this.checkoutService = checkoutService;
        this.customerService = customerService;
        this.addressService = addressService;
        this.shippingRateService = shippingRateService;
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping("/checkout")
    public String showCheckoutPage(Model model, HttpServletRequest servletRequest){
        Customer customer = getAuthenticatedCustomer(servletRequest);

        Address defaultAddress = addressService.getDefaultAddress(customer);
        ShippingRate shippingRate = null;

        if(defaultAddress != null){
            model.addAttribute("shippingAddress", defaultAddress.toString());
            shippingRate = shippingRateService.getShippingRateForAddress(defaultAddress);
        } else {
            model.addAttribute("shippingAddress", customer.toString());
            shippingRate = shippingRateService.getShippingRateForCustomer(customer);
        }

        if(shippingRate == null){
            return "redirect:/cart";
        }

        List<CartItem> cartItems = shoppingCartService.listCartItems(customer);
        CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate);

        model.addAttribute("checkoutInfo", checkoutInfo);
        model.addAttribute("cartItems", cartItems);

        return "checkout/checkout";
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest servletRequest) {
        String email = Utility.getEmailOfAuthenticatedCustomer(servletRequest);

        return customerService.getCustomerByEmail(email);
    }
}