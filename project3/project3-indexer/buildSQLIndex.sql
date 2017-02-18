CREATE TABLE item_location (
	item_id VARCHAR(20) NOT NULL,
	location POINT NOT NULL,
	PRIMARY KEY (item_id),
	FOREIGN KEY (item_id) REFERENCES item(item_id)
) ENGINE=MyISAM COLLATE latin1_general_cs;

INSERT INTO item_location SELECT item_id, POINT(latitude, longitude) FROM item WHERE latitude IS NOT NULL AND longitude IS NOT NULL;

CREATE SPATIAL INDEX item_location_sp ON item_location(location);