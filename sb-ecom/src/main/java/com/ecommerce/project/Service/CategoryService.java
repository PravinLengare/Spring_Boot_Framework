package com.ecommerce.project.Service;

import com.ecommerce.project.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements CategoryI{

    private List<Category> categories = new ArrayList<>();
    private Long Id = 1L;
    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public void createCategory(Category category) {
        category.setCategoryId(Id++);
        categories.add(category);
    }

    @Override
    public String deleteCategoryById(Long categoryId) {
        Category category = categories.stream().filter(c->c.getCategoryId().equals(categoryId))
                .findFirst()
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));


        categories.remove(category);

        return "Category with categoryId" + categoryId + " deleted successfully";
    }

    @Override
    public Category changeCategory(Category category,Long categoryId) {
        Optional<Category> categoryNew = categories.stream().filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst();
        if (categoryNew.isPresent()) {
            Category existingCategory = categoryNew.get();
            existingCategory.setCategoryName(category.getCategoryName());
            return existingCategory;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource Not Found");
        }

    }


}
