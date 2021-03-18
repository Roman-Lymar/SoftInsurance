package com.pasha.springboot.cassandraproject.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PackageDto {

    private UUID id;

    private String name;

    private String description;

    private BigDecimal price = BigDecimal.ZERO;

    private LocalDateTime createdTime = LocalDateTime.now();

    private Set<UUID> productIds = new HashSet<>();

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

    public Set<UUID> getProductIds() {
        return productIds;
    }

    public void setProductIds(Set<UUID> productIds) {
        this.productIds = productIds;
    }
}
