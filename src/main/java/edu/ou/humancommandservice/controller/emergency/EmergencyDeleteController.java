package edu.ou.humancommandservice.controller.emergency;

import edu.ou.coreservice.common.constant.SecurityPermission;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.service.base.IBaseService;
import edu.ou.humancommandservice.common.constant.EndPoint;
import edu.ou.humancommandservice.data.pojo.request.emergency.EmergencyDeleteRequest;
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
@RequestMapping(EndPoint.Emergency.BASE)
public class EmergencyDeleteController {
    private final IBaseService<IBaseRequest, IBaseResponse> emergencyDeleteService;

    /**
     * Delete exist emergency
     *
     * @param emergencyDeleteRequest id of emergency which want to delete
     * @return if of emergency
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.DELETE_EXIST_EMERGENCY)
    @DeleteMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> deleteExistEmergency(
            @Validated
            @RequestBody
            EmergencyDeleteRequest emergencyDeleteRequest
    ) {
        return new ResponseEntity<>(
                emergencyDeleteService.execute(emergencyDeleteRequest),
                HttpStatus.OK
        );
    }
}
