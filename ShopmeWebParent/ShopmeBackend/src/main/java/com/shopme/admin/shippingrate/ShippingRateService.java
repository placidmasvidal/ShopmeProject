package com.shopme.admin.shippingrate;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;

import java.util.List;

public interface ShippingRateService {

    void listByPage(int pageNum, PagingAndSortingHelper pagingAndSortingHelper);

    List<Country> listAllCountries();

    void save(ShippingRate rateInForm) throws ShippingRateAlreadyExistsException;

    ShippingRate get(Integer id) throws ShippingRateNotFoundException;

    void updateCODSupport(Integer id, boolean codSupported) throws ShippingRateNotFoundException;

    void delete(Integer id) throws ShippingRateNotFoundException;

    float calculateShippingCost(Integer productId, Integer countryId, String state) throws ShippingRateNotFoundException;
}
