ALTER TABLE status
  ADD COLUMN popup_choose_magic_apps_seen TINYINT(1) UNSIGNED NOT NULL DEFAULT 1;
UPDATE status
SET popup_choose_magic_apps_seen = 0
WHERE registered = 1;
UPDATE status
SET popup_choose_magic_apps_seen = 1
WHERE registered = 0;

CREATE TABLE MAGIC_APPS_SURVEY (
  id       INT(10) UNSIGNED    NOT NULL AUTO_INCREMENT,
  user_id  INT(10) UNSIGNED    NOT NULL,
  response TINYINT(1) UNSIGNED NOT NULL,
  creationDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  lastUpdateDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);