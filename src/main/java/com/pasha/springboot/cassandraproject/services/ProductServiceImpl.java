package com.pasha.springboot.cassandraproject.services;

import com.pasha.springboot.cassandraproject.domains.Product;
import com.pasha.springboot.cassandraproject.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Iterable<Product> getAllProducts() {
        Iterable<Product> allProducts = productRepository.findAll();
        return allProducts;
    }

    @Override
    public Optional<Product> getProductById(UUID id) {
        Optional<Product> fetchedProduct = productRepository.findById(id);
        return fetchedProduct;
    }

    @Override
    public List<Product> getProductsByNames(List<String> names) {
        List<Product> findedProducts = new ArrayList<>();

        List<Product> sortedProducts = new ArrayList<>();
        for(Product p : getAllProducts()) {
            sortedProducts.add(p);
        }
        Collections.sort(sortedProducts);

        for(int i = 0; i < names.size(); i++) {
            for(int j = 0; j < sortedProducts.size(); j++) {
                if(names.get(i).equals(sortedProducts.get(j).getName())) {
                    findedProducts.add(sortedProducts.get(j));
                }
            }
        }
        return findedProducts;
    }

    @Override
    public Product saveProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product can't be null");
        }
        Product savedProduct = productRepository.save(product);
        return savedProduct;
    }

    @Override
    public BigDecimal getProductsCostByIds(List<UUID> ids) {
        BigDecimal cost = BigDecimal.ZERO;
        if(ids == null) { return cost; }

        for(UUID id : ids) {
            Product product = (productRepository.findById(id)).get();
            cost = cost.add(product.getPrice());
        }
        return cost;
    }

    @Override
    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }
}
