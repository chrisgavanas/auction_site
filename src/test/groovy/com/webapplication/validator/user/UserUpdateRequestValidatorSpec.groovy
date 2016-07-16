package com.webapplication.validator.user

import com.webapplication.dto.user.Gender
import com.webapplication.dto.user.UserUpdateRequestDto
import com.webapplication.error.user.UserError
import com.webapplication.exception.ValidationException
import spock.lang.Specification
import spock.lang.Unroll

class UserUpdateRequestValidatorSpec extends Specification {

    UserUpdateRequestValidator userUpdateRequestValidator

    def setup() {
        userUpdateRequestValidator = new UserUpdateRequestValidator()
    }

    def "User request to update his info with null data"() {
        given:
        UserUpdateRequestDto updateRequestDto = null

        when:
        userUpdateRequestValidator.validate(updateRequestDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == UserError.MISSING_DATA.description
    }

    @Unroll
    def "User request to update his info with missing data"() {
        given:
        UserUpdateRequestDto updateRequestDto = new UserUpdateRequestDto(email: email, firstName: firstName,
                lastName: lastName, country: country, mobileNumber: mobileNumber, gender: gender, vat: vat,
                dateOfBirth: dateOfBirth, phoneNumber: phoneNumber)

        when:
        userUpdateRequestValidator.validate(updateRequestDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == UserError.MISSING_DATA.description

        where:
        email   | firstName   | lastName   | country   | mobileNumber | gender   | vat   | dateOfBirth | phoneNumber
        null    | "firstName" | "lastName" | "country" | "697777777"  | Gender.M | "vat" | new Date()  | "2109595995"
        "email" | null        | "lastName" | "country" | "697777777"  | Gender.M | "vat" | new Date()  | "2109595995"
        "email" | "firstName" | null       | "country" | "697777777"  | Gender.M | "vat" | new Date()  | "2109595995"
        "email" | "firstName" | "lastName" | null      | "697777777"  | Gender.M | "vat" | new Date()  | "2109595995"
        "email" | "firstName" | "lastName" | "country" | null         | Gender.M | "vat" | new Date()  | "2109595995"
        "email" | "firstName" | "lastName" | "country" | "697777777"  | null     | "vat" | new Date()  | "2109595995"
        "email" | "firstName" | "lastName" | "country" | "697777777"  | Gender.M | "vat" | new Date()  | null
        "email" | "firstName" | "lastName" | "country" | "697777777"  | Gender.M | null  | new Date()  | "2109595995"
        "email" | "firstName" | "lastName" | "country" | "697777777"  | Gender.M | "vat" | null        | null
        "email" | null        | "lastName" | null      | null         | Gender.M | "vat" | new Date()  | "2109595995"
        "email" | "firstName" | null       | "country" | "697777777"  | null     | "vat" | new Date()  | "2109595995"
        "email" | "firstName" | "lastName" | "country" | null         | Gender.M | "vat" | null        | "2109595995"
        null    | null        | "lastName" | "country" | "697777777"  | Gender.M | "vat" | new Date()  | "2109595995"
        null    | null        | null       | null      | null         | null     | null  | null        | null
    }

    @Unroll
    def "User request to update his info with invalid data"() {
        given:
        UserUpdateRequestDto updateRequestDto = new UserUpdateRequestDto(email: email, firstName: firstName,
                lastName: lastName, country: country, mobileNumber: mobileNumber, gender: gender, vat: vat,
                dateOfBirth: dateOfBirth, phoneNumber: phoneNumber)

        when:
        userUpdateRequestValidator.validate(updateRequestDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == UserError.INVALID_DATA.description

        where:
        email   | firstName   | lastName   | country   | mobileNumber | gender   | vat      | dateOfBirth | phoneNumber
        ""      | "firstName" | "lastName" | "country" | "697777777"  | Gender.M | "vat"    | new Date()  | "2109595995"
        "email" | ""          | "lastName" | "country" | "697777777"  | Gender.M | "vat"    | new Date()  | "2109595995"
        "email" | "firstName" | ""         | "country" | "697777777"  | Gender.M | "vat"    | new Date()  | "2109595995"
        "email" | "firstName" | "lastName" | ""        | "697777777"  | Gender.M | "vat"    | new Date()  | "2109595995"
        "email" | "firstName" | "lastName" | "country" | ""           | Gender.M | "vat"    | new Date()  | "2109595995"
        "email" | "firstName" | "lastName" | "country" | "697777777"  | Gender.M | "vat"    | new Date()  | ""
        "email" | "firstName" | "lastName" | "country" | "697777777"  | Gender.M | ""       | new Date()  | "2109595995"
        "email" | "firstName" | "lastName" | "country" | "697777777"  | Gender.M | "vat"    | new Date()  | ""
        "email" | ""          | "lastName" | ""        | ""           | Gender.M | "vat"    | new Date()  | "2109595995"
        "email" | "firstName" | ""         | "country" | "697777777"  | Gender.M | "vat"    | new Date()  | "2109595995"
        "email" | "firstName" | "lastName" | "country" | ""           | Gender.M | "vat"    | new Date()  | "2109595995"
        ""      | ""          | "lastName" | "country" | "697777777"  | Gender.M | "vat"    | new Date()  | "2109595995"
        ""      | ""          | ""         | ""        | ""           | Gender.M | Gender.M | new Date()  | ""
    }

}
