CREATE TABLE anyApps (
  id INT(10) UNSIGNED NOT NULL,
  account_id INT(10) UNSIGNED,
  PRIMARY KEY (id),
  FOREIGN KEY (id) REFERENCES websiteApps(id),
  FOREIGN KEY (account_id) REFERENCES accounts(id)
);

ALTER TABLE websiteAttributes ADD COLUMN logo_url VARCHAR(2000);