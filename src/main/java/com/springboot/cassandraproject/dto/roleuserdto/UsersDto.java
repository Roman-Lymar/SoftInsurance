package com.springboot.cassandraproject.dto.roleuserdto;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class UsersDto {

    private UUID id;
    private Set<Role> roles = new HashSet<>();

    public UsersDto(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
