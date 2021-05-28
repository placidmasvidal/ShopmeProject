package com.shopme.admin.category.controller;

import com.shopme.admin.category.CategoryNotFoundException;
import com.shopme.admin.category.CategoryService;
import com.shopme.admin.category.CategoryServiceImpl;
import com.shopme.admin.category.export.CategoryCsvExporter;
import com.shopme.admin.user.RoleService;
import com.shopme.admin.user.UserNotFoundException;
import com.shopme.admin.user.UserService;
import com.shopme.admin.user.UserServiceImpl;
import com.shopme.admin.user.export.UserCsvExporter;
import com.shopme.admin.user.export.UserExcelExporter;
import com.shopme.admin.user.export.UserPdfExporter;
import com.shopme.admin.util.FileUploadUtil;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
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
      CategoryService categoryService,
      CategoryCsvExporter categoryCsvExporter) {
    this.categoryService = categoryService;
    this.categoryCsvExporter = categoryCsvExporter;
  }

  @GetMapping("/categories")
  public String listFirstPage(Model model) {
    List<Category> listCategories = categoryService.listAll();
    model.addAttribute("listCategories", listCategories);

    return "categories/categories";
//    return listByPage(1, model, "name", "asc", null);
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

/*
  @GetMapping("/categories/page/{pageNum}")
  public String listByPage(
      @PathVariable(name = "pageNum") int pageNum,
      Model model,
      @Param("sortField") String sortField,
      @Param("sortDir") String sortDir,
      @Param("keyword") String keyword) {
    LOG.info("Sort field: {}", sortField);
    LOG.info("Sort order: {}", sortDir);
    Page<Category> page = categoryService.listByPage(pageNum, sortField, sortDir, keyword);
    List<Category> listCategories = page.getContent();

    long startCount = (pageNum - 1) * UserServiceImpl.USERS_PER_PAGE + 1;
    long endCount = startCount + CategoryServiceImpl.CATEGORIES_PER_PAGE - 1;
    if (endCount > page.getTotalElements()) {
      endCount = page.getTotalElements();
    }

    String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

    model.addAttribute("totalPages", page.getTotalPages());
    model.addAttribute("currentPage", pageNum);
    model.addAttribute("startCount", startCount);
    model.addAttribute("endCount", endCount);
    model.addAttribute("totalItems", page.getTotalElements());
    model.addAttribute("listCategories", listCategories);
    model.addAttribute("sortField", sortField);
    model.addAttribute("sortDir", sortDir);
    model.addAttribute("reverseSortDir", reverseSortDir);
    model.addAttribute("keyword", keyword);

    return "categories/categories";
  }

  @GetMapping("/users/delete/{id}")
  public String deleteUser(
      @PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
    try {
      userService.delete(id);
      redirectAttributes.addFlashAttribute(
          "message", "The user ID: " + id + " has been deleted successfully");
    } catch (UserNotFoundException ex) {
      redirectAttributes.addFlashAttribute("message", ex.getMessage());
    }
    return "redirect:/users";
  }

  @GetMapping("/users/{id}/enabled/{status}")
  public String updateUserEnabledStatus(
      @PathVariable("id") Integer id,
      @PathVariable("status") boolean enabled,
      RedirectAttributes redirectAttributes) {
    userService.updateUserEnabledStatus(id, enabled);
    String status = enabled ? "enabled" : "disabled";
    String message = "The user ID " + id + " has been " + status;

    redirectAttributes.addFlashAttribute("message", message);

    return "redirect:/users";
  }

  @GetMapping("/users/export/csv")
  public void exportToCSV(HttpServletResponse response) throws IOException {
    List<User> listUsers = userService.listAll();

    userCsvExporter.export(listUsers, response);
  }

  private String getRedirectURLtoAffectedUser(User user) {
    String firstPartOfEmail = user.getEmail().split("@")[0];
    return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
  }

 */
}
