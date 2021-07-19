package com.shopme.customer;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;

import java.util.List;

public interface CustomerService {

  List<Country> listAllCountries();

  boolean isEmailUnique(String email);

  void registerCustomer(Customer customer);

  boolean verify(String verificationCode);

  void updateAuthenticationType(Customer customer, AuthenticationType type);

  Customer getCustomerByEmail(String email);

  void addNewCustomerUponOAuthLogin(
      String name, String email, String countryCode, AuthenticationType authenticationType);

  void update(Customer customerInForm);

  String updateResetPasswordToken(String email) throws CustomerNotFoundException;
}
