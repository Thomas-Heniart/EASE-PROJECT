CREATE TABLE anyApps (
  id         INT(10) UNSIGNED NOT NULL,
  account_id INT(10) UNSIGNED,
  PRIMARY KEY (id),
  FOREIGN KEY (id) REFERENCES websiteApps (id),
  FOREIGN KEY (account_id) REFERENCES accounts (id)
);

ALTER TABLE websiteAttributes
  ADD COLUMN logo_url VARCHAR(2000);

CREATE TABLE `software` (
  `id`       INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name`     VARCHAR(255)     NOT NULL,
  `folder`   VARCHAR(255)     NOT NULL,
  `logo_url` VARCHAR(2000)             DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `softwareConnectionInformation` (
  `id`               INT(10) UNSIGNED    NOT NULL AUTO_INCREMENT,
  `software_id`      INT(10) UNSIGNED    NOT NULL,
  `information_name` VARCHAR(255)        NOT NULL,
  `information_type` VARCHAR(255)        NOT NULL,
  `priority`         TINYINT(3) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  KEY `software_id` (`software_id`),
  CONSTRAINT `softwareconnectioninformation_ibfk_1` FOREIGN KEY (`software_id`) REFERENCES `software` (`id`)
);

CREATE TABLE `softwareApps` (
  `id`          INT(10) UNSIGNED NOT NULL,
  `software_id` INT(10) UNSIGNED NOT NULL,
  `account_id`  INT(10) UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `software_id` (`software_id`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `softwareapps_ibfk_1` FOREIGN KEY (`id`) REFERENCES `apps` (`id`),
  CONSTRAINT `softwareapps_ibfk_2` FOREIGN KEY (`software_id`) REFERENCES `software` (`id`),
  CONSTRAINT `softwareapps_ibfk_3` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`)
);

ALTER TABLE softwareConnectionInformation
  ADD COLUMN placeholder VARCHAR(255) NOT NULL;

CREATE TABLE `teamSoftwareCards` (
  `id`                         INT(10) UNSIGNED    NOT NULL,
  `software_id`                INT(10) UNSIGNED    NOT NULL,
  `password_reminder_interval` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `software_id` (`software_id`),
  CONSTRAINT `teamsoftwarecards_ibfk_1` FOREIGN KEY (`id`) REFERENCES `teamCards` (`id`),
  CONSTRAINT `teamsoftwarecards_ibfk_2` FOREIGN KEY (`software_id`) REFERENCES `software` (`id`)
);

CREATE TABLE `teamSingleSoftwareCards` (
  `id`         INT(10) UNSIGNED NOT NULL,
  `account_id` INT(10) UNSIGNED DEFAULT NULL,
  `teamUser_filler_id` INT(10) UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `teamsinglesoftwarecards_ibfk_1` FOREIGN KEY (`id`) REFERENCES `teamSoftwareCards` (`id`),
  CONSTRAINT `teamsinglesoftwarecards_ibfk_2` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`),
  CONSTRAINT `teamsinglesoftwarecards_ibfk_3` FOREIGN KEY (`teamUser_filler_id`) REFERENCES `teamUsers` (`id`)
);

CREATE TABLE `teamEnterpriseSoftwareCards` (
  `id` INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `teamenterprisesoftwarecards_ibfk_1` FOREIGN KEY (`id`) REFERENCES `teamSoftwareCards` (`id`)
);