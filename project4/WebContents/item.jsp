<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>

<style>
#header {
	background-color: yellow;
	font-size: 35pt;
	width: 100%;
	height: 80px;
	text-align: center;
	line-height: 80px;
	position: fixed;
	top: 0;
	left: 0;
}
#content {
	overflow-y: auto;
	width: calc(100%-3em);
	height: calc(100% - 80px - 1em);
	
	position: fixed;
	top: calc(80px + 2em);
	left: 2em;
	bottom: 1em;
	right: 0;
}
</style>

<div id="header"> Item Details </div>
<div id="content">
	<b>Item ID:</b> <%= request.getAttribute("item_id") %><br><br>
	<b>Item Name:</b> <%= request.getAttribute("name") %><br><br>
	<b>Seller Information:</b> This item is being sold by the user "<b><%= request.getAttribute("seller_user_id") %></b>" (Rating <%= request.getAttribute("seller_rating") %>)<br><br>
	<b>Current Highest Bid:</b> <%= request.getAttribute("currently") %><br><br>
	<b>First Bid Amount:</b> <%= request.getAttribute("first_bid") %><br><br>
	<b>Location:</b> <%= request.getAttribute("location_text") %><br><br>
	<b>Country:</b> <%= request.getAttribute("country") %><br><br>
	<b>Auction Start Time:</b> <%= request.getAttribute("started") %><br><br>
	<b>Auction End Time:</b> <%= request.getAttribute("ends") %><br><br>
	<% if(request.getAttribute("buy_price")!="") {
		%>
		<b>Buy Price:</b> <%= request.getAttribute("buy_price") %> <br><br>
		<%
		}
	%>
	<b>Item Categories:</b>
	<ul>
	<c:forEach items="${categories}" var="category">
		<li> ${category}
	</c:forEach>
	</ul>
	<b>Bidding Information is as follows:</b><br><br>
	<b>Number of bids:</b> <%= request.getAttribute("num_bids") %><br><br>
	<b>Bids:</b>
	<c:forEach items="${bids}" var="bid">
		<ul><li> 
		<b>Bidder's User ID:</b> ${bid.getBidder_id()} <br>
		<b>Bidding Rating:</b> ${bid.getBidder_rating()} <br>
		<b>Location:</b> ${bid.getLocation()} <br>
		<b>Country:</b> ${bid.getCountry()} <br>
		<b>Time of bid:</b> ${bid.getTime()} <br>
		<b>Amount:</b> ${bid.getAmount()} </ul>
	</c:forEach>
	<b>Item Description:</b> <%= request.getAttribute("description") %><br><br>
	<br>
	<br>
	<br>
</div>
</html>