package com.springboot.cassandraproject.controllers;

import com.springboot.cassandraproject.domains.Product;
import com.springboot.cassandraproject.services.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/v0/catalog/products")
@Api(tags = {"Operations available with insurance products"})
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
    @ApiOperation(value = "Gets all the products.",
            notes = "View a list of available products in the catalog.")
    public ResponseEntity<Iterable<Product>> getAllProductsOrFilterBySearchString(
            @ApiParam(value = "Filter uses substrig as a parametr and look for any matches with products fields \"name\" and/or \"description\".")
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
    @ApiOperation(value = "Gets a product by id.",
            notes = "Provides an id to look specific product from the catalog.",
            response = Product.class)
    public ResponseEntity<Product> findProductById(
            @ApiParam(value = "ID value for the product you need to retriev.", required = true)
            @PathVariable(PATH_VARIABLE_ID) final UUID id) {

            logger.info("Request Get product by id {}", id);
            Optional<Product> product = productService.getProductById(id);
            return ResponseEntity.ok().body(product.get());
    }

    @GetMapping(path=MAPPING_PATH_COST)
    @ApiOperation(value = "Gets cost all the products by ids.",
            notes = "Provides a cost of specific product or products from the catalog.")
    public ResponseEntity<BigDecimal> getProductsCostFilterByIds(
            @ApiParam(value = "Finds cost of one or more products by ids. Use ',' as delimiter.", required = true)
            @RequestParam(name = REQUEST_PARAM_IDS) Set<UUID> ids) {

            logger.info("Request Get product by id with filter COST for {}", ids);
            BigDecimal cost = productService.getProductsCostByIds(ids);
            return ResponseEntity.ok(cost);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Creates a new product.",
            notes = "Creates a new product in the catalog.",
            response = Product.class)
    public ResponseEntity<Product> createNewProduct(
            @ApiParam(value = "Valid product body.", required = true)
            @Valid @RequestBody final Product product) throws URISyntaxException {

            Product createdProduct = productService.saveProduct(product);
            logger.info("Request createNewProduct");
            return ResponseEntity.created(new URI("/catalog/products/" + createdProduct
                    .getId())).body(product);
    }

    @PutMapping(path=MAPPING_PATH_ID, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Updates a product.",
            notes = "Fully updates an existing product in the catalog.",
            response = Product.class)
    public ResponseEntity<Product> replaceProduct(
            @ApiParam(value = "Valid product body.", required = true) @Valid @RequestBody final Product newProduct,
            @ApiParam(value = "ID value for the product you need to update.", required = true) @PathVariable(PATH_VARIABLE_ID) final UUID id) {

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

    @PatchMapping(path=MAPPING_PATH_ID, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Updates a product.",
            notes = "Partially updates an existing product in the catalog.",
            response = Void.class)
    public ResponseEntity<Product> partialUpdateProduct(
            @ApiParam(value = "Product fields to update.", required = true) @RequestBody final Product productUpdate,
            @ApiParam(value = "ID value for the product you need to update.", required = true)@PathVariable(PATH_VARIABLE_ID) final UUID id) {

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
            if (productUpdate.getPrice() != BigDecimal.ZERO) {
                product.setPrice(productUpdate.getPrice());
                logger.info("Updated Product Price to: {} for id: {}", product.getPrice(), id);
            }

            return ResponseEntity.ok(productService.saveProduct(product));
    }

    @DeleteMapping(path=MAPPING_PATH_ID)
    @ApiOperation(value = "Deletes a product.",
            notes = "Deletes an existing product in the catalog.")
    public ResponseEntity<Void> deleteProductById(
            @ApiParam(value = "ID value for the product you need to delete.", required = true)
            @PathVariable(PATH_VARIABLE_ID) final UUID id) {

            logger.info("Request deleteProductById for id: {}", id);
            productService.deleteProduct(id);
            return ResponseEntity.status(HttpStatus.OK).build();
    }
}
