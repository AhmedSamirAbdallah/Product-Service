package com.product.controller;

import com.product.model.dto.ApiResponse;
import com.product.model.dto.ProductRequestDto;
import com.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/api/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    ProductController(ProductService proudctService) {
        this.productService = proudctService;
    }

    @PostMapping
    public ApiResponse createProduct(@RequestBody ProductRequestDto request) {
        return productService.createProduct(request);
    }

    @GetMapping
    public ApiResponse getProducts() {
        return productService.getAllProducts();
    }

    @GetMapping(path = "/{id}")
    public ApiResponse getProductById(@PathVariable String id) {
        return productService.getProductById(id);
    }

    @PatchMapping("/{id}")
    public ApiResponse updateById(@PathVariable String id, @RequestBody ProductRequestDto request) {
        return productService.updateById(id, request);
    }


    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteProductById(@RequestParam String id) {
        productService.deleteProductById(id);
    }


}
