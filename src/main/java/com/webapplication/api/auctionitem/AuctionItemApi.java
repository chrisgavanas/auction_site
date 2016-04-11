package com.webapplication.api.auctionitem;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.webapplication.dto.auctionitem.AddAuctionItemRequestDto;
import com.webapplication.dto.auctionitem.AddAuctionItemResponseDto;

@RestController
@RequestMapping(path = "/api")
public interface AuctionItemApi {

    @RequestMapping(path = "/auctionitem", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    AddAuctionItemResponseDto addItem(AddAuctionItemRequestDto auctionItemRequestDto) throws Exception;

}
