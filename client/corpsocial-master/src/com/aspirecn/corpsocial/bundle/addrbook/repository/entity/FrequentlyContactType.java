package com.aspirecn.corpsocial.bundle.addrbook.repository.entity;

public enum FrequentlyContactType {
    NONE(0),
    PHONE(1),
    WEIXIN(2),
    SMS(3);

    private final int value;

    FrequentlyContactType(int value) {
        this.value = value;
    }

    public static FrequentlyContactType getInstance(int forValue) {
        for (FrequentlyContactType c : FrequentlyContactType.values()) {
            if (c.value() == forValue) {
                return c;
            }
        }
        return null;
    }

    public int value() {
        return value;
    }
}