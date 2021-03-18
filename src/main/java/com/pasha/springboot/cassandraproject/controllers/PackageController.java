package com.pasha.springboot.cassandraproject.controllers;

import com.pasha.springboot.cassandraproject.domains.PackageCustom;
import com.pasha.springboot.cassandraproject.domains.Product;
import com.pasha.springboot.cassandraproject.dto.PackageDto;
import com.pasha.springboot.cassandraproject.exceptions.ResourceNotFoundException;
import com.pasha.springboot.cassandraproject.services.PackageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v0/catalog/packages")
public class PackageController {

    @Autowired
    private PackageServiceImpl packageService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PackageCustom> createNewPackageCustom(@RequestBody PackageCustom packageCustom) {
        return new ResponseEntity<>(packageService.createPackageCustom(packageCustom), HttpStatus.CREATED);
    }

    @GetMapping(path="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PackageCustom> findPackageById(@PathVariable("id") final UUID id) {
        try {
            Optional<PackageCustom> getPackage = packageService.getPackageCustomById(id);
            return ResponseEntity.ok(getPackage.get());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
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

    @GetMapping(path="/{id}/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PackageDto> getInfoPackageById(@PathVariable("id") final UUID id) {
        try {
            PackageDto getPackage = packageService.getInfoPackageById(id);
            return ResponseEntity.ok(getPackage);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
