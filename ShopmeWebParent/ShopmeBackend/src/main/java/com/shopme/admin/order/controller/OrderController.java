package com.shopme.admin.order.controller;

import com.shopme.admin.order.OrderService;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.setting.SettingService;
import com.shopme.common.entity.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.shopme.admin.order.OrderConstants.defaultRedirectURL;


@Controller
public class OrderController {

    private OrderService orderService;

    private SettingService settingService;

    @Autowired
    public OrderController(OrderService orderService, SettingService settingService) {
        this.orderService = orderService;
        this.settingService = settingService;
    }

    @GetMapping("/orders")
    public String listFirstPage() {
        return defaultRedirectURL;
    }

    @GetMapping("/orders/page/{pageNum}")
    public String listByPage(
            @PagingAndSortingParam(listName = "listOrders", moduleURL = "/orders") PagingAndSortingHelper helper,
            @PathVariable(name = "pageNum") int pageNum,
            HttpServletRequest servletRequest) {

        orderService.listByPage(pageNum, helper);
        loadCurrencySetting(servletRequest);

        return "orders/orders";
    }

    private void loadCurrencySetting(HttpServletRequest servletRequest) {
        List<Setting> currencySettings = settingService.getCurrencySettings();

        currencySettings.forEach(setting -> servletRequest.setAttribute(setting.getKey(), setting.getValue()));
    }
}