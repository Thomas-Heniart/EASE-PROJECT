DROP TABLE pendingTeamUserVerifications;
ALTER TABLE teamUsers
  ADD COLUMN invitation_code VARCHAR(255);
UPDATE teamUsers t LEFT JOIN pendingTeamInvitations t1 ON t.id = t1.teamUser_id
SET t.invitation_code = t1.code;
DROP TABLE pendingTeamInvitations;

DELETE t1
FROM classicApps AS t1, classicApps AS t2
WHERE t1.website_app_id = t2.website_app_id AND t1.id <> t2.id;

DELETE FROM websiteApps
WHERE type LIKE 'logWithApp' AND id IN (SELECT website_app_id
                                        FROM classicApps);
DELETE FROM logWithApps
WHERE logWith_website_app_id IN (SELECT id
                                 FROM websiteApps
                                 WHERE type LIKE 'classicApp' AND id NOT IN (SELECT website_app_id
                                                                             FROM classicApps));
DELETE FROM websiteApps
WHERE type LIKE 'classicApp' AND id NOT IN (SELECT website_app_id
                                            FROM classicApps);
DELETE FROM classicApps
WHERE website_app_id IN (SELECT id
                         FROM websiteApps
                         WHERE type LIKE 'websiteApp');

DELETE FROM websiteApps
WHERE type LIKE 'websiteApp' AND id IN (SELECT website_app_id
                                        FROM classicApps);
DELETE FROM websiteApps
WHERE type LIKE 'logWithApp' AND id NOT IN (SELECT website_app_id
                                            FROM logWithApps);
DELETE FROM profileAndAppMap
WHERE app_id IN (SELECT id
                 FROM apps
                 WHERE type LIKE 'websiteApp' AND id NOT IN (SELECT app_id
                                                             FROM websiteApps));
DELETE FROM sharedApps
WHERE shareable_app_id IN (SELECT id
                           FROM shareableApps
                           WHERE id IN (SELECT id
                                        FROM apps
                                        WHERE type LIKE 'websiteApp' AND id NOT IN (SELECT app_id
                                                                                    FROM websiteApps)));
DELETE FROM sharedApps
WHERE id IN (SELECT id
             FROM apps
             WHERE type LIKE 'websiteApp' AND id NOT IN (SELECT app_id
                                                         FROM websiteApps));
DELETE FROM shareableApps
WHERE id IN (SELECT id
             FROM apps
             WHERE type LIKE 'websiteApp' AND id NOT IN (SELECT app_id
                                                         FROM websiteApps));
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
SET t.logWith_website_app_id = t1.id;

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