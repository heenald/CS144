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
    	String item_id = request.getParameter("item_id");
		
		String xmlItem = AuctionSearch.getXMLDataForItemId(item_id);
    	
		request.setAttribute("xmlItem", xmlItem);

        request.getRequestDispatcher("/item.jsp").forward(request, response);

    }
}

