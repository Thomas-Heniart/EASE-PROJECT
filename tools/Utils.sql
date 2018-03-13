USE information_schema;
SELECT *
FROM
  KEY_COLUMN_USAGE
WHERE
  REFERENCED_TABLE_NAME = 'users'
  AND REFERENCED_COLUMN_NAME = 'id';
USE ease;

SELECT DISTINCT *
FROM websiteApps t1
WHERE EXISTS(
    SELECT *
    FROM websiteApps t2
    WHERE t1.id <> t2.id
          AND t1.app_id = t2.app_id);

SELECT DISTINCT *
FROM classicApps t1
WHERE EXISTS(
    SELECT *
    FROM classicApps t2
    WHERE t1.id <> t2.id
          AND t1.website_app_id = t2.website_app_id);

SELECT DISTINCT *
FROM logWithApps t1
WHERE EXISTS(
    SELECT *
    FROM logWithApps t2
    WHERE t1.id <> t2.id
          AND t1.website_app_id = t2.website_app_id);

SELECT DISTINCT *
FROM classicApps t1
WHERE EXISTS(
    SELECT *
    FROM classicApps t2
    WHERE t1.id <> t2.id
          AND t1.account_id = t2.account_id);

SELECT DISTINCT *
FROM apps t1
WHERE EXISTS(
    SELECT *
    FROM apps t2
    WHERE t1.id <> t2.id
          AND t1.app_info_id = t2.app_info_id);

DELETE t1
FROM classicApps AS t1, classicApps AS t2
WHERE t1.website_app_id = t2.website_app_id;

SELECT default_character_set_name
FROM information_schema.SCHEMATA
WHERE schema_name = "ease";


SELECT CCSA.character_set_name
FROM information_schema.`TABLES` T,
  information_schema.`COLLATION_CHARACTER_SET_APPLICABILITY` CCSA
WHERE CCSA.collation_name = T.table_collation
      AND T.table_schema = "ease"
      AND T.table_name = "teamUsers";


ALTER DATABASE ease
CHARACTER SET utf8
COLLATE utf8_unicode_ci;

CREATE TABLE `logs` (
  `servlet_name` VARCHAR(255)     NOT NULL,
  `code`         INT(10) UNSIGNED NOT NULL,
  `user_id`      INT(10) UNSIGNED          DEFAULT NULL,
  `args`         TEXT             NOT NULL,
  `retMsg`       TEXT             NOT NULL,
  `date`         DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP
);

UPDATE status
SET new_feature_seen = 0;

SELECT
  first_name,
  last_name,
  teamUsers.email,
  phone_number,
  'benjamin@ease.space'
FROM teamUsers
  JOIN users ON teamUsers.user_id = users.id
  JOIN USER_PERSONAL_INFORMATION ON users.personal_information_id = USER_PERSONAL_INFORMATION.id
  JOIN teamUserRoles ON teamUsers.teamUserRole_id = teamUserRoles.id
  JOIN teams ON teamUsers.team_id = teams.id
WHERE teams.active = 1 AND teamUsers.email NOT LIKE '%@ease.space'
INTO OUTFILE '/tmp/contacts_sample_import.csv'
FIELDS TERMINATED BY ','
  ENCLOSED BY '"'
LINES TERMINATED BY '\n';

SELECT
  SUBSTRING_INDEX(teamUsers.email, '@', -1) AS domain,
  name,
  phone_number
FROM teamUsers
  JOIN users ON teamUsers.user_id = users.id
  JOIN USER_PERSONAL_INFORMATION ON users.personal_information_id = USER_PERSONAL_INFORMATION.id
  JOIN teamUserRoles ON teamUsers.teamUserRole_id = teamUserRoles.id
  JOIN teams ON teamUsers.team_id = teams.id
WHERE role = b'00000011' AND teams.active = 1 AND teamUsers.email NOT LIKE '%@ease.space'
INTO OUTFILE '/tmp/companies.csv'
FIELDS TERMINATED BY ','
  ENCLOSED BY '"'
LINES TERMINATED BY '\n';

