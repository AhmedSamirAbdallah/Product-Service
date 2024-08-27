package com.product.mapper;

import com.product.model.dto.ProductRequestDto;
import com.product.model.dto.ProductResponseDto;
import com.product.model.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductRequestDto toProductRequestDto(Product product);

    Product toProduct(ProductRequestDto productRequestDto);


    ProductResponseDto toProductResponseDto(Product product);

    Product toProduct(ProductResponseDto productResponseDto);

}
