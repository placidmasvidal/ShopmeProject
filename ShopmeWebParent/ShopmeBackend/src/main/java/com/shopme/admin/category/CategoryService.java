package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import com.shopme.common.exception.CategoryNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CategoryService {

    Category saveCategory(Category category);

    List<Category> listByPage(CategoryPageInfo pageInfo, int pageNum, String sortDir, String keyword);

    List<Category> listCategoriesUsedInForm();

    Category get(Integer id) throws CategoryNotFoundException;

    String checkUnique(Integer id, String name, String alias);

    @Transactional
    void updateCategoryEnabledStatus(Integer id, boolean enabled);

    void delete(Integer id) throws CategoryNotFoundException;

}
