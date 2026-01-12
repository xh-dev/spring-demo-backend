package me.xethh.libs.etagCache.enabling;

import me.xethh.libs.etagCache.ETagService;
import me.xethh.libs.etagCache.ETagServiceRedis;
import me.xethh.libs.web.annotations.Prod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@EnableAspectJAutoProxy
@Prod
public class ETagCacheRedisConfiguration {

    @Bean
    public ETagService etagService(StringRedisTemplate redisTemplate){
        return new ETagServiceRedis(redisTemplate);
    }

}