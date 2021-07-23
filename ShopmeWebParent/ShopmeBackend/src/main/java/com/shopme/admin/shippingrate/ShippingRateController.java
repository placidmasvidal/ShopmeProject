package com.shopme.admin.shippingrate;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ShippingRateController {

    private ShippingRateService shippingRateService;

    @Autowired
    public ShippingRateController(ShippingRateService shippingRateService) {
        this.shippingRateService = shippingRateService;
    }

    @GetMapping("/shipping_rates")
    public String listFirstPage() {
        return ShippingRateConstants.defaultRedirectURL;
    }

    @GetMapping("/shipping_rates/page/{pageNum}")
    public String listByPage(@PagingAndSortingParam(listName = "shippingRates",
            moduleURL = "/shipping_rates") PagingAndSortingHelper helper,
                             @PathVariable(name = "pageNum") int pageNum) {
        shippingRateService.listByPage(pageNum, helper);
        return "shipping_rates/shipping_rates";
    }
}
