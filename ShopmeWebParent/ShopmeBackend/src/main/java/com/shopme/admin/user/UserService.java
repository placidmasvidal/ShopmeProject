package com.shopme.admin.user;

import com.shopme.common.entity.User;

import java.util.List;

public interface UserService {

    User createUser();

    User saveUser(User user);

    List<User> listAll();

    boolean isEmailUnique(Integer id, String email);

    User get(Integer id) throws UserNotFoundException;

    void delete(Integer id) throws UserNotFoundException;
}
