package com.shopme.admin.user;

import com.shopme.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {

  User createUser();

  User saveUser(User user);

  List<User> listAll();

  Page<User> listByPage(int pageNum, String sortField, String sortDir, String keyword);

  boolean isEmailUnique(Integer id, String email);

  User get(Integer id) throws UserNotFoundException;

  void delete(Integer id) throws UserNotFoundException;

  User getByEmail(String email);

  @Transactional
  void updateUserEnabledStatus(Integer id, boolean enabled);

  @Transactional
  User updateAccount(User user);
}
