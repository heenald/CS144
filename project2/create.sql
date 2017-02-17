CREATE TABLE user_seller(
	user_id VARCHAR(100) NOT NULL,
	seller_rating INTEGER NOT NULL,
	PRIMARY KEY(user_id)
);

CREATE TABLE user_bidder(
	user_id VARCHAR(100) NOT NULL,
	bidder_rating INTEGER NOT NULL,
	location VARCHAR(100),
	country VARCHAR(100),
	PRIMARY KEY(user_id)
);

CREATE TABLE item(
	item_id VARCHAR(20) NOT NULL,
	name VARCHAR(200) NOT NULL,
	started TIMESTAMP NOT NULL,
	ends TIMESTAMP NOT NULL,
	currently DECIMAL(8,2) NOT NULL,
	buy_price DECIMAL(8,2),
	first_bid DECIMAL(8,2) NOT NULL,
	seller_user_id VARCHAR(100) NOT NULL,
	description VARCHAR(4000) NOT NULL,
	location VARCHAR(200) NOT NULL,
	latitude VARCHAR(100),
	longitude VARCHAR(100),
	country VARCHAR(100) NOT NULL,
	PRIMARY KEY(item_id),
	FOREIGN KEY fk_seller(seller_user_id) REFERENCES user_seller(user_id)
);

CREATE TABLE item_category (
	item_id VARCHAR(20) NOT NULL,
	category VARCHAR(100) NOT NULL,
	PRIMARY KEY(item_id, category),
	FOREIGN KEY fk_item(item_id) REFERENCES item(item_id)
);

CREATE TABLE bid(
	item_id VARCHAR(20) NOT NULL,
	bidder_user_id VARCHAR(100) NOT NULL,
	time TIMESTAMP NOT NULL,
	amount DECIMAL(8,2) NOT NULL,
	PRIMARY KEY(item_id, bidder_user_id, time),
	FOREIGN KEY fk_item(item_id) REFERENCES item(item_id),
	FOREIGN KEY fk_bidder(bidder_user_id) REFERENCES user_bidder(user_id)
);

