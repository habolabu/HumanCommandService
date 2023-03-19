package edu.ou.humancommandservice.repository.emergency;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import edu.ou.humancommandservice.common.constant.CodeStatus;
import edu.ou.humancommandservice.data.entity.EmergencyEntity;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class EmergencyAddRepository extends BaseRepository<EmergencyEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate emergency entity
     *
     * @param emergencyEntity emergency entity
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(EmergencyEntity emergencyEntity) {
        if (validValidation.isInValid(emergencyEntity, EmergencyEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "emergency"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Add new emergency entity
     *
     * @param emergencyEntity input of task
     * @return id of emergency
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(EmergencyEntity emergencyEntity) {
        try {
            return (Integer)
                    entityManager
                            .unwrap(Session.class)
                            .save(emergencyEntity);

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.ADD_FAIL,
                    "emergency"
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
