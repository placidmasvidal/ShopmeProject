package com.shopme.admin.order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductSearchController {

    @GetMapping("/orders/search_product")
    public String showSearchProductPage(){
        return "orders/search_product";
    }
}
