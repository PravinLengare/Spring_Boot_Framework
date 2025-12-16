package com.ecommerce.project.Service;

import com.ecommerce.project.Repository.CategoryRepo;
import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.NOCategoryCreated;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImp implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ModelMapper modelMapper;
    private Category category;


    @Override
    public CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort
                .by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Category> categoryPage = categoryRepo.findAll(pageDetails);
        List<Category> categories = categoryPage.getContent();

        if (categories.isEmpty()){
            throw new NOCategoryCreated("Still No category is created !!");
        }
        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(category -> modelMapper.map(category,CategoryDTO.class))
                .toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());

        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO,Category.class);
        Category categorySaved = categoryRepo.findByCategoryName(category.getCategoryName());
        if (categorySaved != null){
            throw new APIException("Category with the name "+ category.getCategoryName() + " already exist !!!");
        }
        Category category1 = categoryRepo.save(category);
        CategoryDTO savedCatDto = modelMapper.map(category1,CategoryDTO.class);

        return savedCatDto;
    }

    @Override
    public CategoryDTO deleteCategoryById(Long categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("category","categoryId", categoryId));

        categoryRepo.delete(category);
        CategoryDTO categoryDTO1 = modelMapper.map(category,CategoryDTO.class);

        return categoryDTO1;
    }

    @Override
    public CategoryDTO changeCategory(CategoryDTO categoryDTO,Long categoryId) {
        Category category = modelMapper.map(categoryDTO,Category.class);
        Optional<Category> optionalCategory = categoryRepo.findById(categoryId);

        Category savedCategory = optionalCategory
                .orElseThrow(()-> new ResourceNotFoundException("category","categoryId",categoryId));

        category.setCategoryId(categoryId);
        savedCategory = categoryRepo.save(category);
        CategoryDTO savedCategoryDTO = modelMapper.map(savedCategory,CategoryDTO.class);

        return savedCategoryDTO;

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