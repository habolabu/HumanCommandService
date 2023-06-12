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

@Repository
@RequiredArgsConstructor
public class EmergencyFindByIdRepository extends BaseRepository<Integer, EmergencyEntity> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate emergency id
     *
     * @param emergencyId id of emergency
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
    }

    /**
     * Check emergency exist or not by id
     *
     * @param emergencyId id of emergency
     * @return exist or not
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected EmergencyEntity doExecute(Integer emergencyId) {
        final String hqlQuery = "FROM EmergencyEntity E WHERE E.id = :emergencyId AND E.isDeleted IS NULL";

        try {
            return (EmergencyEntity)
                    entityManager
                            .unwrap(Session.class)
                            .createQuery(hqlQuery)
                            .setParameter(
                                    "emergencyId",
                                    emergencyId
                            )
                            .getSingleResult();

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "emergency",
                    "emergency identity",
                    emergencyId
            );

        }
    }

    @Override
    protected void postExecute(Integer input) {
        // do nothing
    }

}
