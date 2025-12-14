package com.ecommerce.project.Service;

import com.ecommerce.project.payload.CartDTO;
import jakarta.transaction.Transactional;

import java.util.List;

public interface CartService {
    CartDTO addProduct(Long productId, Integer quantity);

    List<CartDTO> getAllCart();

    CartDTO getUserCart(String emailId, Long cartId);

    @Transactional
    CartDTO updateProductQuantityInCart(Long productId, Integer quantity);

    @Transactional
    String deleteProductFromCart(Long cartId, Long productId);

    void updateProductInCart(Long cartId, Long productId);


}
