package com.webapplication.api.auctionitem;

import com.google.common.base.Strings;
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
import com.webapplication.error.auctionitem.AuctionItemError;
import com.webapplication.exception.NotAuthorizedException;
import com.webapplication.exception.ValidationException;
import com.webapplication.exception.auctionitem.AuctionAlreadyInProgressException;
import com.webapplication.exception.auctionitem.AuctionDurationTooShortException;
import com.webapplication.exception.auctionitem.AuctionExpiredException;
import com.webapplication.exception.auctionitem.AuctionItemNotFoundException;
import com.webapplication.exception.auctionitem.BidException;
import com.webapplication.exception.auctionitem.BuyoutException;
import com.webapplication.exception.auctionitem.ImageNotExistException;
import com.webapplication.exception.auctionitem.InvalidAuctionException;
import com.webapplication.exception.category.CategoryHierarchyException;
import com.webapplication.exception.category.CategoryNotFoundException;
import com.webapplication.exception.user.UserNotFoundException;
import com.webapplication.service.auctionitem.AuctionItemServiceApi;
import com.webapplication.validator.auctionitem.AuctionItemRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class AuctionItemApiImpl implements AuctionItemApi {

    @Autowired
    private AuctionItemServiceApi auctionItemService;

    @Autowired
    private AuctionItemRequestValidator auctionItemRequestValidator;

    @Override
    public AddAuctionItemResponseDto addAuctionItem(@RequestHeader UUID authToken, @RequestBody AddAuctionItemRequestDto auctionItemRequestDto) throws Exception {
        Optional.ofNullable(authToken).orElseThrow(() -> new ValidationException(AuctionItemError.MISSING_DATA));
        auctionItemRequestValidator.validate(auctionItemRequestDto);

        return auctionItemService.addAuctionItem(authToken, auctionItemRequestDto);
    }

    @Override
    public List<AuctionItemResponseDto> getAuctionItemsOfUserByStatus(HttpServletResponse response, @PathVariable String userId, @RequestParam("status") AuctionStatus status, @PathVariable Integer from, @PathVariable Integer to) throws Exception {
        Optional.ofNullable(userId).orElseThrow(() -> new ValidationException(AuctionItemError.MISSING_DATA));
        Optional.ofNullable(status).orElseThrow(() -> new ValidationException(AuctionItemError.MISSING_DATA));
        validatePaginationValues(from, to);
        if (userId.isEmpty())
            throw new ValidationException(AuctionItemError.INVALID_DATA);

        return auctionItemService.getAuctionItemsOfUserByStatus(response, userId, status, from, to);
    }

    @Override
    public void exportAuctionsAsXmlFile(@RequestHeader UUID authToken, HttpServletResponse response) throws Exception {
        Optional.ofNullable(authToken).orElseThrow(() -> new ValidationException(AuctionItemError.MISSING_DATA));
        auctionItemService.exportAuctionsAsXmlFile(authToken, response);
    }

    @Override
    public AuctionItemResponseDto getAuctionItemById(@PathVariable String auctionItemId) throws Exception {
        Optional.ofNullable(auctionItemId).orElseThrow(() -> new ValidationException(AuctionItemError.MISSING_DATA));
        if (auctionItemId.isEmpty())
            throw new ValidationException(AuctionItemError.INVALID_DATA);

        return auctionItemService.getAuctionItemById(auctionItemId);
    }

    @Override
    public AuctionItemResponseDto startAuction(@RequestHeader UUID authToken, @PathVariable String auctionItemId, @RequestBody StartAuctionDto startAuctionDto) throws Exception {
        Optional.ofNullable(auctionItemId).orElseThrow(() -> new ValidationException(AuctionItemError.MISSING_DATA));
        Optional.ofNullable(authToken).orElseThrow(() -> new ValidationException(AuctionItemError.MISSING_DATA));
        auctionItemRequestValidator.validate(startAuctionDto);
        if (auctionItemId.isEmpty())
            throw new ValidationException(AuctionItemError.INVALID_DATA);

        return auctionItemService.startAuction(authToken, auctionItemId, startAuctionDto);
    }

    @Override
    public AuctionItemResponseDto updateAuctionItem(@RequestHeader UUID authToken, @PathVariable String auctionItemId, @RequestBody AuctionItemUpdateRequestDto auctionItemUpdateRequestDto) throws Exception {
        Optional.ofNullable(auctionItemId).orElseThrow(() -> new ValidationException(AuctionItemError.MISSING_DATA));
        auctionItemRequestValidator.validate(auctionItemUpdateRequestDto);

        return auctionItemService.updateAuctionItem(authToken, auctionItemId, auctionItemUpdateRequestDto);
    }

    @Override
    public List<AuctionItemResponseDto> getActiveAuctionItems(@PathVariable Integer from, @PathVariable Integer to) throws Exception {
        validatePaginationValues(from, to);

        return auctionItemService.getActiveAuctionItems(from, to);
    }

    @Override
    public String uploadPhoto(@RequestHeader UUID authToken, @RequestParam("file") MultipartFile file, @PathVariable String userId) throws Exception {
        Optional.ofNullable(authToken).orElseThrow(() -> new ValidationException(AuctionItemError.MISSING_DATA));
        Optional.ofNullable(file).orElseThrow(() -> new ValidationException(AuctionItemError.MISSING_DATA));
        Optional.ofNullable(userId).orElseThrow(() -> new ValidationException(AuctionItemError.MISSING_DATA));
        if (userId.isEmpty() || file.isEmpty())
            throw new ValidationException(AuctionItemError.INVALID_DATA);

        return auctionItemService.uploadPhoto(authToken, file, userId);
    }

    @Override
    public AuctionItemBidResponseDto bidAuctionItem(@RequestHeader UUID authToken, @PathVariable String auctionItemId, @RequestBody BidRequestDto bidRequestDto) throws Exception {
        Optional.ofNullable(authToken).orElseThrow(() -> new ValidationException(AuctionItemError.MISSING_DATA));
        Optional.ofNullable(auctionItemId).orElseThrow(() -> new ValidationException(AuctionItemError.MISSING_DATA));
        auctionItemRequestValidator.validate(bidRequestDto);

        return auctionItemService.bidAuctionItem(authToken, auctionItemId, bidRequestDto);
    }

    @Override
    public List<BidResponseDto> getBidsOfAuctionItem(@PathVariable String auctionItemId) throws Exception {

        Optional.ofNullable(auctionItemId).orElseThrow(() -> new ValidationException(AuctionItemError.MISSING_DATA));

        return auctionItemService.getBidsOfAuctionItem(auctionItemId);
    }

    @Override
    public List<AuctionItemResponseDto> searchAuctionItem(HttpServletResponse response, @PathVariable Integer from, @PathVariable Integer to, @RequestBody SearchAuctionItemDto searchAuctionItemDto) throws Exception {
        validatePaginationValues(from, to);
        auctionItemRequestValidator.validate(searchAuctionItemDto);

        return auctionItemService.searchAuctionItem(response, from, to, searchAuctionItemDto);
    }

    @Override
    public void buyout(@RequestHeader UUID authToken, @PathVariable String auctionItemId, @RequestBody BuyoutAuctionItemRequestDto buyoutAuctionItemRequestDto) throws Exception {
        Optional.ofNullable(authToken).orElseThrow(() -> new ValidationException(AuctionItemError.MISSING_DATA));
        Optional.ofNullable(auctionItemId).orElseThrow(() -> new ValidationException(AuctionItemError.MISSING_DATA));
        Optional.ofNullable(buyoutAuctionItemRequestDto).orElseThrow(() -> new ValidationException(AuctionItemError.MISSING_DATA));
        Optional.ofNullable(buyoutAuctionItemRequestDto.getBuyerId()).orElseThrow(() -> new ValidationException(AuctionItemError.MISSING_DATA));
        if (Strings.isNullOrEmpty(buyoutAuctionItemRequestDto.getBuyerId()))
            throw new ValidationException(AuctionItemError.INVALID_DATA);

        auctionItemService.buyout(authToken, auctionItemId, buyoutAuctionItemRequestDto);
    }

    @Override
    public List<AuctionItemResponseDto> recommendAuctionItems(@RequestHeader UUID authToken, @RequestBody String userId) throws Exception {
        Optional.ofNullable(authToken).orElseThrow(() -> new ValidationException(AuctionItemError.MISSING_DATA));
        Optional.ofNullable(userId).orElseThrow(() -> new ValidationException(AuctionItemError.MISSING_DATA));

        return auctionItemService.recommendAuctionItems(authToken, userId);
    }

    @Override
    public byte[] getImage(@RequestParam("imagePath") String imagePath) throws Exception {
        Optional.ofNullable(imagePath).orElseThrow(() -> new ValidationException(AuctionItemError.MISSING_DATA));

        return auctionItemService.getImage(imagePath);
    }

    @Override
    public List<AuctionItemResponseDto> getRandomRecommendedAuctionItems() throws Exception {
        return auctionItemService.getRandomRecommendedAuctionItems();
    }

    private void validatePaginationValues(Integer from, Integer to) throws Exception {
        Optional.ofNullable(from).orElseThrow(() -> new ValidationException(AuctionItemError.MISSING_DATA));
        Optional.ofNullable(to).orElseThrow(() -> new ValidationException(AuctionItemError.MISSING_DATA));
        if (from <= 0 || to <= 0)
            throw new ValidationException(AuctionItemError.INVALID_DATA);
        if (from > to)
            throw new ValidationException(AuctionItemError.INVALID_PAGINATION_VALUES);
    }

    @ExceptionHandler(ValidationException.class)
    private void invalidData(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(NotAuthorizedException.class)
    private void unauthorized(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.FORBIDDEN.value());
    }

    @ExceptionHandler({UserNotFoundException.class, CategoryNotFoundException.class, IOException.class,
            AuctionItemNotFoundException.class, ImageNotExistException.class})
    private void userNotFound(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler({AuctionAlreadyInProgressException.class, AuctionDurationTooShortException.class, CategoryHierarchyException.class,
            InvalidAuctionException.class, BidException.class, AuctionExpiredException.class, BuyoutException.class})
    private void startAuctionError(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.CONFLICT.value());
    }

    @ExceptionHandler(Exception.class)
    private void genericError(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

}
