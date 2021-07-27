package com.shopme.checkout;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.ShippingRate;

import java.util.List;

public interface CheckoutService {

    CheckoutInfo prepareCheckout(List<CartItem> cartItems, ShippingRate shippingRate);
}
