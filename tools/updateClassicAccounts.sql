DROP TABLE IF EXISTS ClassicAccountsInformations;
DROP TABLE IF EXISTS websitesInformations;

CREATE TABLE ClassicAccountsInformations (
  information_id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  account_id INT(10) UNSIGNED NOT NULL,
  information_name VARCHAR(255) NOT NULL,
  information_value VARCHAR(255) NOT NULL,
  PRIMARY KEY (information_id),
  FOREIGN KEY (account_id) REFERENCES ClassicAccounts (account_id)
);

CREATE TABLE websitesInformations (
  information_id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  website_id INT(10) UNSIGNED NOT NULL,
  information_name VARCHAR(255) NOT NULL,
  information_type VARCHAR(255) NOT NULL,
  PRIMARY KEY (information_id),
  FOREIGN KEY (website_id) REFERENCES websites (website_id)
);

DROP PROCEDURE IF EXISTS updateAccountsInformations;
DROP PROCEDURE IF EXISTS updateWebsitesInformations;

DELIMITER //

CREATE PROCEDURE updateAccountsInformations()
BEGIN
  INSERT INTO ClassicAccountsInformations (account_id, information_name, information_value)
    SELECT account_id, "login", login FROM ClassicAccounts;
  INSERT INTO ClassicAccountsInformations (account_id, information_name, information_value)
    SELECT account_id, "password", password FROM ClassicAccounts;
  ALTER TABLE ClassicAccounts DROP COLUMN login;
  ALTER TABLE ClassicAccounts DROP COLUMN password;
END //

CREATE PROCEDURE updateWebsitesInformations()
BEGIN
  INSERT INTO websitesInformations (website_id, information_name, information_type)
    SELECT website_id, "login", "text" FROM websites;
  INSERT INTO websitesInformations (website_id, information_name, information_type)
    SELECT website_id, "password", "password" FROM websites;
END //

DELIMITER ;
