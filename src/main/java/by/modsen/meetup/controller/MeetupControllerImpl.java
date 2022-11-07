package by.modsen.meetup.controller;

import by.modsen.meetup.controller.api.MeetupController;
import by.modsen.meetup.filter.FilterImpl;
import by.modsen.meetup.filter.SortField;
import by.modsen.meetup.filter.api.Filter;
import by.modsen.meetup.dto.request.MeetupDto;
import by.modsen.meetup.dto.response.ResponseMeetupDto;
import by.modsen.meetup.entity.Meetup;
import by.modsen.meetup.service.api.MeetupService;
import by.modsen.meetup.utils.LocalDateTimeUtil;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MeetupControllerImpl implements MeetupController {

    private final MeetupService meetupService;
    private final ConversionService conversionService;

    public MeetupControllerImpl(MeetupService meetupService, ConversionService conversionService) {
        this.meetupService = meetupService;
        this.conversionService = conversionService;
    }

    @Override
    public ResponseEntity<List<ResponseMeetupDto>> getAllMeetups(String topic,
                                                                 String organization,
                                                                 String date,
                                                                 SortField sort) {
        final LocalDate filterDate = conversionService.convert(date, LocalDate.class);
        final Filter filter = FilterImpl.of(topic, organization, filterDate, sort);

        final List<Meetup> all = meetupService.getAll(filter);

        return ResponseEntity.ok()
                .body(all.stream().map(meetup ->
                        conversionService.convert(meetup, ResponseMeetupDto.class)
        ).collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<ResponseMeetupDto> getMeetupById(Long id) {
        final Meetup meetup = meetupService.getById(id);
        return ResponseEntity.ok()
                .body(conversionService.convert(meetup, ResponseMeetupDto.class));
    }

    @Override
    public Long postMeetup(MeetupDto dto) {
        return meetupService.save(dto);
    }

    @Override
    public void putMeetup(MeetupDto dto, Long id, Long version) {
        final LocalDateTime dtUpdate = LocalDateTimeUtil.convertMillisToLocalDateTime(version);
        meetupService.update(dto, id, dtUpdate);
    }

    @Override
    public void deleteMeetup(Long id) {
        meetupService.delete(id);
    }
}
