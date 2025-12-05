package com.ecommerce.project.Service;

import com.ecommerce.project.model.Category;
import java.util.List;

public interface CategoryI {
    List<Category> getAllCategories();
    void createCategory(Category category);
    String deleteCategoryById(Long categoryId);
    Category changeCategory(Category category,Long categoryId);
}
