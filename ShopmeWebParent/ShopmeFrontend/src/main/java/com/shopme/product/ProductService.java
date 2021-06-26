package com.shopme.product;

import com.shopme.common.entity.Product;
import org.springframework.data.domain.Page;

public interface ProductService {

    Page<Product> listByCategory(int pageNum, Integer categoryId);
}
