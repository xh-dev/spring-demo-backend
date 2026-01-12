package me.xethh.libs.etagCache.enabling;

import me.xethh.libs.etagCache.ETagService;
import me.xethh.libs.etagCache.ETagServiceCaffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class EtagCacheCaffeineConfiguration {
    @Bean
    public ETagService etagService(){
        return new ETagServiceCaffeine();
    }

}