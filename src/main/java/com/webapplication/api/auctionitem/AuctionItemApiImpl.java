package com.webapplication.api.auctionitem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.webapplication.dto.auctionitem.AddAuctionItemRequestDto;
import com.webapplication.dto.auctionitem.AddAuctionItemResponseDto;
import com.webapplication.service.auctionitem.AuctionItemServiceApi;

@Component
public class AuctionItemApiImpl implements AuctionItemApi {

    @Autowired
    private AuctionItemServiceApi auctionItemService;

    public AddAuctionItemResponseDto addItem(@RequestBody AddAuctionItemRequestDto auctionItemRequestDto) throws Exception {
        return auctionItemService.addItem(auctionItemRequestDto);
    }

}
