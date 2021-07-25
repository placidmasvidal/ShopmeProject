package com.shopme.admin.order.controller;

import com.shopme.admin.order.OrderService;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.setting.SettingService;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.setting.Setting;
import com.shopme.common.exception.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
      @PagingAndSortingParam(listName = "listOrders", moduleURL = "/orders")
          PagingAndSortingHelper helper,
      @PathVariable(name = "pageNum") int pageNum,
      HttpServletRequest servletRequest) {

    orderService.listByPage(pageNum, helper);
    loadCurrencySetting(servletRequest);

    return "orders/orders";
  }

  @GetMapping("/orders/detail/{id}")
  public String viewOrderDetails(
      @PathVariable("id") Integer id,
      Model model,
      RedirectAttributes redirectAttributes,
      HttpServletRequest servletRequest) {
    try {
      Order order = orderService.get(id);
      loadCurrencySetting(servletRequest);
      model.addAttribute("order", order);

      return "orders/order_details_modal";
    } catch (OrderNotFoundException ex) {
      redirectAttributes.addFlashAttribute("message", ex.getMessage());
      return defaultRedirectURL;
    }
  }

  @GetMapping("/orders/delete/{id}")
  public String deleteOrder(
      @PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
    try {
      orderService.delete(id);
      ;
      redirectAttributes.addFlashAttribute("message", "The order ID " + id + " has been deleted.");
    } catch (OrderNotFoundException ex) {
      redirectAttributes.addFlashAttribute("message", ex.getMessage());
    }

    return defaultRedirectURL;
  }

  private void loadCurrencySetting(HttpServletRequest servletRequest) {
    List<Setting> currencySettings = settingService.getCurrencySettings();

    currencySettings.forEach(
        setting -> servletRequest.setAttribute(setting.getKey(), setting.getValue()));
  }
}
