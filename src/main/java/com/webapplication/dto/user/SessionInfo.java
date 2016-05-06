package com.webapplication.dto.user;

import org.joda.time.DateTime;

public class SessionInfo {

    private final Integer userId;
    private final DateTime date;
    private final Boolean isAdmin;

    public SessionInfo(Integer userId, DateTime date, Boolean isAdmin) {
        this.userId = userId;
        this.date = date;
        this.isAdmin = isAdmin;
    }

    public Integer getUserId() {
        return userId;
    }

    public DateTime getDate() {
        return date;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

}
