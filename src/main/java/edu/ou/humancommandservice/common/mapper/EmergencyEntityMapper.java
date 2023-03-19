package edu.ou.humancommandservice.common.mapper;

import edu.ou.humancommandservice.data.entity.EmergencyEntity;
import edu.ou.humancommandservice.data.pojo.request.emergency.EmergencyAddRequest;
import edu.ou.humancommandservice.data.pojo.request.emergency.EmergencyUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmergencyEntityMapper {
    EmergencyEntityMapper INSTANCE = Mappers.getMapper(EmergencyEntityMapper.class);

    /**
     * Map EmergencyAddRequest object to EmergencyEntity object
     *
     * @param emergencyAddRequest represents for EmergencyAddRequest object
     * @return EmergencyEntity object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    EmergencyEntity fromEmergencyAddRequest(EmergencyAddRequest emergencyAddRequest);

    /**
     * Map EmergencyUpdateRequest object to EmergencyEntity object
     *
     * @param emergencyUpdateRequest represents for EmergencyUpdateRequest object
     * @return EmergencyEntity object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "isDeleted", ignore = true)
    EmergencyEntity fromEmergencyUpdateRequest(EmergencyUpdateRequest emergencyUpdateRequest);

}
