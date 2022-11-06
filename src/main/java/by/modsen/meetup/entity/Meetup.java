package by.modsen.meetup.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(schema = "example", name = "meetups")
public class Meetup implements Serializable {

    private long id;
    private String topic;
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

    @Column(name = "topic", nullable = false, length = 100)
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
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
    @Column(name = "dt_update", nullable = false, columnDefinition = "timestamp(3) without time zone")
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
        return id == meetup.id &&
                Objects.equals(topic, meetup.topic) &&
                Objects.equals(description, meetup.description) &&
                Objects.equals(organization, meetup.organization) &&
                Objects.equals(place, meetup.place) &&
                Objects.equals(dtMeetup, meetup.dtMeetup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, topic, description, organization, place, dtMeetup);
    }

    @Override
    public String toString() {
        return "Meetup{" +
                "id=" + id +
                ", topic='" + topic + '\'' +
                ", description='" + description + '\'' +
                ", organization='" + organization + '\'' +
                ", place='" + place + '\'' +
                ", dtMeetup=" + dtMeetup +
                ", dtUpdate=" + dtUpdate +
                '}';
    }
}
