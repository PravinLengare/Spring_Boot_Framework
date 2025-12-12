package com.org.stem_project.service;

import com.org.stem_project.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
   List<Product> getAllRecords();
   Optional<Product> removeProduct(Long id);
}
