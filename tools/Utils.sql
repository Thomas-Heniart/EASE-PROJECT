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
  IF(t3.date IS NULL, 'NEVER', t3.date)       AS 'last co'
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
  LEFT JOIN (SELECT
               user_id,
               MAX(date1) AS date
             FROM (SELECT
                     user_id,
                     DATE_ADD(DATE(CONCAT(year, '-01-01')), INTERVAL (day_of_year - 1)
                              DAY) AS 'date1'
                   FROM METRIC_CONNECTION
                   WHERE connected = 1
                   ORDER BY user_id ASC, date1 DESC) AS t4
             GROUP BY user_id) AS t3 ON t3.user_id = users.id
WHERE status.registered = 1 AND teams.active = 1 AND teamUsers.arrival_date IS NOT NULL AND
      users.email NOT LIKE '%@ease.space' AND users.email NOT LIKE '%@ieseg.fr' AND
      users.email NOT LIKE 'prigent.benjamin@gmail.com' AND users.email NOT LIKE 'fisun.serge76@gmail.com'
      AND USER_PERSONAL_INFORMATION.first_name IS NOT NULL AND USER_PERSONAL_INFORMATION.last_name IS NOT NULL
ORDER BY clickOnApps, nbApps, nbCo
INTO OUTFILE '/tmp/people.csv'
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

SELECT
  user_id,
  MAX(date)
FROM (SELECT
        user_id,
        DATE_ADD(DATE(CONCAT(year, '-01-01')), INTERVAL (day_of_year - 1)
                 DAY) AS 'date'
      FROM METRIC_CONNECTION
      WHERE connected = 1
      ORDER BY user_id ASC, date DESC) AS t
GROUP BY user_id;

SELECT
  year,
  day_of_year,
  COUNT(*) AS clicks
FROM ease_tracking.EASE_EVENT
WHERE (name LIKE 'PasswordUsed' OR name LIKE 'PasswordUser' AND user_id IN (SELECT user_id
                                                                            FROM ease.teamUsers
                                                                            WHERE team_id = 312 AND user_id IS NOT NULL)
       GROUP BY YEAR, day_of_year
       ORDER BY YEAR, day_of_year;

SELECT *
FROM ease_tracking.EASE_EVENT
WHERE name LIKE 'PasswordUsed' OR name LIKE 'PasswordUser' AND user_id IN (SELECT user_id
                                                                           FROM ease.teamUsers
                                                                           WHERE team_id = 312 AND user_id IS NOT NULL);

SELECT
  year,
  day_of_year,
  COUNT(*) AS clicks
FROM (
       SELECT
         year,
         day_of_year,
         id
       FROM ease_tracking.EASE_EVENT
       WHERE name LIKE ('PasswordUsed' OR name LIKE 'PasswordUser') AND user_id = 3411) AS t
GROUP BY year, day_of_year
ORDER BY year, day_of_year;

SELECT COUNT(*)
FROM (SELECT
        year,
        day_of_year,
        COUNT(*) AS clicks
      FROM (
             SELECT
               year,
               day_of_year,
               week_of_year,
               id
             FROM ease_tracking.EASE_EVENT
             WHERE (name LIKE 'PasswordUsed' OR name LIKE 'PasswordUser') AND user_id = 17) AS t
      WHERE year = 2018 AND week_of_year = 16
      GROUP BY year, day_of_year, week_of_year
      ORDER BY year, day_of_year) AS t;
SELECT *
FROM (SELECT
        team_id,
        COUNT(*) AS apps
      FROM teamCards
      GROUP BY team_id) AS t
WHERE t.apps BETWEEN 0 AND 10;

SELECT
  invitation_sent,
  COUNT(team_id) AS sum
FROM (
       SELECT
         invitation_sent,
         team_id,
         COUNT(*) AS people
       FROM teamUsers
         JOIN teamUserStatus ON teamUsers.status_id = teamUserStatus.id
       GROUP BY invitation_sent, team_id) AS t
WHERE people BETWEEN 10 AND 20
GROUP BY invitation_sent;

SELECT
  teamUsers.team_id,
  COUNT(*) AS passwordUsed
FROM ease_tracking.EASE_EVENT
  JOIN teamUsers ON ease_tracking.EASE_EVENT.user_id = teamUsers.user_id
WHERE name LIKE 'PasswordUsed' AND
      DATE(ease_tracking.EASE_EVENT.creation_date) BETWEEN DATE_SUB(CURDATE(), INTERVAL 1 WEEK) AND CURDATE()
GROUP BY team_id;

SELECT *
FROM teams
  JOIN TEAM_ONBOARDING_STATUS ON teams.onboarding_status_id = TEAM_ONBOARDING_STATUS.id
WHERE step = 5;

SELECT
  users.id,
  email,
  registration_date,
  t.n,
  MAX(year),
  MAX(day_of_year),
  COUNT(EASE_UPDATE.id) AS updates
FROM EASE_UPDATE
  JOIN users ON users.id = EASE_UPDATE.user_id
  JOIN (SELECT
          user_id,
          COUNT(*) AS n
        FROM METRIC_CONNECTION
        GROUP BY user_id) AS t ON t.user_id = users.id
  JOIN METRIC_CONNECTION ON METRIC_CONNECTION.user_id = users.id AND connected = 1
GROUP BY EASE_UPDATE.user_id
ORDER BY updates
INTO OUTFILE '/tmp/updates.csv'
FIELDS TERMINATED BY ','
  ENCLOSED BY '"'
LINES TERMINATED BY '\n';

SELECT
  email,
  registration_date,
  t.n AS nb_of_co,
  DATE_ADD(DATE(concat(t.year, '-01-01')), INTERVAL (t.day_of_year - 1) DAY) AS last_connection,
  COUNT(*) AS updates
FROM EASE_UPDATE
  JOIN users ON EASE_UPDATE.user_id = users.id
  JOIN (SELECT
          user_id,
          MAX(year)        AS year,
          MAX(day_of_year) AS day_of_year,
          COUNT(*)         AS n
        FROM METRIC_CONNECTION
        WHERE connected = 1
        GROUP BY user_id) AS t ON t.user_id = users.id
GROUP BY EASE_UPDATE.user_id
ORDER BY updates
INTO OUTFILE '/tmp/updates.csv'
FIELDS TERMINATED BY ','
  ENCLOSED BY '"'
LINES TERMINATED BY '\n';