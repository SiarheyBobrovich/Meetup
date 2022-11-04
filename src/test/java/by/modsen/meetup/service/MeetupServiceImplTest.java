package by.modsen.meetup.service;

import by.modsen.config.ServiceTestConfig;
import by.modsen.meetup.converters.MeetupDtoToMeetupConverter;
import by.modsen.meetup.utils.LocalDateTimeUtil;
import by.modsen.meetup.MeetupApplication;
import by.modsen.meetup.dao.api.MeetupDao;
import by.modsen.meetup.dto.request.MeetupDto;
import by.modsen.meetup.entity.Meetup;
import by.modsen.meetup.exceptions.IllegalIdException;
import by.modsen.meetup.exceptions.MeetupNotFoundException;
import by.modsen.meetup.service.api.MeetupService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {
        MeetupServiceImpl.class,
        ServiceTestConfig.class,
        MeetupApplication.class,
        MeetupDtoToMeetupConverter.class
})
@ActiveProfiles("test")
class MeetupServiceImplTest {

    @Autowired
    MeetupService meetupService;

    @Autowired
    MeetupDao meetupDao;

    @Test
    void getAll() {
        Meetup meetup = getMeetup();
        Mockito.when(meetupDao.getAll()).thenReturn(Set.of(meetup));

        Set<Meetup> meetups = meetupService.getAll();

        assertNotNull(meetups);
        assertEquals(1, meetups.size());
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 81724141147L, Long.MAX_VALUE})
    void getById(Long id) {
        Meetup meetup = getMeetup();
        meetup.setId(id);

        Mockito.when(meetupDao.getById(1L)).thenReturn(meetup);

        Meetup currentMeetup = meetupService.getById(1L);
        assertEquals(meetup, currentMeetup);
    }

    @ParameterizedTest
    @ValueSource(longs = {Long.MIN_VALUE, -1, 0})
    void getByIdFailed(Long id) {

        Mockito.when(meetupDao.getById(id)).thenThrow(MeetupNotFoundException.class);
        assertThrows(IllegalIdException.class, () -> meetupService.getById(id));
    }

    @Test
    void getByIdFailedByNull() {
        Mockito.when(meetupDao.getById(null)).thenThrow(MeetupNotFoundException.class);
        assertThrows(ValidationException.class, () -> meetupService.getById(null));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/meetups.csv", delimiter = ';')
    void save(String title, String description, String organization, String place) {
        Mockito.when(meetupDao.save(Mockito.any())).thenReturn(1L);

        LocalDateTime time = LocalDateTimeUtil.truncatedToMillis(LocalDateTime.now());
        MeetupDto meetupDto = new MeetupDto(title, description, organization, place, time);

        Long id = meetupService.save(meetupDto);

        assertEquals(1, id);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/meetups_fail.csv", numLinesToSkip = 1, delimiter = ';')
    void saveFail(String title, String description, String organization, String place) {
        Mockito.when(meetupDao.save(Mockito.any())).thenReturn(0L);

        LocalDateTime time = LocalDateTimeUtil.truncatedToMillis(LocalDateTime.now());
        MeetupDto meetupDto = new MeetupDto(title, description, organization, place, time);

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
        Mockito.doNothing().when(meetupDao).update(Mockito.any());

        LocalDateTime time = LocalDateTimeUtil.truncatedToMillis(LocalDateTime.now());
        MeetupDto meetupDto = new MeetupDto(title, description, organization, place, time);

        assertDoesNotThrow(() -> meetupService.update(meetupDto, 1L, LocalDateTime.now()));
    }

    @Test
    void updateFailedByNull() {
        Mockito.doNothing().when(meetupDao).update(Mockito.any());
        LocalDateTime time = LocalDateTime.now();

        assertThrows(ValidationException.class, () -> meetupService.update(null, 1L, time));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/meetups_fail.csv", numLinesToSkip = 1, delimiter = ';')
    void updateFailed(String title, String description, String organization, String place) {
        Mockito.doNothing().when(meetupDao).update(Mockito.any());

        LocalDateTime time = LocalDateTimeUtil.truncatedToMillis(LocalDateTime.now());
        MeetupDto meetupDto = new MeetupDto(title, description, organization, place, time);
        LocalDateTime dtUpdate = LocalDateTime.now();

        assertThrows(ValidationException.class, () -> meetupService.update(meetupDto, 1L, dtUpdate));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/meetups_failed_by_id.csv", delimiter = ';')
    void updateFailedById(String title, String description, String organization, String place, Long id) {
        Mockito.doNothing().when(meetupDao).update(Mockito.any());

        LocalDateTime time = LocalDateTimeUtil.truncatedToMillis(LocalDateTime.now());
        MeetupDto meetupDto = new MeetupDto(title, description, organization, place, time);
        LocalDateTime dtUpdate = LocalDateTime.now();

        assertThrows(IllegalIdException.class, () -> meetupService.update(meetupDto, id, dtUpdate));
    }

    @ParameterizedTest
    @ValueSource(longs = {Long.MAX_VALUE, 1, 56465465189L})
    void delete(Long id) {
        Mockito.doNothing().when(meetupDao).delete(id);
        assertDoesNotThrow(() -> meetupService.delete(id));
    }

    @ParameterizedTest
    @ValueSource(longs = {Long.MIN_VALUE, -1892347178246L, 0})
    void deleteFailedById(Long id) {
        Mockito.doNothing().when(meetupDao).delete(id);
        assertThrows(IllegalIdException.class, () -> meetupService.delete(id));
    }

    @Test
    void deleteFailedById() {
        Mockito.doNothing().when(meetupDao).delete(null);
        assertThrows(ValidationException.class, () -> meetupService.delete(null));
    }

    private Meetup getMeetup() {
        Meetup meetup = new Meetup();

        meetup.setTitle("First");
        meetup.setDescription("desc");
        meetup.setOrganization("organization");
        meetup.setPlace("place");
        meetup.setDtMeetup(LocalDateTime.now());

        return meetup;
    }
}