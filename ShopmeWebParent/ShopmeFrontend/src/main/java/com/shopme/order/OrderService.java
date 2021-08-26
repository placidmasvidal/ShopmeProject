package com.shopme.order;

import com.shopme.checkout.CheckoutInfo;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.PaymentMethod;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {

  Order createOrder(
      Customer customer,
      Address address,
      List<CartItem> cartItems,
      PaymentMethod paymentMethod,
      CheckoutInfo checkoutInfo);

  Page<Order> listForCustomerByPage(Customer customer, int pageNum, String sortField, String sortDir, String keyword);
}
