package com.shopme.admin.user;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
    boolean isUpdatingUser = (user.getId() != null);

    if (isUpdatingUser) {
      User existingUser = userRepository.findById(user.getId()).get();

      if (user.getPassword().isEmpty()) {
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
    return (List<User>) userRepository.findAll(Sort.by("firstName").ascending());
  }

  @Override
  public void listByPage(int pageNum, PagingAndSortingHelper pagingAndSortingHelper) {
    pagingAndSortingHelper.listEntities(pageNum, UserConstants.USERS_PER_PAGE, userRepository);
  }

  @Override
  public boolean isEmailUnique(Integer id, String email) {
    User userByEmail = userRepository.getUserByEmail(email);

    if (userByEmail == null) return true;

    boolean isCreatingNew = (id == null);

    if (isCreatingNew) {
      if (userByEmail != null) return false;
    } else {
      if (userByEmail.getId() != id) {
        return false;
      }
    }

    return true;
  }

  @Override
  public User get(Integer id) throws UserNotFoundException {
    try {
      return userRepository.findById(id).get();
    } catch (NoSuchElementException ex) {
      throw new UserNotFoundException("Couldn't find any user with ID: " + id);
    }
  }

  @Override
  public void delete(Integer id) throws UserNotFoundException {
    Long countById = userRepository.countById(id);
    if (countById == null || countById == 0) {
      throw new UserNotFoundException("Couldn't find any user with ID: " + id);
    }

    userRepository.deleteById(id);
  }

  @Override
  public User getByEmail(String email) {
    return userRepository.getUserByEmail(email);
  }

  @Override
  @Transactional
  public void updateUserEnabledStatus(Integer id, boolean enabled) {
    userRepository.updateEnabledStatus(id, enabled);
  }

  @Override
  public User updateAccount(User userInForm) {
    User userInDB = userRepository.findById(userInForm.getId()).get();
    if (!userInForm.getPassword().isEmpty()) {
      userInDB.setPassword(userInForm.getPassword());
      encodePassword(userInDB);
    }

    if (userInForm.getPhotos() != null) {
      userInDB.setPhotos(userInForm.getPhotos());
    }

    userInDB.setFirstName(userInForm.getFirstName());
    userInDB.setLastName(userInForm.getLastName());

    return userRepository.save(userInDB);
  }

  private void encodePassword(User user) {
    String encodedPassword = passwordEncoder.encode(user.getPassword());
    user.setPassword(encodedPassword);
  }
}
