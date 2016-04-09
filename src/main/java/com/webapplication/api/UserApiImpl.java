package com.webapplication.api;


import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.webapplication.dto.UserLogInRequestDto;
import com.webapplication.dto.UserLogInResponseDto;
import com.webapplication.dto.UserRegisterRequestDto;
import com.webapplication.dto.UserRegisterResponseDto;
import com.webapplication.dto.UserResponseDto;
import com.webapplication.exception.EmailUnverifiedException;
import com.webapplication.exception.NotFoundException;
import com.webapplication.exception.UserNotFoundException;
import com.webapplication.exception.UserAlreadyExists;
import com.webapplication.exception.ValidationException;
import com.webapplication.service.UserServiceApi;
import com.webapplication.validator.UserLogInValidator;
import com.webapplication.validator.UserRegisterValidator;
import com.webapplication.validator.UserRequestValidator;

@Component
public class UserApiImpl implements UserApi {

    @Autowired
    private UserServiceApi userServiceApi;

    @Autowired
    private UserLogInValidator userLogInValidator;

    @Autowired
    private UserRegisterValidator userRegisterValidator;

    @Autowired
    private UserRequestValidator userRequestValidator;

    public UserLogInResponseDto login(@RequestBody UserLogInRequestDto userLogInRequestDto) throws Exception {
        userLogInValidator.validate(userLogInRequestDto);
        return userServiceApi.login(userLogInRequestDto);
    }

    public UserResponseDto getUser(@PathVariable Integer userId) throws Exception {
        userRequestValidator.validate(userId);
        return userServiceApi.getUser(userId);
    }

    public UserRegisterResponseDto register(@RequestBody UserRegisterRequestDto userRegisterRequestDto) throws Exception {
        userRegisterValidator.validate(userRegisterRequestDto);
        return userServiceApi.register(userRegisterRequestDto);
    }

    @ExceptionHandler(ValidationException.class)
    private void invalidAttributes(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler({UserNotFoundException.class, EmailUnverifiedException.class })
    private void userNotFound(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.UNAUTHORIZED.value());
    }

    @ExceptionHandler(UserAlreadyExists.class)
    private void userAlreadyExists(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.CONFLICT.value());
    }

    @ExceptionHandler(NotFoundException.class)
    private void resourceNotFound(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(Exception.class)
    private void genericError(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

}
