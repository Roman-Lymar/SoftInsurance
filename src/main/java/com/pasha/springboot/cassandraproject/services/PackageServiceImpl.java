package com.pasha.springboot.cassandraproject.services;

import com.pasha.springboot.cassandraproject.domains.PackageBase;
import com.pasha.springboot.cassandraproject.domains.PackageCustom;
import com.pasha.springboot.cassandraproject.domains.Product;
import com.pasha.springboot.cassandraproject.dto.PackageDto;
import com.pasha.springboot.cassandraproject.exceptions.EmptyPackageException;
import com.pasha.springboot.cassandraproject.exceptions.ErrorMessages;
import com.pasha.springboot.cassandraproject.exceptions.ResourceNotFoundException;
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
        if(productIds.isEmpty()){
            throw new EmptyPackageException(ErrorMessages.EMPTY_PACKAGE.getErrorMessage());
        }
        packageCustom.setPrice(productService.getProductsCostByIds(productIds));
        return packageCustomRepository.insert(packageCustom);
    }

    public PackageBase createPackageBase(PackageBase packageBase) {
        UUID uuid = UUID.randomUUID();
        if (Objects.isNull(packageBase.getId())) {
            packageBase.setId(uuid);
        }

        Set<UUID> productIds = packageBase.getProductIds();
        packageBase.setPrice(productService.getProductsCostByIds(productIds));
        return packageBaseRepository.insert(packageBase);
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
        if(resultList.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.NO_RESOURCE_FOUND_BY_NAME.getErrorMessage()
                    +name);
        }

        return resultList;
    }

    public Optional<PackageCustom> getPackageCustomById(UUID id) {
        Optional<PackageCustom> fetchedPack = Optional.ofNullable(packageCustomRepository.findById(id).
                orElseThrow(()-> new ResourceNotFoundException(
                        ErrorMessages.NO_RESOURCE_FOUND.getErrorMessage() + id)));
        return fetchedPack;
    }

    public Optional<PackageBase> getPackageBaseById(UUID id) {
        Optional<PackageBase> fetchedPack = Optional.ofNullable(packageBaseRepository.findById(id).
                orElseThrow(()-> new ResourceNotFoundException(
                        ErrorMessages.NO_RESOURCE_FOUND.getErrorMessage() + id)));
        return fetchedPack;
    }

    public PackageDto getInfoPackageById(UUID id) {
        PackageCustom packageCustom = getPackageCustomById(id).get();

        List<Product> products = new ArrayList<>();
        for (UUID uuid : packageCustom.getProductIds()) {
            products.add(productService.getProductById(uuid).get());
        }

        PackageDto packageDto = new PackageDto();
        packageDto.setId(packageCustom.getId());
        packageDto.setName(packageCustom.getName());
        packageDto.setDescription(packageCustom.getDescription());
        packageDto.setPrice(packageCustom.getPrice());
        packageDto.setCreatedTime(packageCustom.getCreatedTime());
        packageDto.setProductList(products);
        return packageDto;
    }

    public PackageDto getInfoBasePackageById(UUID id) {
        PackageBase packageBase = getPackageBaseById(id).get();

        List<Product> products = new ArrayList<>();
        for (UUID uuid : packageBase.getProductIds()) {
            products.add(productService.getProductById(uuid).get());
        }

        PackageDto packageDto = new PackageDto();
        packageDto.setId(packageBase.getId());
        packageDto.setName(packageBase.getName());
        packageDto.setDescription(packageBase.getDescription());
        packageDto.setPrice(packageBase.getPrice());
        packageDto.setCreatedTime(packageBase.getCreatedTime());
        packageDto.setProductList(products);
        return packageDto;
    }

    public void deletePackage(UUID id) {
        if(getPackageCustomById(id).isPresent()){
            packageCustomRepository.deleteById(id);
        }
    }

    public Iterable<PackageBase> getAllBasePackages(){
        return packageBaseRepository.findAll();
    }
}
