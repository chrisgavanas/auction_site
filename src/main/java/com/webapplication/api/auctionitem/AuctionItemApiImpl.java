package com.webapplication.api.auctionitem;

import com.webapplication.dto.auctionitem.AddAuctionItemRequestDto;
import com.webapplication.dto.auctionitem.AddAuctionItemResponseDto;
import com.webapplication.dto.auctionitem.AuctionItemResponseDto;
import com.webapplication.error.auctionitem.AuctionItemError;
import com.webapplication.exception.CategoryNotFoundException;
import com.webapplication.exception.UserNotFoundException;
import com.webapplication.exception.ValidationException;
import com.webapplication.service.auctionitem.AuctionItemServiceApi;
import com.webapplication.validator.auctionitem.AuctionItemValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class AuctionItemApiImpl implements AuctionItemApi {

    @Autowired
    private AuctionItemServiceApi auctionItemService;

    @Autowired
    private AuctionItemValidator auctionItemValidator;

    public AddAuctionItemResponseDto addAuctionItem(@RequestBody AddAuctionItemRequestDto auctionItemRequestDto) throws Exception {
        auctionItemValidator.validate(auctionItemRequestDto);
        return auctionItemService.addAuctionItem(auctionItemRequestDto);
    }

    @Override
    public List<AuctionItemResponseDto> getAuctionItemsOfUser(@PathVariable String userId) throws Exception {
        Optional.ofNullable(userId).orElseThrow(() -> new ValidationException(AuctionItemError.MISSING_DATA));
        if (userId.isEmpty())
            throw new ValidationException(AuctionItemError.INVALID_DATA);

        return auctionItemService.getAuctionItemsOfUser(userId) ;
    }

    @ExceptionHandler(ValidationException.class)
    private void invalidData(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler({UserNotFoundException.class, CategoryNotFoundException.class})
    private void userNotFound(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(Exception.class)
    private void genericError(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

}
