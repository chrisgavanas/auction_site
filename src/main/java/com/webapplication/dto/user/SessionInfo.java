package com.webapplication.dto.user;

import org.joda.time.DateTime;

public class SessionInfo {

    private final String userId;
    private DateTime date;
    private final Boolean isAdmin;

    public SessionInfo(String userId, DateTime date, Boolean isAdmin) {
        this.userId = userId;
        this.date = date;
        this.isAdmin = isAdmin;
    }

    public String getUserId() {
        return userId;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

}
