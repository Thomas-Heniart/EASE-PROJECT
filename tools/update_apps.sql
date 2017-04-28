DROP TABLE IF EXISTS profileAndAppMap, userAndProfileMap;

CREATE TABLE profileAndAppMap (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  profile_id INT(10) UNSIGNED NOT NULL,
  app_id INT(10) UNSIGNED NOT NULL,
  position TINYINT(3) UNSIGNED NOT NULL,
  PRIMARY KEY(id),
  FOREIGN KEY (profile_id) REFERENCES profiles(id),
  FOREIGN KEY (app_id) REFERENCES apps(id)
);

INSERT INTO profileAndAppMap
    SELECT NULL, profile_id, id, position FROM apps;

ALTER TABLE apps DROP FOREIGN KEY apps_ibfk_1;
ALTER TABLE apps DROP COLUMN profile_id;
ALTER TABLE apps DROP COLUMN position;

CREATE TABLE userAndProfileMap (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id INT(10) UNSIGNED NOT NULL,
  profile_id INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY(id),
  FOREIGN KEY (profile_id) REFERENCES profiles(id),
  FOREIGN KEY (user_id) REFERENCES users(id)
);

INSERT INTO userAndProfileMap
    SELECT NULL, user_id, id FROM profiles;

ALTER TABLE profiles DROP FOREIGN KEY profiles_ibfk_1;
ALTER TABLE profiles DROP COLUMN user_id;

ALTER TABLE linkApps
    MODIFY COLUMN id INT(10) UNSIGNED NOT NULL;

UPDATE linkApps SET id = app_id;

ALTER TABLE linkApps
    ADD FOREIGN KEY (id) REFERENCES apps(id);

ALTER TABLE linkApps DROP FOREIGN KEY linkapps_ibfk_1;

ALTER TABLE linkApps DROP COLUMN app_id;


/* Classic apps start */

DROP TABLE IF EXISTS updateNewPassword;

ALTER TABLE classicApps MODIFY COLUMN id INT(10) UNSIGNED NOT NULL;

ALTER TABLE classicApps DROP PRIMARY KEY;

UPDATE classicApps ca
  JOIN websiteApps wa ON ca.website_app_id = wa.id
SET ca.id = wa.app_id;

ALTER TABLE classicApps ADD PRIMARY KEY(id);

ALTER TABLE classicApps DROP FOREIGN KEY classicapps_ibfk_1;

/* Classic apps break for logwithApps */

DROP TABLE IF EXISTS updatenewlogwithapp;

ALTER TABLE logwithApps MODIFY COLUMN id INT(10) UNSIGNED NOT NULL;

ALTER TABLE logwithApps DROP PRIMARY KEY;

ALTER TABLE logwithApps DROP FOREIGN KEY logwithapps_ibfk_1;

ALTER TABLE logwithApps DROP FOREIGN KEY logwithapps_ibfk_2;

UPDATE logWithApps la
  JOIN websiteApps wa ON la.website_app_id = wa.id
SET la.id = wa.app_id;

UPDATE logWithApps la
  JOIN websiteApps wa ON la.logWith_website_app_id = wa.id
SET la.logWith_website_app_id = wa.app_id;

ALTER TABLE logwithApps ADD PRIMARY KEY(id);

/* logwithApps break for websiteApps */

ALTER TABLE websiteApps MODIFY COLUMN id INT(10) UNSIGNED NOT NULL;

ALTER TABLE websiteApps DROP PRIMARY KEY;

UPDATE websiteApps SET id = app_id;

ALTER TABLE websiteApps DROP FOREIGN KEY websiteapps_ibfk_1;

ALTER TABLE websiteApps DROP COLUMN app_id;

ALTER TABLE websiteApps ADD PRIMARY KEY(id);

ALTER TABLE websiteApps ADD FOREIGN KEY (id) REFERENCES apps(id);

/* Back to classic apps and logwithApps */

ALTER TABLE classicApps ADD FOREIGN KEY (id) REFERENCES websiteApps(id);

ALTER TABLE classicApps DROP COLUMN website_app_id;

ALTER TABLE logwithApps ADD FOREIGN KEY (id) REFERENCES websiteApps(id);

ALTER TABLE logwithApps ADD FOREIGN KEY (logWith_website_app_id) REFERENCES websiteApps(id);

ALTER TABLE logwithApps DROP COLUMN website_app_id;

/* Backup tables */

CREATE TABLE `updateNewPassword` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `update_id` int(10) unsigned NOT NULL,
  `classic_app_id` int(10) unsigned NOT NULL,
  `new_password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `update_id` (`update_id`),
  KEY `classic_app_id` (`classic_app_id`),
  CONSTRAINT `updateNewPassword_ibfk_1` FOREIGN KEY (`update_id`) REFERENCES `updates` (`id`),
  CONSTRAINT `updateNewPassword_ibfk_2` FOREIGN KEY (`classic_app_id`) REFERENCES `classicApps` (`id`)
);

CREATE TABLE `updatenewlogwithapp` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `update_new_account_id` int(10) unsigned NOT NULL,
  `logWith_app_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `update_new_account_id` (`update_new_account_id`),
  KEY `logWith_app_id` (`logWith_app_id`),
  CONSTRAINT `updateNewLogWithApp_ibfk_1` FOREIGN KEY (`update_new_account_id`) REFERENCES `updateNewAccount` (`id`),
  CONSTRAINT `updateNewLogWithApp_ibfk_2` FOREIGN KEY (`logWith_app_id`) REFERENCES `logWithApps` (`id`)
);

/*
select table_name
from information_schema.KEY_COLUMN_USAGE
where table_schema = 'ease'
and referenced_table_name = 'classicApps';

SELECT id FROM apps WHERE id IN (SELECT app_id FROM websiteApps WHERE id IN (SELECT website_app_id FROM classicApps));

SELECT id FROM classicApps GROUP BY id HAVING Count(*) > 1;
*/