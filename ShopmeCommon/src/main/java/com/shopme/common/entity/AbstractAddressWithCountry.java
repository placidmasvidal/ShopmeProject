package com.shopme.common.entity;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class AbstractAddressWithCountry extends AbstractAddress{

    @ManyToOne
    @JoinColumn(name = "country_id")
    protected Country country;

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(firstName);

        if (lastName != null && !lastName.isEmpty()) sb.append(" " + lastName);

        if (!addressLine1.isEmpty()) sb.append(", " + addressLine1);

        if (addressLine2 != null && !addressLine2.isEmpty()) sb.append(", " + addressLine2);

        if (!city.isEmpty()) sb.append(", " + city);

        if (state != null && !state.isEmpty()) sb.append(", " + state);

        sb.append(", " + country.getName());

        if (!postalCode.isEmpty()) sb.append(". Postal Code: " + postalCode);

        if (!phoneNumber.isEmpty()) sb.append(". Phone Number: " + phoneNumber);

        return sb.toString();
    }
}
