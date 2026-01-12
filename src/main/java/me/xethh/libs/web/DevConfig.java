package me.xethh.libs.web;

import me.xethh.libs.etagCache.enabling.EnableEtagCacheCaffeine;
import me.xethh.libs.gobalCache.enabling.EnableCaffeineCache;
import me.xethh.libs.l2cache.enabling.EnableL2Cache;
import me.xethh.libs.web.annotations.Dev;
import org.springframework.context.annotation.Configuration;

@Configuration
@Dev
@EnableEtagCacheCaffeine
@EnableL2Cache
@EnableCaffeineCache
public class DevConfig {
}
