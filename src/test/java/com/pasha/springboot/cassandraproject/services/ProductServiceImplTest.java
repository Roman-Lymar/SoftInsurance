package com.pasha.springboot.cassandraproject.services;

import com.pasha.springboot.cassandraproject.domains.Product;
import com.pasha.springboot.cassandraproject.repositories.PackageBaseRepository;
import com.pasha.springboot.cassandraproject.repositories.PackageCustomRepository;
import com.pasha.springboot.cassandraproject.repositories.ProductRepository;
import com.pasha.springboot.cassandraproject.services.model.ProductModelService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @MockBean
    private ProductRepository productRepository;
    @Mock
    private PackageCustomRepository packageCustomRepository;
    @Mock
    private PackageBaseRepository packageBaseRepository;
    @Autowired
    ProductService productService;

    @Test
    @DisplayName("Test should ")
    void getAllProducts() {
        when(productRepository.findAll()).thenReturn(ProductModelService.createListOfProducts());
        assertEquals(3, productService.getAllProducts().spliterator().estimateSize());
    }
}