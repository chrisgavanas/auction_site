package com.webapplication.entity;

import java.io.Serializable;
import javax.persistence.*;


@Entity
@NamedQuery(name="Image.findAll", query="SELECT i FROM Image i")
public class Image implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private int imageId;

    @Lob
    private byte[] image;

    @ManyToOne
    @JoinColumn(name="AuctionItemId")
    private Auctionitem auctionitem;

    public Image() {
    }

    public Image(byte[] image, Auctionitem auctionitem) {
        this.image = image;
        this.auctionitem = auctionitem;
    }

    public int getImageId() {
        return this.imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public byte[] getImage() {
        return this.image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Auctionitem getAuctionitem() {
        return this.auctionitem;
    }

    public void setAuctionitem(Auctionitem auctionitem) {
        this.auctionitem = auctionitem;
    }

}
