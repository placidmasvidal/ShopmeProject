package com.shopme.admin.shippingrate;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ShippingRateRepositoryTests {

    @Autowired public ShippingRateRepository sut;

    @Test
    public void testCreateNew(){
        Country india = new Country(106);
        ShippingRate newRate = new ShippingRate();
        newRate.setCountry(india);
        newRate.setState("Maharashtra");
        newRate.setRate(8.25f);
        newRate.setDays(3);
        newRate.setCodSupported(true);

        ShippingRate savedRate = sut.save(newRate);
        assertThat(savedRate).isNotNull();
        assertThat(savedRate.getId()).isGreaterThan(0);
    }



}
