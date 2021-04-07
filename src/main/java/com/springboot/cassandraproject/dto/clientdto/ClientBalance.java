package com.springboot.cassandraproject.dto.clientdto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;

@Schema(description = "Balance details of the client.")
public class ClientBalance implements Serializable {

    private BigDecimal balance = BigDecimal.ZERO;

    @JsonProperty("balance")
    public BigDecimal getBalance() {
        return balance;
    }

    @JsonProperty("balance")
    public void setBalance(BigDecimal amount) {
        this.balance = amount;
    }
}
