package by.modsen.meetup.converters;

import org.apache.logging.log4j.util.Strings;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class StringToLocalDateConverter implements Converter<String, LocalDate> {

    @Override
    public LocalDate convert(@Nullable String source) {
            return Strings.isEmpty(source) ? null : LocalDate.parse(source, DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
