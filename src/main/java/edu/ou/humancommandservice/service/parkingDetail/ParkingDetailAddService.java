package edu.ou.humancommandservice.service.parkingDetail;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.building.external.parking.ParkingCheckExistByIdQueueE;
import edu.ou.coreservice.queue.building.external.parkingType.ParkingTypeCheckExistByIdQueueE;
import edu.ou.coreservice.queue.human.internal.parkingDetail.ParkingDetailAddQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import edu.ou.humancommandservice.common.constant.CodeStatus;
import edu.ou.humancommandservice.common.mapper.ParkingDetailEntityMapper;
import edu.ou.humancommandservice.data.entity.ParkingDetailEntity;
import edu.ou.humancommandservice.data.entity.ParkingDetailEntityPK;
import edu.ou.humancommandservice.data.pojo.request.parkingDetail.ParkingDetailAddRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class ParkingDetailAddService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<ParkingDetailEntity, ParkingDetailEntityPK> parkingDetailAddRepository;
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
        if (validValidation.isInValid(request, ParkingDetailAddRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "parking detail"
            );
        }
    }

    /**
     * Add new parking detail
     *
     * @param request request from client
     * @return response to client
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final ParkingDetailEntity parkingDetailEntity = ParkingDetailEntityMapper.INSTANCE
                .fromParkingDetailAddRequest((ParkingDetailAddRequest) request);

        if (!userCheckExistByIdRepository.execute(parkingDetailEntity.getUserId())) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "user",
                    "user identity",
                    parkingDetailEntity.getUserId()
            );
        }

        if (!this.checkParkingExist(parkingDetailEntity.getParkingId())) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "parking",
                    "parking identity",
                    parkingDetailEntity.getParkingId()
            );
        }

        if (!this.checkParkingTypeExist(parkingDetailEntity.getParkingTypeId())) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "parking type",
                    "parking type identity",
                    parkingDetailEntity.getParkingTypeId()
            );
        }


        final ParkingDetailEntityPK parkingDetailEntityPK = parkingDetailAddRepository.execute(parkingDetailEntity);

        rabbitTemplate.convertSendAndReceive(
                ParkingDetailAddQueueI.EXCHANGE,
                ParkingDetailAddQueueI.ROUTING_KEY,
                parkingDetailEntity
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

    /**
     * Check parking exist or not
     *
     * @param parkingId parking id
     * @return exist status
     * @author Nguyen Trung Kien - OU
     */
    private boolean checkParkingExist(int parkingId) {
        final Object parkingExist = rabbitTemplate.convertSendAndReceive(
                ParkingCheckExistByIdQueueE.EXCHANGE,
                ParkingCheckExistByIdQueueE.ROUTING_KEY,
                parkingId
        );

        return !Objects.isNull(parkingExist)
                && (boolean) parkingExist;
    }

    /**
     * Check parking type exist or not
     *
     * @param parkingTypeId parking type id
     * @return exist status
     * @author Nguyen Trung Kien - OU
     */
    private boolean checkParkingTypeExist(int parkingTypeId) {
        final Object parkingTypeExist = rabbitTemplate.convertSendAndReceive(
                ParkingTypeCheckExistByIdQueueE.EXCHANGE,
                ParkingTypeCheckExistByIdQueueE.ROUTING_KEY,
                parkingTypeId
        );

        return !Objects.isNull(parkingTypeExist)
                && (boolean) parkingTypeExist;
    }
}
