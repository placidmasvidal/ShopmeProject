package com.shopme.admin.brand.controller;

import com.shopme.admin.brand.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BrandRestController {

    private BrandService brandService;

    @Autowired
    public BrandRestController(BrandService brandService){
        this.brandService = brandService;
    }

    @PostMapping("/brands/check_unique")
    public String checkUnique(@Param("id") Integer id, @Param("name") String name){
        return brandService.checkUnique(id, name);
    }
}
