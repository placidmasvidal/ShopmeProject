package com.shopme.checkout.paypal;

import com.shopme.setting.PaymentSettingBag;
import com.shopme.setting.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class PayPalServiceImpl implements PayPalService {

  private SettingService settingService;
  private RestTemplate restTemplate;
  private HttpEntity<MultiValueMap<String, String>> httpEntity;

  @Autowired
  public PayPalServiceImpl(SettingService settingService, RestTemplate restTemplate, HttpEntity<MultiValueMap<String, String>> httpEntity) {
    this.settingService = settingService;
    this.restTemplate = restTemplate;
    this.httpEntity = httpEntity;
  }

  @Override
  public boolean validateOrder(String orderId) throws PayPalApiException {
    PaymentSettingBag paymentSettings = settingService.getPaymentSettings();
    String baseURL = paymentSettings.getURL();
    String requestURL = baseURL + PayPalConstants.GET_ORDER_API + orderId;

    ResponseEntity<PayPalOrderResponse> response =
        restTemplate.exchange(requestURL, HttpMethod.GET, httpEntity, PayPalOrderResponse.class);

    HttpStatus statusCode = response.getStatusCode();

    if (!statusCode.equals(HttpStatus.OK)) {
      throwExceptionForNonOKResponse(statusCode);
    }

    PayPalOrderResponse orderResponse = response.getBody();

    return orderResponse.validate(orderId);
  }

  private void throwExceptionForNonOKResponse(HttpStatus statusCode) throws PayPalApiException {
    String message = null;

    switch (statusCode) {
      case NOT_FOUND:
          message = "Order ID not found";
      case BAD_REQUEST:
          message = "Bad Request to PayPal Checkout API";
      case INTERNAL_SERVER_ERROR:
          message = "PayPal server error";
      default:
        message = "PayPal returned non-OK status code";
    }

    throw new PayPalApiException(message);
  }
}
