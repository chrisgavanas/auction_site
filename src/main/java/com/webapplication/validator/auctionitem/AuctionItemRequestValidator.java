package com.webapplication.validator.auctionitem;


import com.webapplication.dto.auctionitem.AddAuctionItemRequestDto;
import com.webapplication.dto.auctionitem.StartAuctionDto;
import com.webapplication.exception.ValidationException;
import com.webapplication.validator.ValidatorWrapper.AuctionItemRequestValidatorWrapper;
import org.springframework.beans.factory.annotation.Autowired;

public class AuctionItemRequestValidator implements AuctionItemRequestValidatorWrapper {

    @Autowired
    private AuctionItemRequestValidator auctionItemRequestValidator;

    @Autowired
    private StartAuctionValidator startAuctionValidator;

    @Override
    public void validate(AddAuctionItemRequestDto addAuctionItemRequestDto) throws ValidationException {
        auctionItemRequestValidator.validate(addAuctionItemRequestDto);
    }

    @Override
    public void validate(StartAuctionDto startAuctionDto) throws ValidationException {
        startAuctionValidator.validate(startAuctionDto);
    }
}
