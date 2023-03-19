package edu.ou.humancommandservice.controller.emergency;

import edu.ou.coreservice.common.constant.SecurityPermission;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.service.base.IBaseService;
import edu.ou.humancommandservice.common.constant.EndPoint;
import edu.ou.humancommandservice.data.pojo.request.emergency.EmergencyAddRequest;
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
@RequestMapping(EndPoint.Emergency.BASE)
public class EmergencyAddController {
    private final IBaseService<IBaseRequest, IBaseResponse> emergencyAddService;

    /**
     * Add new emergency
     *
     * @param emergencyAddRequest new emergency information
     * @return id of new emergency
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.ADD_NEW_EMERGENCY)
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> addNewEmergency(
            @Validated
            @RequestBody
            EmergencyAddRequest emergencyAddRequest
    ) {
        return new ResponseEntity<>(
                emergencyAddService.execute(emergencyAddRequest),
                HttpStatus.OK
        );
    }
}
