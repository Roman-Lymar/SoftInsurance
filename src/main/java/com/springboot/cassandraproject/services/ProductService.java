package com.springboot.cassandraproject.services;

import com.springboot.cassandraproject.domains.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ProductService {
    Iterable<Product> getAllProducts();
    Optional<Product> getProductById(UUID id);
    List<Product> getProductsBySearchString(String searchStr);
    Product saveProduct(Product product);
    BigDecimal getProductsCostByIds(Set<UUID> ids);
    void deleteProduct(UUID id);
}
