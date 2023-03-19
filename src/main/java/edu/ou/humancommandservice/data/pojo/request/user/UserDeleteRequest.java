package edu.ou.humancommandservice.data.pojo.request.user;

import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class UserDeleteRequest implements IBaseRequest {
    @Min(
            value = 1,
            message = "Value must be greater than or equals to 1"
    )
    private int id;
}
