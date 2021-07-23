package com.shopme.admin.shippingrate;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.setting.country.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShippingRateServiceImpl implements ShippingRateService{

    private ShippingRateRepository shippingRateRepository;

    private CountryRepository countryRepository;

    @Autowired
    public ShippingRateServiceImpl(ShippingRateRepository shippingRateRepository, CountryRepository countryRepository) {
        this.shippingRateRepository = shippingRateRepository;
        this.countryRepository = countryRepository;
    }


    @Override
    public void listByPage(int pageNum, PagingAndSortingHelper pagingAndSortingHelper) {
        pagingAndSortingHelper.listEntities(pageNum, ShippingRateConstants.RATES_PER_PAGE, shippingRateRepository);
    }


}
