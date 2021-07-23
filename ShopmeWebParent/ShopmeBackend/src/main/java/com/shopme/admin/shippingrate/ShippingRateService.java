package com.shopme.admin.shippingrate;

import com.shopme.admin.paging.PagingAndSortingHelper;

public interface ShippingRateService {

    void listByPage(int pageNum, PagingAndSortingHelper pagingAndSortingHelper);


}
