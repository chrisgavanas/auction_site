package com.webapplication.validator.auctionitem;


import com.google.common.base.Strings;
import com.webapplication.dto.auctionitem.SearchAuctionItemDto;
import com.webapplication.error.auctionitem.AuctionItemError;
import com.webapplication.exception.ValidationException;
import com.webapplication.validator.Validator;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class SearchAuctionItemValidator implements Validator<SearchAuctionItemDto> {

    public void validate(SearchAuctionItemDto searchAuctionItemDto) throws ValidationException {
        Optional.ofNullable(searchAuctionItemDto).orElseThrow(() -> new ValidationException(AuctionItemError.MISSING_DATA));
        if (Stream.of(searchAuctionItemDto.getCategoryId(), searchAuctionItemDto.getText())
                .anyMatch(Objects::isNull))
            throw new ValidationException(AuctionItemError.MISSING_DATA);
    }

}
