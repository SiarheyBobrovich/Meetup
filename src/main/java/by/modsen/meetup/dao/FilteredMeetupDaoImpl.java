package by.modsen.meetup.dao;

import by.modsen.meetup.dao.api.FilteredMeetupDao;
import by.modsen.meetup.filter.api.Filter;
import by.modsen.meetup.entity.Meetup;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FilteredMeetupDaoImpl extends MeetupDaoImpl implements FilteredMeetupDao<Meetup> {

    public FilteredMeetupDaoImpl(EntityManagerFactory factory) {
        super(factory);
    }

    @Override
    public List<Meetup> getAll(Filter filter) {
        if (Objects.isNull(filter)) {
            return new ArrayList<>(getAll());
        }

        final EntityManager entityManager = beginTransaction();
        final CriteriaQuery<Meetup> filteredQuery = getFilteredQuery(factory.getCriteriaBuilder(), filter);

        final List<Meetup> resultList = entityManager.createQuery(filteredQuery)
                .getResultList();

        commitAndClose(entityManager);

        return resultList;
    }

    private CriteriaQuery<Meetup> getFilteredQuery(CriteriaBuilder cb , Filter filter) {
        final CriteriaQuery<Meetup> query = cb.createQuery(Meetup.class);
        final Root<Meetup> root = query.from(Meetup.class);
        List<Predicate> predicates = new ArrayList<>();

        query.select(root);

        String topic = filter.getTopic();
        String organization = filter.getOrganization();
        LocalDate date = filter.getDate();
        String sort = filter.getSort();

        if (!Objects.isNull(topic)) {
            predicates.add(cb.like(root.get("topic"), "%" + topic + "%"));
        }

        if (!Objects.isNull(organization)) {
            predicates.add(cb.like(root.get("organization"), "%" + organization + "%"));
        }

        if (!Objects.isNull(date)) {
            LocalDateTime min = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime max = LocalDateTime.of(date, LocalTime.MAX);
            predicates.add(cb.between(root.get("dtMeetup"), min, max));
        }

        if (!Objects.isNull(sort)) {
            query.orderBy(cb.asc(root.get(sort)));
        }

        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }
        return query;
    }
}
