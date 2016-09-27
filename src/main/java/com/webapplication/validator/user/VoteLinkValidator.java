package com.webapplication.validator.user;


import com.webapplication.dto.user.VoteLinkDto;
import com.webapplication.error.user.UserError;
import com.webapplication.exception.ValidationException;
import com.webapplication.validator.Validator;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class VoteLinkValidator implements Validator<VoteLinkDto> {

    public void validate(VoteLinkDto voteLinkDto) throws ValidationException {
        Optional.ofNullable(voteLinkDto).orElseThrow(() -> new ValidationException(UserError.MISSING_DATA));
        if (Stream.of(voteLinkDto.getMessageId(), voteLinkDto.getAuctionItemId(), voteLinkDto.getVoteReceiverId()).anyMatch(Objects::isNull))
            throw new ValidationException(UserError.MISSING_DATA);
        if (Stream.of(voteLinkDto.getMessageId(), voteLinkDto.getAuctionItemId(), voteLinkDto.getVoteReceiverId()).anyMatch(String::isEmpty))
            throw new ValidationException(UserError.INVALID_DATA);
    }

}
