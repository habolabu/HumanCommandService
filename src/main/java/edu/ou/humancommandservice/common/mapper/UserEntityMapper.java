package edu.ou.humancommandservice.common.mapper;

import edu.ou.humancommandservice.data.entity.UserEntity;
import edu.ou.humancommandservice.data.pojo.request.user.UserAddRequest;
import edu.ou.humancommandservice.data.pojo.request.user.UserUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserEntityMapper {
    UserEntityMapper INSTANCE = Mappers.getMapper(UserEntityMapper.class);

    /**
     * Map UserAddRequest object to UserEntity object
     *
     * @param userAddRequest represents for UserAddRequest object
     * @return UserEntity object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    UserEntity fromUserAddRequest(UserAddRequest userAddRequest);

    /**
     * Map UserUpdateRequest object to UserEntity object
     *
     * @param userUpdateRequest represents for UserUpdateRequest object
     * @return UserEntity object
     * @author Nguyen Trung Kien - OU
     */

    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    UserEntity fromUserUpdateRequest(UserUpdateRequest userUpdateRequest);

}
