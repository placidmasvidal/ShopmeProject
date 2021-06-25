package com.shopme.product.controller;

import com.shopme.category.CategoryService;
import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProductController {

    private CategoryService categoryService;

    @Autowired
    public ProductController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/c/{category_alias}")
    public String viewCategory(@PathVariable("category_alias") String alias,
                               Model model){
        Category category = categoryService.getCategory(alias);
        if(category == null){
            return "error/404";
        }

        model.addAttribute("", );
        return "products_by_category";
    }
}
