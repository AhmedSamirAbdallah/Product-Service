package com.product.controller;

import com.product.common.ApiResponse;
import com.product.model.dto.ProductRequestDto;
import com.product.service.ProductService;
import com.product.util.Messages;
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
        return ApiResponse.success(productService.createProduct(request), Messages.PRODUCT_CREATED_SUCCESS, HttpStatus.CREATED);
    }

    @GetMapping
    public ApiResponse getProducts() {
        return ApiResponse.success(productService.getAllProducts(), Messages.PRODUCTS_RETRIEVED_SUCCESSFULLY, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ApiResponse getProductById(@PathVariable String id) {
        return ApiResponse.success(productService.getProductById(id), Messages.PRODUCT_RETRIEVED_SUCCESSFULLY, HttpStatus.OK);
    }

    @PutMapping(path = "/{id}")
    public ApiResponse updateById(@PathVariable String id, @RequestBody ProductRequestDto request) {
        return ApiResponse.success(productService.updateById(id, request), Messages.PRODUCT_UPDATED_SUCCESSFULLY, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ApiResponse deleteProductById(@PathVariable String id) {
        return ApiResponse.success(productService.deleteProductById(id), Messages.PRODUCT_DELETED_SUCCESSFULLY, HttpStatus.OK);
    }


}
