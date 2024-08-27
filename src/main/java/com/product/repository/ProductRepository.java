package com.product.repository;

import com.product.model.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    Boolean existsByName(String name);

    Boolean existsByCode(String code);
}
