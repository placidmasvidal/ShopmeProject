package com.shopme.admin.brand;

import com.shopme.common.entity.Brand;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer> {

    public Brand findByName(String name);

    public Long countById(Integer id);
}
