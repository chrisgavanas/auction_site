package com.webapplication.dto.user;


public enum Vote {
    DOWN(-1), UP(1);

    private int value;

    private Vote(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
