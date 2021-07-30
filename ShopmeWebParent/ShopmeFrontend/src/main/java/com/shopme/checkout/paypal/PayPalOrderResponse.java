package com.shopme.checkout.paypal;

public class PayPalOrderResponse {

    private String id;  //must match id and status of an order done.
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean validate(String orderId){
        return id.equals(orderId) && status.equals("COMPLETED");
    }
}
