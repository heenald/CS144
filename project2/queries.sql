#Find the number of users in the database.
SELECT COUNT( *) FROM(
	SELECT DISTINCT user_id from user_seller 
	UNION
	SELECT DISTINCT user_id from user_bidder
) T;

#Find the number of items in "New York", (i.e., items whose location is exactly the string "New York")
SELECT COUNT(*) FROM
item WHERE location COLLATE latin1_general_cs = "New York";

#Find the number of auctions belonging to exactly four categories.
SELECT COUNT(*) FROM(
	SELECT COUNT(*) AS categories_per_item
	FROM item_category
	GROUP BY item_id) T
WHERE categories_per_item = 4;

#Find the ID(s) of current (unsold) auction(s) with the highest bid.
SELECT bid.item_id AS ITEMID FROM bid INNER JOIN item ON bid.item_id = item.item_id
WHERE ENDS >= '2001-12-20 00:00:01' AND amount = (SELECT MAX(amount) from bid);

#Find the number of sellers whose rating is higher than 1000.
SELECT COUNT(*) FROM user_seller
WHERE seller_rating > 1000;


#Find the number of users who are both sellers and bidders.
SELECT COUNT(*) FROM 
user_seller INNER JOIN user_bidder
ON user_seller.user_id = user_bidder.user_id;


#Find the number of categories that include at least one item with a bid of more than $100.
SELECT COUNT(DISTINCT category) FROM
item_category INNER JOIN bid ON item_category.item_id = bid.item_id
WHERE bid.amount > 100;
