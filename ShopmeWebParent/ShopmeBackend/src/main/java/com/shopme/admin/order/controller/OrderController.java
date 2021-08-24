package com.shopme.admin.order.controller;

import com.shopme.admin.order.OrderConstants;
import com.shopme.admin.order.OrderService;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.admin.setting.SettingService;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderDetail;
import com.shopme.common.entity.order.OrderStatus;
import com.shopme.common.entity.order.OrderTrack;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.setting.Setting;
import com.shopme.common.exception.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

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
    return OrderConstants.defaultRedirectURL;
  }

  @GetMapping("/orders/page/{pageNum}")
  public String listByPage(
      @PagingAndSortingParam(listName = "listOrders", moduleURL = "/orders")
          PagingAndSortingHelper helper,
      @PathVariable(name = "pageNum") int pageNum,
      HttpServletRequest servletRequest,
      @AuthenticationPrincipal ShopmeUserDetails loggedUser) {

    orderService.listByPage(pageNum, helper);
    loadCurrencySetting(servletRequest);

    if (!loggedUser.hasRole("Admin")
        && !loggedUser.hasRole("Salesperson")
        && loggedUser.hasRole("Shipper")) {
      return "orders/orders_shipper";
    }

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
      return OrderConstants.defaultRedirectURL;
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

    return OrderConstants.defaultRedirectURL;
  }

  private void loadCurrencySetting(HttpServletRequest servletRequest) {
    List<Setting> currencySettings = settingService.getCurrencySettings();

    currencySettings.forEach(
        setting -> servletRequest.setAttribute(setting.getKey(), setting.getValue()));
  }

  @GetMapping("/orders/edit/{id}")
  public String editOrder(
      @PathVariable("id") Integer id,
      Model model,
      RedirectAttributes redirectAttributes,
      HttpServletRequest servletRequest) {
    try {
      Order order = orderService.get(id);

      List<Country> listCountries = orderService.listAllCountries();

      model.addAttribute("pageTitle", "Edit Order (ID: " + id + ")");
      model.addAttribute("order", order);
      model.addAttribute("listCountries", listCountries);

      return "orders/order_form";

    } catch (OrderNotFoundException ex) {
      redirectAttributes.addFlashAttribute("message", ex.getMessage());
      return OrderConstants.defaultRedirectURL;
    }
  }

  @PostMapping("/order/save")
  public String saveOrder(
      Order order, HttpServletRequest servletRequest, RedirectAttributes redirectAttributes) {
    String countryName = servletRequest.getParameter("countryName");
    order.setCountry(countryName);

    updateProductDetails(order, servletRequest);
    updateOrderTracks(order, servletRequest);

    orderService.save(order);

    redirectAttributes.addFlashAttribute(
        "message", "The order ID " + order.getId() + " has been updated successfully.");

    return defaultRedirectURL;
  }

  private void updateOrderTracks(Order order, HttpServletRequest servletRequest) {
    String[] trackIds = servletRequest.getParameterValues("trackId");
    String[] trackDates = servletRequest.getParameterValues("trackDate");
    String[] trackStatuses = servletRequest.getParameterValues("trackStatus");
    String[] trackNotes = servletRequest.getParameterValues("trackNotes");

    List<OrderTrack> orderTracks = order.getOrderTracks();
    DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");

    if (trackIds != null) {
      for (int i = 0; i < trackIds.length; i++) {
        OrderTrack trackRecord = new OrderTrack();
        Integer trackId = Integer.parseInt(trackIds[i]);
        if (trackId > 0) {
          trackRecord.setId(trackId);
        }

        trackRecord.setOrder(order);
        trackRecord.setStatus(OrderStatus.valueOf(trackStatuses[i]));
        trackRecord.setNotes(trackNotes[i]);

        try {
          trackRecord.setUpdatedTime(dateFormatter.parse(trackDates[i]));
        } catch (ParseException e) {
          e.printStackTrace();
        }

        orderTracks.add(trackRecord);
      }
    }
  }

  private void updateProductDetails(Order order, HttpServletRequest servletRequest) {
    String[] detailIds = servletRequest.getParameterValues("detailId");
    String[] productIds = servletRequest.getParameterValues("productId");
    String[] productDetailCosts = servletRequest.getParameterValues("productDetailCost");
    String[] quantities = servletRequest.getParameterValues("quantity");
    String[] productPrices = servletRequest.getParameterValues("productPrice");
    String[] productSubtotals = servletRequest.getParameterValues("productSubtotal");
    String[] productShipCosts = servletRequest.getParameterValues("productShipCost");

    Set<OrderDetail> orderDetails = order.getOrderDetails();
    for (int i = 0; i < detailIds.length; i++) {
      System.out.println("Detail ID: " + detailIds[i]);
      System.out.println("\t Prodouct ID: " + productIds[i]);
      System.out.println("\t Cost: " + productDetailCosts[i]);
      System.out.println("\t Quantity: " + quantities[i]);
      System.out.println("\t Subtotal: " + productSubtotals[i]);
      System.out.println("\t Ship cost: " + productShipCosts[i]);

      OrderDetail orderDetail = new OrderDetail();
      Integer detailId = Integer.parseInt(detailIds[i]);
      if (detailId > 0) {
        orderDetail.setId(detailId);
      }

      orderDetail.setOrder(order);
      orderDetail.setProduct(new Product(Integer.parseInt(productIds[i])));
      orderDetail.setProductCost(Float.parseFloat(productDetailCosts[i]));
      orderDetail.setSubtotal(Float.parseFloat(productSubtotals[i]));
      orderDetail.setShippingCost(Float.parseFloat(productShipCosts[i]));
      orderDetail.setQuantity(Integer.parseInt(quantities[i]));
      orderDetail.setUnitPrice(Float.parseFloat(productPrices[i]));

      orderDetails.add(orderDetail);
    }
  }
}
