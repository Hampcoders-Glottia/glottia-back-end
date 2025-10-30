package com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects;

public enum AttendanceStatuses {
    RESERVED(1),
    CHECKED_IN(2),
    NO_SHOW(3),
    CANCELLED(4);

    private final int value;

    AttendanceStatuses(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static AttendanceStatuses fromValue(int value) {
        for (AttendanceStatuses status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid AttendanceStatuses value: " + value);
    }
}