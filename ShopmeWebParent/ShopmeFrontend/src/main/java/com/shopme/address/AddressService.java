package com.shopme.address;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;

import java.util.List;

public interface AddressService {

    List<Address> listAddressBook(Customer customer);

    void save(Address address);

    Address get(Integer addressId, Integer customerId);

    void delete(Integer addressId, Integer customerId);
}
