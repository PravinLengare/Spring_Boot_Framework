package com.org.stem_project.controller;

import com.org.stem_project.model.Product;
import com.org.stem_project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get")
    public List<Product> getAllProducts(){
        return productService.getAllRecords();

    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> remove(@PathVariable Long id){
        Optional<Product> deleteProduct =  productService.removeProduct(id);

        if (deleteProduct.isPresent()) {
            // Success: Return 200 OK
            return ResponseEntity.ok("Product deleted successfully");
        } else {
            // Failure: Return 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }

    }
}
