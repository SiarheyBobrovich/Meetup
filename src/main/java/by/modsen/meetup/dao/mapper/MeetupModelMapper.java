package by.modsen.meetup.dao.mapper;

import by.modsen.meetup.dao.mapper.api.ModelMapper;
import by.modsen.meetup.entity.Meetup;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class MeetupModelMapper implements ModelMapper<Meetup> {

    public void rebase(Meetup source, Meetup destination) {
        if (Objects.isNull(source) || Objects.isNull(destination)) {
            throw new IllegalArgumentException();
        }

        source.setTopic(destination.getTopic());
        source.setDescription(destination.getDescription());
        source.setOrganization(destination.getOrganization());
        source.setPlace(destination.getPlace());
        source.setDtMeetup(destination.getDtMeetup());
    }
}
