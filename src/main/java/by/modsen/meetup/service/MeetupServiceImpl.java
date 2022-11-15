package by.modsen.meetup.service;

import by.modsen.meetup.dao.api.FilteredMeetupDao;
import by.modsen.meetup.dto.response.ResponseMeetupDto;
import by.modsen.meetup.exceptions.MeetupOptimisticLockException;
import by.modsen.meetup.filter.api.Filter;
import by.modsen.meetup.dto.request.MeetupDto;
import by.modsen.meetup.entity.Meetup;
import by.modsen.meetup.service.api.MeetupService;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.persistence.OptimisticLockException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class MeetupServiceImpl implements MeetupService<ResponseMeetupDto, MeetupDto> {

    private final FilteredMeetupDao<Meetup> meetupDao;
    private final ConversionService conversionService;

    public MeetupServiceImpl(FilteredMeetupDao<Meetup> meetupDao, ConversionService conversionService) {
        this.meetupDao = meetupDao;
        this.conversionService = conversionService;
    }

    @Override
    public List<ResponseMeetupDto> getAll(Filter filter) {
        return meetupDao.getAll(filter)
                .stream()
                .map(meetup -> conversionService.convert(meetup, ResponseMeetupDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ResponseMeetupDto getById(Long id) {
        return conversionService.convert(meetupDao.getById(id), ResponseMeetupDto.class);
    }

    @Override
    public Long save(MeetupDto meetup) {
        final Meetup newMeetup = conversionService.convert(meetup, Meetup.class);
        return meetupDao.save(newMeetup);
    }

    @Override
    public void update(MeetupDto meetup, Long id, Long version) throws OptimisticLockException {
        final Meetup currentMeetup = meetupDao.getById(id);
        final Meetup updatedMeetup = conversionService.convert(meetup, Meetup.class);
        assert updatedMeetup != null;

        if (currentMeetup == null || currentMeetup.getVersion() != version) {
            throw new MeetupOptimisticLockException("Old version");
        }

        updatedMeetup.setVersion(version);
        updatedMeetup.setId(id);

        meetupDao.update(updatedMeetup);
    }

    @Override
    public void delete(Long id) {
        meetupDao.delete(id);
    }
}
