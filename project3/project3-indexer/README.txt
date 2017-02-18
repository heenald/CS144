We’ve built a Lucene index with following fields:
itemId - String Field
Name - String Field
Combination of Name, Description and Categories - Text Field
Only the first two fields are stored since the last field doesn’t need to be returned to the user, only indexed.

Class structure:
Class Item has two methods to fetch data from tables item and item_category respectively.
Class Indexer uses above two methods to populate the index /var/lib/lucene/index-directory

Task A.3
If the number of results search finds is lesser than numResultsToReturn - numResultsToSkip, it returns an array of that size only (instead of returning an array of size numResultsToReturn and leaving some elements null).
