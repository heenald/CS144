package edu.ucla.cs.cs144;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Item {
    
    String item_id;
    String name;
    String description;
    String seller_user_id;
    
    @Override
    public String toString() {
        return "Item [item_id=" + item_id + ", name=" + name + ", description=" + description==null? "null": description.length() + ", seller_user_id="
        + seller_user_id + "]";
    }
    
    public Item(String item_id, String name, String description, String seller_user_id) {
        super();
        this.item_id = item_id;
        this.name = name;
        this.description = description;
        this.seller_user_id = seller_user_id;
    }
    
    public String getItem_id() {
        return item_id;
    }
    
    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getSeller_user_id() {
        return seller_user_id;
    }
    
    public void setSeller_user_id(String seller_user_id) {
        this.seller_user_id = seller_user_id;
    }
    
    public static List<Item> getItemsFromDB(Connection connection){
        
        List<Item> items = new ArrayList<Item>();
        
        try {
            Statement s = connection.createStatement();
            
            ResultSet rs = s.executeQuery("SELECT * FROM item");
            
            String item_id;
            String name;
            String description;
            String seller_user_id;
            
            while (rs.next()) {
                item_id = rs.getString("item_id");
                name = rs.getString("name");
                description = rs.getString("description");
                seller_user_id = rs.getString("seller_user_id");
                //System.out.println(item_id + " -- " + name + " -- " + seller_user_id + " --" + description.length());
                
                items.add(new Item(item_id, name, description, seller_user_id));
            }
            /* close the resultset and statement */
            rs.close();
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return items;
    }
    
    public static Map<String, List<String>> getItemCategoriesFromDB(Connection connection){
        
        Map<String, List<String>> itemCategories = new HashMap<String, List<String>>();
        
        try {
            Statement s = connection.createStatement();
            
            ResultSet rs = s.executeQuery("SELECT * FROM item_category");
            
            String item_id;
            String category;
            
            while (rs.next()) {
                item_id = rs.getString("item_id");
                category = rs.getString("category");
                
                if(!itemCategories.containsKey(item_id)){
                    itemCategories.put(item_id, new ArrayList<String>());
                }
                itemCategories.get(item_id).add(category);
            }
            /* close the resultset and statement */
            rs.close();
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return itemCategories;
    }
    
    
}
