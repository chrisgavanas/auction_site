package com.webapplication.api.auctionitem;

import com.webapplication.dto.auctionitem.AddAuctionItemRequestDto;
import com.webapplication.dto.auctionitem.AddAuctionItemResponseDto;
import com.webapplication.dto.auctionitem.AuctionItemResponseDto;
import com.webapplication.dto.auctionitem.StartAuctionDto;
import com.webapplication.dto.auctionitem.Status;
import com.webapplication.error.auctionitem.AuctionItemError;
import com.webapplication.exception.AuctionAlreadyInProgressException;
import com.webapplication.exception.AuctionDurationTooShortException;
import com.webapplication.exception.AuctionItemNotFoundException;
import com.webapplication.exception.CategoryNotFoundException;
import com.webapplication.exception.UserNotFoundException;
import com.webapplication.exception.ValidationException;
import com.webapplication.service.auctionitem.AuctionItemServiceApi;
import com.webapplication.validator.auctionitem.AuctionItemRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class AuctionItemApiImpl implements AuctionItemApi {

    @Autowired
    private AuctionItemServiceApi auctionItemService;

    @Autowired
    private AuctionItemRequestValidator auctionItemRequestValidator;

    @Override
    public AddAuctionItemResponseDto addAuctionItem(@RequestBody AddAuctionItemRequestDto auctionItemRequestDto) throws Exception {
        auctionItemRequestValidator.validate(auctionItemRequestDto);
        return auctionItemService.addAuctionItem(auctionItemRequestDto);
    }

    @Override
    public List<AuctionItemResponseDto> getAuctionItemsOfUserByStatus(@PathVariable String userId, @RequestParam("status") Status status) throws Exception {
        Optional.ofNullable(userId).orElseThrow(() -> new ValidationException(AuctionItemError.MISSING_DATA));
        Optional.ofNullable(status).orElseThrow(() -> new ValidationException(AuctionItemError.MISSING_DATA));
        if (userId.isEmpty())
            throw new ValidationException(AuctionItemError.INVALID_DATA);

        return auctionItemService.getAuctionItemsOfUserByStatus(userId, status);
    }

    @Override
    public void exportAuctionsAsXmlFile(HttpServletResponse response) throws Exception {
        auctionItemService.exportAuctionsAsXmlFile(response);
    }

    @Override
    public AuctionItemResponseDto getAuctionItemById(@PathVariable String auctionItemId) throws Exception {
        Optional.ofNullable(auctionItemId).orElseThrow(() -> new ValidationException(AuctionItemError.MISSING_DATA));
        if (auctionItemId.isEmpty())
            throw new ValidationException(AuctionItemError.INVALID_DATA);

        return auctionItemService.getAuctionItemById(auctionItemId);
    }

    @Override
    public AuctionItemResponseDto startAuction(@PathVariable String auctionItemId, @RequestPart StartAuctionDto startAuctionDto) throws Exception {
        auctionItemRequestValidator.validate(startAuctionDto);
        Optional.ofNullable(auctionItemId).orElseThrow(() -> new ValidationException(AuctionItemError.MISSING_DATA));
        if (auctionItemId.isEmpty())
            throw new ValidationException(AuctionItemError.INVALID_DATA);

        return auctionItemService.startAuction(auctionItemId, startAuctionDto);
    }

    @ExceptionHandler(ValidationException.class)
    private void invalidData(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler({UserNotFoundException.class, CategoryNotFoundException.class, IOException.class, AuctionItemNotFoundException.class})
    private void userNotFound(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler({AuctionAlreadyInProgressException.class, AuctionDurationTooShortException.class})
    private void startAuctionError(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.CONFLICT.value());
    }

    @ExceptionHandler(Exception.class)
    private void genericError(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

}
