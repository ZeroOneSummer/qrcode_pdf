package com.tencet.common;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RequestLimit {

    //默认允许访问的最大次数
    int count() default Integer.MAX_VALUE;

    //毫秒
    long time() default 60000;
}
