package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
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
    List<Category> rootCategories = categoryRepository.findRootCategories();
    return listHierarchicalCategories(rootCategories);
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

          listSubCategoriesUsedInForm(subCategory, 1, categoriesUsedInForm);
        }
      }
    }

    return categoriesUsedInForm;
  }

  private List<Category> listHierarchicalCategories(List<Category> rootCategories){
    List<Category> hierarchicalCategories = new ArrayList<>();

    for (Category rootCategory : rootCategories) {
      hierarchicalCategories.add(Category.copyFull(rootCategory));

      Set<Category> children = rootCategory.getChildren();
      for (Category subCategory : children) {
        String name = "--" + subCategory.getName();
        hierarchicalCategories.add(Category.copyFull(subCategory, name));

        listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1);
      }
    }

    return hierarchicalCategories;
  }

  private void listSubHierarchicalCategories(List<Category> hierarchicalCategories, Category parent, int subLevel){
    Set<Category> children = parent.getChildren();
    int newSubLevel = subLevel + 1;

    for (Category subCategory : children) {
      String name = "";
      for (int i = 0; i < newSubLevel; i++) {
        name += "--";
      }
      name += subCategory.getName();

      hierarchicalCategories.add(Category.copyFull(subCategory, name));

      listSubHierarchicalCategories(hierarchicalCategories, subCategory, newSubLevel);
    }
  }

  private void listSubCategoriesUsedInForm(Category parent, int subLevel, List<Category> categoriesUsedInForm) {
    int newSubLevel = subLevel + 1;

    Set<Category> children = parent.getChildren();

    for (Category subCategory : children) {
      String name = "";
      for (int i = 0; i < newSubLevel; i++) {
        name += "--";
      }
      name += subCategory.getName();
      categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));

      listSubCategoriesUsedInForm(subCategory, newSubLevel, categoriesUsedInForm);
    }
  }
}
