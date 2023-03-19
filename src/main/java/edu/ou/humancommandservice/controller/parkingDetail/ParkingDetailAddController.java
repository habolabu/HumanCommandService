package edu.ou.humancommandservice.controller.parkingDetail;

import edu.ou.coreservice.common.constant.SecurityPermission;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.service.base.IBaseService;
import edu.ou.humancommandservice.common.constant.EndPoint;
import edu.ou.humancommandservice.data.pojo.request.parkingDetail.ParkingDetailAddRequest;
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
@RequestMapping(EndPoint.ParkingDetail.BASE)
public class ParkingDetailAddController {
    private final IBaseService<IBaseRequest, IBaseResponse> parkingDetailAddService;

    /**
     * Add new parking detail
     *
     * @param parkingDetailAddRequest new parking detail information
     * @return id of new parking detail
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.ADD_NEW_PARKING_DETAIL)
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> addNewParkingDetail(
            @Validated
            @RequestBody
            ParkingDetailAddRequest parkingDetailAddRequest
    ) {
        return new ResponseEntity<>(
                parkingDetailAddService.execute(parkingDetailAddRequest),
                HttpStatus.OK
        );
    }
}
