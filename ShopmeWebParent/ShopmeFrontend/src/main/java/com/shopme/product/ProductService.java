package com.shopme.product;

import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.ProductNotFoundException;
import org.springframework.data.domain.Page;

public interface ProductService {

    Page<Product> listByCategory(int pageNum, Integer categoryId);

    Product getProduct(String alias) throws ProductNotFoundException;

    Page<Product> search(String keyword, int pageNum);
}
