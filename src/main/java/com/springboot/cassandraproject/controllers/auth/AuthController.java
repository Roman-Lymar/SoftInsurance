package com.springboot.cassandraproject.controllers.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "Auth", description = "Authorization")
public class AuthController {

    @Operation(summary = "Creates a new user.",
            description = "Creates a new user with credentials and returns a JWT token.", tags = {"Auth"})
    @PostMapping(path = "/singup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> singup() {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Authenticates user.",
            description = "Authenticates user credentials and return a JWT token.", tags = {"Auth"})
    @PostMapping(path = "/singin", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> singin() {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Changes user login.",
            description = "Changes user login.", tags = {"Auth"},
            security = @SecurityRequirement(name = "BearerToken"))
    @PreAuthorize("hasAnyAuthority('admin', 'client')")
    @PatchMapping(path = "/{userId}/change-login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> changeLogin() {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Сhanges user password.",
            description = "Сhanges user password.", tags = {"Auth"},
            security = @SecurityRequirement(name = "BearerToken"))
    @PreAuthorize("hasAnyAuthority('admin', 'client')")
    @PatchMapping(path = "/{userId}/change-password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> changePassword() {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Changes user role.",
            description = "Changes user role.", tags = {"Auth"},
            security = @SecurityRequirement(name = "BearerToken"))
    @PreAuthorize("hasAuthority('admin')")
    @PatchMapping(path = "/{userId}/change-role", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> changeRole() {
        return ResponseEntity.ok().build();
    }
}
