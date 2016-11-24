CREATE TABLE keys (
  id int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  password varchar(50) NOT NULL,
  saltEase char(28) DEFAULT NULL,
  saltPerso char(28) DEFAULT NULL,
  keyUser char(44) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE options (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `background_picked` tinyint(1) NOT NULL DEFAULT 0,
  `infinite_session` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (id)
);

CREATE TABLE status (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `registered` tinyint(1) NOT NULL,
  `tuto` tinyint(1) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE profilePermissions (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `group_id` int(10) unsigned NOT NULL,
  `permission` bit(20) NOT NULL DEFAULT b'11111111111111111111',
  PRIMARY KEY (id),
  FOREIGN KEY (group_id) REFERENCES groups (id)
);

CREATE TABLE appPermissions (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `group_id` int(10) unsigned NOT NULL,
  `permission` bit(20) NOT NULL DEFAULT b'11111111111111111111',
  PRIMARY KEY (id),
  FOREIGN KEY (group_id) REFERENCES groups (id)
)

CREATE TABLE groups (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(25) NOT NULL,
  `parent` int(10) unsigned,
  `infrastructure_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (parent) REFERENCES groups(id),
  FOREIGN KEY (infrastructure_id) REFERENCES infrastructures(id)
);

CREATE TABLE infrastructures (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(25) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE infrastructuresAdminsMap (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `infrastructure_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (infrastructure_id) REFERENCES infrastructures(id),
  FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE groupsAndUsersMap (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `group_id` int(10) unsigned NOT NULL,
  `user_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (group_id) REFERENCES groups(id),
  FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE `requestedWebsites` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `site` text,
  `date` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE `users` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `firstName` varchar(30) NOT NULL,
  `lastName` varchar(30) NOT NULL,
  `email` varchar(100) NOT NULL,
  `key_id` int(10) unsigned NOT NULL,
  `option_id` int(10) unsigned NOT NULL,
  `registration_date` DATETIME,
  `status_id` INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  FOREIGN KEY (key_id) REFERENCES keys (id),
  FOREIGN KEY (option_id) REFERENCES options (id),
  FOREIGN KEY (status_id) REFERENCES status (id)
);

CREATE TABLE `groupProfiles`
(
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  group_id INT(10) UNSIGNED NOT NULL,
  permission_id INT(10) UNSIGNED NOT NULL,
  profile_info_id INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (group_id) REFERENCES groups(id),
  FOREIGN KEY (permission_id) REFERENCES permissions(id),
  FOREIGN KEY (profile_info_id) REFERENCES profileInfo(id)
);

CREATE TABLE `profileInfo`
(
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  name VACHAR(255) NOT NULL,
  color VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
)

CREATE TABLE profiles
(
id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
user_id INT(10) UNSIGNED NOT NULL,
permission_id INT(10) UNSIGNED NOT NULL,
column INT(10) UNSIGNED NOT NULL,
position INT(10) UNSIGNED NOT NULL,
group_profile_id INT(10) UNSIGNED,
profile_info_id INT(10) UNSIGNED NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (user_id) REFERENCES users (id),
FOREIGN KEY (permission_id) REFERENCES profilePermissions (id),
FOREIGN KEY (group_profile_id) REFERENCES groupProfiles (id),
FOREIGN KEY (profile_info_id) REFERENCES profileInfo (id)
);

CREATE TABLE `admins` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE `askingIps` (
  `ip_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ip` char(39) DEFAULT NULL,
  `attempts` tinyint(3) unsigned NOT NULL,
  `attemptDate` datetime NOT NULL,
  `expirationDate` datetime NOT NULL,
  PRIMARY KEY (`ip_id`),
  UNIQUE KEY `ip` (`ip`)
);

CREATE TABLE `websites` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `login_url` text NOT NULL,
  `website_name` varchar(255) NOT NULL,
  `folder` varchar(50) NOT NULL,
  `sso` int(10) unsigned DEFAULT NULL,
  `noLogin` tinyint(1) DEFAULT '0',
  `website_homepage` text NOT NULL,
  `hidden` tinyint(1) DEFAULT '0',
  `ratio` int(10) unsigned DEFAULT '0',
  `position` int(10) unsigned DEFAULT '1',
  `insertDate` datetime DEFAULT CURRENT_TIMESTAMP,
  `locked` tinyint(1) unsigned DEFAULT '0',
  `lockedExpiration` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`sso`) REFERENCES sso (id)
);

CREATE TABLE apps
(
id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
name VARCHAR(14) NOT NULL,
profile_id INT(10) UNSIGNED NOT NULL,
position TINYINT(3) UNSIGNED NOT NULL,
permission_id INT(10) UNSIGNED NOT NULL,
insertDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
trashDate DATETIME,
type VARCHAR(255) NOT NULL,
work TINYINT(1) NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (profile_id) REFERENCES profiles (id),
FOREIGN KEY (permission_id) REFERENCES appPermissions (id),
);

CREATE TABLE websiteApps
(
id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
website_id INT(10) UNSIGNED NOT NULL,
app_id INT(10) UNSIGNED NOT NULL,
type VARCHAR(255) NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (app_id) REFERENCES apps (id),
FOREIGN KEY (website_id) REFERENCES websites (id),
);

CREATE TABLE `sso` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(40) NOT NULL,
  PRIMARY KEY (`id`)
);



CREATE TABLE `tags` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(14) NOT NULL,
  `color` char(7) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uc_tag_name` (`tag_name`),
);

CREATE TABLE `tagsAndSitesMap` (
  `tag_id` int(10) unsigned NOT NULL,
  `website_id` int(10) unsigned NOT NULL,
  UNIQUE KEY `tag_and_website_unique` (`tag_id`,`website_id`),
  FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`),
  FOREIGN KEY (`website_id`) REFERENCES `websites` (`id`)
);

CREATE TABLE `PasswordLost` (
  `user_id` int(10) unsigned NOT NULL,
  `linkCode` varchar(255) DEFAULT NULL,
  UNIQUE KEY `user_id` (`user_id`),
  FOREIGN KEY (`user_id`) REFERENCES users (`id`)
);

CREATE TABLE loginWithWebsites (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `website_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (website_id) REFERENCES websites (id)
);

CREATE TABLE websitesLogWithMap (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `website_id` int(10) unsigned NOT NULL,
  `website_logwith_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (website_id) REFERENCES websites (id),
  FOREIGN KEY (website_logwith_id) REFERENCES loginWithWebsites (id)
);

CREATE TABLE `invitations` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` INT(10) UNSIGNED NOT NULL,
  `linkCode` varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE `usersEmails` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `email` varchar(100) NOT NULL,
  `verified` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`email`),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);

CREATE TABLE `usersEmailsPending` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `userEmail_id` int(10) unsigned NOT NULL,
  `verificationCode` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (userEmail_id) REFERENCES usersEmails(id)
);

CREATE TABLE `date_dimension` (
  `id` bigint(20) NOT NULL,
  `date` date NOT NULL,
  `timestamp` bigint(20) NOT NULL,
  `weekend` char(10) NOT NULL DEFAULT 'Weekday',
  `day_of_week` char(10) NOT NULL,
  `month` char(10) NOT NULL,
  `month_day` int(11) NOT NULL,
  `year` int(11) NOT NULL,
  `week_starting_monday` char(2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `date` (`date`),
  KEY `year_week` (`year`,`week_starting_monday`)
);

CREATE TABLE `savedSessions` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `sessionId` char(56) NOT NULL,
  `sessionToken` char(56) NOT NULL,
  `saltToken` char(28) NOT NULL,
  `keyUser` char(44) NOT NULL,
  `saltUser` char(28) NOT NULL,
  `user_id` int(10) unsigned NOT NULL,
  `datetime` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sessionId` (`sessionId`),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);

CREATE TABLE sharedApps
(
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  app_id INT(10) UNSIGNED NOT NULL,
  share_app_id INT(10) UNSIGNED NOT NULL,
  decrypt_key VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (app_id) REFERENCES apps (id),
  FOREIGN KEY (share_app_id) REFERENCES apps (id)
);

CREATE TABLE tmpSharedApps
(
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id INT(10) UNSIGNED NOT NULL,
  shared_app_id INT(10) UNSIGNED NOT NULL,
  rsa_key VARCHAR(255) NOT NULL,
  insertDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (shared_app_id) REFERENCES apps(id)
);

CREATE TABLE sharedKeys
(
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  account_id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  key VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (account_id) REFERENCES accounts(id)
);

CREATE TABLE groupApps
(
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  group_profile_id INT(10) UNSIGNED NOT NULL,
  permisson_id INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (group_profile_id) REFERENCES groupProfiles(id),
  FOREIGN KEY (permisson_id) REFERENCES permissions(id)
);

CREATE TABLE groupWebsiteApps
(
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  group_app_id INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (group_app_id) REFERENCES groupApps(id)
);

CREATE TABLE groupClassicApps
(
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  group_website_app_id INT(10) UNSIGNED NOT NULL,
  account_id INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (group_website_app_id) REFERENCES groupWebsiteApps(id),
  FOREIGN KEY (account_id) REFERENCES accounts(id)
);

CREATE TABLE classicApps (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  website_app_id INT(10) UNSIGNED NOT NULL,
  account_id INT(10) UNSIGNED NOT NULL,
  group_classic_apps_id INT(10) UNSIGNED,
  PRIMARY KEY (id),
  FOREIGN KEY (website_app_id) REFERENCES websiteApps(id),
  FOREIGN KEY (account_id) REFERENCES accounts(id),
  FOREIGN KEY (group_classic_apps_id) REFERENCES groupClassicApps(id)
);

CREATE TABLE accounts
(
id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
password VARCHAR(255) NOT NULL,
shared TINYINT(1) NOT NULL DEFAULT 0,
PRIMARY KEY (id),
FOREIGN KEY (classic_app_id) REFERENCES classicApps(id)
);

CREATE TABLE `accountsInformations` (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  account_id INT(10) UNSIGNED NOT NULL,
  information_name VARCHAR(255) NOT NULL,
  information_value VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (account_id) REFERENCES accounts(id);
);

CREATE TABLE logWithApps
(
id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
website_app_id INT(10) UNSIGNED NOT NULL,
logWith_website_app_id INT(10) UNSIGNED NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (website_app_id) REFERENCES websiteApps(id),
FOREIGN KEY (logWith_website_app_id) REFERENCES websiteApps(id)
);

CREATE TABLE linkApps
(
id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
app_id INT(10) UNSIGNED NOT NULL,
url VARCHAR(255),
img_url VARCHAR(255),
PRIMARY KEY (id),
FOREIGN KEY (app_id) REFERENCES apps(id)
);

CREATE TABLE `websitesInformations` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `website_id` int(10) unsigned NOT NULL,
  `information_name` varchar(255) NOT NULL,
  `information_type` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`website_id`) REFERENCES `websites` (`website_id`)
);

CREATE TABLE `customApps` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(14) NOT NULL,
  `infrastructure_id` int(10) unsigned NOT NULL,
  `permisson_id` int(10) unsigned NOT NULL,
  `type` VARCHAR(25) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`infrastructure_id`) REFERENCES `infrastructures` (`id`),
  FOREIGN KEY (`permisson_id`) REFERENCES `permissions` (`id`)
);

CREATE TABLE `customLinkApps` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `custom_app_id` int(10) unsigned NOT NULL,
  `link` VARCHAR(255),
  PRIMARY KEY (`id`),
  FOREIGN KEY (`custom_app_id`) REFERENCES `customApps` (`id`)
);

CREATE TABLE `customWebsiteApps` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `custom_app_id` int(10) unsigned NOT NULL,
  `website_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`custom_app_id`) REFERENCES `customApps` (`id`),
  FOREIGN KEY (`website_id`) REFERENCES `websites` (`id`)
);

CREATE TABLE `customClassicApps` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `custom_website_app_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`custom_website_app_id`) REFERENCES `customWebsiteApps` (`id`)
);

CREATE TABLE `customAccounts` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `custom_classic_app_id` int(10) unsigned NOT NULL,
  `information_name` VARCHAR(255),
  `information_value` VARCHAR(255),
  PRIMARY KEY (`id`),
  FOREIGN KEY (`custom_classic_app_id`) REFERENCES `customClassicApps` (`id`)
);

CREATE TABLE `customLogWithApps` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `custom_website_app_id` int(10) unsigned NOT NULL,
  `custom_logWith_website_app_id` INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`custom_website_app_id`) REFERENCES `customWebsiteApps` (`id`),
  FOREIGN KEY (`custom_logWith_website_app_id`) REFERENCES `customWebsiteApps` (`id`)
);

CREATE TABLE `customAppsAndGroupsMap` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `custom_app_id` int(10) unsigned NOT NULL,
  `group_id` INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`custom_app_id`) REFERENCES `customApps` (`id`),
  FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`)
);

CREATE TABLE `customProfiles` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `color` char(7) NOT NULL,
  `group_id` int(10) unsigned NOT NULL,
  `permisson_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`),
  FOREIGN KEY (`permisson_id`) REFERENCES `permissions` (`id`)
);

CREATE TABLE `updates` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `website_id` int(10) unsigned NOT NULL,
  `login` VARCHAR(255) NOT NULL,
  `type` VARCHAR(25) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  FOREIGN KEY (`website_id`) REFERENCES `websites` (`id`)
);

CREATE TABLE `logWithUpdates` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `update_id` int(10) unsigned NOT NULL,
  `website_app_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`update_id`) REFERENCES `updates` (`id`),
  FOREIGN KEY (`website_app_id`) REFERENCES `websitesApps` (`id`)
);

CREATE TABLE `logWithUpdates` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `update_id` int(10) unsigned NOT NULL,
  `cryptedPassword` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`update_id`) REFERENCES `updates` (`id`),
);

CREATE TABLE `removedUpdates` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `website_id` int(10) unsigned NOT NULL,
  `login` VARCHAR(255) NOT NULL,
  `loginWithWebsites_id` int(10) unsigned,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`website_id`) REFERENCES `websites` (`id`),
  FOREIGN KEY (`loginWithWebsites_id`) REFERENCES `loginWithWebsites` (`id`)
);
