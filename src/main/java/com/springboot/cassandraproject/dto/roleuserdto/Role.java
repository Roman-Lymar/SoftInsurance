package com.springboot.cassandraproject.dto.roleuserdto;

public class Role {

    private EnumRole name;

    public Role() {
    }

    public Role(EnumRole name) {
        this.name = name;
    }

    public EnumRole getName() {
        return name;
    }

    public void setName(EnumRole name) {
        this.name = name;
    }
}
