package com.shopme.admin.order;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static com.shopme.admin.order.OrderConstants.ORDERS_PER_PAGE;

@Service
public class OrderServiceImpl implements OrderService{

    private OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void listByPage(int pageNum, PagingAndSortingHelper pagingAndSortingHelper){
        String sortField = pagingAndSortingHelper.getSortField();
        String sortDir = pagingAndSortingHelper.getSortDir();
        String keyword = pagingAndSortingHelper.getKeyword();

        Sort sort = null;

        if ("destination".equals(sortField)) {
            sort = Sort.by("country").and(Sort.by("state")).and(Sort.by("city"));
        } else {
            sort = Sort.by(sortField);
        }

        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, ORDERS_PER_PAGE, sort);

        Page<Order> page = null;

        if (keyword != null) {
            page = orderRepository.findAll(keyword, pageable);
        } else {
            page = orderRepository.findAll(pageable);
        }

        pagingAndSortingHelper.updateModelAttributes(pageNum, page);
    }
}
