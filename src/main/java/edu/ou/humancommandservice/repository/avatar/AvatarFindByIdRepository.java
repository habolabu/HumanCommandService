package edu.ou.humancommandservice.repository.avatar;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import edu.ou.humancommandservice.common.constant.CodeStatus;
import edu.ou.humancommandservice.data.entity.AvatarEntity;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class AvatarFindByIdRepository extends BaseRepository<Integer, AvatarEntity> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate avatar id
     *
     * @param avatarId id of avatar
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
    }

    /**
     * Check avatar exist or not by id
     *
     * @param avatarId id of avatar
     * @return exist or not
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected AvatarEntity doExecute(Integer avatarId) {
        final String hqlQuery = "FROM AvatarEntity A WHERE A.id = :avatarId AND A.isDeleted IS NULL";

        try {
            return (AvatarEntity)
                    entityManager
                            .unwrap(Session.class)
                            .createQuery(hqlQuery)
                            .setParameter(
                                    "avatarId",
                                    avatarId
                            )
                            .getSingleResult();

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "avatar",
                    "avatar identity",
                    avatarId
            );

        }
    }

    /**
     * Close connection
     *
     * @param apartmentId input of task
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void postExecute(Integer apartmentId) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
