package com.product.service;

import com.product.exception.BusinessException;
import com.product.mapper.ProductMapper;
import com.product.model.dto.ProductRequestDto;
import com.product.model.dto.ProductResponseDto;
import com.product.model.entity.Product;
import com.product.repository.ProductRepository;
import com.product.util.ApiResponseMessages;
import com.product.util.KafkaTopics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    ProductService(ProductRepository productRepository, ProductMapper productMapper, KafkaTemplate<String, Object> kafkaTemplate) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    public ProductResponseDto createProduct(ProductRequestDto request) {

        if (productRepository.existsByName(request.name())) {
            throw new BusinessException(ApiResponseMessages.PRODUCT_NAME_EXISTS, HttpStatus.BAD_GATEWAY);
        }

        if (productRepository.existsByCode(request.code())) {
            throw new BusinessException(ApiResponseMessages.PRODUCT_CODE_EXISTS, HttpStatus.BAD_GATEWAY);
        }

        Product product = Product.builder()
                .name(request.name())
                .code(request.code())
                .description(request.description())
                .price(request.price())
                .build();

        kafkaTemplate.send(KafkaTopics.PRODUCT_CREATED_EVENT, productMapper.toProductResponseDto(product));

        return productMapper.toProductResponseDto(productRepository.save(product));
    }

    public List<ProductResponseDto> getAllProducts() {

        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            return new ArrayList<>();
        }

        return products.stream()
                .map(productMapper::toProductResponseDto)
                .toList();
    }

    private Product getProduct(String id) {
        return productRepository.findById(id).orElseThrow(() -> new BusinessException(ApiResponseMessages.PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    public ProductResponseDto getProductById(String id) {


        Product product = getProduct(id);

        return productMapper.toProductResponseDto(product);

    }

    public ProductResponseDto updateById(String id, ProductRequestDto request) {

        Product product = getProduct(id);

        if (request.name() != null && !request.name().equals(product.getName())
                && productRepository.existsByName(request.name())) {
            throw new BusinessException(ApiResponseMessages.PRODUCT_NAME_EXISTS, HttpStatus.BAD_REQUEST);
        }

        if (request.code() != null && !request.code().equals(product.getCode())
                && productRepository.existsByCode(request.code())) {
            throw new BusinessException(ApiResponseMessages.PRODUCT_CODE_EXISTS, HttpStatus.BAD_REQUEST);
        }

        if (request.name() != null) product.setName(request.name());
        if (request.code() != null) product.setCode(request.code());
        if (request.description() != null) product.setDescription(request.description());
        if (request.price() != null) product.setPrice(request.price());

        product = productRepository.save(product);

        return productMapper.toProductResponseDto(product);
    }

    public String deleteProductById(String id) {

        if (!productRepository.existsById(id)) {
            throw new BusinessException(ApiResponseMessages.PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        productRepository.deleteById(id);

        return "Product deleted successfully";
    }


}
