package com.shopme.admin.brand;

import com.shopme.common.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService{

    private BrandRepository brandRepository;

    @Autowired
    public BrandServiceImpl(BrandRepository brandRepository){
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
        return null;
    }

    @Override
    public void delete(Integer id) throws BrandNotFoundException {

    }
}
