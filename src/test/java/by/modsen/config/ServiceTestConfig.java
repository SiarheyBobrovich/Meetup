package by.modsen.config;

import by.modsen.meetup.dao.FilteredMeetupDaoImpl;
import by.modsen.meetup.dao.api.FilteredMeetupDao;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class ServiceTestConfig {

    @Bean
    @Primary
    FilteredMeetupDao meetupDto() {
        return Mockito.mock(FilteredMeetupDaoImpl.class);
    }
}
