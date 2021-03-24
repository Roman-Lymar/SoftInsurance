package com.pasha.springboot.cassandraproject.repositories;

import com.pasha.springboot.cassandraproject.domains.PackageBase;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RestResource(exported = false)
public interface PackageBaseRepository extends CassandraRepository<PackageBase, UUID> {
}
