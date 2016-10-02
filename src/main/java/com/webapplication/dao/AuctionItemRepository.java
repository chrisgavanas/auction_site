package com.webapplication.dao;

import com.webapplication.entity.AuctionItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AuctionItemRepository extends MongoRepository<AuctionItem, String> {

    @Query("{'userId' : '?0', 'endDate' : {'$gte' : '?1'} }")
    List<AuctionItem> findActiveAuctionsOfUser(String userId, Date date, Pageable pageable);

    @Query(value = "{'userId' : '?0', 'endDate' : {'$gte' : '?1'} }", count = true)
    Long countActiveAuctionsOfUser(String userId, Date date);

    @Query("{'userId' : '?0', 'startDate' : null }")
    List<AuctionItem> findPendingAuctionsOfUser(String userId, Pageable pageable);

    @Query(value = "{'userId' : '?0', 'startDate' : null }", count = true)
    Long countPendingAuctionsOfUser(String userId);

    @Query("{'userId' : '?0', 'endDate' : {'$lt' : '?1'} }")
    List<AuctionItem> findInactiveAuctionsOfUser(String userId, Date date, Pageable pageable);

    @Query(value = "{'userId' : '?0', 'endDate' : {'$lt' : '?1'} }", count = true)
    Long countInactiveAuctionsOfUser(String userId, Date date);

    AuctionItem findAuctionItemByAuctionItemId(String auctionItemId);

    @Query(value = "{'endDate' : {'$gte' : '?0'}}", fields = "{'_id' : 1}")
    List<AuctionItem> findAuctionItemIdsOfActiveAuctions(Date date);

    @Query(value = "{'auctionItemId' : '?0'}", fields = "{'bidsNo' : 1}")
    AuctionItem findBidsNoOfAuction(String auctionItemId);

    @Query("{'endDate' : {'$gte' : '?0'} }")
    List<AuctionItem> findActiveAuctions(Date endDate, Pageable pageable);

    @Query("{'endDate' : {$gte : ?6}, $or: [{'name' : {$regex : '?0', $options : 'i'} }, {'description' : {$regex : '?0', $options : 'i'} } ], 'categoriesId' : {$regex : '?1' }, " +
            " 'country' : {$regex : '?2', $options : 'i' }, 'currentBid' : {$gte : ?3, $lte : ?4}, 'userId': {$regex: '?5'}}")
    List<AuctionItem> findAuctionsWithCriteria(String text, String categoryId, String country, Double priceFrom, Double priceTo, String sellerId, Date date, Pageable pageable);

    @Query(value = "{'endDate' : {$gte : ?6}, $or: [{'name' : {$regex : '?0', $options : 'i'} }, {'description' : {$regex : '?0', $options : 'i'} } ], 'categoriesId' : {$regex : '?1' }, " +
            " 'country' : {$regex : '?2', $options : 'i' }, 'currentBid' : {$gte : ?3, $lte : ?4}, 'userId': {$regex: '?5'}}", count = true)
    Long countAuctionsWithCriteria(String text, String categoryId, String country, Double priceFrom, Double priceTo, String sellerId, Date date);


    @Query("{'endDate' : { '$lte' : '?0' }, 'buyerId' : null, 'bidsNo' : { '$gte' : 1 } }")
    List<AuctionItem> findSoldAndBiddedAuctionItems(Date date);

    @Query("{'auctionItemId' : { $in : ?0 } }")
    List<AuctionItem> findAuctionItemsByIds(List<String> auctionItemIds);

}
