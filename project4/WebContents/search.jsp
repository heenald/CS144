<%@ page import="edu.ucla.cs.cs144.SearchResult,java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
	width: calc(100%-3em);
	height: calc(100% - 1em);
	overflow-y: auto;
	
	position: relative;
	top: 80px;
	left: 2em;
	bottom: 1em;
	right: 0;
}

#Col1Input {
	float:left;
	width: calc(25% - 1.5em);
	margin: 0em 0.5em 0em 0em;
	left: 0;
}
#Col2Results {
	float:right;
	width: calc(75% - 1.5em);
	margin: 0em 0em 0em 0.5em;
	right: 1em;
}
</style>

<head>

	<title>Search Items</title>
	<div id="header"> Ebay Auction Search </div>

	<script type="text/javascript" src="search.js"></script>
    <link rel="stylesheet" type="text/css" href="search.css" />

	<script type="text/javascript">
        window.onload = function() {
            var oTextbox = new AutoSuggestControl(document.getElementById("query"), new GoogleSuggestions()); 
        }
 	</script>

</head>
	

<body>
<div id="content" >
	<div id="Col1Input" >
		<form name="itemsForm" action="" method="get" autocomplete="off">
			Your eBay item query goes here:<br>
			<p><input type="text" id = "query" name="query" size="20px">
			<input type="hidden" name="pageNumber" value="0">
			<input type="submit" value="Submit"></p>
		</form>
	</div>
	<div id="Col2Results" >
		<%
			if (request.getAttribute("query")!=null){

				if(request.getAttribute("searchResults")!=null && ((List)request.getAttribute("searchResults")).size() > 0) {

			String query = (String) request.getAttribute("query");
			Integer pageNumber = (Integer)request.getAttribute("pageNumber");

		%>
		<b><h2>Top results matching your query:</b></h2>
		<ul>
			<c:forEach items="${searchResults}" var="result">
				<li>
					<a href="item?item_id=${result.itemId}">${result.itemId}</a> : <span>${result.name}</span>
				</li>
			</c:forEach>
		</ul>

		<form name="nextForm" action="" method="get">
			<input type="hidden" name="query" value="${query}">
			<input type="hidden" name="pageNumber" value="${pageNumber}">
			<input type="submit" value="Get Next 20">
		</form>
		<br>
		<br>

		<% } else {%>
			No results to be displayed.
		<% } 
		}
		%>
	</div>
</div>

</body>

</html>