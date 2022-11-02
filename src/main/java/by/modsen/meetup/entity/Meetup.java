package by.modsen.meetup.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(schema = "modsen", name = "meetups")
public class Meetup {

    private long id;
    private String title;
    private String description;
    private String  organization;
    private String place;
    private LocalDateTime dtMeetup;
    private LocalDateTime dtUpdate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "title", nullable = false, length = 100)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "organization",  length = 100, nullable = false)
    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    @Column(name = "place", length = 150, nullable = false)
    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Column(name = "dt_meetup", nullable = false)
    public LocalDateTime getDtMeetup() {
        return dtMeetup;
    }

    public void setDtMeetup(LocalDateTime dtMeetup) {
        this.dtMeetup = dtMeetup;
    }

    @Version
    @Column(name = "dt_update", nullable = false)
    public LocalDateTime getDtUpdate() {
        return dtUpdate;
    }

    public void setDtUpdate(LocalDateTime dtUpdate) {
        this.dtUpdate = dtUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Meetup)) return false;
        Meetup meetup = (Meetup) o;
        return id == meetup.id && Objects.equals(description, meetup.description) && Objects.equals(organization, meetup.organization) && Objects.equals(place, meetup.place) && Objects.equals(dtMeetup, meetup.dtMeetup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, organization, place, dtMeetup);
    }

    @Override
    public String toString() {
        return "Meetup{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", organization='" + organization + '\'' +
                ", place='" + place + '\'' +
                ", dtMeetup=" + dtMeetup +
                '}';
    }
}
