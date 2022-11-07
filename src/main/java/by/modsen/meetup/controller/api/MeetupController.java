package by.modsen.meetup.controller.api;

import by.modsen.meetup.dao.filter.SortField;
import by.modsen.meetup.dto.request.MeetupDto;
import by.modsen.meetup.dto.response.ResponseMeetupDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/meetup")
public interface MeetupController {

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<List<ResponseMeetupDto>> getAllMeetups(@RequestParam(required = false) String topic,
                                                          @RequestParam(required = false) String organization,
                                                          @RequestParam(required = false) String date,
                                                          @RequestParam(required = false) SortField sort);

    @GetMapping(value = "/id/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<ResponseMeetupDto> getMeetupById(@PathVariable Long id);

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    Long postMeetup(@RequestBody MeetupDto dto);

    @PutMapping(value = "/id/{id}/version/{dt_update}", consumes = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void putMeetup(@RequestBody MeetupDto dto,
                   @PathVariable("id") Long id,
                   @PathVariable("dt_update") Long dtUpdate
    );

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteMeetup(@PathVariable("id") Long id);
}
