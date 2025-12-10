package com.ecommerce.project.Service;

import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO saveProduct(Long categoryId, ProductDTO product);

    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse getProductById(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse getProductByKeyword(String s, Integer pageNumber, Integer pageSize, String sortBy, String keyword);

    ProductDTO updateProducts(ProductDTO product, Long productId);

    ProductDTO removeProducts(Long productId);

    ProductDTO updateImage(MultipartFile image, Long productId) throws IOException;
}
