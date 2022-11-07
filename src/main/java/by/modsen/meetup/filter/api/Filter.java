package by.modsen.meetup.filter.api;

import java.time.LocalDate;

public interface Filter {
    String getTopic();
    String getOrganization();
    LocalDate getDate();
    String getSort();
}
