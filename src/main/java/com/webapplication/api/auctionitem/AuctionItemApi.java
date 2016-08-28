package com.webapplication.api.auctionitem;

import com.webapplication.dto.auctionitem.AddAuctionItemRequestDto;
import com.webapplication.dto.auctionitem.AddAuctionItemResponseDto;
import com.webapplication.dto.auctionitem.AuctionItemBidResponseDto;
import com.webapplication.dto.auctionitem.AuctionItemResponseDto;
import com.webapplication.dto.auctionitem.AuctionItemUpdateRequestDto;
import com.webapplication.dto.auctionitem.BidRequestDto;
import com.webapplication.dto.auctionitem.BidResponseDto;
import com.webapplication.dto.auctionitem.StartAuctionDto;
import com.webapplication.dto.auctionitem.AuctionStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping(path = "/api")
public interface AuctionItemApi {

    @RequestMapping(path = "/auctionitem", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    AddAuctionItemResponseDto addAuctionItem(AddAuctionItemRequestDto auctionItemRequestDto) throws Exception;

    @RequestMapping(path = "/auctionitem/user/{userId}", method = RequestMethod.GET, produces = "application/json")
    List<AuctionItemResponseDto> getAuctionItemsOfUserByStatus(@PathVariable String userId, @RequestParam("status") AuctionStatus status) throws Exception;

    @RequestMapping(path = "/auctionitem-as-xml", method = RequestMethod.GET)
    void exportAuctionsAsXmlFile(HttpServletResponse response) throws Exception;

    @RequestMapping(path = "/auctionitem/{auctionItemId}", method = RequestMethod.GET, produces = "application/json")
    AuctionItemResponseDto getAuctionItemById(@PathVariable String auctionItemId) throws Exception;

    @RequestMapping(path = "/auctionitem/{auctionItemId}/start", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    AuctionItemResponseDto startAuction(@PathVariable String auctionItemId, StartAuctionDto startAuctionDto) throws Exception;

    @RequestMapping(path = "/auctionitem/{auctionItemId}", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    AuctionItemResponseDto updateAuctionItem(@PathVariable String auctionItemId, AuctionItemUpdateRequestDto auctionItemUpdateRequestDto) throws Exception;

    @RequestMapping(path = "/auctionitem/{from}-{to}", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
    List<AuctionItemResponseDto> getActiveAuctionItems(@PathVariable Integer from, @PathVariable Integer to) throws Exception;

    @RequestMapping(path = "/auctionitem/user/{userId}/upload", method = RequestMethod.POST, produces = "text/plain")
    String uploadPhoto(@RequestParam("file") MultipartFile file, @PathVariable String userId) throws Exception;

    @RequestMapping(path = "/auctionitem/{auctionItemId}/bid", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    AuctionItemBidResponseDto bidAuctionItem(@PathVariable String auctionItemId, BidRequestDto bidRequestDto) throws Exception;

    @RequestMapping(path = "/auctionitem/{auctionItemId}/bids", method = RequestMethod.GET, produces = "application/json")
    List<BidResponseDto> getBidsOfAuctionItem(@PathVariable String auctionItemId) throws Exception;

}
