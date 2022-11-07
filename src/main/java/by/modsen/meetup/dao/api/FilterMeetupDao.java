package by.modsen.meetup.dao.api;

import by.modsen.meetup.dao.filter.api.Filter;
import by.modsen.meetup.entity.Meetup;

import java.util.List;

public interface FilterMeetupDao extends MeetupDao{
    List<Meetup> getFilteredMeetups(Filter filter);
}
