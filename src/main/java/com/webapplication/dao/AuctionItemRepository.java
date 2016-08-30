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

    @Query("{'userId' : '?0', 'startDate' : null }")
    List<AuctionItem> findPendingAuctionsOfUser(String userId, Pageable pageable);

    @Query("{'userId' : '?0', 'endDate' : {'$lt' : '?1'} }")
    List<AuctionItem> findInactiveAuctionsOfUser(String userId, Date date, Pageable pageable);

    AuctionItem findAuctionItemByAuctionItemId(String auctionItemId);

    @Query("{'endDate' : {'$gte' : '?0'} }")
    List<AuctionItem> findActiveAuctions(Date endDate, Pageable pageable);

}
