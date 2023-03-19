package edu.ou.humancommandservice.service.parkingDetail;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.human.internal.parkingDetail.ParkingDetailDeleteQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import edu.ou.humancommandservice.common.constant.CodeStatus;
import edu.ou.humancommandservice.common.mapper.ParkingDetailEntityMapper;
import edu.ou.humancommandservice.data.entity.ParkingDetailEntityPK;
import edu.ou.humancommandservice.data.pojo.request.parkingDetail.ParkingDetailDeleteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ParkingDetailDeleteService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<ParkingDetailEntityPK, ParkingDetailEntityPK> parkingDetailDeleteRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ValidValidation validValidation;

    /**
     * Validate request
     *
     * @param request request
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(IBaseRequest request) {
        if (validValidation.isInValid(request, ParkingDetailDeleteRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "parking detail"
            );
        }
    }

    /**
     * Delete exist parking detail
     *
     * @param request request from client
     * @return response to client
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final ParkingDetailEntityPK parkingDetailEntityPK =
                parkingDetailDeleteRepository.execute(
                        ParkingDetailEntityMapper.INSTANCE
                                .fromParkingDetailDeleteRequest((ParkingDetailDeleteRequest) request)
                );

        rabbitTemplate.convertSendAndReceive(
                ParkingDetailDeleteQueueI.EXCHANGE,
                ParkingDetailDeleteQueueI.ROUTING_KEY,
                parkingDetailEntityPK
        );

        return new SuccessResponse<>(
                new SuccessPojo<>(
                        parkingDetailEntityPK,
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
