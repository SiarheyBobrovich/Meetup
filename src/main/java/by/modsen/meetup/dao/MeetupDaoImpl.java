package by.modsen.meetup.dao;

import by.modsen.meetup.dao.api.MeetupDao;
import by.modsen.meetup.entity.Meetup;
import by.modsen.meetup.exceptions.MeetupOptimisticLockException;
import by.modsen.meetup.mapper.MeetupMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Repository
public class MeetupDaoImpl implements MeetupDao {

    private final EntityManagerFactory factory;
    private final MeetupMapper mapper;

    public MeetupDaoImpl(EntityManagerFactory factory, MeetupMapper mapper) {
        this.factory = factory;
        this.mapper = mapper;
    }

    @Override
    public Set<Meetup> getAll() {
        final EntityManager entityManager = beginTransaction();
        final CriteriaQuery<Meetup> query = getAllQuery();
        final List<Meetup> meetups = entityManager.createQuery(query)
                .getResultList();

        commitAndClose(entityManager);

        return new HashSet<>(meetups);
    }

    @Override
    public Meetup getById(Long id) {
        final EntityManager entityManager = beginTransaction();
        Meetup meetup = entityManager.find(Meetup.class, id);
        commitAndClose(entityManager);

        return meetup;
    }

    @Override
    public Long save(Meetup meetup) {
        final EntityManager entityManager = beginTransaction();
        entityManager.persist(meetup);
        commitAndClose(entityManager);

        return meetup.getId();
    }

    @Override
    public void update(Meetup meetup) {
        final EntityManager entityManager = beginTransaction();
        final Meetup currentMeetup = entityManager.find(Meetup.class, meetup.getId());

        if (Objects.isNull(currentMeetup)) {
            commitAndClose(entityManager);
            return;
        }

        checkDtUpdate(currentMeetup, meetup, entityManager);
        mapper.merge(currentMeetup, meetup);
        commitAndClose(entityManager);
    }

    @Override
    public void delete(Long id) {
        final EntityManager entityManager = beginTransaction();
        final Meetup currentMeetup = tryToFind(id);

        if (!Objects.isNull(currentMeetup)) {
            entityManager.remove(currentMeetup);
        }

        commitAndClose(entityManager);
    }

    private Meetup tryToFind(Long id) {
            return getById(id);
    }

    /**
     * Create new EntityManager and start a resource transaction.
     * @return EntityManager with started transaction
     */
    private EntityManager beginTransaction() {
        final EntityManager entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();

        return entityManager;
    }

    /**
     * Commit started transaction and close EntityManager
     * @param manager current EntityManager
     */
    private void commitAndClose(EntityManager manager) {
        final EntityTransaction transaction = manager.getTransaction();

        if (transaction.isActive()) {
            transaction.commit();
        }

        manager.close();
    }

    /**
     * Check version of meetups
     * @param m1 first Meetup
     * @param m2 second Meetup
     * @param manager EntityManager to close
     * @throws OptimisticLockException if version did not equal
     */
    private void checkDtUpdate(final Meetup m1, final Meetup m2, final EntityManager manager) {
        if (!m1.getDtUpdate().equals(m2.getDtUpdate())) {
            commitAndClose(manager);

            throw new MeetupOptimisticLockException("old version");
        }
    }

    /**
     * Create "SELECT * FROM meetups"
     * @return created query
     */
    private CriteriaQuery<Meetup> getAllQuery() {
        CriteriaQuery<Meetup> query = factory.getCriteriaBuilder()
                .createQuery(Meetup.class);

        query.select(query.from(Meetup.class));

        return query;
    }
}
