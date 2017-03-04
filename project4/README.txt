
1. The code is structured as follows:

Servlets
	1. SearchServlet: handles the search request, search requests contain two information: the query and the pageNumber (pageNumber is a hidden field in the form) ; 20 results are shown per page.
	2. ItemServlet: handles the requests to get item details.
	3. ProxyServlet: Called from search.js to handle autosuggestion requests.

JSPs
	1. search.jsp displays the search page with autosuggestion feature and clickable item links. search.js and search.css provide the Google suggestion feature to search page
	2. item.jsp displays the item details


Error page: A general-error.html page is added, which the web.xml directs to in case of error 500 (Internal server error).


2. Some usability decisions we made were:
	1. Message ‘No more results to be displayed’ at end of get next 20 results and message ‘No matching results found’ when query could not find any result.
	2. The user query is retained in the text box when user fetches next 20 results to leave the page more intuitive.
	3. The Google map is not displayed in case of missing coordinates.
	4. If a item detail is missing the attribute is not shown on the details page at all.