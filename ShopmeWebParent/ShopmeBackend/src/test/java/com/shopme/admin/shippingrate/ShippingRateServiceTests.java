package com.shopme.admin.shippingrate;

import com.shopme.admin.product.ProductRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class ShippingRateServiceTests {

  @MockBean private ShippingRateRepository shippingRateRepositoryMock;

  @MockBean private CountryRepository countryRepositoryMock;

  @MockBean private ProductRepository productRepositoryMock;

  private ShippingRateService sut;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    sut =
        new ShippingRateServiceImpl(
            shippingRateRepositoryMock, countryRepositoryMock, productRepositoryMock);
  }

  @Test
  public void testCalculateShippingCost_NoRateFound() {
    Integer productId = 1;
    Integer countryId = 234;
    String state = "ABCDE";

    Mockito.when(shippingRateRepositoryMock.findByCountryAndState(countryId, state))
        .thenReturn(null);

    assertThrows(
        ShippingRateNotFoundException.class,
        new Executable() {

          @Override
          public void execute() throws Throwable {
            sut.calculateShippingCost(productId, countryId, state);
          }
        });
  }

  @Test
  public void testCalculateShippingCost_RateFound() throws ShippingRateNotFoundException {
    Integer productId = 1;
    Integer countryId = 234;
    String state = "New York";

    ShippingRate shippingRate = new ShippingRate();
    shippingRate.setRate(10);

    Mockito.when(shippingRateRepositoryMock.findByCountryAndState(countryId, state))
        .thenReturn(shippingRate);

    Product product = new Product();
    product.setWeight(5);
    product.setWidth(4);
    product.setHeight(3);
    product.setLength(8);

    Mockito.when(productRepositoryMock.findById(productId)).thenReturn(Optional.of(product));

    float shippingCost = sut.calculateShippingCost(productId, countryId, state);

    assertEquals(50, shippingCost);
  }
}
