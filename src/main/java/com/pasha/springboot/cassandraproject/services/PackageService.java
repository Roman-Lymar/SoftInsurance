package com.pasha.springboot.cassandraproject.services;

import com.pasha.springboot.cassandraproject.domains.PackageTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PackageService {
    Iterable<PackageTemplate> getAllPackages();
    Optional<PackageTemplate> getPackageById(UUID id);
    List<PackageTemplate> getPackagesByNames(List<String> names);
    PackageTemplate saveProduct(PackageTemplate aPackageTemplate);
    BigDecimal getCost(List<UUID> ids);
    void deleteProduct(UUID id);
}
