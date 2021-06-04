package com.shopme.admin.brand;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class BrandRepositoryTests {

  @Autowired private BrandRepository brandRepository;

  @Autowired private TestEntityManager entityManager;

  @Test
  public void testCreateNewBrand() {
    Brand brand = new Brand("Samsung");
    Set<Category> categories =
            new HashSet<>(
                    List.of(entityManager.find(Category.class, 29), entityManager.find(Category.class, 24)));
    brand.setCategories(categories);

    Brand savedBrand = brandRepository.save(brand);

    assertThat(savedBrand.getId()).isGreaterThan(0);
  }

  @Test
//  @Transactional(propagation = Propagation.NESTED) see: https://dzone.com/articles/spring-transaction-propagation
  public void testFindAllBrands(){
    Iterable<Brand> brandsInDB = brandRepository.findAll();
    brandsInDB.forEach(System.out::println);

    assertThat(brandsInDB).isNotEmpty();
  }

  @Test
  public void testGetBrandById(){
    Brand brand = brandRepository.findById(1).get();
    assertThat(brand.getName()).isEqualTo("Acer");
  }

  @Test
  public void testUpdateBrand(){
    Brand brand = brandRepository.findById(3).get();
    brand.setName("Samsung Electronics");

    Brand updatedBrand = brandRepository.save(brand);

    assertThat(updatedBrand.getName()).contains("Electronics");
  }

  @Test
  public void testDeleteBrand(){
    Integer id = 2;
    brandRepository.deleteById(id);

    Optional<Brand> result = brandRepository.findById(id);

    assertThat(result.isEmpty());
  }
}
