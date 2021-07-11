package com.shopme.admin.customer;

import com.shopme.common.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Integer> {

  @Query(
      "SELECT c FROM Customer c WHERE CONCAT(c.email, ' ', c.firstName, ' ', c.lastName, ' ', "
          + "c.addressLine1, ' ', c.addressLine2, ' ', c.city, ' ', c.state, "
          + "' ', c.postalCode, ' ', c.country.name) LIKE %?1%")
  Page<Customer> findAll(String keyword, Pageable pageable);

  @Query("UPDATE Customer c SET c.enabled = ?2 WHERE c.id = ?1")
  @Modifying
  void updateEnabledStatus(Integer id, boolean enabled);

  @Query("SELECT c FROM Customer c WHERE c.email = ?1")
  Customer findByEmail(String email);

  Long countById(Integer id);
}
