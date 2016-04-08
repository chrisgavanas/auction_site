package com.webapplication.api;


import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.webapplication.dto.UserIdRequestDto;
import com.webapplication.dto.UserIdResponseDto;
import com.webapplication.dto.UserLogInRequestDto;
import com.webapplication.dto.UserLogInResponseDto;
import com.webapplication.dto.UserRegisterRequestDto;
import com.webapplication.dto.UserRegisterResponseDto;
import com.webapplication.exception.EmailUnverifiedException;
import com.webapplication.exception.NotFoundException;
import com.webapplication.exception.UserAlreadyExists;
import com.webapplication.exception.ValidationException;
import com.webapplication.service.UserServiceApi;
import com.webapplication.validator.UserIdValidator;
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

    
    @Autowired 
    private UserIdValidator userIdValidator;
    



    public UserLogInResponseDto login(@RequestBody UserLogInRequestDto userLogInRequestDto) throws Exception {
        userLogInValidator.validate(userLogInRequestDto);
        return userServiceApi.login(userLogInRequestDto);
    }


	public UserIdResponseDto getUser(@PathVariable String userId) throws Exception {
		UserIdRequestDto userIdRequestDto = new UserIdRequestDto();
		int id = Integer.parseInt(userId);
		userIdRequestDto.setId(id);
		userIdValidator.validate(userIdRequestDto);
		return userServiceApi.getUser(userIdRequestDto);
	}
	

    public UserRegisterResponseDto register(@RequestBody UserRegisterRequestDto userRegisterRequestDto) throws Exception {
        userRegisterValidator.validate(userRegisterRequestDto);
        return userServiceApi.register(userRegisterRequestDto);
    }



    @ExceptionHandler(ValidationException.class)
    private void invalidAttributes(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler({NotFoundException.class, EmailUnverifiedException.class })
    private void userNotFound(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.UNAUTHORIZED.value());
    }

    @ExceptionHandler(UserAlreadyExists.class)
    private void userAlreadyExists(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.CONFLICT.value());
    }


}
