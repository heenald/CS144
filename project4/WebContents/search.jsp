<%@ page import="edu.ucla.cs.cs144.SearchResult,java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head><title>Search Items</title></head>

<body>
	
	<form name="itemsForm" action="" method="get">
		Your eBay item query goes here:
		<input type="text" name="query" size="20px">
		<input type="hidden" name="pageNumber" value="0">
		<input type="submit" value="Submit">
	</form>

	<%
		if (request.getAttribute("query")!=null){

			if(request.getAttribute("searchResults")!=null && ((List)request.getAttribute("searchResults")).size() > 0) {

    	String query = (String) request.getAttribute("query");
    	Integer pageNumber = (Integer)request.getAttribute("pageNumber");

    %>
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

	<% } else {%>
		No results to be displayed.
	<% } 
	}
	%>

</body>

</html>