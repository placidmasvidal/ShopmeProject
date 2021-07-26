package com.shopme.shipping;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;

public interface ShippingRateService {

    ShippingRate getShippingRateForCustomer(Customer customer);

    ShippingRate getShippingRateForAddress(Address address);
}
