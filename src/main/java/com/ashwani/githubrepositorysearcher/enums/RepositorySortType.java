package com.ashwani.githubrepositorysearcher.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RepositorySortType {

    STARS("stars"),
    FORKS("forks"),
    LAST_UPDATED("updated");

    private final String value;

    RepositorySortType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static RepositorySortType fromValue(String value) {

        for (RepositorySortType sortType : RepositorySortType.values()) {

            if (sortType.value.equalsIgnoreCase(value)) {
                return sortType;
            }
        }

        throw new IllegalArgumentException(
                "Invalid sort value: " + value
        );
    }
}