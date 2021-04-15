package com.springboot.cassandraproject.controllers;

import com.springboot.cassandraproject.domains.PackageBase;
import com.springboot.cassandraproject.domains.PackageCustom;
import com.springboot.cassandraproject.dto.PackageDto;
import com.springboot.cassandraproject.services.PackageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/catalog/packages")
public class PackageController {

    private static final Logger logger = LogManager.getLogger(ProductController.class.getSimpleName());
    private static final String PATH_VARIABLE_ID = "id";
    private static final String MAPPING_PATH_ID = "/{id}";
    private static final String MAPPING_PATH_BASE = "/base";
    private static final String MAPPING_PATH_BASE_ID = "/base/{id}";
    private static final String MAPPING_PATH_BASE_ID_INFO = "/base/{id}/info";
    private static final String MAPPING_PATH_CUSTOM_ID_INFO = "/{id}/info";
    private static final String REQUEST_PARAM_FILTER = "filter";
    private static final String URI_PACKAGES = "/catalog/packages/";

    @Autowired
    private PackageService packageService;

    @PreAuthorize("hasAnyAuthority('admin', 'client')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PackageCustom> createNewPackageCustom(
            @Valid @RequestBody PackageCustom packageCustom) throws URISyntaxException {

        logger.info("Request createNewPackageCustom");
        PackageCustom createdPack = packageService.createPackageCustom(packageCustom);
        return ResponseEntity.created(new URI(URI_PACKAGES + createdPack
                .getId())).body(packageCustom);
    }

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping(value = MAPPING_PATH_BASE, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PackageBase> createNewPackageBase(
            @Valid @RequestBody final PackageBase packageBase) throws URISyntaxException {

        logger.info("Request createNewPackageBase");
        PackageBase createdPack = packageService.createPackageBase(packageBase);
        return ResponseEntity.created(new URI(URI_PACKAGES + createdPack
                .getId())).body(packageBase);
    }

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping(path = MAPPING_PATH_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PackageCustom> findPackageById(@PathVariable(PATH_VARIABLE_ID) final UUID id) {

        logger.info("Request findPackageById");
        Optional<PackageCustom> getPackage = packageService.getPackageCustomById(id);
        return ResponseEntity.ok().body(getPackage.get());
    }

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<PackageCustom>> getAllPackagesCustomOrFilterBySearchString(
            @RequestParam(name = REQUEST_PARAM_FILTER, required = false) final String searchStr) {

        logger.info("Request getAllPackagesOrFilterByNames");
        if(searchStr == null) {
            return ResponseEntity.ok(packageService.getAllCustomPackages());
        }
        else {
            return ResponseEntity.ok(packageService.getAllCustomPackagesBySearchString(searchStr));
        }
    }

    @GetMapping(value = MAPPING_PATH_BASE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<PackageBase>> getAllPackagesBase() {

        logger.info("Request getAllPackagesBaseOrFilterByNames");
        return ResponseEntity.ok(packageService.getAllBasePackages());
    }

    @GetMapping(path = MAPPING_PATH_BASE_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PackageBase> findPackageBaseById(
            @PathVariable(PATH_VARIABLE_ID) final UUID id) {

        logger.info("Request findPackageBaseById");
        Optional<PackageBase> getPackage = packageService.getPackageBaseById(id);
        return ResponseEntity.ok().body(getPackage.get());
    }

    @PreAuthorize("hasAnyAuthority('admin', 'client')")
    @GetMapping(path = MAPPING_PATH_CUSTOM_ID_INFO, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PackageDto> getInfoPackageById(@PathVariable(PATH_VARIABLE_ID) final UUID id) {

        logger.info("Request getInfoPackageById");
        PackageDto getPackage = packageService.getInfoPackageById(id);
        return ResponseEntity.ok().body(getPackage);
    }

    @GetMapping(path = MAPPING_PATH_BASE_ID_INFO, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PackageDto> getInfoBasePackageById(@PathVariable(PATH_VARIABLE_ID) final UUID id) {

        logger.info("Request getInfoBasePackageById");
        PackageDto getPackage = packageService.getInfoBasePackageById(id);
        return ResponseEntity.ok().body(getPackage);
    }

    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping(path = MAPPING_PATH_ID)
    public ResponseEntity<Void> deletePackageById(@PathVariable(PATH_VARIABLE_ID) final UUID id) {

        logger.info("Request deletePackageById");
        packageService.deletePackage(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
