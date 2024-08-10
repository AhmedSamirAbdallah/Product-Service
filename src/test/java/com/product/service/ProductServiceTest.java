package com.product.service;

import com.product.model.dto.ApiResponse;
import com.product.model.dto.ProductRequestDto;
import com.product.model.dto.ProductResponseDto;
import com.product.model.entity.Product;
import com.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    // Arrange :  check the method that u want to test it and then mock any other method inside it to your test method
    // , Act : call the method
    // , Assert : make some asserts to make sure the correctness of the behaviour


    @Test
    @Order(1)
    public void testCreateProductWithValidData() {
        Product product = Product.builder().name("product").description("description").price(BigDecimal.valueOf(10)).build();
        ProductRequestDto request = new ProductRequestDto(product.getName(), product.getDescription(), product.getPrice());

        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);

//        "Hey Mockito, when the save method of the productRepository mock object is called with any Product object," +
//        " don't actually save it to a database or perform any real action. Instead," +
//        " just return this specific product object that I've created for this test."

        ApiResponse response = productService.createProduct(request);

        assertNotNull(response);
        assertEquals("Product Created Successfully", response.getMessage());
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(product, response.getResponse());

    }

    @Test
    @Order(2)
    public void testGetAllProductsWithValidScenario() {
        Product product1 = Product.builder().id("id1").name("product1").description("description1").price(new BigDecimal(10)).build();
        Product product2 = Product.builder().id("id2").name("product2").description("description2").price(new BigDecimal(20)).build();

        List<Product> productList = List.of(product1, product2);

        Mockito.when(productRepository.findAll()).thenReturn(productList);

        ApiResponse response = productService.getAllProducts();

        assertNotNull(response);
        assertEquals("success", response.getMessage());
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(productList.stream().map(product -> new ProductResponseDto(product.getId(), product.getName(), product.getDescription(), product.getPrice())).toList(), response.getResponse());

    }

    @Test
    @Order(3)
    public void testGetProductByIdWithValidData() {

        Product product = Product.builder()
                .id("id1")
                .name("product1")
                .description("description1")
                .price(new BigDecimal(10))
                .build();

        Mockito.when(productRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(product));

        ProductResponseDto productResponseDto = new ProductResponseDto(product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice());

        ApiResponse response = productService.getProductById("id1");

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("success", response.getMessage());
        assertEquals(productResponseDto, response.getResponse());
    }

    @Test
    @Order(4)
    public void testUpdateById() {
        Product existingProduct = Product.builder()
                .id("id1")
                .name("product1")
                .description("description1")
                .price(new BigDecimal(10))
                .build();

        Product updatedProduct = Product.builder()
                .id("id1")
                .name("product2")
                .description("description2")
                .price(new BigDecimal(20))
                .build();


        Mockito.when(productRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(existingProduct));
        Mockito.when(productRepository.save((Mockito.any(Product.class)))).thenReturn(updatedProduct);

        ProductRequestDto request = new ProductRequestDto(updatedProduct.getName(),
                updatedProduct.getDescription(),
                updatedProduct.getPrice());

        ProductResponseDto productResponseDto = new ProductResponseDto(updatedProduct.getId(),
                updatedProduct.getName(),
                updatedProduct.getDescription(),
                updatedProduct.getPrice());


        ApiResponse response = productService.updateById("id1", request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("Product Updated Successfully", response.getMessage());
        assertEquals(productResponseDto, response.getResponse());

    }

    @Test
    @Order(5)
    public void testDeleteProductById() {
        String id = "id1";

        Mockito.when(productRepository.existsById(id)).thenReturn(true);
        ApiResponse response = productService.deleteProductById(id);

        assertNotNull(response);
        assertEquals("Product deleted successfully", response.getMessage());
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        Mockito.verify(productRepository, Mockito.times(1)).deleteById(id);

    }


}
