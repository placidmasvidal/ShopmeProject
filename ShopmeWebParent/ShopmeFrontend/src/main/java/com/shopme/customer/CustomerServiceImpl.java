package com.shopme.customer;

import com.shopme.common.entity.AuthenticationType;
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

  @Override
  @Transactional
  public void updateAuthenticationType(Customer customer, AuthenticationType type) {
    if(!customer.getAuthenticationType().equals(type)){
      customerRepository.updateAuthenticationType(customer.getId(), type);
    }
  }

  @Override
  public Customer getCustomerByEmail(String email) {
    return customerRepository.findByEmail(email);
  }

  @Override
  public void addNewCustomerUponOAuthLogin(String name, String email, String countryCode) {
    Customer customer = new Customer();
    customer.setEmail(email);

    setName(name, customer);

    customer.setEnabled(true);
    customer.setCreatedTime(new Date());
    customer.setAuthenticationType(AuthenticationType.GOOGLE);
    customer.setPassword("");
    customer.setAddressLine1("");
    customer.setCity("");
    customer.setState("");
    customer.setPhoneNumber("");
    customer.setPostalCode("");
    customer.setCountry(countryRepository.findByCode(countryCode));

    customerRepository.save(customer);
  }

  private void encodePassword(Customer customer) {
    String encodedPassword = passwordEncoder.encode(customer.getPassword());
    customer.setPassword(encodedPassword);
  }

  private void setName(String name, Customer customer){
    String[] nameArray = name.split(" ");
    if(nameArray.length < 2){
      customer.setFirstName(name);
      customer.setLastName("");
    } else {
      String firstName = nameArray[0];
      customer.setFirstName(firstName);

      String lastName = name.replaceFirst(firstName, "");
      customer.setLastName(lastName);
    }
  }
}
