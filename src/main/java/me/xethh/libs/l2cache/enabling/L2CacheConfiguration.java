package me.xethh.libs.l2cache.enabling;

import me.xethh.libs.l2cache.CacheCleanupFilter;
import me.xethh.libs.l2cache.RequestCacheAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy // Ensures AOP is turned on
public class L2CacheConfiguration {

    @Bean
    public RequestCacheAspect requestCacheAspect() {
        return new RequestCacheAspect();
    }

    @Bean
    public CacheCleanupFilter cacheCleanupFilter() {
        return new CacheCleanupFilter();
    }

}