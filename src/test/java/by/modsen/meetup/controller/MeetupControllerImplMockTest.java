package by.modsen.meetup.controller;

import by.modsen.meetup.dto.request.MeetupDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MeetupControllerImplMockTest {

    final static LocalDateTime DT_MEETUP = LocalDateTime.parse("2022-11-04T21:22:21.621156");
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    @Order(1)
    @ParameterizedTest
    @CsvFileSource(resources = "/meetups.csv", delimiter = ';')
    void postMeetup(String title, String description, String organization, String place) throws Exception {
        MeetupDto meetupDto = MeetupDto.builder().title(title).description(description)
                .organization(organization).place(place).dtMeetup(DT_MEETUP).build();
        String content = mapper.writeValueAsString(meetupDto);

        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post("http://localhost/api/v1/meetup")
                        .content(content)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status()
                .isCreated());
    }

    @Test
    @Order(2)
    void getAll() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("http://localhost/api/v1/meetup/all")
                .accept(MediaType.APPLICATION_JSON);

        String contentAsString = mvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status()
                        .isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Set<TestResponseMeetupDto> actual = mapper.readValue(
                contentAsString, TypeFactory.defaultInstance()
                        .constructCollectionType(Set.class, TestResponseMeetupDto.class));
        assertEquals(new HashSet<>(getExpectedDto()), actual);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2})
    @Order(3)
    void getById1(Long id) throws Exception {
        RequestBuilder request = getRequest(id);
        String contentAsString = getContentAsString(request);

        TestResponseMeetupDto actual = mapper.readValue(contentAsString, TestResponseMeetupDto.class);
        TestResponseMeetupDto expectedDto = getExpectedDto().stream().filter(m -> m.getId() == id).findFirst().get();
        assertEquals(expectedDto, actual);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2})
    @Order(4)
    void put(Long id) throws Exception {
        RequestBuilder request = getRequest(id);
        String content = getContentAsString(request);
        TestResponseMeetupDto actual = mapper.readValue(content, TestResponseMeetupDto.class);
        long version = actual.getVersion();
        TestResponseMeetupDto dto = getExpectedDto().get(Math.toIntExact(id - 1));
        MeetupDto requestDto = MeetupDto.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .organization(dto.getOrganization())
                .place(dto.getPlace())
                .dtMeetup(dto.getDtMeetup())
                .build();

        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.put("http://localhost/api/v1/meetup/id/" + id +"/version/" + version)
                .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto));

        mvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status()
                        .is2xxSuccessful());
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2})
    @Order(5)
    void delete(Long id) throws Exception {
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.delete("http://localhost/api/v1/meetup/delete/" + id);

        mvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        RequestBuilder getRequest = getRequest(id);
        mvc.perform(getRequest).andExpect(
                MockMvcResultMatchers.content().string(""));
    }

    private RequestBuilder getRequest(Long id) {
        return MockMvcRequestBuilders.get("http://localhost/api/v1/meetup/id/" + id)
                .accept(MediaType.APPLICATION_JSON);
    }

    private String getContentAsString(RequestBuilder request) throws Exception {
        return mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status()
                        .isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    private List<TestResponseMeetupDto> getExpectedDto() {
        return List.of(
                new TestResponseMeetupDto(2, "some title", null, "some organization", "Olimp", DT_MEETUP, 1L),
                new TestResponseMeetupDto(1, "title", "description", "organization", "place", DT_MEETUP, 1L)
        );
    }
}
