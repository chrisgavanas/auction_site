package com.webapplication.api.auctionitem;

import com.webapplication.dto.auctionitem.AddAuctionItemRequestDto;
import com.webapplication.dto.auctionitem.AddAuctionItemResponseDto;
import com.webapplication.dto.auctionitem.AuctionItemBidResponseDto;
import com.webapplication.dto.auctionitem.AuctionItemResponseDto;
import com.webapplication.dto.auctionitem.AuctionItemUpdateRequestDto;
import com.webapplication.dto.auctionitem.AuctionStatus;
import com.webapplication.dto.auctionitem.BidRequestDto;
import com.webapplication.dto.auctionitem.BidResponseDto;
import com.webapplication.dto.auctionitem.StartAuctionDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api")
public interface AuctionItemApi {

    @RequestMapping(path = "/auctionitem", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    AddAuctionItemResponseDto addAuctionItem(@RequestHeader UUID authToken, AddAuctionItemRequestDto auctionItemRequestDto) throws Exception;

    @RequestMapping(path = "/auctionitem/user/{userId}/{from}-{to}", method = RequestMethod.GET, produces = "application/json")
    List<AuctionItemResponseDto> getAuctionItemsOfUserByStatus(@PathVariable String userId, @RequestParam("status") AuctionStatus status, @PathVariable Integer from, @PathVariable Integer to) throws Exception;

    @RequestMapping(path = "/auctionitem-as-xml", method = RequestMethod.GET)
    void exportAuctionsAsXmlFile(@RequestHeader UUID authToken, HttpServletResponse response) throws Exception;

    @RequestMapping(path = "/auctionitem/{auctionItemId}", method = RequestMethod.GET, produces = "application/json")
    AuctionItemResponseDto getAuctionItemById(@PathVariable String auctionItemId) throws Exception;

    @RequestMapping(path = "/auctionitem/{auctionItemId}/start", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    AuctionItemResponseDto startAuction(@RequestHeader UUID authToken, @PathVariable String auctionItemId, StartAuctionDto startAuctionDto) throws Exception;

    @RequestMapping(path = "/auctionitem/{auctionItemId}", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    AuctionItemResponseDto updateAuctionItem(@RequestHeader UUID authToken, @PathVariable String auctionItemId, AuctionItemUpdateRequestDto auctionItemUpdateRequestDto) throws Exception;

    @RequestMapping(path = "/auctionitem/{from}-{to}", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
    List<AuctionItemResponseDto> getActiveAuctionItems(@PathVariable Integer from, @PathVariable Integer to) throws Exception;

    @RequestMapping(path = "/auctionitem/user/{userId}/upload", method = RequestMethod.POST, produces = "text/plain")
    String uploadPhoto(@RequestHeader UUID authToken, @RequestParam("file") MultipartFile file, @PathVariable String userId) throws Exception;

    @RequestMapping(path = "/auctionitem/{auctionItemId}/bid", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    AuctionItemBidResponseDto bidAuctionItem(@RequestHeader UUID authToken, @PathVariable String auctionItemId, BidRequestDto bidRequestDto) throws Exception;

    @RequestMapping(path = "/auctionitem/{auctionItemId}/bids", method = RequestMethod.GET, produces = "application/json")
    List<BidResponseDto> getBidsOfAuctionItem(@RequestHeader UUID authToken, @PathVariable String auctionItemId) throws Exception;

}
