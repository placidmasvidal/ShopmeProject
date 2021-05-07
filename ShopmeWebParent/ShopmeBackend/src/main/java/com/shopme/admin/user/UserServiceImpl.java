package com.shopme.admin.user;

import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {

  public static final int USERS_PER_PAGE = 4;

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
    boolean isUpdatingUser = (user.getId() != null);

    if(isUpdatingUser){
      User existingUser = userRepository.findById(user.getId()).get();

      if(user.getPassword().isEmpty()) {
        user.setPassword(existingUser.getPassword());
      } else {
        encodePassword(user);
      }
    } else {
      encodePassword(user);
    }

    return userRepository.save(user);
  }

  @Override
  public List<User> listAll() {
    return (List<User>) userRepository.findAll();
  }

  @Override
  public Page<User> listByPage(int pageNum) {
    Pageable pageable = PageRequest.of(pageNum - 1, USERS_PER_PAGE);
    return userRepository.findAll(pageable);
  }

  @Override
  public boolean isEmailUnique(Integer id, String email) {
    User userByEmail = userRepository.getUserByEmail(email);

    if(userByEmail == null) return true;

    boolean isCreatingNew = (id == null);

    if(isCreatingNew){
      if(userByEmail != null) return false;
    } else {
      if(userByEmail.getId() != id){
        return false;
      }
    }

    return true;
  }

  @Override
  public User get(Integer id) throws UserNotFoundException {
    try{
    return userRepository.findById(id).get();
    } catch(NoSuchElementException ex){
      throw new UserNotFoundException("Couldn't find any user with ID: " + id);
    }
  }

  @Override
  public void delete(Integer id) throws UserNotFoundException {
    Long countById = userRepository.countById(id);
    if(countById == null || countById == 0){
      throw new UserNotFoundException("Couldn't find any user with ID: " + id);
    }

    userRepository.deleteById(id);
  }

  @Override
  @Transactional
  public void updateUserEnabledStatus(Integer id, boolean enabled) {
    userRepository.updateEnabledStatus(id, enabled);
  }

  private void encodePassword(User user) {
    String encodedPassword = passwordEncoder.encode(user.getPassword());
    user.setPassword(encodedPassword);
  }
}
