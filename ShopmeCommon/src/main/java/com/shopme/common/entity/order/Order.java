package com.shopme.common.entity.order;

import com.shopme.common.entity.AbstractAddress;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.persistence.*;

@Entity
@Table(name = "orders")
public class Order extends AbstractAddress {

  @Column(nullable = false, length = 45)
  private String country;

  private Date orderTime;

  private float shippingCost;

  private float productCost;

  private float subtotal;

  private float tax;

  private float total;

  private int deliverDays;

  private Date deliverDate;

  @Enumerated(EnumType.STRING)
  private PaymentMethod paymentMethod;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private Customer customer;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<OrderDetail> orderDetails = new HashSet<>();

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("updatedTime ASC")
  private List<OrderTrack> orderTracks = new ArrayList<>();

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public Date getOrderTime() {
    return orderTime;
  }

  public void setOrderTime(Date orderTime) {
    this.orderTime = orderTime;
  }

  public float getShippingCost() {
    return shippingCost;
  }

  public void setShippingCost(float shippingCost) {
    this.shippingCost = shippingCost;
  }

  public float getProductCost() {
    return productCost;
  }

  public void setProductCost(float productCost) {
    this.productCost = productCost;
  }

  public float getSubtotal() {
    return subtotal;
  }

  public void setSubtotal(float subtotal) {
    this.subtotal = subtotal;
  }

  public float getTax() {
    return tax;
  }

  public void setTax(float tax) {
    this.tax = tax;
  }

  public float getTotal() {
    return total;
  }

  public void setTotal(float total) {
    this.total = total;
  }

  public int getDeliverDays() {
    return deliverDays;
  }

  public void setDeliverDays(int deliverDays) {
    this.deliverDays = deliverDays;
  }

  public Date getDeliverDate() {
    return deliverDate;
  }

  public void setDeliverDate(Date deliverDate) {
    this.deliverDate = deliverDate;
  }

  public PaymentMethod getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(PaymentMethod paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public Set<OrderDetail> getOrderDetails() {
    return orderDetails;
  }

  public void setOrderDetails(Set<OrderDetail> orderDetails) {
    this.orderDetails = orderDetails;
  }

  public List<OrderTrack> getOrderTracks() {
    return orderTracks;
  }

  public void setOrderTracks(List<OrderTrack> orderTracks) {
    this.orderTracks = orderTracks;
  }

  public void copyAddressFromCustomer() {
    setFirstName(customer.getFirstName());
    setLastName(customer.getLastName());
    setPhoneNumber(customer.getPhoneNumber());
    setAddressLine1(customer.getAddressLine1());
    setAddressLine2(customer.getAddressLine2());
    setCity(customer.getCity());
    setCountry(customer.getCountry().getName());
    setPostalCode(customer.getPostalCode());
    setState(customer.getState());
  }

  @Transient
  public String getDestination() {
    String destination = city + ", ";
    if (state != null && !state.isEmpty()) destination += state + ", ";
    destination += country;

    return destination;
  }

  @Override
  public String toString() {
    return "Order{"
        + "id="
        + id
        + ", subtotal="
        + subtotal
        + ", paymentMethod="
        + paymentMethod
        + ", status="
        + status
        + ", customer="
        + customer.getFullName()
        + '}';
  }

  public void copyShippingAddress(Address address) {
    setFirstName(address.getFirstName());
    setLastName(address.getLastName());
    setPhoneNumber(address.getPhoneNumber());
    setAddressLine1(address.getAddressLine1());
    setAddressLine2(address.getAddressLine2());
    setCity(address.getCity());
    setCountry(address.getCountry().getName());
    setPostalCode(address.getPostalCode());
    setState(address.getState());
  }

  @Transient
  public String getShippingAddress() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(firstName);
    List.of(lastName, addressLine1, addressLine2, city, state, country, postalCode, phoneNumber)
        .forEach(
            field -> {
              if (!field.isEmpty() && field != null) {
                if (field.equals(postalCode)) {
                  stringBuilder.append(". Postal Code: " + field);
                } else if (field.equals(phoneNumber)) {
                  stringBuilder.append(". Phone Number: " + field);
                } else {
                  stringBuilder.append(", " + field);
                }
              }
            });
    return stringBuilder.toString();
  }

  @Transient
  public String getDeliverDateOnForm() {
    DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    return dateFormatter.format(this.deliverDate);
  }

  public void setDeliverDateOnForm(String dateString) {
    DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    try {
      this.deliverDate = dateFormatter.parse(dateString);
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  @Transient
  public String getRecipientName() {
    String name =
        (lastName != null && !lastName.isEmpty()) ? firstName + " " + lastName : firstName;
    return name;
  }

  @Transient
  public String getRecipientAddress() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(addressLine1);
    List.of(addressLine2, city, state, country, postalCode)
        .forEach(
            field -> {
              if (!field.isEmpty() && field != null) {
                stringBuilder.append(", " + field);
              }
            });
    return stringBuilder.toString();
  }

  @Transient
  public boolean isCOD() {
    return paymentMethod.equals(PaymentMethod.COD);
  }

  @Transient
  public boolean isPicked() {
    return hasStatus(OrderStatus.PICKED);
  }

  @Transient
  public boolean isShipping() {
    return hasStatus(OrderStatus.SHIPPING);
  }

  @Transient
  public boolean isDelivered() {
    return hasStatus(OrderStatus.DELIVERED);
  }

  @Transient
  public boolean isReturned() {
    return hasStatus(OrderStatus.RETURNED);
  }

  @Transient
  public boolean isReturnRequested() {
    return hasStatus(OrderStatus.RETURN_REQUESTED);
  }

  @Transient
  public boolean isProcessing() {
    return hasStatus(OrderStatus.PROCESSING);
  }

  public boolean hasStatus(OrderStatus status) {
    return orderTracks.stream().anyMatch(orderTrack -> orderTrack.getStatus().equals(status));
  }

  @Transient
  public String getProductNames() {
    StringBuilder sb = new StringBuilder();
    sb.append("<ul>");

    orderDetails.forEach(
        orderDetail -> sb.append("<li>" + orderDetail.getProduct().getShortName() + "</li>"));
    sb.append("</ul>");

    return sb.toString();
  }
}
