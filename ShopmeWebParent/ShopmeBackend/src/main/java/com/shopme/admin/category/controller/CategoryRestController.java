package com.shopme.admin.category.controller;

import com.shopme.admin.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryRestController {

    private CategoryService categoryService;

    @Autowired
    public CategoryRestController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @PostMapping("/categories/check_unique")
    public String checkUnique(@RequestParam("id") Integer id, @RequestParam("name") String name, @RequestParam("alias") String alias){
        return categoryService.checkUnique(id, name, alias);
    }

}
