package by.modsen.meetup.dao.api;

import by.modsen.meetup.filter.api.Filter;
import by.modsen.meetup.entity.Meetup;

import java.util.List;

public interface FilteredMeetupDao extends MeetupDao{

    /**
     * Get all filtered meetups
     * @return List of filtered meetups
     */
    List<Meetup> getAll(Filter filter);
}
