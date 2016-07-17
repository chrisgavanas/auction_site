package com.webapplication.validator.user

import com.webapplication.dto.user.ChangePasswordRequestDto
import com.webapplication.error.user.UserError
import com.webapplication.exception.ValidationException
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll


class ChangePasswordValidatorSpec extends Specification {

    ChangePasswordValidator changePasswordValidator

    def setup() {
        changePasswordValidator = new ChangePasswordValidator()
    }

    def "User requests for a password with null data"() {
        given:
        ChangePasswordRequestDto changePasswordRequestDto = null

        when:
        changePasswordValidator.validate(changePasswordRequestDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == UserError.MISSING_DATA.description
    }

    @Unroll
    def "User requests for a password change with missing data"() {
        given:
        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto(oldPassword: oldPassword,
                newPassword: newPassword)

        when:
        changePasswordValidator.validate(changePasswordRequestDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == UserError.MISSING_DATA.description

        where:
        oldPassword | newPassword
        null        | "newPass"
        "oldPass"   | null
        null        | null
    }

    @Unroll
    def "User requests for a password change with empty data"() {
        given:
        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto(oldPassword: oldPassword,
                newPassword: newPassword)

        when:
        changePasswordValidator.validate(changePasswordRequestDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == UserError.INVALID_DATA.description

        where:
        oldPassword | newPassword
        ""          | "newPass"
        "oldPass"   | ""
        ""          | ""
    }

    def "User requests for a password change with the new password to be the same with the old one"() {
        given:
        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto(oldPassword: "A password",
                newPassword: "A password")

        when:
        changePasswordValidator.validate(changePasswordRequestDto)

        then:
        ValidationException e = thrown()
        e.localizedMessage == UserError.NEW_PASSWORD_DO_NOT_DIFFER.description
    }

    def "User requests for a password change successfully"() {
        given:
        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto(oldPassword: "A password",
                newPassword: "Another password")

        when:
        changePasswordValidator.validate(changePasswordRequestDto)

        then:
        ValidationException e = notThrown()
    }
}
