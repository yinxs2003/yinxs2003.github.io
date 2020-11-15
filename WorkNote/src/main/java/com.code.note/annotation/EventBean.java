package com.code.note.annotation;

import lombok.Data;

@Data
public class EventBean {

    @EventName("coding now...")
    private String name;

    @EventType(eventType = EventType.Type.MEETING)
    private String type;

    @User(id = 1, name = "testName", email = "15090552277@163.com")
    private String user;
}