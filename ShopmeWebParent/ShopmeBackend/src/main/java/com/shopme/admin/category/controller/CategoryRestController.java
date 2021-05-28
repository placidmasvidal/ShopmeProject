package com.shopme.admin.category.controller;

import com.shopme.admin.category.CategoryService;
import com.shopme.admin.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryRestController {

    private CategoryService categoryService;

    @Autowired
    public CategoryRestController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @PostMapping("/categories/check_alias")
    public String checkDuplicateAlias(@Param("id") Integer id, @Param("alias") String alias){
        return categoryService.isAliasUnique(id, alias) ? "OK" : "Duplicated";
    }
}
