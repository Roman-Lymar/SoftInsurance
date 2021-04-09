package com.springboot.cassandraproject.dto.roleuserdto;

import java.util.UUID;

public class UsersDto {

    private UUID id;
    private String role;

    public UsersDto() {
    }

    public UsersDto(UUID id, String role) {
        this.id = id;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UsersDto{" +
                "id=" + id +
                ", role='" + role + '\'' +
                '}';
    }
}
