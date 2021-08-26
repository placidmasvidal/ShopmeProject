package com.shopme.order.controller;

import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.common.exception.OrderNotFoundException;
import com.shopme.customer.CustomerService;
import com.shopme.order.OrderReturnRequest;
import com.shopme.order.OrderReturnResponse;
import com.shopme.order.OrderService;
import com.shopme.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class OrderRestController {

  private OrderService orderService;

  private CustomerService customerService;

  @Autowired
  public OrderRestController(OrderService orderService, CustomerService customerService) {
    this.orderService = orderService;
    this.customerService = customerService;
  }

  @PostMapping("/orders/return")
  public ResponseEntity<?> handleOrderReturnRequest(
      @RequestBody OrderReturnRequest returnRequest, HttpServletRequest servletRequest) {
    System.out.println("Order ID: " + returnRequest.getOrderId());
    System.out.println("Reason: " + returnRequest.getReason());
    System.out.println("Note: " + returnRequest.getNote());
    try {
      Customer customer = getAuthenticatedCustomer(servletRequest);
      orderService.setOrderReturnRequested(returnRequest, customer);
      return new ResponseEntity<>(
          new OrderReturnResponse(returnRequest.getOrderId()), HttpStatus.OK);
    } catch (CustomerNotFoundException e) {
      return new ResponseEntity<>("Authentication required", HttpStatus.BAD_REQUEST);
    } catch (OrderNotFoundException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  private Customer getAuthenticatedCustomer(HttpServletRequest servletRequest)
      throws CustomerNotFoundException {
    String email = Utility.getEmailOfAuthenticatedCustomer(servletRequest);
    if (email == null) {
      throw new CustomerNotFoundException("No authenticated customer");
    }
    return customerService.getCustomerByEmail(email);
  }
}
