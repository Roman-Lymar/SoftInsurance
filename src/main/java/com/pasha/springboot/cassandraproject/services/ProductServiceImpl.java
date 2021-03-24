package com.pasha.springboot.cassandraproject.services;

import com.pasha.springboot.cassandraproject.controllers.ProductController;
import com.pasha.springboot.cassandraproject.domains.PackageBase;
import com.pasha.springboot.cassandraproject.domains.PackageCustom;
import com.pasha.springboot.cassandraproject.domains.Product;
import com.pasha.springboot.cassandraproject.exceptions.ErrorMessages;
import com.pasha.springboot.cassandraproject.exceptions.ResourceNotFoundException;
import com.pasha.springboot.cassandraproject.exceptions.UnableDeleteProductException;
import com.pasha.springboot.cassandraproject.repositories.PackageBaseRepository;
import com.pasha.springboot.cassandraproject.repositories.PackageCustomRepository;
import com.pasha.springboot.cassandraproject.repositories.ProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LogManager.getLogger(ProductServiceImpl.class.getSimpleName());

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PackageCustomRepository packageCustomRepository;

    @Autowired
    private PackageBaseRepository packageBaseRepository;

    @Override
    public Iterable<Product> getAllProducts() {
        logger.info("Method getAllProducts called");
        Iterable<Product> allProducts = productRepository.findAll();
        logger.info("All products successfully returned");
        return allProducts;
    }


    @Override
    public Optional<Product> getProductById(UUID id) {
        logger.info("Method getProductById called");
        Optional<Product> fetchedProduct = Optional.ofNullable(productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorMessages.NO_RESOURCE_FOUND.getErrorMessage() + id)));
        logger.info("Products successfully returned for id: "+ id);
        return fetchedProduct;
    }

    @Override
    public List<Product> getProductsByName(String name) {
        logger.info("Method getProductsByName called");
        List<Product> foundProducts = new ArrayList<>();
        for (Product p : productRepository.findAll()) {
            if (p.getName().contains(name) || p.getDescription().contains(name)) {
                foundProducts.add(p);
            }
        }
        if (foundProducts.isEmpty()) {
            logger.warn("None results by filter: "+ name + " NO_RESOURCE_FOUND_BY_NAME");
            throw new ResourceNotFoundException(ErrorMessages.NO_RESOURCE_FOUND_BY_NAME.getErrorMessage()
                    + name);
        } else
            logger.info("Products successfully returned with filter: " + name);
            return foundProducts;
    }


    @Override
    public Product saveProduct(Product product) {
        logger.info("Method saveProduct called");
        Product savedProduct = productRepository.save(product);
        logger.info("Product successfully saved ");
        return savedProduct;
    }

    @Override
    public BigDecimal getProductsCostByIds(Set<UUID> ids) {
        logger.info(" Method getProductsCostByIds called with ids: " + ids);
        BigDecimal cost = BigDecimal.ZERO;
        if (CollectionUtils.isEmpty(ids)) {
            logger.warn("None selected products");
            return cost;
        }

        for (UUID id : ids) {
            Product product = getProductById(id).get();
            cost = cost.add(product.getPrice());
        }
        logger.info("Products successfully returned for " + ids);
        return cost;
    }

    @Override
    public void deleteProduct(UUID id) {
        logger.info("Method deleteProduct called");
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
            logger.info("Product " + id + " was successfully deleted");
        }
    }
}
