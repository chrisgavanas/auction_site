package com.webapplication.service.auctionitem;

import com.webapplication.dto.auctionitem.AddAuctionItemRequestDto;
import com.webapplication.dto.auctionitem.AddAuctionItemResponseDto;
import com.webapplication.dto.auctionitem.AuctionItemResponseDto;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface AuctionItemServiceApi {

    AddAuctionItemResponseDto addAuctionItem(AddAuctionItemRequestDto auctionItemRequestDto) throws Exception;

    List<AuctionItemResponseDto> getAuctionItemsOfUser(String userId) throws Exception;

    void exportAuctionsAsXmlFile(HttpServletResponse response) throws Exception;

}
