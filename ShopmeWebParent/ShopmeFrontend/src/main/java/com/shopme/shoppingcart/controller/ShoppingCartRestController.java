package com.shopme.shoppingcart.controller;

import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.customer.CustomerService;
import com.shopme.shoppingcart.ShoppingCartException;
import com.shopme.shoppingcart.ShoppingCartService;
import com.shopme.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
public class ShoppingCartRestController {

  private ShoppingCartService shoppingCartService;

  private CustomerService customerService;

  @Autowired
  public ShoppingCartRestController(ShoppingCartService shoppingCartService, CustomerService customerService) {
    this.shoppingCartService = shoppingCartService;
    this.customerService = customerService;
  }

  @PostMapping("/cart/add/{productId}/{quantity}")
  public String addProductToCart(
      @PathVariable(name = "productId") Integer productId,
      @PathVariable(name = "quantity") Integer quantity,
      HttpServletRequest servletRequest) {
    try {
      Customer customer = getAuthenticatedCustomer(servletRequest);
      Integer updatedQuantity = shoppingCartService.addProduct(productId, quantity, customer);
      return updatedQuantity + " item(s) of this product were added to your shopping cart.";
    } catch (CustomerNotFoundException e) {
      return "You must login to add this product to cart.";
    } catch (ShoppingCartException e){
      return e.getMessage();
    }
  }

  private Customer getAuthenticatedCustomer(HttpServletRequest servletRequest) throws CustomerNotFoundException {
    String email = Utility.getEmailOfAuthenticatedCustomer(servletRequest);
    if(email == null){
      throw new CustomerNotFoundException("No authenticated customer");
    }

    return customerService.getCustomerByEmail(email);
  }
}
