CREATE TABLE anyApps (
  id INT(10) UNSIGNED NOT NULL,
  account_id INT(10) UNSIGNED,
  PRIMARY KEY (id),
  FOREIGN KEY (id) REFERENCES websiteApps(id),
  FOREIGN KEY (account_id) REFERENCES accounts(id)
);

ALTER TABLE websiteAttributes ADD COLUMN logo_url VARCHAR(2000);

CREATE TABLE `software` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `folder` varchar(255) NOT NULL,
  `logo_url` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `softwareConnectionInformation` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `software_id` int(10) unsigned NOT NULL,
  `information_name` varchar(255) NOT NULL,
  `information_type` varchar(255) NOT NULL,
  `priority` tinyint(3) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `software_id` (`software_id`),
  CONSTRAINT `softwareconnectioninformation_ibfk_1` FOREIGN KEY (`software_id`) REFERENCES `software` (`id`)
);

CREATE TABLE `softwareApps` (
  `id` int(10) unsigned NOT NULL,
  `software_id` int(10) unsigned NOT NULL,
  `account_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `software_id` (`software_id`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `softwareapps_ibfk_1` FOREIGN KEY (`id`) REFERENCES `apps` (`id`),
  CONSTRAINT `softwareapps_ibfk_2` FOREIGN KEY (`software_id`) REFERENCES `software` (`id`),
  CONSTRAINT `softwareapps_ibfk_3` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`)
);

ALTER TABLE softwareConnectionInformation ADD COLUMN placeholder VARCHAR(255) NOT NULL;