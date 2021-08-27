package com.shopme.admin.setting;

import com.shopme.common.entity.Currency;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CurrencyRepository extends CrudRepository<Currency, Integer> {

    List<Currency> findAllByOrderByNameAsc();
}
