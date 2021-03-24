package com.pasha.springboot.cassandraproject.repositories;

import com.pasha.springboot.cassandraproject.domains.Product;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RestResource(exported = false)
public interface ProductRepository extends CassandraRepository<Product, UUID> {
}
