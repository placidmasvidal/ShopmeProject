package com.shopme.address.controller;

import com.shopme.address.AddressService;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.customer.CustomerService;
import com.shopme.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
  public String showAddressBook(Model model, HttpServletRequest servletRequest) {
    Customer customer = getAuthenticatedCustomer(servletRequest);
    List<Address> listAddresses = addressService.listAddressBook(customer);

    boolean usePrimaryAddressAsDefault = true;

    for (Address address : listAddresses) {
      if (address.isDefaultForShipping()) {
        usePrimaryAddressAsDefault = false;
        break;
      }
    }

    model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);
    model.addAttribute("customer", customer);
    model.addAttribute("listAddresses", listAddresses);

    return "address_book/addresses";
  }

  @GetMapping("/address_book/new")
  public String newAddress(Model model) {
    List<Country> listCountries = customerService.listAllCountries();

    model.addAttribute("listCountries", listCountries);
    model.addAttribute("address", new Address());
    model.addAttribute("pageTitle", "Add New Address");

    return "address_book/address_form";
  }

  @PostMapping("/address_book/save")
  public String saveAddress(
      Address address, HttpServletRequest servletRequest, RedirectAttributes redirectAttributes) {
    Customer customer = getAuthenticatedCustomer(servletRequest);

    address.setCustomer(customer);
    addressService.save(address);

    String redirectOption = servletRequest.getParameter("redirect");
    String redirectURL = "redirect:/address_book";

    if ("checkout".equals(redirectOption)) {
      redirectURL += "?redirect=checkout";
    }

    redirectAttributes.addFlashAttribute("message", "The address has been saved successfully.");

    return redirectURL;
  }

  @GetMapping("/address_book/edit/{id}")
  public String editAddress(@PathVariable("id") Integer addressId, Model model, HttpServletRequest servletRequest){
    Customer customer = getAuthenticatedCustomer(servletRequest);
    List<Country> listCountries = customerService.listAllCountries();

    Address address = addressService.get(addressId, customer.getId());

    model.addAttribute("address", address);
    model.addAttribute("listCountries", listCountries);
    model.addAttribute("pageTitle", "Edit Address (ID: " + addressId + ")");

    return "address_book/address_form";
  }

  @GetMapping("/address_book/delete/{id}")
  public String deleteAddress(@PathVariable("id") Integer addressId, RedirectAttributes redirectAttributes,
                              HttpServletRequest request) {
    Customer customer = getAuthenticatedCustomer(request);
    addressService.delete(addressId, customer.getId());

    redirectAttributes.addFlashAttribute("message", "The address ID " + addressId + " has been deleted.");

    return "redirect:/address_book";
  }

  @GetMapping("/address_book/default/{id}")
  public String setDefaultAddress(@PathVariable("id") Integer addressId, HttpServletRequest servletRequest){
    Customer customer = getAuthenticatedCustomer(servletRequest);
    addressService.setDefaultAddress(addressId, customer.getId());

    String redirectOption = servletRequest.getParameter("redirect");
    String redirectURL = "redirect:/address_book";

    if ("cart".equals(redirectOption)) {
      redirectURL = "redirect:/cart";
    } else if("checkout".equals(redirectOption)){
      redirectURL = "redirect:/checkout";
    }

    return redirectURL;
  }

  private Customer getAuthenticatedCustomer(HttpServletRequest servletRequest) {
    String email = Utility.getEmailOfAuthenticatedCustomer(servletRequest);

    return customerService.getCustomerByEmail(email);
  }
}
