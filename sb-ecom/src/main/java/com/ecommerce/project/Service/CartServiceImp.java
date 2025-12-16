package com.ecommerce.project.Service;

import com.ecommerce.project.Repository.CartItemRepository;
import com.ecommerce.project.Repository.CartRepository;
import com.ecommerce.project.Repository.ProductRepo;
import com.ecommerce.project.Util.AuthUtil;
import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.CartItem;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.payload.ProductDTO;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class CartServiceImp implements CartService{
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CartDTO addProduct(Long productId, Integer quantity) {
        Cart cart = createCart();
        Product product = productRepo.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(),productId);
        if (cartItem != null){
            throw new APIException("Product "+product.getProductName() + "already exists in cart ");
        }
        if (product.getQuantity() == 0){
            throw new APIException(product.getProductName() + " is not available");

        }
        if (product.getQuantity() < quantity){
            throw new APIException("Please ,make an order of the "+product.getProductName() + " less than or equal to quantity "+ product.getQuantity());

        }
        CartItem newCartItems = new CartItem();
        newCartItems.setCart(cart);
        newCartItems.setProduct(product);
        newCartItems.setProductPrice(product.getSpecialPrice());
        newCartItems.setQuantity(quantity);
        newCartItems.setDiscount(product.getDiscount());

        CartItem savedCartItem = cartItemRepository.save(newCartItems);


        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
        cartRepository.save(cart);

        CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
        List<CartItem> cartItems = cart.getCartItems();
        Stream<ProductDTO> products =  cartItems.stream()       // to show the products with the dto so we used model mapper to map them
                .map(item -> {
                    ProductDTO map = modelMapper.map(item.getProduct(),ProductDTO.class);
                    map.setQuantity(item.getQuantity());
                    return map;
                }
                );

        cartDTO.setProducts(products.toList());
        return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCart() {
        List<Cart> carts = cartRepository.findAll();
        if (carts.isEmpty()){
            throw new APIException("No cart exists");
        }
        List<CartDTO> cartDTOList = carts.stream()
                .map(cart -> {
                    CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
                    List<ProductDTO> products = cart.getCartItems().stream()
                            .map(p -> {
                                ProductDTO productDTO = modelMapper.map(p.getProduct(),ProductDTO.class);
                                productDTO.setQuantity(p.getQuantity());
                                return productDTO;
                            }).toList();
                    cartDTO.setProducts(products);
                    return cartDTO;
                })
                .toList();

        return  cartDTOList;
    }

    @Override
    public CartDTO getUserCart(String emailId, Long cartId) {
        Cart cart = cartRepository.findCartByEmailAndCartId(emailId,cartId);
        if (cart == null){
            throw new ResourceNotFoundException("Cart","cartId",cartId);
        }

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        cart.getCartItems().forEach(c -> c.getProduct().setQuantity(c.getQuantity()));
        List<ProductDTO> products = cart.getCartItems().stream()
                .map(p -> modelMapper.map(p.getProduct(),ProductDTO.class))
                .toList();
        cartDTO.setProducts(products);
        return cartDTO;
    }

    @Transactional
    @Override
    public CartDTO updateProductQuantityInCart(Long productId, Integer quantity) {
        String emailId = authUtil.loggedInEmail();
        Cart userCart = cartRepository.findCartByEmail(emailId);
        Long cartId = userCart.getCartId();
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart","cartId",cartId));

        Product product = productRepo.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));
        if (product.getQuantity() == 0){
            throw new APIException(product.getProductName()+" is not available");
        }
        if (product.getQuantity() < quantity){
            throw new APIException("Please ,make an order of the "+product.getProductName() + " less than or equal to quantity "+ product.getQuantity());

        }

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId,productId);
        if (cartItem == null){
            throw new APIException(product.getProductName()+" not available in cart !");
        }

        int newQuantity = cartItem.getQuantity() + quantity;

        if (newQuantity < 0){
            throw new APIException("The resulting quantity can not be negative !");
        }
        if (newQuantity == 0){
            deleteProductFromCart(cartId,productId);
        }
        else {

            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setDiscount(product.getDiscount());
            cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getProductPrice() * quantity));
            cartRepository.save(cart);

        }
        CartItem updatedItem = cartItemRepository.save(cartItem);
        if (updatedItem.getQuantity() == 0) {
            cartItemRepository.deleteById(updatedItem.getCartItemId());
        }
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<CartItem> cartItems = cart.getCartItems();
        Stream<ProductDTO> products = cartItems.stream()
                .map(item -> {
                    ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
                    productDTO.setQuantity(item.getQuantity());
                    return productDTO;
                });
        cartDTO.setProducts(products.toList());
        return cartDTO;



    }

    @Transactional
    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(()->new ResourceNotFoundException("Cart","cartId",cartId));
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId,productId);
        if (cartItem == null){
            throw new ResourceNotFoundException("Product","productId",productId);
        }
        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * (cartItem.getQuantity())));
        cartItemRepository.deleteCartItemByProductIdAndCartId(cartId,productId);
        return "Product "+ cartItem.getProduct().getProductName() + "removed from the cart!";
    }

    @Override
    public void updateProductInCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(()->new ResourceNotFoundException("Cart","cartId",cartId));
        Product product = productRepo.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId,productId);
        if (cartItem == null){
            throw new APIException("Product " + product.getProductName() + " not available in cart");
        }
        double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());
        cartItem.setProductPrice(product.getSpecialPrice());
        cart.setTotalPrice(cartPrice + (cartItem.getProductPrice() * cartItem.getQuantity()));
        cartItemRepository.save(cartItem);
    }


    public Cart createCart(){
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if (userCart != null){
            return userCart;
        }
        Cart cart = new Cart();
        cart.setTotalPrice(0.0);
        cart.setUser(authUtil.loggedInUser());
        Cart newCart = cartRepository.save(cart);
        return newCart;

    }


}
