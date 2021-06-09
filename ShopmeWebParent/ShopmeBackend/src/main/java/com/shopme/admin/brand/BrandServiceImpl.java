package com.shopme.admin.brand;

import com.shopme.common.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BrandServiceImpl implements BrandService {

  public static final int BRANDS_PER_PAGE = 10;

  private BrandRepository brandRepository;

  @Autowired
  public BrandServiceImpl(BrandRepository brandRepository) {
    this.brandRepository = brandRepository;
  }

  @Override
  public Brand save(Brand brand) {
    return brandRepository.save(brand);
  }

  @Override
  public Page<Brand> listByPage(int pageNum, String sortDir, String keyword) {
    Sort sort = Sort.by("name");

    if (sortDir.equals("asc")) {
      sort = sort.ascending();
    } else if (sortDir.equals("desc")) {
      sort = sort.descending();
    }

    Pageable pageable = PageRequest.of(pageNum -1, BRANDS_PER_PAGE, sort);

    if(keyword != null){
      return brandRepository.findAll(keyword, pageable);
    }

    return brandRepository.findAll(pageable);
  }

  @Override
  public Brand get(Integer id) throws BrandNotFoundException {
    try{
      return brandRepository.findById(id).get();
    } catch(NoSuchElementException ex){
      throw new BrandNotFoundException("Could not find any brand with ID " + id);
    }
  }

  @Override
  public void delete(Integer id) throws BrandNotFoundException {
    Long countById = brandRepository.countById(id);

    if(countById == null || countById == 0){
      throw new BrandNotFoundException("Could not find any brand with ID " + id);
    }

    brandRepository.deleteById(id);
  }

  @Override
  public String checkUnique(Integer id, String name) {
    boolean isCreatingNew = (id == null || id == 0);

    Brand brandByName = brandRepository.findByName(name);

    if (isCreatingNew) {
      if (brandByName != null) {
        return "Duplicate";
      }
    } else {
      if (brandByName != null && brandByName.getId() != id) {
        return "Duplicate";
      }
    }
    return "OK";
  }
}
