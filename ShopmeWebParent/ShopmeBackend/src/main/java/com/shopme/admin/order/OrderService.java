package com.shopme.admin.order;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Order;
import com.shopme.common.exception.OrderNotFoundException;

public interface OrderService {

    void listByPage(int pageNum, PagingAndSortingHelper pagingAndSortingHelper);

    Order get(Integer id) throws OrderNotFoundException;

    void delete(Integer id) throws OrderNotFoundException;
}
