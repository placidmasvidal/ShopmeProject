package com.shopme.admin.customer;

import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
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

@Service
public class CustomerServiceImpl implements CustomerService {

  private CustomerRepository customerRepository;

  private CountryRepository countryRepository;

  private PasswordEncoder passwordEncoder;

  @Autowired
  public CustomerServiceImpl(
      CustomerRepository customerRepository,
      CountryRepository countryRepository,
      PasswordEncoder passwordEncoder) {
    this.customerRepository = customerRepository;
    this.countryRepository = countryRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Page<Customer> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
    Sort sort = Sort.by(sortField);
    sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

    Pageable pageable = PageRequest.of(pageNum - 1, CustomerConstants.CUSTOMERS_PER_PAGE, sort);

    if (keyword != null) {
      return customerRepository.findAll(keyword, pageable);
    }

    return customerRepository.findAll(pageable);
  }

  @Override
  @Transactional
  public void updateCustomerEnabledStatus(Integer id, boolean enabled) {
    customerRepository.updateEnabledStatus(id, enabled);
  }

  @Override
  public Customer get(Integer id) throws CustomerNotFoundException {
    try {
      return customerRepository.findById(id).get();
    } catch (NoSuchElementException ex) {
      throw new CustomerNotFoundException("Could not find any customers with ID " + id);
    }
  }

  @Override
  public List<Country> listAllCountries() {
    return countryRepository.findAllByOrderByNameAsc();
  }

  @Override
  public boolean isEmailUnique(Integer id, String email) {
    Customer existCustomer = customerRepository.findByEmail(email);

    if (existCustomer != null && existCustomer.getId() != id) {
      // found another customer having the same email
      return false;
    }

    return true;
  }

  @Override
  public void save(Customer customerInForm) {
    if (!customerInForm.getPassword().isEmpty()) {
      String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
      customerInForm.setPassword(encodedPassword);
    } else {
      Customer customerInDB = customerRepository.findById(customerInForm.getId()).get();
      customerInForm.setPassword(customerInDB.getPassword());
    }
    customerRepository.save(customerInForm);
  }

  @Override
  public void delete(Integer id) throws CustomerNotFoundException {
    Long count = customerRepository.countById(id);
    if (count == null || count == 0) {
      throw new CustomerNotFoundException("Could not find any customers with ID " + id);
    }

    customerRepository.deleteById(id);
  }
}
