DROP TABLE IF EXISTS ClassicAccountsInformations;
DROP TABLE IF EXISTS websitesInformations;

CREATE TABLE ClassicAccountsInformations (
  information_id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  account_id INT(10) UNSIGNED NOT NULL,
  information_name VARCHAR(255) NOT NULL,
  information_value VARCHAR(255) NOT NULL,
  PRIMARY KEY (information_id),
  FOREIGN KEY (account_id) REFERENCES classicAccounts (account_id)
);

CREATE TABLE websitesInformations (
  information_id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  website_id INT(10) UNSIGNED NOT NULL,
  information_name VARCHAR(255) NOT NULL,
  information_type VARCHAR(255) NOT NULL,
  PRIMARY KEY (information_id),
  FOREIGN KEY (website_id) REFERENCES websites (website_id)
);

DELIMITER //

CREATE PROCEDURE updateAccountsInformations2()
BEGIN
  INSERT INTO ClassicAccountsInformations (account_id, information_name, information_value)
    SELECT account_id, "login", login FROM classicAccounts;
  INSERT INTO ClassicAccountsInformations (account_id, information_name, information_value)
    SELECT account_id, "password", password FROM classicAccounts;
  ALTER TABLE classicAccounts DROP COLUMN login;
  ALTER TABLE classicAccounts DROP COLUMN password;
END //

CREATE PROCEDURE updateWebsitesInformations2()
BEGIN
  INSERT INTO websitesInformations (website_id, information_name, information_type)
    SELECT website_id, "login", "text" FROM websites;
  INSERT INTO websitesInformations (website_id, information_name, information_type)
    SELECT website_id, "password", "password" FROM websites;
END //

DELIMITER ;
