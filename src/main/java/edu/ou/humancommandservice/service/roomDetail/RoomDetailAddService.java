package edu.ou.humancommandservice.service.roomDetail;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.building.external.room.RoomCheckExistByIdQueueE;
import edu.ou.coreservice.queue.human.internal.roomDetail.RoomDetailAddQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import edu.ou.humancommandservice.common.constant.CodeStatus;
import edu.ou.humancommandservice.common.mapper.RoomDetailEntityMapper;
import edu.ou.humancommandservice.data.entity.RoomDetailEntity;
import edu.ou.humancommandservice.data.entity.RoomDetailEntityPK;
import edu.ou.humancommandservice.data.pojo.request.roomDetail.RoomDetailAddRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomDetailAddService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<RoomDetailEntity, RoomDetailEntityPK> roomDetailAddRepository;
    private final IBaseRepository<Integer, Boolean> userCheckExistByIdRepository;
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
        if (validValidation.isInValid(request, RoomDetailAddRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "room detail"
            );
        }
    }

    /**
     * Add new room detail
     *
     * @param request request from client
     * @return response to client
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final RoomDetailEntity roomDetailEntity = RoomDetailEntityMapper.INSTANCE
                .fromRoomDetailAddRequest((RoomDetailAddRequest) request);

        if (!userCheckExistByIdRepository.execute(roomDetailEntity.getUserId())) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "user",
                    "user identity",
                    roomDetailEntity.getUserId()
            );
        }

        if (!this.checkRoomExist(roomDetailEntity.getRoomId())) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "room",
                    "room identity",
                    roomDetailEntity.getUserId()
            );
        }

        final RoomDetailEntityPK roomDetailEntityPK = roomDetailAddRepository.execute(roomDetailEntity);

        rabbitTemplate.convertSendAndReceive(
                RoomDetailAddQueueI.EXCHANGE,
                RoomDetailAddQueueI.ROUTING_KEY,
                roomDetailEntity
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

    /**
     * Check room exist or not
     *
     * @param roomId room id
     * @return exist status
     * @author Nguyen Trung Kien - OU
     */
    private boolean checkRoomExist(int roomId) {
        final Object roomExist = rabbitTemplate.convertSendAndReceive(
                RoomCheckExistByIdQueueE.EXCHANGE,
                RoomCheckExistByIdQueueE.ROUTING_KEY,
                roomId
        );

        return !Objects.isNull(roomExist)
                && (boolean) roomExist;
    }
}
