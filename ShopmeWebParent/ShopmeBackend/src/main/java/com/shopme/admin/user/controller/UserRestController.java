package com.shopme.admin.user.controller;

import com.shopme.admin.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {

    private UserService userService;

    @Autowired
    public UserRestController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/users/check_email")
    public String checkDuplicateEmail(@RequestParam("id") Integer id, @RequestParam("email") String email){
        return userService.isEmailUnique(id, email) ? "OK" : "Duplicated";
    }
}
