package by.modsen.meetup.dao.api;

import javax.persistence.OptimisticLockException;
import java.util.Set;

public interface MeetupDao<T> {

    /**
     * Get all T
     * @return All saved T
     */
    Set<T> getAll();

    /**
     * Find T by id
     * @param id T's id
     * @return If found -> T
     */
    T getById(Long id);

    /**
     * Create new T
     * @return id
     */
    Long save(T t);

    /**     *
     * @param t updated T
     * @throws OptimisticLockException if versions is not equals
     */
    void update(T t) throws OptimisticLockException;

    /**
     * Deleted T by id
     * @param id T's id
     */
    void delete(Long id);
}
