package com.ecommerce.project.payload.Category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    @Schema(description = "Category Id for particular category",example = "101")
    private Long categoryId;
    @Schema(description = "Category Name for particular category",example = "Iphone")
    private String categoryName;
}
