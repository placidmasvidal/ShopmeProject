package com.shopme.shoppingcart;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;

import java.util.List;

public interface ShoppingCartService {

    Integer addProduct(Integer productId, Integer quantity, Customer customer) throws ShoppingCartException;

    List<CartItem> listCartItems(Customer customer);

    float updateQuantity(Integer productId, Integer quantity, Customer customer);

    void removeProduct(Customer customer, Integer productId);

    void deleteByCustomer(Customer customer);
}
