package com.shopme.shipping;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ShippingRateRepositoryTests {

    @Autowired private ShippingRateRepository sut;

    @Autowired TestEntityManager entityManager;

    @Test
    public void testFindByCountryAndState(){
        Country country = entityManager.find(Country.class, 234);
        String state = "California";

        ShippingRate shippingRate = sut.findByCountryAndState(country, state);

        assertThat(shippingRate).isNotNull();
        System.out.println("shippingRate = " + shippingRate);
    }
}
