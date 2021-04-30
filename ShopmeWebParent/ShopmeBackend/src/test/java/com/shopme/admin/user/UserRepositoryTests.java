package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTests {

  @Autowired private UserRepository repo;

  @Autowired private TestEntityManager entityManager;

  @Test
  public void testCreateNewUserWithOneRole() {
    Role roleAdmin = entityManager.find(Role.class, 1);
    User userPlacidM = new User("placidmasvidal@gmail.com", "1234", "Placid", "Masvidal");
    userPlacidM.addRole(roleAdmin);

    User savedUser = repo.save(userPlacidM);
    assertThat(savedUser.getId()).isGreaterThan(0);
  }

  @Test
  public void testCreateNewUserWithTwoRoles() {
    User userMarta = new User("marta@gmail.com", "12345", "Marta", "Cab");
    Role roleEditor = new Role(3);
    Role roleAssistant = new Role(5);

    userMarta.addRole(roleEditor);
    userMarta.addRole(roleAssistant);

    User savedUser = repo.save(userMarta);
    assertThat(savedUser.getId()).isGreaterThan(0);
  }

  @Test
  public void testListAllUsers() {
    Iterable<User> listUsers = repo.findAll();
    listUsers.forEach(user -> System.out.println(user));
  }

  @Test
  public void testGetUserById() {
    User userPlacid = repo.findById(1).get();
    System.out.println(userPlacid);
    assertThat(userPlacid).isNotNull();
  }

  @Test
  public void testUpdateUserDetails(){
    User userPlacid = repo.findById(1).get();
    userPlacid.setEnabled(true);
    userPlacid.setEmail("contact@placidmasvidal.com");

    repo.save(userPlacid);
  }

  @Test
  public void testUpdateUserRoles(){
    User userMarta = repo.findById(2).get();
    Role roleEditor = new Role(3);
    Role roleSalesperson = new Role(2);
    userMarta.getRoles().remove(roleEditor);
    userMarta.addRole(roleSalesperson);

    repo.save(userMarta);
  }

  @Test
  public void testDeleteUser(){
    Integer userId = 2;
    repo.deleteById(userId);
  }

  @Test
  public void testGetUserByEmail(){
    String email = "contact@placidmasvidal.com";
    User user = repo.getUserByEmail(email);

    assertThat(user).isNotNull();
  }
}
