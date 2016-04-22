package com.webapplication.dto.user;

import java.util.UUID;

public class ChangePasswordRequestDto {

    private String password;
    private UUID authToken;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UUID getAuthToken() {
        return authToken;
    }

    public void setAuthToken(UUID authToken) {
        this.authToken = authToken;
    }
}
