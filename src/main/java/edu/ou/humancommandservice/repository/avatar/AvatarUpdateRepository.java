package edu.ou.humancommandservice.repository.avatar;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import edu.ou.humancommandservice.common.constant.CodeStatus;
import edu.ou.humancommandservice.data.entity.AvatarEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class AvatarUpdateRepository extends BaseRepository<AvatarEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    /**
     * Validate avatar
     *
     * @param avatar avatar
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(AvatarEntity avatar) {
        if (validValidation.isInValid(avatar, AvatarEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "avatar"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Update exist avatar
     *
     * @param avatar avatar information
     * @return id of updated avatar
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(AvatarEntity avatar) {
        try {
            entityTransaction.begin();
            entityManager
                    .find(
                            AvatarEntity.class,
                            avatar.getId()
                    )
                    .setUrl(avatar.getUrl())
                    .setSelected(avatar.isSelected())
                    .setUserId(avatar.getUserId())
                    .setIsDeleted(avatar.getIsDeleted());
            entityTransaction.commit();

            return avatar.getId();

        } catch (Exception e) {
            entityTransaction.rollback();

            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.DELETE_FAIL,
                    "avatar",
                    "avatar identity",
                    avatar
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
