package com.springboot.cassandraproject.domains;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Table("product")
public class Product implements Serializable, Comparable<Product>{

    @PrimaryKey
    @Column("id")
    private UUID id;

    @Column("name")
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Column("description")
    @NotBlank(message = "Description cannot be empty")
    private String description;

    @Column("price")
    private BigDecimal price = BigDecimal.ZERO;

    public Product() {
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

    @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return this.id.equals(product.id) &&
                    this.name.equals(product.name) &&
                    this.description.equals(product.description) &&
                    this.price.equals(product.price);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.id, this.name, this.description, this.price);
    }

    @Override
    public String toString() {
        return "Product: " +
                "id=" + this.id +
                ", name='" + this.name + '\'' +
                ", description='" + this.description + '\'' +
                ", price=" + this.price;
    }

    @Override
    public int compareTo(Product o) {
        if(this.getName() == null) {
            this.setName(StringUtils.EMPTY);
        }
        return this.getName().compareTo(o.getName());
    }
}
