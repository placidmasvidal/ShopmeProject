package com.shopme.admin.brand.controller;

import com.shopme.admin.brand.BrandService;
import com.shopme.admin.brand.BrandServiceImpl;
import com.shopme.common.entity.Brand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BrandController {

    private Logger LOG = LoggerFactory.getLogger(BrandController.class);

    private BrandService brandService;

    @Autowired
    public BrandController(BrandService brandService){
        this.brandService = brandService;
    }

    @GetMapping("/brands")
    public String listAll(Model model){
        List<Brand> listBrands = brandService.listAll();

        model.addAttribute("listBrands", listBrands);
        return "/brands/brands";
    }

}
