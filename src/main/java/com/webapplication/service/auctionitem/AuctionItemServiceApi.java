package com.webapplication.service.auctionitem;

import com.webapplication.dto.auctionitem.AddAuctionItemRequestDto;
import com.webapplication.dto.auctionitem.AddAuctionItemResponseDto;

public interface AuctionItemServiceApi {

    AddAuctionItemResponseDto addItem(AddAuctionItemRequestDto auctionItemRequestDto) throws Exception;

}
