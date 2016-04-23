package com.webapplication.dao;

import com.webapplication.entity.Auctionitem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuctionItemRepository extends CrudRepository<Auctionitem, Integer> {

    @Query("select a from Auctionitem a where a.user.userId = ?1")
    List<Auctionitem> findAuctionitemByUser(Integer userId);

}
