package me.xethh.libs.gobalCache.enabling;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RedisConfig.class)
public @interface EnableGlobalCache {
}