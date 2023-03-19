package edu.ou.humancommandservice.data.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class ParkingDetailEntityPK implements Serializable {
    @Column(name = "userId")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    @Column(name = "parkingId")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int parkingId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getParkingId() {
        return parkingId;
    }

    public void setParkingId(int parkingId) {
        this.parkingId = parkingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingDetailEntityPK that = (ParkingDetailEntityPK) o;
        return userId == that.userId && parkingId == that.parkingId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, parkingId);
    }
}
