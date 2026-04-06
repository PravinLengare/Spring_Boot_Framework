package com.ecommerce.project.Service;

import com.ecommerce.project.payload.Product.ProductDTO;
import com.ecommerce.project.payload.Product.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, ProductDTO product);

    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse getProductsByKeyword(String s, Integer pageNumber, Integer pageSize, String sortBy, String keyword);

    ProductDTO updateProduct(ProductDTO product, Long productId);

    ProductDTO removeProduct(Long productId);

    ProductDTO updateImage(MultipartFile image, Long productId) throws IOException;
}
