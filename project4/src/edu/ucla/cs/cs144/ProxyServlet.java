package edu.ucla.cs.cs144;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProxyServlet extends HttpServlet implements Servlet {
	
	private static final String queryURL = "http://google.com/complete/search?output=toolbar&q=";
       
    public ProxyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	
    	String log= "/tmp/proxyServletLog";
    	FileWriter fw = new FileWriter(log,true);
    	
        String query = request.getParameter("query");
        query = query == null ? "" : query;
        
        URL url = new URL(queryURL + query);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		conn.setRequestMethod("GET");
		conn.setDoOutput(true);
		
		if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			StringBuffer sb = new StringBuffer();
			String line = "";
			while((line = br.readLine()) != null) {
				sb.append(line);
			}
			
			response.setContentType("text/xml");
            PrintWriter out = response.getWriter();
            out.println(sb.toString());
            
            fw.write("Writing to response: "+sb.toString()+"\n");
            
			br.close();
			fw.close();


		}
        
    }
}
