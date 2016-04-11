package com.webapplication.api.user;


import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.webapplication.dto.user.UserLogInRequestDto;
import com.webapplication.dto.user.UserLogInResponseDto;
import com.webapplication.dto.user.UserRegisterRequestDto;
import com.webapplication.dto.user.UserRegisterResponseDto;
import com.webapplication.dto.user.UserResponseDto;
import com.webapplication.exception.user.EmailUnverifiedException;
import com.webapplication.exception.user.NotFoundException;
import com.webapplication.exception.user.UserAlreadyExistsException;
import com.webapplication.exception.user.UserAlreadyVerifiedException;
import com.webapplication.exception.user.UserNotFoundException;
import com.webapplication.exception.user.ValidationException;
import com.webapplication.service.user.UserServiceApi;
import com.webapplication.validator.user.UserLogInValidator;
import com.webapplication.validator.user.UserRegisterValidator;
import com.webapplication.validator.user.UserRequestValidator;

@Component
public class UserApiImpl implements UserApi {

    @Autowired
    private UserServiceApi userService;

    @Autowired
    private UserLogInValidator userLogInValidator;

    @Autowired
    private UserRegisterValidator userRegisterValidator;

    @Autowired
    private UserRequestValidator userRequestValidator;

    public UserLogInResponseDto login(@RequestBody UserLogInRequestDto userLogInRequestDto) throws Exception {
        userLogInValidator.validate(userLogInRequestDto);
        return userService.login(userLogInRequestDto);
    }

    public UserRegisterResponseDto register(@RequestBody UserRegisterRequestDto userRegisterRequestDto) throws Exception {
        userRegisterValidator.validate(userRegisterRequestDto);
        return userService.register(userRegisterRequestDto);
    }

    public UserResponseDto getUser(@PathVariable Integer userId) throws Exception {
        userRequestValidator.validate(userId);
        return userService.getUser(userId);
    }

    public void verifyUser(@PathVariable Integer userId) throws Exception {
        userRequestValidator.validate(userId);
        userService.verifyUser(userId);
    }

    @ExceptionHandler(ValidationException.class)
    private void invalidAttributes(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler({UserNotFoundException.class, EmailUnverifiedException.class })
    private void userNotFound(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.UNAUTHORIZED.value());
    }

    @ExceptionHandler({UserAlreadyExistsException.class, UserAlreadyVerifiedException.class})
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
