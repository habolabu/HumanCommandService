package edu.ou.humancommandservice.data.pojo.request.emergency;

import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class EmergencyDeleteRequest implements IBaseRequest {
    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    private int id;
}
