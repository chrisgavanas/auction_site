package com.webapplication.api.user;


import com.webapplication.dto.user.*;
import com.webapplication.error.user.UserError;
import com.webapplication.exception.*;
import com.webapplication.service.user.UserServiceApi;
import com.webapplication.validator.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserApiImpl implements UserApi {

    @Autowired
    private UserServiceApi userService;

    @Autowired
    private UserRequestValidator userRequestValidator;

    @Override
    public UserLogInResponseDto login(@RequestBody UserLogInRequestDto userLogInRequestDto) throws Exception {
        userRequestValidator.validate(userLogInRequestDto);
        return userService.login(userLogInRequestDto);
    }

    @Override
    public UserRegisterResponseDto register(@RequestBody UserRegisterRequestDto userRegisterRequestDto) throws Exception {
        userRequestValidator.validate(userRegisterRequestDto);
        return userService.register(userRegisterRequestDto);
    }

    @Override
    public UserResponseDto getUser(@RequestHeader UUID authToken, @PathVariable Integer userId) throws Exception {
        Optional.ofNullable(userId).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        if (userId <= 0)
            throw new ValidationException(UserError.INVALID_DATA);

        return userService.getUser(authToken, userId);
    }

    @Override
    public void verifyUser(@RequestHeader UUID authToken, @PathVariable Integer userId) throws Exception {
        Optional.ofNullable(userId).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        if (userId <= 0)
            throw new ValidationException(UserError.INVALID_DATA);

        userService.verifyUser(authToken, userId);
    }

    @Override
    public UserResponseDto updateUser(@PathVariable Integer userId, @RequestBody UserUpdateRequestDto userUpdateRequestDto) throws Exception {
        userRequestValidator.validate(userUpdateRequestDto);
        return userService.updateUser(userUpdateRequestDto);
    }

    @Override
    public void changePassword(@PathVariable Integer userId, @RequestBody ChangePasswordRequestDto changePasswordRequestDto) throws Exception {
        Optional.ofNullable(userId).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        userRequestValidator.validate(changePasswordRequestDto);
        if (userId <= 0)
            throw new ValidationException(UserError.INVALID_DATA);

        userService.changePassword(userId, changePasswordRequestDto);
    }

    @Override
    public void check(@RequestBody MultiValueMap<String, String> formData) {
        formData.forEach((k,v) -> System.out.print("Key: " + k + " Value: " + v));
    }


    @ExceptionHandler(ValidationException.class)
    private void invalidAttributes(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler({UserNotFoundException.class, EmailUnverifiedException.class, NotAuthorizedException.class})
    private void userNotFound(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.UNAUTHORIZED.value());
    }

    @ExceptionHandler({UserAlreadyExistsException.class, UserAlreadyVerifiedException.class, EmailAlreadyInUseException.class})
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

    @ExceptionHandler({ForbiddenException.class, NotAuthenticatedException.class})
    private void notAllowedAction(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.FORBIDDEN.value());
    }
}
