package by.modsen.meetup.dao;

import by.modsen.meetup.dao.api.FilteredMeetupDao;
import by.modsen.meetup.dao.filter.FilterImpl;
import by.modsen.meetup.dao.filter.SortField;
import by.modsen.meetup.dao.filter.api.Filter;
import by.modsen.meetup.entity.Meetup;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilteredMeetupDaoImplTest {

    @Autowired
    private FilteredMeetupDao meetupDao;

    @Test
    @Order(1)
    void saveMeetups() throws IOException {
        final List<String> list = Files.readAllLines(Paths.get("src/test/resources/filtered_meetups.csv"));
        final List<Meetup> meetups = new ArrayList<>(3);

        list.forEach(x -> {
            String[] fields = x.split(";");
            meetups.add(getMeetup(fields[0], fields[1], fields[2], fields[3], fields[4]));
        });

        meetups.forEach(assertDoesNotThrow(() -> meetupDao::save));


    }

    @Test
    void getFilteredMeetups() {
        Filter f = FilterImpl.of("f", null, null, null);

        List<Meetup> filteredMeetups = meetupDao.getAll(f);
        assertEquals("first", filteredMeetups.get(0).getTopic());
        assertEquals(1, filteredMeetups.size());
    }

    @Test
    void getFilteredMeetups2() {
        Filter f = FilterImpl.of("z", null, null, null);

        List<Meetup> filteredMeetups = meetupDao.getAll(f);
        assertEquals(0, filteredMeetups.size());
    }

    @Test
    void getFilteredMeetups3() {
        Filter f = FilterImpl.of(null, "r", null, null);

        List<Meetup> filteredMeetups = meetupDao.getAll(f);
        assertEquals(2, filteredMeetups.size());
    }

    @Test
    void getFilteredMeetupsDtMeetup() {
        Filter f = FilterImpl.of(null, null, LocalDate.ofEpochDay(3333), null);

        List<Meetup> filteredMeetups = meetupDao.getAll(f);
        assertEquals(1, filteredMeetups.size());
        assertEquals("third", filteredMeetups.get(0).getTopic());
    }

    @Test
    void getFilteredMeetupsSort() {
        Filter f = FilterImpl.of(null, null, null, SortField.TOPIC);

        List<Meetup> filteredMeetups = meetupDao.getAll(f);
        assertEquals(3, filteredMeetups.size());
        assertEquals("first", filteredMeetups.get(0).getTopic());
        assertEquals("second", filteredMeetups.get(1).getTopic());
        assertEquals("third", filteredMeetups.get(2).getTopic());
    }

    private Meetup getMeetup(String topic, String desc, String org, String place, String dt) {
        Meetup meetup = new Meetup();
        meetup.setTopic(topic);
        meetup.setDescription(desc);
        meetup.setOrganization(org);
        meetup.setPlace(place);
        meetup.setDtMeetup(LocalDateTime.of(LocalDate.ofEpochDay(Long.parseLong(dt)), LocalTime.MAX));
        return meetup;
    }
}