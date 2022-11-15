package by.modsen.meetup.controller;

import by.modsen.meetup.controller.api.MeetupController;
import by.modsen.meetup.filter.FilterImpl;
import by.modsen.meetup.filter.SortField;
import by.modsen.meetup.filter.api.Filter;
import by.modsen.meetup.dto.request.MeetupDto;
import by.modsen.meetup.dto.response.ResponseMeetupDto;
import by.modsen.meetup.service.api.MeetupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MeetupControllerImpl implements MeetupController {

    private final MeetupService<ResponseMeetupDto, MeetupDto> meetupService;

    public MeetupControllerImpl(MeetupService<ResponseMeetupDto, MeetupDto> meetupService) {
        this.meetupService = meetupService;
    }

    @Override
    public ResponseEntity<List<ResponseMeetupDto>> getAllMeetups(String topic,
                                                                 String organization,
                                                                 String date,
                                                                 SortField sort) {
        final Filter filter = FilterImpl.of(topic, organization, date, sort);

        return ResponseEntity.ok(meetupService.getAll(filter));
    }

    @Override
    public ResponseEntity<ResponseMeetupDto> getMeetupById(Long id) {
        return ResponseEntity.ok()
                .body(meetupService.getById(id));
    }

    @Override
    public Long postMeetup(MeetupDto dto) {
        return meetupService.save(dto);
    }

    @Override
    public void putMeetup(MeetupDto dto, Long id, Long version) {
        meetupService.update(dto, id, version);
    }

    @Override
    public void deleteMeetup(Long id) {
        meetupService.delete(id);
    }
}
