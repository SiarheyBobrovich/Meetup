package by.modsen.meetup.service.api;

import by.modsen.meetup.filter.api.Filter;

import javax.persistence.OptimisticLockException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * @param <R> - response to
 * @param <D> - request dto
 */
public interface MeetupService<R, D> {
    /**
     * Get all D
     * @return All saved Set of D
     */
    List<R> getAll(Filter filter);

    /**
     * Find a D by id
     * @param id D id
     * @return If found -> D
     */
    R getById(@NotNull Long id);

    /**
     * Create new D
     * @return id
     */
    Long save(@Valid @NotNull D meetup);

    /**
     * @param meetup updated D
     * @param id D id
     * @param version D version
     * @throws OptimisticLockException if dtUpdate is not equals
     */
    void update(@Valid @NotNull D meetup,
                @NotNull Long id,
                @NotNull Long version) throws OptimisticLockException;

    /**
     * Deleted R by id
     * @param id R's id
     */
    void delete(@NotNull Long id);
}
