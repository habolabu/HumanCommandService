package edu.ou.humancommandservice.data.pojo.request.parkingDetail;

import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class ParkingDetailDeleteRequest implements IBaseRequest {
    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    private int parkingId;

    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    private int parkingTypeId;

    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    private int userId;
}
