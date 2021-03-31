package com.springboot.cassandraproject.repositories;

import com.springboot.cassandraproject.domains.PackageCustom;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RepositoryRestResource(exported = false)
public interface PackageCustomRepository extends CassandraRepository<PackageCustom, UUID> {
}
