package com.shopme.address.controller;

import com.shopme.address.AddressService;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;
import com.shopme.customer.CustomerService;
import com.shopme.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class AddressController {

    private AddressService addressService;

    private CustomerService customerService;

    @Autowired
    public AddressController(AddressService addressService, CustomerService customerService) {
        this.addressService = addressService;
        this.customerService = customerService;
    }

    @GetMapping("/address_book")
    public String showAddressBook(Model model, HttpServletRequest servletRequest){
        Customer customer = getAuthenticatedCustomer(servletRequest);
        List<Address> listAddresses = addressService.listAddressBook(customer);

        model.addAttribute("customer", customer);
        model.addAttribute("listAddresses", listAddresses);

        return "address_book/addresses";
    }



    private Customer getAuthenticatedCustomer(HttpServletRequest servletRequest) {
        String email = Utility.getEmailOfAuthenticatedCustomer(servletRequest);

        return customerService.getCustomerByEmail(email);
    }
}
