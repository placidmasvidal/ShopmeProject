package com.shopme.admin.brand;

import com.shopme.common.entity.Brand;
import org.springframework.data.domain.Page;

public interface BrandService {

    public Brand save(Brand brand);

    public Page<Brand> listByPage(int pageNum, String sortDir, String keyword);

    public Brand get(Integer id) throws BrandNotFoundException;

    public void delete(Integer id) throws BrandNotFoundException;

    public String checkUnique(Integer id, String name);
}
