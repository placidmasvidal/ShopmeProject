package com.shopme.admin.product.controller;

import com.shopme.admin.brand.BrandService;
import com.shopme.admin.category.CategoryNotFoundException;
import com.shopme.admin.product.ProductService;
import com.shopme.admin.util.FileUploadUtil;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ProductController {

  private ProductService productService;

  private BrandService brandService;

  @Autowired
  public ProductController(ProductService productService, BrandService brandService) {
    this.productService = productService;
    this.brandService = brandService;
  }

  @GetMapping("/products")
  public String listAll(Model model) {

    List<Product> listProducts = productService.listAll();

    model.addAttribute("listProducts", listProducts);
    return "products/products";
  }

  @GetMapping("/products/new")
  public String newProduct(Model model) {
    List<Brand> listBrands = brandService.listAll();

    Product product = new Product();
    product.setEnabled(true);
    product.setInStock(true);

    model.addAttribute("listBrands", listBrands);
    model.addAttribute("product", product);
    model.addAttribute("pageTitle", "Create New Product");

    return "products/product_form";
  }

  @PostMapping("/products/save")
  public String saveProduct(Product product, RedirectAttributes redirectAttributes) {

    productService.save(product);

    redirectAttributes.addFlashAttribute("message", "The product has been saved successfully.");

    return "redirect:/products";
  }

  @GetMapping("/products/{id}/enabled/{status}")
  public String updateProductEnabledStatus(@PathVariable("id") Integer id,
  @PathVariable("status") boolean enabled,
  RedirectAttributes redirectAttributes){
    productService.updateProductEnabledStatus(id, enabled);
    String status = enabled ? "enabled" : "disabled";
    String message = "The product ID " + id + " has been " + status;

    redirectAttributes.addFlashAttribute("message", message);

    return "redirect:/products";
  }

  @GetMapping("/products/delete/{id}")
  public String deleteProduct(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
      try {
        productService.delete(id);
        redirectAttributes.addFlashAttribute(
                "message", "The product ID: " + id + " has been deleted successfully");
      } catch (ProductNotFoundException ex) {
        redirectAttributes.addFlashAttribute("message", ex.getMessage());
      }

    return "redirect:/products";
  }

}
