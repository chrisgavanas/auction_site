package com.webapplication.dto.user;

import org.joda.time.DateTime;

public class SessionInfo {

    private final Integer userId;
    private final DateTime date;

    public SessionInfo(Integer username, DateTime date) {
        this.userId = username;
        this.date = date;
    }

    public Integer getUserId() {
        return userId;
    }

    public DateTime getDate() {
        return date;
    }

}
