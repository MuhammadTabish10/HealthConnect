package com.healthconnect.userservice.controller;

import com.healthconnect.userservice.service.KeycloakAdminService;
import jakarta.ws.rs.core.Response;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/keycloak/users")
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
            return ResponseEntity.status(201).body("User created successfully.");
        } else {
            return ResponseEntity.status(response.getStatus()).body("User creation failed.");
        }
    }

    @GetMapping
    public List<UserRepresentation> getAllUsers(@RequestParam String realm) {
        return keycloakAdminService.getAllUsers(realm);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserRepresentation> getUserById(@RequestParam String realm, @PathVariable String userId) {
        UserRepresentation user = keycloakAdminService.getUserById(realm, userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(@RequestParam String realm, @PathVariable String userId, @RequestBody UserRepresentation user) {
        keycloakAdminService.updateUser(realm, userId, user);
        return ResponseEntity.ok("User updated successfully.");
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@RequestParam String realm, @PathVariable String userId) {
        keycloakAdminService.deleteUser(realm, userId);
        return ResponseEntity.ok("User deleted successfully.");
    }
}
