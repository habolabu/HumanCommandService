package edu.ou.humancommandservice.repository.emergency;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import edu.ou.humancommandservice.common.constant.CodeStatus;
import edu.ou.humancommandservice.data.entity.EmergencyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class EmergencyUpdateRepository extends BaseRepository<EmergencyEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    /**
     * Validate emergency
     *
     * @param emergency emergency
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(EmergencyEntity emergency) {
        if (validValidation.isInValid(emergency, EmergencyEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "emergency"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Update exist emergency
     *
     * @param emergency emergency information
     * @return id of updated emergency
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(EmergencyEntity emergency) {
        try {
            entityTransaction.begin();
            entityManager
                    .find(
                            EmergencyEntity.class,
                            emergency.getId()
                    )
                    .setName(emergency.getName())
                    .setAddress(emergency.getAddress())
                    .setPhoneNumber(emergency.getPhoneNumber())
                    .setUserId(emergency.getUserId())
                    .setIsDeleted(emergency.getIsDeleted());
            entityTransaction.commit();

            return emergency.getId();

        } catch (Exception e) {
            entityTransaction.rollback();

            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.DELETE_FAIL,
                    "emergency",
                    "emergency identity",
                    emergency
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
    protected void postExecute(EmergencyEntity input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
