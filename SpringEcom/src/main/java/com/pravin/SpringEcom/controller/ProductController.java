package com.pravin.SpringEcom.controller;

import com.pravin.SpringEcom.model.Product;
import com.pravin.SpringEcom.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductService service;


    /*
    @GetMapping("/products")
    public List<Product> getProduct(){
        return service.getAllProducts();
    }

     */

    //Response Entity
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProduct(){
        return new ResponseEntity<>(service.getAllProducts(), HttpStatus.ACCEPTED);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Optional<Product>> getProductById(@PathVariable("id") int id){
        Optional<Product> product = service.getProduct(id);

        if (product.isPresent()){
            return new ResponseEntity<>(product,HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile) {
        Product savedProduct = null;
        try {
            savedProduct = service.addProduct(product, imageFile);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
