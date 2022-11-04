package by.modsen.meetup.controller;

import by.modsen.config.ServiceTestConfig;
import by.modsen.meetup.MeetupApplication;
import by.modsen.meetup.controller.api.MeetupController;
import by.modsen.meetup.converters.MeetupToResponseMeetupDtoConverter;
import by.modsen.meetup.dao.api.MeetupDao;
import by.modsen.meetup.dto.request.MeetupDto;
import by.modsen.meetup.dto.response.ResponseMeetupDto;
import by.modsen.meetup.entity.Meetup;
import by.modsen.meetup.utils.LocalDateTimeUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {
        ServiceTestConfig.class,
        MeetupApplication.class
})
@ActiveProfiles("test")
class MeetupControllerImplTest {

    @Autowired
    private MeetupDao meetupDao;
    @Autowired
    private MeetupController meetupController;

    @Autowired
    private MeetupToResponseMeetupDtoConverter converter;

    @Test
    void getAllMeetups() {
        Set<Meetup> meetups = getMeetups();
        Mockito.when(meetupDao.getAll()).thenReturn(meetups);
        ResponseEntity<Set<ResponseMeetupDto>> allMeetups = meetupController.getAllMeetups();

        assertEquals(HttpStatus.OK, allMeetups.getStatusCode());

        Set<ResponseMeetupDto> expected = meetups.stream()
                .map(meetup -> converter.convert(meetup))
                .collect(Collectors.toSet());
        Set<ResponseMeetupDto> actual = allMeetups.getBody();

        assertEquals(expected, actual);
    }

    @Test
    void getMeetupById() {
        Meetup meetup = getSingleMeetup(2);

        Mockito.when(meetupDao.getById(2L)).thenReturn(meetup);

        ResponseEntity<ResponseMeetupDto> meetupById = meetupController.getMeetupById(2L);
        assertEquals(HttpStatus.OK, meetupById.getStatusCode());

        ResponseMeetupDto expected = converter.convert(meetup);
        ResponseMeetupDto actual = meetupById.getBody();

        assertEquals(expected, actual);
    }

    @Test
    void postMeetup() {
        Meetup meetup = getSingleMeetup(1);
        Mockito.when(meetupDao.save(Mockito.any())).thenReturn(1L);
        MeetupDto requestDto = getRequestDto(meetup);
        Long actual = meetupController.postMeetup(requestDto);
        assertEquals(meetup.getId(), actual);
    }

    @Test
    void putMeetup() {
        Meetup meetup = getSingleMeetup(2);
        Mockito.doThrow(RuntimeException.class).when(meetupDao).update(meetup);
        MeetupDto requestDto = getRequestDto(meetup);
        long dtUpdate = LocalDateTimeUtil.convertLocalDateTimeToMillis(LocalDateTimeUtil.truncatedToMillis(LocalDateTime.now()));

        assertThrows(RuntimeException.class, () -> meetupController.putMeetup(requestDto, 2L, dtUpdate));
    }

    @Test
    void deleteMeetup() {
        Mockito.doThrow(RuntimeException.class).when(meetupDao).delete(1L);
        assertThrows(RuntimeException.class, () -> meetupController.deleteMeetup( 1L));
    }

    private Meetup getSingleMeetup(long id) {
        return getMeetups()
                .stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .get();
    }

    private MeetupDto getRequestDto(Meetup meetup) {
        return MeetupDto.builder()
                .title(meetup.getTitle())
                .description(meetup.getDescription())
                .organization(meetup.getOrganization())
                .place(meetup.getPlace())
                .dtMeetup(meetup.getDtMeetup())
                .build();
    }

    private Set<Meetup> getMeetups() {
        Meetup meetup1 = new Meetup();
        Meetup meetup2 = new Meetup();

        meetup1.setId(1);
        meetup1.setTitle("First");
        meetup1.setDescription("Fa");
        meetup1.setOrganization("F");
        meetup1.setPlace("First hotel");
        meetup1.setDtMeetup(LocalDateTime.now());
        meetup1.setDtUpdate(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));

        meetup2.setId(2);
        meetup2.setTitle("Second");
        meetup2.setDescription("Second description");
        meetup2.setOrganization("S");
        meetup2.setPlace("Second hotel");
        meetup2.setDtMeetup(LocalDateTime.now());
        meetup2.setDtUpdate(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));

        return Set.of(meetup1, meetup2);
    }
}