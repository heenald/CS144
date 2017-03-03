package edu.ucla.cs.cs144;

public class Bid {
	
	private String bidder_rating;
	private String bidder_id;
	private String location;
	private String country;
	private String time;
	private String amount;
	
	public Bid() {
		bidder_rating = "";
		bidder_id = "";
		location = "";
		country = "";
		time = "";
		amount = "";
	}
	
	public Bid(String bidder_rating, String bidder_id, String location, String country, String time, String amount) {
		this.bidder_rating = bidder_rating;
		this.bidder_id = bidder_id;
		this.location = location;
		this.country = country;
		this.time = time;
		this.amount = amount;
	}
	
	public String getBidder_rating() {
		return bidder_rating;
	}
	public void setBidder_rating(String bidder_rating) {
		this.bidder_rating = bidder_rating;
	}
	public String getBidder_id() {
		return bidder_id;
	}
	public void setBidder_id(String bidder_id) {
		this.bidder_id = bidder_id;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
}
