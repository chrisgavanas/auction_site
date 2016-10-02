package com.webapplication.api.user;

import com.webapplication.dto.user.ChangePasswordRequestDto;
import com.webapplication.dto.user.MessageRequestDto;
import com.webapplication.dto.user.MessageResponseDto;
import com.webapplication.dto.user.MessageType;
import com.webapplication.dto.user.SellerResponseDto;
import com.webapplication.dto.user.UserLogInRequestDto;
import com.webapplication.dto.user.UserLogInResponseDto;
import com.webapplication.dto.user.UserRegisterRequestDto;
import com.webapplication.dto.user.UserRegisterResponseDto;
import com.webapplication.dto.user.UserResponseDto;
import com.webapplication.dto.user.UserUpdateRequestDto;
import com.webapplication.dto.user.Vote;
import com.webapplication.dto.user.VoteLinkDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
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
    List<UserResponseDto> getUnverifiedUsers(HttpServletResponse response, @RequestHeader UUID authToken, @PathVariable Integer from, @PathVariable Integer to) throws Exception;

    @RequestMapping(path = "/user/verified/{from}-{to}", method = RequestMethod.GET, produces = "application/json")
    List<UserResponseDto> getVerifiedUsers(HttpServletResponse response, @RequestHeader UUID authToken, @PathVariable Integer from, @PathVariable Integer to) throws Exception;

    @RequestMapping(path = "/user/{userId}/send-message", method = RequestMethod.POST, consumes = "application/json")
    void sendMessage(@RequestHeader UUID authToken, @PathVariable String userId, MessageRequestDto messageRequestDto) throws Exception;

    @RequestMapping(path = "/user/{userId}/message", method = RequestMethod.GET, produces = "application/json")
    List<MessageResponseDto> getMessagesByType(@RequestHeader UUID authToken, @PathVariable String userId, @RequestParam("messageType") MessageType messageType) throws Exception;

    @RequestMapping(path = "/user/{userId}/message/{messageId}/seen", method = RequestMethod.POST)
    void markMessageAsSeen(@RequestHeader UUID authToken, @PathVariable String userId, @PathVariable String messageId) throws Exception;

    @RequestMapping(path = "/user/{userId}/message/delete", method = RequestMethod.POST)
    void deleteMessage(@RequestHeader UUID authToken, @PathVariable String userId, List<String> messageIds, @RequestParam("messageType") MessageType messageType) throws Exception;

    @RequestMapping(path = "/user/{userId}/vote/{vote}", method = RequestMethod.POST, consumes = "application/json")
    void vote(@RequestHeader UUID authToken, @PathVariable String userId, @PathVariable Vote vote, VoteLinkDto voteLinkDto) throws Exception;

}
