package edu.ou.humancommandservice.data.pojo.request.keycloak;

import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import lombok.Data;

@Data
public class KeyCloakUpdateUserRequest implements IBaseRequest {
    private String password;
    private String email;
    private String firstName;
    private String lastName;
}
