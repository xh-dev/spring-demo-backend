package me.xethh.libs.etagCache.enabling;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ETagCacheRedisConfiguration.class, ETagCacheFilterConfiguration.class})
public @interface EnableEtagCacheRedis {
}