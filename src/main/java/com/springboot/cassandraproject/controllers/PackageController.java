package com.springboot.cassandraproject.controllers;

import com.springboot.cassandraproject.domains.PackageBase;
import com.springboot.cassandraproject.domains.PackageCustom;
import com.springboot.cassandraproject.dto.PackageDto;
import com.springboot.cassandraproject.services.PackageServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(tags = {"Operations available with insurance packages"})
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
    private PackageServiceImpl packageService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Creates a new custom package.",
            notes = "Creates a new custom package in the catalog.",
            response = PackageCustom.class)
    public ResponseEntity<PackageCustom> createNewPackageCustom(
            @ApiParam(value = "Valid package body.", required = true)
            @Valid @RequestBody PackageCustom packageCustom) throws URISyntaxException {

        logger.info("Request createNewPackageCustom");
        PackageCustom createdPack = packageService.createPackageCustom(packageCustom);
        return ResponseEntity.created(new URI(URI_PACKAGES + createdPack
                .getId())).body(packageCustom);
    }

    @PostMapping(value = MAPPING_PATH_BASE, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Creates a new base package.",
            notes = "Creates a new base package in the catalog.",
            response = PackageBase.class)
    public ResponseEntity<PackageBase> createNewPackageBase(
            @ApiParam(value = "Valid package body.", required = true)
            @Valid @RequestBody final PackageBase packageBase) throws URISyntaxException {

        logger.info("Request createNewPackageBase");
        PackageBase createdPack = packageService.createPackageBase(packageBase);
        return ResponseEntity.created(new URI(URI_PACKAGES + createdPack
                .getId())).body(packageBase);
    }

    @GetMapping(path = MAPPING_PATH_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Gets a custom package by id.",
            notes = "Provides an id to look specific custom package from the catalog.",
            response = PackageCustom.class)
    public ResponseEntity<PackageCustom> findPackageById(
            @ApiParam(value = "ID value for the custom package you need to retriev.", required = true)
            @PathVariable(PATH_VARIABLE_ID) final UUID id) {

        logger.info("Request findPackageById");
        Optional<PackageCustom> getPackage = packageService.getPackageCustomById(id);
        return ResponseEntity.ok().body(getPackage.get());
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Gets all the custom packages.",
            notes = "View a list of available custom packages in the catalog.")
    public ResponseEntity<Iterable<PackageCustom>> getAllPackagesCustomOrFilterBySearchString(
            @ApiParam(value = "Filter uses substrig as a parametr and look for any matches with custom packages fields \"name\" and/or \"description\".")
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
    @ApiOperation(value = "Gets all the base packages.",
            notes = "View a list of available base packages in the catalog.")
    public ResponseEntity<Iterable<PackageBase>> getAllPackagesBase() {

        logger.info("Request getAllPackagesBaseOrFilterByNames");
        return ResponseEntity.ok(packageService.getAllBasePackages());
    }

    @GetMapping(path = MAPPING_PATH_BASE_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Gets a base package by id.",
            notes = "Provides an id to look specific base package from the catalog.",
            response = PackageCustom.class)
    public ResponseEntity<PackageBase> findPackageBaseById(
            @ApiParam(value = "ID value for the base package you need to retriev.", required = true)
            @PathVariable(PATH_VARIABLE_ID) final UUID id) {

        logger.info("Request findPackageBaseById");
        Optional<PackageBase> getPackage = packageService.getPackageBaseById(id);
        return ResponseEntity.ok().body(getPackage.get());
    }

    @GetMapping(path = MAPPING_PATH_CUSTOM_ID_INFO, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Gets a full custom package info with products.",
            notes = "Provides an id to look specific custom package from the catalog.",
            response = PackageDto.class)
    public ResponseEntity<PackageDto> getInfoPackageById(
            @ApiParam(value = "ID value for the custom package you need to retriev.", required = true)
            @PathVariable(PATH_VARIABLE_ID) final UUID id) {

        logger.info("Request getInfoPackageById");
        PackageDto getPackage = packageService.getInfoPackageById(id);
        return ResponseEntity.ok().body(getPackage);
    }

    @GetMapping(path = MAPPING_PATH_BASE_ID_INFO, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Gets a full base package info with products.",
            notes = "Provides an id to look specific base package from the catalog.",
            response = PackageDto.class)
    public ResponseEntity<PackageDto> getInfoBasePackageById(
            @ApiParam(value = "ID value for the base package you need to retriev.", required = true)
            @PathVariable(PATH_VARIABLE_ID) final UUID id) {

        logger.info("Request getInfoBasePackageById");
        PackageDto getPackage = packageService.getInfoBasePackageById(id);
        return ResponseEntity.ok().body(getPackage);
    }

    @DeleteMapping(path = MAPPING_PATH_ID)
    @ApiOperation(value = "Deletes a package.",
            notes = "Deletes an existing package in the catalog.")
    public ResponseEntity<Void> deletePackageById(
            @ApiParam(value = "ID value for the package you need to delete.", required = true)
            @PathVariable(PATH_VARIABLE_ID) final UUID id) {

        logger.info("Request deletePackageById");
        packageService.deletePackage(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
