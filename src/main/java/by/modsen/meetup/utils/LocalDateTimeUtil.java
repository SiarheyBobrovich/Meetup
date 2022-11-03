package by.modsen.meetup.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class LocalDateTimeUtil {
    private LocalDateTimeUtil() {}

    public static LocalDateTime truncatedToMicros(LocalDateTime localDateTime) {
        return localDateTime.truncatedTo(ChronoUnit.MICROS);
    }
}
