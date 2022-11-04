package by.modsen.meetup.controller.api;

import by.modsen.meetup.dto.request.MeetupDto;
import by.modsen.meetup.dto.response.ResponseMeetupDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequestMapping("/api/v1/meetup")
public interface MeetupController {

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Set<ResponseMeetupDto>> getAllMeetups();

    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<ResponseMeetupDto> getMeetupById(@PathVariable Long id);

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Long postMeetup(@RequestBody MeetupDto dto);

    @PutMapping("/id/{id}/version/{dt_update}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void putMeetup(@RequestBody MeetupDto dto,
                   @PathVariable("id") Long id,
                   @PathVariable("dt_update") Long dtUpdate
    );

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteMeetup(@PathVariable("id") Long id);
}
