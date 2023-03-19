package edu.ou.humancommandservice.service.user;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.auth.external.account.AccountDeleteQueueE;
import edu.ou.coreservice.queue.human.internal.avatar.AvatarDeleteQueueI;
import edu.ou.coreservice.queue.human.internal.user.UserDeleteQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import edu.ou.humancommandservice.common.constant.CodeStatus;
import edu.ou.humancommandservice.common.util.KeyCloakUtils;
import edu.ou.humancommandservice.data.pojo.request.user.UserDeleteRequest;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserDeleteService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<Integer, Integer> userDeleteRepository;
    private final IBaseRepository<Integer, Boolean> userCheckExistByIdRepository;
    private final IBaseRepository<Integer, Integer> avatarDeleteAllByUserIdRepository;
    private final IBaseRepository<Integer, String> userGetKeyCloakIdRepository;
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
        if (validValidation.isInValid(request, UserDeleteRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "user"
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
        final UserDeleteRequest userDeleteRequest = (UserDeleteRequest) request;

        if (!userCheckExistByIdRepository.execute(userDeleteRequest.getId())) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "user",
                    "user identity",
                    userDeleteRequest.getId()
            );
        }


        final int userId = userDeleteRepository.execute(userDeleteRequest.getId());

        this.deleteAllAvatarOfUser(userId);
        this.deleteAccountOfUser(userId);
        this.deleteUserToKeyCloak(userGetKeyCloakIdRepository.execute(userDeleteRequest.getId()));

        rabbitTemplate.convertSendAndReceive(
                UserDeleteQueueI.EXCHANGE,
                UserDeleteQueueI.ROUTING_KEY,
                userId

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
     * Delete all avatar of user
     *
     * @param userId user id
     * @author Nguyen Trung Kien - OU
     */
    private void deleteAllAvatarOfUser(int userId) {
        avatarDeleteAllByUserIdRepository.execute(userId);

        rabbitTemplate.convertSendAndReceive(
                AvatarDeleteQueueI.EXCHANGE,
                AvatarDeleteQueueI.ROUTING_KEY,
                userId
        );
    }

    /**
     * Delete account of user
     *
     * @param userId user id
     * @author Nguyen Trung Kien - OU
     */
    private void deleteAccountOfUser(int userId) {
        rabbitTemplate.convertSendAndReceive(
                AccountDeleteQueueE.EXCHANGE,
                AccountDeleteQueueE.ROUTING_KEY,
                userId
        );
    }

    /**
     * Delete keycloak user
     *
     * @param keyCloakId keycloak id
     */
    private void deleteUserToKeyCloak(String keyCloakId) {
        KeyCloakUtils.deleteUser(
                keyCloakInstance,
                realm,
                keyCloakId
        );
    }
}
