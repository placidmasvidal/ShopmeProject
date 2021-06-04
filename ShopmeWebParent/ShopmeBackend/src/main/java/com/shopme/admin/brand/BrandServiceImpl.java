package com.shopme.admin.brand;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

  private BrandRepository brandRepository;

  @Autowired
  public BrandServiceImpl(BrandRepository brandRepository) {
    this.brandRepository = brandRepository;
  }

  @Override
  public Brand saveBrand(Brand brand) {
    return null;
  }

  @Override
  public List<Brand> listAll() {
    return (List<Brand>) brandRepository.findAll();
  }

  @Override
  public Brand get(Integer id) throws BrandNotFoundException {
    return brandRepository.findById(id).get();
  }

  @Override
  public void delete(Integer id) throws BrandNotFoundException {}

  @Override
  public String checkUnique(Integer id, String name) {
    boolean isCreatingNew = (id == null || id == 0);

    Brand brandByName = brandRepository.findByName(name);

    if (isCreatingNew) {
      if (brandByName != null) {
        return "DuplicateName";
      }
    } else {
      if (brandByName != null && brandByName.getId() != id) {
        return "DuplicateName";
      }
    }
    return "OK";
  }
}
