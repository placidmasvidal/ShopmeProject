package com.shopme.checkout.paypal;

public interface PayPalService {

    boolean validateOrder(String orderId) throws PayPalApiException;
}
