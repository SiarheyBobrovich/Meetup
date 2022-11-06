package by.modsen.meetup.controller;

import by.modsen.meetup.dto.response.ResponseMeetupDto;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.time.LocalDateTime;
import java.util.Objects;

@JsonDeserialize(builder = TestResponseMeetupDto.Builder.class)
class TestResponseMeetupDto  extends ResponseMeetupDto{

    public TestResponseMeetupDto(long id, String topic, String description, String organization, String place, LocalDateTime dtMeetup, Long version) {
        super(id, topic, description, organization, place, dtMeetup, version);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TestResponseMeetupDto)) return false;

        TestResponseMeetupDto responseMeetupDto = (TestResponseMeetupDto) o;

        return getId() == responseMeetupDto.getId()
                && Objects.equals(getTopic(), responseMeetupDto.getTopic())
                && Objects.equals(getDescription(), responseMeetupDto.getDescription())
                && Objects.equals(getOrganization(), responseMeetupDto.getOrganization())
                && Objects.equals(getPlace(), responseMeetupDto.getPlace())
                && Objects.equals(getDtMeetup(), responseMeetupDto.getDtMeetup());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTopic(), getDescription(), getOrganization(), getPlace(), getDtMeetup());
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix = "set")
    static class Builder {
        private long id;
        private String topic;
        private String description;
        private String organization;
        private String place;
        private LocalDateTime dtMeetup;
        private long version;

        private Builder() {
        }

        public void setId(long id) {
            this.id = id;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setOrganization(String organization) {
            this.organization = organization;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public void setDtMeetup(LocalDateTime dtMeetup) {
            this.dtMeetup = dtMeetup;
        }

        public void setVersion(long version) {
            this.version = version;
        }

        public TestResponseMeetupDto build() {
            return new TestResponseMeetupDto(id, topic, description, organization, place, dtMeetup, version);
        }
    }
}
