package uz.xnarx.xnarx.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.xnarx.xnarx.entity.Product;
import uz.xnarx.xnarx.exception.ResourceNotFoundException;
import uz.xnarx.xnarx.payload.ApiResponse;
import uz.xnarx.xnarx.payload.ProductDto;
import uz.xnarx.xnarx.repository.PriceHistoryRepository;
import uz.xnarx.xnarx.repository.ProductRepository;
import uz.xnarx.xnarx.utils.CommonUtills;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    PriceHistoryRepository priceHistoryRepository;


    public ApiResponse saveProduct(ProductDto dto){
        try {
            Product product=new Product();
            product.setProduct_name(dto.getName());
            product.setProduct_link(dto.getProduct_link());
            product.setCategory_name(dto.getCategory_name());
            product.setCharacteristics(dto.getCharacteristics());
            product.setStore_name(dto.getStore_name());
            productRepository.save(product);
            return new ApiResponse(dto.getId()!=null?"Edited":"Saved",true);
        }catch (Exception e){
            return new ApiResponse("Error",false);
        }
    }

    public ApiResponse getProductByCategory(String category_name, Integer page, Integer size) {
        Page<ProductDto> productPage=productRepository.
                findProductsByCategory_name(category_name,CommonUtills.simplePageable(page,size));


        return new ApiResponse("Success",
                true,
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.getContent());
    }

    public ApiResponse getProductByName(String name,Double min,Double max, Boolean orderType, Integer page, Integer size) {
        Page<ProductDto> productPage=null;
        if (orderType) {
            productPage = productRepository
                    .searchByNameASC(name, min,max, CommonUtills.simplePageable(page, size));
        }else {
            productPage=productRepository.
                    searchByNameDESC(name, min,max, CommonUtills.simplePageable(page, size));
        }


        return new ApiResponse("Suc" +
                "cess",
                true,
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.getContent());
    }
    public ApiResponse getAllProductHistory(String name, Integer page, Integer size) {
        Page<Product> productPage = productRepository.findAllProductsWithHistory(name, CommonUtills.simplePageable(page, size));
        return new ApiResponse("Success", true, productPage.getTotalElements(), productPage.getTotalPages(), productPage.getContent());
    }
    public List<ProductDto> getAllProduct() {
        return productRepository.getAllProduct();
    }

    public ApiResponse getProductById(Integer id) {
        try {
            ProductDto productDto = productRepository.findCheapestById(id);
            return new ApiResponse("Product found",
                    true,
                    productDto);
        }catch (Exception e){
            return new ApiResponse("Product not found",false);
        }
    }

    public ApiResponse getMinMaxProduct(String categoryName, Double minPrice, Double maxPrice, Boolean orderType,Integer page, Integer size) {
        Page<ProductDto> productPage=null;
        if (orderType) {
            productPage = productRepository.
                    getMinMaxProductASC(categoryName, minPrice, maxPrice, CommonUtills.simplePageable(page, size));
        }else {
            productPage = productRepository.
                    getMinMaxProductDESC(categoryName, minPrice, maxPrice, CommonUtills.simplePageable(page, size));
        }
        return new ApiResponse("Success",
                true,
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.getContent());
    }
}
