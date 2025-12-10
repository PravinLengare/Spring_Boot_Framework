package com.org.stem_project.service;

import com.org.stem_project.model.Product;
import com.org.stem_project.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImp implements ProductService{
    @Autowired
    private ProductRepo productRepo;

    @Override
    public List<Product> getAllRecords() {
        return  productRepo.findAll();
    }

    @Override
    public Optional<Product> removeProduct(Long id) {
        Optional<Product> productWrapper = productRepo.findById(id);

        if (productWrapper.isPresent()) {
            Product productToDelete = productWrapper.get();
            productRepo.delete(productToDelete);
            return productWrapper;
        }
        return Optional.empty();
    }
}
