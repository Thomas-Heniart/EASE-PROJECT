CREATE TABLE ACCOUNT_STRENGTH (
  id           INT(10) UNSIGNED    NOT NULL AUTO_INCREMENT,
  pwned        TINYINT(1) UNSIGNED NOT NULL,
  score        TINYINT UNSIGNED,
  hacking_time VARCHAR(255),
  warning      TEXT,
  suggestion   TEXT,
  PRIMARY KEY (id)
);

ALTER TABLE accounts
  ADD COLUMN account_strength_id INT(10) UNSIGNED;
ALTER TABLE accounts
  ADD FOREIGN KEY (account_strength_id) REFERENCES ACCOUNT_STRENGTH (id);

ALTER TABLE accounts
  ADD COLUMN lastPasswordReminderDate DATETIME;