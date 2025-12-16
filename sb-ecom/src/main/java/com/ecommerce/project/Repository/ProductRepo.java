package com.ecommerce.project.Repository;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {

    List<Product> findByCategory(Category category);

    List<Product> findByCategoryOrderByPriceAsc(Category category);

    List<Product> findByProductNameLikeIgnoreCase(String keyword);

    Product findByProductName(Product product);

    Page<Product> findByCategoryOrderByPriceAsc(Category category, org.springframework.data.domain.Pageable pageDetails);

    Page<Product> findByProductNameLikeIgnoreCase(String s, org.springframework.data.domain.Pageable pageDetails);
}
