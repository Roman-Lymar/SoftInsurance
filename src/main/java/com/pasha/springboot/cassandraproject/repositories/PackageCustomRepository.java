package com.pasha.springboot.cassandraproject.repositories;

import com.pasha.springboot.cassandraproject.domains.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PackageCustomRepository extends CrudRepository<Product, UUID> {
}
