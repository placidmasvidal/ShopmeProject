package com.shopme.checkout;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.product.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckoutServiceImpl implements CheckoutService{


    @Override
    public CheckoutInfo prepareCheckout(List<CartItem> cartItems, ShippingRate shippingRate) {
        CheckoutInfo checkoutInfo = new CheckoutInfo();

        float productTotal = calculateProductTotal(cartItems);
        float shippingCostTotal = calculateShippingCostTotal(cartItems, shippingRate);

        checkoutInfo.setProductCost(calculateProductCost(cartItems));
        checkoutInfo.setProductTotal(productTotal);
        checkoutInfo.setDeliverDays(shippingRate.getDays());
        checkoutInfo.setCodSupported(shippingRate.isCodSupported());
        checkoutInfo.setShippingCostTotal(shippingCostTotal);
        checkoutInfo.setPaymentTotal(productTotal + shippingCostTotal);

        return checkoutInfo;
    }

    private float calculateShippingCostTotal(List<CartItem> cartItems, ShippingRate shippingRate) {
        return (float) cartItems.stream()
                .mapToDouble(cartItem -> {
                    Product product = cartItem.getProduct();
                    float dimWeight = product.getLength() * product.getWidth() * product.getHeight() / CheckoutConstants.DIM_DIVISOR;
                    float finalWeight = product.getWeight() > dimWeight ? product.getWeight() : dimWeight;
                    float shippingCost = finalWeight * cartItem.getQuantity() * shippingRate.getRate();
                    cartItem.setShippingCost(shippingCost);
                    return shippingCost;
                })
                .sum();
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
