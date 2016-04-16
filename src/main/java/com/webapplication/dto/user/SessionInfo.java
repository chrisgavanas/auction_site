package com.webapplication.dto.user;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

public class SessionInfo {

    private final String username;
    private final DateTime date;

    public SessionInfo(String username, DateTime date) {
        this.username = username;
        this.date = date;
    }

    public String getUsername() {

        return username;
    }

    public DateTime getDate() {
        return date;
    }

}
