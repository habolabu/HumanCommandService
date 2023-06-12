package edu.ou.humancommandservice.service.emergency;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.human.internal.emergency.EmergencyUpdateQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import edu.ou.humancommandservice.common.constant.CodeStatus;
import edu.ou.humancommandservice.data.entity.EmergencyEntity;
import edu.ou.humancommandservice.data.pojo.request.emergency.EmergencyUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EmergencyUpdateService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<EmergencyEntity, Integer> emergencyUpdateRepository;
    private final IBaseRepository<Integer, EmergencyEntity> emergencyFindByIdRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ValidValidation validValidation;

    /**
     * Validate request
     *
     * @param request input of task
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(IBaseRequest request) {
        if (validValidation.isInValid(request, EmergencyUpdateRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "emergency"
            );
        }
    }

    /**
     * Update exist emergency
     *
     * @param request request from client
     * @return response to client
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final EmergencyUpdateRequest emergencyUpdateRequest = (EmergencyUpdateRequest) request;
        final EmergencyEntity emergencyEntity = emergencyFindByIdRepository.execute(emergencyUpdateRequest.getId());

        emergencyEntity.setAddress(emergencyUpdateRequest.getAddress());
        emergencyEntity.setName(emergencyUpdateRequest.getName());
        emergencyEntity.setPhoneNumber(emergencyUpdateRequest.getPhoneNumber());

        emergencyUpdateRepository.execute(emergencyEntity);

        rabbitTemplate.convertSendAndReceive(
                EmergencyUpdateQueueI.EXCHANGE,
                EmergencyUpdateQueueI.ROUTING_KEY,
                emergencyEntity
        );

        return new SuccessResponse<>(
                new SuccessPojo<>(
                        emergencyEntity.getId(),
                        CodeStatus.SUCCESS,
                        Message.Success.SUCCESSFUL
                )
        );
    }

    @Override
    protected void postExecute(IBaseRequest input) {
        // do nothing
    }
}
