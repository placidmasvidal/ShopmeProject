package com.shopme.admin.product;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.exception.ProductNotFoundException;
import com.shopme.common.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductService {

    List<Product> listAll();

    Product save(Product product);

    String checkUnique(Integer id, String name);

    @Transactional
    void updateProductEnabledStatus(Integer id, boolean enabled);

    void delete(Integer id) throws ProductNotFoundException;

    public Product get(Integer id) throws ProductNotFoundException;

    void listByPage(int pageNum, PagingAndSortingHelper pagingAndSortingHelper, Integer categoryId);

    void saveProductPrice(Product productInForm);
}
