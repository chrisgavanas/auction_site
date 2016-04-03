package com.webapplication.api;

import javax.xml.bind.ValidationException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.webapplication.dto.UserLogInRequestDto;
import com.webapplication.dto.UserLogInResponseDto;

@RestController
@RequestMapping(path = "/api")
public interface UserApi {

	@RequestMapping(path = "/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	UserLogInResponseDto login(UserLogInRequestDto userLogInRequestDto) throws ValidationException;

}
	