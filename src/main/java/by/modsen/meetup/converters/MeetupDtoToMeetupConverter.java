package by.modsen.meetup.converters;

import by.modsen.meetup.dto.request.MeetupDto;
import by.modsen.meetup.entity.Meetup;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MeetupDtoToMeetupConverter implements Converter<MeetupDto, Meetup> {

    @Override
    public Meetup convert(MeetupDto source) {
        Meetup meetup = new Meetup();

        meetup.setTopic(source.getTopic());
        meetup.setDescription(source.getDescription());
        meetup.setOrganization(source.getOrganization());
        meetup.setPlace(source.getPlace());
        meetup.setDtMeetup(source.getDtMeetup());

        return meetup;
    }
}
