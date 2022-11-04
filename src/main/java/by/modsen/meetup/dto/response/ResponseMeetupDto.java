package by.modsen.meetup.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class ResponseMeetupDto implements Serializable {
    private final long id;
    private final String title;
    private final String description;
    private final String organization;
    private final String place;
    private final LocalDateTime dtMeetup;
    private final long version;

    public ResponseMeetupDto(long id,
                             String title,
                             String description,
                             String organization,
                             String place,
                             LocalDateTime dtMeetup,
                             long version) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.organization = organization;
        this.place = place;
        this.dtMeetup = dtMeetup;
        this.version = version;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getOrganization() {
        return organization;
    }

    public String getPlace() {
        return place;
    }

    public LocalDateTime getDtMeetup() {
        return dtMeetup;
    }

    public long getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResponseMeetupDto)) return false;

        ResponseMeetupDto responseMeetupDto = (ResponseMeetupDto) o;

        return id == responseMeetupDto.id &&
                version == responseMeetupDto.version
                && Objects.equals(title, responseMeetupDto.title)
                && Objects.equals(description, responseMeetupDto.description)
                && Objects.equals(organization, responseMeetupDto.organization)
                && Objects.equals(place, responseMeetupDto.place)
                && Objects.equals(dtMeetup, responseMeetupDto.dtMeetup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, organization, place, dtMeetup, version);
    }

    @Override
    public String toString() {
        return "MeetupDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", organization='" + organization + '\'' +
                ", place='" + place + '\'' +
                ", dtMeetup=" + dtMeetup +
                ", version=" + version +
                '}';
    }
}
