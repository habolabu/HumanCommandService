package edu.ou.humancommandservice.data.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import java.io.Serializable;

@Data
public class ParkingDetailEntityPK implements Serializable {
    @Column(name = "userId")
    @Id
    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    private int userId;

    @Column(name = "parkingId")
    @Id
    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    private int parkingId;

    @Column(name = "parkingTypeId")
    @Id
    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    private int parkingTypeId;
}
