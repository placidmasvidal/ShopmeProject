package com.shopme.admin.category.controller;

import com.shopme.admin.category.*;
import com.shopme.admin.category.export.CategoryCsvExporter;
import com.shopme.admin.util.FileUploadUtil;
import com.shopme.common.entity.Category;
import com.shopme.common.exception.CategoryNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class CategoryController {

  private Logger LOG = LoggerFactory.getLogger(CategoryController.class);

  private CategoryService categoryService;

  private CategoryCsvExporter categoryCsvExporter;

  @Autowired
  public CategoryController(
      CategoryService categoryService, CategoryCsvExporter categoryCsvExporter) {
    this.categoryService = categoryService;
    this.categoryCsvExporter = categoryCsvExporter;
  }

  @GetMapping("/categories")
  public String listFirstPage(@Param("sortDir") String sortDir, Model model) {
    return listByPage(1, sortDir, null, model);
  }

  @GetMapping("/categories/page/{pageNum}")
  public String listByPage(
      @PathVariable(name = "pageNum") int pageNum,
      @Param("sortDir") String sortDir,
      @Param("keyword") String keyword,
      Model model) {
    if (sortDir == null || sortDir.isEmpty()) {
      sortDir = "asc";
    }

    CategoryPageInfo pageInfo = new CategoryPageInfo();
    List<Category> listCategories = categoryService.listByPage(pageInfo, pageNum, sortDir, keyword);

    long startCount = (pageNum - 1) * CategoryConstants.ROOT_CATEGORIES_PER_PAGE + 1;
    long endCount = startCount + CategoryConstants.ROOT_CATEGORIES_PER_PAGE - 1;
    if (endCount > pageInfo.getTotalElements()) {
      endCount = pageInfo.getTotalElements();
    }

    String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

    model.addAttribute("totalPages", pageInfo.getTotalPages());
    model.addAttribute("totalItems", pageInfo.getTotalElements());
    model.addAttribute("currentPage", pageNum);
    model.addAttribute("sortField", "name");
    model.addAttribute("sortDir", sortDir);
    model.addAttribute("keyword", keyword);
    model.addAttribute("startCount", startCount);
    model.addAttribute("endCount", endCount);

    model.addAttribute("listCategories", listCategories);
    model.addAttribute("reverseSortDir", reverseSortDir);
    model.addAttribute("moduleURL", "/categories");

    return "categories/categories";
}

  @GetMapping("/categories/new")
  public String newCategory(Model model) {
    List<Category> listCategories = categoryService.listCategoriesUsedInForm();
    model.addAttribute("category", new Category());
    model.addAttribute("pageTitle", "Create New Category");
    model.addAttribute("listCategories", listCategories);

    return "categories/category_form";
  }

  @PostMapping("/categories/save")
  public String saveCategory(
      Category category,
      RedirectAttributes redirectAttributes,
      @RequestParam("fileImage") MultipartFile multipartFile)
      throws IOException {

    if (!multipartFile.isEmpty()) {
      String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
      category.setImage(fileName);
      Category savedCategory = categoryService.saveCategory(category);

      String uploadDir = "../category-images/" + savedCategory.getId();

      FileUploadUtil.cleanDir(uploadDir);
      FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
    } else {
      categoryService.saveCategory(category);
    }

    redirectAttributes.addFlashAttribute("message", "The category has been saved succesfully.");
    return "redirect:/categories";
  }

  @GetMapping("/categories/edit/{id}")
  public String editCategory(
      @PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
    try {
      Category category = categoryService.get(id);
      model.addAttribute("category", category);
      model.addAttribute("pageTitle", "Edit Category (ID: " + id + ")");
      model.addAttribute("listCategories", categoryService.listCategoriesUsedInForm());
      return "categories/category_form";
    } catch (CategoryNotFoundException ex) {
      redirectAttributes.addFlashAttribute("message", ex.getMessage());
      return "redirect:/categories";
    }
  }

  @GetMapping("/categories/{id}/enabled/{status}")
  public String updateUserEnabledStatus(
      @PathVariable("id") Integer id,
      @PathVariable("status") boolean enabled,
      RedirectAttributes redirectAttributes) {
    categoryService.updateCategoryEnabledStatus(id, enabled);
    String status = enabled ? "enabled" : "disabled";
    String message = "The category ID " + id + " has been " + status;

    redirectAttributes.addFlashAttribute("message", message);

    return "redirect:/categories";
  }

  @GetMapping("/categories/delete/{id}")
  public String deleteCategory(
      @PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
    try {
      categoryService.delete(id);
      String categoryDir = "../category-images/" + id;
      FileUploadUtil.removeDir(categoryDir);
      redirectAttributes.addFlashAttribute(
          "message", "The category ID: " + id + " has been deleted successfully");
    } catch (CategoryNotFoundException ex) {
      redirectAttributes.addFlashAttribute("message", ex.getMessage());
    }
    return "redirect:/categories";
  }

  @GetMapping("/categories/export/csv")
  public void exportToCSV(HttpServletResponse response) throws IOException{
    List<Category> listCategories = categoryService.listCategoriesUsedInForm();
    categoryCsvExporter.export(listCategories, response);
  }
}
