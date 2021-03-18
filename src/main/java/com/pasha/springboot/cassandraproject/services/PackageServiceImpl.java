package com.pasha.springboot.cassandraproject.services;

import com.pasha.springboot.cassandraproject.domains.PackageCustom;
import com.pasha.springboot.cassandraproject.domains.Product;
import com.pasha.springboot.cassandraproject.dto.PackageDto;
import com.pasha.springboot.cassandraproject.repositories.PackageBaseRepository;
import com.pasha.springboot.cassandraproject.repositories.PackageCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
        if (Objects.isNull(packageCustom.getId())) {
            packageCustom.setId(uuid);
        }

        Set<UUID> productIds = packageCustom.getProductIds();
        packageCustom.setPrice(productService.getProductsCostByIds(productIds));
        return packageCustomRepository.insert(packageCustom);
    }

    public Iterable<PackageCustom> getAllCustomPackages() {
        return packageCustomRepository.findAll();
    }

    public List<PackageCustom> getAllCustomPackagesByName(String name) {
        List<PackageCustom> packageCustoms = packageCustomRepository.findAll();
        List<PackageCustom> resultList = new ArrayList<>();

        for (PackageCustom p : packageCustoms) {
            if (p.getName().contains(name) || p.getDescription().contains(name)) {
                resultList.add(p);
            }
        }

        return resultList;
    }

    public Optional<PackageCustom> getPackageCustomById(UUID id) {
        return packageCustomRepository.findById(id);
    }

    public PackageDto getInfoPackageById(UUID id) {
        PackageCustom packageCustom = packageCustomRepository.findById(id).get();

        Collection<Product> products = new ArrayList<>();
        for (UUID uuid : packageCustom.getProductIds()) {
            products.add(productService.getProductById(uuid).get());
        }

        PackageDto packageDto = new PackageDto();
        packageDto.setId(packageCustom.getId());
        packageDto.setName(packageCustom.getName());
        packageDto.setDescription(packageCustom.getDescription());
        packageDto.setPrice(packageCustom.getPrice());
        ;
        packageDto.setCreatedTime(packageCustom.getCreatedTime());
        packageDto.setProductList(products);
        return packageDto;
    }

    public void deletePackage(UUID id) {
        packageCustomRepository.deleteById(id);
    }

}


