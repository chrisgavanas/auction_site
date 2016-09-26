package com.webapplication.api.user;


import com.webapplication.dto.user.*;
import com.webapplication.entity.AuctionItem;
import com.webapplication.error.user.UserError;
import com.webapplication.exception.ForbiddenException;
import com.webapplication.exception.NotAuthenticatedException;
import com.webapplication.exception.NotAuthorizedException;
import com.webapplication.exception.ValidationException;
import com.webapplication.exception.user.*;
import com.webapplication.service.user.UserServiceApi;
import com.webapplication.validator.user.UserRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

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
    
    @Override
    public List<UserResponseDto> getVerifiedUsers(@RequestHeader UUID authToken, @PathVariable Integer from, @PathVariable Integer to) throws Exception {
        Optional.ofNullable(authToken).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        Optional.ofNullable(from).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        Optional.ofNullable(to).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        if (from <= 0 || to <= 0)
            throw new ValidationException(UserError.INVALID_DATA);

        if (from > to)
            throw new ValidationException(UserError.INVALID_PAGINATION_VALUES);

        return userService.getVerifiedUsers(authToken, from, to);
    }

    @Override
    public void sendMessage(@RequestHeader UUID authToken, @PathVariable String userId, @RequestBody MessageRequestDto messageRequestDto) throws Exception {
        Optional.ofNullable(authToken).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        Optional.ofNullable(userId).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        userRequestValidator.validate(messageRequestDto);

        userService.sendMessage(authToken, userId, messageRequestDto);
    }

    @Override
    public List<MessageResponseDto> getMessagesByType(@RequestHeader UUID authToken, @PathVariable String userId, @RequestParam("messageType") MessageType messageType) throws Exception {
        Optional.ofNullable(authToken).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        Optional.ofNullable(userId).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        Optional.ofNullable(messageType).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));

        return userService.getMessagesByType(authToken, userId, messageType);
    }

    @Override
    public void markMessageAsSeen(@RequestHeader UUID authToken, @PathVariable String userId, @PathVariable String messageId) throws Exception {
        Optional.ofNullable(authToken).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        Optional.ofNullable(userId).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        Optional.ofNullable(messageId).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));

        userService.markMessageAsSeen(authToken, userId, messageId);
    }

    @Override
    public void deleteMessage(@RequestHeader UUID authToken, @PathVariable String userId, @RequestBody List<String> messageIds, @RequestParam("messageType") MessageType messageType) throws Exception {
        Optional.ofNullable(authToken).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        Optional.ofNullable(userId).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        Optional.ofNullable(messageIds).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        if (messageIds.isEmpty())
            throw new ValidationException(UserError.MISSING_DATA);
        Optional.ofNullable(messageType).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));

        userService.deleteMessage(authToken, userId, messageIds, messageType);
    }

    @Override
    public void vote(@RequestHeader UUID authToken, @PathVariable String userId, @PathVariable Vote vote, @RequestBody VoteLinkDto voteLinkDto) throws Exception {
        Optional.ofNullable(authToken).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        Optional.ofNullable(userId).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        Optional.ofNullable(vote).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        userRequestValidator.validate(voteLinkDto);

        userService.vote(authToken, userId, vote, voteLinkDto);
    }

    private void validateVoteParams(UUID authToken, String userId, Vote vote, String sellerId) throws Exception {
        Optional.ofNullable(authToken).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        Optional.ofNullable(userId).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        Optional.ofNullable(vote).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        Optional.ofNullable(sellerId).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        if (Stream.of(userId, sellerId).anyMatch(String::isEmpty))
            throw new ValidationException(UserError.INVALID_DATA);
    }

    @ExceptionHandler(ValidationException.class)
    private void invalidAttributes(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(NotAuthenticatedException.class)
    private void userNotFound(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.UNAUTHORIZED.value());
    }

    @ExceptionHandler({UserAlreadyExistsException.class, UserAlreadyVerifiedException.class, EmailAlreadyInUseException.class, VoteException.class})
    private void conflict(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.CONFLICT.value());
    }

    @ExceptionHandler({UserNotFoundException.class, MessageNotFoundException.class})
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
