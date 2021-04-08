package com.springboot.cassandraproject.controllers.catalog;

import com.springboot.cassandraproject.domains.Product;
import com.springboot.cassandraproject.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
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
@Tag(name = "Products", description = "Operations available with insurance products")
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
    @Operation(summary = "Gets all the products.",
            description = "View a list of available products in the catalog.", tags = {"Products"})
    public ResponseEntity<Iterable<Product>> getAllProductsOrFilterBySearchString(
            @Parameter(description = "Filter uses substrig as a parametr and look for any matches with products fields \"name\" and/or \"description\".")
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
    @Operation(summary = "Gets a product by id.",
            description = "Provides an id to look specific product from the catalog.", tags = {"Products"})
    public ResponseEntity<Product> findProductById(
            @Parameter(description = "ID value for the product you need to retriev.", required = true)
            @PathVariable(PATH_VARIABLE_ID) final UUID id) {

            logger.info("Request Get product by id {}", id);
            Optional<Product> product = productService.getProductById(id);
            return ResponseEntity.ok().body(product.get());
    }

    @GetMapping(path=MAPPING_PATH_COST)
    @Operation(summary = "Gets cost all the products by ids.",
            description = "Provides a cost of specific product or products from the catalog.", tags = {"Products"})
    public ResponseEntity<BigDecimal> getProductsCostFilterByIds(
            @Parameter(description = "Finds cost of one or more products by ids. Use ',' as delimiter.", required = true)
            @RequestParam(name = REQUEST_PARAM_IDS) Set<UUID> ids) {

            logger.info("Request Get product by id with filter COST for {}", ids);
            BigDecimal cost = productService.getProductsCostByIds(ids);
            return ResponseEntity.ok(cost);
    }

    //@PreAuthorize("hasRole('admin')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Creates a new product.",
            description = "Creates a new product in the catalog.", tags = {"Products"},
            security = @SecurityRequirement(name = "BearerToken"))
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "400", description = "Something went wrong"),
//            @ApiResponse(responseCode = "403", description = "Access denied"),
//            @ApiResponse(responseCode = "404", description = "The client doesn't exist"),
//            @ApiResponse(responseCode = "500", description = "Expired or invalid JWT token")
//    })
    public ResponseEntity<Product> createNewProduct(
            @Parameter(description = "Valid product body.", required = true)
            @Valid @RequestBody final Product product) throws URISyntaxException {

            Product createdProduct = productService.saveProduct(product);
            logger.info("Request createNewProduct");
            return ResponseEntity.created(new URI("/catalog/products/" + createdProduct
                    .getId())).body(product);
    }

    @PreAuthorize("hasRole('admin')")
    @PutMapping(path=MAPPING_PATH_ID, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Updates a product.",
            description = "Fully updates an existing product in the catalog.", tags = {"Products"},
            security = @SecurityRequirement(name = "BearerToken"))
    public ResponseEntity<Product> replaceProduct(
            @Parameter(description = "Valid product body.", required = true) @Valid @RequestBody final Product newProduct,
            @Parameter(description = "ID value for the product you need to update.", required = true) @PathVariable(PATH_VARIABLE_ID) final UUID id) {

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

    @PreAuthorize("hasRole('admin')")
    @PatchMapping(path=MAPPING_PATH_ID, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Updates a product.",
            description = "Partially updates an existing product in the catalog.", tags = {"Products"},
            security = @SecurityRequirement(name = "BearerToken"))
    public ResponseEntity<Product> partialUpdateProduct(
            @Parameter(description = "Product fields to update.", required = true) @RequestBody final Product productUpdate,
            @Parameter(description = "ID value for the product you need to update.", required = true) @PathVariable(PATH_VARIABLE_ID) final UUID id) {

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

    @PreAuthorize("hasRole('admin')")
    @DeleteMapping(path=MAPPING_PATH_ID)
    @Operation(summary = "Deletes a product.",
            description = "Deletes an existing product in the catalog.", tags = {"Products"},
            security = @SecurityRequirement(name = "BearerToken"))
    public ResponseEntity<Void> deleteProductById(
            @Parameter(description = "ID value for the product you need to delete.", required = true)
            @PathVariable(PATH_VARIABLE_ID) final UUID id) {

            logger.info("Request deleteProductById for id: {}", id);
            productService.deleteProduct(id);
            return ResponseEntity.status(HttpStatus.OK).build();
    }
}
