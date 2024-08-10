package com.product.service;

import com.product.model.dto.ApiResponse;
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

    @Autowired
    ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ApiResponse createProduct(ProductRequestDto request) {
        if (request.name() == null || request.description() == null || request.price() == null) {
            return ApiResponse.ok("", "Invalid request data", HttpStatus.BAD_REQUEST.value());
        }

        Product product = Product.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price()).build();

        product = productRepository.save(product);

        return ApiResponse.ok(product, "Product Created Successfully", HttpStatus.CREATED.value());
    }

    public ApiResponse getAllProducts() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            return ApiResponse.ok(List.of(), "No product found", HttpStatus.OK.value());
        }

        List<ProductResponseDto> productResponseDtoList = products.stream()
                .map(product -> new ProductResponseDto(product.getId(), product.getName(), product.getDescription(), product.getPrice()))
                .toList();

        return ApiResponse.ok(productResponseDtoList, "success", HttpStatus.OK.value());
    }

    public ApiResponse getProductById(String id) {

        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ApiResponse.ok("", "Product not found", HttpStatus.OK.value());
        }
        ProductResponseDto productResponseDto = new ProductResponseDto(product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice());

        return ApiResponse.ok(productResponseDto, "success", HttpStatus.OK.value());

    }

    public ApiResponse updateById(String id, ProductRequestDto request) {

        Product product = productRepository.findById(id).orElse(null);

        if (product == null) {
            return ApiResponse.ok("", "Product not found", HttpStatus.OK.value());
        }

        if (request.name() != null) product.setName(request.name());
        if (request.description() != null) product.setDescription(request.description());
        if (request.price() != null) product.setPrice(request.price());

        product = productRepository.save(product);

        ProductResponseDto productResponseDto = new ProductResponseDto(product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice());

        return ApiResponse.ok(productResponseDto, "Product Updated Successfully", HttpStatus.OK.value());

    }

    public ApiResponse deleteProductById(String id) {

        if (productRepository.existsById(id)) {

            productRepository.deleteById(id);
            return ApiResponse.ok("", "Product deleted successfully", HttpStatus.OK.value());

        }

        return ApiResponse.ok("", "Product not found", HttpStatus.NOT_FOUND.value());

    }


}
