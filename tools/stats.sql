CREATE TABLE WEEKLY_STATS (
  id               INT(10) UNSIGNED    NOT NULL AUTO_INCREMENT,
  year             INT(10) UNSIGNED    NOT NULL,
  week_of_year     TINYINT(3) UNSIGNED NOT NULL,
  new_companies    INT(10) UNSIGNED    NOT NULL,
  new_users        INT(10) UNSIGNED    NOT NULL,
  new_apps         INT(10) UNSIGNED    NOT NULL,
  new_team_apps    INT(10) UNSIGNED    NOT NULL,
  passwords_killed INT(10) UNSIGNED    NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE WEEKLY_STATS
  ADD COLUMN active_users INT(10) UNSIGNED NOT NULL;
ALTER TABLE WEEKLY_STATS
  ADD COLUMN active_teams INT(10) UNSIGNED NOT NULL;

ALTER TABLE metricClickOnApp
  ADD COLUMN user_id INT(10);
UPDATE metricClickOnApp
SET user_id = (SELECT users.id
               FROM users
                 JOIN profiles ON users.id = profiles.user_id
                 JOIN apps ON profiles.id = apps.profile_id
               WHERE app_id = apps.id);