package com.shopme.customer;

import com.shopme.common.entity.Country;
import com.shopme.setting.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService{

    private CustomerRepository customerRepository;

    private CountryRepository countryRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, CountryRepository countryRepository) {
        this.customerRepository = customerRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    public List<Country> listAllCountries() {
        return countryRepository.findAllByOrderByNameAsc();
    }
}
