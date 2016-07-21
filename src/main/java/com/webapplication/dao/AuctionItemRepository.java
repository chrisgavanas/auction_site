package com.webapplication.dao;

import com.webapplication.entity.AuctionItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuctionItemRepository extends MongoRepository<AuctionItem, String> {

    List<AuctionItem> findAuctionItemByUserId(String userId);

}
