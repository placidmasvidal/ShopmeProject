package com.shopme.address;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;
import com.shopme.customer.CustomerRepository;
import com.shopme.setting.country.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService{

    private AddressRepository addressRepository;

    private CountryRepository countryRepository;

    private CustomerRepository customerRepository;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository, CountryRepository countryRepository, CustomerRepository customerRepository) {
        this.addressRepository = addressRepository;
        this.countryRepository = countryRepository;
        this.customerRepository = customerRepository;
    }


    @Override
    public List<Address> listAddressBook(Customer customer) {
        return addressRepository.findByCustomer(customer);
    }
}
