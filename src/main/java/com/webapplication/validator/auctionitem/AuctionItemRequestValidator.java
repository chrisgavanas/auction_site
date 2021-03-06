package com.webapplication.validator.auctionitem;


import com.webapplication.dto.auctionitem.AddAuctionItemRequestDto;
import com.webapplication.dto.auctionitem.AuctionItemUpdateRequestDto;
import com.webapplication.dto.auctionitem.BidRequestDto;
import com.webapplication.dto.auctionitem.SearchAuctionItemDto;
import com.webapplication.dto.auctionitem.StartAuctionDto;
import com.webapplication.exception.ValidationException;
import com.webapplication.validator.ValidatorWrapper.AuctionItemRequestValidatorWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuctionItemRequestValidator implements AuctionItemRequestValidatorWrapper {

    @Autowired
    private AuctionItemValidator auctionItemRequestValidator;

    @Autowired
    private StartAuctionValidator startAuctionValidator;

    @Autowired
    private AuctionItemUpdateRequestValidator auctionItemUpdateRequestValidator;

    @Autowired
    private BidRequestValidator bidRequestValidator;

    @Autowired
    private SearchAuctionItemValidator searchAuctionItemValidator;

    @Override
    public void validate(AddAuctionItemRequestDto addAuctionItemRequestDto) throws ValidationException {
        auctionItemRequestValidator.validate(addAuctionItemRequestDto);
    }

    @Override
    public void validate(StartAuctionDto startAuctionDto) throws ValidationException {
        startAuctionValidator.validate(startAuctionDto);
    }

    @Override
    public void validate(AuctionItemUpdateRequestDto auctionItemUpdateRequestDto) throws ValidationException {
        auctionItemUpdateRequestValidator.validate(auctionItemUpdateRequestDto);
    }

    @Override
    public void validate(BidRequestDto bidRequestDto) throws ValidationException {
        bidRequestValidator.validate(bidRequestDto);
    }

    @Override
    public void validate(SearchAuctionItemDto searchAuctionItemDto) throws ValidationException {
        searchAuctionItemValidator.validate(searchAuctionItemDto);
    }

}
