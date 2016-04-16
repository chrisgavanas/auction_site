package com.webapplication.dto.user;

import java.util.UUID;

public class UserLogInResponseDto {
	private Integer userId;
	private UUID authToken;

	public UserLogInResponseDto(Integer userId, UUID authToken) {
		this.userId = userId;
		this.authToken = authToken;
	}

	public Integer getUseId() {
		return userId;
	}

	public void setUseId(Integer userId) {
		this.userId = userId;
	}

	public UUID getAuthToken() {
		return authToken;
	}

	public void setAuthToken(UUID authToken) {
		this.authToken = authToken;
	}
}
