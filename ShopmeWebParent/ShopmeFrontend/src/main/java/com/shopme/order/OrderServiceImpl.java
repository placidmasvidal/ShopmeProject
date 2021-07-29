package com.shopme.order;

import com.shopme.checkout.CheckoutInfo;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderDetail;
import com.shopme.common.entity.order.OrderStatus;
import com.shopme.common.entity.order.PaymentMethod;
import com.shopme.common.entity.product.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

  private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

  private OrderRepository orderRepository;

  @Autowired
  public OrderServiceImpl(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }


  @Override
  public Order createOrder(
      Customer customer,
      Address address,
      List<CartItem> cartItems,
      PaymentMethod paymentMethod,
      CheckoutInfo checkoutInfo) {
    Order newOrder = new Order();
    newOrder.setOrderTime(new Date());
    newOrder.setStatus(OrderStatus.NEW);
    newOrder.setCustomer(customer);
    newOrder.setProductCost(checkoutInfo.getProductCost());
    newOrder.setSubtotal(checkoutInfo.getProductTotal());
    newOrder.setShippingCost(checkoutInfo.getShippingCostTotal());
    newOrder.setTax(0.0f);
    newOrder.setTotal(checkoutInfo.getPaymentTotal());
    newOrder.setPaymentMethod(paymentMethod);
    newOrder.setDeliverDays(checkoutInfo.getDeliverDays());
    newOrder.setDeliverDate(checkoutInfo.getDeliverDate());

    if (address == null) { // means recipient's address is the same as customer's address
      newOrder.copyAddressFromCustomer();
    } else {
      newOrder.copyShippingAddress(address);
    }

    Set<OrderDetail> orderDetails =
        cartItems.stream()
            .map(
                cartItem -> {
                  Product product = cartItem.getProduct();
                  OrderDetail orderDetail = new OrderDetail();
                  orderDetail.setOrder(newOrder);
                  orderDetail.setProduct(product);
                  orderDetail.setQuantity(cartItem.getQuantity());
                  orderDetail.setUnitPrice(product.getDiscountPrice());
                  orderDetail.setProductCost(product.getCost() * cartItem.getQuantity());
                  orderDetail.setSubtotal(cartItem.getSubtotal());
                  orderDetail.setShippingCost(cartItem.getShippingCost());
                  return orderDetail;
                })
            .collect(Collectors.toSet());

    newOrder.setOrderDetails(orderDetails);

    LOGGER.info("Processing new order: {}", newOrder);
    newOrder.getOrderDetails().forEach(orderDetail -> LOGGER.info(orderDetail.toString()));

    return orderRepository.save(newOrder);
  }
}
