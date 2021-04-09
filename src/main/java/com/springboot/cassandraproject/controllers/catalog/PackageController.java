package com.springboot.cassandraproject.controllers.catalog;

import com.springboot.cassandraproject.domains.PackageBase;
import com.springboot.cassandraproject.domains.PackageCustom;
import com.springboot.cassandraproject.dto.PackageDto;
import com.springboot.cassandraproject.services.PackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Packages", description = "Operations available with packages")
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

    @Operation(summary = "Creates a new custom package.",
            description = "Creates a new custom package in the catalog.", tags = {"Packages"},
            security = @SecurityRequirement(name = "BearerToken"))
    @PreAuthorize("hasAnyAuthority('admin', 'client')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PackageCustom> createNewPackageCustom(
            @Parameter(description = "Valid package body.", required = true)
            @Valid @RequestBody PackageCustom packageCustom) throws URISyntaxException {

        System.out.println("POST package");
        logger.info("Request createNewPackageCustom");
        PackageCustom createdPack = packageService.createPackageCustom(packageCustom);
        return ResponseEntity.created(new URI(URI_PACKAGES + createdPack
                .getId())).body(packageCustom);
    }

    @Operation(summary = "Creates a new base package.",
            description = "Creates a new base package in the catalog.", tags = {"Packages"},
            security = @SecurityRequirement(name = "BearerToken"))
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping(value = MAPPING_PATH_BASE, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PackageBase> createNewPackageBase(
            @Parameter(description = "Valid package body.", required = true)
            @Valid @RequestBody final PackageBase packageBase) throws URISyntaxException {

        logger.info("Request createNewPackageBase");
        PackageBase createdPack = packageService.createPackageBase(packageBase);
        return ResponseEntity.created(new URI(URI_PACKAGES + createdPack
                .getId())).body(packageBase);
    }

    @Operation(summary = "Gets a custom package by id.",
            description = "Provides an id to look specific custom package from the catalog.", tags = {"Packages"},
            security = @SecurityRequirement(name = "BearerToken"))
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping(path = MAPPING_PATH_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PackageCustom> findPackageById(
            @Parameter(description = "ID value for the custom package you need to retriev.", required = true)
            @PathVariable(PATH_VARIABLE_ID) final UUID id) {

        logger.info("Request findPackageById");
        Optional<PackageCustom> getPackage = packageService.getPackageCustomById(id);
        return ResponseEntity.ok().body(getPackage.get());
    }

    @Operation(summary = "Gets all the custom packages.",
            description = "View a list of available custom packages in the catalog.", tags = {"Packages"},
            security = @SecurityRequirement(name = "BearerToken"))
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<PackageCustom>> getAllPackagesCustomOrFilterBySearchString(
            @Parameter(description = "Filter uses substrig as a parametr and look for any matches with custom packages fields \"name\" and/or \"description\".")
            @RequestParam(name = REQUEST_PARAM_FILTER, required = false) final String searchStr) {

        logger.info("Request getAllPackagesOrFilterByNames");
        if(searchStr == null) {
            return ResponseEntity.ok(packageService.getAllCustomPackages());
        }
        else {
            return ResponseEntity.ok(packageService.getAllCustomPackagesBySearchString(searchStr));
        }
    }

    @Operation(summary = "Gets all the base packages.",
            description = "View a list of available base packages in the catalog.")
    @GetMapping(value = MAPPING_PATH_BASE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<PackageBase>> getAllPackagesBase() {

        logger.info("Request getAllPackagesBaseOrFilterByNames");
        return ResponseEntity.ok(packageService.getAllBasePackages());
    }

    @Operation(summary = "Gets a base package by id.",
            description = "Provides an id to look specific base package from the catalog.", tags = {"Packages"})
    @GetMapping(path = MAPPING_PATH_BASE_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PackageBase> findPackageBaseById(
            @Parameter(description = "ID value for the base package you need to retriev.", required = true)
            @PathVariable(PATH_VARIABLE_ID) final UUID id) {

        logger.info("Request findPackageBaseById");
        Optional<PackageBase> getPackage = packageService.getPackageBaseById(id);
        return ResponseEntity.ok().body(getPackage.get());
    }

    @Operation(summary = "Gets a full custom package info with products.",
            description = "Provides an id to look specific custom package from the catalog.", tags = {"Packages"},
            security = @SecurityRequirement(name = "BearerToken"))
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping(path = MAPPING_PATH_CUSTOM_ID_INFO, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PackageDto> getInfoPackageById(
            @Parameter(description = "ID value for the custom package you need to retriev.", required = true)
            @PathVariable(PATH_VARIABLE_ID) final UUID id) {

        logger.info("Request getInfoPackageById");
        PackageDto getPackage = packageService.getInfoPackageById(id);
        return ResponseEntity.ok().body(getPackage);
    }

    @Operation(summary = "Gets a full base package info with products.",
            description = "Provides an id to look specific base package from the catalog.", tags = {"Packages"})
    @GetMapping(path = MAPPING_PATH_BASE_ID_INFO, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PackageDto> getInfoBasePackageById(
            @Parameter(description = "ID value for the base package you need to retriev.", required = true)
            @PathVariable(PATH_VARIABLE_ID) final UUID id) {

        logger.info("Request getInfoBasePackageById");
        PackageDto getPackage = packageService.getInfoBasePackageById(id);
        return ResponseEntity.ok().body(getPackage);
    }

    @Operation(summary = "Deletes a package.",
            description = "Deletes an existing package in the catalog.",
            security = @SecurityRequirement(name = "BearerToken"))
    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping(path = MAPPING_PATH_ID)
    public ResponseEntity<Void> deletePackageById(
            @Parameter(description = "ID value for the package you need to delete.", required = true)
            @PathVariable(PATH_VARIABLE_ID) final UUID id) {

        logger.info("Request deletePackageById");
        packageService.deletePackage(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
