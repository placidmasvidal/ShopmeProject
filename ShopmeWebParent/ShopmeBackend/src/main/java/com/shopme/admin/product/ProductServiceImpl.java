package com.shopme.admin.product;

import com.shopme.admin.product.controller.ProductNotFoundException;
import com.shopme.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public List<Product> listAll() {
        return (List<Product>) productRepository.findAll();
    }

    public Page<Product> listByPage(int pageNum, String sortField, String sortDir, String keyword, Integer categoryId) {
        Sort sort = Sort.by(sortField);

        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, ProductConstants.PRODUCTS_PER_PAGE, sort);

        if (keyword != null && !keyword.isEmpty()) {
            if (categoryId != null && categoryId > 0) {
                String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
                return productRepository.searchInCategory(categoryId, categoryIdMatch, keyword, pageable);
            }
            return productRepository.findAll(keyword, pageable);
        }

        if (categoryId != null && categoryId > 0) {
            String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
            return productRepository.findAllInCategory(categoryId, categoryIdMatch, pageable);
        }

        return productRepository.findAll(pageable);
    }

    @Override
    public Product save(Product product) {
        if(product.getId() == null){
            product.setCreatedTime(new Date());
        }

        if(product.getAlias() == null || product.getAlias().isEmpty()){
            String defaultAlias = product.getName().replaceAll(" ", "-");
            product.setAlias(defaultAlias);
        } else{
            product.setAlias(product.getAlias().replaceAll(" ", "-"));
        }

        product.setUpdatedTime(new Date());

        return productRepository.save(product);
    }

  @Override
  public String checkUnique(Integer id, String name) {

    boolean isCreatingNew = (id == null || id == 0);

    Product productByName = productRepository.findByName(name);

    if (isCreatingNew) {
      if (productByName != null) {
        return "Duplicate";
      }
    } else {
      if (productByName != null && productByName.getId() != id) {
        return "Duplicate";
      }
    }

    return "OK";
  }

    @Override
    public void updateProductEnabledStatus(Integer id, boolean enabled) {
        productRepository.updateEnabledStatus(id, enabled);
    }

    @Override
    public void delete(Integer id) throws ProductNotFoundException {
        Long countById = productRepository.countById(id);
        if(countById == null || countById == 0){
            throw new ProductNotFoundException("Couldn't find any product with ID: " + id);
        }

        productRepository.deleteById(id);
    }

    @Override
    public Product get(Integer id) throws ProductNotFoundException {
        try{
            return productRepository.findById(id).get();
        } catch (NoSuchElementException ex){
            throw new ProductNotFoundException("Could not find any product with ID " + id);
        }
    }

}
