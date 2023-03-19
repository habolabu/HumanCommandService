package edu.ou.humancommandservice.repository.emergency;

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
public class EmergencyDeleteRepository extends BaseRepository<Integer, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    /**
     * Validate emergency id
     *
     * @param emergencyId emergency id
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(Integer emergencyId) {
        if (validValidation.isInValid(emergencyId)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "emergency identity"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Delete exist emergencyId by emergencyId
     *
     * @param emergencyId emergencyId
     * @return row change number
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(Integer emergencyId) {
        final String hqlQuery =
                "UPDATE EmergencyEntity E " +
                        "SET E.isDeleted = CURRENT_TIMESTAMP " +
                        "WHERE E.id = :emergencyId";

        try {
            entityTransaction.begin();
            final int rowChangeNumber =
                    entityManager
                            .unwrap(Session.class)
                            .createQuery(hqlQuery)
                            .setParameter(
                                    "emergencyId",
                                    emergencyId
                            )
                            .executeUpdate();
            entityTransaction.commit();

            if (rowChangeNumber >= 1) {
                return emergencyId;
            }
            return null;

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.DELETE_FAIL,
                    "emergency",
                    "emergency identity",
                    emergencyId
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
