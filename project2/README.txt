CS144 Project2 - Part B (UID: 004758927)

Q1. The tables and relations they hold are as follows:

(1) Item
		i. item_id  (PRIMARY KEY)
		ii. name
		iii. started
		iv. ends
		v. buy_price
		vi. first_bid
		vii. seller_user_id (FOREIGN KEY referencing table User_seller)
		viii. description
		ix. location
		x. latitude
		xi. longitude
		xii. country

Note: Since the values highest_bid and currently can be calculated when required from Item and Bid tables, they are not stored.

(2) Item_category (PRIMARY KEY is combination of item_id and category)
		i. item_id (FOREIGN KEY referencing table item)
		ii. category

(3) User_Seller
		i. user_id (PRIMARY KEY)
		ii. Seller_rating

(4) User_Bidder
		i. user_id (PRIMARY KEY)
		ii. bidder_rating
		iii. location
		iv. country

(5) Bid (PRIMARY KEY is combination of item_id, user_id, time)
		i. item_id (FOREIGN KEY referencing table Item)
		ii. user_id (FOREIGN KEY referencing table User_bidder)
		iii. time
		iv. amount

=========================================================================================================

Q2. There are no completely nontrivial functional dependencies in above relations excluding those that specify keys.

==========================================================================================================

Q3. Since there are no nontrivial functional dependencies excluding keys, the schema is in BCNF.

===========================================================================================================

Q4. All the above relations are in 4NF.
