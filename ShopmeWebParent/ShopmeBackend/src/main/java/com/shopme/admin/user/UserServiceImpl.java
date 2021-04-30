package com.shopme.admin.user;

import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

  private UserRepository userRepository;

  private PasswordEncoder passwordEncoder;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public User createUser() {
    User user = new User();
    user.setEnabled(true);
    return user;
  }

  @Override
  public User saveUser(User user) {
    encodePassword(user);
    return userRepository.save(user);
  }

  @Override
  public List<User> listAll() {
    return (List<User>) userRepository.findAll();
  }

  @Override
  public boolean isEmailUnique(String email) {
    User userByEmail = userRepository.getUserByEmail(email);
    return userByEmail == null;
  }

  private void encodePassword(User user) {
    String encodedPassword = passwordEncoder.encode(user.getPassword());
    user.setPassword(encodedPassword);
  }
}
