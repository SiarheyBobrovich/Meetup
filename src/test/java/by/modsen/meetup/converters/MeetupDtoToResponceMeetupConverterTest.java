package by.modsen.meetup.converters;

import by.modsen.meetup.dto.request.MeetupDto;
import by.modsen.meetup.entity.Meetup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MeetupDtoToMeetupConverter.class)
@ActiveProfiles("test")
class MeetupDtoToResponceMeetupConverterTest {

    @Autowired
    Converter<MeetupDto, Meetup> converter;

    @Test
    void convert() {
        String title = "Title";
        String description = "description";
        String organization = "Olympic";
        String place = "park";
        LocalDateTime dtMeetup = LocalDateTime.now();

        Meetup meetup = converter.convert(MeetupDto.builder()
                .topic(title)
                .description(description)
                .organization(organization)
                .place(place)
                .dtMeetup(dtMeetup)
                .build()
        );

        assertNotNull(meetup);
        assertEquals(0, meetup.getId());
        assertEquals(title, meetup.getTopic());
        assertEquals(description, meetup.getDescription());
        assertEquals(organization, meetup.getOrganization());
        assertEquals(place, meetup.getPlace());
        assertEquals(dtMeetup, meetup.getDtMeetup());
        assertEquals(0, meetup.getVersion());
    }
}