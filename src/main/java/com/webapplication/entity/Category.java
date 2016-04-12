package com.webapplication.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the category database table.
 * 
 */
@Entity
@NamedQuery(name="Category.findAll", query="SELECT c FROM Category c")
public class Category implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int categoryId;

	private String description;

	//bi-directional many-to-many association to Auctionitem
	@ManyToMany
	@JoinTable(
		name="auctionitem_has_category"
		, joinColumns={
			@JoinColumn(name="CategoryId")
			}
		, inverseJoinColumns={
			@JoinColumn(name="AuctionItemId")
			}
		)
	private List<Auctionitem> auctionitems;

	public Category() {
	}

	public int getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Auctionitem> getAuctionitems() {
		return this.auctionitems;
	}

	public void setAuctionitems(List<Auctionitem> auctionitems) {
		this.auctionitems = auctionitems;
	}

}