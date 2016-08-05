package com.webapplication.service.auctionitem;

import com.webapplication.dto.auctionitem.AddAuctionItemRequestDto;
import com.webapplication.dto.auctionitem.AddAuctionItemResponseDto;
import com.webapplication.dto.auctionitem.AuctionItemResponseDto;
import com.webapplication.dto.auctionitem.AuctionItemUpdateRequestDto;
import com.webapplication.dto.auctionitem.StartAuctionDto;
import com.webapplication.dto.auctionitem.Status;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface AuctionItemServiceApi {

    AddAuctionItemResponseDto addAuctionItem(AddAuctionItemRequestDto auctionItemRequestDto) throws Exception;

    List<AuctionItemResponseDto> getAuctionItemsOfUserByStatus(String userId, Status status) throws Exception;

    void exportAuctionsAsXmlFile(HttpServletResponse response) throws Exception;

    AuctionItemResponseDto getAuctionItemById(String auctionItemId) throws Exception;

    AuctionItemResponseDto startAuction(String auctionItemId, StartAuctionDto startAuctionDto) throws Exception;

    AuctionItemResponseDto updateAuctionItem(String auctionItemId, AuctionItemUpdateRequestDto auctionItemUpdateRequestDto) throws Exception;

    List<AuctionItemResponseDto> getActiveAuctionItems(Integer from, Integer to) throws Exception;

}
