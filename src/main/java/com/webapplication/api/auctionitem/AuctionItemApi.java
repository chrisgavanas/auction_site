package com.webapplication.api.auctionitem;

import com.webapplication.dto.auctionitem.AddAuctionItemRequestDto;
import com.webapplication.dto.auctionitem.AddAuctionItemResponseDto;
import com.webapplication.dto.auctionitem.AuctionItemResponseDto;
import com.webapplication.dto.auctionitem.StartAuctionDto;
import com.webapplication.dto.auctionitem.Status;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/api")
public interface AuctionItemApi {

    @RequestMapping(path = "/auctionitem", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    AddAuctionItemResponseDto addAuctionItem(AddAuctionItemRequestDto auctionItemRequestDto) throws Exception;

    @RequestMapping(path = "/auctionitem/user/{userId}", method = RequestMethod.GET, produces = "application/json")
    List<AuctionItemResponseDto> getAuctionItemsOfUserByStatus(@PathVariable String userId, @RequestParam("status") Status status) throws Exception;

    @RequestMapping(path = "/auctionitem-as-xml", method = RequestMethod.GET)
    void exportAuctionsAsXmlFile(HttpServletResponse response) throws Exception;

    @RequestMapping(path = "/auctionitem/{auctionItemId}", method = RequestMethod.GET, produces = "application/json")
    AuctionItemResponseDto getAuctionItemById(@PathVariable String auctionItemId) throws Exception;

    @RequestMapping(path = "/auctionitem/{auctionItemId}/start", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    AuctionItemResponseDto startAuction(@PathVariable String auctionItemId, StartAuctionDto startAuctionDto) throws Exception;

}
