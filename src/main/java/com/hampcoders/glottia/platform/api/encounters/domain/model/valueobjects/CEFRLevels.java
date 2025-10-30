package com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects;

public enum CEFRLevels {
    A1(1),
    A2(2),
    B1(3),
    B2(4),
    C1(5),
    C2(6),
    NATIVE(7);

    private final int value;

    CEFRLevels(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static CEFRLevels fromValue(int value) {
        for (CEFRLevels level : values()) {
            if (level.value == value) {
                return level;
            }
        }
        throw new IllegalArgumentException("Invalid CEFRLevels value: " + value);
    }
}