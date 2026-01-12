package me.xethh.libs.etagCache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;

public class ETagServiceCaffeine implements ETagService {
    private final Cache<String, String> cache;
    public ETagServiceCaffeine(){
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofHours(1))
                .maximumSize(10_000)
                .build();

    }

    @Override
    public String getEtag(String urlKey) {
        return cache.get(ETagService.getEtagKey(urlKey), key -> null);
    }

    @Override
    public void putEtag(String urlKey, String hash) {
        cache.put(ETagService.getEtagKey(urlKey), hash);
    }

    @Override
    public void evictEtag(String urlKey) {
        cache.invalidate(ETagService.getEtagKey(urlKey));
    }
}