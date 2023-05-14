package edu.ou.humancommandservice.common.util;

import edu.ou.humancommandservice.data.pojo.request.keycloak.KeyCloakAddUserRequest;
import edu.ou.humancommandservice.data.pojo.request.keycloak.KeyCloakUpdateUserRequest;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;

import javax.ws.rs.core.Response;
import java.util.Optional;

public class KeyCloakUtils {

    /**
     * Create user
     *
     * @param keycloak               keycloak instance
     * @param realm                  realm
     * @param keyCloakAddUserRequest request info
     * @return keycloak id
     */
    public static Optional<String> createUser(
            Keycloak keycloak,
            String realm,
            KeyCloakAddUserRequest keyCloakAddUserRequest
    ) {
        final UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEmail(keyCloakAddUserRequest.getEmail());
        userRepresentation.setFirstName(keyCloakAddUserRequest.getFirstName());
        userRepresentation.setLastName(keyCloakAddUserRequest.getLastName());
        userRepresentation.setUsername(keyCloakAddUserRequest.getUsername());
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);

        final Response response = keycloak.realm(realm).users().create(userRepresentation);
        if (response.getStatus() != 201) {
            return Optional.empty();
        }

        return Optional.of(response
                .getLocation()
                .getPath()
                .replaceAll(".*/([^/]+)$", "$1"));

    }

    /**
     * Update exist user
     *
     * @param keycloak                  keycloak instance
     * @param realm                     realm
     * @param keyCloakId                keycloak id
     * @param keyCloakUpdateUserRequest request info
     */
    public static void updateUser(
            Keycloak keycloak,
            String realm,
            String keyCloakId,
            KeyCloakUpdateUserRequest keyCloakUpdateUserRequest
    ) {
        final UserResource userResource = keycloak.realm(realm).users().get(keyCloakId);
        final UserRepresentation userRepresentation = userResource.toRepresentation();
        userRepresentation.setFirstName(keyCloakUpdateUserRequest.getFirstName());
        userRepresentation.setLastName(keyCloakUpdateUserRequest.getLastName());
        userRepresentation.setEmail(keyCloakUpdateUserRequest.getEmail());
        userResource.update(userRepresentation);

    }

    /**
     * Delete exist user
     *
     * @param keycloak   keycloak instance
     * @param realm      realm
     * @param keyCloakId keycloak id
     */
    public static void deleteUser(
            Keycloak keycloak,
            String realm,
            String keyCloakId
    ) {
        keycloak.realm(realm).users().delete(keyCloakId);
    }

}
