package com.webapplication.service.user;

import com.webapplication.authentication.Authenticator;
import com.webapplication.dao.UserRepository;
import com.webapplication.dto.user.ChangePasswordRequestDto;
import com.webapplication.dto.user.MessageRequestDto;
import com.webapplication.dto.user.MessageResponseDto;
import com.webapplication.dto.user.MessageType;
import com.webapplication.dto.user.SellerResponseDto;
import com.webapplication.dto.user.SessionInfo;
import com.webapplication.dto.user.UserLogInRequestDto;
import com.webapplication.dto.user.UserLogInResponseDto;
import com.webapplication.dto.user.UserRegisterRequestDto;
import com.webapplication.dto.user.UserRegisterResponseDto;
import com.webapplication.dto.user.UserResponseDto;
import com.webapplication.dto.user.UserUpdateRequestDto;
import com.webapplication.dto.user.Vote;
import com.webapplication.dto.user.VoteLinkDto;
import com.webapplication.entity.Message;
import com.webapplication.entity.User;
import com.webapplication.entity.VoteLink;
import com.webapplication.error.user.UserError;
import com.webapplication.error.user.UserLogInError;
import com.webapplication.error.user.UserRegisterError;
import com.webapplication.exception.ForbiddenException;
import com.webapplication.exception.NotAuthenticatedException;
import com.webapplication.exception.NotAuthorizedException;
import com.webapplication.exception.user.EmailAlreadyInUseException;
import com.webapplication.exception.user.EmailUnverifiedException;
import com.webapplication.exception.user.MessageNotFoundException;
import com.webapplication.exception.user.UserAlreadyExistsException;
import com.webapplication.exception.user.UserAlreadyVerifiedException;
import com.webapplication.exception.user.UserNotFoundException;
import com.webapplication.exception.user.VoteException;
import com.webapplication.mapper.UserMapper;
import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.List;
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
        User user = userRepository.findUserByUsernameOrEmail(userLogInRequestDto.getUsername(), userLogInRequestDto.getEmail());
        Optional.ofNullable(user).orElseThrow(() -> new NotAuthenticatedException(UserLogInError.INVALID_CREDENTIALS));
        if (!validatePassword(userLogInRequestDto.getPassword(), user.getPassword(), user.getSalt()))
            throw new NotAuthenticatedException(UserLogInError.INVALID_CREDENTIALS);
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

        byte[] salt = createSaltForUser();
        String encodedSaltAsString = new String(Base64.encodeBase64(salt));
        String encodedPassword = encodePassword(userRegisterRequestDto.getPassword(), salt);
        user = userMapper.registerRequestToUser(userRegisterRequestDto, encodedSaltAsString, encodedPassword);
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
    public List<UserResponseDto> getVerifiedUsers(UUID authToken, Integer from, Integer to) throws Exception {
        SessionInfo sessionInfo = getActiveSession(authToken);
        validateAuthorization(sessionInfo);
        List<User> users = userRepository.findUserByIsVerified(true, new PageRequest(from / paginationPageSize, to - from + 1));

        return userMapper.userListToUserResponseList(users);
    }

    @Override
    public void sendMessage(UUID authToken, String userId, MessageRequestDto messageRequestDto) throws Exception {
        SessionInfo sessionInfo = getActiveSession(authToken);
        validateAuthorization(userId, sessionInfo);
        User sender = getUserByUserIdAndUsername(userId, messageRequestDto.getFrom());
        User receiver = validateAndGetUser(messageRequestDto.getTo());
        Message message = userMapper.convertMessageRequestDtoToMessage(messageRequestDto);
        addMessages(sender, receiver, message);
    }

    @Override
    public List<MessageResponseDto> getMessagesByType(UUID authToken, String userId, MessageType messageType) throws Exception {
        SessionInfo sessionInfo = getActiveSession(authToken);
        validateAuthorization(userId, sessionInfo);
        List<Message> messages = getMessagesOfUserByType(userId, messageType);

        return userMapper.convertMessageListToMessageResponseDtoList(messages);
    }

    @Override
    public void markMessageAsSeen(UUID authToken, String userId, String messageId) throws Exception {
        SessionInfo sessionInfo = getActiveSession(authToken);
        validateAuthorization(userId, sessionInfo);
        markMessageAsSeen(userId, messageId);
    }

    @Override
    public void deleteMessage(UUID authToken, String userId, List<String> messageIds, MessageType messageType) throws Exception {
        SessionInfo sessionInfo = getActiveSession(authToken);
        validateAuthorization(userId, sessionInfo);
        User user = getUser(userId);
        List<Message> messages;
        switch (messageType) {
            case RECEIVED:
                messages = user.getReceivedMessages();
                break;
            case SENT:
                messages = user.getSentMessages();
                break;
            default:
                throw new MessageNotFoundException(UserError.MESSAGE_NOT_FOUND);
        }

        int messageNo = messages.size();
        messages.removeIf(message -> messageIds.contains(message.getMessageId()));
        messageNo -= messages.size();

        if (messageNo != messageIds.size())
            throw new MessageNotFoundException(UserError.MESSAGE_NOT_FOUND);
        else
            userRepository.save(user);
    }

    @Override
    public void vote(UUID authToken, String userId, Vote vote, VoteLinkDto voteLinkDto) throws Exception {
        SessionInfo sessionInfo = getActiveSession(authToken);
        validateAuthorization(userId, sessionInfo);
        String voteReceiverId = voteLinkDto.getVoteReceiverId();
        User voter = getUser(userId);
        User voteReceiver = getUser(voteReceiverId);
        List<Message> messages = voter.getReceivedMessages();
        String auctionItemId = voteLinkDto.getAuctionItemId();

        for (Message message : messages) {
            VoteLink voteLink = message.getVoteLink();
            if (voteLink != null && voteLink.getVoterId().equals(userId) && voteLink.getVoteReceiverId().equals((voteReceiverId))
                    && voteLink.getAuctionItemId().equals(auctionItemId)) {
                if (voteLink.getVoteUserAsSeller())
                    voteReceiver.setRatingAsSeller(voteReceiver.getRatingAsSeller() + vote.getValue());
                else
                    voteReceiver.setRatingAsBidder(voteReceiver.getRatingAsBidder() + vote.getValue());
                message.setVoteLink(null);
                userRepository.save(Arrays.asList(new User[]{voter, voteReceiver}));
                return;
            }
        }

        throw new VoteException(UserError.ALREADY_VOTED);
    }

    private byte[] createSaltForUser() throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[8];
        random.nextBytes(salt);
        return salt;
    }

    private String encodePassword(String password, byte[] salt) throws Exception {
        String algorithm = "PBKDF2WithHmacSHA1";
        int derivedKeyLength = 160;
        int iterations = 20000;
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, derivedKeyLength);
        SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);
        byte[] encodedPassword = Base64.encodeBase64(f.generateSecret(spec).getEncoded());
        return new String(encodedPassword);
    }


    private Boolean validatePassword(String attemptedPassword, String password, String saltStored) throws Exception {
        byte[] salt = Base64.decodeBase64(saltStored.getBytes());
        String algorithm = "PBKDF2WithHmacSHA1";
        int derivedKeyLength = 160;
        int iterations = 20000;
        KeySpec spec = new PBEKeySpec(attemptedPassword.toCharArray(), salt, iterations, derivedKeyLength);
        SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);
        String encodedAttemptedPassword = new String(Base64.encodeBase64(f.generateSecret(spec).getEncoded()));
        return password.equals(encodedAttemptedPassword);
    }

    private void markMessageAsSeen(String userId, String messageId) throws Exception {
        User userReceived = getUser(userId);
        List<Message> receivedMessages = userReceived.getReceivedMessages();
        for (Message receivedMessage : receivedMessages) {
            if (receivedMessage.getMessageId().equals(messageId)) {
                receivedMessage.setSeen(true);
                userRepository.save(userReceived);
                User userSent = userRepository.findUserByUsername(receivedMessage.getFrom());
                List<Message> sentMessages = userSent.getSentMessages();
                for (Message sentMessage : sentMessages) {
                    if (sentMessage.getMessageId().equals(messageId)) {
                        sentMessage.setSeen(true);
                        if (userReceived.getUserId().equals(userSent.getUserId()))
                            userSent.setReceivedMessages(receivedMessages);
                        userRepository.save(userSent);
                        break;
                    }
                }
                return;
            }
        }

        throw new MessageNotFoundException(UserError.MESSAGE_NOT_FOUND);
    }

    private List<Message> getMessagesOfUserByType(String userId, MessageType messageType) throws Exception {
        User user = getUser(userId);
        switch (messageType) {
            case RECEIVED:
                return user.getReceivedMessages();
            case SENT:
                return user.getSentMessages();
            default:
                return null;
        }
    }

    private void addMessages(User sender, User receiver, Message message) {
        List<Message> senderSentMessages = sender.getSentMessages();
        List<Message> receiverReceivedMessages = receiver.getReceivedMessages();
        senderSentMessages.add(0, message);
        receiverReceivedMessages.add(0, message);

        if (sender.getUserId().equals(receiver.getUserId())) {
            sender.setReceivedMessages(receiverReceivedMessages);
            userRepository.save(sender);
        } else {
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
        sessionInfo.setDate(DateTime.now().plusMinutes(Authenticator.SESSION_TIME_OUT_MINUTES));

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

    private User getUserByUserIdAndUsername(String userId, String username) throws Exception {
        User user = userRepository.findUserByUserIdAndUsername(userId, username);
        Optional.ofNullable(user).orElseThrow(() -> new UserNotFoundException(UserError.USER_DOES_NOT_EXIST));

        return user;
    }

}
