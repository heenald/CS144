LOAD DATA LOCAL INFILE 'user_seller.dat' INTO TABLE user_seller FIELDS TERMINATED BY ',' ENCLOSED BY '"' ESCAPED BY '\\';
LOAD DATA LOCAL INFILE 'user_bidder.dat' INTO TABLE user_bidder FIELDS TERMINATED BY ',' ENCLOSED BY '"' ESCAPED BY '\\';
LOAD DATA LOCAL INFILE 'item.dat' INTO TABLE item FIELDS TERMINATED BY ',' ENCLOSED BY '"' ESCAPED BY '\\';
LOAD DATA LOCAL INFILE 'item_category.dat' INTO TABLE item_category FIELDS TERMINATED BY ',' ENCLOSED BY '"' ESCAPED BY '\\';
LOAD DATA LOCAL INFILE 'bid.dat' INTO TABLE bid FIELDS TERMINATED BY ',' ENCLOSED BY '"' ESCAPED BY '\\';