package com.shopme.address;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService{

    private AddressRepository addressRepository;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public List<Address> listAddressBook(Customer customer) {
        return addressRepository.findByCustomer(customer);
    }

    @Override
    public void save(Address address) {
        addressRepository.save(address);
    }

    @Override
    public Address get(Integer addressId, Integer customerId) {
        return addressRepository.findByIdAndCustomer(addressId, customerId);
    }

    @Override
    @Transactional
    public void delete(Integer addressId, Integer customerId) {
        addressRepository.deleteByIdAndCustomer(addressId, customerId);
    }

    @Override
    @Transactional
    public void setDefaultAddress(Integer defaultAddressId, Integer customerId) {
        if(defaultAddressId > 0){
            addressRepository.setDefaultAddress(defaultAddressId);
        }
        addressRepository.setNonDefaultForOthers(defaultAddressId, customerId);
    }

    @Override
    public Address getDefaultAddress(Customer customer) {
        return addressRepository.findDefaultByCustomer(customer.getId());
    }
}
