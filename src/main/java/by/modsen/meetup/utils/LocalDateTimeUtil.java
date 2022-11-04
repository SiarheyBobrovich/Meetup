package by.modsen.meetup.utils;

import java.time.*;
import java.time.temporal.*;

public class LocalDateTimeUtil {
    private LocalDateTimeUtil() {}

    public static LocalDateTime truncatedToMillis(LocalDateTime localDateTime) {
        return localDateTime.truncatedTo(ChronoUnit.MILLIS);
    }

    public static LocalDateTime convertMillisToLocalDateTime(long version) {
        return Instant.ofEpochMilli(version)
                .atZone(ZoneId.of("UTC"))
                .toLocalDateTime();
    }

    public static long convertLocalDateTimeToMillis(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }
}
