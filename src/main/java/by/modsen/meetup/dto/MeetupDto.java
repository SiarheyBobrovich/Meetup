package by.modsen.meetup.dto;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class MeetupDto implements Serializable {
    private final String title;
    private final String description;
    private final String organization;
    private final String place;
    private final LocalDateTime dtMeetup;

    public MeetupDto(String title, String description, String organization, String place, LocalDateTime dtMeetup) {
        this.title = title;
        this.description = description;
        this.organization = organization;
        this.place = place;
        this.dtMeetup = dtMeetup;
    }

    @NotEmpty(message = "must be not empty")
    @Length(max = 100, message = "length must be 0~100 chars")
    @Pattern(regexp = "^\\p{LD}[\\p{Punct}\\p{LD}\\s-]+", message = "title")
    public String getTitle() {
        return title;
    }

    @Pattern(regexp = "^[\\S]+[\\p{Punct}\\p{LD}\\s]+")
    public String getDescription() {
        return description;
    }

    @NotEmpty(message = "must be not empty")
    @Length(max = 100, message = "length must be 0~100 chars")
    @Pattern(regexp = "^\\p{LD}[\\p{LD}\\s]+", message = "must be only alphabetic")
    public String getOrganization() {
        return organization;
    }

    @NotEmpty(message = "must be not empty")
    @Length(max = 150, message = "length must be 0~100 chars")
    @Pattern(regexp = "^\\p{LD}[\\p{LD}\\s]+", message = "must be only alphabetic")
    public String getPlace() {
        return place;
    }

    @NotNull(message = "must be not null")
    public LocalDateTime getDtMeetup() {
        return dtMeetup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeetupDto entity = (MeetupDto) o;
        return Objects.equals(this.title, entity.title) &&
                Objects.equals(this.description, entity.description) &&
                Objects.equals(this.organization, entity.organization) &&
                Objects.equals(this.place, entity.place) &&
                Objects.equals(this.dtMeetup, entity.dtMeetup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, organization, place, dtMeetup);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "title = " + title + ", " +
                "description = " + description + ", " +
                "organization = " + organization + ", " +
                "place = " + place + ", " +
                "dtMeetup = " + dtMeetup + ")";
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {
        private String title;
        private String description;
        private String organization;
        private String place;
        private LocalDateTime dtMeetup;

        private Builder() {
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder organization(String organization) {
            this.organization = organization;
            return this;
        }

        public Builder place(String place) {
            this.place = place;
            return this;
        }

        public Builder dtMeetup(LocalDateTime dtMeetup) {
            this.dtMeetup = dtMeetup;
            return this;
        }

        public MeetupDto build() {
            return new MeetupDto(
                    title,
                    description,
                    organization,
                    place,
                    dtMeetup
            );
        }
    }
}
