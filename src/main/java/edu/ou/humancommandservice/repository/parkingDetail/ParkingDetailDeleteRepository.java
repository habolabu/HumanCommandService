package edu.ou.humancommandservice.repository.parkingDetail;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import edu.ou.humancommandservice.common.constant.CodeStatus;
import edu.ou.humancommandservice.data.entity.ParkingDetailEntityPK;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class ParkingDetailDeleteRepository extends BaseRepository<ParkingDetailEntityPK, ParkingDetailEntityPK> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    /**
     * Validate parking detail id
     *
     * @param parkingDetailEntityPK parking detail id
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(ParkingDetailEntityPK parkingDetailEntityPK) {
        if (validValidation.isInValid(parkingDetailEntityPK, Object.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "parking detail identity"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Delete exist parkingDetailEntityPK by parkingDetailEntityPK
     *
     * @param parkingDetailEntityPK parkingDetailEntityPK
     * @return row change number
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected ParkingDetailEntityPK doExecute(ParkingDetailEntityPK parkingDetailEntityPK) {
        final String hqlQuery = "DELETE FROM ParkingDetailEntity P " +
                "WHERE P.parkingId = :parkingId AND P.userId = :userId";

        try {
            entityTransaction.begin();
            final int rowChangeNumber =
                    entityManager
                            .unwrap(Session.class)
                            .createQuery(hqlQuery)
                            .setParameter(
                                    "parkingId",
                                    parkingDetailEntityPK.getParkingId()
                            )
                            .setParameter(
                                    "userId",
                                    parkingDetailEntityPK.getUserId()
                            )
                            .executeUpdate();
            entityTransaction.commit();

            if (rowChangeNumber >= 1) {
                return parkingDetailEntityPK;
            }
            return null;

        } catch (Exception e) {
            entityTransaction.rollback();

            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.DELETE_FAIL,
                    "parking detail ",
                    "parking detail primary key (parking identity, parking type identity, user identity)",
                    parkingDetailEntityPK.getParkingId() + ", " +
                            parkingDetailEntityPK.getParkingTypeId() + ", " +
                            parkingDetailEntityPK.getUserId()
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
    protected void postExecute(ParkingDetailEntityPK input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
