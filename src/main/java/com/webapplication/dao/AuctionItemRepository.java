package com.webapplication.dao;

import com.webapplication.entity.AuctionItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AuctionItemRepository extends MongoRepository<AuctionItem, String> {

    @Query("{'userId' : '?0', 'endDate' : {'$gte' : '?1'} }")
    List<AuctionItem> findActiveAuctionsOfUser(String userId, Date date, Pageable pageable);

    @Query("{'userId' : '?0', 'startDate' : null }")
    List<AuctionItem> findPendingAuctionsOfUser(String userId, Pageable pageable);

    @Query("{'userId' : '?0', 'endDate' : {'$lt' : '?1'} }")
    List<AuctionItem> findInactiveAuctionsOfUser(String userId, Date date, Pageable pageable);

    AuctionItem findAuctionItemByAuctionItemId(String auctionItemId);

    @Query("{'endDate' : {'$gte' : '?0'} }")
    List<AuctionItem> findActiveAuctions(Date endDate, Pageable pageable);

    @Query("{$or: [{'name' : {$regex : '?0', $options : 'i'} }, {'description' : {$regex : '?0', $options : 'i'} } ], 'categoriesId' : {$regex : '?1' }, " +
            " 'country' : {$regex : '?2', $options : 'i' }, 'currentBid' : {$gte : ?3, $lte : ?4}, 'userId': {$regex: '?5'}}")
    List<AuctionItem> findAuctionsWithCriteria(String text, String categoryId, String country, Double priceFrom, Double priceTo, String sellerid,Pageable pageable);

    @Query("{'endDate' : { '$lte' : '?0' }, 'buyerId' : null, 'bidsNo' : { '$gte' : 1 } }")
    List<AuctionItem> findSoldAndBiddedAuctionItems(Date date);

}
