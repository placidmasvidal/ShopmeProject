package com.shopme.customer;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.setting.country.CountryRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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
  public List<Country> listAllCountries() {
    return countryRepository.findAllByOrderByNameAsc();
  }

  @Override
  public boolean isEmailUnique(String email) {
    Customer customer = customerRepository.findByEmail(email);
    return customer == null;
  }

  @Override
  public void registerCustomer(Customer customer) {
    encodePassword(customer);
    customer.setEnabled(false);
    customer.setCreatedTime(new Date());

    String randomCode = RandomString.make(64);
    customer.setVerificationCode(randomCode);

    customerRepository.save(customer);
    System.out.println("Verification code: " + customer.getVerificationCode());
  }

  @Transactional
  @Override
  public boolean verify(String verificationCode) {
    Customer customer = customerRepository.findByVerificationCode(verificationCode);
    if(customer == null || customer.isEnabled()){
      return false;
    } else {
      customerRepository.enable(customer.getId());
      return true;
    }
  }

  private void encodePassword(Customer customer) {
    String encodedPassword = passwordEncoder.encode(customer.getPassword());
    customer.setPassword(encodedPassword);
  }
}
