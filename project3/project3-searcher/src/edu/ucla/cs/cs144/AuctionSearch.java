package edu.ucla.cs.cs144;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

public class AuctionSearch implements IAuctionSearch {
    
    private IndexSearcher searcher = null;
    
    private QueryParser parser = null;
    
    /** Creates a new instance of SearchEngine */
    public AuctionSearch() {
        
    }
    
    private Document getDocument(int docId) throws IOException {
        return searcher.doc(docId);
    }
    
    private List<SearchResult> performSearch(String queryString, int numResultsToSkip, int numResultsToReturn)
    throws IOException, ParseException {
        
        if (searcher == null || parser == null) {
            searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("/var/lib/lucene/index-directory"))));
            parser = new QueryParser("content", new StandardAnalyzer());
        }
        Query query = parser.parse(queryString);
        TopDocs topDocs = searcher.search(query, numResultsToReturn + numResultsToSkip);
        
        List<SearchResult> searchResults = new ArrayList<SearchResult>();
        
        System.out.println("Results found: " + topDocs.totalHits);
        ScoreDoc[] hits = topDocs.scoreDocs;
        for (int i = numResultsToSkip; i < hits.length; i++) {
            Document doc = getDocument(hits[i].doc);
            searchResults.add(new SearchResult(doc.get("item_id"), doc.get("name")));
            
        }
        
        return searchResults;
    }
    
    /*
     * You will probably have to use JDBC to access MySQL data Lucene
     * IndexSearcher class to lookup Lucene index. Read the corresponding
     * tutorial to learn about how to use these.
     *
     * You may create helper functions or classes to simplify writing these
     * methods. Make sure that your helper functions are not public, so that
     * they are not exposed to outside of this class.
     *
     * Any new classes that you create should be part of edu.ucla.cs.cs144
     * package and their source files should be placed at src/edu/ucla/cs/cs144.
     *
     */
    
    public SearchResult[] basicSearch(String query, int numResultsToSkip, int numResultsToReturn) {
        
        
        SearchResult[] searchResults = new SearchResult[0];
        
        try {
            searchResults= performSearch(query, numResultsToSkip, numResultsToReturn).toArray(searchResults);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        
        return searchResults;
    }
    
    
    private String getPoly(SearchRegion region){
        
        double lx = region.getLx();
        double ly = region.getLy();
        double rx = region.getRx();
        double ry = region.getRy();
        
        String poly = "GeomFromText('Polygon((" + lx + " " + ly + ", " + lx + " " + ry + ", " + rx +
        " " + ry + ", " + rx + " " + ly + ", " + lx + " " + ly +  "))')";
        
        return poly;
        
    }
    
    public SearchResult[] spatialSearch(String query, SearchRegion region, int numResultsToSkip,
                                        int numResultsToReturn) {
        
        Connection connection = null;
        Statement s = null;
        
        List<SearchResult> spatialSearchResult = new ArrayList<SearchResult>();
        
        // create a connection to the database to retrieve Items from MySQL
        try {
            connection = DbManager.getConnection(true);
            
            s = connection.createStatement();
            
            ResultSet rs = s.executeQuery("SELECT count(*) as count FROM item");
            int itemCount = 0;
            if(rs.next())
                itemCount=rs.getInt(1);
            else
                throw new RuntimeException("Error getting count of items");
            
            rs.close();
            
            //SELECT item_id FROM item_location WHERE  MBRContains(GeomFromText('Polygon((-100 -100, -100 200, 200 200, 200 -100, -100 -100))'),location);
            
            System.out.println("Running Spatial query: SELECT item_id FROM item_location WHERE MBRContains("+getPoly(region)+", location)");
            rs = s.executeQuery("SELECT item_id FROM item_location WHERE MBRContains("+getPoly(region)+", location)");
            
            String item_id;
            Set<String> itemIDsInRegion = new HashSet<String>();
            while(rs.next()){
                item_id = rs.getString("item_id");
                itemIDsInRegion.add(item_id);
            }
            System.out.println("Items in region: "+itemIDsInRegion.size());
            rs.close();
            
            
            SearchResult[] basicSearchResults = basicSearch(query, 0, itemCount);
            System.out.println("basic search result: "+basicSearchResults.length);
            
            //Find intersection
            for(SearchResult basicSearchResult: basicSearchResults){
                if(itemIDsInRegion.contains(basicSearchResult.getItemId()))
                    spatialSearchResult.add(basicSearchResult);
                if(spatialSearchResult.size()==numResultsToSkip+numResultsToReturn)
                    break;
            }
            
            s.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        SearchResult[] results = new SearchResult[0];
        return spatialSearchResult.subList(numResultsToSkip, spatialSearchResult.size()).toArray(results);
        
    }
    
    
    private String getCategoryXML(String itemId, Connection connection) throws SQLException{
        
        StringBuffer xml = new StringBuffer();
        
        Statement s = connection.createStatement();
        
        ResultSet rs = s.executeQuery("SELECT * FROM item_category where item_id = "+itemId);
        
        while(rs.next()){
            xml.append("\t<Category>"+formatSpecialCharacters(rs.getString("category"))+"</Category>\n");
        }
        
        s.close();
        
        return xml.toString();
        
    }
    
    private String getBidsXML(String itemId,Connection connection) throws SQLException, java.text.ParseException{
        
        StringBuffer xml = new StringBuffer();
        
        Statement s_bid = connection.createStatement();
        Statement s_bidder = connection.createStatement();
        
        ResultSet rs_bid = s_bid.executeQuery("SELECT * FROM bid where item_id = "+itemId);
        
        int numberOfBids = 0;
        
        while(rs_bid.next()){
            
            numberOfBids++;
            
            xml.append("\t\t<Bid>\n");
            
            String bidder_id = rs_bid.getString("bidder_user_id");
            ResultSet rs_bidder = s_bidder.executeQuery("SELECT * FROM user_bidder where user_id = \""+bidder_id+"\"");
            
            if(!rs_bidder.next()) throw new IllegalStateException("Cannot find bidder data for id: "+bidder_id);
            
            xml.append("\t\t\t<Bidder Rating=\""+rs_bidder.getString("bidder_rating")+"\" UserID=\""+bidder_id+"\">\n");
            
            if(rs_bidder.getString("location")!=null)
                xml.append("\t\t\t\t<Location>"+formatSpecialCharacters(rs_bidder.getString("location"))+"</Location>\n");
            if(rs_bidder.getString("country")!=null)
                xml.append("\t\t\t\t<Country>"+formatSpecialCharacters(rs_bidder.getString("country"))+"</Country>\n");
            
            xml.append("\t\t\t</Bidder>\n");
            
            xml.append("\t\t\t<Time>"+formatForSQLTimestamp(rs_bid.getString("time"))+"</Time>\n");
            xml.append("\t\t\t<Amount>$"+rs_bid.getString("amount")+"</Amount>\n");
            
            xml.append("\t\t</Bid>\n");
            
        }
        
        if(numberOfBids == 0){
            xml.insert(0, "\t<Bids />\n");
        }else{
            xml.insert(0, "\t<Bids>\n");
            xml.append("\t</Bids>\n");
        }
        
        xml.insert(0, "\t<Number_of_Bids>"+numberOfBids+"</Number_of_Bids>\n");
        
        s_bid.close();
        s_bidder.close();
        
        return xml.toString();
    }
    
    private String getSellerXML(String seller_user_id, Connection connection) throws SQLException{
        
        StringBuffer xml = new StringBuffer();
        
        Statement s = connection.createStatement();
        
        ResultSet rs = s.executeQuery("SELECT * FROM user_seller where user_id = \""+seller_user_id+"\"");
        if(!rs.next()) throw new IllegalStateException("Cannot find bidder data for id: "+seller_user_id);
        
        
        xml.append("\t<Seller Rating=\""+rs.getString("seller_rating")+"\" UserID=\""+seller_user_id+"\" />\n");
        
        s.close();
        
        return xml.toString();
        
    }
    
    private String getItemLocationXML(String location, String latitude, String longitude){
        
        StringBuffer xml = new StringBuffer();
        
        xml.append("\t<Location");
        if(latitude!=null)
            xml.append(" Latitude=\""+latitude+"\"");
        if(longitude!=null)
            xml.append(	" Longitude=\""+longitude+"\"");
        xml.append(">"+formatSpecialCharacters(location)+"</Location>\n");
        
        return xml.toString();
        
    }
    
    private String formatForSQLTimestamp(String time) throws  java.text.ParseException {
        
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        
        Date parsed = null;
        
        // See if we can parse the output of Date.toString()
        
        parsed = format1.parse(time);
        
        return format2.format(parsed);
        
    }
    
    private String formatSpecialCharacters(String original){
        return original.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }
    
    public String getXMLDataForItemId(String itemId) {
        
        
        Connection connection = null;
        Statement s = null;
        
        try {
            connection = DbManager.getConnection(true);
            
            s = connection.createStatement();
            
            ResultSet rs = s.executeQuery("SELECT * FROM item where item_id = "+itemId);
            
            if(rs.next()){
                
                StringBuffer xml = new StringBuffer();
                xml.append("<Item ItemID=\""+itemId+"\">\n");
                
                xml.append("\t<Name>"+formatSpecialCharacters(rs.getString("name"))+"</Name>\n");
                xml.append(getCategoryXML(itemId, connection));
                
                xml.append("\t<Currently>$"+rs.getString("currently")+"</Currently>\n");
                
                if(rs.getString("buy_price")!=null)
                    xml.append("\t<Buy_Price>$"+rs.getString("buy_price")+"</Buy_Price>\n");
                xml.append("\t<First_Bid>$"+rs.getString("first_bid")+"</First_Bid>\n");
                
                xml.append(getBidsXML(itemId, connection));
                
                xml.append(getItemLocationXML(rs.getString("location"), rs.getString("latitude"), rs.getString("longitude")));
                xml.append("\t<Country>"+formatSpecialCharacters(rs.getString("country"))+"</Country>\n");
                
                String started = formatForSQLTimestamp(rs.getString("started"));
                String ends = formatForSQLTimestamp(rs.getString("ends"));
                
                xml.append("\t<Started>"+started+"</Started>\n");
                xml.append("\t<Ends>"+ends+"</Ends>\n");
                
                xml.append(getSellerXML(rs.getString("seller_user_id"), connection));
                xml.append("\t<Description>"+formatSpecialCharacters(rs.getString("description"))+"</Description>\n");
                
                xml.append("</Item>\n");
                return xml.toString();
            }
            
            
        } catch (SQLException | java.text.ParseException e) {
            e.printStackTrace();
        }
        
        return "";
        
    }
    
    public String echo(String message) {
        return message;
    }
    
}
