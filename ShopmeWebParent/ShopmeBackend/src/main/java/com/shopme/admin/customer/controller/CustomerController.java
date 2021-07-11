package com.shopme.admin.customer.controller;

import com.shopme.admin.customer.CustomerConstants;
import com.shopme.admin.customer.CustomerNotFoundException;
import com.shopme.admin.customer.CustomerService;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CustomerController {

  private CustomerService customerService;

  @Autowired
  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  @GetMapping("/customers")
  public String listFirstPage(Model model) {
    return listByPage(model, 1, "firstName", "asc", null);
  }

  @GetMapping("/customers/page/{pageNum}")
  public String listByPage(
      Model model,
      @PathVariable(name = "pageNum") int pageNum,
      @Param("sortField") String sortField,
      @Param("sortDir") String sortDir,
      @Param("keyword") String keyword) {

    Page<Customer> page = customerService.listByPage(pageNum, sortField, sortDir, keyword);
    List<Customer> listCustomers = page.getContent();

    long startCount = (pageNum - 1) * CustomerConstants.CUSTOMERS_PER_PAGE + 1;
    model.addAttribute("startCount", startCount);

    long endCount = startCount + CustomerConstants.CUSTOMERS_PER_PAGE - 1;
    if (endCount > page.getTotalElements()) {
      endCount = page.getTotalElements();
    }

    model.addAttribute("totalPages", page.getTotalPages());
    model.addAttribute("totalItems", page.getTotalElements());
    model.addAttribute("currentPage", pageNum);
    model.addAttribute("listCustomers", listCustomers);
    model.addAttribute("sortField", sortField);
    model.addAttribute("sortDir", sortDir);
    model.addAttribute("keyword", keyword);
    model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
    model.addAttribute("endCount", endCount);

    return "customers/customers";
  }

  @GetMapping("/customers/{id}/enabled/{status}")
  public String updateCustomerEnabledStatus(
      @PathVariable("id") Integer id,
      @PathVariable("status") boolean enabled,
      RedirectAttributes redirectAttributes) {
    customerService.updateCustomerEnabledStatus(id, enabled);
    String status = enabled ? "enabled" : "disabled";
    String message = "The Customer ID " + id + " has been " + status;
    redirectAttributes.addFlashAttribute("message", message);

    return "redirect:/customers";
  }

  @GetMapping("/customers/detail/{id}")
  public String viewCustomer(
      @PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
    try {
      Customer customer = customerService.get(id);
      model.addAttribute("customer", customer);

      return "customers/customer_detail_modal";
    } catch (CustomerNotFoundException ex) {
      redirectAttributes.addFlashAttribute("message", ex.getMessage());
      return "redirect:/customers";
    }
  }

  @GetMapping("/customers/edit/{id}")
  public String editCustomer(
      @PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
    try {
      Customer customer = customerService.get(id);
      List<Country> countries = customerService.listAllCountries();

      model.addAttribute("listCountries", countries);
      model.addAttribute("customer", customer);
      model.addAttribute("pageTitle", String.format("Edit Customer (ID: %d)", id));

      return "customers/customer_form";

    } catch (CustomerNotFoundException ex) {
      redirectAttributes.addFlashAttribute("message", ex.getMessage());
      return "redirect:/customers";
    }
  }

  @PostMapping("/customers/save")
  public String saveCustomer(
      Customer customer, Model model, RedirectAttributes redirectAttributes) {
    customerService.save(customer);
    redirectAttributes.addFlashAttribute(
        "message", "The customer ID " + customer.getId() + " has been updated successfully.");
    return "redirect:/customers";
  }

  @GetMapping("/customers/delete/{id}")
  public String deleteCustomer(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
    try {
      customerService.delete(id);
      redirectAttributes.addFlashAttribute(
          "message", "The customer ID " + id + " has been deleted successfully.");

    } catch (CustomerNotFoundException ex) {
      redirectAttributes.addFlashAttribute("message", ex.getMessage());
    }

    return "redirect:/customers";
  }
}
