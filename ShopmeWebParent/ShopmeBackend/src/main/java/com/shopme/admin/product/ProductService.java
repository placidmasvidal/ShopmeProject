package com.shopme.admin.product;

import com.shopme.admin.product.controller.ProductNotFoundException;
import com.shopme.common.entity.Product;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductService {

    public List<Product> listAll();

    public Product save(Product product);

    public String checkUnique(Integer id, String name);

    @Transactional
    void updateProductEnabledStatus(Integer id, boolean enabled);

    void delete(Integer id) throws ProductNotFoundException;
}
