package edu.ou.humancommandservice.service.avatar;

import com.cloudinary.Cloudinary;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.util.SecurityUtils;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.human.internal.avatar.AvatarAddQueueI;
import edu.ou.coreservice.queue.human.internal.avatar.AvatarUnSelectedByUserIdQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import edu.ou.humancommandservice.common.constant.CodeStatus;
import edu.ou.humancommandservice.common.mapper.AvatarEntityMapper;
import edu.ou.humancommandservice.common.util.CloudinaryUtils;
import edu.ou.humancommandservice.data.entity.AvatarEntity;
import edu.ou.humancommandservice.data.pojo.request.avatar.AvatarAddRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class AvatarAddService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<AvatarEntity, Integer> avatarAddRepository;
    private final IBaseRepository<Integer, Boolean> userCheckExistByIdRepository;
    private final IBaseRepository<Integer, Integer> avatarUnselectByUserIdRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ValidValidation validValidation;
    private final Cloudinary cloudinary;

    /**
     * Validate request
     *
     * @param request input of task
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(IBaseRequest request) {
        if (validValidation.isInValid(request, AvatarAddRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "avatar"
            );
        }
    }

    /**
     * Add new avatar
     *
     * @param request request from client
     * @return response to client
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final AvatarEntity avatarEntity = AvatarEntityMapper.INSTANCE
                .fromAvatarAddRequest((AvatarAddRequest) request);
        avatarEntity.setUserId(SecurityUtils.getCurrentAccount(rabbitTemplate).getUserId());

        if (!userCheckExistByIdRepository.execute(avatarEntity.getUserId())) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "user",
                    "user identity",
                    avatarEntity.getUserId()
            );
        }

        try {
            final String url = CloudinaryUtils.uploadImage(
                    cloudinary,
                    avatarEntity.getImage(),
                    "auto",
                    "avatar"
            );
            avatarEntity.setUrl(url);
        } catch (IOException e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.UPLOAD_FAIL
            );
        }

        avatarUnselectByUserIdRepository.execute(avatarEntity.getUserId());
        rabbitTemplate.convertSendAndReceive(
                AvatarUnSelectedByUserIdQueueI.EXCHANGE,
                AvatarUnSelectedByUserIdQueueI.ROUTING_KEY,
                avatarEntity.getUserId()
        );

        avatarEntity.setSelected(true);

        final int avatarId = avatarAddRepository.execute(avatarEntity);
        avatarEntity.setId(avatarId);
        avatarEntity.setImage(null);

        rabbitTemplate.convertSendAndReceive(
                AvatarAddQueueI.EXCHANGE,
                AvatarAddQueueI.ROUTING_KEY,
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
