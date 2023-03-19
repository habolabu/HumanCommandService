package edu.ou.humancommandservice.repository.roomDetail;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import edu.ou.humancommandservice.common.constant.CodeStatus;
import edu.ou.humancommandservice.data.entity.RoomDetailEntityPK;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class RoomDetailDeleteRepository extends BaseRepository<RoomDetailEntityPK, RoomDetailEntityPK> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    /**
     * Validate room detail id
     *
     * @param roomDetailEntityPK room detail id
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(RoomDetailEntityPK roomDetailEntityPK) {
        if (validValidation.isInValid(roomDetailEntityPK, Object.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "room detail identity"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Delete exist roomDetailEntityPK by roomDetailEntityPK
     *
     * @param roomDetailEntityPK roomDetailEntityPK
     * @return row change number
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected RoomDetailEntityPK doExecute(RoomDetailEntityPK roomDetailEntityPK) {
        final String hqlQuery = "DELETE FROM RoomDetailEntity R WHERE R.roomId = :roomId AND R.userId = :userId";

        try {
            entityTransaction.begin();
            final int rowChangeNumber =
                    entityManager
                            .unwrap(Session.class)
                            .createQuery(hqlQuery)
                            .setParameter(
                                    "roomId",
                                    roomDetailEntityPK.getRoomId()
                            )
                            .setParameter(
                                    "userId",
                                    roomDetailEntityPK.getUserId()
                            )
                            .executeUpdate();
            entityTransaction.commit();

            if (rowChangeNumber >= 1) {
                return roomDetailEntityPK;
            }
            return null;

        } catch (Exception e) {
            entityTransaction.rollback();

            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.DELETE_FAIL,
                    "room detail ",
                    "room detail primary key (room identity, user identity)",
                    roomDetailEntityPK.getRoomId() + ", " + roomDetailEntityPK.getUserId()
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
    protected void postExecute(RoomDetailEntityPK input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
