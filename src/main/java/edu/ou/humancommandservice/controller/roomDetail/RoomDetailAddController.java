package edu.ou.humancommandservice.controller.roomDetail;

import edu.ou.coreservice.common.constant.SecurityPermission;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.service.base.IBaseService;
import edu.ou.humancommandservice.common.constant.EndPoint;
import edu.ou.humancommandservice.data.pojo.request.roomDetail.RoomDetailAddRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(EndPoint.RoomDetail.BASE)
public class RoomDetailAddController {
    private final IBaseService<IBaseRequest, IBaseResponse> roomDetailAddService;

    /**
     * Add new room detail
     *
     * @param roomDetailAddRequest new room detail information
     * @return id of new room detail
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.ADD_NEW_ROOM_DETAIL)
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> addNewRoomDetail(
            @Validated
            @RequestBody
            RoomDetailAddRequest roomDetailAddRequest
    ) {
        return new ResponseEntity<>(
                roomDetailAddService.execute(roomDetailAddRequest),
                HttpStatus.OK
        );
    }
}
