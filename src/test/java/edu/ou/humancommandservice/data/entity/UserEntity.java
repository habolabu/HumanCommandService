package edu.ou.humancommandservice.data.entity;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "User", schema = "HumanCommandService", catalog = "")
public class UserEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "username")
    private String username;
    @Basic
    @Column(name = "slug")
    private String slug;
    @Basic
    @Column(name = "firstName")
    private String firstName;
    @Basic
    @Column(name = "lastName")
    private String lastName;
    @Basic
    @Column(name = "address")
    private String address;
    @Basic
    @Column(name = "idCard")
    private String idCard;
    @Basic
    @Column(name = "phoneNumber")
    private String phoneNumber;
    @Basic
    @Column(name = "gender")
    private byte gender;
    @Basic
    @Column(name = "dateOfBirth")
    private Date dateOfBirth;
    @Basic
    @Column(name = "nationality")
    private String nationality;
    @Basic
    @Column(name = "token")
    private String token;
    @Basic
    @Column(name = "createdAt")
    private Timestamp createdAt;
    @Basic
    @Column(name = "isDeleted")
    private Timestamp isDeleted;
    @Basic
    @Column(name = "roleId")
    private Integer roleId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public byte getGender() {
        return gender;
    }

    public void setGender(byte gender) {
        this.gender = gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return id == that.id && gender == that.gender && Objects.equals(username, that.username) && Objects.equals(slug, that.slug) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(address, that.address) && Objects.equals(idCard, that.idCard) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(dateOfBirth, that.dateOfBirth) && Objects.equals(nationality, that.nationality) && Objects.equals(token, that.token) && Objects.equals(createdAt, that.createdAt) && Objects.equals(isDeleted, that.isDeleted) && Objects.equals(roleId, that.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, slug, firstName, lastName, address, idCard, phoneNumber, gender, dateOfBirth, nationality, token, createdAt, isDeleted, roleId);
    }
}
