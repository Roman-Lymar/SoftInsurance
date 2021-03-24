package com.pasha.springboot.cassandraproject.services.model;

import com.pasha.springboot.cassandraproject.domains.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductModelService {
    private static List<Product> products = new ArrayList<>();

    public static List<Product> createListOfProducts() {
        Product product_1 = new Product();
        product_1.setId(UUID.fromString("b4ad2cdd-66b6-4431-951c-dac0d3c100c8"));
        product_1.setName("Product_1");
        product_1.setDescription("This is Product_1");
        product_1.setPrice(BigDecimal.valueOf(250.34));

        Product product_2 = new Product();
        product_1.setId(UUID.fromString("9aa62b99-b279-40c6-83d2-f83b74aa15ed"));
        product_1.setName("Product_2");
        product_1.setDescription("This is Product_2");
        product_1.setPrice(BigDecimal.valueOf(50.87));

        Product product_3 = new Product();
        product_1.setId(UUID.fromString("d9ebc39f-d9ee-4585-9356-b3cb004ad4be"));
        product_1.setName("Product_3");
        product_1.setDescription("This is Product_3");
        product_1.setPrice(BigDecimal.valueOf(998.00));

        products.add(product_1);
        products.add(product_2);
        products.add(product_3);

        return products;
    }
}
