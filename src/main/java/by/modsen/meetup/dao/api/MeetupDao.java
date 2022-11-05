package by.modsen.meetup.dao.api;

import by.modsen.meetup.entity.Meetup;

import javax.persistence.OptimisticLockException;
import java.util.Set;

public interface MeetupDao {

    /**
     * Get all meetups
     * @return All saved meetups
     */
    Set<Meetup> getAll();

    /**
     * Find meetup by id
     * @param id Meetup's id
     * @return If found -> Meetup
     */
    Meetup getById(Long id);

    /**
     * Create new meetup
     * @return id
     */
    Long save(Meetup meetup);

    /**
     *
     * @param meetup updated meetup
     * @throws OptimisticLockException if dtUpdate is not equals
     */
    void update(Meetup meetup) throws OptimisticLockException;

    /**
     * Deleted meetup by id
     * @param id meetup's id
     */
    void delete(Long id);
}
