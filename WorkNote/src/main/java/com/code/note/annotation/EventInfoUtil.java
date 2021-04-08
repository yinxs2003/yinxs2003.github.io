package com.code.note.annotation;

import java.lang.reflect.Field;
 
/**
 * 注解处理类
 */
public class EventInfoUtil {

    public static void getEventInfo(Class<?> cla) {
        String strEventName = "事件名称: ";

        String strEventType = "事件类型: ";

        String strUser = "创建人: ";

        Field[] fields = cla.getDeclaredFields();
        EventName eventName;
        EventType eventType;
        User user;

        for (Field field : fields) {
            if (field.isAnnotationPresent(EventName.class)) {
                eventName = field.getAnnotation(EventName.class);
                strEventName = strEventName + eventName.value();
                System.out.println(strEventName);
            } else if (field.isAnnotationPresent(EventType.class)) {
                eventType = field.getAnnotation(EventType.class);
                strEventType = strEventType + eventType.eventType().toString();
                System.out.println(strEventType);
            } else if (field.isAnnotationPresent(User.class)) {
                user = (User) field.getAnnotation(User.class);
                strUser = strUser + " Id: " + user.id() + " name: " + user.name() + " email: " + user.email();
                System.out.println(strUser);
            }
        }
    }
}