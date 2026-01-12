package me.xethh.libs.l2cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Arrays;
import java.util.Map;

@Aspect
public class RequestCacheAspect {

    @Around("@annotation(l2Cache)")
    public Object cache(ProceedingJoinPoint joinPoint, L2Cache l2Cache) throws Throwable {


        String key = generateKey(joinPoint);
        Map<String, Object> cache = RequestCacheContext.get();

        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        Object result = joinPoint.proceed();
        cache.put(key, result);
        return result;
    }

    private String generateKey(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringTypeName() + "." + signature.getName();
        Object[] args = joinPoint.getArgs();

        // Using Arrays.deepToString for nested arrays/objects
        return methodName + "(" + Arrays.deepToString(args) + ")";
    }

    private String generateKey(ProceedingJoinPoint jp, String expression) {
        // Use SpEL or simple string concat to create a unique key
        return jp.getSignature().toLongString() + Arrays.toString(jp.getArgs());
    }
}