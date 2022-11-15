package by.modsen.meetup.filter;

import by.modsen.meetup.filter.api.Filter;

import java.time.LocalDate;
import java.util.Objects;

public class FilterImpl implements Filter {

    private final String topic;
    private final String organization;
    private final LocalDate date;
    private final SortField sort;

    private FilterImpl(String topic, String organization, LocalDate date, SortField sort) {
        this.topic = topic;
        this.organization = organization;
        this.date = date;
        this.sort = sort;
    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public String getOrganization() {
        return organization;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public String getSort() {
        return Objects.isNull(sort) ? null : sort.getFieldName();
    }

    public static Filter of(String topic, String organization, String date, SortField sort) {
        return new FilterImpl(
                topic,
                organization,
                date == null ? null : LocalDate.parse(date),
                sort
        );
    }
}
