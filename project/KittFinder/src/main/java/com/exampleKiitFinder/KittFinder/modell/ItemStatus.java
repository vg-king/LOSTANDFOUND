package com.exampleKiitFinder.KittFinder.modell;

public enum ItemStatus {
    LOST("LOST"),
    FOUND_PENDING("FOUND_PENDING"),
    FOUND_CONFIRMED("FOUND_CONFIRMED");

    private final String value;

    ItemStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
