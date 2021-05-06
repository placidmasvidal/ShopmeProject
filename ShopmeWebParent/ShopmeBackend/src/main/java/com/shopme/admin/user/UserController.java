package com.shopme.admin.user;

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
  public String listAll(Model model) {
    List<User> listUsers = userService.listAll();
    model.addAttribute("listUsers", listUsers);
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
  public String saveUser(User user, RedirectAttributes redirectAttributes, @RequestParam("image") MultipartFile multipartFile) throws IOException {

    if(!multipartFile.isEmpty()){
      String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
      user.setPhotos(fileName);
      User savedUser = userService.saveUser(user);

      String uploadDir = "ShopmeWebParent/ShopmeBackend/user-photos/" + savedUser.getId();

      FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
    }

    redirectAttributes.addFlashAttribute("message", "The user has been saved successfully");
    return "redirect:/users";
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
          @PathVariable("id") Integer id, @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
    userService.updateUserEnabledStatus(id, enabled);
    String status = enabled ? "enabled" : "disabled";
    String message = "The user ID " + id + " has been " + status;

    redirectAttributes.addFlashAttribute("message", message);

    return "redirect:/users";
  }
}
