package edu.ou.humancommandservice.data.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(
        name = "Emergency",
        schema = "HumanCommandService"
)
public class EmergencyEntity implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @NotBlank
    @Size(
            min = 1,
            max = 255,
            message = "Length must be in range 1 - 255"
    )
    @Column(name = "name")
    private String name;

    @Basic
    @NotBlank
    @Size(
            min = 1,
            max = 255,
            message = "Length must be in range 1 - 255"
    )
    @Column(name = "address")
    private String address;

    @Basic
    @NotBlank
    @Size(
            min = 10,
            max = 10,
            message = "Length must be 10"
    )
    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Basic
    @Column(name = "isDeleted")
    private Timestamp isDeleted;

    @Basic
    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    @Column(name = "userId")
    private int userId;
}
