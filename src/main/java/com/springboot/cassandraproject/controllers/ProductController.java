package com.springboot.cassandraproject.controllers;

import com.springboot.cassandraproject.domains.Product;
import com.springboot.cassandraproject.services.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/catalog/products")
public class ProductController {

    private static final Logger logger = LogManager.getLogger(ProductController.class.getSimpleName());
    private static final String MAPPING_PATH_ID = "/{id}";
    private static final String MAPPING_PATH_COST = "/cost";
    private static final String PATH_VARIABLE_ID = "id";
    private static final String REQUEST_PARAM_FILTER = "filter";
    private static final String REQUEST_PARAM_IDS = "ids";

    @Autowired
    private ProductService productService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Product>> getAllProductsOrFilterBySearchString(
            @RequestParam(name = REQUEST_PARAM_FILTER, required = false) final String searchStr) {

        if(searchStr == null) {
            logger.info("Request getAllProductsOrFilterByName");
            return ResponseEntity.ok(productService.getAllProducts());
        }
        else {
            logger.info("Request getAllProductsOrFilterByName with filter: {}", searchStr);
            return ResponseEntity.ok(productService.getProductsBySearchString(searchStr));
        }
    }

    @GetMapping(path=MAPPING_PATH_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> findProductById(@PathVariable(PATH_VARIABLE_ID) final UUID id) {

            logger.info("Request Get product by id {}", id);
            Optional<Product> product = productService.getProductById(id);
            return ResponseEntity.ok().body(product.get());
    }

    @GetMapping(path=MAPPING_PATH_COST)
    public ResponseEntity<BigDecimal> getProductsCostFilterByIds(
            @RequestParam(name = REQUEST_PARAM_IDS) Set<UUID> ids) {

            logger.info("Request Get product by id with filter COST for {}", ids);
            BigDecimal cost = productService.getProductsCostByIds(ids);
            return ResponseEntity.ok(cost);
    }

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> createNewProduct(
            @Valid @RequestBody final Product product) throws URISyntaxException {

            Product createdProduct = productService.saveProduct(product);
            logger.info("Request createNewProduct");
            return ResponseEntity.created(new URI("/catalog/products/" + createdProduct
                    .getId())).body(product);
    }

    @PreAuthorize("hasAuthority('admin')")
    @PutMapping(path=MAPPING_PATH_ID, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> replaceProduct(@Valid @RequestBody final Product newProduct,
                                                  @PathVariable(PATH_VARIABLE_ID) final UUID id) {

            logger.info("Request replaceProduct for id: {}", id);
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

    @PreAuthorize("hasAuthority('admin')")
    @PatchMapping(path=MAPPING_PATH_ID, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> partialUpdateProduct(@RequestBody final Product productUpdate,
                                                        @PathVariable(PATH_VARIABLE_ID) final UUID id) {

            logger.info("Request partialUpdateProduct for id: {}", id);
            Optional<Product> productOptional = productService.getProductById(id);

            Product product = productOptional.get();
            if (productUpdate.getName() != null) {
                product.setName(productUpdate.getName());
                logger.info("Updated Product Name to: {} for id: {}", product.getName(), id);
            }
            if (productUpdate.getDescription() != null) {
                product.setDescription(productUpdate.getDescription());
                logger.info("Updated Product Description to: "+ product.getDescription()+ " for id: " + id);
                logger.info("Updated Product Description to: {} for id: {}", product.getDescription(), id);
            }
            if (!productUpdate.getPrice().equals(BigDecimal.ZERO)) {
                product.setPrice(productUpdate.getPrice());
                logger.info("Updated Product Price to: {} for id: {}", product.getPrice(), id);
            }

            return ResponseEntity.ok(productService.saveProduct(product));
    }

    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping(path=MAPPING_PATH_ID)
    public ResponseEntity<Void> deleteProductById(@PathVariable(PATH_VARIABLE_ID) final UUID id) {

            logger.info("Request deleteProductById for id: {}", id);
            productService.deleteProduct(id);
            return ResponseEntity.status(HttpStatus.OK).build();
    }
}
