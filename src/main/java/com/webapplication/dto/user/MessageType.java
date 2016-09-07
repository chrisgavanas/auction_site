package com.webapplication.dto.user;


public enum MessageType {
    SENT("SENT"), RECEIVED("RECEIVED");

    public String description;

    private MessageType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
