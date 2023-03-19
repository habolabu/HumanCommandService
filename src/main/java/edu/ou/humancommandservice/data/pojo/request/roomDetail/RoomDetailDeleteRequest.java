package edu.ou.humancommandservice.data.pojo.request.roomDetail;

import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class RoomDetailDeleteRequest implements IBaseRequest {
    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    private int roomId;

    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    private int userId;
}
