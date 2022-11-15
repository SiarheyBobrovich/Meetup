package by.modsen.meetup.service;

import by.modsen.config.ServiceTestConfig;
import by.modsen.meetup.converters.MeetupToResponseMeetupDtoConverter;
import by.modsen.meetup.dao.api.FilteredMeetupDao;
import by.modsen.meetup.dto.request.MeetupDto;
import by.modsen.meetup.dto.response.ResponseMeetupDto;
import by.modsen.meetup.entity.Meetup;
import by.modsen.meetup.exceptions.MeetupOptimisticLockException;
import by.modsen.meetup.service.api.MeetupService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {
        ValidationAutoConfiguration.class,
        MeetupServiceImpl.class,
        ServiceTestConfig.class,
        MeetupToResponseMeetupDtoConverter.class
})
@ActiveProfiles("test")
class MeetupServiceImplTest {

    @Autowired
    MeetupService<ResponseMeetupDto, MeetupDto> meetupService;

    @Autowired
    FilteredMeetupDao<Meetup> meetupDao;

    @Autowired
    ConversionService conversionService;

    @Test
    void getAll() {
        Meetup meetup = getMeetup();
        Mockito.when(meetupDao.getAll(Mockito.any())).thenReturn(List.of(meetup));

        List<ResponseMeetupDto> meetups = meetupService.getAll(null);

        assertNotNull(meetups);
        assertEquals(1, meetups.size());
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 81724141147L, Long.MAX_VALUE})
    void getById(Long id) {
        Meetup meetup = getMeetup();
        meetup.setId(id);

        Mockito.when(meetupDao.getById(1L)).thenReturn(meetup);

        ResponseMeetupDto currentMeetup = meetupService.getById(1L);
        ResponseMeetupDto expected = conversionService.convert(meetup, ResponseMeetupDto.class);

        assertEquals(expected, currentMeetup);
    }

    @Test
    void getByIdFailedByNull() {
        Mockito.when(meetupDao.getById(null)).thenThrow(NullPointerException.class);
        assertThrows(ValidationException.class, () -> meetupService.getById(null));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/meetups.csv", delimiter = ';')
    void save(String title, String description, String organization, String place) {
        Mockito.when(meetupDao.save(Mockito.any())).thenReturn(1L);

        MeetupDto meetupDto = new MeetupDto(title, description, organization, place, LocalDateTime.now());

        Long id = meetupService.save(meetupDto);

        assertEquals(1, id);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/meetups_fail.csv", numLinesToSkip = 1, delimiter = ';')
    void saveFail(String title, String description, String organization, String place) {
        Mockito.when(meetupDao.save(Mockito.any())).thenReturn(0L);

        MeetupDto meetupDto = new MeetupDto(title, description, organization, place, LocalDateTime.now());

        assertThrows(ValidationException.class, () -> meetupService.save(meetupDto));
    }

    @Test
    void saveFailedByNull() {
        Mockito.when(meetupDao.save(Mockito.any())).thenReturn(0L);
        assertThrows(ValidationException.class, () -> meetupService.save(null));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/meetups.csv", delimiter = ';')
    void saveFailByDtMeetup(String title, String description, String organization, String place) {
        Mockito.when(meetupDao.save(Mockito.any())).thenReturn(0L);

        MeetupDto meetupDto = new MeetupDto(title, description, organization, place, null);

        assertThrows(ValidationException.class, () -> meetupService.save(meetupDto));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/meetups.csv", delimiter = ';')
    void update(String title, String description, String organization, String place) {
        long version = 123456;
        long id = 1;

        Meetup newMeetup = getNewMeetup(id, title, description, organization, place, version);

        Mockito.doNothing().when(meetupDao).update(Mockito.any());
        Mockito.doReturn(newMeetup).when(meetupDao).getById(Mockito.any());

        MeetupDto meetupDto = new MeetupDto(title, description, organization, place, LocalDateTime.now());

        assertDoesNotThrow(() -> meetupService.update(meetupDto, id, version));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/meetups.csv", delimiter = ';')
    void updateFailByOptimisticLock(String title, String description, String organization, String place) {
        long id = 1;
        long version = 123456;
        long oldVersion = 123455;

        Meetup newMeetup = getNewMeetup(id, title, description, organization, place, version);

        Mockito.doNothing().when(meetupDao).update(Mockito.any());
        Mockito.doReturn(newMeetup).when(meetupDao).getById(Mockito.any());

        MeetupDto meetupDto = new MeetupDto(title, description, organization, place, LocalDateTime.now());

        assertThrows(MeetupOptimisticLockException.class, () -> meetupService.update(meetupDto, id, oldVersion));
    }

    @Test
    void updateFailedByNull() {
        Mockito.doNothing().when(meetupDao).update(Mockito.any());

        assertThrows(ValidationException.class, () -> meetupService.update(null, 1L, 1111L));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/meetups_fail.csv", numLinesToSkip = 1, delimiter = ';')
    void updateFailed(String title, String description, String organization, String place) {
        Mockito.doNothing().when(meetupDao).update(Mockito.any());

        MeetupDto meetupDto = new MeetupDto(title, description, organization, place, LocalDateTime.now());

        assertThrows(ValidationException.class, () -> meetupService.update(meetupDto, 1L, 1111L));
    }

    @ParameterizedTest
    @ValueSource(longs = {Long.MAX_VALUE, 1, 56465465189L})
    void delete(Long id) {
        Mockito.doNothing().when(meetupDao).delete(id);
        assertDoesNotThrow(() -> meetupService.delete(id));
    }

    @Test
    void deleteFailedById() {
        Mockito.doNothing().when(meetupDao).delete(null);
        assertThrows(ValidationException.class, () -> meetupService.delete(null));
    }

    private Meetup getMeetup() {
        Meetup meetup = new Meetup();

        meetup.setTopic("First");
        meetup.setDescription("desc");
        meetup.setOrganization("organization");
        meetup.setPlace("place");
        meetup.setDtMeetup(LocalDateTime.now());

        return meetup;
    }

    private Meetup getNewMeetup(Long id, String title, String description, String organization, String place, Long version) {
        Meetup meetup = new Meetup();

        meetup.setId(id);
        meetup.setTopic(title);
        meetup.setDescription(description);
        meetup.setOrganization(organization);
        meetup.setPlace(place);
        meetup.setVersion(version);

        return meetup;
    }
}