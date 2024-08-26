package com.healthconnect.userservice.service.impl;

import com.healthconnect.baseservice.exception.EntitySaveException;
import com.healthconnect.userservice.service.KeycloakAdminService;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.healthconnect.userservice.constant.LogMessages.FAILED_TO_CREATE_USER_IN_KEYCLOAK_STATUS_LOG;
import static com.healthconnect.userservice.constant.UserConstants.ROLE_CLIENT;
import static com.healthconnect.userservice.util.UserUtils.extractUserIdFromResponse;

@Service
public class KeycloakAdminServiceImpl implements KeycloakAdminService {

    @Value("${keycloak.realm}")
    private String realm;

    private final Keycloak keycloak;

    public KeycloakAdminServiceImpl(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    @Override
    public Response addUser(String realm, String email, String firstName, String lastName, String password) {
        UserRepresentation user = createUserRepresentation(email, firstName, lastName, password);

        try (Response response = keycloak.realm(realm).users().create(user)) {
            if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
                assignClientRoleToUser(realm, extractUserIdFromResponse(response));
            } else {
                throw new EntitySaveException(FAILED_TO_CREATE_USER_IN_KEYCLOAK_STATUS_LOG + response.getStatus());
            }
            return response;
        }
    }

    @Override
    public List<UserRepresentation> getAllUsers() {
        return keycloak.realm(realm).users().list();
    }

    @Override
    public UserRepresentation getUserById(String userId) {
        return keycloak.realm(realm).users().get(userId).toRepresentation();
    }

    @Override
    public void updateUser(String userId, UserRepresentation user) {
        keycloak.realm(realm).users().get(userId).update(user);
    }

    @Override
    public void deleteUser(String userId) {
        keycloak.realm(realm).users().get(userId).remove();
    }

    private static UserRepresentation createUserRepresentation(String email, String firstName, String lastName, String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);

        UserRepresentation user = new UserRepresentation();
        user.setUsername(email);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEnabled(true);
        user.setCredentials(Collections.singletonList(credential));

        return user;
    }

    private void assignClientRoleToUser(String realm, String userId) {
        RoleRepresentation clientRole = keycloak.realm(realm).roles().get(ROLE_CLIENT).toRepresentation();
        keycloak.realm(realm).users().get(userId).roles().realmLevel().add(Collections.singletonList(clientRole));
    }
}
