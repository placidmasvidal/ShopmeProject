package com.shopme.customer.controller;

import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ForgotPasswordController {

  private CustomerService customerService;

  @Autowired
  public ForgotPasswordController(CustomerService customerService) {
    this.customerService = customerService;
  }

  @GetMapping("/forgot_password")
  public String showRequestForm() {
    return "customer/forgot_password_form";
  }

  @PostMapping("/forgot_password")
  public String processRequestForm(HttpServletRequest servletRequest, Model model) {
    String email = servletRequest.getParameter("email");
    try {
      String token = customerService.updateResetPasswordToken(email);
      System.out.println("email = " + email);
      System.out.println("token = " + token);
    } catch (CustomerNotFoundException ex) {
      model.addAttribute("error", ex.getMessage());
    }
    return "customer/forgot_password_form";
  }
}
