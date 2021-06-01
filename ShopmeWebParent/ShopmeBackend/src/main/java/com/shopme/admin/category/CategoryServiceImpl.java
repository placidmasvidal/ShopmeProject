package com.shopme.admin.category;

import com.shopme.admin.user.UserNotFoundException;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryServiceImpl implements CategoryService {

  public static final int CATEGORIES_PER_PAGE = 8;

  private CategoryRepository categoryRepository;

  @Autowired
  public CategoryServiceImpl(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Override
  public Category saveCategory(Category category) {
    return categoryRepository.save(category);
  }

  @Override
  public List<Category> listAll(String sortDir) {
    Sort sort = Sort.by("name");

    if (sortDir.equals("asc")) {
      sort = sort.ascending();
    } else if (sortDir.equals("desc")) {
      sort = sort.descending();
    }

    List<Category> rootCategories = categoryRepository.findRootCategories(sort);
    return listHierarchicalCategories(rootCategories, sortDir);
  }

  @Override
  public List<Category> listCategoriesUsedInForm() {
    List<Category> categoriesUsedInForm = new ArrayList<>();
    Iterable<Category> categoriesInDB =
        categoryRepository.findRootCategories(Sort.by("name").ascending());

    for (Category category : categoriesInDB) {
      if (category.getParent() == null) {
        categoriesUsedInForm.add(Category.copyIdAndName(category));

        Set<Category> children = sortSubCategories(category.getChildren());

        for (Category subCategory : children) {
          String name = "--" + subCategory.getName();
          categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));

          listSubCategoriesUsedInForm(subCategory, 1, categoriesUsedInForm);
        }
      }
    }

    return categoriesUsedInForm;
  }

  @Override
  public Category get(Integer id) throws CategoryNotFoundException {
    try {
      return categoryRepository.findById(id).get();
    } catch (NoSuchElementException ex) {
      throw new CategoryNotFoundException("Couldn't find any category with ID: " + id);
    }
  }

  @Override
  public String checkUnique(Integer id, String name, String alias) {

    boolean isCreatingNew = (id == null || id == 0);

    Category categoryByName = categoryRepository.findByName(name);

    if (isCreatingNew) {
      if (categoryByName != null) {
        return "DuplicateName";
      } else {
        Category categoryByAlias = categoryRepository.findByAlias(alias);
        if (categoryByAlias != null) {
          return "DuplicateAlias";
        }
      }
    } else {
      if (categoryByName != null && categoryByName.getId() != id) {
        return "DuplicateName";
      }

      Category categoryByAlias = categoryRepository.findByAlias(alias);
      if (categoryByAlias != null && categoryByAlias.getId() != id) {
        return "DuplicateAlias";
      }
    }

    return "OK";
  }

  @Override
  public void updateCategoryEnabledStatus(Integer id, boolean enabled) {
    categoryRepository.updateEnabledStatus(id, enabled);
  }

  private List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir) {
    List<Category> hierarchicalCategories = new ArrayList<>();

    for (Category rootCategory : rootCategories) {
      hierarchicalCategories.add(Category.copyFull(rootCategory));

      Set<Category> children = sortSubCategories(rootCategory.getChildren(), sortDir);
      for (Category subCategory : children) {
        String name = "--" + subCategory.getName();
        hierarchicalCategories.add(Category.copyFull(subCategory, name));

        listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1, sortDir);
      }
    }

    return hierarchicalCategories;
  }

  private void listSubHierarchicalCategories(
      List<Category> hierarchicalCategories, Category parent, int subLevel, String sortDir) {
    Set<Category> children = sortSubCategories(parent.getChildren(), sortDir);
    int newSubLevel = subLevel + 1;

    for (Category subCategory : children) {
      String name = "";
      for (int i = 0; i < newSubLevel; i++) {
        name += "--";
      }
      name += subCategory.getName();

      hierarchicalCategories.add(Category.copyFull(subCategory, name));

      listSubHierarchicalCategories(hierarchicalCategories, subCategory, newSubLevel, sortDir);
    }
  }

  private void listSubCategoriesUsedInForm(
      Category parent, int subLevel, List<Category> categoriesUsedInForm) {
    int newSubLevel = subLevel + 1;

    Set<Category> children = sortSubCategories(parent.getChildren());

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

  private SortedSet<Category> sortSubCategories(Set<Category> children) {
    return sortSubCategories(children, "asc");
  }

  private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir) {
    SortedSet<Category> sortedChildren =
        new TreeSet<>(
            new Comparator<Category>() {
              @Override
              public int compare(Category category1, Category category2) {
                if (sortDir == null || sortDir.isEmpty() || "asc".equals(sortDir)) {
                  return category1.getName().compareTo(category2.getName());
                } else {
                  return category2.getName().compareTo(category1.getName());
                }
              }
            });

    sortedChildren.addAll(children);
    return sortedChildren;
  }
}
