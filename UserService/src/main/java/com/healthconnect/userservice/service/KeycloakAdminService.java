package com.healthconnect.userservice.service;

import jakarta.ws.rs.core.Response;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface KeycloakAdminService {
    Response addUser(String realm, String email, String firstName, String lastName, String password);
    List<UserRepresentation> getAllUsers(String realm);
    UserRepresentation getUserById(String realm, String userId);
    void updateUser(String realm, String userId, UserRepresentation user);
    void deleteUser(String realm, String userId);
}
