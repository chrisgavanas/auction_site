package com.webapplication.service.auctionitem;

import com.webapplication.authentication.Authenticator;
import com.webapplication.dao.AuctionItemRepository;
import com.webapplication.dao.CategoryRepository;
import com.webapplication.dao.UserRepository;
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
import com.webapplication.dto.user.SessionInfo;
import com.webapplication.entity.AuctionItem;
import com.webapplication.entity.Bid;
import com.webapplication.entity.Category;
import com.webapplication.entity.User;
import com.webapplication.error.auctionitem.AuctionItemError;
import com.webapplication.error.category.CategoryError;
import com.webapplication.exception.NotAuthorizedException;
import com.webapplication.exception.auctionitem.AuctionAlreadyInProgressException;
import com.webapplication.exception.auctionitem.AuctionDurationTooShortException;
import com.webapplication.exception.auctionitem.AuctionExpiredException;
import com.webapplication.exception.auctionitem.AuctionItemNotFoundException;
import com.webapplication.exception.auctionitem.BidException;
import com.webapplication.exception.auctionitem.BuyoutException;
import com.webapplication.exception.category.CategoryNotFoundException;
import com.webapplication.exception.user.UserNotFoundException;
import com.webapplication.mapper.AuctionItemMapper;
import com.xmlparser.XmlParser;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Transactional
@Service
public class AuctionItemServiceApiImpl implements AuctionItemServiceApi {

    @Autowired
    private AuctionItemRepository auctionItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AuctionItemMapper auctionItemMapper;

    @Autowired
    private Authenticator authenticator;

    @Autowired
    private XmlParser xmlParser;

    private Random random;

    @Value("${minAuctionDurationInHours}")
    private Integer minAuctionDurationInHours;

    @Value("${imagesPath}")
    private String imagesPath;

    @Value("${paginationPageSize}")
    private Integer paginationPageSize;

    @Override
    public AddAuctionItemResponseDto addAuctionItem(UUID authToken, AddAuctionItemRequestDto auctionItemRequestDto) throws Exception {
        SessionInfo sessionInfo = getActiveSession(authToken);
        validateAuthorization(auctionItemRequestDto.getUserId(), sessionInfo);
        AuctionItem auctionItem = auctionItemMapper.addAuctionItemRequestDtoToAuctionItem(auctionItemRequestDto);
        validateUserId(auctionItem.getUserId());
        auctionItemRepository.save(auctionItem);

        return auctionItemMapper.auctionItemToAddAuctionItemResponseDto(auctionItem);
    }

    @Override
    public List<AuctionItemResponseDto> getAuctionItemsOfUserByStatus(String userId, AuctionStatus status, Integer from, Integer to) throws Exception {
        List<AuctionItem> auctionItems = null;
        switch (status) {
            case ACTIVE:
                auctionItems = auctionItemRepository.findActiveAuctionsOfUser(userId, new Date(), new PageRequest(from / paginationPageSize, to - from + 1));
                break;
            case PENDING:
                auctionItems = auctionItemRepository.findPendingAuctionsOfUser(userId, new PageRequest(from / paginationPageSize, to - from + 1));
                break;
            case INACTIVE:
                auctionItems = auctionItemRepository.findInactiveAuctionsOfUser(userId, new Date(), new PageRequest(from / paginationPageSize, to - from + 1));
                break;
        }

        return auctionItemMapper.auctionItemsToAuctionItemResponseDto(auctionItems);
    }

    @Override
    public List<AuctionItemResponseDto> getActiveAuctionItems(Integer from, Integer to) throws Exception {
        List<AuctionItem> auctionItems = auctionItemRepository.findActiveAuctions(new Date(), new PageRequest(from / paginationPageSize, to - from + 1));

        return auctionItemMapper.auctionItemsToAuctionItemResponseDto(auctionItems);
    }

    @Override
    public void exportAuctionsAsXmlFile(UUID authToken, HttpServletResponse response) throws Exception {
        SessionInfo sessionInfo = getActiveSession(authToken);
        validateAuthorization(sessionInfo);
        xmlParser.marshall();
        File xmlFile = new File("auctions.xml");
        InputStream stream = new FileInputStream(xmlFile);
        response.addHeader("Content-disposition", "attachment;filename=auctions.xml");
        response.setContentType("xml");
        IOUtils.copy(stream, response.getOutputStream());
        stream.close();
        xmlFile.delete();
        response.flushBuffer();
    }

    @Override
    public AuctionItemResponseDto getAuctionItemById(String auctionItemId) throws Exception {
        AuctionItem auctionItem = getAuctionItem(auctionItemId);

        return auctionItemMapper.auctionItemToAuctionItemResponseDto(auctionItem);
    }

    @Override
    public AuctionItemResponseDto startAuction(UUID authToken, String auctionItemId, StartAuctionDto startAuctionDto) throws Exception {
        SessionInfo sessionInfo = getActiveSession(authToken);
        ;
        AuctionItem auctionItem = getAuctionItem(auctionItemId);
        if (auctionItem.getStartDate() != null)
            throw new AuctionAlreadyInProgressException(AuctionItemError.AUCTION_ALREADY_IN_PROGRESS);
        validateAuthorization(auctionItem.getUserId(), sessionInfo);

        Date startDate = validateDates(startAuctionDto.getEndDate());
        auctionItemMapper.update(auctionItem, startDate, startAuctionDto.getEndDate());
        auctionItemRepository.save(auctionItem);

        return auctionItemMapper.auctionItemToAuctionItemResponseDto(auctionItem);
    }

    @Override
    public AuctionItemResponseDto updateAuctionItem(UUID authToken, String auctionItemId, AuctionItemUpdateRequestDto auctionItemUpdateRequestDto) throws Exception {
        SessionInfo sessionInfo = getActiveSession(authToken);
        AuctionItem auctionItem = getAuctionItem(auctionItemId);
        validateAuthorization(auctionItem.getUserId(), sessionInfo);
        validateEditing(auctionItem);
        deletePossibleImages(auctionItem, auctionItemUpdateRequestDto.getImages());
        auctionItemMapper.update(auctionItem, auctionItemUpdateRequestDto);
        auctionItemRepository.save(auctionItem);

        return auctionItemMapper.auctionItemToAuctionItemResponseDto(auctionItem);
    }

    @Override
    public String uploadPhoto(UUID authToken, MultipartFile file, String userId) throws Exception {
        SessionInfo sessionInfo = getActiveSession(authToken);
        validateAuthorization(userId, sessionInfo);
        validateUserId(userId);
        File path = getOrCreatePath(userId);
        File convertedFile = convert(file);
        File storedImage = storeFile(convertedFile, path);

        return storedImage.getPath();
    }

    @Override
    public AuctionItemBidResponseDto bidAuctionItem(UUID authToken, String auctionItemId, BidRequestDto bidRequestDto) throws Exception {
        SessionInfo sessionInfo = getActiveSession(authToken);
        validateAuthorization(bidRequestDto.getUserId(), sessionInfo);
        AuctionItem auctionItem = getAuctionItem(auctionItemId);
        validateUserId(bidRequestDto.getUserId());
        validateBid(auctionItem, bidRequestDto);
        addNewBidToAuctionItem(auctionItem, bidRequestDto);
        auctionItemRepository.save(auctionItem);

        return auctionItemMapper.auctionItemToAuctionItemBidResponseDto(auctionItem);
    }

    @Override
    public List<BidResponseDto> getBidsOfAuctionItem(UUID authToken, String auctionItemId) throws Exception {
        SessionInfo sessionInfo = getActiveSession(authToken);
        AuctionItem auctionItem = getAuctionItem(auctionItemId);
        validateAuthorization(auctionItem.getUserId(), sessionInfo);

        return auctionItemMapper.bidsToBidResponseDtos(auctionItem.getBids());
    }

    @Override
    public List<AuctionItemResponseDto> searchAuctionItem(Integer from, Integer to, SearchAuctionItemDto searchAuctionItemDto) throws Exception {
        validateCategory(searchAuctionItemDto.getCategoryId());
        String categoryId = searchAuctionItemDto.getCategoryId();
        Double priceFrom = searchAuctionItemDto.getPriceFrom();
        Double priceTo = searchAuctionItemDto.getPriceTo();
        String sellerId = searchAuctionItemDto.getSellerId();
        
        String categoryIdToSearch = categoryId.equals("ALL") ? "" : categoryId;
        Double priceFromToSearch = priceFrom == null ? 0 : priceFrom;
        Double priceToToSearch = priceTo == null ? Double.MAX_VALUE : priceTo;
        List<AuctionItem> auctionItems = auctionItemRepository.findAuctionsWithCriteria(searchAuctionItemDto.getText(), categoryIdToSearch,
                searchAuctionItemDto.getCountry(), priceFromToSearch, priceToToSearch, sellerId, new Date(),
                new PageRequest(from / paginationPageSize, to - from + 1, new Sort(Sort.Direction.ASC, "currentBid")));

        return auctionItemMapper.auctionItemsToAuctionItemResponseDto(auctionItems);
    }

    @Override
    public void buyout(UUID authToken, String auctionItemId, BuyoutAuctionItemRequestDto buyoutAuctionItemRequestDto) throws Exception {
        SessionInfo sessionInfo = getActiveSession(authToken);
        validateAuthorization(buyoutAuctionItemRequestDto.getBuyerId(), sessionInfo);
        AuctionItem auctionItem = getAuctionItem(auctionItemId);
        buyoutAuctionItem(auctionItem, buyoutAuctionItemRequestDto.getBuyerId());
    }

    private void validateCategory(String categoryId) throws Exception {
        if (!categoryId.equals("ALL")) {
            Category category = categoryRepository.findCategoryByCategoryId(categoryId);
            Optional.ofNullable(category).orElseThrow(() -> new CategoryNotFoundException(CategoryError.CATEGORY_NOT_FOUND));
        }
    }

    private void buyoutAuctionItem(AuctionItem auctionItem, String buyerId) throws Exception {
        if (auctionItem.getStartDate() == null)
            throw new BuyoutException((AuctionItemError.AUCTION_HAS_NOT_STARTED));
        if (auctionItem.getEndDate() == null || new Date().after(auctionItem.getEndDate()))
            throw new BuyoutException(AuctionItemError.AUCTION_EXPIRED);
        if (auctionItem.getUserId().equals(buyerId))
            throw new BuyoutException(AuctionItemError.INVALID_BUYOUT_FROM_USER);

        auctionItem.setBuyerId(buyerId);
        auctionItem.setEndDate(new Date());
        auctionItemRepository.save(auctionItem);
    }

    private void deletePossibleImages(AuctionItem auctionItem, List<String> editedImages) {
        List<String> images = new LinkedList<>(auctionItem.getImages());
        images.removeAll(editedImages);
        images.forEach(image -> new File(image).delete());
    }

    private void addNewBidToAuctionItem(AuctionItem auctionItem, BidRequestDto bidRequestDto) {
        auctionItem.setBidsNo(auctionItem.getBidsNo() + 1);
        auctionItem.setCurrentBid(bidRequestDto.getAmount());
        Bid newBid = new Bid(bidRequestDto.getAmount(), new Date(), bidRequestDto.getUserId());
        auctionItem.getBids().add(0, newBid);      //TODO check if it's added at start or end
    }

    private void validateBid(AuctionItem auctionItem, BidRequestDto bidRequestDto) throws Exception {
        if (auctionItem.getStartDate() == null)
            throw new BidException(AuctionItemError.AUCTION_HAS_NOT_STARTED);
        if (DateTime.now().minusSeconds(10).toDate().after(auctionItem.getEndDate()))
            throw new BidException(AuctionItemError.AUCTION_HAS_BEEN_COMPLETED);
        if (auctionItem.getUserId().equals(bidRequestDto.getUserId()))
            throw new BidException(AuctionItemError.ITEM_BELONGS_TO_BIDDER);

        Double minBidAmount = getMinBidAmountBasedOnPriceOfAuctionItem(auctionItem.getCurrentBid());
        if (minBidAmount > bidRequestDto.getAmount() - auctionItem.getCurrentBid())
            throw new BidException(AuctionItemError.BID_AMOUNT_BELOW_ALLOWED_AMOUNT);
        if (auctionItem.getBuyout() != null && bidRequestDto.getAmount() > auctionItem.getBuyout())
            throw new BidException(AuctionItemError.BID_AMOUNT_ABOVE_BUYOUT);
    }

    private Double getMinBidAmountBasedOnPriceOfAuctionItem(Double price) throws Exception {
        if (price >= 0.01 && price <= 0.99)
            return 0.05;
        else if (price >= 1.00 && price <= 4.99)
            return 0.25;
        else if (price >= 5.00 && price <= 24.99)
            return 0.5;
        else if (price >= 25.00 && price <= 99.99)
            return 1.00;
        else if (price >= 100.00 & price <= 249.99)
            return 2.50;
        else if (price >= 250.00 && price <= 499.99)
            return 5.00;
        else if (price >= 500.00 && price <= 999.99)
            return 10.00;
        else if (price >= 1000.00 && price <= 2499.99)
            return 25.00;
        else if (price >= 2500.00 && price <= 4999.99)
            return 50.00;
        else if (price >= 5000.00)
            return 100.00;
        else
            throw new Exception("Generic error.");
    }

    private File storeFile(File file, File path) throws Exception {
        File newFile = new File(path.getPath() + "/" + new Random().nextInt() + file.getName());
        FileUtils.copyFile(file, newFile);
        return newFile;
    }

    private File convert(MultipartFile file) throws Exception {
        File convertedFile = new File(file.getOriginalFilename());
        convertedFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convertedFile);
        fos.write(file.getBytes());
        fos.close();
        return convertedFile;
    }

    private synchronized File getOrCreatePath(String userId) {
        File file = new File(imagesPath + "/" + userId);
        if (!file.exists())
            file.mkdir();
        return file;
    }

    private void validateUserId(String userId) throws Exception {
        User user = userRepository.findUserByUserId(userId);
        Optional.ofNullable(user).orElseThrow(() -> new UserNotFoundException(AuctionItemError.USER_NOT_FOUND));
    }

    private Date validateDates(Date endDate) throws Exception {
        Date date = DateTime.now().plusHours(minAuctionDurationInHours).toDate();
        if (date.after(endDate))
            throw new AuctionDurationTooShortException(AuctionItemError.AUCTION_DURATION_TOO_SHORT);
        return date;
    }

    private AuctionItem getAuctionItem(String auctionItemId) throws Exception {
        AuctionItem auctionItem = auctionItemRepository.findAuctionItemByAuctionItemId(auctionItemId);
        Optional.ofNullable(auctionItem).orElseThrow(() -> new AuctionItemNotFoundException(AuctionItemError.AUCTION_ITEM_NOT_FOUND));

        return auctionItem;
    }

    private void validateEditing(AuctionItem auctionItem) throws Exception {
        if (auctionItem.getBidsNo() != 0)
            throw new AuctionAlreadyInProgressException(AuctionItemError.AUCTION_ALREADY_IN_PROGRESS);
        if (auctionItem.getEndDate() != null && new Date().after(auctionItem.getEndDate()))
            throw new AuctionExpiredException(AuctionItemError.AUCTION_EXPIRED);
    }

    private SessionInfo getActiveSession(UUID authToken) throws NotAuthorizedException {
        SessionInfo sessionInfo = authenticator.getSession(authToken);
        Optional.ofNullable(sessionInfo).orElseThrow(() -> new NotAuthorizedException(AuctionItemError.UNAUTHORIZED));

        return sessionInfo;
    }

    private void validateAuthorization(String userId, SessionInfo sessionInfo) throws NotAuthorizedException {
        if (!userId.equals(sessionInfo.getUserId()) && !sessionInfo.getIsAdmin())
            throw new NotAuthorizedException(AuctionItemError.UNAUTHORIZED);
    }

    private void validateAuthorization(SessionInfo sessionInfo) throws NotAuthorizedException {
        if (!sessionInfo.getIsAdmin())
            throw new NotAuthorizedException(AuctionItemError.UNAUTHORIZED);
    }

}
