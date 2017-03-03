package edu.ucla.cs.cs144;

import java.util.ArrayList;
import java.util.List;

public class Item {
	
	private String item_id;
	private String name;
	private String seller_user_id;
	private String seller_rating;
	private List<String> categories;
	private String currently;
	private String first_bid;
	private String num_bids;
	private String description;
	private String buy_price;
	private String latitude;
	private String longitude;
	private String location_text;
	private String country;
	private String started;
	private String ends;
	private List<Bid> bids;
	
	public Item() {
		item_id = "";
		name = "";
		seller_user_id = "";
		seller_rating = "";
		categories = new ArrayList<String>();
		currently = "";
		first_bid = "";
		num_bids = "";
		description = "";
		buy_price = "";
		latitude = "";
		longitude = "";
		location_text = "";
		country = "";
		started = "";
		ends = "";
		bids = new ArrayList<Bid>();
	}
	
	public Item(String item_id, String name, String seller_user_id, String seller_rating, List<String>categories, String currently, String first_bid, String num_bids, String description, String buy_price, String latitude, String longitude, String location_text, String country, String started, String ends, List<Bid> bids) {
		this.item_id = item_id;
		this.name = name;
		this.seller_user_id = seller_user_id;
		this.seller_rating = seller_rating;
		this.categories = categories;
		this.currently = currently;
		this.first_bid = first_bid;
		this.num_bids = num_bids;
		this.description = description;
		this.buy_price = buy_price;
		this.latitude = latitude;
		this.longitude = longitude;
		this.location_text = location_text;
		this.country = country;
		this.started = started;
		this.ends = ends;
		this.bids = bids;
	}
	
	public String getItem_id() {
		return item_id;
	}
	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSeller_user_id() {
		return seller_user_id;
	}
	public void setSeller_user_id(String seller_user_id) {
		this.seller_user_id = seller_user_id;
	}
	public String getSeller_rating() {
		return seller_rating;
	}
	public void setSeller_rating(String seller_rating) {
		this.seller_rating = seller_rating;
	}
	public List<String> getCategories() {
		return categories;
	}
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	public String getCurrently() {
		return currently;
	}
	public void setCurrently(String currently) {
		this.currently = currently;
	}
	public String getFirst_bid() {
		return first_bid;
	}
	public void setFirst_bid(String first_bid) {
		this.first_bid = first_bid;
	}
	public String getNum_bids() {
		return num_bids;
	}
	public void setNum_bids(String num_bids) {
		this.num_bids = num_bids;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getBuy_price() {
		return buy_price;
	}
	public void setBuy_price(String buy_price) {
		this.buy_price = buy_price;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getLocation_text() {
		return location_text;
	}
	public void setLocation_text(String location_text) {
		this.location_text = location_text;
	}
	public String getStarted() {
		return started;
	}
	public void setStarted(String started) {
		this.started = started;
	}
	public List<Bid> getBids() {
		return bids;
	}
	public void setBids(List<Bid> bids) {
		this.bids = bids;
	}
	public String getEnds() {
		return ends;
	}
	public void setEnds(String ends) {
		this.ends = ends;
	}

}
