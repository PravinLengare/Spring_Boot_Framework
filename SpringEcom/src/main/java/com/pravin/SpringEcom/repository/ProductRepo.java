package com.pravin.SpringEcom.repository;

import com.pravin.SpringEcom.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product,Integer> {



}
