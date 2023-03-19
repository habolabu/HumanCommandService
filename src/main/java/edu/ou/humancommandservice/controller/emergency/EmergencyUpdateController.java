package edu.ou.humancommandservice.controller.emergency;

import edu.ou.coreservice.common.constant.SecurityPermission;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.service.base.IBaseService;
import edu.ou.humancommandservice.common.constant.EndPoint;
import edu.ou.humancommandservice.data.pojo.request.emergency.EmergencyUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(EndPoint.Emergency.BASE)
public class EmergencyUpdateController {
    private final IBaseService<IBaseRequest, IBaseResponse> emergencyUpdateService;

    /**
     * Update exist emergency
     *
     * @param emergencyUpdateRequest new information of exist emergency
     * @return id of emergency
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.MODIFY_EXIST_EMERGENCY)
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> updateExistEmergency(
            @Validated
            @RequestBody
            EmergencyUpdateRequest emergencyUpdateRequest
    ) {
        return new ResponseEntity<>(
                emergencyUpdateService.execute(emergencyUpdateRequest),
                HttpStatus.OK
        );
    }
}
