package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements CategoryService {

  public static final int CATEGORIES_PER_PAGE = 8;

  private CategoryRepository categoryRepository;

  @Autowired
  public CategoryServiceImpl(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Override
  public Category createCategory() {
    return null;
  }

  @Override
  public Category saveCategory(Category category) {
    return categoryRepository.save(category);
  }

  @Override
  public List<Category> listAll() {
    return (List<Category>) categoryRepository.findAll();
  }

  @Override
  public List<Category> listCategoriesUsedInForm() {
    List<Category> categoriesUsedInForm = new ArrayList<>();
    Iterable<Category> categoriesInDB = categoryRepository.findAll();

    for (Category category : categoriesInDB) {
      if (category.getParent() == null) {
        categoriesUsedInForm.add(Category.copyIdAndName(category));

        Set<Category> children = category.getChildren();

        for (Category subCategory : children) {
          String name = "--" + subCategory.getName();
          categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));

          listChildren(subCategory, 1, categoriesUsedInForm);
        }
      }
    }

    return categoriesUsedInForm;
  }

  /*    @Override
      public Page<Category> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
          return null;
      }

      @Override
      public boolean isAliasUnique(Integer id, String alias) {
          return false;
      }

      @Override
      public Category get(Integer id) throws CategoryNotFoundException {
          return null;
      }

      @Override
      public void delete(Integer id) throws CategoryNotFoundException {

      }

      @Override
      public Category getByAlias(String alias) {
          return null;
      }

      @Override
      public void updateCategoryEnabledStatus(Integer id, boolean enabled) {

      }
  */
  private void listChildren(Category parent, int subLevel, List<Category> categoriesUsedInForm) {
    int newSubLevel = subLevel + 1;

    Set<Category> children = parent.getChildren();

    for (Category subCategory : children) {
      String name = "";
      for (int i = 0; i < newSubLevel; i++) {
        name += "--";
      }
      name += subCategory.getName();
      categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));

      listChildren(subCategory, newSubLevel, categoriesUsedInForm);
    }
  }
}
