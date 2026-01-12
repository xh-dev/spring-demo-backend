package me.xethh.libs.etagCache;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.mockito.Mockito.when;

@DisplayName("Test ETag Service")
class TestETagService {
    @Test
    @DisplayName("Test generate etag key")
    void testGenETagKey(){
        final String key = ETagService.getEtagKey("ABC");
        Assertions.assertEquals("etag::ABC", key);
    }
    @Test
    @DisplayName("Test generate etag value")
    void testGenETagValue(){
        final String value = ETagService.genETagValue("ABC".getBytes());
        Assertions.assertEquals("\"902fbdd2b1df0c4f70b4a5d23525e932\"", value);
    }

    @Test
    @DisplayName("Test ETag Service Caffeine")
    void testETagServiceCaffeine(){
        final ETagService etagService = new ETagServiceCaffeine();
        final String key = "K";
        Assertions.assertNull(etagService.getEtag(key));
        etagService.putEtag(key, "V");
        Assertions.assertEquals("V", etagService.getEtag(key));
        etagService.evictEtag(key);
        Assertions.assertNull(etagService.getEtag(key));
    }

    @Test
    @DisplayName("Test ETag Service Redis")
    void testETagServiceRedis(){
        final var mock = Mockito.mock(StringRedisTemplate.class);
        final var mockOps = Mockito.mock(ValueOperations.class);

        final ETagService etagService =new ETagServiceRedis(mock);
        final String key = "K";

        when(mock.opsForValue()).thenReturn(mockOps);
        when(mockOps.get(ETagService.getEtagKey(key))).thenReturn(null, "V", null);

        Assertions.assertNull(etagService.getEtag(key));
        etagService.putEtag(key, "V");
        Assertions.assertEquals("V", etagService.getEtag(key));
        etagService.evictEtag(key);
        Assertions.assertNull(etagService.getEtag(key));
    }
}
