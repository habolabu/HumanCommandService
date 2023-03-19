package edu.ou.humancommandservice.repository.parkingDetail;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import edu.ou.humancommandservice.common.constant.CodeStatus;
import edu.ou.humancommandservice.data.entity.ParkingDetailEntity;
import edu.ou.humancommandservice.data.entity.ParkingDetailEntityPK;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class ParkingDetailAddRepository extends BaseRepository<ParkingDetailEntity, ParkingDetailEntityPK> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate parking detail entity
     *
     * @param parkingDetailEntity parking detail entity
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(ParkingDetailEntity parkingDetailEntity) {
        if (validValidation.isInValid(parkingDetailEntity, ParkingDetailEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "parking detail"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Add new parking detail entity
     *
     * @param parkingDetailEntity input of task
     * @return id of parking detail
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected ParkingDetailEntityPK doExecute(ParkingDetailEntity parkingDetailEntity) {
        try {

            final Session session = entityManager.unwrap(Session.class);
            final Transaction transaction = session.beginTransaction();

            final ParkingDetailEntityPK parkingDetailEntityPK = (ParkingDetailEntityPK)
                    session.save(parkingDetailEntity);

            transaction.commit();
            session.close();

            return parkingDetailEntityPK;

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.ADD_FAIL,
                    "parking detail"
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
    protected void postExecute(ParkingDetailEntity input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
