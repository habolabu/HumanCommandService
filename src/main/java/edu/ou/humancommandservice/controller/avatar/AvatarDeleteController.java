package edu.ou.humancommandservice.controller.avatar;

import edu.ou.coreservice.common.constant.SecurityPermission;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.service.base.IBaseService;
import edu.ou.humancommandservice.common.constant.EndPoint;
import edu.ou.humancommandservice.data.pojo.request.avatar.AvatarDeleteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(EndPoint.Avatar.BASE)
public class AvatarDeleteController {
    private final IBaseService<IBaseRequest, IBaseResponse> avatarDeleteService;

    /**
     * Delete exist avatar
     *
     * @param avatarDeleteRequest id of avatar which want to delete
     * @return id avatar
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.DELETE_EXIST_AVATAR)
    @DeleteMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> deleteExistAvatar(
            @Validated
            @RequestBody
            AvatarDeleteRequest avatarDeleteRequest
    ) {
        return new ResponseEntity<>(
                avatarDeleteService.execute(avatarDeleteRequest),
                HttpStatus.OK
        );
    }
}
