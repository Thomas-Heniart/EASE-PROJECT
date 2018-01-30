CREATE TABLE USER_PERSONAL_INFORMATION (
  id           INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  first_name   VARCHAR(255)     NOT NULL DEFAULT '',
  last_name    VARCHAR(255)     NOT NULL DEFAULT '',
  phone_number VARCHAR(255)     NOT NULL DEFAULT '',
  PRIMARY KEY (id)
);

INSERT INTO USER_PERSONAL_INFORMATION SELECT
                                        id,
                                        '',
                                        '',
                                        ''
                                      FROM users;

ALTER TABLE users
  ADD COLUMN personal_information_id INT(10) UNSIGNED NOT NULL;
UPDATE users
SET personal_information_id = id;

UPDATE USER_PERSONAL_INFORMATION upi
  JOIN users u ON u.personal_information_id = upi.id
  JOIN teamUsers tU ON u.id = tU.user_id
SET upi.first_name = tU.firstName, upi.last_name = tU.lastName;

UPDATE USER_PERSONAL_INFORMATION upi
  JOIN users u ON u.personal_information_id = upi.id
  JOIN teamUsers tU ON u.id = tU.user_id
SET upi.phone_number = tU.phone_number
WHERE tU.phone_number IS NOT NULL;

SET FOREIGN_KEY_CHECKS = 0;
ALTER TABLE users
  ADD FOREIGN KEY (personal_information_id) REFERENCES USER_PERSONAL_INFORMATION (id);
SET FOREIGN_KEY_CHECKS = 1;

ALTER TABLE teamUsers
  DROP COLUMN firstName;
ALTER TABLE teamUsers
  DROP COLUMN lastName;
ALTER TABLE teamUsers
  DROP COLUMN jobTitle;
ALTER TABLE teamUsers
  DROP COLUMN phone_number;

ALTER TABLE teams
  ADD COLUMN company_size INT(10) UNSIGNED NOT NULL;
UPDATE teams t
SET company_size = (SELECT COUNT(*)
                    FROM teamUsers
                    WHERE team_id = t.id);

CREATE TABLE TEAM_ONBOARDING_STATUS (
  id   INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  step TINYINT UNSIGNED NOT NULL DEFAULT 0,
  PRIMARY KEY (id)
);

ALTER TABLE teams
  ADD COLUMN onboarding_status_id INT(10) UNSIGNED NOT NULL;

INSERT INTO TEAM_ONBOARDING_STATUS SELECT
                                     id,
                                     0
                                   FROM teams;
UPDATE teams
SET onboarding_status_id = id;

SET FOREIGN_KEY_CHECKS = 0;
ALTER TABLE teams
  ADD FOREIGN KEY (onboarding_status_id) REFERENCES TEAM_ONBOARDING_STATUS (id);
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE ONBOARDING_ROOM (
  id      INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  name    VARCHAR(255)     NOT NULL,
  example VARCHAR(255)     NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE ONBOARDING_ROOM_WEBSITE (
  id                 INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  onboarding_room_id INT(10) UNSIGNED NOT NULL,
  website_id         INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (onboarding_room_id) REFERENCES ONBOARDING_ROOM (id),
  FOREIGN KEY (website_id) REFERENCES websites (id),
  UNIQUE (onboarding_room_id, website_id)
);

ALTER TABLE users
  CHANGE COLUMN firstName username VARCHAR(255) NOT NULL;

ALTER TABLE status
  ADD COLUMN onboarding_step TINYINT UNSIGNED NOT NULL;
UPDATE status
SET onboarding_step = 2;

ALTER TABLE userPendingRegistrations
  ADD COLUMN newsletter TINYINT(1) UNSIGNED NOT NULL DEFAULT 1;
UPDATE userPendingRegistrations
SET newsletter = 1;

ALTER TABLE teams
  ADD COLUMN invitations_sent TINYINT(1) NOT NULL DEFAULT 0;
UPDATE teams
SET invitations_sent = 0;
UPDATE teams
SET invitations_sent = 1
WHERE id IN (SELECT DISTINCT team_id
             FROM teamUsers
             WHERE state > 0);

ALTER TABLE status
  ADD COLUMN tip_importation_seen TINYINT(1) UNSIGNED NOT NULL DEFAULT 0;
UPDATE status
SET tip_importation_seen = 0;