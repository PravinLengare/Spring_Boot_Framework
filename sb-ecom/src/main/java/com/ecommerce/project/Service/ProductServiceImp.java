package com.ecommerce.project.Service;

import com.ecommerce.project.Repo.CategoryRepo;
import com.ecommerce.project.Repo.ProductRepo;
import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.NOCategoryCreated;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImp implements ProductService{
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private  String path;

    @Override
    public ProductDTO saveProduct(Long categoryId, ProductDTO productDTO) {
        /**
         * check if product already exists or not
         */

        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));


        List<Product> products = category.getProducts();
        boolean isProductNotPresent = true;

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getProductName().equals(productDTO.getProductName())){
                isProductNotPresent = false;
                break;
            }
        }
        if (isProductNotPresent == true){
            Product product = modelMapper.map(productDTO,Product.class);

            product.setImage("default.png");
            product.setCategory(category);
            double specialPrice = product.getPrice() -
                    (product.getDiscount() * 0.01  * product.getPrice());
            product.setSpecialPrice(specialPrice);

            Product savedProduct = productRepo.save(product);
            return modelMapper.map(savedProduct,ProductDTO.class);
        }

        else {
            throw new APIException("Product Already Exists! ");
        }
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        /**
         * Paging and Sorting
         */
        Sort sortByOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        Pageable pageDetails = (Pageable) PageRequest.of(pageNumber,pageSize,sortByOrder);

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
    public ProductResponse getProductById(Long categoryId,Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageDetails = (Pageable) PageRequest.of(pageNumber,pageSize,sortByOrder);

        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",categoryId));

        Page<Product> pageProducts = productRepo.findByCategoryOrderByPriceAsc(category,pageDetails);
        List<Product> products = pageProducts.getContent();

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .toList();


        if (products.isEmpty()){
            throw new NOCategoryCreated("Product not saved yet ");
        }

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
    public ProductResponse getProductByKeyword(String keyword,Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageDetails = (Pageable) PageRequest.of(pageNumber,pageSize,sortByOrder);

        Page<Product> PageProducts = productRepo.findByProductNameLikeIgnoreCase('%'+keyword+'%',pageDetails);
        List<Product> products = PageProducts.getContent();

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .toList();

        if (products.isEmpty()){
            throw new NOCategoryCreated("Product not saved yet ");
        }

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(PageProducts.getNumber());
        productResponse.setPageSize(PageProducts.getSize());
        productResponse.setTotalElements(PageProducts.getTotalElements());
        productResponse.setTotalPages(PageProducts.getTotalPages());
        productResponse.setLastPage(PageProducts.isLast());
        return productResponse;
    }

    @Override
    public ProductDTO updateProducts(ProductDTO productDTO, Long productId) {
        /**
         * find the product with id in the database
         */
        Product productFound = productRepo.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product","productId",productId));

        Product product = modelMapper.map(productDTO,Product.class);

        /**
         * update the info of product
         */
        productFound.setProductName(product.getProductName());
        productFound.setDescription(product.getDescription());
        productFound.setImage(product.getImage());
        productFound.setDiscount(product.getDiscount());
        productFound.setPrice(product.getPrice());
        productFound.setQuantity(product.getQuantity());

        double specialPrice = product.getPrice() -
                (product.getDiscount() * 0.01  * product.getPrice());
        product.setSpecialPrice(specialPrice);
        productFound.setSpecialPrice(specialPrice);

        /**
         *  save the updated product in the database.
         */

        Product savedProduct = productRepo.save(productFound);

        /**
         * map using the model mapper class
         */
        return modelMapper.map(savedProduct,ProductDTO.class);
    }

    @Override
    public ProductDTO removeProducts(Long productId) {
        /**
         * Get the product with the id
         * then after getting simply remove it from the db
         * and return the object of response
         */
        Product product = productRepo.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product","productId",productId));

        productRepo.delete(product);
        ProductDTO productDTO = modelMapper.map(product,ProductDTO.class);
        return productDTO;
    }


    @Override
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
