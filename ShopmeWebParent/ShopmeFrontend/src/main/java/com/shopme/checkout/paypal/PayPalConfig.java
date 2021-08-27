package com.shopme.checkout.paypal;

import com.shopme.setting.PaymentSettingBag;
import com.shopme.setting.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Configuration
public class PayPalConfig {

    private SettingService settingService;

    @Autowired
    public PayPalConfig(SettingService settingService) {
        this.settingService = settingService;
    }

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    @Bean
    public HttpEntity<MultiValueMap<String, String>> getHttpEntity(){

        PaymentSettingBag paymentSettings = settingService.getPaymentSettings();
        String clientId = paymentSettings.getClientID();
        String clientSecret = paymentSettings.getClientSecret();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Accept-Language", "en_US");
        headers.setBasicAuth(clientId, clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        return request;
    }
}
