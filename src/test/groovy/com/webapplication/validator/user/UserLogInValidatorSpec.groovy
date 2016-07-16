package com.webapplication.validator.user

import com.webapplication.dto.user.UserLogInRequestDto
import com.webapplication.error.user.UserLogInError
import com.webapplication.exception.ValidationException
import spock.lang.Specification
import spock.lang.Unroll


class UserLogInValidatorSpec extends Specification {

    UserLogInValidator userLogInValidator

    def setup() {
        userLogInValidator = new UserLogInValidator()
    }

    def "User attempts to log in with null data"() {
        given:
        UserLogInRequestDto userLogInRequestDto = null

        when:
        userLogInValidator.validate(userLogInRequestDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == UserLogInError.MISSING_DATA.description
    }

    @Unroll
    def "User attempts to log in with missing data"() {
        given:
        UserLogInRequestDto userLogInRequestDto = new UserLogInRequestDto(username: username,
                email: email, password: password)

        when:
        userLogInValidator.validate(userLogInRequestDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == UserLogInError.MISSING_DATA.description

        where:
        username   | email   | password
        null       | null    | null
        null       | "email" | null
        null       | null    | "password"
        "username" | null    | null
        "username" | "email" | null
    }

    @Unroll
    def "User attempts to log in with invalid data"() {
        given:
        UserLogInRequestDto userLogInRequestDto = new UserLogInRequestDto(username: username,
                email: email, password: password)

        when:
        userLogInValidator.validate(userLogInRequestDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == UserLogInError.INVALID_DATA.description

        where:
        username   | email   | password
        ""         | "email" | "password"
        ""         | ""      | "password"
        ""         | ""      | ""
        "username" | ""      | "password"
        "username" | ""      | ""
        "username" | "email" | ""
        ""         | "email" | "password"
        "username" | ""      | "password"
    }

    def "User successfully logs in"() {
        given:
        UserLogInRequestDto userLogInRequestDto = new UserLogInRequestDto(username: "username",
                "email": "email", password: "password")

        when:
        userLogInValidator.validate(userLogInRequestDto)

        then:
        ValidationException e = notThrown()
    }
}
