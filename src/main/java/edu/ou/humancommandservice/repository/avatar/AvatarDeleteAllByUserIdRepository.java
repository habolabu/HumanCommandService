package edu.ou.humancommandservice.repository.avatar;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import edu.ou.humancommandservice.common.constant.CodeStatus;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class AvatarDeleteAllByUserIdRepository extends BaseRepository<Integer, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    /**
     * Validate user id
     *
     * @param userId user id
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
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Delete exist avatar by user id
     *
     * @param userId user id
     * @return row change number
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(Integer userId) {
        final String hqlQuery =
                "UPDATE AvatarEntity A " +
                        "SET A.isDeleted = CURRENT_TIMESTAMP " +
                        "WHERE A.userId = :userId";

        try {
            entityTransaction.begin();
            final int rowChangeNumber =
                    entityManager
                            .unwrap(Session.class)
                            .createQuery(hqlQuery)
                            .setParameter(
                                    "userId",
                                    userId
                            )
                            .executeUpdate();
            entityTransaction.commit();

            if (rowChangeNumber >= 1) {
                return rowChangeNumber;
            }
            return null;

        } catch (Exception e) {
            entityTransaction.rollback();

            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.DELETE_FAIL,
                    "avatar",
                    "user identity",
                    userId
            );
        }
    }

    /**
     * Close connection
     *
     * @param input input of task
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void postExecute(Integer input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
