package com.shopme.checkout;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.ShippingRate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckoutServiceImpl implements CheckoutService{


    @Override
    public CheckoutInfo prepareCheckout(List<CartItem> cartItems, ShippingRate shippingRate) {
        CheckoutInfo checkoutInfo = new CheckoutInfo();

        checkoutInfo.setProductCost(calculateProductCost(cartItems));
        checkoutInfo.setProductTotal(calculateProductTotal(cartItems));
        checkoutInfo.setDeliverDays(shippingRate.getDays());
        checkoutInfo.setCodSupported(shippingRate.isCodSupported());

        return checkoutInfo;
    }

    private float calculateProductTotal(List<CartItem> cartItems) {
        return (float) cartItems.stream()
                .mapToDouble(cartItem -> {
                    return cartItem.getSubtotal();
                })
                .sum();
    }

    private float calculateProductCost(List<CartItem> cartItems) {
        return (float) cartItems.stream()
                .mapToDouble(cartItem -> {
                    return cartItem.getQuantity() * cartItem.getProduct().getCost();
                })
                .sum();
    }
}
