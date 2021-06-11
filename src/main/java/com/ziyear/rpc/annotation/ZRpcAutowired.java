package com.ziyear.rpc.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 功能描述 : Rpc服务注入
 *
 * @author Ziyear 2021-06-05 15:24
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Autowired
public @interface ZRpcAutowired {
    @AliasFor("value")
    String name() default "";

    @AliasFor("name")
    String value() default "";
}
