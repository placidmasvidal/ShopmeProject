package com.shopme.shoppingcart;

import com.shopme.common.entity.Customer;

public interface ShoppingCartService {

    Integer addProduct(Integer productId, Integer quantity, Customer customer) throws ShoppingCartException;

}
