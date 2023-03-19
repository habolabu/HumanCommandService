package edu.ou.humancommandservice.data.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@Entity
@Table(
        name = "User",
        schema = "HumanCommandService"
)
public class UserEntity implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @NotBlank
    @Size(
            min = 1,
            max = 100,
            message = "Length must be in range 1 - 100"
    )
    @Column(name = "firstName")
    private String firstName;

    @Basic
    @NotBlank
    @Size(
            min = 1,
            max = 255,
            message = "Length must be in range 1 - 255"
    )
    @Column(name = "lastName")
    private String lastName;

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
            min = 12,
            max = 12,
            message = "Length must be 12"
    )
    @Column(name = "idCard")
    private String idCard;

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
    @NotBlank
    @Size(
            min = 1,
            max = 255,
            message = "Length must be 255"
    )
    @Column(name = "email")
    private String email;

    @Basic
    @Column(name = "gender")
    private boolean gender = true;

    @Basic
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "dateOfBirth")
    private Date dateOfBirth;

    @Basic
    @NotBlank
    @Size(
            min = 1,
            max = 100,
            message = "Length must be in range 1 - 100"
    )
    @Column(name = "nationality")
    private String nationality;

    @Basic
    @Column(name = "createdAt")
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    @Basic
    @Column(name = "isDeleted")
    private Timestamp isDeleted;

    @Basic
    @Column(name = "keyCloakId")
    private String keyCloakId;
    @Transient
    @Size(
            min = 1,
            max = 50,
            message = "Length must be in range 1 - 50"
    )
    private String username;

    @Transient
    private int roleId;
}
