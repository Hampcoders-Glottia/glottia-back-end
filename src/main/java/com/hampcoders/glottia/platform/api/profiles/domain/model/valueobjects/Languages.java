package com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects;

public enum Languages {
    ENGLISH(1),
    SPANISH(2),
    FRENCH(3),
    GERMAN(4),
    ITALIAN(5),
    PORTUGUESE(6),
    CHINESE(7);

    private final int value;

    Languages(int value) {
        this.value = value;
    }
}
