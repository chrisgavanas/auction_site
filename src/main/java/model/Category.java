package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

@Entity
@NamedQueries({ 
	@NamedQuery(name = "Category.findAll", query = "SELECT c FROM Category c") 
})
public class Category implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "category_id")
	private int categoryId;

	private String category;

	// bi-directional many-to-many association to Auction_item
	@ManyToMany
	@JoinTable(name = "`auction item_has_category`", joinColumns = {
			@JoinColumn(name = "category_id") }, inverseJoinColumns = { @JoinColumn(name = "auctionItem_id") })
	private List<Auction_item> auctionItems;

	public Category() {
	}

	public int getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<Auction_item> getAuctionItems() {
		return this.auctionItems;
	}

	public void setAuctionItems(List<Auction_item> auctionItems) {
		this.auctionItems = auctionItems;
	}

}