package me.xethh.libs.web.annotations;

import org.springframework.web.bind.annotation.CrossOrigin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static me.xethh.libs.web.Const.CORS_URL;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@CrossOrigin(origins = CORS_URL, exposedHeaders = "ETag")
public @interface WithCrossOrigin {
}
