package com.ecommerce.project.Controller;

import com.ecommerce.project.Repository.CartRepository;
import com.ecommerce.project.Service.CartService;
import com.ecommerce.project.Util.AuthUtil;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.payload.CartDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final AuthUtil authUtil;
    private final CartRepository cartRepository;

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(
            @PathVariable Long productId,
            @PathVariable Integer quantity){

        CartDTO cartDTO = cartService.addProduct(productId,quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getAllCart(){

        List<CartDTO> cartDTOList =  cartService.getAllCart();
        return new ResponseEntity<>(cartDTOList,HttpStatus.OK);

    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO> getUserCart(){

            String emailId = authUtil.loggedInEmail();
            Cart cart =  cartRepository.findCartByEmail(emailId);
            Long cartId = cart.getCartId();
            CartDTO cartDTO = cartService.getUserCart(emailId,cartId);

            return new ResponseEntity<>(cartDTO,HttpStatus.OK);

    }
    @PutMapping("/carts/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateCartProducts(@PathVariable Long productId,@PathVariable String operation){
        CartDTO cartDTO = cartService.updateProductQuantityInCart(productId,operation.equalsIgnoreCase("delete") ? -1 : 1);
        return new ResponseEntity<>(cartDTO,HttpStatus.OK);
    }

    @DeleteMapping("/carts/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(
            @PathVariable Long cartId,
            @PathVariable Long productId){

        String status = cartService.deleteProductFromCart(cartId,productId);
        return new ResponseEntity<>(status,HttpStatus.OK);
    }



}
