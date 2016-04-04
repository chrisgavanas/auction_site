package com.webapplication.api;


import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;

import com.webapplication.dto.UserLogInRequestDto;
import com.webapplication.dto.UserLogInResponseDto;
import com.webapplication.dto.UserRegisterRequestDto;
import com.webapplication.dto.UserRegisterResponseDto;
import com.webapplication.service.UserServiceApi;
import com.webapplication.validator.UserLogInValidator;
import com.webapplication.validator.UserRegisterValidator;

@Component
public class UserApiImpl implements UserApi {

    @Autowired
    private UserServiceApi userServiceApi;

    @Autowired
    private UserLogInValidator userLogInValidator;
    
    @Autowired
    private UserRegisterValidator userRegisterValidator;
    
    public UserLogInResponseDto login(@RequestBody UserLogInRequestDto userLogInRequestDto) throws Exception {
    	userLogInValidator.validate(userLogInRequestDto);
        return userServiceApi.login(userLogInRequestDto);
    }
    
	public UserRegisterResponseDto register(@RequestBody UserRegisterRequestDto userLogInRequestDto) throws Exception {
		userRegisterValidator.validate(userLogInRequestDto);
		return userServiceApi.register(userLogInRequestDto);
	}
   
    @ExceptionHandler(ValidationException.class)
    private void invalidAttributes(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

}
