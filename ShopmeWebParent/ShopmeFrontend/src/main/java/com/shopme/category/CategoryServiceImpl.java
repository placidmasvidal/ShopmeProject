package com.shopme.category;

import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

  private CategoryRepository categoryRepository;

  @Autowired
  public CategoryServiceImpl(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Override
  public List<Category> listNoChildrenCategories() {

    return categoryRepository.findAllEnabled().stream()
            .filter(category -> category.getChildren() == null || category.getChildren().size() == 0)
            .collect(Collectors.toList());
  }

  @Override
  public Category getCategory(String alias) {
    return categoryRepository.findByAliasEnabled(alias);
  }
}
