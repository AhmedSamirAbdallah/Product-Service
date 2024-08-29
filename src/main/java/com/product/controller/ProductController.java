package com.product.controller;

import com.product.common.ApiResponse;
import com.product.model.dto.ProductRequestDto;
import com.product.model.dto.ProductResponseDto;
import com.product.service.ProductService;
import com.product.util.ApiResponseMessages;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    ProductController(ProductService proudctService) {
        this.productService = proudctService;
    }

    @PostMapping
    public ApiResponse createProduct(@Valid @RequestBody ProductRequestDto request) {
        return ApiResponse.success(productService.createProduct(request), ApiResponseMessages.PRODUCT_CREATED_SUCCESS, HttpStatus.CREATED);
    }

    @GetMapping
    public ApiResponse getProducts() {
        return ApiResponse.success(productService.getAllProducts(), ApiResponseMessages.PRODUCTS_RETRIEVED_SUCCESSFULLY, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ApiResponse getProductById(@PathVariable String id) {
        return ApiResponse.success(productService.getProductById(id), ApiResponseMessages.PRODUCT_RETRIEVED_SUCCESSFULLY, HttpStatus.OK);
    }

    @PutMapping(path = "/{id}")
    public ApiResponse updateById(@PathVariable String id, @RequestBody ProductRequestDto request) {
        return ApiResponse.success(productService.updateById(id, request), ApiResponseMessages.PRODUCT_UPDATED_SUCCESSFULLY, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ApiResponse deleteProductById(@PathVariable String id) {
        return ApiResponse.success(productService.deleteProductById(id), ApiResponseMessages.PRODUCT_DELETED_SUCCESSFULLY, HttpStatus.OK);
    }


}
