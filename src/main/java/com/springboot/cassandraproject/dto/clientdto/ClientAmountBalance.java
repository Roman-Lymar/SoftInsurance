package com.springboot.cassandraproject.dto.clientdto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;

@Schema(description = "Amount balance details of the client.")
public class ClientAmountBalance implements Serializable {

    private BigDecimal amount = BigDecimal.ZERO;

    @JsonProperty("amount")
    public BigDecimal getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
