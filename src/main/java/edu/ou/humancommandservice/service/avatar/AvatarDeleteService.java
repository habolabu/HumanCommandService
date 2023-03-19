package edu.ou.humancommandservice.service.avatar;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.human.internal.avatar.AvatarDeleteQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import edu.ou.humancommandservice.common.constant.CodeStatus;
import edu.ou.humancommandservice.data.entity.AvatarEntity;
import edu.ou.humancommandservice.data.pojo.request.avatar.AvatarDeleteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AvatarDeleteService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<Integer, Integer> avatarDeleteRepository;
    private final IBaseRepository<Integer, AvatarEntity> avatarFindByIdRepository;
    private final RabbitTemplate rabbitTemplate;
    private final Environment environment;
    private final ValidValidation validValidation;

    /**
     * Validate request
     *
     * @param request input of task
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(IBaseRequest request) {
        if (validValidation.isInValid(request, AvatarDeleteRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "avatar"
            );
        }
    }

    /**
     * delete exist avatar
     *
     * @param request request from client
     * @return response to client
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final AvatarDeleteRequest avatarDeleteRequest = (AvatarDeleteRequest) request;

        final AvatarEntity existAvatar = avatarFindByIdRepository.execute(avatarDeleteRequest.getId());
        if (existAvatar.isSelected()) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "avatar status"
            );
        }

        final int avatarId = avatarDeleteRepository.execute(avatarDeleteRequest.getId());
        rabbitTemplate.convertSendAndReceive(
                AvatarDeleteQueueI.EXCHANGE,
                AvatarDeleteQueueI.ROUTING_KEY,
                avatarId
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
