package com.webapplication.api.auctionitem;

import com.webapplication.dto.auctionitem.AddAuctionItemRequestDto;
import com.webapplication.dto.auctionitem.AddAuctionItemResponseDto;
import com.webapplication.dto.auctionitem.AuctionItemResponseDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
public interface AuctionItemApi {

    @RequestMapping(path = "/auctionitem", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    AddAuctionItemResponseDto addAuctionItem(AddAuctionItemRequestDto auctionItemRequestDto) throws Exception;

    @RequestMapping(path = "/auctionitem/user/{userId}", method = RequestMethod.GET, produces = "application/json")
    List<AuctionItemResponseDto> getAuctionItemsOfUser(@PathVariable Integer userId) throws Exception;
}
