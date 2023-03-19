package edu.ou.humancommandservice.common.mapper;

import edu.ou.humancommandservice.data.entity.RoomDetailEntity;
import edu.ou.humancommandservice.data.entity.RoomDetailEntityPK;
import edu.ou.humancommandservice.data.pojo.request.roomDetail.RoomDetailAddRequest;
import edu.ou.humancommandservice.data.pojo.request.roomDetail.RoomDetailDeleteRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoomDetailEntityMapper {
    RoomDetailEntityMapper INSTANCE = Mappers.getMapper(RoomDetailEntityMapper.class);

    /**
     * Map RoomDetailAddRequest object to RoomDetailEntity object
     *
     * @param roomDetailAddRequest represents for RoomDetailAddRequest object
     * @return RoomDetailEntity object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "joinDate", ignore = true)
    RoomDetailEntity fromRoomDetailAddRequest(RoomDetailAddRequest roomDetailAddRequest);

    /**
     * Map RoomDetailDeleteRequest object to RoomDetailEntityPK object
     *
     * @param roomDetailDeleteRequest represents for RoomDetailDeleteRequest object
     * @return RoomDetailEntityPK object
     * @author Nguyen Trung Kien - OU
     */
    RoomDetailEntityPK fromRoomDetailDeleteRequest(RoomDetailDeleteRequest roomDetailDeleteRequest);

}
