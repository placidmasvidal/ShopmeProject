package com.shopme.admin.shippingrate;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;

import java.util.List;

public interface ShippingRateService {

    void listByPage(int pageNum, PagingAndSortingHelper pagingAndSortingHelper);

    List<Country> listAllCountries();

    void save(ShippingRate rateInForm) throws ShippingRateAlreadyExistsException;
}
