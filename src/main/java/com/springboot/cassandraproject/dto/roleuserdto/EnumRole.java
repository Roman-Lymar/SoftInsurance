package com.springboot.cassandraproject.dto.roleuserdto;

public enum EnumRole {
    CLIENT("client"),
    ADMIN("admin");

    EnumRole(String role) {
        this.role = role;
    }

    private String role;

    public String getRole() {
        return role;
    }
}
