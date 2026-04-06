package com.ecommerce.project.Service;

import com.ecommerce.project.Repository.CategoryRepo;
import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.Category.CategoryDTO;
import com.ecommerce.project.payload.Category.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepo categoryRepo;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort
                .by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Category> categoryPage = categoryRepo.findAll(pageDetails);
        List<Category> categories = categoryPage.getContent();

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
    @Transactional
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
    @Transactional
    public CategoryDTO deleteCategory(Long categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("category","categoryId", categoryId));

        categoryRepo.delete(category);
        CategoryDTO categoryDTO1 = modelMapper.map(category,CategoryDTO.class);

        return categoryDTO1;
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(CategoryDTO categoryDTO,Long categoryId) {
        Category existingCategory = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category", "categoryId", categoryId));
        existingCategory.setCategoryName(categoryDTO.getCategoryName());
        Category updatedCategory = categoryRepo.save(existingCategory);
        return modelMapper.map(updatedCategory, CategoryDTO.class);

    }


}