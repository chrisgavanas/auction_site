package com.webapplication.validator.auctionitem;


import java.util.Arrays;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.webapplication.dto.auctionitem.AddAuctionItemRequestDto;
import com.webapplication.error.auctionitem.AuctionItemError;
import com.webapplication.exception.ValidationException;
import com.webapplication.validator.Validator;

@Component
public class AuctionItemValidator implements Validator<AddAuctionItemRequestDto> {

	@Override
	public void validate(AddAuctionItemRequestDto request) throws ValidationException {
        if (request == null)
            throw new ValidationException(AuctionItemError.MISSING_DATA);

        if (Arrays.asList(request.getName(), request.getStartDate(), request.getEndDate(),
        		request.getUserId()).stream().anyMatch(Objects::isNull))
            throw new ValidationException(AuctionItemError.MISSING_DATA);
        		
        if (request.getName().isEmpty())
            throw new ValidationException(AuctionItemError.INVALID_DATA);

        if (Arrays.asList(request.getCurrentBid(), request.getBuyout())
        		.stream().filter(Objects::nonNull).anyMatch(value -> {
        			return value < 0; }))
            throw new ValidationException(AuctionItemError.INVALID_DATA);

        if (request.getStartDate().after(request.getEndDate()))
            throw new ValidationException(AuctionItemError.INVALID_DATA);

        if (request.getLatitude() != null && (request.getLatitude() > 90 || request.getLatitude() < -90))
        	throw new ValidationException(AuctionItemError.INVALID_DATA);
        
        if (request.getLongitude() != null && (request.getLongitude() > 180 || request.getLongitude() < -180))
        	throw new ValidationException(AuctionItemError.INVALID_DATA);
    }

}
