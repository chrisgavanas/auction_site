package com.xmlparser;

import com.webapplication.dao.AuctionItemRepository;
import com.webapplication.dao.CategoryRepository;
import com.webapplication.dao.UserRepository;
import com.webapplication.entity.Address;
import com.webapplication.entity.AuctionItem;
import com.webapplication.entity.Bid;
import com.webapplication.entity.Category;
import com.webapplication.entity.GeoLocation;
import com.webapplication.entity.User;
import com.xmlparser.entity.Auction;
import com.xmlparser.entity.BidAuction;
import com.xmlparser.entity.Bidder;
import com.xmlparser.entity.Seller;
import com.xmlparser.entitylist.AuctionItemList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Transactional
@Component
public class XmlParser {

    @Autowired
    private AuctionItemRepository auctionItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Value("${load}")
    private Boolean load;

    @Value("${path}")
    private String path;

    private SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");


    public void parse() {
        if (load == null || !load || path == null)
            return;

        File file = new File(path);
        File[] datasetFolder = file.listFiles();
        if (datasetFolder == null) {
            System.err.println("Path not found");
            System.exit(-1);
        }

        List<String> categoryIds = new LinkedList<>();
        List<Bid> bids = new LinkedList<>();
        Arrays.stream(datasetFolder).forEach(xmlDataset -> {
            try {
                if (!xmlDataset.isDirectory() && xmlDataset.getName().endsWith(".xml")) {
                    JAXBContext jaxbContext = JAXBContext.newInstance(AuctionItemList.class);
                    Unmarshaller jaxbMarshaller = jaxbContext.createUnmarshaller();
                    AuctionItemList auctionItemList = (AuctionItemList) jaxbMarshaller.unmarshal(xmlDataset);
                    auctionItemList.getAuctionList().forEach(auctionItem -> {
                        auctionItem.getCategories().forEach(auctionCategory -> categoryIds.add(getCategoryIdOfAuctionOrAdd(auctionCategory)));
                        String sellerId = addUserIfNotExists(auctionItem.getSeller());

                        if (!auctionItem.getBids().isEmpty()) {
                            auctionItem.getBids().getBids().forEach(bid -> {
                                bids.add(addUserIfNotExistsAndGetBids(bid));
                            });
                        }
                        addAuctions(auctionItem, categoryIds, bids, sellerId);

                        categoryIds.clear();
                        bids.clear();
                    });
                    System.out.println("Successfully loaded file: " + xmlDataset);
                }
            } catch (JAXBException e) {
                System.err.println("Xml parsing of file " + xmlDataset + " failed. Please check the input format.");
            }
        });
    }

    private void addAuctions(Auction auction, List<String> categoryIds, List<Bid> bids, String sellerId) {
        GeoLocation geoLocation = new GeoLocation(
                auction.getLocation() == null ? null : auction.getLocation().getLatitude(),
                auction.getLocation() == null ? null : auction.getLocation().getLongitude()
        );

        AuctionItem auctionItem = new AuctionItem();
        auctionItem.setName(auction.getName());
        auctionItem.setCategories(categoryIds);
        auctionItem.setCurrentBid(Double.valueOf(auction.getCurrentBid().replace(",", "").substring(1)));
        auctionItem.setMinBid(Double.valueOf(auction.getMinBid().replace(",", "").substring(1)));
        auctionItem.setBidsNo(auction.getBidsNo());
        auctionItem.setBids(bids);
        auctionItem.setGeoLocation(geoLocation);
        try {
            auctionItem.setStartDate(sdf.parse(auction.getStartDate()));
            auctionItem.setEndDate(sdf.parse(auction.getEndDate()));
        } catch (ParseException e) {
        }
        auctionItem.setUserId(sellerId);
        auctionItem.setDescription(auction.getDescription());

        auctionItemRepository.save(auctionItem);
    }

    private String getCategoryIdOfAuctionOrAdd(String auctionCategory) {
        Category category;
        if ((category = categoryRepository.findCategoryByDescription(auctionCategory)) == null)
            category = categoryRepository.save(new Category(auctionCategory));

        return category.getCategoryId();
    }

    private String addUserIfNotExists(Seller seller) {
        User user;
        if ((user = userRepository.findUserByUsername(seller.getUsername())) == null)
            user = userRepository.save(new User(seller.getUsername(), seller.getRating(), false));

        return user.getUserId();
    }

    private Bid addUserIfNotExistsAndGetBids(BidAuction bidAuction) {
        addBidderIfNotExists(bidAuction.getBidder());
        Date bidTime;
        String username = bidAuction.getBidder().getUsername();
        Double amount = Double.valueOf(bidAuction.getAmount().replace(",", "").substring(1));
        try {
            bidTime = sdf.parse(bidAuction.getDate());
        } catch (ParseException e) {
            bidTime = null;
        }
        return new Bid(amount, bidTime, username);
    }

    private void addBidderIfNotExists(Bidder bidder) {
        Address address = new Address(bidder.getLocation() == null ? null : bidder.getLocation().getCity(), null, null);
        if (userRepository.findUserByUsername(bidder.getUsername()) == null)
            userRepository.save(new User(bidder.getUsername(), bidder.getRating(), bidder.getCountry(), address, false));
    }

}
