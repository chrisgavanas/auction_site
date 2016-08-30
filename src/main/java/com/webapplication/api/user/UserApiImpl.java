package com.webapplication.api.user;


import com.webapplication.dto.user.*;
import com.webapplication.error.user.UserError;
import com.webapplication.exception.*;
import com.webapplication.exception.user.EmailAlreadyInUseException;
import com.webapplication.exception.user.EmailUnverifiedException;
import com.webapplication.exception.user.UserAlreadyExistsException;
import com.webapplication.exception.user.UserAlreadyVerifiedException;
import com.webapplication.exception.user.UserNotFoundException;
import com.webapplication.service.user.UserServiceApi;
import com.webapplication.validator.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
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
    public UserResponseDto getUser(@RequestHeader UUID authToken, @PathVariable String userId) throws Exception {
        Optional.ofNullable(authToken).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        Optional.ofNullable(userId).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        if (userId.isEmpty())
            throw new ValidationException(UserError.INVALID_DATA);

        return userService.getUser(authToken, userId);
    }

    @Override
    public SellerResponseDto getSeller(@PathVariable String sellerId) throws Exception {
        Optional.ofNullable(sellerId).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        if (sellerId.isEmpty())
            throw new ValidationException(UserError.INVALID_DATA);

        return userService.getSeller(sellerId);
    }
    @Override
    public void verifyUser(@RequestHeader UUID authToken, @PathVariable String userId) throws Exception {
        Optional.ofNullable(authToken).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        Optional.ofNullable(userId).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        if (userId.isEmpty())
            throw new ValidationException(UserError.INVALID_DATA);

        userService.verifyUser(authToken, userId);
    }

    @Override
    public UserResponseDto updateUser(@RequestHeader UUID authToken, @PathVariable String userId, @RequestBody UserUpdateRequestDto userUpdateRequestDto) throws Exception {
        Optional.ofNullable(authToken).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        Optional.ofNullable(userId).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        if (userId.isEmpty())
            throw new ValidationException(UserError.INVALID_DATA);

        userRequestValidator.validate(userUpdateRequestDto);
        return userService.updateUser(authToken, userId, userUpdateRequestDto);
    }

    @Override
    public void changePassword(@RequestHeader UUID authToken, @PathVariable String userId, @RequestBody ChangePasswordRequestDto changePasswordRequestDto) throws Exception {
        Optional.ofNullable(authToken).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        Optional.ofNullable(userId).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        userRequestValidator.validate(changePasswordRequestDto);
        if (userId.isEmpty())
            throw new ValidationException(UserError.INVALID_DATA);

        userService.changePassword(authToken, userId, changePasswordRequestDto);
    }

    @Override
    public List<UserResponseDto> getUnverifiedUsers(@RequestHeader UUID authToken, @PathVariable Integer from, @PathVariable Integer to) throws Exception {
        Optional.ofNullable(authToken).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        Optional.ofNullable(from).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        Optional.ofNullable(to).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        if (from <= 0 || to <= 0)
            throw new ValidationException(UserError.INVALID_DATA);

        if (from > to)
            throw new ValidationException(UserError.INVALID_PAGINATION_VALUES);

        return userService.getUnverifiedUsers(authToken, from, to);
    }

    @ExceptionHandler(ValidationException.class)
    private void invalidAttributes(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(NotAuthenticatedException.class)
    private void userNotFound(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.UNAUTHORIZED.value());
    }

    @ExceptionHandler({UserAlreadyExistsException.class, UserAlreadyVerifiedException.class, EmailAlreadyInUseException.class})
    private void conflict(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.CONFLICT.value());
    }

    @ExceptionHandler(UserNotFoundException.class)
    private void resourceNotFound(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler({NotAuthorizedException.class, ForbiddenException.class, EmailUnverifiedException.class})
    private void forbiddenAction(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.FORBIDDEN.value());
    }

    @ExceptionHandler(Exception.class)
    private void genericError(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

}
