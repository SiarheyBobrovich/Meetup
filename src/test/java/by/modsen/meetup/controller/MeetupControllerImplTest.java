package by.modsen.meetup.controller;

import by.modsen.config.ServiceTestConfig;
import by.modsen.meetup.controller.api.MeetupController;
import by.modsen.meetup.converters.MeetupToResponseMeetupDtoConverter;
import by.modsen.meetup.dao.api.FilteredMeetupDao;
import by.modsen.meetup.dto.request.MeetupDto;
import by.modsen.meetup.dto.response.ResponseMeetupDto;
import by.modsen.meetup.entity.Meetup;
import by.modsen.meetup.filter.api.Filter;
import by.modsen.meetup.service.MeetupServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {
        ValidationAutoConfiguration.class,
        MeetupServiceImpl.class,
        ServiceTestConfig.class,
        MeetupControllerImpl.class,
        MeetupToResponseMeetupDtoConverter.class
})
@ActiveProfiles("test")
class MeetupControllerImplTest {

    @Autowired
    private FilteredMeetupDao<Meetup> meetupDao;
    @Autowired
    private MeetupController meetupController;

    @Autowired
    private MeetupToResponseMeetupDtoConverter converter;

    @Test
    void getAllMeetups() {
        List<Meetup> meetups = getMeetups();
        Mockito.when(meetupDao.getAll(Mockito.any(Filter.class))).thenReturn(meetups);

        ResponseEntity<List<ResponseMeetupDto>> allMeetups = meetupController.getAllMeetups(null, null, null, null);
        assertEquals(HttpStatus.OK, allMeetups.getStatusCode());

        List<ResponseMeetupDto> expected = meetups.stream()
                .map(meetup -> converter.convert(meetup))
                .collect(Collectors.toList());
        List<ResponseMeetupDto> actual = allMeetups.getBody();

        assertEquals(expected, actual);
    }

    @Test
    void getMeetupById() {
        long id = 2L;
        Meetup meetup = getSingleMeetup(id);

        Mockito.when(meetupDao.getById(id)).thenReturn(meetup);

        ResponseEntity<ResponseMeetupDto> meetupById = meetupController.getMeetupById(id);
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
        long version = 123456;

        assertThrows(RuntimeException.class, () -> meetupController.putMeetup(requestDto, 2L, version));
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
                .orElseThrow();
    }

    private MeetupDto getRequestDto(Meetup meetup) {
        return MeetupDto.builder()
                .topic(meetup.getTopic())
                .description(meetup.getDescription())
                .organization(meetup.getOrganization())
                .place(meetup.getPlace())
                .dtMeetup(meetup.getDtMeetup())
                .build();
    }

    private List<Meetup> getMeetups() {
        Meetup meetup1 = new Meetup();
        Meetup meetup2 = new Meetup();

        meetup1.setId(1);
        meetup1.setTopic("First");
        meetup1.setDescription("Fa");
        meetup1.setOrganization("F");
        meetup1.setPlace("First hotel");
        meetup1.setDtMeetup(LocalDateTime.now());
        meetup1.setVersion(11111);

        meetup2.setId(2);
        meetup2.setTopic("Second");
        meetup2.setDescription("Second description");
        meetup2.setOrganization("S");
        meetup2.setPlace("Second hotel");
        meetup2.setDtMeetup(LocalDateTime.now());
        meetup2.setVersion(22222);

        return List.of(meetup1, meetup2);
    }
}