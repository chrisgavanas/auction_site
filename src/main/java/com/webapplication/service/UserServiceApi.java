package com.webapplication.service;

import com.webapplication.dto.UserLogInRequestDto;
import com.webapplication.dto.UserLogInResponseDto;
import com.webapplication.dto.UserRegisterRequestDto;
import com.webapplication.dto.UserRegisterResponseDto;

public interface UserServiceApi {

	UserLogInResponseDto login(UserLogInRequestDto userLogInRequestDto);
	
	UserRegisterResponseDto register(UserRegisterRequestDto userRegisterRequestDto);
}
