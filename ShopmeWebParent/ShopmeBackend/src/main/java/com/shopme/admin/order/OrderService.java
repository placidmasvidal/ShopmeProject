package com.shopme.admin.order;

import com.shopme.admin.paging.PagingAndSortingHelper;

public interface OrderService {

    void listByPage(int pageNum, PagingAndSortingHelper pagingAndSortingHelper);
}
