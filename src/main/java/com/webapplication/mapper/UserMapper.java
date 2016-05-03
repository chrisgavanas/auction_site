package com.webapplication.mapper;

import com.webapplication.dto.user.*;
import org.springframework.stereotype.Component;

import com.webapplication.entity.User;

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
        user.setIsAdmin(userRegisterDto.getIsAdmin() == null ? false : userRegisterDto.getIsAdmin());
        user.setIsVerified(false);
        user.setVat(userRegisterDto.getVat());
        user.setDateOfBirth(userRegisterDto.getDateOfBirth());
        user.setPhoneNumber(userRegisterDto.getPhoneNumber());
        user.setRatingAsSeller(0);
        user.setRatingAsBidder(0);
        AddressDto addressDto = userRegisterDto.getAddress();
        if (addressDto != null)
            user.setAddress(addressDto);

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
        userResponse.setAddress(user.getAddress());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setRatingAsSeller(user.getRatingAsSeller());
        userResponse.setRatingasBidder(user.getRatingAsBidder());

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
        userResponse.setRatingasBidder(user.getRatingAsBidder());
        userResponse.setAddress(user.getAddress());

        return userResponse;
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
        if (addressDto != null)
            user.setAddress(addressDto);
        user.setPhoneNumber(userUpdateRequestDto.getPhoneNumber());
    }

}
