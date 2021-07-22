package com.shopme.admin.product.controller;

import com.shopme.admin.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {

    private ProductService productService;

    @Autowired
    public ProductRestController(ProductService productService){
        this.productService = productService;
    }

    @PostMapping("/products/check_unique")
    public String checkUnique(@RequestParam("id") Integer id, @RequestParam("name") String name){
        return productService.checkUnique(id, name);
    }

}
