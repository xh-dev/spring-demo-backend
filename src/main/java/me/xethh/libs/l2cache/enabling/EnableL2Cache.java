package me.xethh.libs.l2cache.enabling;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(L2CacheConfiguration.class) // The "Magic" part
public @interface EnableL2Cache {
}