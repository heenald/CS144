package edu.ucla.cs.cs144;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // Get XML data for item from AuctionSearch
    	// Parse XML data
    	String item_id = request.getParameter("item_id");
    	String log= "/tmp/searchServletLog";
    	//FileWriter fw = new FileWriter(log,true);
		
    	Item item = null;
		String xmlItem = AuctionSearch.getXMLDataForItemId(item_id);
    	//fw.write("ItemId: "+item_id+"\n");
		//fw.write("XMLItem: "+xmlItem+"\n");
		
    	if(xmlItem!=null && !xmlItem.isEmpty()) {
	    	// Using dummy item for now
	    	//String item_id = "1043495702";
	    	String name = "Precious Moments Fig-ANGEL OF MERCY- NURSE";
	    	String seller_user_id = "lwm123";
	    	String seller_rating = "813";
	    	List<String> categories = new ArrayList<String>();
	    	categories.add("Collectibles");
	    	categories.add("Decorative &amp; Holiday");
	    	categories.add("Decorative by Brand");
	    	categories.add("Enesco");
	    	categories.add("Precious Moments");
	    	String currently = "$28.00";
	    	String first_bid = "$9.99";
	    	String num_bids = "2";
	    	String description = "Precious Moments Fig-ANGEL OF MERCY- NURSE Click picture to enlarge Makes a great gift for Collectors Description Up for bids is this great figurine from Precious Moments. It is #102482, ANGEL OF MERCY. It is a 5 1/2 inch figurine of an angel nurse. She is carrying a plant. It is really cute, just look at the pictures. It was released in 1986. This figurine bears the FIRST production mark of the Olive Branch. It is in EXCELLENT condition. It has only been displayed in a glass case and comes in its original box with all production tags included. The bidding starts at $9.99 with NO RESERVE!!! THANK YOU FOR LOOKING AND HAPPY BIDDING!!!!!!!!!!!!!! US Shipping is $5.60 for Priority Mail with tracking. International shipping, including Canada, is different depending on destination. International bidders, including Canada, must pay through Pay Pal or in US Cash. Email with any questions. Insurance, if desired, is extra. 0-$50.=1.10 - $51.-$100.=$2.00 -$101.-$200.=$3.00 - $201 - $250.=$4.00. Winning bidder must make contact within 3 days and payment must be received witin 10 days (21 days for international winners) or Ebay will be contacted and item relisted. If you don't intend to complete the transaction, Please Don't Bid. Details Click picture to enlarge Condition Excellent This item is in EXCELLENT condition it has only been displayed in a glass case. It comes in its original box with all production tags included!!!!! Payment and Shipping Info Payment Options Money Order/Cashier's Check or Personal Check _ _ _ Shipping Fixed Shipping Charges.$5.60 (Domestic) Will Ship Internationally Quantity Available 1 Special! PLEASE CLICK THE LINK BELOW TO VIEW MY OTHER ITEMS. It Shows ALL My Auctions!!!...I have many quality Precious Moments Figurines and Dolls up for bids with values up to $250.00....ALL with starting bids of $9.99 or less and ALL with NO RESERVE!!!...CHECK THEM OUT!!!!!.... Combine auctions and SAVE on shipping!!!...I accept Pay Pal, Money Orders and Personal Checks. Please note there is 10 day hold on shipping till Personal Checks clear. Click picture to enlarge _ Click picture to enlarge _ Click picture to enlarge _ Click picture to enlarge _ Click picture to enlarge _ Use the REAL selling tools a million sellers do - Andale!";
	    	String buy_price = "";
	    	String latitude = "38.638318";
	    	String longitude = "-90.427118";
	    	String location_text = "Missouri The Show Me State";
	    	String country = "USA";
	    	String started = "Dec-03-01 20:40:07";
	    	String ends = "Dec-13-01 20:40:07";
	    	List<Bid> bids = new ArrayList<Bid>();
	    	bids.add(new Bid("427","nobody138","","","Dec-04-01 23:20:07","$12.99"));
	    	bids.add(new Bid("1","danielhb2000","Huntington Beach, Ca.","USA","Dec-06-01 02:00:07","$15.99"));
	
	    	item = new Item(item_id,name,seller_user_id,seller_rating,categories,currently,first_bid,num_bids,description,buy_price,latitude,longitude,location_text,country,started,ends,bids);
    	}
    	
    	request.setAttribute("Item", item);

        request.getRequestDispatcher("/item.jsp").forward(request, response);
        //fw.close();
    }
}

