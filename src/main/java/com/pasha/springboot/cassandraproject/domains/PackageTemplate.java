package com.pasha.springboot.cassandraproject.domains;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public abstract class PackageTemplate implements Serializable, Comparable<PackageTemplate> {

    @PrimaryKey
    @Column("id")
    private UUID id;

    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @Column("price")
    private BigDecimal price = BigDecimal.ZERO;

    @Column("createdTime")
    private LocalDateTime createdTime = LocalDateTime.now();

    @Column("productIds")
    private Set<UUID> productIds = new HashSet<>();

    public PackageTemplate() {
        id = UUID.randomUUID();
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PackageTemplate aPackageTemplate = (PackageTemplate) o;
        return this.id.equals(aPackageTemplate.id) &&
                this.name.equals(aPackageTemplate.name) &&
                this.description.equals(aPackageTemplate.description) &&
                this.price.equals(aPackageTemplate.price) &&
                this.createdTime.equals(aPackageTemplate.createdTime) &&
                this.productIds.equals(aPackageTemplate.productIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.description, this.price, this.createdTime, this.productIds);
    }

    @Override
    public String toString() {
        return "Package: " +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", createdTime=" + createdTime +
                ", productIds=" + productIds;
    }

    @Override
    public int compareTo(PackageTemplate o) {
        if(this.getName() == null) {
            this.setName(StringUtils.EMPTY);
        }
        return this.getName().compareTo(o.getName());
    }
}
