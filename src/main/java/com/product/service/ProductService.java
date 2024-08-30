package com.product.service;

import com.product.exception.BusinessException;
import com.product.mapper.ProductMapper;
import com.product.model.dto.ProductRequestDto;
import com.product.model.dto.ProductResponseDto;
import com.product.model.entity.Product;
import com.product.repository.ProductRepository;
import com.product.util.Messages;
import com.product.util.KafkaTopics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${redis.cache.ttl}")
    private Long timeToLive;

    @Autowired
    ProductService(ProductRepository productRepository, ProductMapper productMapper, KafkaTemplate<String, Object> kafkaTemplate, RedisTemplate<String, Object> redisTemplate) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.kafkaTemplate = kafkaTemplate;
        this.redisTemplate = redisTemplate;
    }

    public ProductResponseDto createProduct(ProductRequestDto request) {

        if (productRepository.existsByName(request.name())) {
            throw new BusinessException(Messages.PRODUCT_NAME_EXISTS, HttpStatus.BAD_REQUEST);
        }

        if (productRepository.existsByCode(request.code())) {
            throw new BusinessException(Messages.PRODUCT_CODE_EXISTS, HttpStatus.BAD_REQUEST);
        }

        Product product = Product.builder()
                .name(request.name())
                .code(request.code())
                .description(request.description())
                .price(request.price())
                .build();

        product = productRepository.save(product);

        redisTemplate.opsForValue().set(Messages.PRODUCT_CACHE_KEY_PREFIX + product.getId(), product, timeToLive, TimeUnit.MINUTES);

        ProductResponseDto responseDto = productMapper.toProductResponseDto(product);

        kafkaTemplate.send(KafkaTopics.PRODUCT_CREATED_EVENT, responseDto);

        return responseDto;
    }

    @Scheduled(cron = "${cache.update.cron}", zone = "${cache.update.zone}")
    public void updateProductCache() {
        try {
            List<Product> products = productRepository.findAll();

            if (!products.isEmpty()) {

                redisTemplate.opsForValue().set(Messages.PRODUCTS_CACHE_KEY_PREFIX, products, timeToLive, TimeUnit.MINUTES);
                logger.info(Messages.PRODUCTS_CACHED);

            }
            else
            {
                logger.info(Messages.NO_PRODUCTS_TO_CACHE);
            }

        } catch (Exception e)
        {

            logger.error(Messages.CACHE_UPDATE_ERROR, e);
        }

    }

    public List<ProductResponseDto> getAllProducts() {

        List<Product> cachedProducts = (List<Product>) redisTemplate.opsForValue().get(Messages.PRODUCTS_CACHE_KEY_PREFIX);

        if (cachedProducts != null && !cachedProducts.isEmpty()) {
            return cachedProducts.stream()
                    .map(productMapper::toProductResponseDto)
                    .toList();
        }

        List<Product> products = productRepository.findAll();

        if (products.isEmpty()) {
            return new ArrayList<>();
        }

        redisTemplate.opsForValue().set(Messages.PRODUCTS_CACHE_KEY_PREFIX, products, timeToLive, TimeUnit.MINUTES);

        return products.stream()
                .map(productMapper::toProductResponseDto)
                .toList();
    }

    private Product getProduct(String id) {
        String cacheKey = Messages.PRODUCT_CACHE_KEY_PREFIX + id;

        Product cachedProduct = (Product) redisTemplate.opsForValue().get(cacheKey);

        if (cachedProduct != null) {
            return cachedProduct;
        }

        Product product = productRepository.findById(id).orElseThrow(() -> new BusinessException(Messages.PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND));

        redisTemplate.opsForValue().set(cacheKey, product, timeToLive, TimeUnit.MINUTES);

        return product;
    }

    public ProductResponseDto getProductById(String id) {


        Product product = getProduct(id);

        return productMapper.toProductResponseDto(product);

    }

    public ProductResponseDto updateById(String id, ProductRequestDto request) {

        Product product = getProduct(id);

        if (request.name() != null && !request.name().equals(product.getName())
                && productRepository.existsByName(request.name())) {
            throw new BusinessException(Messages.PRODUCT_NAME_EXISTS, HttpStatus.BAD_REQUEST);
        }

        if (request.code() != null && !request.code().equals(product.getCode())
                && productRepository.existsByCode(request.code())) {
            throw new BusinessException(Messages.PRODUCT_CODE_EXISTS, HttpStatus.BAD_REQUEST);
        }

        if (request.name() != null) product.setName(request.name());
        if (request.code() != null) product.setCode(request.code());
        if (request.description() != null) product.setDescription(request.description());
        if (request.price() != null) product.setPrice(request.price());

        product = productRepository.save(product);

        redisTemplate.delete(Messages.PRODUCT_CACHE_KEY_PREFIX + product.getId());

        ProductResponseDto responseDto = productMapper.toProductResponseDto(product);

        kafkaTemplate.send(KafkaTopics.PRODUCT_UPDATED_EVENT, responseDto);

        return responseDto;
    }

    public ProductResponseDto deleteProductById(String id) {

        if (!productRepository.existsById(id)) {
            throw new BusinessException(Messages.PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        Product product = getProduct(id);

        ProductResponseDto responseDto = productMapper.toProductResponseDto(product);

        productRepository.deleteById(id);

        redisTemplate.delete(Messages.PRODUCT_CACHE_KEY_PREFIX + id);
        kafkaTemplate.send(KafkaTopics.PRODUCT_DELETED_EVENT, responseDto);

        return responseDto;
    }


}
