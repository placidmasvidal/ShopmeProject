package com.shopme.customer;

import com.shopme.common.entity.Country;

import java.util.List;

public interface CustomerService {

    List<Country> listAllCountries();

    boolean isEmailUnique(String email);
}
