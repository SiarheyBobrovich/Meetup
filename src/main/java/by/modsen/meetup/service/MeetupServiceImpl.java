package by.modsen.meetup.service;

import by.modsen.meetup.dao.api.MeetupDao;
import by.modsen.meetup.dto.request.MeetupDto;
import by.modsen.meetup.entity.Meetup;
import by.modsen.meetup.exceptions.IllegalIdException;
import by.modsen.meetup.service.api.MeetupService;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.persistence.OptimisticLockException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Service
@Validated
public class MeetupServiceImpl implements MeetupService {

    private final MeetupDao meetupDao;
    private final ConversionService conversionService;

    public MeetupServiceImpl(MeetupDao meetupDao, ConversionService conversionService) {
        this.meetupDao = meetupDao;
        this.conversionService = conversionService;
    }

    @Override
    public Set<Meetup> getAll() {
        return meetupDao.getAll();
    }

    @Override
    public Meetup getById(Long id) {
        checkId(id);
        return meetupDao.getById(id);
    }

    @Override
    public Long save(MeetupDto meetup) {
        Meetup newMeetup = conversionService.convert(meetup, Meetup.class);
        return meetupDao.save(newMeetup);
    }

    @Override
    public void update(MeetupDto meetup, Long id, LocalDateTime dtUpdate) throws OptimisticLockException {
        checkId(id);

        Meetup updatedMeetup = conversionService.convert(meetup, Meetup.class);

        assert updatedMeetup != null;
        updatedMeetup.setDtUpdate(dtUpdate);
        updatedMeetup.setId(id);

        meetupDao.update(updatedMeetup);
    }

    @Override
    public void delete(Long id) {
        checkId(id);
        meetupDao.delete(id);
    }

    private void checkId(Long id) {
        if (Objects.isNull(id) || id < 1) {
            throw new IllegalIdException(id);
        }
    }
}
