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
    List<Category> listNoChildrenCategories = new ArrayList<>();

    List<Category> listEnabledCategories = categoryRepository.findAllEnabled();

    listEnabledCategories.forEach(
        category -> {
          Set<Category> children = category.getChildren();
          if (children == null || children.size() == 0) {
            listNoChildrenCategories.add(category);
          }
        });

    return listNoChildrenCategories;
  }
}