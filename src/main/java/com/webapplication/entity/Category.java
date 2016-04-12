package com.webapplication.entity;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;


@Entity
@NamedQuery(name="Category.findAll", query="SELECT c FROM Category c")
public class Category implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int categoryId;

	private String description;

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
	@JsonIgnore
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