package com.healthconnect.userservice.service.impl;

import com.healthconnect.userservice.service.KeycloakAdminService;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class KeycloakAdminServiceImpl implements KeycloakAdminService {

    private final Keycloak keycloak;

    public KeycloakAdminServiceImpl(Keycloak keycloak) {
        this.keycloak = keycloak;
    }
    @Override
    public Response addUser(String realm, String email, String firstName, String lastName, String password) {
        // Create user representation
        UserRepresentation user = new UserRepresentation();
        user.setUsername(email);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEnabled(true);

        // Set credentials
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        user.setCredentials(Collections.singletonList(credential));

        // Create the user in Keycloak
        Response response;
        try (Response res = keycloak.realm(realm).users().create(user)) {
            response = res;
        }

        if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
            // Retrieve the user ID from the response location header
            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

            // Assign realm role to the user (Use "CLIENT" instead of "ROLE_CLIENT")
            RoleRepresentation clientRole = keycloak.realm(realm).roles().get("CLIENT").toRepresentation();
            keycloak.realm(realm).users().get(userId).roles().realmLevel().add(Collections.singletonList(clientRole));
        } else {
            throw new RuntimeException("Failed to create user in Keycloak. Response status: " + response.getStatus());
        }

        return response;
    }



    @Override
    public List<UserRepresentation> getAllUsers(String realm) {
        return keycloak.realm(realm).users().list();
    }

    @Override
    public UserRepresentation getUserById(String realm, String userId) {
        return keycloak.realm(realm).users().get(userId).toRepresentation();
    }

    @Override
    public void updateUser(String realm, String userId, UserRepresentation user) {
        keycloak.realm(realm).users().get(userId).update(user);
    }

    @Override
    public void deleteUser(String realm, String userId) {
        keycloak.realm(realm).users().get(userId).remove();
    }
}
