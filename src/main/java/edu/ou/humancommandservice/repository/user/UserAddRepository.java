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
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class UserAddRepository extends BaseRepository<UserEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate user entity
     *
     * @param userEntity user entity
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(UserEntity userEntity) {
        if (validValidation.isInValid(userEntity, UserEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "user"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Add new user entity
     *
     * @param userEntity input of task
     * @return id of user
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(UserEntity userEntity) {
        try {
            return (Integer)
                    entityManager
                            .unwrap(Session.class)
                            .save(userEntity);

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.ADD_FAIL,
                    "user"
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
    protected void postExecute(UserEntity input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
