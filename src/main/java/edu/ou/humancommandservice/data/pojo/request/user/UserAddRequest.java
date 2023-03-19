package edu.ou.humancommandservice.data.pojo.request.user;

import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;

@Data
public class UserAddRequest implements IBaseRequest {
    @NotBlank
    @Size(
            min = 1,
            max = 100,
            message = "Length must be in range 1 - 100"
    )
    private String firstName;

    @NotBlank
    @Size(
            min = 1,
            max = 255,
            message = "Length must be in range 1 - 255"
    )
    private String lastName;

    @NotBlank
    @Size(
            min = 1,
            max = 255,
            message = "Length must be in range 1 - 255"
    )
    private String address;

    @NotBlank
    @Size(
            min = 12,
            max = 12,
            message = "Length must be 12"
    )
    private String idCard;

    @NotBlank
    @Size(
            min = 10,
            max = 10,
            message = "Length must be 10"
    )
    private String phoneNumber;

    @NotBlank
    @Size(
            min = 1,
            max = 255,
            message = "Length must be 255"
    )
    private String email;

    private boolean gender = true;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date dateOfBirth;

    @NotBlank
    @Size(
            min = 1,
            max = 100,
            message = "Length must be in range 1 - 100"
    )
    private String nationality;

    @NotBlank
    @Size(
            min = 1,
            max = 50,
            message = "Length must be in range 1 - 50"
    )
    private String username;

    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    private int roleId;
}
