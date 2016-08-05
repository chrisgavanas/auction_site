package com.webapplication.mapper;

import com.webapplication.dto.user.AddressDto;
import com.webapplication.dto.user.UserRegisterRequestDto;
import com.webapplication.dto.user.UserRegisterResponseDto;
import com.webapplication.dto.user.UserResponseDto;
import com.webapplication.dto.user.UserUpdateRequestDto;
import com.webapplication.entity.Address;
import com.webapplication.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User registerRequestToUser(UserRegisterRequestDto userRegisterDto) {
        if (userRegisterDto == null)
            return null;

        User user = new User();
        user.setUsername(userRegisterDto.getUsername());
        user.setEmail(userRegisterDto.getEmail());
        user.setPassword(userRegisterDto.getPassword());
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
        AddressDto addressDto = userRegisterDto.getAddress();
        if (addressDto != null) {
            Address address = new Address(addressDto.getCity(), addressDto.getPostalCode(), addressDto.getStreet());
            user.setAddress(address);
        }
        user.setAuctionItemIds(new ArrayList<>());
        user.setBidIds(new ArrayList<>());

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

    public List<UserResponseDto> userListToUserResponseList(List<User> users) {
        if (users == null)
            return null;

        return users.stream().map(user -> userToUserResponse(user)).collect(Collectors.toList());
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

}
