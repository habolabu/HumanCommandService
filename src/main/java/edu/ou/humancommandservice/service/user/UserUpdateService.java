package edu.ou.humancommandservice.service.user;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.human.internal.user.UserUpdateQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import edu.ou.humancommandservice.common.constant.CodeStatus;
import edu.ou.humancommandservice.common.mapper.UserEntityMapper;
import edu.ou.humancommandservice.common.util.KeyCloakUtils;
import edu.ou.humancommandservice.data.entity.UserEntity;
import edu.ou.humancommandservice.data.pojo.request.keycloak.KeyCloakUpdateUserRequest;
import edu.ou.humancommandservice.data.pojo.request.user.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserUpdateService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<UserEntity, Integer> userUpdateRepository;
    private final IBaseRepository<Integer, Boolean> userCheckExistByIdRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ValidValidation validValidation;

    private final Keycloak keyCloakInstance;
    @Value("${keycloak.realm}")
    private String realm;

    /**
     * Validate request
     *
     * @param request input of task
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(IBaseRequest request) {
        if (validValidation.isInValid(request, UserUpdateRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "user"
            );
        }
    }

    /**
     * Add new user
     *
     * @param request request from client
     * @return response to client
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final UserEntity userEntity = UserEntityMapper.INSTANCE
                .fromUserUpdateRequest((UserUpdateRequest) request);

        if (!userCheckExistByIdRepository.execute(userEntity.getId())) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "user",
                    "user identity",
                    userEntity.getId()
            );
        }

        this.updateToKeyCloak(userEntity);

        final int userId = userUpdateRepository.execute(userEntity);

        rabbitTemplate.convertSendAndReceive(
                UserUpdateQueueI.EXCHANGE,
                UserUpdateQueueI.ROUTING_KEY,
                userEntity
        );

        return new SuccessResponse<>(
                new SuccessPojo<>(
                        userId,
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
     * Update exist user to keycloak
     *
     * @param userEntity user info
     */
    private void updateToKeyCloak(UserEntity userEntity) {
        KeyCloakUtils.updateUser(
                keyCloakInstance,
                realm,
                userEntity.getKeyCloakId(),
                new KeyCloakUpdateUserRequest()
                        .setEmail(userEntity.getEmail())
                        .setFirstName(userEntity.getFirstName())
                        .setLastName(userEntity.getLastName())

        );
    }
}
