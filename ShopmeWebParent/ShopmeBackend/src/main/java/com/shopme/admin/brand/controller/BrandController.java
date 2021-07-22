package com.shopme.admin.brand.controller;

import com.shopme.admin.brand.BrandConstants;
import com.shopme.admin.brand.BrandNotFoundException;
import com.shopme.admin.brand.BrandService;
import com.shopme.admin.brand.BrandServiceImpl;
import com.shopme.admin.category.CategoryService;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.util.FileUploadUtil;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class BrandController {

  private Logger LOG = LoggerFactory.getLogger(BrandController.class);

  private BrandService brandService;

  private CategoryService categoryService;

  @Autowired
  public BrandController(BrandService brandService, CategoryService categoryService) {
    this.brandService = brandService;
    this.categoryService = categoryService;
  }

  @GetMapping("/brands")
  public String listFirstPage() {
    return "redirect:/brands/page/1?sortField=name&sortDir=asc";
  }

  @GetMapping("/brands/page/{pageNum}")
  public String listByPage(
          @PagingAndSortingParam(listName = "listBrands", moduleURL = "/brands")
                  PagingAndSortingHelper pagingAndSortingHelper,
          @PathVariable(name = "pageNum") int pageNum
  ) {
    brandService.listByPage(pageNum, pagingAndSortingHelper);

    return "brands/brands";
  }

  @GetMapping("/brands/new")
  public String newBrand(Model model) {
    List<Category> listCategories = categoryService.listCategoriesUsedInForm();

    model.addAttribute("listCategories", listCategories);
    model.addAttribute("brand", new Brand());
    model.addAttribute("pageTitle", "Create New Brand");

    return "brands/brand_form";
  }

  @GetMapping("/brands/edit/{id}")
  public String editBrand(
      @PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
    try {
      Brand brand = brandService.get(id);
      List<Category> listCategories = categoryService.listCategoriesUsedInForm();

      model.addAttribute("listCategories", listCategories);
      model.addAttribute("brand", brand);
      model.addAttribute("pageTitle", "Edit Brand (ID: " + id + ")");
      return "brands/brand_form";
    } catch (BrandNotFoundException ex) {
      redirectAttributes.addFlashAttribute("message", ex.getMessage());
      return "redirect:/brands";
    }
  }

  @PostMapping("/brands/save")
  public String saveBrand(
      Brand brand,
      RedirectAttributes redirectAttributes,
      @RequestParam("fileImage") MultipartFile multipartFile)
      throws IOException {

    if (!multipartFile.isEmpty()) {
      String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
      brand.setLogo(fileName);
      Brand savedBrand = brandService.save(brand);

      String uploadDir = "../brand-logos/" + savedBrand.getId();

      FileUploadUtil.cleanDir(uploadDir);
      FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
    } else {
      brandService.save(brand);
    }

    redirectAttributes.addFlashAttribute("message", "The brand has been saved succesfully.");
    return "redirect:/brands";
  }

  @GetMapping("/brands/delete/{id}")
  public String deleteBrand(
      @PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
    try {
      brandService.delete(id);
      String brandDir = "../brand-logos/" + id;
      FileUploadUtil.removeDir(brandDir);

      redirectAttributes.addFlashAttribute(
          "message", "The brand ID " + id + " has been deleted successfully");
    } catch (BrandNotFoundException ex) {
      redirectAttributes.addFlashAttribute("message", ex.getMessage());
    }

    return "redirect:/brands";
  }
}
