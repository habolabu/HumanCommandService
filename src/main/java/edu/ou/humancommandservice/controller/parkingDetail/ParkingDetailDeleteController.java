package edu.ou.humancommandservice.controller.parkingDetail;

import edu.ou.coreservice.common.constant.SecurityPermission;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.service.base.IBaseService;
import edu.ou.humancommandservice.common.constant.EndPoint;
import edu.ou.humancommandservice.data.pojo.request.parkingDetail.ParkingDetailDeleteRequest;
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
@RequestMapping(EndPoint.ParkingDetail.BASE)
public class ParkingDetailDeleteController {
    private final IBaseService<IBaseRequest, IBaseResponse> parkingDetailDeleteService;

    /**
     * Delete exist parking detail
     *
     * @param parkingDetailDeleteRequest id of parking detail which want to delete
     * @return if of parking detail
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.DELETE_EXIST_PARKING_DETAIL)
    @DeleteMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> deleteExistParkingDetail(
            @Validated
            @RequestBody
            ParkingDetailDeleteRequest parkingDetailDeleteRequest
    ) {
        return new ResponseEntity<>(
                parkingDetailDeleteService.execute(parkingDetailDeleteRequest),
                HttpStatus.OK
        );
    }
}
