package edu.ou.humancommandservice.repository.roomDetail;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import edu.ou.humancommandservice.common.constant.CodeStatus;
import edu.ou.humancommandservice.data.entity.RoomDetailEntity;
import edu.ou.humancommandservice.data.entity.RoomDetailEntityPK;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class RoomDetailAddRepository extends BaseRepository<RoomDetailEntity, RoomDetailEntityPK> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate room detail entity
     *
     * @param roomDetailEntity room detail entity
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(RoomDetailEntity roomDetailEntity) {
        if (validValidation.isInValid(roomDetailEntity, RoomDetailEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "room detail"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Add new room detail entity
     *
     * @param roomDetailEntity input of task
     * @return id of room detail
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected RoomDetailEntityPK doExecute(RoomDetailEntity roomDetailEntity) {
        try {
            final Session session = entityManager.unwrap(Session.class);
            final Transaction transaction = session.beginTransaction();

            final RoomDetailEntityPK roomDetailEntityPK = (RoomDetailEntityPK)
                    session.save(roomDetailEntity);

            transaction.commit();
            session.close();

            return roomDetailEntityPK;

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.ADD_FAIL,
                    "room detail"
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
    protected void postExecute(RoomDetailEntity input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
