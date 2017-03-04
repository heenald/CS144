<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<!DOCTYPE html>
<html>

<style>
#header {
	background-color: #C63D0F;
	color: #FDF3E7;
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
	background-color: #FDF3E7;
	
	position: fixed;
	top: calc(80px + 0.5em);
	left: 0.5em;
	bottom: 0em;
	right: 0.5em;
}
#Col1Data {
	float:left;
	width: calc(50% - 2em);
	margin: 1em 0em 0em 1em;
}
#Col2Map {
	float:right;
	width: calc(50% - 2em);
	margin: 1em 1em 0em 0em;
}
#Col1Data, #Col2Map {
	min-height: 1px;
	overflow: hidden;
}
#ContentBottom {
	float:left
	background-color: #FDF3E7;
	width: calc(100%-2em);
	margin: 0em 1em 0em 1em;
	
	clear:both;
	
}
</style>

<body>

<div id="header"> Ebay Auction Search </div>

<div id="ContentTop">
<c:choose>
	<c:when test="${not empty xmlItem}">
		<x:parse xml="${xmlItem}" var="Item"/>

			<div id = "Col1Data" >
				<b>Item ID:</b> <x:out select="$Item/Item/@ItemID" /> <br><br>
				<b>Item Name:</b> <x:out select="$Item/Item/Name" /><br><br>
				<b>Location:</b> <x:out select="$Item/Item/Location" /><br><br>
				<x:if select="$Item/Item/Location/@Latitude">
					<b>Location Coordinates:</b> (<x:out select="$Item/Item/Location/@Latitude" />, <x:out select="$Item/Item/Location/@Longitude" />) <br><br>
				</x:if>
				<b>Country:</b> <x:out select="$Item/Item/Country" /> <br><br>
				<b>Seller Information:</b> This item is being sold by the user "<b><x:out select="$Item/Item/Seller/@UserID" /></b>" (Rating <x:out select="$Item/Item/Seller/@Rating" />)<br><br>
				<b>Current Highest Bid:</b> <x:out select="$Item/Item/Currently" /> <br><br>
				<b>First Bid Amount:</b> <x:out select="$Item/Item/First_Bid" /> <br><br>
				<b>Auction Start Time:</b> <x:out select="$Item/Item/Started" /> <br><br>
				<b>Auction End Time:</b> <x:out select="$Item/Item/Ends" /> <br><br>
				<x:if select="$Item/Item/Buy_Price">
					<b>Buy Price:</b> <x:out select="$Item/Item/Buy_Price" /> <br><br>
				</x:if>
				<b>Item Categories:</b>
				<ul>
				<x:forEach select="$Item/Item/Category" var="category">
					<li> <x:out select="$category" />
				</x:forEach>
				</ul>
			</div>
			
			<x:if select="$Item/Item/Location/@Latitude">
				<script type="text/javascript" 
				src="http://maps.google.com/maps/api/js?sensor=false"> 
				</script> 
				<script type="text/javascript"> 
				  function initialize() { 
					var latitude = parseFloat('<x:out select="$Item/Item/Location/@Latitude" />');
					var longitude = parseFloat('<x:out select="$Item/Item/Location/@Longitude" />');
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
				  
					window.onload = initialize;
				</script>
			
				<div id = "Col2Map" style="width:50%; height:50%"> </div>
			</x:if>

			<div id="ContentBottom" >
				<br>
				<b>Bidding Information is as follows:</b><br><br>
				<b>Number of bids:</b> <x:out select="$Item/Item/Number_of_Bids" /> <br><br>
				<x:if select="$Item/Item/Number_of_Bids > 0">
					<b>Bids:</b>
					<x:forEach select="$Item/Item/Bids/Bid" var="bid">
						<ul><li> 
						<b>Bidder's User ID:</b> <x:out select="$bid/Bidder/@UserID" /> <br>
						<b>Bidding Rating:</b> <x:out select="$bid/Bidder/@Rating" /> <br>
						<x:if select="$bid/Bidder/Location">
							<b>Location:</b> <x:out select="$bid/Bidder/Location" /> <br>
						</x:if>
						<x:if select="$bid/Bidder/Country">
							<b>Country:</b> <x:out select="$bid/Bidder/Country" /> <br>
						</x:if>
						<b>Time of bid:</b> <x:out select="$bid/Time" /> <br>
						<b>Amount:</b> <x:out select="$bid/Amount" /> </ul>
					</x:forEach>
				</x:if>
				<b>Item Description:</b> <x:out select="$Item/Item/Description" /> <br><br>
				<br>
				<br>
				<br> 
			</div>  
	</c:when>
	<c:otherwise>
		<b><h2> Item doesn't exist! Please try another item ID. </h2><b>
	</c:otherwise>
</c:choose>
</div>

</body>
</html>