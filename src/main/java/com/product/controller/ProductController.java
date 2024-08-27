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

//    @GetMapping
//    public ApiResponse getProducts() {
//        return productService.getAllProducts();
//    }
//
//    @GetMapping(path = "/{id}")
//    public ApiResponse getProductById(@PathVariable String id) {
//        return productService.getProductById(id);
//    }
//
//    @PatchMapping("/{id}")
//    public ApiResponse updateById(@PathVariable String id, @RequestBody ProductRequestDto request) {
//        return productService.updateById(id, request);
//    }
//
//
//    @DeleteMapping
//    @ResponseStatus(HttpStatus.OK)
//    public ApiResponse deleteProductById(@RequestParam String id) {
//        return productService.deleteProductById(id);
//    }


}
