package com.shopme.admin.shippingrate;

import com.shopme.admin.paging.SearchRepository;
import com.shopme.common.entity.ShippingRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ShippingRateRepository extends SearchRepository<ShippingRate, Integer> {

    @Query("SELECT sr FROM ShippingRate sr WHERE sr.country.name LIKE %?1% OR sr.state LIKE %?1%")
    Page<ShippingRate> findAll(String keyword, Pageable pageable);

    @Query("SELECT sr FROM ShippingRate sr WHERE sr.country.id = ?1 AND sr.state = ?2")
    ShippingRate findByCountryAndState(Integer countryId, String state);

    @Query("UPDATE ShippingRate sr SET sr.codSupported = ?2 WHERE sr.id = ?1")
    @Modifying
    void updateCODSupport(Integer id, boolean enabled);

    Long countById(Integer id);
}
