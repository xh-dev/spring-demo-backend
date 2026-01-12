package me.xethh.libs.web.annotations;

import org.springframework.cache.annotation.Cacheable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Cacheable(
        value = "request_cache",
        key = "T(org.springframework.web.context.request.RequestContextHolder).currentRequestAttributes().request.requestURI"
)
public @interface GlobalCache {
}