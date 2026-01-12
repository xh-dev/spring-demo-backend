package me.xethh.libs.etagCache;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;

public class ETagServiceRedis implements ETagService {

    private final StringRedisTemplate redisTemplate;
    public ETagServiceRedis(StringRedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }
    public String getEtag(String urlKey) {
        return redisTemplate.opsForValue().get(ETagService.getEtagKey(urlKey));
    }

    public void putEtag(String urlKey, String hash) {
        redisTemplate.opsForValue().set(ETagService.getEtagKey(urlKey), hash, Duration.ofHours(1));
    }

    @Override
    public void evictEtag(String urlKey) {
        redisTemplate.delete(urlKey);
    }
}