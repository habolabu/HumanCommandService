package edu.ou.humancommandservice.controller.avatar;

import edu.ou.coreservice.common.constant.SecurityPermission;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.service.base.IBaseService;
import edu.ou.humancommandservice.common.constant.EndPoint;
import edu.ou.humancommandservice.data.pojo.request.avatar.AvatarAddRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(EndPoint.Avatar.BASE)
public class AvatarAddController {
    private final IBaseService<IBaseRequest, IBaseResponse> avatarAddService;

    /**
     * Add new avatar
     *
     * @param avatarAddRequest new avatar information
     * @return id of new avatar
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.ADD_NEW_AVATAR)
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> addNewAvatar(
            @Validated
            AvatarAddRequest avatarAddRequest
    ) {
        return new ResponseEntity<>(
                avatarAddService.execute(avatarAddRequest),
                HttpStatus.OK);
    }
}
