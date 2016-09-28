package com.webapplication.service.auctionitem;

import com.webapplication.dto.auctionitem.AddAuctionItemRequestDto;
import com.webapplication.dto.auctionitem.AddAuctionItemResponseDto;
import com.webapplication.dto.auctionitem.AuctionItemBidResponseDto;
import com.webapplication.dto.auctionitem.AuctionItemResponseDto;
import com.webapplication.dto.auctionitem.AuctionItemUpdateRequestDto;
import com.webapplication.dto.auctionitem.AuctionStatus;
import com.webapplication.dto.auctionitem.BidRequestDto;
import com.webapplication.dto.auctionitem.BidResponseDto;
import com.webapplication.dto.auctionitem.BuyoutAuctionItemRequestDto;
import com.webapplication.dto.auctionitem.SearchAuctionItemDto;
import com.webapplication.dto.auctionitem.StartAuctionDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

public interface AuctionItemServiceApi {

    AddAuctionItemResponseDto addAuctionItem(UUID authToken, AddAuctionItemRequestDto auctionItemRequestDto) throws Exception;

    List<AuctionItemResponseDto> getAuctionItemsOfUserByStatus(String userId, AuctionStatus status, Integer from, Integer to) throws Exception;

    void exportAuctionsAsXmlFile(UUID authToken, HttpServletResponse response) throws Exception;

    AuctionItemResponseDto getAuctionItemById(String auctionItemId) throws Exception;

    AuctionItemResponseDto startAuction(UUID authToken, String auctionItemId, StartAuctionDto startAuctionDto) throws Exception;

    AuctionItemResponseDto updateAuctionItem(UUID authToken, String auctionItemId, AuctionItemUpdateRequestDto auctionItemUpdateRequestDto) throws Exception;

    List<AuctionItemResponseDto> getActiveAuctionItems(Integer from, Integer to) throws Exception;

    String uploadPhoto(UUID authToken, MultipartFile file, String userId) throws Exception;

    AuctionItemBidResponseDto bidAuctionItem(UUID authToken, String auctionItemId, BidRequestDto bidRequestDto) throws Exception;

    List<BidResponseDto> getBidsOfAuctionItem(String auctionItemId) throws Exception;

    List<AuctionItemResponseDto> searchAuctionItem(Integer from, Integer to, SearchAuctionItemDto searchAuctionItemDto) throws Exception;

    void buyout(UUID authToken, String auctionItemId, BuyoutAuctionItemRequestDto buyoutAuctionItemRequestDto) throws Exception;

    List<AuctionItemResponseDto> recommendAuctionItems(UUID authToken, String userId) throws Exception;

    byte[] getImage(String imagePath) throws Exception;

}
