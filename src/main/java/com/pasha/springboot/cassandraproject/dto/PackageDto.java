package com.pasha.springboot.cassandraproject.dto;

import com.pasha.springboot.cassandraproject.domains.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PackageDto {

    private UUID id;

    private String name;

    private String description;

    private BigDecimal price;

    private LocalDateTime createdTime;

    private Collection<Product> productList;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public Collection<Product> getProductList(){
        return productList;
    }

    public void setProductList(Collection<Product> productList) {
        this.productList = productList;
    }
}
