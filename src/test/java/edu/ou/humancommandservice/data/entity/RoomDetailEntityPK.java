package edu.ou.humancommandservice.data.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class RoomDetailEntityPK implements Serializable {
    @Column(name = "userId")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    @Column(name = "roomId")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roomId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomDetailEntityPK that = (RoomDetailEntityPK) o;
        return userId == that.userId && roomId == that.roomId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, roomId);
    }
}
