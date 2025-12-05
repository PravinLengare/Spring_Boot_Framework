package com.ecommerce.project.Service;

import com.ecommerce.project.Repo.CategoryRepo;
import com.ecommerce.project.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements CategoryI{

    @Autowired
    private CategoryRepo categoryRepo;

   // private List<Category> categories = new ArrayList<>();

    @Override
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    @Override
    public void createCategory(Category category) {
        categoryRepo.save(category);
    }

    @Override
    public String deleteCategoryById(Long categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Resourse not found"));

        categoryRepo.delete(category);

        return "Category with categoryId " + categoryId + " deleted successfully";
    }

    @Override
    public Category changeCategory(Category category,Long categoryId) {
        Optional<Category> optionalCategory = categoryRepo.findById(categoryId);

        Category savedCategory = optionalCategory
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Resource not found"));

        category.setCategoryId(categoryId);
        savedCategory = categoryRepo.save(category);

        return savedCategory;


        /*
        Optional<Category> categoryNew = categories.stream().filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst();
        if (categoryNew.isPresent()) {
            Category existingCategory = categoryNew.get();
            existingCategory.setCategoryName(category.getCategoryName());
            Category savedCategory = categoryRepo.save(existingCategory);
            return savedCategory;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource Not Found");
        }


         */
    }


}
