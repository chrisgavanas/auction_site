package com.webapplication.service.user;

import com.webapplication.authentication.Authenticator;
import com.webapplication.dao.UserRepository;
import com.webapplication.dto.user.ChangePasswordRequestDto;
import com.webapplication.dto.user.MessageDto;
import com.webapplication.dto.user.SellerResponseDto;
import com.webapplication.dto.user.SessionInfo;
import com.webapplication.dto.user.UserLogInRequestDto;
import com.webapplication.dto.user.UserLogInResponseDto;
import com.webapplication.dto.user.UserRegisterRequestDto;
import com.webapplication.dto.user.UserRegisterResponseDto;
import com.webapplication.dto.user.UserResponseDto;
import com.webapplication.dto.user.UserUpdateRequestDto;
import com.webapplication.entity.Message;
import com.webapplication.entity.User;
import com.webapplication.error.user.UserError;
import com.webapplication.error.user.UserLogInError;
import com.webapplication.error.user.UserRegisterError;
import com.webapplication.exception.ForbiddenException;
import com.webapplication.exception.NotAuthenticatedException;
import com.webapplication.exception.NotAuthorizedException;
import com.webapplication.exception.ValidationException;
import com.webapplication.exception.user.EmailAlreadyInUseException;
import com.webapplication.exception.user.EmailUnverifiedException;
import com.webapplication.exception.user.UserAlreadyExistsException;
import com.webapplication.exception.user.UserAlreadyVerifiedException;
import com.webapplication.exception.user.UserNotFoundException;
import com.webapplication.mapper.UserMapper;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
public class UserServiceApiImpl implements UserServiceApi {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Authenticator authenticator;

    @Value("${paginationPageSize}")
    private Integer paginationPageSize;

    @Override
    public UserLogInResponseDto login(UserLogInRequestDto userLogInRequestDto) throws Exception {
        User user = userRepository.findUserByUsernameOrEmailAndPassword(userLogInRequestDto.getUsername(), userLogInRequestDto.getEmail(), userLogInRequestDto.getPassword());
        Optional.ofNullable(user).orElseThrow(() -> new NotAuthenticatedException(UserLogInError.INVALID_CREDENTIALS));
        if (!user.getIsVerified())
            throw new EmailUnverifiedException(UserLogInError.USER_NOT_EMAIL_VERIFIED);

        SessionInfo session = new SessionInfo(user.getUserId(), DateTime.now().plusMinutes(Authenticator.SESSION_TIME_OUT_MINUTES), user.getIsAdmin());
        UUID authToken = authenticator.createSession(session);

        return new UserLogInResponseDto(user.getUserId(), authToken, user.getIsAdmin());
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
    public UserResponseDto getUser(UUID authToken, String userId) throws Exception {
        SessionInfo sessionInfo = getActiveSession(authToken);
        User user = getUser(userId);
        validateAuthorization(user.getUserId(), sessionInfo);

        return userMapper.userToUserResponse(user);
    }

    @Override
    public SellerResponseDto getSeller(String sellerId) throws Exception {
        User user = getUser(sellerId);

        return userMapper.userToSellerResponseDto(user);
    }


    @Override
    public void verifyUser(UUID authToken, String userId) throws Exception {
        SessionInfo sessionInfo = getActiveSession(authToken);
        if (!sessionInfo.getIsAdmin())
            throw new NotAuthorizedException(UserError.UNAUTHORIZED);

        User user = getUser(userId);
        if (user.getIsVerified())
            throw new UserAlreadyVerifiedException(UserError.USER_ALREADY_VERIFIED);

        user.setIsVerified(true);
        userRepository.save(user);
    }

    @Override
    public UserResponseDto updateUser(UUID authToken, String userId, UserUpdateRequestDto userUpdateRequestDto) throws Exception {
        SessionInfo sessionInfo = getActiveSession(authToken);
        User user = getUser(userId);
        validateAuthorization(userId, sessionInfo);

        if (!user.getEmail().equals(userUpdateRequestDto.getEmail()) && userRepository.countByEmail(userUpdateRequestDto.getEmail()) > 0)
            throw new EmailAlreadyInUseException(UserError.EMAIL_ALREADY_IN_USE);

        userMapper.update(user, userUpdateRequestDto);
        userRepository.save(user);

        return userMapper.userToUserResponse(user);
    }

    @Override
    public void changePassword(UUID authToken, String userId, ChangePasswordRequestDto changePasswordRequestDto) throws Exception {
        SessionInfo sessionInfo = getActiveSession(authToken);
        User user = getUser(userId);
        validateAuthorization(userId, sessionInfo);

        if (!user.getPassword().equals(changePasswordRequestDto.getOldPassword()))
            throw new ForbiddenException(UserError.PASSWORD_MISSMATCH);

        user.setPassword(changePasswordRequestDto.getNewPassword());
        userRepository.save(user);
    }

    @Override
    public List<UserResponseDto> getUnverifiedUsers(UUID authToken, Integer from, Integer to) throws Exception {
        SessionInfo sessionInfo = getActiveSession(authToken);
        validateAuthorization(sessionInfo);
        List<User> users = userRepository.findUserByIsVerified(false, new PageRequest(from / paginationPageSize, to - from + 1));

        return userMapper.userListToUserResponseList(users);
    }

    @Override
    public void sendMessage(UUID authToken, String userId, MessageDto messageDto) throws Exception {
        SessionInfo sessionInfo = getActiveSession(authToken);
        validateAuthorization(userId, sessionInfo);
        User sender = getUser(userId);
        User receiver = validateAndGetUser(messageDto.getUsername());
        Message message = userMapper.convertMessageDtoToMessage(messageDto);
        addMessages(sender, receiver, message);
    }

    private void addMessages(User sender, User receiver, Message message) {
        Map<String, List<Message>> senderSentMessages = sender.getSentMessages();
        Map<String, List<Message>> receiverReceivedMessages = receiver.getReceivedMessages();

        senderSentMessages.putIfAbsent(receiver.getUsername(), new LinkedList<>());
        receiverReceivedMessages.putIfAbsent(sender.getUsername(), new LinkedList<>());
        senderSentMessages.get(receiver.getUsername()).add(message);
        receiverReceivedMessages.get(sender.getUsername()).add(message);
        if (sender.getUserId().equals(receiver.getUserId())) {
            sender.setReceivedMessages(receiverReceivedMessages);
            userRepository.save(sender);
        }
        else {
            userRepository.save(sender);
            userRepository.save(receiver);
        }
    }

    private User validateAndGetUser(String username) throws Exception {
        User user = userRepository.findUserByUsername(username);
        Optional.ofNullable(user).orElseThrow(() -> new UserNotFoundException(UserError.USER_DOES_NOT_EXIST));

        return user;
    }

    private SessionInfo getActiveSession(UUID authToken) throws NotAuthenticatedException {
        SessionInfo sessionInfo = authenticator.getSession(authToken);
        Optional.ofNullable(sessionInfo).orElseThrow(() -> new NotAuthenticatedException(UserError.NOT_AUTHENTICATED));
        return sessionInfo;
    }

    private void validateAuthorization(String userId, SessionInfo sessionInfo) throws NotAuthorizedException {
        if (!userId.equals(sessionInfo.getUserId()) && !sessionInfo.getIsAdmin())
            throw new NotAuthorizedException(UserError.UNAUTHORIZED);
    }

    private void validateAuthorization(SessionInfo sessionInfo) throws NotAuthorizedException {
        if (!sessionInfo.getIsAdmin())
            throw new NotAuthorizedException(UserError.UNAUTHORIZED);
    }

    private User getUser(String userId) throws Exception {
        User user = userRepository.findUserByUserId(userId);
        Optional.ofNullable(user).orElseThrow(() -> new UserNotFoundException(UserError.USER_DOES_NOT_EXIST));

        return user;
    }

}
