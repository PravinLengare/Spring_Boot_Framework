package com.ecommerce.project.Controller;

import com.ecommerce.project.Service.CategoryService;
import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Tag(name = "Category APIs")
    @Operation(summary = "Get Category",
            description = "API to get all Categories")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "Created Retrieved !")
    })
    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize" , defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_BY) String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_ORDER) String sortOrder
            ) {

        CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @Tag(name = "Category APIs")
    @Operation(summary = "Create Category",
            description = "API to create new Category")
    @ApiResponses({
            @ApiResponse(responseCode = "201",description = "Created Successfully")
    })
    @PostMapping("/public/categories")
    public ResponseEntity<CategoryDTO> addCategories(@Valid @RequestBody CategoryDTO categoryDTO) {

        CategoryDTO categoryDTO1 = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(categoryDTO1, HttpStatus.CREATED);

    }


    @Tag(name = "Category APIs")
    @Operation(summary = "Update Category",
            description = "API to update Category")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "Updated Successfully")
    })
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId) {

            CategoryDTO savedCategoryDTO = categoryService.deleteCategoryById(categoryId);
            return new ResponseEntity<>(savedCategoryDTO, HttpStatus.OK);

    }


    @Tag(name = "Category APIs",description = "To manage the category")
    @Operation(summary = "Delete Category",
            description = "API to delete Category")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "Deleted Successfully!")
    })
    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO, @PathVariable Long categoryId) {

            CategoryDTO savedCategoryDTO = categoryService.changeCategory(categoryDTO,categoryId);

            return new ResponseEntity<>(savedCategoryDTO, HttpStatus.OK);

    }
}