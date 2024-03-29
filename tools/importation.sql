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

/*
ALTER TABLE users DROP FOREIGN KEY `users_ibfk_5`;
ALTER TABLE users DROP COLUMN post_registration_emails_id;
DROP TABLE USER_POST_REGISTRATION_EMAILS;
*/

CREATE TABLE USER_POST_REGISTRATION_EMAILS (
  id                                    INT(10) UNSIGNED    NOT NULL AUTO_INCREMENT,
  email_j2_sent                         TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
  email_j4_sent                         TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
  email_j6_sent                         TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
  email_j13_sent                        TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
  email_team_creation_sent              TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
  email_use_seven_on_fourteen_days_sent TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
  PRIMARY KEY (id)
);

ALTER TABLE users
  ADD COLUMN post_registration_emails_id INT(10) UNSIGNED;
INSERT INTO USER_POST_REGISTRATION_EMAILS SELECT
                                            id,
                                            0,
                                            0,
                                            0,
                                            0,
                                            0,
                                            0
                                          FROM users;
UPDATE users
SET post_registration_emails_id = id;
SET FOREIGN_KEY_CHECKS = 0;
ALTER TABLE users
  ADD FOREIGN KEY (post_registration_emails_id) REFERENCES USER_POST_REGISTRATION_EMAILS (id);
SET FOREIGN_KEY_CHECKS = 1;
CREATE TABLE METRIC_CONNECTION (
  id          INT(10) UNSIGNED    NOT NULL AUTO_INCREMENT,
  user_id     INT(10) UNSIGNED    NOT NULL,
  year        INT(10) UNSIGNED    NOT NULL,
  day_of_year MEDIUMINT UNSIGNED  NOT NULL,
  connected   TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
  PRIMARY KEY (id)
);

/* FOR PROD */

CREATE TABLE WEBSITE_ALTERNATIVE_URL (
  id         INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  website_id INT(10) UNSIGNED NOT NULL,
  url        VARCHAR(2000)    NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (website_id) REFERENCES websites (id)
);

SET FOREIGN_KEY_CHECKS = 0;
ALTER TABLE teamUsers
  ADD UNIQUE (team_id, username);
SET FOREIGN_KEY_CHECKS = 1;

SELECT DISTINCT *
FROM teamUsers t1
WHERE EXISTS(
    SELECT *
    FROM teamUsers t2
    WHERE t1.ID <> t2.ID
          AND t1.team_id = t2.team_id
          AND t1.username = t2.username);