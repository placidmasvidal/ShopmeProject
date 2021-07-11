package com.shopme.admin.customer;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerService {

    Page<Customer> listByPage(int pageNum, String sortField, String sortDir, String keyword);

    void updateCustomerEnabledStatus(Integer id, boolean enabled);

    Customer get(Integer id) throws CustomerNotFoundException;

    List<Country> listAllCountries();

    boolean isEmailUnique(Integer id, String email);

    void save(Customer customerInForm);

    void delete(Integer id) throws CustomerNotFoundException;


}
