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
public class AvatarAddRepository extends BaseRepository<AvatarEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate avatar entity
     *
     * @param avatarEntity avatar entity
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(AvatarEntity avatarEntity) {
        if (validValidation.isInValid(avatarEntity, AvatarEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "avatar"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Add new avatar entity
     *
     * @param avatarEntity input of task
     * @return id of avatar
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(AvatarEntity avatarEntity) {
        try {
            return (Integer)
                    entityManager
                            .unwrap(Session.class)
                            .save(avatarEntity);

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.ADD_FAIL,
                    "avatar"
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
    protected void postExecute(AvatarEntity input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
