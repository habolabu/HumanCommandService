package edu.ou.humancommandservice.data.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "Password", schema = "HumanCommandService", catalog = "")
public class PasswordEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "password")
    private String password;
    @Basic
    @Column(name = "createdAt")
    private Timestamp createdAt;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
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
        PasswordEntity that = (PasswordEntity) o;
        return id == that.id && Objects.equals(password, that.password) && Objects.equals(createdAt, that.createdAt) && Objects.equals(isDeleted, that.isDeleted) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, password, createdAt, isDeleted, userId);
    }
}
