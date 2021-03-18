package com.pasha.springboot.cassandraproject.controllers;

import com.pasha.springboot.cassandraproject.domains.Product;
import com.pasha.springboot.cassandraproject.exceptions.BadResourceException;
import com.pasha.springboot.cassandraproject.exceptions.ResourceAlreadyExistsException;
import com.pasha.springboot.cassandraproject.exceptions.ResourceNotFoundException;
import com.pasha.springboot.cassandraproject.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/v0/catalog/products")
public class ProductController {

    //private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ProductService productService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Product>> getAllProductsOrFilterByNames(
            @RequestParam(name = "filter", required = false) final List<String> names) {
        if(names == null) {
            return ResponseEntity.ok(productService.getAllProducts());
        }
        else {
            return ResponseEntity.ok(productService.getProductsByNames(names));
        }
    }

    @GetMapping(path="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> findProductById(@PathVariable("id") final UUID id) {
        try {
            Optional<Product> product = productService.getProductById(id);
            return ResponseEntity.ok(product.get());
        } catch (ResourceNotFoundException e) {
            //logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping(path="/cost")
    public ResponseEntity<BigDecimal> getProductsCostFilterByIds(
            @RequestParam(name = "filter") UUID ids) {
        try {
            BigDecimal cost = productService.getProductsCostByIds(Set.of(ids));
            return ResponseEntity.ok(cost);
        } catch (ResourceNotFoundException e) {
            //logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> createNewProduct(@RequestBody final Product product)
            throws URISyntaxException {
        try {
            Product createdProduct = productService.saveProduct(product);
            return ResponseEntity.created(new URI("/catalog/products/" + createdProduct.getId()))
                    .body(product);
        } catch (ResourceAlreadyExistsException e) {
            //logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (BadResourceException e) {
            //logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping(path="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> replaceProduct(@RequestBody final Product newProduct,
                                                 @PathVariable("id") final UUID id) {
        try {
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
        } catch (ResourceNotFoundException e) {
            //logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (BadResourceException e) {
            //logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PatchMapping(path="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> partialUpdateProduct(@RequestBody final Product productUpdate,
                                                     @PathVariable("id") final UUID id) {
        try {
            Optional<Product> productOptional = productService.getProductById(id);
            if (productOptional.isEmpty()) {
                throw new ResourceNotFoundException();
            }

            Product product = productOptional.get();
            if (productUpdate.getName() != null) {
                product.setName(productUpdate.getName());
            }
            if (productUpdate.getDescription() != null) {
                product.setDescription(productUpdate.getDescription());
            }
            if (productUpdate.getPrice() != BigDecimal.ZERO) {
                product.setPrice(productUpdate.getPrice());
            }

            productService.saveProduct(product);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ResourceNotFoundException e) {
            //logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable("id") final UUID id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ResourceNotFoundException e) {
            //logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
