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
public class AvatarDeleteRepository extends BaseRepository<Integer, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    /**
     * Validate avatar id
     *
     * @param avatarId avatar id
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(Integer avatarId) {
        if (validValidation.isInValid(avatarId)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "avatar identity"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Delete exist avatarId by avatarId id
     *
     * @param avatarId avatarId id
     * @return row change number
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(Integer avatarId) {
        final String hqlQuery =
                "UPDATE AvatarEntity A " +
                        "SET A.isDeleted = CURRENT_TIMESTAMP " +
                        "WHERE A.id = :avatarId";

        try {
            entityTransaction.begin();
            final int rowChangeNumber =
                    entityManager
                            .unwrap(Session.class)
                            .createQuery(hqlQuery)
                            .setParameter(
                                    "avatarId",
                                    avatarId
                            )
                            .executeUpdate();
            entityTransaction.commit();

            if (rowChangeNumber >= 1) {
                return avatarId;
            }
            return null;

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.DELETE_FAIL,
                    "avatar",
                    "avatar identity",
                    avatarId
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
