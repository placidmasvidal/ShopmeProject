package com.shopme.admin.user.controller;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.user.*;
import com.shopme.admin.user.export.UserCsvExporter;
import com.shopme.admin.user.export.UserExcelExporter;
import com.shopme.admin.user.export.UserPdfExporter;
import com.shopme.admin.util.FileUploadUtil;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class UserController {

  private Logger LOG = LoggerFactory.getLogger(UserController.class);

  private UserService userService;

  private RoleService roleService;

  private UserCsvExporter userCsvExporter;

  private UserExcelExporter userExcelExporter;

  private UserPdfExporter userPdfExporter;

  @Autowired
  public UserController(
      UserService userService,
      RoleService roleService,
      UserCsvExporter userCsvExporter,
      UserExcelExporter userExcelExporter,
      UserPdfExporter userPdfExporter) {
    this.userService = userService;
    this.roleService = roleService;
    this.userCsvExporter = userCsvExporter;
    this.userExcelExporter = userExcelExporter;
    this.userPdfExporter = userPdfExporter;
  }

  @GetMapping("/users")
  public String listFirstPage() {
    return "redirect:/users/page/1?sortField=firstName&sortDir=asc";
  }

  @GetMapping("/users/page/{pageNum}")
  public String listByPage(
      @PagingAndSortingParam(listName = "listUsers", moduleURL = "/users")
          PagingAndSortingHelper pagingAndSortingHelper,
      @PathVariable(name = "pageNum") int pageNum) {
    userService.listByPage(pageNum, pagingAndSortingHelper);

    return "users/users";
  }

  @GetMapping("/users/new")
  public String newUser(Model model) {
    List<Role> listRoles = roleService.listRoles();
    User user = userService.createUser();
    model.addAttribute("user", user);
    model.addAttribute("listRoles", listRoles);
    model.addAttribute("pageTitle", "Create New User");

    return "users/user_form";
  }

  @PostMapping("/users/save")
  public String saveUser(
      User user,
      RedirectAttributes redirectAttributes,
      @RequestParam("image") MultipartFile multipartFile)
      throws IOException {

    if (!multipartFile.isEmpty()) {
      String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
      user.setPhotos(fileName);
      User savedUser = userService.saveUser(user);

      String uploadDir = "user-photos/" + savedUser.getId();

      FileUploadUtil.cleanDir(uploadDir);
      FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
    } else {
      if (user.getPhotos().isEmpty()) user.setPhotos(null);
      userService.saveUser(user);
    }

    redirectAttributes.addFlashAttribute("message", "The user has been saved successfully");
    return getRedirectURLtoAffectedUser(user);
  }

  @GetMapping("/users/edit/{id}")
  public String editUser(
      @PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
    try {
      User user = userService.get(id);
      List<Role> listRoles = roleService.listRoles();
      model.addAttribute("user", user);
      model.addAttribute("listRoles", listRoles);
      model.addAttribute("pageTitle", "Edit User (ID: " + id + ")");
      return "users/user_form";
    } catch (UserNotFoundException ex) {
      redirectAttributes.addFlashAttribute("message", ex.getMessage());
      return "redirect:/users";
    }
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

  @GetMapping("/users/export/excel")
  public void exportToExcel(HttpServletResponse response) throws IOException {
    List<User> listUsers = userService.listAll();

    userExcelExporter.export(listUsers, response);
  }

  @GetMapping("/users/export/pdf")
  public void exportToPDF(HttpServletResponse response) throws IOException {
    List<User> listUsers = userService.listAll();

    userPdfExporter.export(listUsers, response);
  }

  private String getRedirectURLtoAffectedUser(User user) {
    String firstPartOfEmail = user.getEmail().split("@")[0];
    return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
  }
}
