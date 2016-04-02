package com.webapplication.service;

import com.webapplication.dto.UserLogInRequestDto;
import com.webapplication.dto.UserLogInResponseDto;

public interface UserServiceApi {

	UserLogInResponseDto login(UserLogInRequestDto userLogInRequestDto);
	
}
