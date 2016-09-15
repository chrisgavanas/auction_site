package com.webapplication.api.user;

import com.webapplication.dto.user.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api")
public interface UserApi {

    @RequestMapping(path = "/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    UserLogInResponseDto login(UserLogInRequestDto userLogInRequestDto) throws Exception;

    @RequestMapping(path = "/user", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    UserRegisterResponseDto register(UserRegisterRequestDto userRegisterRequestDto) throws Exception;

    @RequestMapping(path = "/user/{userId}", method = RequestMethod.GET, produces = "application/json")
    UserResponseDto getUser(@RequestHeader UUID authToken, @PathVariable String userId) throws Exception;

    @RequestMapping(path = "/seller/{sellerId}", method = RequestMethod.GET, produces = "application/json")
    SellerResponseDto getSeller(@PathVariable String sellerId) throws Exception;
    
    @RequestMapping(path = "/user/{userId}", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    UserResponseDto updateUser(@RequestHeader UUID authToken, @PathVariable String userId, UserUpdateRequestDto userUpdateRequestDto) throws Exception;

    @RequestMapping(path = "/user/{userId}/verify-user", method = RequestMethod.POST)
    void verifyUser(@RequestHeader UUID authToken, @PathVariable String userId) throws Exception;

    @RequestMapping(path = "/user/{userId}/change-password", method = RequestMethod.POST, consumes = "application/json")
    void changePassword(@RequestHeader UUID authToken, @PathVariable String userId, ChangePasswordRequestDto changePasswordRequestDto) throws Exception;

    @RequestMapping(path = "/user/unverified/{from}-{to}", method = RequestMethod.GET, produces = "application/json")
    List<UserResponseDto> getUnverifiedUsers(@RequestHeader UUID authToken, @PathVariable Integer from, @PathVariable Integer to) throws Exception;

    @RequestMapping(path = "/user/verified/{from}-{to}", method = RequestMethod.GET, produces = "application/json")
    List<UserResponseDto> getVerifiedUsers(@RequestHeader UUID authToken, @PathVariable Integer from, @PathVariable Integer to) throws Exception;
    
    @RequestMapping(path = "/user/{userId}/send-message", method = RequestMethod.POST, consumes = "application/json")
    void sendMessage(@RequestHeader UUID authToken, @PathVariable String userId, MessageRequestDto messageRequestDto) throws Exception;

    @RequestMapping(path = "/user/{userId}/message", method = RequestMethod.GET, produces = "application/json")
    List<MessageResponseDto> getMessagesByType(@RequestHeader UUID authToken, @PathVariable String userId, @RequestParam("messageType") MessageType messageType) throws Exception;

    @RequestMapping(path = "/user/{userId}/message/{messageId}/seen", method = RequestMethod.POST)
    void markMessageAsSeen(@RequestHeader UUID authToken, @PathVariable String userId, @PathVariable String messageId) throws Exception;

    @RequestMapping(path = "/user/{userId}/message/{messageId}/delete", method = RequestMethod.POST)
    void deleteMessage(@RequestHeader UUID authToken, @PathVariable String userId, @PathVariable String messageId, @RequestParam("messageType") MessageType messageType) throws Exception;

    @RequestMapping(path = "/user/{userId}/vote-seller/{vote}", method = RequestMethod.PUT, consumes = "text/plain")
    void voteSeller(@RequestHeader UUID authToken, @PathVariable String userId, @PathVariable Vote vote, @RequestParam String sellerId) throws Exception;

}
