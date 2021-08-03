package com.shopme.admin.order;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.order.Order;
import com.shopme.common.exception.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

import static com.shopme.admin.order.OrderConstants.ORDERS_PER_PAGE;

@Service
public class OrderServiceImpl implements OrderService{

    private OrderRepository orderRepository;
    private CountryRepository countryRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, CountryRepository countryRepository) {
        this.orderRepository = orderRepository;
        this.countryRepository = countryRepository;
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

    @Override
    public Order get(Integer id) throws OrderNotFoundException {
        try{
            return orderRepository.findById(id).get();
        } catch(NoSuchElementException e){
            throw new OrderNotFoundException("Could not find any orders with ID " + id);
        }
    }

    @Override
    public void delete(Integer id) throws OrderNotFoundException {
        Long count = orderRepository.countById(id);
        if(count == null || count == 0){
            throw new OrderNotFoundException("Could not find any orders with ID " + id);
        }
        orderRepository.deleteById(id);
    }

    @Override
    public List<Country> listAllCountries() {
        return countryRepository.findAllByOrderByNameAsc();
    }
}
