package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CategoryService {

    Category createCategory();

    Category saveCategory(Category category);

    List<Category> listAll();

    List<Category> listCategoriesUsedInForm();

/*    Page<Category> listByPage(int pageNum, String sortField, String sortDir, String keyword);

    boolean isAliasUnique(Integer id, String alias);

    Category get(Integer id) throws CategoryNotFoundException;

    void delete(Integer id) throws CategoryNotFoundException;

    Category getByAlias(String alias);

    @Transactional
    void updateCategoryEnabledStatus(Integer id, boolean enabled);
*/
}
