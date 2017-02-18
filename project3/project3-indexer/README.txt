We’ve built a Lucene index with following fields:
itemId - String Field
Name - String Field
Combination of Name, Description and Categories - Text Field

Class structure:
Class Item has two methods to fetch data from tables item and item_category respectively.
Class Indexer uses above two methods to populate the index /var/lib/lucene/index-directory
