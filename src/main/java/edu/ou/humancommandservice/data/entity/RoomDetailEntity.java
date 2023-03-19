package edu.ou.humancommandservice.data.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(
        name = "RoomDetail",
        schema = "HumanCommandService"
)
@IdClass(RoomDetailEntityPK.class)
public class RoomDetailEntity implements Serializable {
    @Id
    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    @Column(name = "userId")
    private int userId;

    @Id
    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    @Column(name = "roomId")
    private int roomId;

    @Basic
    @Column(name = "joinDate")
    private Timestamp joinDate = new Timestamp(System.currentTimeMillis());
}
