package com.shopme.category;

import com.shopme.common.entity.Category;

import java.util.List;

public interface CategoryService {

    List<Category> listNoChildrenCategories();
}
