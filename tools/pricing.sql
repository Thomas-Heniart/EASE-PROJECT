ALTER TABLE teamUsers
  CHANGE COLUMN arrivalDate creation_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE teamUsers
  ADD COLUMN arrival_date DATETIME;

UPDATE teamUsers
SET arrival_date = creation_date;

CREATE TABLE TEAM_EMAIL_INVITED (
  id      INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  team_id INT(10) UNSIGNED NOT NULL,
  email   VARCHAR(255)     NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (team_id) REFERENCES teams (id)
);

UPDATE teamSingleCardReceivers SET allowed_to_see_password = 1;