package com.pasha.springboot.cassandraproject.services;

import com.pasha.springboot.cassandraproject.domains.Product;
import com.pasha.springboot.cassandraproject.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    @Mock
    private ProductRepository productRepository;

    private UUID id;
    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setPrice(BigDecimal.TEN);
        product.setDescription("description");
        product.setName("name");
        id = product.getId();

    }

    @Test
    void getAllProducts() {
    }

    @Test
    void getProductById() {
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        Optional<Product> productById = productServiceImpl.getProductById(id);

        verify(productRepository).findById(id);
        assertTrue(productById.isPresent());
        assertEquals(id, productById.get().getId());
// проверить на носачэлементэксепшен

    }

    @Test
    void getProductsByNames() {
    }

    @Test
    void saveProductSuccess() {
        when(productRepository.save(product)).thenReturn(product);

        Product productSaved = productServiceImpl.saveProduct(product);

        verify(productRepository).save(product);
        assertNotNull(productSaved);
        assertEquals(product.getId(), productSaved.getId());
        assertEquals(product.getName(), productSaved.getName());
        assertEquals(product.getDescription(), productSaved.getDescription());
        assertEquals(product.getPrice(), productSaved.getPrice());

    }

  /*  @Test
    void saveProductIsNull() {
        verify(productRepository, never()).save(any(Product.class));
        assertThrows(Exception.class,
                () -> productServiceImpl.saveProduct(null),
                "Product can't be null");
    }*/

    @Test
    void getProductsCostByIds() {

        //правильность калькуляции цены
    }

    @Test
    void deleteProduct() {
    }
}