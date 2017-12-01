ALTER TABLE linkAppInformations
  MODIFY `img_url` VARCHAR(2000) NOT NULL;

DROP TABLE pendingTeamUserVerifications;
ALTER TABLE teamUsers
  ADD COLUMN invitation_code VARCHAR(255);
UPDATE teamUsers t LEFT JOIN pendingTeamInvitations t1 ON t.id = t1.teamUser_id
SET t.invitation_code = t1.code;
DROP TABLE pendingTeamInvitations;

DELETE t1
FROM classicApps AS t1, classicApps AS t2
WHERE t1.website_app_id = t2.website_app_id AND t1.id <> t2.id;

DELETE FROM classicApps
WHERE website_app_id IN (SELECT id
                         FROM websiteApps
                         WHERE type LIKE 'websiteApp');

DELETE FROM websiteApps
WHERE type LIKE 'websiteApp' AND id IN (SELECT website_app_id
                                        FROM classicApps);
DELETE FROM apps
WHERE type LIKE 'websiteApp' AND id NOT IN (SELECT app_id
                                            FROM websiteApps);
DELETE FROM apps
WHERE type LIKE 'linkApp' AND id NOT IN (SELECT app_id
                                         FROM linkApps);
DELETE FROM appsInformations
WHERE id NOT IN (SELECT app_info_id
                 FROM apps);
DELETE FROM accountsInformations
WHERE account_id IN (SELECT id
                     FROM accounts
                     WHERE id NOT IN (SELECT account_id
                                      FROM classicApps));

DROP TABLE sharedKeys, tmpSharedApps;
DELETE FROM accounts
WHERE id NOT IN (SELECT account_id
                 FROM classicApps);

ALTER TABLE classicApps
  DROP FOREIGN KEY classicapps_ibfk_1;
ALTER TABLE classicApps
  MODIFY id INT(10) UNSIGNED NOT NULL;
ALTER TABLE classicApps
  DROP PRIMARY KEY;
UPDATE classicApps t
  JOIN websiteApps t1 ON t.website_app_id = t1.id
SET t.id = t1.app_id;

ALTER TABLE logWithApps
  DROP FOREIGN KEY logwithapps_ibfk_1;
ALTER TABLE logWithApps
  DROP FOREIGN KEY logwithapps_ibfk_2;
ALTER TABLE logWithApps
  MODIFY id INT(10) UNSIGNED NOT NULL;
ALTER TABLE logWithApps
  DROP PRIMARY KEY;
UPDATE logWithApps t
  JOIN websiteApps t1 ON t.website_app_id = t1.id
SET t.id = t1.app_id;
UPDATE logWithApps t
  JOIN websiteApps t1 ON t.logWith_website_app_id = t1.id
SET t.logWith_website_app_id = t1.app_id;

ALTER TABLE websiteApps
  DROP FOREIGN KEY websiteapps_ibfk_1;
ALTER TABLE websiteApps
  MODIFY id INT(10) UNSIGNED NOT NULL;
ALTER TABLE websiteApps
  DROP PRIMARY KEY;
UPDATE websiteApps
SET id = app_id;

ALTER TABLE websiteApps
  DROP COLUMN app_id;
ALTER TABLE classicApps
  DROP COLUMN website_app_id;
ALTER TABLE logWithApps
  DROP COLUMN website_app_id;
ALTER TABLE websiteApps
  ADD FOREIGN KEY (id) REFERENCES apps (id);
ALTER TABLE classicApps
  ADD FOREIGN KEY (id) REFERENCES websiteApps (id);
ALTER TABLE logWithApps
  ADD FOREIGN KEY (id) REFERENCES websiteApps (id);
ALTER TABLE logWithApps
  ADD FOREIGN KEY (logWith_website_app_id) REFERENCES websiteApps (id);
ALTER TABLE websiteApps
  ADD PRIMARY KEY (id);
ALTER TABLE classicApps
  ADD PRIMARY KEY (id);
ALTER TABLE logWithApps
  ADD PRIMARY KEY (id);

ALTER TABLE linkApps
  DROP FOREIGN KEY linkapps_ibfk_1;
ALTER TABLE linkApps
  MODIFY id INT(10) UNSIGNED NOT NULL;
ALTER TABLE linkApps
  DROP PRIMARY KEY;
UPDATE linkApps
SET id = app_id;
ALTER TABLE linkApps
  DROP COLUMN app_id;
ALTER TABLE linkApps
  ADD FOREIGN KEY (id) REFERENCES apps (id);
ALTER TABLE linkApps
  ADD PRIMARY KEY (id);

ALTER TABLE apps
  ADD COLUMN profile_id INT(10) UNSIGNED;
ALTER TABLE apps
  ADD FOREIGN KEY (profile_id) REFERENCES profiles (id);
ALTER TABLE apps
  ADD COLUMN position INT UNSIGNED;
UPDATE apps a LEFT JOIN profileAndAppMap p ON a.id = p.app_id
SET a.profile_id = p.profile_id, a.position = p.position;
DROP TABLE profileAndAppMap;
ALTER TABLE profileInfo
  DROP COLUMN color;

ALTER TABLE classicApps
  MODIFY account_id INT(10) UNSIGNED;

CREATE TABLE teamCards (
  id            INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  team_id       INT(10) UNSIGNED NOT NULL,
  channel_id    INT(10) UNSIGNED NOT NULL,
  name          VARCHAR(255)     NOT NULL,
  description   VARCHAR(255),
  creation_date DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (team_id) REFERENCES teams (id),
  FOREIGN KEY (channel_id) REFERENCES channels (id)
);

CREATE TABLE teamLinkCards (
  id      INT(10) UNSIGNED NOT NULL,
  url     VARCHAR(2000),
  img_url VARCHAR(2000),
  PRIMARY KEY (id),
  FOREIGN KEY (id) REFERENCES teamCards (id)
);

CREATE TABLE teamWebsiteCards (
  id                         INT(10) UNSIGNED NOT NULL,
  website_id                 INT(10) UNSIGNED NOT NULL,
  password_reminder_interval TINYINT UNSIGNED NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  FOREIGN KEY (id) REFERENCES teamCards (id),
  FOREIGN KEY (website_id) REFERENCES websites (id)
);

CREATE TABLE teamSingleCards (
  id                 INT(10) UNSIGNED NOT NULL,
  account_id         INT(10) UNSIGNED,
  teamUser_filler_id INT(10) UNSIGNED,
  PRIMARY KEY (id),
  FOREIGN KEY (id) REFERENCES teamWebsiteCards (id),
  FOREIGN KEY (account_id) REFERENCES accounts (id),
  FOREIGN KEY (teamUser_filler_id) REFERENCES teamUsers (id)
);

CREATE TABLE teamEnterpriseCards (
  id INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id) REFERENCES teamWebsiteCards (id)
);

DELETE FROM profiles
WHERE column_idx = 0;
DELETE FROM profileInfo
WHERE id NOT IN (SELECT profile_info_id
                 FROM profiles);

INSERT INTO classicApps SELECT
                          id,
                          NULL
                        FROM websiteApps
                        WHERE type = 'websiteApp';

ALTER TABLE apps
  DROP COLUMN type;
ALTER TABLE websiteApps
  DROP COLUMN type;

CREATE TABLE teamCardReceivers (
  id           INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  teamCard_id  INT(10) UNSIGNED NOT NULL,
  teamUser_id  INT(10) UNSIGNED NOT NULL,
  app_id       INT(10) UNSIGNED,
  sharing_date DATETIME         NOT NULL DEFAULT current_timestamp,
  PRIMARY KEY (id),
  FOREIGN KEY (teamCard_id) REFERENCES teamCards (id),
  FOREIGN KEY (teamUser_id) REFERENCES teamUsers (id),
  FOREIGN KEY (app_id) REFERENCES apps (id)
);

CREATE TABLE teamLinkCardReceivers (
  id INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id) REFERENCES teamCardReceivers (id)
);

CREATE TABLE teamEnterpriseCardReceivers (
  id INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id) REFERENCES teamCardReceivers (id)
);

CREATE TABLE teamSingleCardReceivers (
  id                      INT(10) UNSIGNED NOT NULL,
  allowed_to_see_password TINYINT(1)       NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id) REFERENCES teamCardReceivers (id)
);

ALTER TABLE logWithApps
  ADD COLUMN logWithWebsite_id INT(10) UNSIGNED NOT NULL;
UPDATE logWithApps t
  JOIN websiteApps t1 ON t.logWith_website_app_id = t1.id
SET t.logWithWebsite_id = t1.website_id;
ALTER TABLE logWithApps
  ADD FOREIGN KEY (logWithWebsite_id) REFERENCES websites (id);

UPDATE profiles
SET column_idx = column_idx - 1;

INSERT INTO teamCards SELECT
                        shareableApps.id,
                        team_id,
                        channel_id,
                        name,
                        description,
                        NOW()
                      FROM shareableApps
                        JOIN apps ON shareableApps.id = apps.id
                        JOIN appsInformations ON apps.app_info_id = appsInformations.id;
INSERT INTO teamLinkCards SELECT
                            apps.id,
                            linkAppInformations.url,
                            linkAppInformations.img_url
                          FROM shareableApps
                            JOIN apps ON shareableApps.id = apps.id
                            JOIN appsInformations ON apps.app_info_id = appsInformations.id
                            JOIN linkApps ON apps.id = linkApps.id
                            JOIN linkAppInformations ON linkApps.link_app_info_id = linkAppInformations.id;

UPDATE websiteApps SET reminderIntervalValue = 0 WHERE reminderIntervalValue IS NULL;

INSERT INTO teamWebsiteCards SELECT
                               apps.id,
                               websiteApps.website_id,
                               websiteApps.reminderIntervalValue
                             FROM shareableApps
                               JOIN apps ON shareableApps.id = apps.id
                               JOIN appsInformations ON apps.app_info_id = appsInformations.id
                               JOIN websiteApps ON apps.id = websiteApps.id;

INSERT INTO teamEnterpriseCards SELECT apps.id
                                FROM shareableApps
                                  JOIN apps ON shareableApps.id = apps.id
                                  JOIN appsInformations ON apps.app_info_id = appsInformations.id
                                  JOIN websiteApps ON apps.id = websiteApps.id
                                  JOIN classicApps ON websiteApps.id = classicApps.id
                                WHERE classicApps.account_id IS NULL;

INSERT INTO teamSingleCards SELECT
                              apps.id,
                              classicApps.account_id,
                              NULL
                            FROM shareableApps
                              JOIN apps ON shareableApps.id = apps.id
                              JOIN appsInformations ON apps.app_info_id = appsInformations.id
                              JOIN websiteApps ON apps.id = websiteApps.id
                              JOIN classicApps ON websiteApps.id = classicApps.id
                            WHERE classicApps.account_id IS NOT NULL;

INSERT INTO teamCardReceivers SELECT
                                sharedApps.id,
                                sharedApps.shareable_app_id,
                                sharedApps.teamUser_tenant_id,
                                apps.id,
                                NOW()
                              FROM sharedApps
                                JOIN apps ON sharedApps.id = apps.id;

INSERT INTO teamLinkCardReceivers SELECT sharedApps.id
                                  FROM sharedApps
                                    JOIN apps ON sharedApps.id = apps.id
                                    JOIN linkApps ON apps.id = linkApps.id;

INSERT INTO teamEnterpriseCardReceivers SELECT sharedApps.id
                                        FROM sharedApps
                                          JOIN apps ON sharedApps.id = apps.id
                                          JOIN websiteApps ON apps.id = websiteApps.id
                                          JOIN classicApps ON websiteApps.id = classicApps.id
                                          JOIN teamEnterpriseCards
                                            ON sharedApps.shareable_app_id = teamEnterpriseCards.id;

INSERT INTO teamSingleCardReceivers SELECT
                                      sharedApps.id,
                                      sharedApps.canSeeInformation
                                    FROM sharedApps
                                      JOIN apps ON sharedApps.id = apps.id
                                      JOIN websiteApps ON apps.id = websiteApps.id
                                      JOIN classicApps ON websiteApps.id = classicApps.id
                                      JOIN teamSingleCards
                                        ON sharedApps.shareable_app_id = teamSingleCards.id;

DELETE FROM sharedApps;
DELETE FROM classicApps
WHERE id IN (SELECT id
             FROM shareableApps);
DELETE FROM websiteApps
WHERE id IN (SELECT id
             FROM shareableApps);
DELETE FROM linkApps
WHERE id IN (SELECT id
             FROM shareableApps);
DROP TABLE enterpriseAppAttributes, enterpriseAppRequests, pendingJoinAppRequests, sharedApps, shareableApps;
DELETE FROM apps
WHERE id NOT IN (SELECT id
                 FROM websiteApps) AND id NOT IN (SELECT id
                                                  FROM linkApps);

ALTER TABLE websiteApps
  DROP COLUMN reminderIntervalType;
ALTER TABLE websiteApps
  DROP COLUMN reminderIntervalValue;

CREATE TABLE ssoGroups (
  id         INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id    INT(10) UNSIGNED NOT NULL,
  sso_id     INT(10) UNSIGNED NOT NULL,
  account_id INT(10) UNSIGNED,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES users (id),
  FOREIGN KEY (sso_id) REFERENCES sso (id),
  FOREIGN KEY (account_id) REFERENCES accounts (id)
);

CREATE TABLE ssoApps (
  id          INT(10) UNSIGNED NOT NULL,
  ssoGroup_id INT(10) UNSIGNED,
  PRIMARY KEY (id),
  FOREIGN KEY (id) REFERENCES websiteApps (id),
  FOREIGN KEY (ssoGroup_id) REFERENCES ssoGroups (id)
);

CREATE TABLE joinTeamCardRequests (
  id          INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  teamCard_id INT(10) UNSIGNED NOT NULL,
  teamUser_id INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (teamCard_id) REFERENCES teamCards (id),
  FOREIGN KEY (teamUser_id) REFERENCES teamUsers (id)
);

CREATE TABLE joinTeamEnterpriseCardRequests (
  id         INT(10) UNSIGNED NOT NULL,
  account_id INT(10) UNSIGNED,
  PRIMARY KEY (id),
  FOREIGN KEY (id) REFERENCES joinTeamCardRequests (id),
  FOREIGN KEY (account_id) REFERENCES accounts (id)
);

CREATE TABLE joinTeamSingleCardRequests (
  id INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id) REFERENCES joinTeamCardRequests (id)
);

DROP TABLE userAndEmailInvitationsMap;
DROP TABLE usersPrivateExtensions;
DROP TABLE requestedWebsites;

ALTER TABLE users
  ADD COLUMN jwt_id INT(10) UNSIGNED;
ALTER TABLE users
  ADD FOREIGN KEY (jwt_id) REFERENCES jsonWebTokens (id);
ALTER TABLE jsonWebTokens
  DROP FOREIGN KEY jsonWebTokens_ibfk_1;
ALTER TABLE jsonWebTokens
  DROP COLUMN user_id;

ALTER TABLE teamUserStatus
  ADD COLUMN invitation_sent TINYINT(1) NOT NULL;
UPDATE teamUserStatus
SET invitation_sent = 1;

ALTER TABLE teamUsers
  ADD COLUMN profile_id INT(10) UNSIGNED;
ALTER TABLE teamUserStatus
  ADD COLUMN profile_created TINYINT(1) NOT NULL DEFAULT 0;

ALTER TABLE apps
  ADD COLUMN new TINYINT(1) NOT NULL DEFAULT 1;
UPDATE apps
SET new = 0;

INSERT INTO classicApps SELECT
                          id,
                          NULL
                        FROM websiteApps
                        WHERE id NOT IN (SELECT id
                                         FROM classicApps) AND id NOT IN (SELECT id
                                                                          FROM logWithApps);

ALTER TABLE status
  ADD COLUMN new_feature_seen TINYINT(1) NOT NULL DEFAULT 1;
UPDATE status
SET new_feature_seen = 0;

CREATE TABLE pendingTeamUserNotifications (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  teamUser_id INT(10) UNSIGNED NOT NULL,
  content TEXT NOT NULL,
  creation_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  url VARCHAR(255),
  icon VARCHAR(255),
  PRIMARY KEY (id),
  FOREIGN KEY (teamUser_id) REFERENCES teamUsers(id)
);