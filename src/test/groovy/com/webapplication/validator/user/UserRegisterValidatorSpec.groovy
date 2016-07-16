package com.webapplication.validator.user

import com.webapplication.dto.user.AddressDto
import com.webapplication.dto.user.Gender
import com.webapplication.dto.user.UserRegisterRequestDto
import com.webapplication.error.user.UserRegisterError
import com.webapplication.exception.ValidationException
import spock.lang.Specification
import spock.lang.Unroll

public class UserRegisterValidatorSpec extends Specification {

    UserRegisterValidator userRegisterValidator

    def setup() {
        userRegisterValidator = new UserRegisterValidator()
    }

    def "User attempts to register with null data"() {
        given:
        UserRegisterRequestDto userRegisterRequestDto = null

        when:
        userRegisterValidator.validate(userRegisterRequestDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == UserRegisterError.MISSING_DATA.description
    }

    @Unroll
    def "User attempts to register with missing some of the mandatory data"() {
        given:
        UserRegisterRequestDto userRegisterRequestDto = new UserRegisterRequestDto(country: country,
                dateOfBirth: dateOfBirth, email: email, firstName: firstName, gender: gender,
                lastName: lastName, mobileNumber: mobileNumber, password: password, registrationDate: registrationDate,
                username: username, vat: vat)

        when:
        userRegisterValidator.validate(userRegisterRequestDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == UserRegisterError.MISSING_DATA.description

        where:
        country   | dateOfBirth | email   | firstName   | gender   | lastName   | mobileNumber | password   | registrationDate | username   | vat
        "country" | new Date()  | "email" | "firstname" | Gender.M | "lastname" | "6977777777" | "password" | new Date()       | "username" | null
        "country" | new Date()  | "email" | "firstname" | Gender.M | "lastname" | null         | "password" | new Date()       | "username" | "vat"
        "country" | new Date()  | "email" | "firstname" | null     | "lastname" | "6977777777" | "password" | new Date()       | "username" | "vat"
        "country" | new Date()  | null    | "firstname" | Gender.M | "lastname" | "6977777777" | "password" | new Date()       | "username" | "vat"
        null      | new Date()  | "email" | "firstname" | Gender.M | "lastname" | "6977777777" | "password" | new Date()       | null       | "vat"
        "country" | null        | null    | "firstname" | null     | null       | null         | "password" | null             | "username" | null
        null      | null        | null    | "firstname" | null     | null       | "6977777777" | null       | new Date()       | null       | "vat"
        null      | new Date()  | null    | null        | null     | "lastname" | "6977777777" | null       | null             | "username" | "vat"
        "country" | new Date()  | "email" | null        | null     | null       | null         | null       | null             | null       | "vat"
        null      | null        | "email" | "firstname" | Gender.M | "lastname" | "6977777777" | null       | null             | "username" | null
        "country" | new Date()  | "email" | null        | null     | "lastname" | null         | null       | new Date()       | null       | "vat"
        "country" | null        | "email" | null        | Gender.M | null       | "6977777777" | "password" | null             | "password" | "vat"
        "country" | new Date()  | null    | "firstname" | null     | "lastname" | "6977777777" | null       | null             | null       | null
        null      | null        | null    | null        | null     | null       | null         | null       | null             | null       | null
    }

    @Unroll
    def "User attempts to register with invalid some of the mandatory data"() {
        given:
        UserRegisterRequestDto userRegisterRequestDto = new UserRegisterRequestDto(country: country,
                dateOfBirth: dateOfBirth, email: email, firstName: firstName, gender: gender,
                lastName: lastName, mobileNumber: mobileNumber, password: password, registrationDate: registrationDate,
                username: username, vat: vat)

        when:
        userRegisterValidator.validate(userRegisterRequestDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == UserRegisterError.INVALID_DATA.description

        where:
        country   | dateOfBirth | email   | firstName   | gender   | lastName   | mobileNumber | password   | registrationDate | username   | vat
        "country" | new Date()  | "email" | "firstname" | Gender.M | "lastname" | "6977777777" | "password" | new Date()       | "username" | ""
        "country" | new Date()  | "email" | "firstname" | Gender.M | "lastname" | ""           | "password" | new Date()       | "username" | "vat"
        "country" | new Date()  | "email" | "firstname" | Gender.M | "lastname" | ""           | "password" | new Date()       | "username" | "vat"
        "country" | new Date()  | ""      | "firstname" | Gender.M | "lastname" | "6977777777" | "password" | new Date()       | "username" | "vat"
        ""        | new Date()  | "email" | "firstname" | Gender.M | "lastname" | "6977777777" | "password" | new Date()       | ""         | "vat"
        "country" | new Date()  | ""      | "firstname" | Gender.M | ""         | ""           | "password" | new Date()       | "username" | ""
        ''        | new Date()  | ""      | "firstname" | Gender.M | ""         | "6977777777" | ""         | new Date()       | ""         | "vat"
        ""        | new Date()  | ""      | ""          | Gender.M | "lastname" | "6977777777" | ""         | new Date()       | "username" | "vat"
        "country" | new Date()  | "email" | ""          | Gender.M | ""         | ""           | ""         | new Date()       | ""         | "vat"
        ""        | new Date()  | "email" | "firstname" | Gender.M | "lastname" | "6977777777" | ""         | new Date()       | "username" | ""
        "country" | new Date()  | "email" | ""          | Gender.M | "lastname" | ""           | ""         | new Date()       | ""         | "vat"
        "country" | new Date()  | "email" | ""          | Gender.M | ""         | "6977777777" | "password" | new Date()       | "password" | "vat"
        "country" | new Date()  | ""      | "firstname" | Gender.M | "lastname" | "6977777777" | ""         | new Date()       | ""         | ""
        ""        | new Date()  | ""      | ""          | Gender.M | ""         | ""           | ""         | new Date()       | ""         | ""
    }

    @Unroll
    def "User attempts to register with missing a part of his residence info"() {
        given:
        AddressDto addressDto = new AddressDto(city: city, postalCode: postalCode, street: street)
        UserRegisterRequestDto userRegisterRequestDto = new UserRegisterRequestDto(country: "country",
                dateOfBirth: new Date(), email: "email", firstName: "firstname", gender: Gender.M,
                lastName: "lastname", mobileNumber: "6977777777", password: "password", registrationDate: new Date(),
                username: "username", vat: "vat", address: addressDto)

        when:
        userRegisterValidator.validate(userRegisterRequestDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == UserRegisterError.MISSING_DATA.description

        where:
        city        | postalCode | street
        null        | null       | null
        "Kallithea" | "17672"    | null
        "Kallithea" | null       | "Andromahis"
        null        | "17672"    | "Andromahis"
        null        | null       | "Andromahis"
        null        | "17672"    | null
        "Kallithea" | null       | null
    }

    @Unroll
    def "User attempts to register with invalid a part of his residence info"() {
        given:
        AddressDto addressDto = new AddressDto(city: city, postalCode: postalCode, street: street)
        UserRegisterRequestDto userRegisterRequestDto = new UserRegisterRequestDto(country: "country",
                dateOfBirth: new Date(), email: "email", firstName: "firstname", gender: Gender.M,
                lastName: "lastname", mobileNumber: "6977777777", password: "password", registrationDate: new Date(),
                username: "username", vat: "vat", address: addressDto)

        when:
        userRegisterValidator.validate(userRegisterRequestDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == UserRegisterError.INVALID_DATA.description

        where:
        city        | postalCode | street
        ""          | ""         | ""
        "Kallithea" | "17672"    | ""
        "Kallithea" | ""         | "Andromahis"
        ""          | "17672"    | "Andromahis"
        ""          | ""         | "Andromahis"
        ""          | "17672"    | ""
        "Kallithea" | ""         | ""
    }

    def "User successfully registers"() {
        given:
        AddressDto addressDto = new AddressDto(city: "city", postalCode: "17672", street: "street")
        UserRegisterRequestDto userRegisterRequestDto = new UserRegisterRequestDto(country: "country",
                dateOfBirth: new Date(), email: "email", firstName: "firstname", gender: Gender.M,
                lastName: "lastname", mobileNumber: "6977777777", password: "password", registrationDate: new Date(),
                username: "username", vat: "vat", address: addressDto)

        when:
        userRegisterValidator.validate(userRegisterRequestDto)

        then:
        ValidationException e = notThrown()
    }

}
