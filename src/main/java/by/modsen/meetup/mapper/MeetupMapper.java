package by.modsen.meetup.mapper;

import by.modsen.meetup.entity.Meetup;
import org.springframework.stereotype.Component;

@Component
public class MeetupMapper {

    public void merge(Meetup source, Meetup destination) {
        source.setTitle(destination.getTitle());
        source.setDescription(destination.getDescription());
        source.setOrganization(destination.getOrganization());
        source.setPlace(destination.getPlace());
        source.setDtMeetup(destination.getDtMeetup());
    }
}
