package com.springboot.cassandraproject.dto.clientdto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

@Schema(description = "Only name details of the client.")
public class ClientOnlyNameBody implements Serializable {

    private String name;

    public ClientOnlyNameBody() {
    }

    public ClientOnlyNameBody(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
