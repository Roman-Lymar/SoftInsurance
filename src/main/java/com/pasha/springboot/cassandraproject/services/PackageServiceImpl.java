package com.pasha.springboot.cassandraproject.services;

import com.pasha.springboot.cassandraproject.domains.PackageCustom;
import com.pasha.springboot.cassandraproject.repositories.PackageBaseRepository;
import com.pasha.springboot.cassandraproject.repositories.PackageCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
public class PackageServiceImpl {

    @Autowired
    private PackageCustomRepository packageCustomRepository;

    @Autowired
    private PackageBaseRepository packageBaseRepository;

    @Autowired
    private ProductServiceImpl productService;

    public PackageCustom createPackageCustom(PackageCustom packageCustom) {
        UUID uuid = UUID.randomUUID();
        if (Objects.isNull(packageCustom.getId())){
            packageCustom.setId(uuid);
        }

        Set<UUID> productIds = packageCustom.getProductIds();
        packageCustom.setPrice(productService.getProductsCostByIds(productIds));
        return packageCustomRepository.insert(packageCustom);
    }


    }


