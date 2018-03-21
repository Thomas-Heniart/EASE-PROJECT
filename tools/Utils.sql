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

CREATE TABLE IF NOT EXISTS tmp1 AS (SELECT
                                      user_id,
                                      DATE_ADD(DATE(CONCAT(year, '-01-01')), INTERVAL (day_of_year - 1)
                                               DAY) AS 'date'
                                    FROM METRIC_CONNECTION
                                    WHERE connected = 1
                                    ORDER BY user_id ASC, date DESC);

SELECT
  USER_PERSONAL_INFORMATION.first_name,
  USER_PERSONAL_INFORMATION.last_name,
  USER_PERSONAL_INFORMATION.phone_number,
  users.email,
  IF(role = b'00000011', 'OWNER', 'MEMBER')   AS role,
  IF(t.clickOnApps IS NULL, 0, t.clickOnApps) AS '#Clicks',
  IF(t1.nbApps IS NULL, 0, t1.nbApps)         AS '#Apps',
  IF(t2.nbCo IS NULL, 0, t2.nbCo)             AS '#Connections',
  teamUsers.arrival_date                      AS '1st co',
  t3.date                                     AS 'last co'
FROM users
  JOIN USER_PERSONAL_INFORMATION ON users.personal_information_id = USER_PERSONAL_INFORMATION.id
  JOIN status ON users.status_id = status.id
  JOIN teamUsers ON users.id = teamUsers.user_id
  JOIN teamUserRoles ON teamUsers.teamUserRole_id = teamUserRoles.id
  JOIN teams ON teamUsers.team_id = teams.id
  JOIN TEAM_ONBOARDING_STATUS ON teams.onboarding_status_id = TEAM_ONBOARDING_STATUS.id
  LEFT JOIN (SELECT
               user_id,
               SUM(weekClicks) AS clickOnApps
             FROM (SELECT
                     user_id,
                     (day_0 + day_1 + day_2 + day_3 + day_4 + day_5 + day_6) AS weekClicks
                   FROM metricClickOnApp
                   ORDER BY user_id) AS t6
             GROUP BY user_id) AS t ON t.user_id = users.id
  LEFT JOIN (SELECT
               user_id,
               COUNT(apps.id) AS nbApps
             FROM apps
               JOIN profiles ON apps.profile_id = profiles.id
             GROUP BY user_id) AS t1 ON users.id = t1.user_id
  LEFT JOIN (SELECT
               user_id,
               COUNT(*) AS nbCo
             FROM METRIC_CONNECTION
             WHERE connected = 1
             GROUP BY user_id) AS t2 ON users.id = t2.user_id
  JOIN (SELECT
          t5.user_id,
          t5.date
        FROM tmp1 AS t5
        WHERE t5.date = (SELECT MAX(t6.date)
                         FROM tmp1 AS t6
                         WHERE t5.user_id = t6.user_id
                         ORDER BY t6.user_id)
        ORDER BY t5.user_id) AS t3 ON t3.user_id = users.id
WHERE status.registered = 1 AND teams.active = 1 AND teamUsers.arrival_date IS NOT NULL AND
      users.email NOT LIKE '%@ease.space' AND users.email NOT LIKE '%@ieseg.fr' AND
      users.email NOT LIKE 'prigent.benjamin@gmail.com' AND users.email NOT LIKE 'fisun.serge76@gmail.com'
      AND USER_PERSONAL_INFORMATION.first_name IS NOT NULL AND USER_PERSONAL_INFORMATION.last_name IS NOT NULL
ORDER BY clickOnApps, nbApps, nbCo
INTO OUTFILE '/tmp/team_users.csv'
FIELDS TERMINATED BY ','
  ENCLOSED BY '"'
LINES TERMINATED BY '\n';

DROP TABLE tmp1;

SELECT
  user_id,
  COUNT(*)
FROM metricClickOnApp
GROUP BY user_id;

SELECT
  user_id,
  COUNT(apps.id)
FROM apps
  JOIN profiles ON apps.profile_id = profiles.id
GROUP BY user_id;

SELECT
  user_id,
  COUNT(*)
FROM METRIC_CONNECTION
GROUP BY user_id;

SELECT
  t5.user_id,
  t5.date
FROM tmp1 AS t5
WHERE t5.date = (SELECT MAX(t6.date)
                 FROM tmp1 AS t6
                 WHERE t5.user_id = t6.user_id
                 ORDER BY t6.user_id)
ORDER BY t5.user_id;

SELECT
  user_id,
  SUM(weekClicks) AS clicks
FROM (SELECT
        user_id,
        (day_0 + day_1 + day_2 + day_3 + day_4 + day_5 + day_6) AS weekClicks
      FROM metricClickOnApp
      ORDER BY user_id) AS t4
GROUP BY user_id;