package com.webapplication.validator.user;


import com.google.common.base.Strings;
import com.webapplication.dto.user.MessageDto;
import com.webapplication.error.user.UserError;
import com.webapplication.exception.ValidationException;
import com.webapplication.validator.Validator;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class MessageValidator implements Validator<MessageDto> {

    @Override
    public void validate(MessageDto messageDto) throws ValidationException {
        Optional.ofNullable(messageDto).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        if (Stream.of(messageDto.getMessage(), messageDto.getUsername()).anyMatch(Objects::isNull))
            throw new ValidationException(UserError.MISSING_DATA);

        if (Stream.of(messageDto.getMessage(), messageDto.getUsername()).anyMatch(Strings::isNullOrEmpty))
            throw new ValidationException(UserError.MISSING_DATA);
    }

}
