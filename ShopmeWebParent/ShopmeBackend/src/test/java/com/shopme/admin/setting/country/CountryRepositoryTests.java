package com.shopme.admin.setting.country;

import com.shopme.common.entity.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CountryRepositoryTests {

  @Autowired private CountryRepository sut;

  @Test
  public void testCreateCountry() {
    Country country = sut.save(new Country("Netherlands", "NL"));

    assertThat(country).isNotNull();
    assertThat(country.getId()).isGreaterThan(0);
  }

  @Test
  public void testListCountries() {
    List<Country> listCountries = sut.findAllByOrderByNameAsc();

    listCountries.forEach(System.out::println);

    assertThat(listCountries.size()).isGreaterThan(0);
  }

  @Test
  public void testUpdateCountry() {
    Integer id = 1;
    String name = "Republic of India";

    Country country = sut.findById(id).get();
    country.setName(name);

    Country updatedCountry = sut.save(country);

    assertThat(updatedCountry.getName()).isEqualTo(name);
  }

  @Test
  public void testGetCountry() {
    Integer id = 1;
    Country country = sut.findById(id).get();
    assertThat(country).isNotNull();
  }

  @Test
  public void testDeleteCountry() {
    Integer id = 5;
    sut.deleteById(id);

    Optional<Country> findById = sut.findById(id);
    assertThat(findById.isEmpty());
  }
}
