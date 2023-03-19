package edu.ou.humancommandservice.common.mapper;

import edu.ou.humancommandservice.data.entity.AvatarEntity;
import edu.ou.humancommandservice.data.pojo.request.avatar.AvatarAddRequest;
import edu.ou.humancommandservice.data.pojo.request.avatar.AvatarUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AvatarEntityMapper {
    AvatarEntityMapper INSTANCE = Mappers.getMapper(AvatarEntityMapper.class);

    /**
     * Map AvatarAddRequest object to AvatarEntity object
     *
     * @param avatarAddRequest represents for AvatarAddRequest object
     * @return AvatarEntity object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "url", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "selected", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    AvatarEntity fromAvatarAddRequest(AvatarAddRequest avatarAddRequest);

    /**
     * Map AvatarUpdateRequest object to AvatarEntity object
     *
     * @param avatarUpdateRequest represents for AvatarUpdateRequest object
     * @return AvatarUpdateRequest object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "url", ignore = true)
    @Mapping(target = "selected", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    AvatarEntity fromAvatarUpdateRequest(AvatarUpdateRequest avatarUpdateRequest);
}
