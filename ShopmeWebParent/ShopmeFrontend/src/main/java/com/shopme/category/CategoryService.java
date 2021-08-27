package com.shopme.category;

import com.shopme.common.entity.Category;
import com.shopme.common.exception.CategoryNotFoundException;

import java.util.List;

public interface CategoryService {

    List<Category> listNoChildrenCategories();

    Category getCategory(String alias) throws CategoryNotFoundException;

    List<Category> getCategoryParents(Category child);
}
