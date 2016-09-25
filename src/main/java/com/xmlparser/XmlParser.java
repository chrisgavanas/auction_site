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
import com.xmlparser.entity.Location;
import com.xmlparser.entity.Seller;
import com.xmlparser.entitylist.AuctionItemList;
import com.xmlparser.entitylist.BidAuctionList;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

    private SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yy HH:mm:ss", Locale.ENGLISH);
    private Map<String, User> users;
    private Map<String, Category> categories;


    public void unmarshall() {
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
                        categoryIds.addAll(getCategoryIdsOfAuctionsOrAdd(auctionItem.getCategories()));
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
        System.out.println("Xml parsing is completed.");
    }

    public void marshall() throws IOException {
        try {
            File xmlFile = new File("auctions.xml");
            xmlFile.createNewFile();
            FileOutputStream os = new FileOutputStream(xmlFile);
            users = new HashMap<>();
            categories = new HashMap<>();
            AuctionItemList auctionItemList = getAuctionItems();
            JAXBContext jaxbContext = JAXBContext.newInstance(AuctionItemList.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(auctionItemList, os);
            os.close();
        } catch (JAXBException e) {
            System.err.print("Could not export auctions to xml.");
        } finally {
            users = null;
            categories = null;
        }
    }

    private AuctionItemList getAuctionItems() {
        AuctionItemList auctionItemList = new AuctionItemList();
        auctionItemList.setAuctionList(new LinkedList<>());
        List<AuctionItem> auctionItems = auctionItemRepository.findAll();
        auctionItems.forEach(auctionItem -> auctionItemList.getAuctionList().add(createAuction(auctionItem)));

        return auctionItemList;
    }

    private Auction createAuction(AuctionItem auctionItem) {
        Auction auction = new Auction();
        auction.setName(auctionItem.getName());
        List<String> categories = getCategories(auctionItem.getCategoriesId());
        auction.setCategories(categories);
        auction.setMinBid("$" + auctionItem.getMinBid());
        auction.setBidsNo(auctionItem.getBidsNo());
        BidAuctionList bidAuctionList = getBids(auctionItem);
        auction.setBids(bidAuctionList);
        Location location = new Location();
        GeoLocation geoLocation = auctionItem.getGeoLocation();
        if (geoLocation != null) {
            location.setLatitude(geoLocation.getLatitude());
            location.setLongitude(geoLocation.getLongitude());
        }
        User user = users.get(auctionItem.getUserId());
        if (user == null) {
            user = userRepository.findUserByUserId(auctionItem.getUserId());
            users.put(user.getUserId(), user);
        }
        Address address = user.getAddress();
        if (address != null) {
            location.setCity(address.getCity());
            auction.setCountry(auctionItem.getCountry());
        }
        
        Date startDate = auctionItem.getStartDate();
        if(startDate != null)
        	auction.setStartDate(sdf.format(auctionItem.getStartDate()));
        
        
        Date endDate = auctionItem.getEndDate();
        if(endDate != null)
        	auction.setEndDate(sdf.format(auctionItem.getEndDate()));
        Seller seller = createSeller(user);
        auction.setSeller(seller);
        auction.setDescription(auctionItem.getDescription());

        return auction;
    }

    private BidAuctionList getBids(AuctionItem auctionItem) {
        BidAuctionList bidAuctionList = new BidAuctionList();
        List<BidAuction> bidAuctions = new LinkedList<>();
        auctionItem.getBids().forEach(bid -> bidAuctions.add(createBidAuction(bid)));
        bidAuctionList.setBids(bidAuctions);
        return bidAuctionList;
    }

    private BidAuction createBidAuction(Bid bid) {
        BidAuction bidAuction = new BidAuction();
        bidAuction.setAmount("$" + bid.getAmount());
        bidAuction.setDate(sdf.format(bid.getBidDate()));
        Bidder bidder = createBidder(bid.getUserId());
        bidAuction.setBidder(bidder);
        return bidAuction;
    }

    private Bidder createBidder(String userId) {
        Bidder bidder = new Bidder();
        User user = users.get(userId);

        if (user == null) {
            user = userRepository.findUserByUserId(userId);
            users.put(userId, user);
        }
        bidder.setUsername(user.getUsername());
        bidder.setRating(user.getRatingAsBidder());

        Location location = new Location();
        Address address = user.getAddress();
        if (address != null) {
            bidder.setCountry(user.getCountry());
            location.setCity(address.getCity());
        }
        bidder.setLocation(location);
        return bidder;
    }

    private List<String> getCategories(List<String> categories) {
        List<String> rv = new LinkedList<>();

        categories.forEach(category -> {
            Category categoryEntity = this.categories.get(category);
            if (categoryEntity == null) {
                categoryEntity = categoryRepository.findCategoryByCategoryId(category);
                this.categories.put(category, categoryEntity);
            }
            rv.add(categoryEntity.getDescription());
        });

        return rv;
    }

    private Seller createSeller(User user) {
        Seller seller = new Seller();
        seller.setRating(user.getRatingAsSeller());
        seller.setUsername(user.getUsername());
        return seller;
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
        auctionItem.setCountry(auction.getCountry());
        auctionItem.setGeoLocation(geoLocation);
        try {
            auctionItem.setStartDate(sdf.parse(auction.getStartDate()));
            auctionItem.setEndDate(new DateTime(sdf.parse(auction.getEndDate())).plusYears(20).toDate());
        } catch (ParseException ignored) {
        }
        auctionItem.setUserId(sellerId);
        auctionItem.setDescription(auction.getDescription());
        auctionItem.setImages(new LinkedList<>());

        auctionItemRepository.save(auctionItem);
    }

    private List<String> getCategoryIdsOfAuctionsOrAdd(List<String> categoryDescriptions) {
        String[] categoryIds = new String[categoryDescriptions.size()];
        Category[] categories = new Category[categoryDescriptions.size()];
        List<String> childrenIds = new LinkedList<>();
        for (int i = 0; i < categories.length; i++) {
            categories[i] = categoryRepository.findCategoryByDescription(categoryDescriptions.get(i));
            categoryIds[i] = categories[i] == null ? ObjectId.get().toString() : categories[i].getCategoryId();
        }
        for (int i = 0; i < categories.length; i++) {
            String parentId = i == 0 ? null : categoryIds[i - 1];
            childrenIds.addAll(Arrays.asList(categoryIds).subList(i + 1, categories.length));
            categories[i] = categoryRepository.save(new Category(categoryIds[i], parentId, childrenIds, categoryDescriptions.get(i)));
            childrenIds.clear();
        }

        return Arrays.asList(categoryIds);
    }

    private String addUserIfNotExists(Seller seller) {
        User user;
        if ((user = userRepository.findUserByUsername(seller.getUsername())) == null)
            user = userRepository.save(new User(seller.getUsername(), seller.getRating(), true));

        return user.getUserId();
    }

    private Bid addUserIfNotExistsAndGetBids(BidAuction bidAuction) {
        String userId = addBidderIfNotExists(bidAuction.getBidder());
        Date bidTime;
        Double amount = Double.valueOf(bidAuction.getAmount().replace(",", "").substring(1));
        try {
            bidTime = sdf.parse(bidAuction.getDate());
        } catch (ParseException e) {
            bidTime = null;
        }
        return new Bid(amount, bidTime, userId);
    }

    private String addBidderIfNotExists(Bidder bidder) {
        User user;
        Address address = new Address(bidder.getLocation() == null ? null : bidder.getLocation().getCity(), null, null);
        if ((user = userRepository.findUserByUsername(bidder.getUsername())) == null)
            user = userRepository.save(new User(bidder.getUsername(), bidder.getRating(), bidder.getCountry(), address, true));

        return user.getUserId();
    }

}
