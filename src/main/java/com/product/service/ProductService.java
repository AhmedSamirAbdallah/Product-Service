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

        Product product = Product.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price()).build();

        product = productRepository.save(product);

        return ApiResponse.ok(product,"Product Created Successfully",HttpStatus.CREATED.value());
    }

    public ApiResponse getAllProducts() {

        List<ProductResponseDto> products = productRepository.findAll()
                .stream()
                .map(product -> new ProductResponseDto(product.getId(), product.getName(), product.getDescription(), product.getPrice()))
                .toList();

        return ApiResponse.ok(products,"success",HttpStatus.OK.value());
    }

    public ApiResponse getProductById(String id) {

        Product product = productRepository.findById(id).orElse(null);
        ProductResponseDto productResponseDto = new ProductResponseDto(product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice());

        return ApiResponse.ok(productResponseDto,"success",HttpStatus.OK.value());

    }

    public ApiResponse updateById(String id, ProductRequestDto request) {

        Product product = productRepository.findById(id).orElse(null);

        if (product == null) return null;
        if (request.name() != null) product.setName(request.name());
        if (request.description() != null) product.setDescription(request.description());
        if (request.price() != null) product.setPrice(request.price());

        product = productRepository.save(product);

        ProductResponseDto productResponseDto = new ProductResponseDto(product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice());

        return ApiResponse.ok(productResponseDto,"Product Updated Successfully",HttpStatus.OK.value());

    }

    public void deleteProductById(String id) {

        productRepository.deleteById(id);
    }


}
