package me.xethh.libs.etagCache;

import org.springframework.util.DigestUtils;

public interface ETagService {
    String ETAG_PREFIX = "etag::";
    String getEtag(String urlKey);
    void putEtag(String urlKey, String hash);
    void evictEtag(String urlKey);

    static String genETagValue(byte[] bytes){
        String newHash = DigestUtils.md5DigestAsHex(bytes);
        return "\""+newHash+"\"";
    }

    static String getEtagKey(String urlKey){
        return ETAG_PREFIX + urlKey;
    }
}
