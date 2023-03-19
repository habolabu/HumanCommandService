package edu.ou.humancommandservice.repository.user;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import edu.ou.humancommandservice.common.constant.CodeStatus;
import edu.ou.humancommandservice.data.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class UserUpdateRepository extends BaseRepository<UserEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    /**
     * Validate user
     *
     * @param user user
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(UserEntity user) {
        if (validValidation.isInValid(user, UserEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "user"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Update exist user
     *
     * @param user user information
     * @return id of updated user
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(UserEntity user) {
        try {
            entityTransaction.begin();
            entityManager
                    .find(
                            UserEntity.class,
                            user.getId()
                    )
                    .setFirstName(user.getFirstName())
                    .setLastName(user.getLastName())
                    .setAddress(user.getAddress())
                    .setIdCard(user.getIdCard())
                    .setPhoneNumber(user.getPhoneNumber())
                    .setEmail(user.getEmail())
                    .setGender(user.isGender())
                    .setDateOfBirth(user.getDateOfBirth())
                    .setNationality(user.getNationality())
                    .setIsDeleted(user.getIsDeleted());
            entityTransaction.commit();

            return user.getId();

        } catch (Exception e) {
            entityTransaction.rollback();

            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.DELETE_FAIL,
                    "user",
                    "user identity",
                    user
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
