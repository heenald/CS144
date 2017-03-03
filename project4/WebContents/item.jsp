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
#ContentTop {
	width: calc(100%-3em);
	height: calc(100% - 80px - 1em);
	overflow-y: auto;
	
	position: fixed;
	top: calc(80px + 2em);
	left: 2em;
	bottom: 1em;
	right: 0;
}
#Col1Data {
	float:left;
	width: calc(50% - 1.5em);
	margin: 0em 0.5em 0em 0em;
	left: 0;
}
#Col2Map {
	float:right;
	width: calc(50% - 1.5em);
	margin: 0em 0em 0em 0.5em;
	right: 2em
}
#Col1Data, #Col2Map {
	min-height: 1px;
	overflow: hidden;
}
#ContentBottom {
	float:left
	width: calc(100%-3em);
	clear:both
}
</style>

<script type="text/javascript" 
	src="http://maps.google.com/maps/api/js?sensor=false"> 
</script> 
<script type="text/javascript"> 
  function initialize() { 
	var latitude = parseFloat('${Item.latitude}');
	var longitude = parseFloat('${Item.longitude}');
	var latlng = new google.maps.LatLng(latitude,longitude); 
	var myOptions = { 
	  zoom: 14, // default is 8  
	  center: latlng, 
	  mapTypeId: google.maps.MapTypeId.ROADMAP 
	}; 
	var map = new google.maps.Map(document.getElementById("Col2Map"),
		myOptions);
	var marker = new google.maps.Marker({
		position: latlng,
		map: map
	});
  }
</script>

<body onload="initialize()">

<c:choose>
	<c:when test="${not empty Item}">	
		<div id="header"> Item Details </div>

		<div id="ContentTop">
			<div id = "Col1Data" >
				<b>Item ID:</b> ${Item.item_id} <br><br>
				<b>Item Name:</b> ${Item.name} <br><br>
				<b>Location:</b> ${Item.location_text} <br><br>
				<b>Location Coordinates:</b> (${Item.latitude}, ${Item.longitude}) <br><br>
				<b>Country:</b> ${Item.country} <br><br>
			</div>
			
			<div id = "Col2Map" style="width:50%; height:50%"> </div>

			<div id="ContentBottom" >
				<br><br>
				<b>Seller Information:</b> This item is being sold by the user "<b>${Item.seller_user_id} </b>" (Rating ${Item.seller_rating})<br><br>
				<b>Current Highest Bid:</b> ${Item.currently} <br><br>
				<b>First Bid Amount:</b> ${Item.first_bid} <br><br>
				<b>Auction Start Time:</b> ${Item.started} <br><br>
				<b>Auction End Time:</b> ${Item.ends} <br><br>
				<c:if test="${not empty Item.buy_price}">
					<b>Buy Price:</b> ${Item.buy_price} <br><br>
				</c:if>
				<b>Item Categories:</b>
				<ul>
				<c:forEach items="${Item.categories}" var="category">
					<li> ${category}
				</c:forEach>
				</ul>
				<b>Bidding Information is as follows:</b><br><br>
				<b>Number of bids:</b> ${Item.num_bids} <br><br>
				<c:if test="${Item.num_bids ne 0}">
					<b>Bids:</b>
					<c:forEach items="${Item.bids}" var="bid">
						<ul><li> 
						<b>Bidder's User ID:</b> ${bid.getBidder_id()} <br>
						<b>Bidding Rating:</b> ${bid.getBidder_rating()} <br>
						<c:if test="${not empty bid.getLocation()}">
							<b>Location:</b> ${bid.getLocation()} <br>
						</c:if>
						<c:if test="${not empty bid.getCountry()}">
							<b>Country:</b> ${bid.getCountry()} <br>
						</c:if>
						<b>Time of bid:</b> ${bid.getTime()} <br>
						<b>Amount:</b> ${bid.getAmount()} </ul>
					</c:forEach>
				</c:if>
				<b>Item Description:</b> ${Item.description} <br><br>
				<br>
				<br>
				<br>
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<b><h2> Item doesn't exist! Please try another item ID. </h2><b>
	</c:otherwise>
</c:choose>

</body>
</html>