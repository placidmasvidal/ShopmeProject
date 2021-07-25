package com.shopme.admin.order.controller;

import com.shopme.admin.order.OrderService;
import com.shopme.admin.setting.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class OrderController {

    private OrderService orderService;

    private SettingService settingService;


    @Autowired
    public OrderController(OrderService orderService, SettingService settingService) {
        this.orderService = orderService;
        this.settingService = settingService;
    }
}
