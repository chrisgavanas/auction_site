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
import com.webapplication.entity.Message;
import com.webapplication.entity.User;
import com.webapplication.entity.VoteLink;
import com.webapplication.error.auctionitem.AuctionItemError;
import com.webapplication.error.category.CategoryError;
import com.webapplication.exception.NotAuthorizedException;
import com.webapplication.exception.auctionitem.AuctionAlreadyInProgressException;
import com.webapplication.exception.auctionitem.AuctionDurationTooShortException;
import com.webapplication.exception.auctionitem.AuctionExpiredException;
import com.webapplication.exception.auctionitem.AuctionItemNotFoundException;
import com.webapplication.exception.auctionitem.BidException;
import com.webapplication.exception.auctionitem.BuyoutException;
import com.webapplication.exception.auctionitem.ImageNotExistException;
import com.webapplication.exception.category.CategoryNotFoundException;
import com.webapplication.exception.user.UserNotFoundException;
import com.webapplication.mapper.AuctionItemMapper;
import com.webapplication.recommendation.Recommendation;
import com.webapplication.recommendation.SessionRecommendation;
import com.xmlparser.XmlParser;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
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
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Autowired
    private Recommendation recommendation;

    @Autowired
    private SessionRecommendation sessionRecommendation;

    @Value("${minAuctionDurationInHours}")
    private Integer minAuctionDurationInHours;

    @Value("${imagesPath}")
    private String imagesPath;

    @Value("${paginationPageSize}")
    private Integer paginationPageSize;

    @Value("${recommendedNo}")
    private Integer recommendedNo;

    @Override
    public AddAuctionItemResponseDto addAuctionItem(UUID authToken, AddAuctionItemRequestDto auctionItemRequestDto) throws Exception {
        SessionInfo sessionInfo = getActiveSession(authToken);
        validateAuthorization(auctionItemRequestDto.getUserId(), sessionInfo);
        List<String> finalizedImagesNames = finalizeImages(auctionItemRequestDto.getImages());
        auctionItemRequestDto.setImages(finalizedImagesNames);
        AuctionItem auctionItem = auctionItemMapper.addAuctionItemRequestDtoToAuctionItem(auctionItemRequestDto);
        validateUserId(auctionItem.getUserId());
        auctionItemRepository.save(auctionItem);

        return auctionItemMapper.auctionItemToAddAuctionItemResponseDto(auctionItem);
    }

    @Override
    public List<AuctionItemResponseDto> getAuctionItemsOfUserByStatus(HttpServletResponse response, String userId, AuctionStatus status, Integer from, Integer to) throws Exception {
        List<AuctionItem> auctionItems = null;
        Date currentDate = new Date();
        Long totalAuctions;
        switch (status) {
            case ACTIVE:
                auctionItems = auctionItemRepository.findActiveAuctionsOfUser(userId, currentDate, new PageRequest(from / paginationPageSize, to - from + 1));
                totalAuctions = auctionItemRepository.countActiveAuctionsOfUser(userId, currentDate);
                break;
            case PENDING:
                auctionItems = auctionItemRepository.findPendingAuctionsOfUser(userId, new PageRequest(from / paginationPageSize, to - from + 1));
                totalAuctions = auctionItemRepository.countPendingAuctionsOfUser(userId);
                break;
            case INACTIVE:
                auctionItems = auctionItemRepository.findInactiveAuctionsOfUser(userId, currentDate, new PageRequest(from / paginationPageSize, to - from + 1));
                totalAuctions = auctionItemRepository.countInactiveAuctionsOfUser(userId, currentDate);
                break;
            default:
                totalAuctions = 0L;
        }
        response.addHeader("totalAuctions", totalAuctions.toString());

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
    public List<BidResponseDto> getBidsOfAuctionItem(String auctionItemId) throws Exception {

        AuctionItem auctionItem = getAuctionItem(auctionItemId);

        return auctionItemMapper.bidsToBidResponseDtos(auctionItem.getBids());
    }

    @Override
    public List<AuctionItemResponseDto> searchAuctionItem(HttpServletResponse response, Integer from, Integer to, SearchAuctionItemDto searchAuctionItemDto) throws Exception {
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

        Long count = auctionItemRepository.countAuctionsWithCriteria(searchAuctionItemDto.getText(), categoryIdToSearch,
                searchAuctionItemDto.getCountry(), priceFromToSearch, priceToToSearch, sellerId, new Date());
        response.addHeader("totalAuctions", count.toString());

        return auctionItemMapper.auctionItemsToAuctionItemResponseDto(auctionItems);
    }

    @Override
    public void buyout(UUID authToken, String auctionItemId, BuyoutAuctionItemRequestDto buyoutAuctionItemRequestDto) throws Exception {
        SessionInfo sessionInfo = getActiveSession(authToken);
        validateAuthorization(buyoutAuctionItemRequestDto.getBuyerId(), sessionInfo);
        AuctionItem auctionItem = getAuctionItem(auctionItemId);
        buyoutAuctionItem(auctionItem, buyoutAuctionItemRequestDto.getBuyerId());
    }

    @Override
    public List<AuctionItemResponseDto> recommendAuctionItems(UUID authToken, String userId) throws Exception {
        SessionInfo sessionInfo = getActiveSession(authToken);
        validateAuthorization(userId, sessionInfo);
        startRecommendation(userId);
        List<String> recommendedAuctionItemIds = sessionRecommendation.recommend();
        List<AuctionItem> recommendedAuctionItems = auctionItemRepository.findAuctionItemsByIds(recommendedAuctionItemIds);

        return auctionItemMapper.auctionItemsToAuctionItemResponseDto(recommendedAuctionItems);
    }

    @Override
    public byte[] getImage(String imagePath) throws Exception {
        File image = new File(imagePath);
        if (!image.exists())
            throw new ImageNotExistException(AuctionItemError.IMAGE_DOES_NOT_EXIST);

        InputStream is = new FileInputStream(image);
        byte[] imageToByteArrayEncoded = Base64.encodeBase64(IOUtils.toByteArray(is));
        is.close();

        return imageToByteArrayEncoded;
    }

    @Override
    public List<AuctionItemResponseDto> getRandomRecommendedAuctionItems() {
        Integer totalAuctions = (int) auctionItemRepository.count();
        Random rng = new Random();
        Integer random = rng.nextInt(totalAuctions / recommendedNo);
        List<AuctionItem> randomAuctionItems = auctionItemRepository.findActiveAuctions(new Date(),
                new PageRequest(random, recommendedNo));

        return auctionItemMapper.auctionItemsToAuctionItemResponseDto(randomAuctionItems);
    }

    private List<String> finalizeImages(List<String> images) {
        return images.stream().map(image -> {
            File oldName = new File(image);
            String[] imagePathParts = image.split("\\\\");
            String newName = "";
            for (int i = 0; i < imagePathParts.length - 1; i++)
                newName += imagePathParts[i] + "/";
            File newFileName = new File(newName + "FINALIZED_AUCTION_" + imagePathParts[imagePathParts.length - 1]);
            oldName.renameTo(newFileName);
            return newFileName.getPath();
        }).collect(Collectors.toList());
    }

    private void startRecommendation(String userId) {
        Map<String, Set<String>> preferredAuctionsPerUser = recommendation.getPreferredAuctionsPerUser();
        Map<String, Integer> bidsOrBuyoutPerAuction = recommendation.getBidsOrBuyoutPerAuction();
        LocalDateTime lastRun = recommendation.getLastRun();
        sessionRecommendation.recommendItems(preferredAuctionsPerUser, bidsOrBuyoutPerAuction, lastRun, userId);
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

        User buyer = validateUserId(buyerId);
        auctionItem.setBuyerId(buyerId);
        auctionItem.setEndDate(new Date());
        sendAutomaticMessage(auctionItem, buyerId, auctionItem.getUserId(), auctionItem.getBuyout());
        auctionItemRepository.save(auctionItem);
    }

    public void sendAutomaticMessage(AuctionItem auctionItem, String buyerId, String sellerId, Double price) throws Exception {
        User buyer = validateUserId(buyerId);
        User seller = validateUserId(sellerId);
        Message messageToBuyer = new Message();
        messageToBuyer.setMessageId(ObjectId.get().toString());
        messageToBuyer.setSubject("Auction Completed");
        messageToBuyer.setMessage("You have successfully purchased item '" + auctionItem.getName() + "' from '"
                + seller.getUsername() + "' at the price of: $" + price);
        messageToBuyer.setDate(new Date());
        messageToBuyer.setFrom("");
        messageToBuyer.setTo(buyer.getUsername());
        messageToBuyer.setSeen(false);
        buyer.getReceivedMessages().add(0, messageToBuyer);
        VoteLink voteSeller = new VoteLink(auctionItem.getAuctionItemId(), buyerId, sellerId, true);
        messageToBuyer.setVoteLink(voteSeller);

        Message messageToSeller = new Message();
        messageToSeller.setMessageId(ObjectId.get().toString());
        messageToSeller.setSubject("Auction Completed");
        messageToSeller.setMessage("Your item '" + auctionItem.getName() + "' has been purchased by '" + buyer.getUsername()
                + "' at the price of: $" + price);
        messageToSeller.setDate(new Date());
        messageToSeller.setFrom("");
        messageToSeller.setTo(seller.getUsername());
        messageToSeller.setSeen(false);
        seller.getReceivedMessages().add(0, messageToSeller);
        VoteLink voteBuyer = new VoteLink(auctionItem.getAuctionItemId(), sellerId, buyerId, false);
        messageToSeller.setVoteLink(voteBuyer);

        userRepository.save(Arrays.asList(new User[]{buyer, seller}));
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
        auctionItem.getBids().add(0, newBid);
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

    private User validateUserId(String userId) throws Exception {
        User user = userRepository.findUserByUserId(userId);
        Optional.ofNullable(user).orElseThrow(() -> new UserNotFoundException(AuctionItemError.USER_NOT_FOUND));
        return user;
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
