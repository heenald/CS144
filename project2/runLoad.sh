#!/bin/bash

# Run the drop.sql batch file to drop existing tables, if they exist.
mysql -u cs144 CS144 < drop.sql

# Run the create.sql batch file to create the database and tables
mysql -u cs144 CS144 < create.sql

# Compile and run the parser to generate the appropriate load files
ant
#Drop dat files if they exist, else data will be appended
rm -f item.dat item_category.dat user_bidder.dat user_seller.dat bid.dat
ant run-all

#Deduplicates the dat files - sorts and keeps unique
sort user_bidder.dat | uniq > user_bidder_tmp.dat
sort user_seller.dat | uniq > user_seller_tmp.dat
sort item_category.dat | uniq > item_category_tmp.dat
sort item.dat | uniq > item_tmp.dat
sort bid.dat | uniq > bid_tmp.dat
mv user_bidder_tmp.dat user_bidder.dat
mv user_seller_tmp.dat user_seller.dat
mv item_category_tmp.dat item_category.dat
mv item_tmp.dat item.dat
mv bid_tmp.dat bid.dat

# Run the load.sql batch file to load the data
mysql -u cs144 CS144 < load.sql

# Remove all temporary files
rm -f item.dat item_category.dat user_bidder.dat user_seller.dat bid.dat
