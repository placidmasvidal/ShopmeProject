package com.shopme.customer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTests {

  @Autowired private CustomerRepository sut;

  @Autowired private TestEntityManager entityManager;

  @Test
  public void testCreateCustomer1() {
    Integer countryId = 234;
    Country country = entityManager.find(Country.class, countryId);

    Customer customer = new Customer();
    customer.setCountry(country);
    customer.setFirstName("David");
    customer.setLastName("Fountaine");
    customer.setPassword("password123");
    customer.setEmail("david.s.fountaine@gmail.com");
    customer.setPhoneNumber("312-462-7518");
    customer.setAddressLine1("1927  West Drive");
    customer.setCity("Sacramento");
    customer.setState("California");
    customer.setPostalCode("95867");
    customer.setCreatedTime(new Date());

    Customer savedCustomer = sut.save(customer);

    assertThat(savedCustomer).isNotNull();
    assertThat(savedCustomer.getId()).isGreaterThan(0);
  }

  @Test
  public void testCreateCustomer2() {
    Integer countryId = 106;
    Country country = entityManager.find(Country.class, countryId);

    Customer customer = new Customer();
    customer.setCountry(country);
    customer.setFirstName("Sanya");
    customer.setLastName("Lad");
    customer.setPassword("password456");
    customer.setEmail("sanya.lad2020@gmail.com");
    customer.setPhoneNumber("02224928052");
    customer.setAddressLine1("173 , A-, Shah & Nahar Indl.estate, Sunmill Road");
    customer.setAddressLine2("Dhanraj Mill Compound, Lower Parel (west)");
    customer.setCity("Mumbai");
    customer.setState("Maharashtra");
    customer.setPostalCode("400013");
    customer.setCreatedTime(new Date());
    customer.setVerificationCode("code_123");

    Customer savedCustomer = sut.save(customer);

    assertThat(savedCustomer).isNotNull();
    assertThat(savedCustomer.getId()).isGreaterThan(0);
  }

  @Test
  public void testListCustomers() {
    Iterable<Customer> customers = sut.findAll();
    customers.forEach(System.out::println);

    assertThat(customers).hasSizeGreaterThan(1);
  }

  @Test
  public void testUpdateCustomer() {
    Integer customerId = 1;
    String lastName = "Stanfield";

    Customer customer = sut.findById(customerId).get();
    customer.setLastName(lastName);
    customer.setEnabled(true);

    Customer updatedCustomer = sut.save(customer);
    assertThat(updatedCustomer.getLastName()).isEqualTo(lastName);
  }

  @Test
  public void testGetCustomer() {
    Integer customerId = 2;
    Optional<Customer> findById = sut.findById(customerId);

    assertThat(findById).isPresent();

    Customer customer = findById.get();
    System.out.println(customer);
  }

  @Test
  public void testDeleteCustomer() {
    Integer customerId = 2;
    sut.deleteById(customerId);

    Optional<Customer> findById = sut.findById(customerId);
    assertThat(findById).isNotPresent();
  }

  @Test
  public void testFindByEmail() {
    String email = "david.s.fountaine@gmail.com";
    Customer customer = sut.findByEmail(email);

    assertThat(customer).isNotNull();
    System.out.println(customer);
  }

  @Test
  public void testFindByVerificationCode() {
    String code = "code_123";
    Customer customer = sut.findByVerificationCode(code);

    assertThat(customer).isNotNull();
    System.out.println(customer);
  }

  @Test
  public void testEnableCustomer() {
    Integer customerId = 1;
    sut.enable(customerId);

    Customer customer = sut.findById(customerId).get();
    assertThat(customer.isEnabled()).isTrue();
  }
}
