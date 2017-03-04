package edu.ucla.cs.cs144;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchServlet extends HttpServlet implements Servlet {
       
	
    public SearchServlet() {
    	
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	
    	String query = request.getParameter("query");
    	
    	if(!(query==null || query.isEmpty())){
    		
    		Integer pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
    	
    		SearchResult[] searchResults = AuctionSearch.basicSearch(query, pageNumber*20, 20);
    		
    		request.setAttribute("searchResults", Arrays.asList(searchResults));
    		request.setAttribute("pageNumber", pageNumber+1);
    		request.setAttribute("query", query);
    		
    	}
    	
    	request.getRequestDispatcher("/search.jsp").forward(request, response);
    }
}
