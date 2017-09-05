/* ALTER TABLE teamUsers ADD COLUMN jobTitle VARCHAR(100);

ALTER TABLE sharedApps ADD COLUMN adminHasAccess TINYINT(1) NOT NULL;

CREATE TABLE teamUserRoles (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  role BIT(8) NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE 

INSERT INTO teamUserRoles
  SELECT * FROM teamUserPermissions;

ALTER TABLE teamUsers CHANGE permissions_id teamUserRole_id INT(10) UNSIGNED NOT NULL;
ALTER TABLE teamUsers DROP FOREIGN KEY teamusers_ibfk_3;
ALTER TABLE teamUsers ADD FOREIGN KEY (teamUserRole_id) REFERENCES teamUserRoles(id);

DROP TABLE teamUserPermissions;

ALTER TABLE logs MODIFY COLUMN date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP; 

ALTER TABLE shareableApps ADD COLUMN origin_type VARCHAR(10) NOT NULL;
ALTER TABLE shareableApps ADD COLUMN origin_id INT(10) UNSIGNED NOT NULL;*/

DROP DATABASE IF EXISTS easeLogs;
CREATE DATABASE easeLogs;

USE easeLogs;

CREATE TABLE `logs` (
	id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	`servlet_name` varchar(255) NOT NULL,
	`code` int(10) unsigned NOT NULL,
	`user_id` int(10) unsigned DEFAULT NULL,
	`args` text NOT NULL,
	`retMsg` text NOT NULL,
	`date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(id)
);

USE ease;

ALTER TABLE sharedApps ADD COLUMN canSeeInformation TINYINT(1) NOT NULL;

ALTER TABLE sharedApps ADD COLUMN pinned_app_id INT(10) UNSIGNED;
ALTER TABLE sharedApps ADD CONSTRAINT sharedapps_ibfk_6 FOREIGN KEY (pinned_app_id) REFERENCES apps(id);

ALTER TABLE teams ADD COLUMN customer_id VARCHAR(30);
ALTER TABLE teams ADD COLUMN subscription_id VARCHAR(30);
ALTER TABLE teamUsers ADD COLUMN active TINYINT(1) DEFAULT 0;

CREATE TABLE pendingJoinAppRequests (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  shareable_app_id INT(10) UNSIGNED NOT NULL,
  team_user_id INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (shareable_app_id) REFERENCES shareableApps(id),
  FOREIGN KEY (team_user_id) REFERENCES teamUsers(id)
);

CREATE TABLE waitingCredits (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  email VARCHAR(100) NOT NULL,
  credit INT NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE (email)
);

CREATE TABLE userAndEmailInvitationsMap (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id INT(10) UNSIGNED NOT NULL,
  email_1 VARCHAR(255),
  email_2 VARCHAR(255),
  email_3 VARCHAR(255),
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES users(id)
);

ALTER TABLE apps ADD COLUMN disabled TINYINT(1) NOT NULL DEFAULT 0;
ALTER TABLE teamUsers ADD COLUMN disabled TINYINT(1) NOT NULL DEFAULT 0;

ALTER TABLE sharedApps DROP FOREIGN KEY sharedapps_ibfk_6;
ALTER TABLE sharedApps DROP COLUMN pinned_app_id;
ALTER TABLE sharedApps ADD COLUMN pinned TINYINT(1) NOT NULL DEFAULT 0;

ALTER TABLE userKeys DROP COLUMN backUpKey;
ALTER TABLE users DROP COLUMN lastName;
ALTER TABLE teamUsers ADD COLUMN admin_email VARCHAR(255);

CREATE TABLE teamUserStatus (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  reminder_three_days_sended TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (id)
);

ALTER TABLE teamUsers ADD COLUMN status_id INT(10) UNSIGNED NOT NULL;
ALTER TABLE teamUsers ADD CONSTRAINT teamusers_ibfk_4 FOREIGN KEY (status_id) REFERENCES  teamUserStatus(id);

CREATE TABLE userPendingRegistrations (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  email VARCHAR(255) NOT NULL,
  digits VARCHAR(6) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (email)
);

ALTER TABLE userPendingRegistrations ADD COLUMN date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE status ADD COLUMN send_news TINYINT(1) NOT NULL;
ALTER TABLE status ADD COLUMN terms_reviewed TINYINT(1) NOT NULL DEFAULT 0;
UPDATE status SET terms_reviewed = 1, send_news = 1;

ALTER TABLE teamUsers ADD COLUMN phone_number VARCHAR(50);

CREATE TABLE `notifications` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(30) NOT NULL,
  `content` varchar(255) NOT NULL,
  `logo_path` varchar(255) NOT NULL,
  `seen` tinyint(1) NOT NULL DEFAULT '0',
  `creation_date` datetime NOT NULL,
  `seen_date` datetime DEFAULT NULL,
  `url` varchar(2083) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `teamUserNotifications` (
  `id` int(10) unsigned NOT NULL,
  `team_user_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `team_user_id` (`team_user_id`),
  CONSTRAINT `teamusernotifications_ibfk_2` FOREIGN KEY (`team_user_id`) REFERENCES `teamUsers` (`id`),
  CONSTRAINT `teamusernotifications_ibfk_3` FOREIGN KEY (`id`) REFERENCES `notifications` (`id`)
);

ALTER TABLE teams DROP INDEX name;

CREATE TABLE teamCredit (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  credit INT NOT NULL DEFAULT 0,
  team_id INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (team_id),
  FOREIGN KEY (team_id) REFERENCES teams(id)
);

ALTER TABLE teamUserStatus ADD COLUMN tuto_done TINYINT(1) NOT NULL DEFAULT 0;
ALTER TABLE teamUserStatus ADD COLUMN first_app_received TINYINT(1) NOT NULL DEFAULT 0;

ALTER TABLE status DROP COLUMN send_news;

ALTER TABLE notifications DROP COLUMN title;

ALTER TABLE channels ADD COLUMN creator_id INT(10) UNSIGNED NOT NULL ;
ALTER TABLE channels ADD CONSTRAINT channels_ibfk_2 FOREIGN KEY (creator_id) REFERENCES teamUsers(id);

ALTER TABLE accounts ADD COLUMN passwordMustBeUpdated TINYINT(1) NOT NULL DEFAULT 0;
ALTER TABLE accounts ADD COLUMN adminNotified TINYINT(1) NOT NULL DEFAULT 0;

ALTER TABLE teamUsers DROP COLUMN admin_email;
ALTER TABLE teamUsers ADD COLUMN admin_id INT(10) UNSIGNED;
ALTER TABLE teamUsers ADD CONSTRAINT teamusers_ibfk_5 FOREIGN KEY (admin_id) REFERENCES teamUsers(id);
ALTER TABLE teamUsers ADD COLUMN disabled_date DATETIME;

DROP TABLE customAccounts;
DROP TABLE customClassicApps;
DROP TABLE customAppsAndGroupsMap;
DROP TABLE customLinkApps;
DROP TABLE customLogWithApps;
DROP TABLE customWebsiteApps;
DROP TABLE customApps;

DROP TABLE IF EXISTS createTeamInvitations;
CREATE TABLE pendingTeamCreations (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  email VARCHAR(255) NOT NULL,
  digits VARCHAR(6) NOT NULL,
  date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS customerCredentialsReception, serverPublicKeys;

CREATE TABLE serverPublicKeys (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  publicKey TEXT NOT NULL,
  admin_email VARCHAR(255) NOT NULL,
  creation_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE TABLE customerCredentialsReception (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  sender_email VARCHAR(255) NOT NULL,
  url TEXT NOT NULL,
  login TEXT NOT NULL,
  password TEXT NOT NULL,
  serverPublicKey_id INT(10) UNSIGNED NOT NULL,
  date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (serverPublicKey_id) REFERENCES serverPublicKeys(id)
);

ALTER TABLE accounts ADD COLUMN canSeeInformation TINYINT(1) NOT NULL DEFAULT 0;

ALTER TABLE teamUsers DROP COLUMN verified;
ALTER TABLE teamUsers ADD COLUMN state TINYINT NOT NULL;

ALTER TABLE status DROP COLUMN CGU;
ALTER TABLE status
  DROP COLUMN click_on_app;
ALTER TABLE status
  DROP COLUMN move_apps;
ALTER TABLE status
  DROP COLUMN open_catalog;
ALTER TABLE status
  DROP COLUMN add_an_app;
ALTER TABLE status
  DROP COLUMN invite_sended;
ALTER TABLE status ADD COLUMN team_tuto_done TINYINT(1) NOT NULL DEFAULT 0;

DROP TABLE IF EXISTS teamUserNotifications;
DROP TABLE IF EXISTS channelNotifications;
DROP TABLE IF EXISTS teamNotifications;
DROP TABLE IF EXISTS notifications;
CREATE TABLE notifications (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  content TEXT NOT NULL,
  creation_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  url VARCHAR(255) NOT NULL,
  icon VARCHAR(255) NOT NULL,
  is_new TINYINT(1) NOT NULL DEFAULT 1,
  user_id INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES users(id)
);

UPDATE websites SET website_name = 'JobTeaser Edhec' WHERE folder = 'EdhecJobTeaser';
UPDATE websites SET website_name = 'JobTeaser Estice' WHERE folder = 'EsticeJobTeaser';

ALTER TABLE websiteAttributes CHANGE COLUMN `work` `public` TINYINT(1) NOT NULL DEFAULT 1;
ALTER TABLE websiteAttributes ADD COLUMN `integrated` TINYINT(1) NOT NULL DEFAULT 1;

UPDATE websites SET website_name =  "JobTeaser Ieseg" WHERE folder = "IesegJobTeaser";

ALTER TABLE teams ADD COLUMN subscription_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE teams ADD COLUMN card_entered TINYINT(1) NOT NULL DEFAULT 0;