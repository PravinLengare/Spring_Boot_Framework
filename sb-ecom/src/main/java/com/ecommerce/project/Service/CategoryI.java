package com.ecommerce.project.Service;

import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;

import java.util.Iterator;

public interface CategoryI {
    CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO deleteCategoryById(Long categoryId);
    CategoryDTO changeCategory(CategoryDTO categoryDTO,Long categoryId);
}
