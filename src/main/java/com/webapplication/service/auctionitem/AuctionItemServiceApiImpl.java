package com.webapplication.service.auctionitem;

import com.webapplication.dao.AuctionItemRepository;
import com.webapplication.dao.UserRepository;
import com.webapplication.dto.auctionitem.AddAuctionItemRequestDto;
import com.webapplication.dto.auctionitem.AddAuctionItemResponseDto;
import com.webapplication.dto.auctionitem.AuctionItemResponseDto;
import com.webapplication.dto.auctionitem.Status;
import com.webapplication.entity.AuctionItem;
import com.webapplication.entity.User;
import com.webapplication.error.auctionitem.AuctionItemError;
import com.webapplication.exception.AuctionItemNotFoundException;
import com.webapplication.exception.UserNotFoundException;
import com.webapplication.mapper.AuctionItemMapper;
import com.xmlparser.XmlParser;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
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

    @Override
    public AddAuctionItemResponseDto addAuctionItem(AddAuctionItemRequestDto auctionItemRequestDto) throws Exception {
        AuctionItem auctionItem = auctionItemMapper.addAuctionItemRequestDtoToAuctionItem(auctionItemRequestDto);
        User user = userRepository.findUserByUserId(auctionItem.getUserId());
        Optional.ofNullable(user).orElseThrow(() -> new UserNotFoundException(AuctionItemError.USER_NOT_FOUND));
        auctionItemRepository.save(auctionItem);

        return auctionItemMapper.auctionItemToAddAuctionItemResponseDto(auctionItem);
    }

    @Override
    public List<AuctionItemResponseDto> getAuctionItemsOfUserByStatus(String userId, Status status) throws Exception {
        List<AuctionItem> auctionItems = status.equals(Status.ACTIVE) ?
                auctionItemRepository.findActiveAuctionsOfUser(userId, new Date()) : auctionItemRepository.findInactiveAuctionsOfUser(userId, new Date());

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
        AuctionItem auctionItem = auctionItemRepository.findAuctionItemByAuctionItemId(auctionItemId);
        Optional.ofNullable(auctionItem).orElseThrow(() -> new AuctionItemNotFoundException(AuctionItemError.AUCTION_ITEM_NOT_FOUND));

        return auctionItemMapper.auctionItemToAuctionItemResponseDto(auctionItem);
    }

}
