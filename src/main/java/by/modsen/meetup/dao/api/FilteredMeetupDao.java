package by.modsen.meetup.dao.api;

import by.modsen.meetup.filter.api.Filter;

import java.util.List;

public interface FilteredMeetupDao<T> extends MeetupDao<T>{

    /**
     * Get all filtered meetups
     * @return List of filtered T
     */
    List<T> getAll(Filter filter);
}
