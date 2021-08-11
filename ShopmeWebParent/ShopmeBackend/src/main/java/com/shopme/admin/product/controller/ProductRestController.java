package com.shopme.admin.product.controller;

import com.shopme.admin.product.ProductService;
import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {

  private ProductService productService;

  @Autowired
  public ProductRestController(ProductService productService) {
    this.productService = productService;
  }

  @PostMapping("/products/check_unique")
  public String checkUnique(Integer id, String name) {
    return productService.checkUnique(id, name);
  }

  @GetMapping("/products/get/{id}")
  public ProductDTO getProductInfo(@PathVariable("id") Integer id) throws ProductNotFoundException {
    Product product = productService.get(id);
    return new ProductDTO(
        product.getName(),
        product.getMainImagePath(),
        product.getDiscountPrice(),
        product.getCost());
  }
}
