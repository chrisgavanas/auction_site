package com.webapplication.mapper;

import com.webapplication.dto.user.AddressDto;
import com.webapplication.dto.user.MessageRequestDto;
import com.webapplication.dto.user.MessageResponseDto;
import com.webapplication.dto.user.SellerResponseDto;
import com.webapplication.dto.user.UserRegisterRequestDto;
import com.webapplication.dto.user.UserRegisterResponseDto;
import com.webapplication.dto.user.UserResponseDto;
import com.webapplication.dto.user.UserUpdateRequestDto;
import com.webapplication.dto.user.VoteLinkDto;
import com.webapplication.entity.Address;
import com.webapplication.entity.Message;
import com.webapplication.entity.User;
import com.webapplication.entity.VoteLink;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User registerRequestToUser(UserRegisterRequestDto userRegisterDto, String salt, String encodedPassword) {
        if (userRegisterDto == null)
            return null;

        User user = new User();
        user.setUsername(userRegisterDto.getUsername());
        user.setEmail(userRegisterDto.getEmail());
        user.setPassword(encodedPassword);
        user.setFirstName(userRegisterDto.getFirstName());
        user.setLastName(userRegisterDto.getLastName());
        user.setCountry(userRegisterDto.getCountry());
        user.setMobileNumber(userRegisterDto.getMobileNumber());
        user.setRegistrationDate(userRegisterDto.getRegistrationDate());
        user.setGender(userRegisterDto.getGender());
        user.setIsVerified(false);
        user.setIsAdmin(false);
        user.setVat(userRegisterDto.getVat());
        user.setDateOfBirth(userRegisterDto.getDateOfBirth());
        user.setPhoneNumber(userRegisterDto.getPhoneNumber());
        user.setRatingAsSeller(0);
        user.setRatingAsBidder(0);
        user.setSalt(salt);
        AddressDto addressDto = userRegisterDto.getAddress();
        if (addressDto != null) {
            Address address = new Address(addressDto.getCity(), addressDto.getPostalCode(), addressDto.getStreet());
            user.setAddress(address);
        }
        user.setAuctionItemIds(new ArrayList<>());
        user.setBidIds(new ArrayList<>());
        user.setSentMessages(new LinkedList<>());
        user.setReceivedMessages(new LinkedList<>());

        return user;
    }

    public UserRegisterResponseDto userToRegisterResponse(User user) {
        if (user == null)
            return null;

        UserRegisterResponseDto userResponse = new UserRegisterResponseDto();
        userResponse.setUserId(user.getUserId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setCountry(user.getCountry());
        userResponse.setMobileNumber(user.getMobileNumber());
        userResponse.setRegistrationDate(user.getRegistrationDate());
        userResponse.setGender(user.getGender());
        userResponse.setIsAdmin(user.getIsAdmin());
        userResponse.setIsVerified(user.getIsVerified());
        userResponse.setVat(user.getVat());
        userResponse.setDateOfBirth(user.getDateOfBirth());
        Address address = user.getAddress();
        if (address != null) {
            AddressDto addressDto = new AddressDto(address.getCity(), address.getStreet(), address.getPostalCode());
            userResponse.setAddress(addressDto);
        }
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setRatingAsSeller(user.getRatingAsSeller());
        userResponse.setRatingAsBidder(user.getRatingAsBidder());

        return userResponse;
    }

    public UserResponseDto userToUserResponse(User user) {
        if (user == null)
            return null;

        UserResponseDto userResponse = new UserResponseDto();
        userResponse.setUserId(user.getUserId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setCountry(user.getCountry());
        userResponse.setMobileNumber(user.getMobileNumber());
        userResponse.setRegistrationDate(user.getRegistrationDate());
        userResponse.setGender(user.getGender());
        userResponse.setIsAdmin(user.getIsAdmin());
        userResponse.setIsVerified(user.getIsVerified());
        userResponse.setVat(user.getVat());
        userResponse.setDateOfBirth(user.getDateOfBirth());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setRatingAsSeller(user.getRatingAsSeller());
        userResponse.setRatingAsBidder(user.getRatingAsBidder());
        Address address = user.getAddress();
        if (address != null) {
            AddressDto addressDto = new AddressDto(address.getCity(), address.getStreet(), address.getPostalCode());
            userResponse.setAddress(addressDto);
        }

        return userResponse;
    }

    public SellerResponseDto userToSellerResponseDto(User user) {
        if (user == null)
            return null;

        SellerResponseDto userResponse = new SellerResponseDto();
        userResponse.setSellerId(user.getUserId());
        userResponse.setUsername(user.getUsername());
        userResponse.setCountry(user.getCountry());
        userResponse.setRegistrationDate(user.getRegistrationDate());
        userResponse.setGender(user.getGender());
        userResponse.setRatingAsSeller(user.getRatingAsSeller());
        userResponse.setRatingAsBidder(user.getRatingAsBidder());

        return userResponse;
    }

    public List<UserResponseDto> userListToUserResponseList(List<User> users) {
        if (users == null)
            return null;

        return users.stream().map(this::userToUserResponse).collect(Collectors.toList());
    }

    public void update(User user, UserUpdateRequestDto userUpdateRequestDto) {
        user.setEmail(userUpdateRequestDto.getEmail());
        user.setFirstName(userUpdateRequestDto.getFirstName());
        user.setLastName(userUpdateRequestDto.getLastName());
        user.setCountry(userUpdateRequestDto.getCountry());
        user.setMobileNumber(userUpdateRequestDto.getMobileNumber());
        user.setGender(userUpdateRequestDto.getGender());
        user.setVat(userUpdateRequestDto.getVat());
        user.setDateOfBirth(userUpdateRequestDto.getDateOfBirth());
        AddressDto addressDto = userUpdateRequestDto.getAddress();
        if (addressDto != null) {
            Address address = new Address(addressDto.getCity(), addressDto.getPostalCode(), addressDto.getStreet());
            user.setAddress(address);
        } else
            user.setAddress(null);

        user.setPhoneNumber(userUpdateRequestDto.getPhoneNumber());
    }

    public Message convertMessageRequestDtoToMessage(MessageRequestDto messageRequestDto) {
        if (messageRequestDto == null)
            return null;

        Message message = new Message();
        message.setMessageId(ObjectId.get().toString());
        message.setMessage(messageRequestDto.getMessage());
        message.setFrom(messageRequestDto.getFrom());
        message.setTo(messageRequestDto.getTo());
        message.setSubject(messageRequestDto.getSubject());
        message.setDate(new Date());
        message.setSeen(false);
        message.setVoteLink(null);

        return message;
    }

    private MessageResponseDto convertMessageToMessageResponseDto(Message message) {
        if (message == null)
            return null;

        MessageResponseDto messageResponseDto = new MessageResponseDto();
        messageResponseDto.setMessageId(message.getMessageId());
        messageResponseDto.setSubject(message.getSubject());
        messageResponseDto.setMessage(message.getMessage());
        messageResponseDto.setFrom(message.getFrom());
        messageResponseDto.setTo(message.getTo());
        messageResponseDto.setDate(message.getDate());
        messageResponseDto.setSeen(message.getSeen());
        VoteLink voteLink = message.getVoteLink();
        VoteLinkDto voteLinkDto = convertVoteLinkToVoteLinkDto(message.getMessageId(), voteLink);
        messageResponseDto.setVoteLinkDto(voteLinkDto);

        return messageResponseDto;
    }

    private VoteLinkDto convertVoteLinkToVoteLinkDto(String messageId, VoteLink voteLink) {
        if (voteLink == null)
            return null;

        VoteLinkDto voteLinkDto = new VoteLinkDto();
        voteLinkDto.setMessageId(messageId);
        voteLinkDto.setAuctionItemId(voteLink.getAuctionItemId());
        voteLinkDto.setVoteReceiverId(voteLink.getVoteReceiverId());
        return voteLinkDto;
    }

    public List<MessageResponseDto> convertMessageListToMessageResponseDtoList(List<Message> messages) {
        if (messages == null)
            return null;

        return messages.stream().map(this::convertMessageToMessageResponseDto)
                .collect(Collectors.toList());
    }

}
