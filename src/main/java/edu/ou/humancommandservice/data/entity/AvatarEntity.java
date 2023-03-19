package edu.ou.humancommandservice.data.entity;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(
        name = "Avatar",
        schema = "HumanCommandService"
)
public class AvatarEntity implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @NotBlank
    @Size(
            max = 255,
            message = "Length must less than 255"
    )
    @Column(name = "url")
    private String url = "https://res.cloudinary.com/dzd9sonxs/image/upload/v1664544714/avatar/default-avatar_xh2rub.png";

    @Basic
    @Column(name = "createdAt")
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    @Basic
    @Column(name = "isSelected")
    private boolean isSelected = true;

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

    @Transient
    private MultipartFile image;
}
