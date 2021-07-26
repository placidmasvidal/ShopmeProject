package com.shopme.shipping;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;
import com.shopme.setting.country.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShippingRateServiceImpl implements ShippingRateService{

    private ShippingRateRepository shippingRateRepository;

    @Autowired
    public ShippingRateServiceImpl(ShippingRateRepository shippingRateRepository) {
        this.shippingRateRepository = shippingRateRepository;
    }


    @Override
    public ShippingRate getShippingRateForCustomer(Customer customer) {
        String state = customer.getState();
        if(state == null || state.isEmpty()){
            state = customer.getCity();
        }

    return shippingRateRepository.findByCountryAndState(customer.getCountry(), state);
    }

    @Override
    public ShippingRate getShippingRateForAddress(Address address) {
        String state = address.getState();
        if(state == null || state.isEmpty()){
            state = address.getCity();
        }

    return shippingRateRepository.findByCountryAndState(address.getCountry(), state);
    }


}
