package by.modsen.meetup.converters;

import by.modsen.meetup.dto.response.ResponseMeetupDto;
import by.modsen.meetup.entity.Meetup;
import by.modsen.meetup.utils.LocalDateTimeUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MeetupToResponseMeetupDtoConverter implements Converter<Meetup, ResponseMeetupDto> {

    @Override
    public ResponseMeetupDto convert(Meetup source) {
        return new ResponseMeetupDto(source.getId(),
                source.getTopic(),
                source.getDescription(),
                source.getOrganization(),
                source.getPlace(),
                source.getDtMeetup(),
                LocalDateTimeUtil.convertLocalDateTimeToMillis(source.getDtUpdate())
        );
    }
}
