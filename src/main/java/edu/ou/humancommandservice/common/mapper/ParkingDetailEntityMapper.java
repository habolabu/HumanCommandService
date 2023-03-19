package edu.ou.humancommandservice.common.mapper;

import edu.ou.humancommandservice.data.entity.ParkingDetailEntity;
import edu.ou.humancommandservice.data.entity.ParkingDetailEntityPK;
import edu.ou.humancommandservice.data.pojo.request.parkingDetail.ParkingDetailAddRequest;
import edu.ou.humancommandservice.data.pojo.request.parkingDetail.ParkingDetailDeleteRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ParkingDetailEntityMapper {
    ParkingDetailEntityMapper INSTANCE = Mappers.getMapper(ParkingDetailEntityMapper.class);

    /**
     * Map ParkingDetailAddRequest object to ParkingDetailEntity object
     *
     * @param parkingDetailAddRequest represents for ParkingDetailAddRequest object
     * @return ParkingDetailEntity object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "joinDate", ignore = true)
    ParkingDetailEntity fromParkingDetailAddRequest(ParkingDetailAddRequest parkingDetailAddRequest);

    /**
     * Map ParkingDetailDeleteRequest object to ParkingDetailEntityPK object
     *
     * @param parkingDetailDeleteRequest represents for ParkingDetailDeleteRequest object
     * @return ParkingDetailEntityPK object
     * @author Nguyen Trung Kien - OU
     */
    ParkingDetailEntityPK fromParkingDetailDeleteRequest(ParkingDetailDeleteRequest parkingDetailDeleteRequest);


}
