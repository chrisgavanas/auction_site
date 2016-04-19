package com.webapplication.mapper;

import com.webapplication.dto.user.UserRequestDto;
import org.springframework.stereotype.Component;

import com.webapplication.dto.user.UserRegisterRequestDto;
import com.webapplication.dto.user.UserRegisterResponseDto;
import com.webapplication.dto.user.UserResponseDto;
import com.webapplication.entity.User;

@Component
public class UserMapper {

    public User registerRequestToUser(UserRegisterRequestDto userDto) {
        if (userDto == null)
            return null;

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setCountry(userDto.getCountry());
        user.setMobileNumber(userDto.getMobileNumber());
        user.setRegistrationDate(userDto.getRegistrationDate());
        user.setGender(userDto.getGender());
        user.setIsAdmin(userDto.getIsAdmin() == null ? false : userDto.getIsAdmin());
        user.setIsVerified(false);
        user.setVat(userDto.getVat());
        user.setDateOfBirth(userDto.getDateOfBirth());
        user.setStreet(userDto.getStreet());
        user.setCity(userDto.getCity());
        user.setPostalCode(userDto.getPostalCode());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setRatingAsSeller(0);
        user.setRatingAsBidder(0);

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
        userResponse.setStreet(user.getStreet());
        userResponse.setCity(user.getCity());
        userResponse.setPostalCode(user.getPostalCode());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setRatingAsSeller(user.getRatingAsSeller());
        userResponse.setRatingasBidder(user.getRatingAsBidder());

        return userResponse;
    }



    public UserResponseDto userToUserResponse(User user){
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
         userResponse.setStreet(user.getStreet());
         userResponse.setCity(user.getCity());
         userResponse.setPostalCode(user.getPostalCode());
         userResponse.setPhoneNumber(user.getPhoneNumber());
         userResponse.setRatingAsSeller(user.getRatingAsSeller());
         userResponse.setRatingasBidder(user.getRatingAsBidder());

         return userResponse;

    }

    public void update(User user, UserRequestDto userRequestDto) {
        user.setUserId(userRequestDto.getUserId());
        user.setEmail(userRequestDto.getEmail());
        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getFirstName());
        user.setCountry(userRequestDto.getCountry());
        user.setMobileNumber(userRequestDto.getMobileNumber());
        user.setRegistrationDate(userRequestDto.getRegistrationDate());
        user.setGender(userRequestDto.getGender());
        user.setVat(userRequestDto.getVat());
        user.setDateOfBirth(userRequestDto.getDateOfBirth());
    }

}
