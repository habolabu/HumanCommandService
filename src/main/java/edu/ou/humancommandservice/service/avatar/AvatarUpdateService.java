package edu.ou.humancommandservice.service.avatar;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.util.SecurityUtils;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.human.internal.avatar.AvatarUnSelectedByUserIdQueueI;
import edu.ou.coreservice.queue.human.internal.avatar.AvatarUpdateQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import edu.ou.humancommandservice.common.constant.CodeStatus;
import edu.ou.humancommandservice.common.mapper.AvatarEntityMapper;
import edu.ou.humancommandservice.data.entity.AvatarEntity;
import edu.ou.humancommandservice.data.pojo.request.avatar.AvatarUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class AvatarUpdateService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<AvatarEntity, Integer> avatarUpdateRepository;
    private final IBaseRepository<Integer, Boolean> userCheckExistByIdRepository;
    private final IBaseRepository<Integer, Integer> avatarUnselectByUserIdRepository;
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
        if (validValidation.isInValid(request, AvatarUpdateRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "avatar"
            );
        }
    }

    /**
     * Update exist avatar
     *
     * @param request request from client
     * @return response to client
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final Map<String, String> dataMap = SecurityUtils.getCurrentAccount(rabbitTemplate);
        final AvatarEntity avatarEntity = AvatarEntityMapper.INSTANCE
                .fromAvatarUpdateRequest((AvatarUpdateRequest) request);
        avatarEntity.setUserId(Integer.parseInt(dataMap.get("userId")));

        if (!userCheckExistByIdRepository.execute(avatarEntity.getUserId())) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "user",
                    "user identity",
                    avatarEntity.getUserId()
            );
        }

        avatarUnselectByUserIdRepository.execute(avatarEntity.getUserId());
        rabbitTemplate.convertSendAndReceive(
                AvatarUnSelectedByUserIdQueueI.EXCHANGE,
                AvatarUnSelectedByUserIdQueueI.ROUTING_KEY,
                avatarEntity.getUserId()
        );

        avatarEntity.setSelected(true);

        final int avatarId = avatarUpdateRepository.execute(avatarEntity);
        avatarEntity.setId(avatarId);

        rabbitTemplate.convertSendAndReceive(
                AvatarUpdateQueueI.EXCHANGE,
                AvatarUpdateQueueI.ROUTING_KEY,
                avatarEntity
        );

        return new SuccessResponse<>(
                new SuccessPojo<>(
                        avatarId,
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
