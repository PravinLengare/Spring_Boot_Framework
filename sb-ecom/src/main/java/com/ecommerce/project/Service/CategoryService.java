package com.ecommerce.project.Service;

import com.ecommerce.project.Repo.CategoryRepo;
import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.NOCategoryCreated;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements CategoryI{

    @Autowired
    private CategoryRepo categoryRepo;



    @Override
    public List<Category> getAllCategories() {
        List<Category> category = categoryRepo.findAll();
        if (category.isEmpty()){
            throw new NOCategoryCreated("Still No category is created !!");
        }

        return categoryRepo.findAll();
    }

    @Override
    public void createCategory(Category category) {
        Category categorySaved = categoryRepo.findByCategoryName(category.getCategoryName());
        if (categorySaved != null){
            throw new APIException("Category with the name "+ category.getCategoryName() + " already exist !!!");
        }
        categoryRepo.save(category);
    }

    @Override
    public String deleteCategoryById(Long categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("category","categoryId", categoryId));

        categoryRepo.delete(category);

        return "Category with categoryId " + categoryId + " deleted successfully";
    }

    @Override
    public Category changeCategory(Category category,Long categoryId) {
        Optional<Category> optionalCategory = categoryRepo.findById(categoryId);

        Category savedCategory = optionalCategory
                .orElseThrow(()-> new ResourceNotFoundException("category","categoryId",categoryId));

        category.setCategoryId(categoryId);
        savedCategory = categoryRepo.save(category);

        return savedCategory;



    }


}









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