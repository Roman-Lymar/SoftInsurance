package com.pasha.springboot.cassandraproject.controllers;

import com.pasha.springboot.cassandraproject.domains.PackageBase;
import com.pasha.springboot.cassandraproject.domains.PackageCustom;
import com.pasha.springboot.cassandraproject.dto.PackageDto;
import com.pasha.springboot.cassandraproject.exceptions.ResourceNotFoundException;
import com.pasha.springboot.cassandraproject.services.PackageServiceImpl;
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

    @Autowired
    private PackageServiceImpl packageService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PackageCustom> createNewPackageCustom(@Valid @RequestBody final PackageCustom packageCustom)
            throws URISyntaxException {
        PackageCustom createdPack = packageService.createPackageCustom(packageCustom);
        return ResponseEntity.created(new URI("/catalog/products/" + createdPack
                .getId())).body(packageCustom);
    }

    @PostMapping(value ="/base", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PackageBase> createNewPackageBase(@Valid @RequestBody final PackageBase packageBase)
            throws URISyntaxException {
        PackageBase createdPack = packageService.createPackageBase(packageBase);
        return ResponseEntity.created(new URI("/catalog/packages/" + createdPack
                .getId())).body(packageBase);
    }

    @GetMapping(path="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PackageCustom> findPackageById(@PathVariable("id") final UUID id) {
        Optional<PackageCustom> getPackage = packageService.getPackageCustomById(id);
        return ResponseEntity.ok().body(getPackage.get());
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<PackageCustom>> getAllProductsOrFilterByNames(
            @RequestParam(name = "filter", required = false) final String name) {
        if(name == null) {
            return ResponseEntity.ok(packageService.getAllCustomPackages());
        }
        else {
            return ResponseEntity.ok(packageService.getAllCustomPackagesByName(name));
        }
    }

    @GetMapping(value = "/base", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<PackageBase>> getAllProductsOrFilterByNames() {
        return ResponseEntity.ok(packageService.getAllBasePackages());
    }

    @GetMapping(path="/base/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PackageBase> findPackageBaseById(@PathVariable("id") final UUID id) {
        Optional<PackageBase> getPackage = packageService.getPackageBaseById(id);
        return ResponseEntity.ok().body(getPackage.get());
    }

    @GetMapping(path="/{id}/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PackageDto> getInfoPackageById(@PathVariable("id") final UUID id) {
        PackageDto getPackage = packageService.getInfoPackageById(id);
        return ResponseEntity.ok().body(getPackage);
    }

    @GetMapping(path="/base/{id}/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PackageDto> getInfoBasePackageById(@PathVariable("id") final UUID id) {
        PackageDto getPackage = packageService.getInfoBasePackageById(id);
        return ResponseEntity.ok().body(getPackage);
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<Void> deletePackageById(@PathVariable("id") final UUID id) {
        packageService.deletePackage(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
