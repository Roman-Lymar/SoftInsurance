package com.pasha.springboot.cassandraproject.services;

import com.pasha.springboot.cassandraproject.domains.Package;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PackageService {
    Iterable<Package> getAllPackages();
    Optional<Package> getPackageById(UUID id);
    List<Package> getPackagesByNames(List<String> names);
    Package saveProduct(Package aPackage);
    BigDecimal getCost(List<UUID> ids);
    void deleteProduct(UUID id);
}
