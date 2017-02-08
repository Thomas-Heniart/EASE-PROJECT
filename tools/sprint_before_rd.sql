ALTER TABLE status ADD last_connection DATETIME DEFAULT CURRENT_TIMESTAMP;

CREATE TABLE integrateWebsitesAndUsersMap (
	id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	website_id INT(10) UNSIGNED NOT NULL,
	user_id INT(10) UNSIGNED NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (website_id) REFERENCES websites(id),
	FOREIGN KEY (user_id) REFERENCES users(id)
);

ALTER TABLE passwordLost ADD dateOfRequest DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE options ADD homepage_state TINYINT(1) NOT NULL;

UPDATE options SET homepage_state = 1;

ALTER TABLE status ADD homepage_email_sent TINYINT(1) NOT NULL;

UPDATE status SET homepage_email_sent = 0;