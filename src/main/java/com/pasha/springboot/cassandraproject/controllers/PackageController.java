package com.pasha.springboot.cassandraproject.controllers;

import com.pasha.springboot.cassandraproject.domains.PackageBase;
import com.pasha.springboot.cassandraproject.domains.PackageCustom;
import com.pasha.springboot.cassandraproject.dto.PackageDto;
import com.pasha.springboot.cassandraproject.exceptions.ResourceNotFoundException;
import com.pasha.springboot.cassandraproject.services.PackageServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v0/catalog/packages")
public class PackageController {

    private static final Logger logger = LogManager.getLogger(ProductController.class.getSimpleName());

    private static final String URI_PACKAGES = "/catalog/packages/";

    @Autowired
    private PackageServiceImpl packageService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PackageCustom> createNewPackageCustom(@RequestBody PackageCustom packageCustom)
            throws URISyntaxException {
        logger.info("Request createNewPackageCustom");
        PackageCustom createdPack = packageService.createPackageCustom(packageCustom);
        return ResponseEntity.created(new URI(URI_PACKAGES + createdPack
                .getId())).body(packageCustom);
    }

    @PostMapping(value ="/base", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PackageBase> createNewPackageBase(@Valid @RequestBody final PackageBase packageBase)
            throws URISyntaxException {
        logger.info("Request createNewPackageBase");
        PackageBase createdPack = packageService.createPackageBase(packageBase);
        return ResponseEntity.created(new URI(URI_PACKAGES + createdPack
                .getId())).body(packageBase);
    }

    @GetMapping(path="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PackageCustom> findPackageById(@PathVariable("id") final UUID id) {
        logger.info("Request findPackageById");
        Optional<PackageCustom> getPackage = packageService.getPackageCustomById(id);
        return ResponseEntity.ok().body(getPackage.get());
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<PackageCustom>> getAllPackagesOrFilterByNames(
            @RequestParam(name = "filter", required = false) final String name) {
        logger.info("Request getAllPackagesOrFilterByNames");
        if(name == null) {
            return ResponseEntity.ok(packageService.getAllCustomPackages());
        }
        else {
            return ResponseEntity.ok(packageService.getAllCustomPackagesByName(name));
        }
    }

    @GetMapping(value = "/base", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<PackageBase>> getAllPackagesBaseOrFilterByNames() {
        logger.info("Request getAllPackagesBaseOrFilterByNames");
        return ResponseEntity.ok(packageService.getAllBasePackages());
    }

    @GetMapping(path="/base/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PackageBase> findPackageBaseById(@PathVariable("id") final UUID id) {
        logger.info("Request findPackageBaseById");
        Optional<PackageBase> getPackage = packageService.getPackageBaseById(id);
        return ResponseEntity.ok().body(getPackage.get());
    }

    @GetMapping(path="/{id}/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PackageDto> getInfoPackageById(@PathVariable("id") final UUID id) {
        logger.info("Request getInfoPackageById");
        PackageDto getPackage = packageService.getInfoPackageById(id);
        return ResponseEntity.ok().body(getPackage);
    }

    @GetMapping(path="/base/{id}/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PackageDto> getInfoBasePackageById(@PathVariable("id") final UUID id) {
        logger.info("Request getInfoBasePackageById");
        PackageDto getPackage = packageService.getInfoBasePackageById(id);
        return ResponseEntity.ok().body(getPackage);
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<Void> deletePackageById(@PathVariable("id") final UUID id) {
        logger.info("Request deletePackageById");
        packageService.deletePackage(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
