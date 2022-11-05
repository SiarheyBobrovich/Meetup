package by.modsen.meetup.service.api;

import by.modsen.meetup.dto.request.MeetupDto;
import by.modsen.meetup.entity.Meetup;;

import javax.persistence.OptimisticLockException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

public interface MeetupService {
    /**
     * Get all meetups
     * @return All saved Set of meetups
     */
    Set<Meetup> getAll();

    /**
     * Find a meetup by id
     * @param id meetup id
     * @return If found -> meetup
     */
    Meetup getById(@NotNull Long id);

    /**
     * Create new meetup
     * @return id
     */
    Long save(@Valid @NotNull MeetupDto meetup);

    /**
     * @param meetup updated meetup
     * @param id meetup id
     * @param dtUpdate meetup version
     * @throws OptimisticLockException if dtUpdate is not equals
     */
    void update(@Valid @NotNull MeetupDto meetup,
                @NotNull Long id,
                @NotNull LocalDateTime dtUpdate) throws OptimisticLockException;

    /**
     * Deleted R by id
     * @param id R's id
     */
    void delete(@NotNull Long id);
}
