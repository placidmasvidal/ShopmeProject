package com.shopme.customer.controller;

import com.shopme.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ForgotPasswordController {

    private CustomerService customerService;

    @Autowired
    public ForgotPasswordController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/forgot_password")
    public String showRequestForm(){
        return "customer/forgot_password_form";
    }
}
