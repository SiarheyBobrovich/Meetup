package by.modsen.meetup.dao;

import by.modsen.meetup.dao.api.MeetupDao;
import by.modsen.meetup.dao.mapper.api.ModelMapper;
import by.modsen.meetup.entity.Meetup;
import by.modsen.meetup.exceptions.MeetupOptimisticLockException;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class MeetupDaoImpl implements MeetupDao {

    protected final EntityManagerFactory factory;
    private final ModelMapper<Meetup> mapper;

    protected MeetupDaoImpl(EntityManagerFactory factory, ModelMapper<Meetup> mapper) {
        this.factory = factory;
        this.mapper = mapper;
    }

    @Override
    public Set<Meetup> getAll() {
        final CriteriaQuery<Meetup> query = getAllQuery();
        final EntityManager entityManager = beginTransaction();
        final Set<Meetup> meetups = entityManager.createQuery(query)
                .getResultStream().collect(Collectors.toSet());

        commitAndClose(entityManager);

        return meetups;
    }

    @Override
    public Meetup getById(Long id) {
        final EntityManager entityManager = beginTransaction();
        final Meetup meetup = entityManager.find(Meetup.class, id);
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

        if (isInvalidForUpdate(currentMeetup, meetup)) {
            commitAndClose(entityManager);
            throw new MeetupOptimisticLockException("old version");
        }

        mapper.rebase(currentMeetup, meetup);
        commitAndClose(entityManager);
    }

    @Override
    public void delete(Long id) {
        final CriteriaBuilder criteriaBuilder = factory.getCriteriaBuilder();
        final CriteriaDelete<Meetup> delete = criteriaBuilder.createCriteriaDelete(Meetup.class);
        final Root<Meetup> root = delete.from(Meetup.class);
        delete.where(criteriaBuilder.equal(root.get("id"), id));

        final EntityManager entityManager = beginTransaction();
        entityManager.createQuery(delete).executeUpdate();
        commitAndClose(entityManager);
    }

    /**
     * Create new EntityManager and start a resource transaction.
     * @return EntityManager with started transaction
     */
    protected EntityManager beginTransaction() {
        final EntityManager entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();

        return entityManager;
    }

    /**
     * Commit started transaction and close EntityManager
     * @param manager current EntityManager
     */
    protected void commitAndClose(EntityManager manager) {
        final EntityTransaction transaction = manager.getTransaction();

        if (transaction.isActive()) {
            transaction.commit();
        }

        manager.close();
    }

    /**
     * Check version of meetups
     * @param m1 current Meetup
     * @param m2 updated Meetup
     */
    private boolean isInvalidForUpdate(final Meetup m1, final Meetup m2) {
        return Objects.isNull(m1) || Objects.isNull(m2) || !m1.isEqualVersion(m2);
    }

    /**
     * Create "SELECT * FROM meetups"
     * @return created query
     */
    private CriteriaQuery<Meetup> getAllQuery() {
        final CriteriaQuery<Meetup> query = factory.getCriteriaBuilder()
                .createQuery(Meetup.class);

        query.select(query.from(Meetup.class));

        return query;
    }
}
