package com.hampcoders.glottia.platform.api.profiles.domain.model.valueobjects;

public enum BusinessRoles {
    Learner(1),
    Partner(2);

    private final int value;

    BusinessRoles(int value) {
        this.value = value;
    }
}
