package com.shopme.admin.brand.controller;

import com.shopme.admin.brand.BrandNotFoundException;
import com.shopme.admin.brand.BrandService;
import com.shopme.admin.brand.BrandServiceImpl;
import com.shopme.admin.category.CategoryNotFoundException;
import com.shopme.admin.category.CategoryService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class BrandController {

    private Logger LOG = LoggerFactory.getLogger(BrandController.class);

    private BrandService brandService;

    private CategoryService categoryService;

    @Autowired
    public BrandController(BrandService brandService, CategoryService categoryService){
        this.brandService = brandService;
        this.categoryService = categoryService;
    }

    @GetMapping("/brands")
    public String listAll(Model model){
        List<Brand> listBrands = brandService.listAll();

        model.addAttribute("listBrands", listBrands);
        return "/brands/brands";
    }


    @GetMapping("/brands/new")
    public String newBrand(Model model){
        List<Category> listCategories = categoryService.listCategoriesUsedInForm();

        model.addAttribute("listCategories", listCategories);
        model.addAttribute("brand", new Brand());
        model.addAttribute("pageTitle", "Create New Brand");

        return "brands/brand_form";
    }

    @GetMapping("/brands/edit/{id}")
    public String editBrand(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes){
        try {
            Brand brand = brandService.get(id);
            model.addAttribute("brand", brand);
            model.addAttribute("pageTitle", "Edit Brand (ID: " + id + ")");
            return "brands/brand_form";
        } catch (BrandNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/categories";
        }
    }

}
