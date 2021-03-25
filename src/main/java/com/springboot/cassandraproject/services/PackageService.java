package com.springboot.cassandraproject.services;

import com.springboot.cassandraproject.domains.PackageTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PackageService {
    Iterable<PackageTemplate> getAllPackages();
    Optional<PackageTemplate> getPackageById(UUID id);
    List<PackageTemplate> getPackagesBySearchString(String searchStr);
    PackageTemplate saveProduct(PackageTemplate aPackageTemplate);
    BigDecimal getCost(List<UUID> ids);
    void deletePackage(UUID id);
}
