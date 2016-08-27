package com.webapplication.service.auctionitem;

import com.webapplication.dao.AuctionItemRepository;
import com.webapplication.dao.UserRepository;
import com.webapplication.dto.auctionitem.AddAuctionItemRequestDto;
import com.webapplication.dto.auctionitem.AddAuctionItemResponseDto;
import com.webapplication.dto.auctionitem.AuctionItemBidResponseDto;
import com.webapplication.dto.auctionitem.AuctionItemResponseDto;
import com.webapplication.dto.auctionitem.AuctionItemUpdateRequestDto;
import com.webapplication.dto.auctionitem.AuctionStatus;
import com.webapplication.dto.auctionitem.StartAuctionDto;
import com.webapplication.entity.AuctionItem;
import com.webapplication.entity.Bid;
import com.webapplication.entity.User;
import com.webapplication.error.auctionitem.AuctionItemError;
import com.webapplication.exception.AuctionAlreadyInProgressException;
import com.webapplication.exception.AuctionDurationTooShortException;
import com.webapplication.exception.AuctionItemNotFoundException;
import com.webapplication.exception.BidException;
import com.webapplication.exception.UserNotFoundException;
import com.webapplication.mapper.AuctionItemMapper;
import com.xmlparser.XmlParser;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class AuctionItemServiceApiImpl implements AuctionItemServiceApi {

    @Autowired
    private AuctionItemRepository auctionItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuctionItemMapper auctionItemMapper;

    @Autowired
    private XmlParser xmlParser;

    @Value("${minAuctionDurationInHours}")
    private Integer minAuctionDurationInHours;

    @Value("${imagesPath}")
    private String imagesPath;

    @Override
    public AddAuctionItemResponseDto addAuctionItem(AddAuctionItemRequestDto auctionItemRequestDto) throws Exception {
        AuctionItem auctionItem = auctionItemMapper.addAuctionItemRequestDtoToAuctionItem(auctionItemRequestDto);
        validateUserId(auctionItem.getUserId());
        auctionItemRepository.save(auctionItem);

        return auctionItemMapper.auctionItemToAddAuctionItemResponseDto(auctionItem);
    }

    @Override
    public List<AuctionItemResponseDto> getAuctionItemsOfUserByStatus(String userId, AuctionStatus status) throws Exception {
        List<AuctionItem> auctionItems = null;
        switch (status) {
            case ACTIVE:
                auctionItems = auctionItemRepository.findActiveAuctionsOfUser(userId, new Date());
                break;
            case PENDING:
                auctionItems = auctionItemRepository.findPendingAuctionsOfUser(userId);
                break;
            case INACTIVE:
                auctionItems = auctionItemRepository.findInactiveAuctionsOfUser(userId, new Date());
                break;
        }

        return auctionItemMapper.auctionItemsToAuctionItemResponseDto(auctionItems);
    }

    @Override
    public List<AuctionItemResponseDto> getActiveAuctionItems(Integer from, Integer to) throws Exception {
        List<AuctionItem> auctionItems = auctionItemRepository.findActiveAuctions(new Date(), new PageRequest(from - 1, to - from + 1));

        return auctionItemMapper.auctionItemsToAuctionItemResponseDto(auctionItems);
    }

    @Override
    public void exportAuctionsAsXmlFile(HttpServletResponse response) throws Exception {
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
    public AuctionItemResponseDto updateAuctionItem(String auctionItemId, AuctionItemUpdateRequestDto auctionItemUpdateRequestDto) throws Exception {
        AuctionItem auctionItem = getAuctionItem(auctionItemId);
        validateEditing(auctionItem);
        auctionItemMapper.update(auctionItem, auctionItemUpdateRequestDto);
        auctionItemRepository.save(auctionItem);

        return auctionItemMapper.auctionItemToAuctionItemResponseDto(auctionItem);
    }

    @Override
    public AuctionItemResponseDto startAuction(String auctionItemId, StartAuctionDto startAuctionDto) throws Exception {
        AuctionItem auctionItem = getAuctionItem(auctionItemId);
        if (auctionItem.getStartDate() != null)
            throw new AuctionAlreadyInProgressException(AuctionItemError.AUCTION_ALREADY_IN_PROGRESS);

        Date startDate = validateDates(startAuctionDto.getEndDate());
        auctionItemMapper.update(auctionItem, startDate, startAuctionDto.getEndDate());
        auctionItemRepository.save(auctionItem);

        return auctionItemMapper.auctionItemToAuctionItemResponseDto(auctionItem);
    }

    @Override
    public String uploadPhoto(MultipartFile file, String userId) throws Exception {
        validateUserId(userId);
        File path = getOrCreatePath(userId);
        File convertedFile = convert(file);
        File storedImage = storeFile(convertedFile, path, userId);

        return storedImage.getPath();
    }

    @Override
    public AuctionItemBidResponseDto bidAuctionItem(String auctionItemId, String userId) throws Exception {
        AuctionItem auctionItem = getAuctionItem(auctionItemId);
        validateUserId(userId);
        Double newBidAmount = validateAndCalculateBid(auctionItem, userId);
        addNewBidToAuctionItem(auctionItem, newBidAmount, userId);
        auctionItemRepository.save(auctionItem);

        return auctionItemMapper.auctionItemToAuctionItemBidResponseDto(auctionItem);
    }

    private void addNewBidToAuctionItem(AuctionItem auctionItem, Double bidAmount, String userId) {
        auctionItem.setBidsNo(auctionItem.getBidsNo() + 1);
        auctionItem.setCurrentBid(bidAmount);
        Bid newBid = new Bid(bidAmount, new Date(), userId);
        auctionItem.getBids().add(newBid);      //TODO check if it's added at start or end
    }

    private Double validateAndCalculateBid(AuctionItem auctionItem, String bidderId) throws Exception {
        if (auctionItem.getStartDate() == null)
            throw new BidException(AuctionItemError.AUCTION_HAS_NOT_STARTED);
        if (DateTime.now().minusSeconds(10).toDate().after(auctionItem.getEndDate()))
            throw new BidException(AuctionItemError.AUCTION_HAS_BEEN_COMPLETED);
        if (auctionItem.getUserId().equals(bidderId))
            throw new BidException(AuctionItemError.ITEM_BELONGS_TO_BIDDER);

        Double bidAmount = getBidAmountBasedOnPriceOfAuctionItem(auctionItem.getBuyout());
        Double minBid = auctionItem.getMinBid();
        Double lastBid = auctionItem.getCurrentBid();

        if (lastBid.equals(minBid))
            return minBid;
        else if (lastBid + bidAmount < auctionItem.getBuyout())
            return bidAmount;
        else
            throw new BidException(AuctionItemError.BID_AMOUNT_ABOVE_BUYOUT);
    }

    private Double getBidAmountBasedOnPriceOfAuctionItem(Double price) throws Exception {
        if (price == null)
            return 1.00;
        else if (price >= 0.01 && price <= 0.99)
            return 0.05;
        else if (price >= 1.00 && price <= 4.99)
            return 0.25;
        else if (price >= 5.00 && price <= 24.99)
            return 0.5;
        else if (price >= 25.00 && price <= 99.99)
            return 1.00;
        else if (price >= 100.00 & price <= 249.99)
            return 2.50;
        else if (price >= 250.00)
            return 5.00;
        else
            throw new Exception("Generic error.");
    }

    private File storeFile(File file, File path, String userId) throws Exception {
        File newFile = new File(path.getPath() + "/" + file.getName());
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
        if (auctionItem.getStartDate() != null)
            throw new AuctionAlreadyInProgressException(AuctionItemError.AUCTION_ALREADY_IN_PROGRESS);
    }

}
