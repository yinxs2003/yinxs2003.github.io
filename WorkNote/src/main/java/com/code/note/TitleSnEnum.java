package com.code.note;

import java.util.Objects;

public enum TitleSnEnum {
    /**
     * 非双标题
     */
    TITLE_SN_0(0),
    /**
     * 双标题
     */
    TITLE_SN_1(1),
    TITLE_SN_2(2);

    private int value;

    TitleSnEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }


}