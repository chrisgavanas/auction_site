package com.webapplication.dto.user;

public class UserLogInResponseDto {
	private Integer userId;
	private String authToken;

	public Integer getUseId() {
		return userId;
	}

	public void setUseId(Integer userId) {
		this.userId = userId;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
}
