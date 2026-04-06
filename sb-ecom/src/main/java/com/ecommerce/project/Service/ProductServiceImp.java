package com.ecommerce.project.Service;

import com.ecommerce.project.Repository.CartRepository;
import com.ecommerce.project.Repository.CategoryRepo;
import com.ecommerce.project.Repository.ProductRepository;
import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.NOCategoryCreated;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.Product.ProductDTO;
import com.ecommerce.project.payload.Product.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService{

    private final ModelMapper modelMapper;
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final ProductRepository productRepo;
    private final CategoryRepo categoryRepo;
    private final FileService fileService;

    @Value("${project.image}")
    private  String path;

    @Override
    @Transactional
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {

        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        boolean productExists = productRepo.existsByProductNameAndCategory(productDTO.getProductName(), category);

        if (productExists) {
            throw new APIException("Product " + productDTO.getProductName() + " already exists in this category!");
        }

        Product product = modelMapper.map(productDTO, Product.class);
        product.setImage("default.png");
        product.setCategory(category);

        double specialPrice = product.getPrice() - ((product.getDiscount() / 100.0) * product.getPrice());
        product.setSpecialPrice(specialPrice);

        Product savedProduct = productRepo.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getAllProducts(
                                            Integer pageNumber,
                                            Integer pageSize,
                                            String sortBy,
                                            String sortOrder) {
        /**
         * Paging and Sorting
         */
        Sort sortByOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        // pagination information
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByOrder);

        Page<Product> products =  productRepo.findAll(pageDetails);
        List<Product> productList = products.getContent();

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .toList();

        if (products.isEmpty()){
            throw new NOCategoryCreated("Product not saved yet ");
        }

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(products.getNumber());
        productResponse.setPageSize(products.getSize());
        productResponse.setTotalElements(products.getTotalElements());
        productResponse.setTotalPages(products.getTotalPages());
        productResponse.setLastPage(products.isLast());

        return  productResponse;
    }


    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductsByCategory(
                                                Long categoryId,
                                                Integer pageNumber,
                                                Integer pageSize,
                                                String sortBy,
                                                String sortOrder) {

        Sort sortByOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByOrder);

        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",categoryId));

        Page<Product> pageProducts = productRepo.findByCategoryOrderByPriceAsc(category,pageDetails);
        List<Product> products = pageProducts.getContent();

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductsByKeyword(
                                                String keyword,
                                                Integer pageNumber,
                                                Integer pageSize,
                                                String sortBy,
                                                String sortOrder) {

        Sort sortByOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByOrder);

        Page<Product> pageProducts = productRepo.findByProductNameContainingIgnoreCase(keyword, pageDetails);
        List<Product> products = pageProducts.getContent();

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {

        Product productFound = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        productFound.setProductName(productDTO.getProductName());
        productFound.setDescription(productDTO.getDescription());
        productFound.setImage(productDTO.getImage());
        productFound.setDiscount(productDTO.getDiscount());
        productFound.setPrice(productDTO.getPrice());
        productFound.setQuantity(productDTO.getQuantity());

        double specialPrice = productDTO.getPrice() - ((productDTO.getDiscount() / 100.0) * productDTO.getPrice());
        productFound.setSpecialPrice(specialPrice);

        List<Cart> carts = cartRepository.findCartByProductId(productId);

        carts.forEach(cart -> cartService.updateProductInCart(cart.getCartId(), productId));

        return modelMapper.map(productFound, ProductDTO.class);
    }

    @Override
    @Transactional
    public ProductDTO removeProduct(Long productId) {
        /**
         * Get the product with the id
         * then after getting simply remove it from the db
         * and return the object of response
         */
        Product product = productRepo.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product","productId",productId));
        List<Cart> carts = cartRepository.findCartByProductId(productId);
        carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartId(),productId));
        productRepo.delete(product);
        ProductDTO productDTO = modelMapper.map(product,ProductDTO.class);
        return productDTO;
    }


    @Override
    @Transactional
    public ProductDTO updateImage(MultipartFile image, Long productId) throws IOException {
        /**
         * 1. Get product from db
         * 2. Upload image to server
         * 3. Get the file name of uploaded image
         * 4. Updating the new file name to the product
         * 5 .return DTO object
         */
        Product productFound = productRepo.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));

        String fileName = fileService.uploadImage(path,image);

        Product updatedProduct = productRepo.save(productFound);

        return modelMapper.map(updatedProduct,ProductDTO.class);
    }

}
