package com.shopme.admin.user;

import com.shopme.admin.util.FileUploadUtil;
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

  @Autowired
  public UserController(UserService userService, RoleService roleService) {
    this.userService = userService;
    this.roleService = roleService;
  }

  @GetMapping("/users")
  public String listFirstPage(Model model) {
    return listByPage(1, model, "firstName", "asc", null);
  }

  @GetMapping("/users/page/{pageNum}")
  public String listByPage(
      @PathVariable(name = "pageNum") int pageNum,
      Model model,
      @Param("sortField") String sortField,
      @Param("sortDir") String sortDir,
      @Param("keyword") String keyword) {
    LOG.info("Sort field: {}", sortField);
    LOG.info("Sort order: {}", sortDir);
    Page<User> page = userService.listByPage(pageNum, sortField, sortDir, keyword);
    List<User> listUsers = page.getContent();

    long startCount = (pageNum - 1) * UserServiceImpl.USERS_PER_PAGE + 1;
    long endCount = startCount + UserServiceImpl.USERS_PER_PAGE - 1;
    if (endCount > page.getTotalElements()) {
      endCount = page.getTotalElements();
    }

    String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

    model.addAttribute("totalPages", page.getTotalPages());
    model.addAttribute("currentPage", pageNum);
    model.addAttribute("startCount", startCount);
    model.addAttribute("endCount", endCount);
    model.addAttribute("totalItems", page.getTotalElements());
    model.addAttribute("listUsers", listUsers);
    model.addAttribute("sortField", sortField);
    model.addAttribute("sortDir", sortDir);
    model.addAttribute("reverseSortDir", reverseSortDir);
    model.addAttribute("keyword", keyword);

    return "users";
  }

  @GetMapping("/users/new")
  public String newUser(Model model) {
    List<Role> listRoles = roleService.listRoles();
    User user = userService.createUser();
    model.addAttribute("user", user);
    model.addAttribute("listRoles", listRoles);
    model.addAttribute("pageTitle", "Create New User");

    return "user_form";
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
      return "user_form";
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

/*    String fileName = "users_";
    String[] csvHeader = {"User ID", "E-mail", "First Name", "Last Name", "Roles", "Enabled"};
    String[] fieldMapping = {"id", "email", "firstName", "lastName", "roles", "enabled"};
    csvExporter.export(fileName, csvHeader, fieldMapping, listUsers, response);

 */
    UserCsvExporter userCsvExporter = new UserCsvExporter();
    userCsvExporter.export(listUsers, response);
  }

  @GetMapping("/users/export/excel")
  public void exportToExcel(HttpServletResponse response) throws IOException {
    List<User> listUsers = userService.listAll();

    UserExcelExporter userExcelExporter = new UserExcelExporter();
    userExcelExporter.export(listUsers, response);
  }

  private String getRedirectURLtoAffectedUser(User user) {
    String firstPartOfEmail = user.getEmail().split("@")[0];
    return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
  }

}
