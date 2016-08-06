package com.webapplication.dto.user;

import java.util.UUID;

public class UserLogInResponseDto {
    private String userId;
    private UUID authToken;
    private Boolean isAdmin;
    
    public UserLogInResponseDto(String userId, UUID authToken, Boolean isAdmin) {
    	this.isAdmin = isAdmin;
        this.userId = userId;
        this.authToken = authToken;
    }

    public String getUseId() {
        return userId;
    }

    public void setUseId(String userId) {
        this.userId = userId;
    }

    public UUID getAuthToken() {
        return authToken;
    }

    public void setAuthToken(UUID authToken) {
        this.authToken = authToken;
    }
    
    public void setIsAdmin(Boolean isAdmin){
    	this.isAdmin = isAdmin;
    }
    
    public Boolean getIsAdmin(){
    	return this.isAdmin;
    }
}
