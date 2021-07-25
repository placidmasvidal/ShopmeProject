package com.shopme.order;

import com.shopme.common.entity.Order;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderRepository extends PagingAndSortingRepository<Order, Integer> {
}
