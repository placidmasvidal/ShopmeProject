package com.shopme.address;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;

import java.util.List;

public interface AddressService {

    List<Address> listAddressBook(Customer customer);
}
