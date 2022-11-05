package by.modsen.meetup.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE;

@Configuration
public class ControllerConfig implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(Objects.requireNonNull(mapperFactoryBean().getObject()));
        converters.add(converter);
    }

    @Bean
    public Jackson2ObjectMapperFactoryBean mapperFactoryBean() {
        Jackson2ObjectMapperFactoryBean factoryBean = new Jackson2ObjectMapperFactoryBean();

        factoryBean.setObjectMapper(new ObjectMapper());
        factoryBean.setPropertyNamingStrategy(SNAKE_CASE);
        factoryBean.setModulesToInstall(JavaTimeModule.class);

        return factoryBean;
    }

    @Bean
    public MappingJackson2HttpMessageConverter converter(Jackson2ObjectMapperFactoryBean objectMapper) {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(List.of(MediaType.APPLICATION_JSON));
        mappingJackson2HttpMessageConverter.setObjectMapper(Objects.requireNonNull(objectMapper.getObject()));

        return mappingJackson2HttpMessageConverter;
    }

    @Bean
    public RequestMappingHandlerAdapter adapter( MappingJackson2HttpMessageConverter converter) {
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = new RequestMappingHandlerAdapter();
        requestMappingHandlerAdapter.setMessageConverters(List.of(converter));

        return requestMappingHandlerAdapter;
    }

}
