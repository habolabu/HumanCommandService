package edu.ou.humancommandservice.data.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import java.io.Serializable;

@Data
public class RoomDetailEntityPK implements Serializable {
    @Column(name = "userId")
    @Id
    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    private int userId;
    @Column(name = "roomId")
    @Id
    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    private int roomId;
}
