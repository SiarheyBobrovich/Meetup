package by.modsen.config;

import by.modsen.meetup.converters.MeetupDtoToMeetupConverter;
import by.modsen.meetup.converters.MeetupToResponseMeetupDtoConverter;
import by.modsen.meetup.dao.FilteredMeetupDaoImpl;
import by.modsen.meetup.dao.api.FilteredMeetupDao;
import by.modsen.meetup.entity.Meetup;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ConversionServiceFactoryBean;

import java.util.Set;

@Configuration
@Profile("test")
public class ServiceTestConfig {

    @Bean
    @Primary
    FilteredMeetupDao<Meetup> meetupDto() {
        return Mockito.mock(FilteredMeetupDaoImpl.class);
    }

    @Bean
    public ConversionServiceFactoryBean conversionService() {
        ConversionServiceFactoryBean conversionServiceFactoryBean = new ConversionServiceFactoryBean();
        conversionServiceFactoryBean.setConverters(Set.of(
                new MeetupDtoToMeetupConverter(),
                new MeetupToResponseMeetupDtoConverter()
        ));

        return conversionServiceFactoryBean;
    }
}
