package edu.ou.humancommandservice.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EndPoint {
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class User {
        public static final String BASE = "/user";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Avatar {
        public static final String BASE = "/avatar";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Emergency {
        public static final String BASE = "/emergency";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ParkingDetail {
        public static final String BASE = "/parking-detail";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class RoomDetail {
        public static final String BASE = "/room-detail";
    }

}
