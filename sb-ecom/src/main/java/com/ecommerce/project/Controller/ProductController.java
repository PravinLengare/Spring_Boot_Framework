package com.ecommerce.project.Controller;

import com.ecommerce.project.Service.ProductService;
import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.payload.Product.ProductDTO;
import com.ecommerce.project.payload.Product.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(
                                                 @Valid @RequestBody ProductDTO productDTO,
                                                 @PathVariable Long categoryId){

       ProductDTO savedProductDTO =  productService.addProduct(categoryId,productDTO);
       return new ResponseEntity<>(savedProductDTO,HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(value = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER)Integer pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE)Integer pageSize ,
            @RequestParam(value = "sortBy",defaultValue = AppConstants.SORT_BY)String sortBy,
            @RequestParam(value = "sortOrder",defaultValue = AppConstants.SORT_ORDER)String sortOrder){

        ProductResponse productResponse1 = productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productResponse1,HttpStatus.OK);

    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(value = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER)Integer pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE)Integer pageSize ,
            @RequestParam(value = "sortBy",defaultValue = AppConstants.SORT_BY)String sortBy,
            @RequestParam(value = "sortOrder",defaultValue = AppConstants.SORT_ORDER)String sortOrder) {

        ProductResponse productResponse =  productService.getProductsByCategory(categoryId,pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(
            @PathVariable String keyword,
            @RequestParam(value = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER)Integer pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE)Integer pageSize ,
            @RequestParam(value = "sortBy",defaultValue = AppConstants.SORT_BY)String sortBy,
            @RequestParam(value = "sortOrder",defaultValue = AppConstants.SORT_ORDER)String sortOrder){

        ProductResponse productResponse =  productService.getProductsByKeyword(keyword,pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.FOUND);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(
            @Valid @RequestBody ProductDTO productDTO,
            @PathVariable Long productId){

            ProductDTO savedProductDTO = productService.updateProduct(productDTO,productId);
            return new ResponseEntity<>(savedProductDTO,HttpStatus.OK);
    }


    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
       ProductDTO deletedProduct = productService.removeProduct(productId);
       return new ResponseEntity<>(deletedProduct,HttpStatus.OK);
    }

    @PutMapping("/admin/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateImageOfProduct(@RequestParam("Image")MultipartFile image, @PathVariable Long productId) throws IOException {
        ProductDTO productDTO1 = productService.updateImage(image,productId);
        return new ResponseEntity<>(productDTO1,HttpStatus.OK);
    }


}
