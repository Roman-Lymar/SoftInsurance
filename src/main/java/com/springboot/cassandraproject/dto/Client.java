package com.springboot.cassandraproject.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Details about the client.")
public class Client implements Serializable {

    private UUID id;
    private String name;
    private BigDecimal balance = BigDecimal.ZERO;
    private UUID packageId;

    public Client() {
    }

    public Client(UUID id, String name, BigDecimal balance, UUID packageId) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.packageId = packageId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @JsonProperty("package")
    public UUID getPackageId() {
        return packageId;
    }

    @JsonProperty("package")
    public void setPackageId(UUID packageId) {
        this.packageId = packageId;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                ", packageId=" + packageId +
                '}';
    }
}
