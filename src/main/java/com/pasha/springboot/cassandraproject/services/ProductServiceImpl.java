package com.pasha.springboot.cassandraproject.services;

import com.pasha.springboot.cassandraproject.domains.PackageBase;
import com.pasha.springboot.cassandraproject.domains.PackageCustom;
import com.pasha.springboot.cassandraproject.domains.Product;
import com.pasha.springboot.cassandraproject.exceptions.ErrorMessages;
import com.pasha.springboot.cassandraproject.exceptions.ResourceNotFoundException;
import com.pasha.springboot.cassandraproject.exceptions.UnableDeleteProductException;
import com.pasha.springboot.cassandraproject.repositories.PackageBaseRepository;
import com.pasha.springboot.cassandraproject.repositories.PackageCustomRepository;
import com.pasha.springboot.cassandraproject.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PackageCustomRepository packageCustomRepository;

    @Autowired
    private PackageBaseRepository packageBaseRepository;

    @Override
    public Iterable<Product> getAllProducts() {
        Iterable<Product> allProducts = productRepository.findAll();
        return allProducts;
    }

    @Override
    public Optional<Product> getProductById(UUID id) {
        Optional<Product> fetchedProduct = Optional.ofNullable(productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorMessages.NO_RESOURCE_FOUND.getErrorMessage() + id)));
        return fetchedProduct;
    }

    @Override
    public List<Product> getProductsByName(String name) {
        List<Product> finedProducts = new ArrayList<>();
        for (Product p : productRepository.findAll()) {
            if (p.getName().contains(name) || p.getDescription().contains(name)) {
                finedProducts.add(p);
            }
        }
        if (finedProducts.isEmpty()) {
            throw new ResourceNotFoundException(ErrorMessages.NO_RESOURCE_FOUND_BY_NAME.getErrorMessage()
                    + name);
        } else
            return finedProducts;
    }


    @Override
    public Product saveProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        return savedProduct;
    }

    @Override
    public BigDecimal getProductsCostByIds(Set<UUID> ids) {
        BigDecimal cost = BigDecimal.ZERO;
        if (CollectionUtils.isEmpty(ids)) {
            return cost;
        }

        for (UUID id : ids) {
            Product product = getProductById(id).get();
            cost = cost.add(product.getPrice());
        }
        return cost;
    }

    @Override
    public void deleteProduct(UUID id) {
        if (getProductById(id).isPresent()) {
            for(PackageBase packageBase: packageBaseRepository.findAll()){
                for(UUID uuid: packageBase.getProductIds()){
                    if(uuid.equals(id)){
                        throw new UnableDeleteProductException(ErrorMessages.UNABLE_DELETE_PACKAGE.getErrorMessage());
                    }
                }
            }
            for(PackageCustom packageCustom: packageCustomRepository.findAll()){
                for(UUID uuid: packageCustom.getProductIds()){
                    if(uuid.equals(id)){
                        throw new UnableDeleteProductException(ErrorMessages.UNABLE_DELETE_PACKAGE.getErrorMessage());
                    }
                }
            }
            productRepository.deleteById(id);
        }
    }
}
