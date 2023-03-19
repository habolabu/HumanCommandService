package edu.ou.humancommandservice.service.emergency;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.human.internal.emergency.EmergencyDeleteQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import edu.ou.humancommandservice.common.constant.CodeStatus;
import edu.ou.humancommandservice.data.pojo.request.emergency.EmergencyDeleteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EmergencyDeleteService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<Integer, Integer> emergencyDeleteRepository;
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
        if (validValidation.isInValid(request, EmergencyDeleteRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "emergency"
            );
        }
    }

    /**
     * delete exist emergency
     *
     * @param request request from client
     * @return response to client
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final EmergencyDeleteRequest emergencyDeleteRequest = (EmergencyDeleteRequest) request;
        final int emergencyId = emergencyDeleteRepository.execute(emergencyDeleteRequest.getId());

        rabbitTemplate.convertSendAndReceive(
                EmergencyDeleteQueueI.EXCHANGE,
                EmergencyDeleteQueueI.ROUTING_KEY,
                emergencyId
        );

        return new SuccessResponse<>(
                new SuccessPojo<>(
                        emergencyId,
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
