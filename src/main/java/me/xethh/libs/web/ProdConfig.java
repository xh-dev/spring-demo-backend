package me.xethh.libs.web;

import me.xethh.libs.etagCache.enabling.EnableEtagCacheRedis;
import me.xethh.libs.gobalCache.enabling.EnableGlobalCache;
import me.xethh.libs.l2cache.enabling.EnableL2Cache;
import me.xethh.libs.web.annotations.Prod;
import org.springframework.context.annotation.Configuration;

@Configuration
@Prod
@EnableEtagCacheRedis
@EnableGlobalCache
@EnableL2Cache
public class ProdConfig {
}
