package com.shopme.checkout.paypal;

import org.junit.jupiter.api.Test;

import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PayPalApiTests {

  private static final String BASE_URL = "https://api.sandbox.paypal.com";
  private static final String GET_ORDER_API = "/v2/checkout/orders/";
  private static final String CLIENT_ID = "NOT_SET_YET";
  private static final String CLIENT_SECRET = "NOT_SET_YET";

  @Test
  public void testGetOrderDetails() {
    // Get it from alert("Transaction completed. Order ID: " + orderId + ". Amount paid: " + totalAmount); in checkout.html
    String orderId = "4A027975W0474063L";
    String requestURL = BASE_URL + GET_ORDER_API + orderId;

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.add("Accept-Language", "en_US");
    headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
    RestTemplate restTemplate = new RestTemplate();

    ResponseEntity<PayPalOrderResponse> responseEntity =
        restTemplate.exchange(requestURL, HttpMethod.GET, request, PayPalOrderResponse.class);

    PayPalOrderResponse orderResponse = responseEntity.getBody();

    System.out.println("Order ID: = " + orderResponse.getId());
    System.out.println("Validated: " + orderResponse.validate(orderId));
  }
}
