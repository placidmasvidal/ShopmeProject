package com.shopme.admin.brand;

import com.shopme.common.entity.Brand;

import java.util.List;

public interface BrandService {

    public Brand save(Brand brand);

    public List<Brand> listAll();

    public Brand get(Integer id) throws BrandNotFoundException;

    public void delete(Integer id) throws BrandNotFoundException;

    public String checkUnique(Integer id, String name);
}
