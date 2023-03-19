package edu.ou.humancommandservice.data.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "Avatar", schema = "HumanCommandService", catalog = "")
public class AvatarEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "url")
    private String url;
    @Basic
    @Column(name = "createdAt")
    private Timestamp createdAt;
    @Basic
    @Column(name = "isSelected")
    private byte isSelected;
    @Basic
    @Column(name = "isDeleted")
    private Timestamp isDeleted;
    @Basic
    @Column(name = "userId")
    private Integer userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public byte getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(byte isSelected) {
        this.isSelected = isSelected;
    }

    public Timestamp getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Timestamp isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvatarEntity that = (AvatarEntity) o;
        return id == that.id && isSelected == that.isSelected && Objects.equals(url, that.url) && Objects.equals(createdAt, that.createdAt) && Objects.equals(isDeleted, that.isDeleted) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, createdAt, isSelected, isDeleted, userId);
    }
}
