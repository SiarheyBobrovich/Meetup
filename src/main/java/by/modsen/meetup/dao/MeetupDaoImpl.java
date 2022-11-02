package by.modsen.meetup.dao;

import by.modsen.meetup.dao.api.MeetupDao;
import by.modsen.meetup.entity.Meetup;
import by.modsen.meetup.exceptions.MeetupNotFoundException;
import by.modsen.meetup.mapper.MeetupMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.List;
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
    public Meetup getById(Long id) throws MeetupNotFoundException {
        final EntityManager entityManager = beginTransaction();
        final CriteriaQuery<Meetup> query = getSelectByIdQuery(id);
        final Meetup meetup = entityManager.createQuery(query)
                .getResultStream()
                .findFirst()
                .orElseThrow(() -> {
                    commitAndClose(entityManager);
                    throw new MeetupNotFoundException("there is no meetups with id: " + id);
                });

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

        checkDtUpdate(currentMeetup, meetup, entityManager);
        mapper.merge(currentMeetup, meetup);
        commitAndClose(entityManager);
    }

    @Override
    public void delete(Long id) {
        final EntityManager entityManager = beginTransaction();
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Meetup> query = criteriaBuilder.createQuery(Meetup.class);
        final Root<Meetup> from = query.from(Meetup.class);

        query.select(from).where(criteriaBuilder.equal(from.get("id"), id));
        entityManager.createQuery(query).executeUpdate();
        commitAndClose(entityManager);
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

            throw new OptimisticLockException();
        }
    }

    /**
     * Create "SELECT * FROM meetups WHERE id = ?"
     * @param id Meetup id
     * @return created query
     */
    private CriteriaQuery<Meetup> getSelectByIdQuery(Long id) {
        final CriteriaBuilder criteriaBuilder = factory.getCriteriaBuilder();
        final CriteriaQuery<Meetup> query = criteriaBuilder.createQuery(Meetup.class);
        final Root<Meetup> root = query.from(Meetup.class);

        query.select(root)
                .where(criteriaBuilder.equal(root.get("id"), id));

        return query;
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
