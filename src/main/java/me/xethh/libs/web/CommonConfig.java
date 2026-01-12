package me.xethh.libs.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;

@Configuration
@EnableWebMvc
public class CommonConfig {

    @Bean
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(Arrays.asList(
                MongoConverters.OffsetDateTimeToStringConverter.INSTANCE,
                MongoConverters.StringToOffsetDateTimeConverter.INSTANCE,
                MongoConverters.LocalDateToStringConverter.INSTANCE,
                MongoConverters.StringToLocalDateConverter.INSTANCE
        ));
    }
}