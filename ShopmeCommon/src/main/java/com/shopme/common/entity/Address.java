package com.shopme.common.entity;

import javax.persistence.*;

@Entity
@Table(name = "addresses")
public class Address extends AbstractAddressWithCountry {

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private Customer customer;

  @Column(name = "default_address")
  private boolean defaultForShipping;

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public boolean isDefaultForShipping() {
    return defaultForShipping;
  }

  public void setDefaultForShipping(boolean defaultForShipping) {
    this.defaultForShipping = defaultForShipping;
  }

}
