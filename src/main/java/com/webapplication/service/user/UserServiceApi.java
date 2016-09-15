package com.webapplication.service.user;

import com.webapplication.dto.user.*;

import java.util.List;
import java.util.UUID;


public interface UserServiceApi {

    UserLogInResponseDto login(UserLogInRequestDto userLogInRequestDto) throws Exception;

    UserRegisterResponseDto register(UserRegisterRequestDto userRegisterRequestDto) throws Exception;

    UserResponseDto getUser(UUID authToken, String userId) throws Exception;

    SellerResponseDto getSeller(String sellerId) throws Exception;

    void verifyUser(UUID authToken, String userId) throws Exception;

    UserResponseDto updateUser(UUID authToken, String userId, UserUpdateRequestDto userUpdateRequestDto) throws Exception;

    void changePassword(UUID authToken, String userId, ChangePasswordRequestDto changePasswordRequestDto) throws Exception;

    List<UserResponseDto> getUnverifiedUsers(UUID authToken, Integer from, Integer to) throws Exception;

    List<UserResponseDto> getVerifiedUsers(UUID authToken, Integer from, Integer to) throws Exception;

    void sendMessage(UUID authToken, String userId, MessageRequestDto messageRequestDto) throws Exception;

    List<MessageResponseDto> getMessagesByType(UUID authToken, String userId, MessageType messageType) throws Exception;

    void markMessageAsSeen(UUID authToken, String userId, String messageId) throws Exception;

    void deleteMessage(UUID authToken, String userId, String messageId, MessageType messageType) throws Exception;

    void voteSeller(UUID authToken, String userId, Vote vote, String sellerId) throws Exception;

}
