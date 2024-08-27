package com.product.service;

import com.product.mapper.ProductMapper;
import com.product.model.dto.ProductRequestDto;
import com.product.model.dto.ProductResponseDto;
import com.product.model.entity.Product;
import com.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Autowired
    ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ProductResponseDto createProduct(ProductRequestDto request) {

        Product product = Product.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price()).build();

        return productMapper.toProductResponseDto(productRepository.save(product));
    }

//    public ApiResponse getAllProducts() {
//        List<Product> products = productRepository.findAll();
//        if (products.isEmpty()) {
//            return ApiResponse.ok(List.of(), "No product found", HttpStatus.OK.value());
//        }
//
//        List<ProductResponseDto> productResponseDtoList = products.stream()
//                .map(product -> new ProductResponseDto(product.getId(), product.getName(), product.getDescription(), product.getPrice()))
//                .toList();
//
//        return ApiResponse.ok(productResponseDtoList, "success", HttpStatus.OK.value());
//    }
//
//    public ApiResponse getProductById(String id) {
//
//        Product product = productRepository.findById(id).orElse(null);
//        if (product == null) {
//            return ApiResponse.ok("", "Product not found", HttpStatus.OK.value());
//        }
//        ProductResponseDto productResponseDto = new ProductResponseDto(product.getId(),
//                product.getName(),
//                product.getDescription(),
//                product.getPrice());
//
//        return ApiResponse.ok(productResponseDto, "success", HttpStatus.OK.value());
//
//    }
//
//    public ApiResponse updateById(String id, ProductRequestDto request) {
//
//        Product product = productRepository.findById(id).orElse(null);
//
//        if (product == null) {
//            return ApiResponse.ok("", "Product not found", HttpStatus.OK.value());
//        }
//
//        if (request.name() != null) product.setName(request.name());
//        if (request.description() != null) product.setDescription(request.description());
//        if (request.price() != null) product.setPrice(request.price());
//
//        product = productRepository.save(product);
//
//        ProductResponseDto productResponseDto = new ProductResponseDto(product.getId(),
//                product.getName(),
//                product.getDescription(),
//                product.getPrice());
//
//        return ApiResponse.ok(productResponseDto, "Product Updated Successfully", HttpStatus.OK.value());
//
//    }
//
//    public ApiResponse deleteProductById(String id) {
//
//        if (productRepository.existsById(id)) {
//
//            productRepository.deleteById(id);
//            return ApiResponse.ok("", "Product deleted successfully", HttpStatus.OK.value());
//
//        }
//
//        return ApiResponse.ok("", "Product not found", HttpStatus.NOT_FOUND.value());
//
//    }


}
