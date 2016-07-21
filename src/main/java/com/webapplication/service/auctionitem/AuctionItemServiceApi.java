package com.webapplication.service.auctionitem;

import com.webapplication.dto.auctionitem.AddAuctionItemRequestDto;
import com.webapplication.dto.auctionitem.AddAuctionItemResponseDto;
import com.webapplication.dto.auctionitem.AuctionItemResponseDto;

import java.util.List;

public interface AuctionItemServiceApi {

    AddAuctionItemResponseDto addAuctionItem(AddAuctionItemRequestDto auctionItemRequestDto) throws Exception;

    List<AuctionItemResponseDto> getAuctionItemsOfUser(String userId) throws Exception;

}
