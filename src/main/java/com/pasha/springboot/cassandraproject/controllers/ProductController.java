package com.pasha.springboot.cassandraproject.controllers;

import com.pasha.springboot.cassandraproject.domains.Product;
import com.pasha.springboot.cassandraproject.services.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/v0/catalog/products")
public class ProductController {

private static final Logger logger = LogManager.getLogger(ProductController.class.getSimpleName());


    @Autowired
    private ProductService productService;


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Product>> getAllProductsOrFilterByName(
            @RequestParam(name = "filter", required = false) final String name) {

        if(name == null) {
            logger.info("Request getAllProductsOrFilterByName");

            return ResponseEntity.ok(productService.getAllProducts());
        }
        else {
            logger.info("Request getAllProductsOrFilterByName with filter: " + name);
            return ResponseEntity.ok(productService.getProductsByName(name));
        }
    }


    @GetMapping(path="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> findProductById(@PathVariable("id") final UUID id) {
        logger.info("Request Get product by id " + id);
            Optional<Product> product = productService.getProductById(id);
            return ResponseEntity.ok().body(product.get());
    }

    @GetMapping(path="/cost")
    public ResponseEntity<BigDecimal> getProductsCostFilterByIds(
            @RequestParam(name = "ids") Set<UUID> ids) {
        logger.info("Request Get product by id with filter COST for " + ids);
            BigDecimal cost = productService.getProductsCostByIds(ids);
            return ResponseEntity.ok(cost);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> createNewProduct(@RequestBody final Product product)
            throws URISyntaxException {
            Product createdProduct = productService.saveProduct(product);
        logger.info("Request createNewProduct");
            return ResponseEntity.created(new URI("/catalog/products/" + createdProduct
                    .getId())).body(product);
    }

    @PutMapping(path="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> replaceProduct(@RequestBody final Product newProduct,
                                                 @PathVariable("id") final UUID id) {
        logger.info("Request replaceProduct for id: " + id);
            return productService.getProductById(id)
                    .map(product -> {
                        product.setName(newProduct.getName());
                        product.setDescription(newProduct.getDescription());
                        product.setPrice(newProduct.getPrice());
                        return ResponseEntity.ok(productService.saveProduct(product));
                    })
                    .orElseGet(() -> {
                        newProduct.setId(id);
                        return ResponseEntity.ok(productService.saveProduct(newProduct));
                    });
    }

    @PatchMapping(path="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> partialUpdateProduct(@RequestBody final Product productUpdate,
                                                     @PathVariable("id") final UUID id) {
        logger.info("Request partialUpdateProduct for id: " + id);
            Optional<Product> productOptional = productService.getProductById(id);

            Product product = productOptional.get();
            if (productUpdate.getName() == null) {
                product.setName(productUpdate.getName());
                logger.info("Updated Product Name" +" to: "+ product.getName()+ " for id: " + id);

            }
            if (productUpdate.getDescription() != null) {
                product.setDescription(productUpdate.getDescription());
                logger.info("Updated Product Description" +" to: "+ product.getDescription()+ " for id: " + id);
            }
            if (productUpdate.getPrice() != BigDecimal.ZERO) {
                product.setPrice(productUpdate.getPrice());
                logger.info("Updated Product Price" +" to: "+ product.getPrice()+ " for id: " + id);
            }

            productService.saveProduct(product);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable("id") final UUID id) {
        logger.info("Request deleteProductById for id: " + id);
            productService.deleteProduct(id);
            return ResponseEntity.status(HttpStatus.OK).build();
    }
}
