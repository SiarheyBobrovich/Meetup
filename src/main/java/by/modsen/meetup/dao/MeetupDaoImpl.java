package by.modsen.meetup.dao;

import by.modsen.meetup.dao.api.MeetupDao;
import by.modsen.meetup.entity.Meetup;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.Set;
import java.util.stream.Collectors;

public class MeetupDaoImpl implements MeetupDao<Meetup> {

    protected final EntityManagerFactory factory;

    protected MeetupDaoImpl(EntityManagerFactory factory) {
        this.factory = factory;
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
        CriteriaUpdate<Meetup> updateQuery = getUpdateQuery(meetup);
        final EntityManager entityManager = beginTransaction();

        int i = entityManager.createQuery(updateQuery).executeUpdate();
        commitAndClose(entityManager);

        if (i < 0) {
            throw new OptimisticLockException();
        }
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

    protected CriteriaUpdate<Meetup> getUpdateQuery(Meetup meetup) {
        final CriteriaBuilder criteriaBuilder = factory.getCriteriaBuilder();
        final CriteriaUpdate<Meetup> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Meetup.class);
        final Root<Meetup> root = criteriaUpdate.from(Meetup.class);

        criteriaUpdate.set(root.get("topic"), meetup.getTopic())
                .set(root.get("description"), meetup.getDescription())
                .set(root.get("organization"), meetup.getOrganization())
                .set(root.get("place"), meetup.getPlace())
                .set(root.get("dtMeetup"), meetup.getDtMeetup())
                .set(root.get("version"), meetup.getVersion() + 1)
                .where(criteriaBuilder.equal(root.get("id"), meetup.getId()),
                        criteriaBuilder.equal(root.get("version"), meetup.getVersion()));

        return criteriaUpdate;
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
