package edu.ou.humancommandservice.service.user;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.auth.external.account.AccountAddQueueE;
import edu.ou.coreservice.queue.human.internal.avatar.AvatarAddQueueI;
import edu.ou.coreservice.queue.human.internal.user.UserAddQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import edu.ou.humancommandservice.common.constant.CodeStatus;
import edu.ou.humancommandservice.common.mapper.UserEntityMapper;
import edu.ou.humancommandservice.common.util.KeyCloakUtils;
import edu.ou.humancommandservice.data.entity.AvatarEntity;
import edu.ou.humancommandservice.data.entity.UserEntity;
import edu.ou.humancommandservice.data.pojo.request.keycloak.KeyCloakAddUserRequest;
import edu.ou.humancommandservice.data.pojo.request.user.UserAddRequest;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class UserAddService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<UserEntity, Integer> userAddRepository;
    private final IBaseRepository<AvatarEntity, Integer> avatarAddRepository;
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
        if (validValidation.isInValid(request, UserAddRequest.class)) {
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
        final UserEntity userEntity = UserEntityMapper.INSTANCE.fromUserAddRequest((UserAddRequest) request);

        final String keyCloakId = this.saveToKeyCloak(userEntity);

        if (Objects.isNull(keyCloakId)) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.ADD_FAIL,
                    "user"
            );
        }
        userEntity.setKeyCloakId(keyCloakId);

        final int userId = userAddRepository.execute(userEntity);
        userEntity.setId(userId);

        this.saveDefaultAvatar(userId);
        this.saveDefaultAccount(userEntity);

        rabbitTemplate.convertSendAndReceive(
                UserAddQueueI.EXCHANGE,
                UserAddQueueI.ROUTING_KEY,
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
     * Save default avatar for new user
     *
     * @param userId new user id
     * @author Nguyen Trung Kien - OU
     */
    private void saveDefaultAvatar(int userId) {
        final AvatarEntity defaultAvatar = new AvatarEntity().setUserId(userId);
        final int avatarId = avatarAddRepository.execute(defaultAvatar);
        defaultAvatar.setId(avatarId);

        rabbitTemplate.convertSendAndReceive(
                AvatarAddQueueI.EXCHANGE,
                AvatarAddQueueI.ROUTING_KEY,
                defaultAvatar
        );
    }

    /**
     * Save default account for new user
     *
     * @param userEntity user
     * @author Nguyen Trung Kien - OU
     */
    private void saveDefaultAccount(UserEntity userEntity) {
        rabbitTemplate.convertSendAndReceive(
                AccountAddQueueE.EXCHANGE,
                AccountAddQueueE.ROUTING_KEY,
                new HashMap<>() {
                    {
                        put("username", userEntity.getUsername());
                        put("userId", userEntity.getId());
                        put("roleId", userEntity.getRoleId());
                    }
                }
        );
    }

    /**
     * Add new user to keycloak
     *
     * @param userEntity user info
     * @return keycloak id
     */
    private String saveToKeyCloak(UserEntity userEntity) {
        return KeyCloakUtils
                .createUser(
                        keyCloakInstance,
                        realm,
                        new KeyCloakAddUserRequest()
                                .setEmail(userEntity.getEmail())
                                .setUsername(userEntity.getUsername())
                                .setFirstName(userEntity.getFirstName())
                                .setLastName(userEntity.getLastName())

                )
                .orElse(null);
    }
}
