package edu.ou.humancommandservice.service.roomDetail;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.human.internal.roomDetail.RoomDetailDeleteQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import edu.ou.humancommandservice.common.constant.CodeStatus;
import edu.ou.humancommandservice.common.mapper.RoomDetailEntityMapper;
import edu.ou.humancommandservice.data.entity.RoomDetailEntityPK;
import edu.ou.humancommandservice.data.pojo.request.roomDetail.RoomDetailDeleteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomDetailDeleteService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<RoomDetailEntityPK, RoomDetailEntityPK> roomDetailDeleteRepository;
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
        if (validValidation.isInValid(request, RoomDetailDeleteRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "room detail"
            );
        }
    }

    /**
     * Delete exist room detail
     *
     * @param request request from client
     * @return response to client
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final RoomDetailEntityPK roomDetailEntityPK =
                roomDetailDeleteRepository.execute(
                        RoomDetailEntityMapper.INSTANCE
                                .fromRoomDetailDeleteRequest((RoomDetailDeleteRequest) request)
                );

        rabbitTemplate.convertSendAndReceive(
                RoomDetailDeleteQueueI.EXCHANGE,
                RoomDetailDeleteQueueI.ROUTING_KEY,
                roomDetailEntityPK
        );

        return new SuccessResponse<>(
                new SuccessPojo<>(
                        roomDetailEntityPK,
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
