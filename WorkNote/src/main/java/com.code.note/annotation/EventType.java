package com.code.note.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 日程事件类型注解
 */
@Target(FIELD)
@Retention(RUNTIME)
@Documented
public @interface EventType {

    /**
     * 事件类型枚举
     */
    public enum Type {
        COMMON, MEETING, ACTIVITY
    }

    /**
     * 事件类型属性
     *
     * @return
     */
    Type eventType() default Type.COMMON;

}