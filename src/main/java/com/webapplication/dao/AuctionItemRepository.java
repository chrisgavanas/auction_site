package com.webapplication.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.webapplication.entity.Auctionitem;

@Repository
public interface AuctionItemRepository extends CrudRepository<Auctionitem, Integer> {


}
