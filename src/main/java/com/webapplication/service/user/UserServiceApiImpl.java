package com.webapplication.service.user;

import com.webapplication.authentication.Authenticator;
import com.webapplication.dao.UserRepository;
import com.webapplication.dto.user.*;
import com.webapplication.entity.User;
import com.webapplication.error.user.UserError;
import com.webapplication.error.user.UserLogInError;
import com.webapplication.error.user.UserRegisterError;
import com.webapplication.exception.*;
import com.webapplication.mapper.UserMapper;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Transactional
@Component
public class UserServiceApiImpl implements UserServiceApi {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Authenticator authenticator;

    @Override
    public UserLogInResponseDto login(UserLogInRequestDto userLogInRequestDto) throws Exception {
        User user = userRepository.findUserByUsernameOrEmailAndPassword(userLogInRequestDto.getUsername(), userLogInRequestDto.getEmail(), userLogInRequestDto.getPassword());

        Optional.ofNullable(user).orElseThrow(() -> new UserNotFoundException(UserLogInError.INVALID_CREDENTIALS));
        if (!user.getIsVerified())
            throw new EmailUnverifiedException(UserLogInError.USER_NOT_EMAIL_VERIFIED);

        SessionInfo session = new SessionInfo(user.getUserId(), DateTime.now().plusMinutes(Authenticator.SESSION_TIME_OUT_MINUTES), user.getIsAdmin());
        UUID authToken = authenticator.createSession(session);

        return new UserLogInResponseDto(user.getUserId(), authToken);
    }

    @Override
    public UserRegisterResponseDto register(UserRegisterRequestDto userRegisterRequestDto) throws Exception {
        User user = userRepository.findUserByUsernameOrEmail(userRegisterRequestDto.getUsername(), userRegisterRequestDto.getEmail());
        if (user != null)
            throw new UserAlreadyExistsException(user.getUsername().equals(userRegisterRequestDto.getUsername())
                    ? UserRegisterError.USERNAME_ALREADY_IN_USE : UserRegisterError.EMAIL_ALREADY_USED);

        user = userMapper.registerRequestToUser(userRegisterRequestDto);
        userRepository.save(user);

        return userMapper.userToRegisterResponse(user);
    }

    @Override
    public UserResponseDto getUser(UUID authToken, Integer userId) throws Exception {
        SessionInfo sessionInfo = getActiveSession(authToken);
        User user = userRepository.findUserByUserId(userId);
        Optional.ofNullable(user).orElseThrow(() -> new NotFoundException(UserError.USER_DOES_NOT_EXIST));
        validateAuthorization(user.getUserId(), sessionInfo);

        return userMapper.userToUserResponse(user);
    }

    @Override
    public void verifyUser(UUID authToken, Integer userId) throws Exception {
        SessionInfo sessionInfo = authenticator.getSession(authToken);
        Optional.ofNullable(sessionInfo).orElseThrow(() -> new NotAuthorizedException(UserError.UNAUTHORIZED));
        if (!sessionInfo.getIsAdmin())
            throw new NotAuthorizedException(UserError.UNAUTHORIZED);

        User user = userRepository.findUserByUserId(userId);
        Optional.ofNullable(user).orElseThrow(() -> new UserNotFoundException(UserError.USER_DOES_NOT_EXIST));
        if (user.getIsVerified())
            throw new UserAlreadyVerifiedException(UserError.USER_ALREADY_VERIFIED);

        user.setIsVerified(true);
        userRepository.save(user);
    }

    @Override
    public UserResponseDto updateUser(UserUpdateRequestDto userUpdateRequestDto) throws Exception {
        User user = userRepository.findUserByUserId(userUpdateRequestDto.getUserId());
        Optional.ofNullable(user).orElseThrow(() -> new UserNotFoundException(UserError.USER_DOES_NOT_EXIST));

        if (!user.getEmail().equals(userUpdateRequestDto.getEmail()) && userRepository.countByEmail(userUpdateRequestDto.getEmail()) > 0)
            throw new EmailAlreadyInUseException(UserError.EMAIL_ALREADY_IN_USE);

        userMapper.update(user, userUpdateRequestDto);
        userRepository.save(user);

        return userMapper.userToUserResponse(user);
    }

    @Override
    public void changePassword(Integer userId, ChangePasswordRequestDto changePasswordRequestDto) throws Exception {
        User user = userRepository.findUserByUserId(userId);
        Optional.ofNullable(user).orElseThrow(() -> new UserNotFoundException(UserError.USER_DOES_NOT_EXIST));

        if (!user.getPassword().equals(changePasswordRequestDto.getOldPassword()))
            throw new ForbiddenException(UserError.PASSWORD_MISSMATCH);

        user.setPassword(changePasswordRequestDto.getNewPassword());
        userRepository.save(user);
    }

    private SessionInfo getActiveSession(UUID authToken) throws  NotAuthenticatedException {
        SessionInfo sessionInfo = authenticator.getSession(authToken);
        Optional.ofNullable(sessionInfo).orElseThrow(() -> new NotAuthenticatedException(UserError.NOT_AUTHENTICATED));
        return sessionInfo;
    }

    private void validateAuthorization(Integer userId, SessionInfo sessionInfo) throws NotAuthorizedException {
        if (!userId.equals(sessionInfo.getUserId()) && !sessionInfo.getIsAdmin())
            throw new NotAuthorizedException(UserError.UNAUTHORIZED);
    }

}
