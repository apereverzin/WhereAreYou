package com.creek.whereareyou.android.infrastructure.sqlite;

/**
 * 
 * @author Andrey Pereverzin
 */
public enum Comparison {
    EQUALS("="),
    NOT_EQUALS("!="),
    GREATER_THAN(">"),
    GREATER_THAN_OR_EQUALS(">="),
    LESS_THAN("<"),
    LESS_THAN_OR_EQUALS("<=");

    private String value;

    private Comparison(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
