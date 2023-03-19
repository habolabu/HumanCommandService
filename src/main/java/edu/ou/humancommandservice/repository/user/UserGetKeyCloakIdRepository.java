package edu.ou.humancommandservice.repository.user;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import edu.ou.humancommandservice.common.constant.CodeStatus;
import edu.ou.humancommandservice.data.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class UserGetKeyCloakIdRepository extends BaseRepository<Integer, String> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate user id
     *
     * @param userId id of user
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(Integer userId) {
        if (validValidation.isInValid(userId)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "user identity"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    @Override
    protected String doExecute(Integer userId) {
        final String hqlQuery = "FROM UserEntity U WHERE U.id = :userId";

        try {
            final UserEntity user = (UserEntity) entityManager
                    .unwrap(Session.class)
                    .createQuery(hqlQuery)
                    .setParameter(
                            "userId",
                            userId
                    )
                    .getSingleResult();
            return user.getKeyCloakId();

        } catch (NoResultException e) {
            return null;

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.SERVER_ERROR,
                    Message.Error.UN_KNOWN
            );
        }
    }

    /**
     * Close connection
     *
     * @param userId input of task
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void postExecute(Integer userId) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
