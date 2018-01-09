DROP TABLE IMPORTED_ACCOUNT_INFORMATION, IMPORTED_ACCOUNT;

ALTER TABLE websites
  ADD COLUMN logo_version TINYINT UNSIGNED NOT NULL DEFAULT 0;
ALTER TABLE software
  ADD COLUMN logo_version TINYINT UNSIGNED NOT NULL DEFAULT 0;

CREATE TABLE IMPORTED_ACCOUNT (
  id         INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  name       VARCHAR(255)     NOT NULL,
  url        VARCHAR(2000),
  website_id INT(10) UNSIGNED,
  user_id    INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (website_id) REFERENCES websites (id),
  FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IMPORTED_ACCOUNT_INFORMATION (
  id                  INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  name                VARCHAR(255)     NOT NULL,
  value               VARCHAR(255)     NOT NULL,
  imported_account_id INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (imported_account_id) REFERENCES IMPORTED_ACCOUNT (id)
);

ALTER TABLE metricTeam
  ADD COLUMN cards_names TEXT NOT NULL;
ALTER TABLE metricTeam
  ADD COLUMN cards_with_receiver_names TEXT NOT NULL;
ALTER TABLE metricTeam
  ADD COLUMN cards_with_receiver_and_password_policy_names TEXT NOT NULL;
ALTER TABLE metricTeam
  ADD COLUMN single_cards_names TEXT NOT NULL;
ALTER TABLE metricTeam
  ADD COLUMN enterprise_cards_names TEXT NOT NULL;
ALTER TABLE metricTeam
  ADD COLUMN link_cards_names TEXT NOT NULL;

ALTER TABLE metricClickOnApp
  ADD COLUMN team_id INT(10) UNSIGNED;