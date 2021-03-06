package edu.ucla.cs.cs144;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {
    
    /** Creates a new instance of Indexer */
    public Indexer() {
    }
    
    private IndexWriter indexWriter = null;
    
    public IndexWriter getIndexWriter(boolean create) throws IOException {
        if (indexWriter == null) {
            Directory indexDir = FSDirectory.open(new File("/var/lib/lucene/index-directory"));
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
            indexWriter = new IndexWriter(indexDir, config);
        }
        return indexWriter;
    }
    
    public void closeIndexWriter() throws IOException {
        if (indexWriter != null) {
            indexWriter.close();
        }
    }
    
    public void indexItems(List<Item> items, Map<String, List<String>> itemCategories) throws IOException {
        
        IndexWriter writer = getIndexWriter(true);
        System.out.println("Indexing items: " + items.size());
        
        for (Item item : items) {
            
            Document doc = new Document();
            doc.add(new StringField("item_id", item.getItem_id(), Field.Store.YES));
            doc.add(new StringField("name", item.getName(), Field.Store.YES));
            String fullSearchableText = item.getName() + " "+ item.getDescription();
            for(String category: itemCategories.get(item.item_id)){
                fullSearchableText += (" "+category);
            }
            doc.add(new TextField("content", fullSearchableText, Field.Store.NO));
            writer.addDocument(doc);
        }
        //
        // Don't forget to close the index writer when done
        //
        closeIndexWriter();
    }
    
    public void rebuildIndexes() {
        
        Connection conn = null;
        
        // create a connection to the database to retrieve Items from MySQL
        try {
            conn = DbManager.getConnection(true);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        /*
         * Add your code here to retrieve Items using the connection and add
         * corresponding entries to your Lucene inverted indexes.
         *
         * You will have to use JDBC API to retrieve MySQL data from Java. Read
         * our tutorial on JDBC if you do not know how to use JDBC.
         *
         * You will also have to use Lucene IndexWriter and Document classes to
         * create an index and populate it with Items data. Read our tutorial on
         * Lucene as well if you don't know how.
         *
         * As part of this development, you may want to add new methods and
         * create additional Java classes. If you create new classes, make sure
         * that the classes become part of "edu.ucla.cs.cs144" package and place
         * your class source files at src/edu/ucla/cs/cs144/.
         *
         */
        
        List<Item> items = Item.getItemsFromDB(conn);
        Map<String, List<String>> itemCategories = Item.getItemCategoriesFromDB(conn);
        
        try {
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        try {
            indexItems(items, itemCategories);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public static void main(String args[]) {
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
        System.out.println("Completed rebuilding lucene index");
    }
}
