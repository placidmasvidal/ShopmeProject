package com.shopme.category;

import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryTests {

    @Autowired
    private CategoryRepository sut;

    @Test
    public void testListEnabledCategories(){
        List<Category> categories = sut.findAllEnabled();

    categories.forEach(
        category -> {
          System.out.println(category.getName() + " (" + category.isEnabled() + ")");
        });
    }

}
