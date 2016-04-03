package com.webapplication.api;


import javax.validation.ValidationException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.webapplication.dto.UserLogInRequestDto;
import com.webapplication.dto.UserLogInResponseDto;
import com.webapplication.dto.UserRegisterRequestDto;
import com.webapplication.dto.UserRegisterResponseDto;

@RestController
@RequestMapping(path = "/api")
public interface UserApi {

	@RequestMapping(path = "/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	UserLogInResponseDto login(UserLogInRequestDto userLogInRequestDto) throws ValidationException;

	@RequestMapping(path = "/user", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	UserRegisterResponseDto register(UserRegisterRequestDto userLogInRequestDto) throws ValidationException;
	
}
	