package com.shopme.shoppingcart.controller;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.customer.CustomerService;
import com.shopme.shoppingcart.ShoppingCartService;
import com.shopme.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.DoubleStream;

@Controller
public class ShoppingCartController {

  private ShoppingCartService shoppingCartService;

  private CustomerService customerService;

  @Autowired
  public ShoppingCartController(
      ShoppingCartService shoppingCartService, CustomerService customerService) {
    this.shoppingCartService = shoppingCartService;
    this.customerService = customerService;
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

    model.addAttribute("cartItems", cartItems);
    model.addAttribute("estimatedTotal", estimatedTotal);

    return "cart/shopping_cart";
  }

  private Customer getAuthenticatedCustomer(HttpServletRequest servletRequest) {
    String email = Utility.getEmailOfAuthenticatedCustomer(servletRequest);

    return customerService.getCustomerByEmail(email);
  }
}
