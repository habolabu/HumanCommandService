package edu.ou.humancommandservice.data.pojo.request.keycloak;

import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import lombok.Data;

@Data
public class KeyCloakAddUserRequest implements IBaseRequest {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
}
