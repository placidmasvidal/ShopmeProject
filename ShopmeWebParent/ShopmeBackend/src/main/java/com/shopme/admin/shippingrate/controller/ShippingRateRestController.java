package com.shopme.admin.shippingrate.controller;

import com.shopme.admin.shippingrate.ShippingRateNotFoundException;
import com.shopme.admin.shippingrate.ShippingRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShippingRateRestController {

    private ShippingRateService shippingRateService;

    @Autowired
    public ShippingRateRestController(ShippingRateService shippingRateService) {
        this.shippingRateService = shippingRateService;
    }

    @PostMapping("/get_shipping_cost")
    public String getShippingCost(Integer productId, Integer countryId, String state) throws ShippingRateNotFoundException {
        float shippingCost = shippingRateService.calculateShippingCost(productId, countryId, state);

        return String.valueOf(shippingCost);
    }
}
