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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PackageServiceImpl {

    private static final Logger logger = LogManager.getLogger(PackageServiceImpl.class.getSimpleName());

    @Autowired
    private PackageCustomRepository packageCustomRepository;

    @Autowired
    private PackageBaseRepository packageBaseRepository;

    @Autowired
    private ProductServiceImpl productService;

    public PackageCustom createPackageCustom(PackageCustom packageCustom) {
        logger.info("Method createPackageCustom called");
        UUID uuid = UUID.randomUUID();
        if (Objects.isNull(packageCustom.getId())) {
            packageCustom.setId(uuid);
        }
        logger.warn("Package cannot be created. EMPTY_PACKAGE ");
        Set<UUID> productIds = packageCustom.getProductIds();
        if(productIds.isEmpty()){

            throw new EmptyPackageException(ErrorMessages.EMPTY_PACKAGE.getErrorMessage());
        }
        packageCustom.setPrice(productService.getProductsCostByIds(productIds));
        logger.info("Package" + packageCustom.getName() + "created" );
        return packageCustomRepository.insert(packageCustom);
    }

    public PackageBase createPackageBase(PackageBase packageBase) {
        logger.info("Method createPackageBase called");
        UUID uuid = UUID.randomUUID();
        if (Objects.isNull(packageBase.getId())) {
            packageBase.setId(uuid);
        }
        Set<UUID> productIds = packageBase.getProductIds();
        packageBase.setPrice(productService.getProductsCostByIds(productIds));
        logger.info("Base package created");
        return packageBaseRepository.insert(packageBase);
    }

    public Iterable<PackageCustom> getAllCustomPackages() {
        logger.info("Method getAllCustomPackages called");
        logger.info("Custom Packages successfully returned");
        return packageCustomRepository.findAll();
    }

    public List<PackageCustom> getAllCustomPackagesByName(String name) {
        logger.info("Method getAllCustomPackagesByName called");
        List<PackageCustom> packageCustoms = packageCustomRepository.findAll();
        List<PackageCustom> resultList = new ArrayList<>();

        for (PackageCustom p : packageCustoms) {
            if (p.getName().contains(name) || p.getDescription().contains(name)) {
                resultList.add(p);
            }
        }
        if(resultList.isEmpty()){
            logger.warn("None results by filter: "+ name + " NO_RESOURCE_FOUND_BY_NAME");
            throw new ResourceNotFoundException(ErrorMessages.NO_RESOURCE_FOUND_BY_NAME.getErrorMessage()
                    +name);
        }else
        logger.info("Custom Packages successfully returned with filter: " + name);
        return resultList;
    }


    public Optional<PackageCustom> getPackageCustomById(UUID id) {
        logger.info("Method getPackageCustomById called");
        Optional<PackageCustom> fetchedPack = Optional.ofNullable(packageCustomRepository.findById(id).
                orElseThrow(()-> new ResourceNotFoundException(
                        ErrorMessages.NO_RESOURCE_FOUND.getErrorMessage() + id)));
        logger.info("Custom Package successfully returned for: "+ id );
        return fetchedPack;
    }

    public Optional<PackageBase> getPackageBaseById(UUID id) {
        logger.info("Method getPackageBaseById called");
        Optional<PackageBase> fetchedPack = Optional.ofNullable(packageBaseRepository.findById(id).
                orElseThrow(()-> new ResourceNotFoundException(
                        ErrorMessages.NO_RESOURCE_FOUND.getErrorMessage() + id)));
        logger.info("Base package successfully returned for: "+ id );
        return fetchedPack;
    }

    public PackageDto getInfoPackageById(UUID id) {
        logger.info("Method getInfoPackageById called");
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
        logger.info("Package INFO successfully returned for: "+ id );
        return packageDto;
    }

    public PackageDto getInfoBasePackageById(UUID id) {
        logger.info("Method getInfoBasePackageById called");

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
        logger.info("Base package INFO successfully returned for: "+ id );
        return packageDto;
    }

    public void deletePackage(UUID id) {
        logger.info("Method deletePackage called");
        if(getPackageCustomById(id).isPresent()){
            packageCustomRepository.deleteById(id);
            logger.info("Package " + id + " was successfully deleted");
        }
    }

    public Iterable<PackageBase> getAllBasePackages(){
        logger.info("Method getAllBasePackages called");
        logger.info("All Base packages successfully returned");
        return packageBaseRepository.findAll();
    }
}
