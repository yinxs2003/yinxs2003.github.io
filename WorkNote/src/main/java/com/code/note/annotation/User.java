package com.code.note.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 日程事件名称注解
 */
@Target(FIELD)
@Retention(RUNTIME)
@Documented
public @interface User {

    int id() default 0;

    String name() default "";

    String email() default "";
}