package com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects;

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
}
