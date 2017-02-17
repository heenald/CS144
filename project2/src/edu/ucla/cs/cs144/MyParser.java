/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;


class MyParser {
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
    
    static final String[] typeName = {
    "none",
    "Element",
    "Attr",
    "Text",
    "CDATA",
    "EntityRef",
    "Entity",
    "ProcInstr",
    "Comment",
    "Document",
    "DocType",
    "DocFragment",
    "Notation",
    };
    
    //Tags
    static final String ITEMS_TAG = "Items";
    static final String ITEM_TAG = "Item";
    static final String NAME_TAG = "Name";
    static final String CATEGORY_TAG = "Category";
    //static final String CURRENTLY_TAG = "Currently";
    static final String BUY_PRICE_TAG = "Buy_Price";
    static final String FIRST_BID_TAG = "First_Bid";
    //static final String NUMBER_OF_BIDS_TAG = "Number_of_Bids";
    static final String BIDS_TAG = "Bids";
    static final String BID_TAG = "Bid";
    static final String BIDDER_TAG = "Bidder";
    static final String LOCATION_TAG = "Location";
    static final String COUNTRY_TAG = "Country";
    static final String STARTED_TAG = "Started";
    static final String ENDS_TAG = "Ends";
    static final String SELLER_TAG = "Seller";
    static final String DESCRIPTION_TAG = "Description";
    static final String TIME_TAG = "Time";
    static final String AMOUNT_TAG = "Amount";
    
    //Attributes
    static final String ITEM_ID_ATTRIBUTE = "ItemID";
    static final String USER_ID_ATTRIBUTE = "UserID";
    static final String RATING_ATTRIBUTE = "Rating";
    static final String LATITUDE_ATTRIBUTE = "Latitude";
    static final String LONGITUDE_ATTRIBUTE = "Longitude";
    
    static String datFilesPrefix="";
    
    //itemID, name, started, ends, buyPrice, firstBid, sellerUserID, description, location, latitude, longitude, country
    static String itemFile = "item.dat";
    //itemID, category
    static String itemCategoryFile = "item_category.dat";
    //itemID, bidderUserID, time, amount
    static String bidFile = "bid.dat";
    //Seller and Bidder need to be two separate tables since if a user is seller/bidder that rating is required, else not.
    //userID, sellerRating
    static String userSellerFile = "user_seller.dat";
    //userID, bidderRating, location, country - should this location country be shared by seller, in that case place in a different table?
    static String userBidderFile = "user_bidder.dat";
    
    static Map<String, FileWriter> fileWriters;
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }
    
    static String formatForSQLTimestamp(String time) throws ParseException {
        
        SimpleDateFormat format1 = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        
        Date parsed = null;
        
        // See if we can parse the output of Date.toString()
        
        parsed = format1.parse(time);
        
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format2.format(parsed);
        
    }
    
    //Wrap in quotations and escape " in value
    static String processValue(String value){
        //Can value be always enclosed in quotes? String and Non String?
        if (value==null || value.isEmpty()){
            return "NULL";
        }
        if(value.length()>4000)
            value=value.substring(0, 4000);
        return "\"" +value.replace("\\", "\\\\").replace("\"", "\\\"")+ "\"";
        //TODO: Test if this format is inserted correctly using LOAD INFILE
    }
    
    static String joinStrings(List<String> values, String delimeter){
        
        if(values.isEmpty()) return "";
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<values.size()-1;i++)
            sb.append(values.get(i)+delimeter);
        sb.append(values.get(values.size()-1));
        
        return sb.toString();
    }
    
    static String createLine(List<String> values){
        
        List<String> processedValues = new ArrayList<String>();
        for(int i=0;i<values.size();i++)
            processedValues.add( processValue(values.get(i)));
        return joinStrings( processedValues,",")+"\n";
        
    }
    
    //creates/opens the dat file of name fileName
    static FileWriter createCSVFile(String filePath) {
        FileWriter temp = null;
        try {
            temp = new FileWriter(filePath, true);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        return temp;
    }
    
    static void addRowToCSV(String datFile, List<String> values) throws IOException{
        fileWriters.get(datFile).write(createLine(values));
    }
    
    static void processCategory(Element category, String itemID) throws IOException{
        String categoryVal = getElementText(category);
        List<String> row = new ArrayList<String>();
        row.add(itemID);
        row.add(categoryVal);
        addRowToCSV(itemCategoryFile, row);
    }
    
    static void processBid(Element bid, String itemID) throws IOException, ParseException{
        
        List<String> bidRow = new ArrayList<String>();
        List<String> userBidderRow = new ArrayList<String>();
        
        Element bidder = getElementByTagNameNR(bid, BIDDER_TAG);
        String bidderID = bidder.getAttribute( USER_ID_ATTRIBUTE);
        
        bidRow.add(itemID);
        bidRow.add(bidderID);
        bidRow.add(formatForSQLTimestamp(getElementTextByTagNameNR(bid, TIME_TAG)));
        bidRow.add(strip(getElementTextByTagNameNR(bid, AMOUNT_TAG)));
        addRowToCSV(bidFile, bidRow);
        
        userBidderRow.add(bidderID);
        userBidderRow.add(bidder.getAttribute( RATING_ATTRIBUTE));
        userBidderRow.add(getElementTextByTagNameNR(bidder, LOCATION_TAG));
        userBidderRow.add(getElementTextByTagNameNR(bidder, COUNTRY_TAG));
        addRowToCSV(userBidderFile, userBidderRow);
        
    }
    
    static String processSeller(Element seller) throws IOException{
        
        List<String> userSellerRow = new ArrayList<String>();
        
        String sellerUserID=seller.getAttribute(USER_ID_ATTRIBUTE);
        userSellerRow.add(sellerUserID);
        userSellerRow.add(seller.getAttribute( RATING_ATTRIBUTE));
        
        addRowToCSV(userSellerFile, userSellerRow);
        
        return sellerUserID;
        
    }
    
    static void processItem(Element item) throws IOException, ParseException{
        
        String itemID = item.getAttribute(ITEM_ID_ATTRIBUTE);
        
        Element[] categories = getElementsByTagNameNR(item, CATEGORY_TAG);
        for(Element category: categories)
            processCategory(category, itemID);
        
        Element[] bids = getElementsByTagNameNR(getElementByTagNameNR(item, BIDS_TAG), BID_TAG);
        for(Element bid:bids){
            processBid(bid, itemID);
        }
        
        Element seller = getElementByTagNameNR(item, SELLER_TAG);
        String sellerUserID=processSeller(seller);
        List<String> itemRow = new ArrayList<String>();
        itemRow.add(itemID);
        itemRow.add(getElementTextByTagNameNR(item, NAME_TAG));
        itemRow.add(formatForSQLTimestamp(getElementTextByTagNameNR(item, STARTED_TAG)));
        itemRow.add(formatForSQLTimestamp(getElementTextByTagNameNR(item, ENDS_TAG)));
        itemRow.add(strip(getElementTextByTagNameNR(item, BUY_PRICE_TAG)));
        itemRow.add(strip(getElementTextByTagNameNR(item, FIRST_BID_TAG)));
        itemRow.add(sellerUserID);
        itemRow.add(getElementTextByTagNameNR(item, DESCRIPTION_TAG));
        itemRow.add(getElementTextByTagNameNR(item, LOCATION_TAG));
        itemRow.add(getElementByTagNameNR(item, LOCATION_TAG).getAttribute(LATITUDE_ATTRIBUTE));
        itemRow.add(getElementByTagNameNR(item, LOCATION_TAG).getAttribute(LONGITUDE_ATTRIBUTE));
        itemRow.add(getElementTextByTagNameNR(item, COUNTRY_TAG));
        
        addRowToCSV(itemFile, itemRow);
    }
    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) throws IOException, ParseException {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
        
        /* Fill in code here (you will probably need to write auxiliary
         methods). */
        
        Element[] items = getElementsByTagNameNR(doc.getDocumentElement(), ITEM_TAG);
        for(Element item: items)
            processItem(item);
        
        /**************************************************************/
        
    }
    
    public static void main (String[] args) throws IOException, ParseException {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        //Create and open Filewriters
        fileWriters = new HashMap<String, FileWriter>();
        fileWriters.put(itemFile, createCSVFile(datFilesPrefix+itemFile));
        fileWriters.put(itemCategoryFile, createCSVFile(datFilesPrefix+itemCategoryFile));
        fileWriters.put(bidFile, createCSVFile(datFilesPrefix+bidFile));
        fileWriters.put(userBidderFile, createCSVFile(datFilesPrefix+userBidderFile));
        fileWriters.put(userSellerFile, createCSVFile(datFilesPrefix+userSellerFile));
        
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        }
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        
        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }
        
        //Close FileWriters
        for(FileWriter fw : fileWriters.values()){
            fw.close();
        }
        
    }
}
