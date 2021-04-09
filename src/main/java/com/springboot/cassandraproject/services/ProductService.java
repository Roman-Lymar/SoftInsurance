package com.springboot.cassandraproject.services;

import com.springboot.cassandraproject.domains.PackageBase;
import com.springboot.cassandraproject.domains.PackageCustom;
import com.springboot.cassandraproject.domains.Product;
import com.springboot.cassandraproject.exceptions.ErrorMessages;
import com.springboot.cassandraproject.exceptions.ResourceNotFoundException;
import com.springboot.cassandraproject.exceptions.UnableDeleteProductException;
import com.springboot.cassandraproject.repositories.PackageBaseRepository;
import com.springboot.cassandraproject.repositories.PackageCustomRepository;
import com.springboot.cassandraproject.repositories.ProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

@Service
public class ProductService {

    private static final Logger logger = LogManager.getLogger(ProductService.class.getSimpleName());

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PackageCustomRepository packageCustomRepository;

    @Autowired
    private PackageBaseRepository packageBaseRepository;

    public Iterable<Product> getAllProducts() {
        logger.info("Method getAllProducts called");
        Iterable<Product> allProducts = productRepository.findAll();
        logger.info("All products successfully returned");
        return allProducts;
    }

    public Optional<Product> getProductById(UUID id) {
        logger.info("Method getProductById called");
        Optional<Product> fetchedProduct = Optional.ofNullable(productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorMessages.NO_RESOURCE_FOUND.getErrorMessage() + id)));
        logger.info("Products successfully returned for id: {}", id);
        return fetchedProduct;
    }

    public List<Product> getProductsBySearchString(String searchStr) {
        logger.info("Method getProductsByName called");
        List<Product> foundProducts = new ArrayList<>();
        for (Product p : productRepository.findAll()) {
            if (containsIgnoreCase(p.getName(), searchStr) || containsIgnoreCase(p.getDescription(), searchStr)) {
                foundProducts.add(p);
            }
        }
        if (foundProducts.isEmpty()) {
            logger.warn("None results by filter: {} NO_RESOURCE_FOUND_BY_NAME", searchStr);
            throw new ResourceNotFoundException(ErrorMessages.NO_RESOURCE_FOUND_BY_NAME.getErrorMessage()
                    + searchStr);
        } else
            logger.info("Products successfully returned with filter: {}", searchStr);
            return foundProducts;
    }

    public Product saveProduct(Product product) {
        logger.info("Method saveProduct called");
        Product savedProduct = productRepository.save(product);
        logger.info("Product successfully saved ");
        return savedProduct;
    }

    public BigDecimal getProductsCostByIds(Set<UUID> ids) {
        logger.info(" Method getProductsCostByIds called with ids: {}", ids);
        BigDecimal cost = BigDecimal.ZERO;
        if (CollectionUtils.isEmpty(ids)) {
            logger.warn("None selected products");
            return cost;
        }

        for (UUID id : ids) {
            Product product = getProductById(id).get();
            cost = cost.add(product.getPrice());
        }
        logger.info("Products successfully returned for {}", ids);
        return cost;
    }

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
            logger.info("Product {} was successfully deleted", id);
        }
    }
}
