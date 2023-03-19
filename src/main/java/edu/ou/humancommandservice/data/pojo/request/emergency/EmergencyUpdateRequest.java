package edu.ou.humancommandservice.data.pojo.request.emergency;

import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class EmergencyUpdateRequest implements IBaseRequest {
    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    private int id;

    @NotBlank
    @Size(
            min = 1,
            max = 255,
            message = "Length must be in range 1 - 255"
    )
    private String name;

    @NotBlank
    @Size(
            min = 1,
            max = 255,
            message = "Length must be in range 1 - 255"
    )
    private String address;

    @NotBlank
    @Size(
            min = 10,
            max = 10,
            message = "Length must be 10"
    )
    private String phoneNumber;
}
