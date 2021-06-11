package com.ziyear.rpc.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 功能描述 : Rpc服务实现注解
 *
 * @author Ziyear 2021-06-05 15:24
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ZRpcService {
    @AliasFor("value")
    String name() default "";

    @AliasFor("name")
    String value() default "";
}
