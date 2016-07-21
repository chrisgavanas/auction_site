package com.webapplication.mapper

import com.webapplication.dto.user.*
import com.webapplication.entity.Address
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
                country: 'Greece', mobileNumber: '6988888888', registrationDate: date, gender: Gender.M, vat: '1234567890',
                dateOfBirth: date, address: addressDto, phoneNumber: '2109595959', email: 'chrisgavanas@gmail.com')

        when:
        User user = userMapper.registerRequestToUser(userRegisterRequestDto)

        then:
        with(user) {
            username == 'chris'
            password == '123123'
            firstName == 'Chris'
            lastName == 'Gavanas'
            email == 'chrisgavanas@gmail.com'
            country == 'Greece'
            mobileNumber == '6988888888'
            registrationDate == date
            gender == Gender.M
            !isAdmin
            !isVerified
            vat == '1234567890'
            dateOfBirth == date
            with(address) {
                city == 'Athens'
                street == 'Andromahis'
                postalCode == '17672'
            }
            phoneNumber == '2109595959'
            ratingAsBidder == 0
            ratingAsSeller == 0
            auctionItemIds == []
            bidIds == []
        }
    }

    def "Convert User to UserRegisterResponseDto"() {
        given:
        Date date = new Date()
        Address address = new Address(city: 'Athens', street: 'Andromahis', postalCode: '17672')
        User user = new User(userId: '578f869f5a61a77b7915252a', username: 'chris', firstName: 'Chris', lastName: 'Gavanas',
                country: 'Greece', mobileNumber: '6988888888', registrationDate: date, gender: Gender.M, isAdmin: false,
                vat: '1234567890', dateOfBirth: date, address: address, phoneNumber: '2109595959', ratingAsBidder: 123,
                ratingAsSeller: 234, email: 'chrisgavanas@gmail.com', isVerified: false)

        when:
        UserRegisterResponseDto userRegisterResponseDto = userMapper.userToRegisterResponse(user)

        then:
        with(userRegisterResponseDto) {
            userId == '578f869f5a61a77b7915252a'
            username == 'chris'
            firstName == 'Chris'
            lastName == 'Gavanas'
            email == 'chrisgavanas@gmail.com'
            country == 'Greece'
            mobileNumber == '6988888888'
            registrationDate == date
            gender == Gender.M
            !isAdmin
            !isVerified
            vat == '1234567890'
            dateOfBirth == date
            with(address) {
                city == 'Athens'
                street == 'Andromahis'
                postalCode == '17672'
            }
            phoneNumber == '2109595959'
            ratingAsBidder == 123
            ratingAsSeller == 234
        }
    }

    def "Convert User to UserResponseDto"() {
        given:
        Date date = new Date()
        Address address = new Address(city: 'Athens', street: 'Andromahis', postalCode: '17672')
        User user = new User(userId: '578f869f5a61a77b7915252a', username: 'chris', firstName: 'Chris', lastName: 'Gavanas',
                country: 'Greece', mobileNumber: '6988888888', registrationDate: date, gender: Gender.M, isAdmin: false,
                vat: '1234567890', dateOfBirth: date, address: address, phoneNumber: '2109595959', ratingAsBidder: 123,
                ratingAsSeller: 234, isVerified: true, email: 'chrisgavanas@gmail.com')

        when:
        UserResponseDto userResponseDto = userMapper.userToUserResponse(user)

        then:
        with(userResponseDto) {
            userId == '578f869f5a61a77b7915252a'
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
            with(address) {
                city == 'Athens'
                street == 'Andromahis'
                postalCode == '17672'
            }
            phoneNumber == '2109595959'
            ratingAsBidder == 123
            ratingAsSeller == 234
            isVerified
            email == 'chrisgavanas@gmail.com'
        }
    }

    def "Updates a user entity from a UserUpdateRequestDto"() {
        given:
        Date date = new Date()
        Address address = new Address(city: 'Athens', street: 'Andromahis', postalCode: '17672')
        AddressDto addressDto = new AddressDto(city: 'Athens', street: 'Davakh', postalCode: '17672')
        User user = new User(username: 'chris', firstName: 'Chris', lastName: 'Gavanas',
                country: 'Greece', mobileNumber: '6988888888', registrationDate: date, gender: Gender.M, isAdmin: false,
                vat: '1234567890', dateOfBirth: date, address: address, phoneNumber: '2109595959', ratingAsBidder: 123,
                ratingAsSeller: 234)

        UserUpdateRequestDto userUpdateRequestDto = new UserUpdateRequestDto(email: 'chrisgavanas@gmail.com', firstName: 'Chris', lastName: 'Gavanas',
                country: 'Greece', mobileNumber: '6977777777', gender: Gender.M, vat: '1234567890', dateOfBirth: date, address: addressDto,
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
            with(user.address) {
                city == 'Athens'
                street == 'Davakh'
                postalCode == '17672'
            }
            phoneNumber == '2109596978'
            ratingAsBidder == 123
            ratingAsSeller == 234
        }
    }

    def "Convert userList to userResponseList"() {
        given:
        Address address1 = new Address(city: 'Kallithea', postalCode: '17672', street: 'Andromahis')
        Address address2 = new Address(city: 'Kallithea2', postalCode: '176722', street: 'Andromahis2')
        List<User> userList = [
                new User(username: 'chris', firstName: 'Chris', lastName: 'Gavanas',
                        country: 'Greece', mobileNumber: '6988888888', registrationDate: new Date(123), gender: Gender.M, isAdmin: false,
                        vat: '1234567890', dateOfBirth: new Date(123), address: address1, phoneNumber: '2109595959', ratingAsBidder: 123,
                        ratingAsSeller: 234),
                new User(username: 'chris2', firstName: 'Chris2', lastName: 'Gavanas2',
                        country: 'Greece2', mobileNumber: '69888888882', registrationDate: new Date(124), gender: Gender.M, isAdmin: true,
                        vat: '12345678902', dateOfBirth: new Date(124), address: address2, phoneNumber: '21095959592', ratingAsBidder: 234,
                        ratingAsSeller: 345)
        ]

        when:
        List<UserResponseDto> userResponseDtoList = userMapper.userListToUserResponseList(userList)

        then:
        with(userResponseDtoList[0]) {
            username == 'chris'
            firstName == 'Chris'
            lastName == 'Gavanas'
            country == 'Greece'
            mobileNumber == '6988888888'
            registrationDate == new Date(123)
            gender == Gender.M
            vat == '1234567890'
            dateOfBirth == new Date(123)
            phoneNumber == '2109595959'
            ratingAsBidder == 123
            ratingAsSeller == 234
            with(address) {
                city == 'Kallithea'
                postalCode == '17672'
                street == 'Andromahis'
            }
        }
        with(userResponseDtoList[1]) {
            username == 'chris2'
            firstName == 'Chris2'
            lastName == 'Gavanas2'
            country == 'Greece2'
            mobileNumber == '69888888882'
            registrationDate == new Date(124)
            gender == Gender.M
            vat == '12345678902'
            dateOfBirth == new Date(124)
            phoneNumber == '21095959592'
            ratingAsBidder == 234
            ratingAsSeller == 345
            with(address) {
                city == 'Kallithea2'
                postalCode == '176722'
                street == 'Andromahis2'
            }

        }
    }

}
