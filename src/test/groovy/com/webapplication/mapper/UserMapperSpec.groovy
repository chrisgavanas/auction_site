package com.webapplication.mapper

import com.webapplication.dto.user.AddressDto
import com.webapplication.dto.user.Gender
import com.webapplication.dto.user.UserRegisterRequestDto
import com.webapplication.dto.user.UserRegisterResponseDto
import com.webapplication.dto.user.UserResponseDto
import com.webapplication.dto.user.UserUpdateRequestDto
import com.webapplication.entity.User
import spock.lang.Specification

class UserMapperSpec extends Specification {

    UserMapper userMapper

    def setup() {
        userMapper = new UserMapper()
    }

    def "Converts UserRegisterRequestDto to User with null data"() {
        when:
        User user = userMapper.registerRequestToUser(null)

        then:
        user == null
    }

    def "Converts User to UserRegisterResponseDto with null data"() {
        when:
        UserRegisterResponseDto userRegisterResponseDto = userMapper.userToRegisterResponse(null)

        then:
        userRegisterResponseDto == null
    }

    def "Converts User to UserResponseDto with null data"() {
        when:
        UserResponseDto userResponseDto = userMapper.userToUserResponse(null)

        then:
        userResponseDto == null
    }

    def "Convert UserRegisterRequestDto to User"() {
        given:
        Date date = new Date()
        AddressDto addressDto = new AddressDto(city: 'Athens', street: 'Andromahis', postalCode: '17672')
        UserRegisterRequestDto userRegisterRequestDto = new UserRegisterRequestDto(username: 'chris', password: '123123', firstName: 'Chris', lastName: 'Gavanas',
                country: 'Greece', mobileNumber: '6988888888', registrationDate: date, gender: Gender.M, isAdmin: false, vat: '1234567890',
                dateOfBirth: date, address: addressDto, phoneNumber: '2109595959')

        when:
        User user = userMapper.registerRequestToUser(userRegisterRequestDto)

        then:
        with(user) {
            username == 'chris'
            password == '123123'
            firstName == 'Chris'
            lastName == 'Gavanas'
            country == 'Greece'
            mobileNumber == '6988888888'
            registrationDate == date
            gender == Gender.M
            !isAdmin
            vat == '1234567890'
            dateOfBirth == date
            with(address) {
                city == 'Athens'
                street == 'Andromahis'
                postalCode == '17672'
            }
            phoneNumber == '2109595959'
            ratingAsBidder == 0.floatValue()
            ratingAsSeller == 0.floatValue()
        }
    }

    def "Convert User to UserRegisterResponseDto"() {
        given:
        Date date = new Date()
        AddressDto addressDto = new AddressDto(city: 'Athens', street: 'Andromahis', postalCode: '17672')
        User user = new User(username: 'chris', firstName: 'Chris', lastName: 'Gavanas',
                country: 'Greece', mobileNumber: '6988888888', registrationDate: date, gender: Gender.M, isAdmin: false,
                vat: '1234567890', dateOfBirth: date, address: addressDto, phoneNumber: '2109595959', ratingAsBidder: 4.9,
                ratingAsSeller: 2.12)

        when:
        UserRegisterResponseDto userRegisterResponseDto = userMapper.userToRegisterResponse(user)

        then:
        with(userRegisterResponseDto) {
            username == 'chris'
            firstName == 'Chris'
            lastName == 'Gavanas'
            country == 'Greece'
            mobileNumber == '6988888888'
            registrationDate == date
            gender == Gender.M
            !isAdmin
            vat == '1234567890'
            dateOfBirth == date
            with(addressDto) {
                city == 'Athens'
                street == 'Andromahis'
                postalCode == '17672'
            }
            phoneNumber == '2109595959'
            ratingasBidder == 4.9.floatValue()
            ratingAsSeller == 2.12.floatValue()
        }
    }

    def "Convert User to UserResponseDto"() {
        given:
        Date date = new Date()
        AddressDto addressDto = new AddressDto(city: 'Athens', street: 'Andromahis', postalCode: '17672')
        User user = new User(username: 'chris', firstName: 'Chris', lastName: 'Gavanas',
                country: 'Greece', mobileNumber: '6988888888', registrationDate: date, gender: Gender.M, isAdmin: false,
                vat: '1234567890', dateOfBirth: date, address: addressDto, phoneNumber: '2109595959', ratingAsBidder: 4.9,
                ratingAsSeller: 2.12)

        when:
        UserResponseDto userResponseDto = userMapper.userToUserResponse(user)

        then:
        with(userResponseDto) {
            username == 'chris'
            firstName == 'Chris'
            lastName == 'Gavanas'
            country == 'Greece'
            mobileNumber == '6988888888'
            registrationDate == date
            gender == Gender.M
            !isAdmin
            vat == '1234567890'
            dateOfBirth == date
            with(addressDto) {
                city == 'Athens'
                street == 'Andromahis'
                postalCode == '17672'
            }
            phoneNumber == '2109595959'
            ratingasBidder == 4.9.floatValue()
            ratingAsSeller == 2.12.floatValue()
        }
    }

    def "Updates a user entity from a UserUpdateRequestDto"() {
        given:
        Date date = new Date()
        AddressDto userAddressDto = new AddressDto(city: 'Athens', street: 'Andromahis', postalCode: '17672')
        AddressDto userUpdateAddressDto = new AddressDto(city: 'Athens', street: 'Davakh', postalCode: '17672')
        User user = new User(username: 'chris', firstName: 'Chris', lastName: 'Gavanas',
                country: 'Greece', mobileNumber: '6988888888', registrationDate: date, gender: Gender.M, isAdmin: false,
                vat: '1234567890', dateOfBirth: date, address: userAddressDto , phoneNumber: '2109595959', ratingAsBidder: 4.9,
                ratingAsSeller: 2.12)

        UserUpdateRequestDto userUpdateRequestDto  = new UserUpdateRequestDto(email: 'chrisgavanas@gmail.com', firstName: 'Chris', lastName: 'Gavanas',
        country: 'Greece', mobileNumber: '6977777777', gender: Gender.M, vat: '1234567890', dateOfBirth: date, address: userUpdateAddressDto,
        phoneNumber: '2109596978')

        when:
        userMapper.update(user, userUpdateRequestDto)

        then:
        with(user) {
            username == 'chris'
            firstName == 'Chris'
            lastName == 'Gavanas'
            country == 'Greece'
            mobileNumber == '6977777777'
            registrationDate == date
            gender == Gender.M
            !isAdmin
            vat == '1234567890'
            dateOfBirth == date
            with(address) {
                city == 'Athens'
                street == 'Davakh'
                postalCode == '17672'
            }
            phoneNumber == '2109596978'
            ratingAsBidder == 4.9.floatValue()
            ratingAsSeller == 2.12.floatValue()
        }
    }

//    def "Convert userList to userResponseList"() {
//        given:
//        List<User> userList = [
//                new User(username: 'chris', firstName: 'Chris', lastName: 'Gavanas',
//                        country: 'Greece', mobileNumber: '6988888888', registrationDate: date, gender: Gender.M, isAdmin: false,
//                        vat: '1234567890', dateOfBirth: date, address: userAddressDto , phoneNumber: '2109595959', ratingAsBidder: 4.9,
//                        ratingAsSeller: 2.12),
//                new User(username: 'chris2', firstName: 'Chris2', lastName: 'Gavanas',
//                        country: 'Greece', mobileNumber: '6988888888', registrationDate: date, gender: Gender.M, isAdmin: false,
//                        vat: '1234567890', dateOfBirth: date, address: userAddressDto , phoneNumber: '2109595959', ratingAsBidder: 4.9,
//                        ratingAsSeller: 2.12)
//        ]
//
//        when:
//        List<UserResponseDto> userResponseDtoList = userMapper.userListToUserResponseList(userList)
//
//        then:
//        with(userResponseDtoList[0]) {
//            userId == 1
//        }
//    }

}
