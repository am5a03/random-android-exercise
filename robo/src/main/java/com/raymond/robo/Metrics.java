package com.raymond.robo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Raymond on 2016-04-24.
 */
@Retention(RetentionPolicy.CLASS)
@Target({
        ElementType.CONSTRUCTOR,
        ElementType.METHOD,
        ElementType.TYPE
})
public @interface Metrics {
    String eventName() default "";

    String eventLabel() default "";

    String eventAction() default "";
}
