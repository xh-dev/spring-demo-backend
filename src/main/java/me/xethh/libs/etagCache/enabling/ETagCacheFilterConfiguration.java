package me.xethh.libs.etagCache.enabling;

import me.xethh.libs.etagCache.ETagService;
import me.xethh.libs.etagCache.EtagFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class ETagCacheFilterConfiguration {

    @Bean
    public EtagFilter etagFilter(ETagService etagService) {
        return new EtagFilter(etagService);
    }

}