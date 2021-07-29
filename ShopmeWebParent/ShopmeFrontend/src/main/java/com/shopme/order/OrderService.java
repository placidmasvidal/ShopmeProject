package com.shopme.order;

import com.shopme.checkout.CheckoutInfo;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.PaymentMethod;

import java.util.List;

public interface OrderService {

  Order createOrder(
      Customer customer,
      Address address,
      List<CartItem> cartItems,
      PaymentMethod paymentMethod,
      CheckoutInfo checkoutInfo);
}
