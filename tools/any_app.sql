CREATE TABLE anyApps (
  id INT(10) UNSIGNED NOT NULL,
  account_id INT(10) UNSIGNED,
  PRIMARY KEY (id),
  FOREIGN KEY (id) REFERENCES websiteApps(id),
  FOREIGN KEY (account_id) REFERENCES accounts(id)
);

ALTER TABLE websiteAttributes ADD COLUMN logo_url VARCHAR(2000);

CREATE TABLE software (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  folder VARCHAR(255) NOT NULL,
  logo_url VARCHAR(2000),
  PRIMARY KEY (id)
);

CREATE TABLE softwareApps (
  id INT(10) UNSIGNED NOT NULL,
  software_id INT(10) UNSIGNED NOT NULL,
  account_id INT(10) UNSIGNED,
  PRIMARY KEY (id),
  FOREIGN KEY (id) REFERENCES apps(id),
  FOREIGN KEY (software_id) REFERENCES software(id),
  FOREIGN KEY (account_id) REFERENCES accounts(id)
);

CREATE TABLE softwareConnectionInformation (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  software_id INT(10) UNSIGNED NOT NULL,
  information_name VARCHAR(255) NOT NULL,
  information_type VARCHAR(255) NOT NULL,
  priority TINYINT UNSIGNED NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (software_id) REFERENCES software(id)
);