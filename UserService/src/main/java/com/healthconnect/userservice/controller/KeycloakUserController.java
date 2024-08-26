package com.healthconnect.userservice.controller;

import com.healthconnect.userservice.constant.ApiEndpoints;
import com.healthconnect.userservice.service.KeycloakAdminService;
import jakarta.ws.rs.core.Response;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.healthconnect.userservice.constant.LogMessages.*;

@RestController
@RequestMapping(ApiEndpoints.KEYCLOAK)
public class KeycloakUserController {

    private final KeycloakAdminService keycloakAdminService;

    public KeycloakUserController(KeycloakAdminService keycloakAdminService) {
        this.keycloakAdminService = keycloakAdminService;
    }

    @PostMapping
    public ResponseEntity<String> addUser(@RequestParam String realm,
                                          @RequestParam String email,
                                          @RequestParam String firstName,
                                          @RequestParam String lastName,
                                          @RequestParam String password) {
        Response response = keycloakAdminService.addUser(realm, email, firstName, lastName, password);
        if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
            return ResponseEntity.status(201).body(USER_CREATED_SUCCESSFULLY);
        } else {
            return ResponseEntity.status(response.getStatus()).body(USER_CREATION_FAILED);
        }
    }

    @GetMapping
    public ResponseEntity<List<UserRepresentation>> getAllUsers() {
        return ResponseEntity.ok(keycloakAdminService.getAllUsers());
    }

    @GetMapping(ApiEndpoints.ID)
    public ResponseEntity<UserRepresentation> getUserById(@PathVariable String userId) {
        UserRepresentation user = keycloakAdminService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping(ApiEndpoints.ID)
    public ResponseEntity<String> updateUser(@PathVariable String userId, @RequestBody UserRepresentation user) {
        keycloakAdminService.updateUser(userId, user);
        return ResponseEntity.ok(USER_UPDATED_SUCCESSFULLY);
    }

    @DeleteMapping(ApiEndpoints.ID)
    public ResponseEntity<String> deleteUser(@PathVariable String userId) {
        keycloakAdminService.deleteUser(userId);
        return ResponseEntity.ok(USER_DELETED_SUCCESSFULLY);
    }
}
