package com.shopme.admin.setting;

import com.shopme.common.entity.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CurrencyRepositoryTests {

    @Autowired
    private CurrencyRepository sut;

    @Test
    public void testCreateCurrencies(){

        List<Currency> listCurrencies = Arrays.asList(
                new Currency("United States Dollar", "$", "USD"),
                new Currency("British Pound", "£", "GPB"),
                new Currency("Japanese Yen", "¥", "JPY"),
                new Currency("Euro", "€", "EUR"),
                new Currency("Russian Ruble", "₽", "RUB"),
                new Currency("South Korean Won", "₩", "KRW"),
                new Currency("Chinese Yuan", "¥", "CNY"),
                new Currency("Brazilian Real", "R$", "BRL"),
                new Currency("Australian Dollar", "$", "AUD"),
                new Currency("Canadian Dollar", "$", "CAD"),
                new Currency("Vietnamese đồng ", "₫", "VND"),
                new Currency("Indian Rupee", "₹", "INR")
        );

        sut.saveAll(listCurrencies);

        Iterable<Currency> iterable = sut.findAll();

        assertThat(iterable).size().isEqualTo(12);
    }
}
