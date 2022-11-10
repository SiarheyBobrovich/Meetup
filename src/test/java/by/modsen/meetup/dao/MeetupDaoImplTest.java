package by.modsen.meetup.dao;

import by.modsen.meetup.config.DaoConfig;
import by.modsen.meetup.dao.mapper.MeetupModelMapper;
import by.modsen.meetup.utils.LocalDateTimeUtil;
import by.modsen.meetup.dao.api.MeetupDao;
import by.modsen.meetup.entity.Meetup;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.OptimisticLockException;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {DaoConfig.class, MeetupModelMapper.class})
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MeetupDaoImplTest {

    @Autowired
    private MeetupDao dao;

    @Test
    @Order(1)
    void save() {
        assertEquals(1L, dao.save(getMeetup()));
    }

    @Test
    void saveFailed1() {
        Meetup meetup = getMeetup();
        meetup.setTopic(null);

        assertThrows(RuntimeException.class, () -> dao.save(meetup));
    }

    @Test
    void saveFailed2() {
        Meetup meetup = getMeetup();
        meetup.setPlace(null);

        assertThrows(RuntimeException.class, () -> dao.save(meetup));
    }

    @Test
    void saveFailed3() {
        Meetup meetup = getMeetup();
        meetup.setOrganization(null);

        assertThrows(RuntimeException.class, () -> dao.save(meetup));
    }

    @Test
    void saveFailed4() {
        Meetup meetup = getMeetup();
        meetup.setDtMeetup(null);

        assertThrows(RuntimeException.class, () -> dao.save(meetup));
    }

    @Test
    @Order(2)
    void getAll() {
        Set<Meetup> all = dao.getAll();
        assertNotNull(all);
        assertEquals(1, all.size());
        all.forEach( meetup -> {
            assertEquals(1, meetup.getId());
            assertEquals("Concert", meetup.getTopic());
            assertNull(meetup.getDescription());
            assertEquals("Stars", meetup.getOrganization());
            assertEquals("Independent square", meetup.getPlace());
            assertNotNull(meetup.getDtMeetup());
        });
    }

    @Test
    @Order(3)
    void getById1() {
        Meetup meetup = dao.getById(1L);

        assertEquals(1, meetup.getId());
        assertEquals("Concert", meetup.getTopic());
        assertNull(meetup.getDescription());
        assertEquals("Stars", meetup.getOrganization());
        assertEquals("Independent square", meetup.getPlace());
        assertNotNull(meetup.getDtMeetup());
    }

    @Test
    @Order(4)
    void getById2() {
        assertNull(dao.getById(12L));
    }

    @Test
    @Order(5)
    void update() {
        LocalDateTime dtMeetup = LocalDateTimeUtil.truncatedToMillis(LocalDateTime.now());

        Meetup oldMeetup = dao.getById(1L);
        oldMeetup.setTopic("Updated title");
        oldMeetup.setDescription("Updated description");
        oldMeetup.setOrganization("Updated organization");
        oldMeetup.setPlace("Updated place");
        oldMeetup.setDtMeetup(dtMeetup);

        dao.update(oldMeetup);
        Meetup updatedMeetup = dao.getById(1L);

        assertEquals(oldMeetup, updatedMeetup);
    }

    @Test
    @Order(6)
    void getByIdUpdated() {
        Meetup meetup = dao.getById(1L);

        assertEquals(1, meetup.getId());
        assertEquals("Updated title", meetup.getTopic());
        assertEquals("Updated description", meetup.getDescription());
        assertEquals("Updated organization", meetup.getOrganization());
        assertEquals("Updated place", meetup.getPlace());
        assertNotNull(meetup.getDtMeetup());
    }

    @Test
    @Order(7)
    void updateFailed() {
        LocalDateTime dtMeetup = LocalDateTimeUtil.truncatedToMillis(LocalDateTime.now());

        Meetup oldMeetup = dao.getById(1L);
        oldMeetup.setTopic("Updated title 2");
        oldMeetup.setPlace("Updated place 2");
        oldMeetup.setOrganization("Updated organization 2");
        oldMeetup.setDescription("Updated description 2");
        oldMeetup.setDtMeetup(dtMeetup);
        oldMeetup.setDtUpdate(LocalDateTimeUtil.truncatedToMillis(LocalDateTime.now()));

        assertThrows(OptimisticLockException.class,() -> dao.update(oldMeetup));
    }

    @Order(8)
    @ParameterizedTest
    @ValueSource(longs = {Long.MIN_VALUE + 1, 1, Long.MAX_VALUE})
    void delete(Long id) {
        assertDoesNotThrow(() -> dao.delete(id));
    }

    private Meetup getMeetup() {
        Meetup meetup = new Meetup();

        meetup.setTopic("Concert");
        //meetup.setDescription("First meetup");
        meetup.setOrganization("Stars");
        meetup.setPlace("Independent square");
        meetup.setDtMeetup(LocalDateTime.now());

        return meetup;
    }
}