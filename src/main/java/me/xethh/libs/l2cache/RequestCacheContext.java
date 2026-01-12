package me.xethh.libs.l2cache;

import java.util.HashMap;
import java.util.Map;

public class RequestCacheContext {
    private RequestCacheContext() {
    }

    private static final ThreadLocal<Map<String, Object>> CONTEXT =
            ThreadLocal.withInitial(HashMap::new);

    public static Map<String, Object> get() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}