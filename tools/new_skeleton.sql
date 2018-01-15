-- MySQL dump 10.13  Distrib 5.7.17, for macos10.12 (x86_64)
--
-- Host: 127.0.0.1    Database: ease
-- ------------------------------------------------------
-- Server version	5.7.14

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `IMPORTED_ACCOUNT`
--

DROP TABLE IF EXISTS `IMPORTED_ACCOUNT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMPORTED_ACCOUNT` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `url` varchar(2000) NOT NULL,
  `website_id` int(10) unsigned DEFAULT NULL,
  `user_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `website_id` (`website_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `imported_account_ibfk_1` FOREIGN KEY (`website_id`) REFERENCES `websites` (`id`),
  CONSTRAINT `imported_account_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IMPORTED_ACCOUNT_INFORMATION`
--

DROP TABLE IF EXISTS `IMPORTED_ACCOUNT_INFORMATION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMPORTED_ACCOUNT_INFORMATION` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `value` varchar(255) NOT NULL,
  `imported_account_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `imported_account_id` (`imported_account_id`),
  CONSTRAINT `imported_account_information_ibfk_1` FOREIGN KEY (`imported_account_id`) REFERENCES `IMPORTED_ACCOUNT` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `METRIC_CONNECTION`
--

DROP TABLE IF EXISTS `METRIC_CONNECTION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `METRIC_CONNECTION` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `year` int(10) unsigned NOT NULL,
  `day_of_year` mediumint(8) unsigned NOT NULL,
  `connected` tinyint(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `USER_POST_REGISTRATION_EMAILS`
--

DROP TABLE IF EXISTS `USER_POST_REGISTRATION_EMAILS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USER_POST_REGISTRATION_EMAILS` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `email_j2_sent` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `email_j4_sent` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `email_j6_sent` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `email_j13_sent` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `email_team_creation_sent` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `email_use_seven_on_fourteen_days_sent` tinyint(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3019 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `WEBSITE_ALTERNATIVE_URL`
--

DROP TABLE IF EXISTS `WEBSITE_ALTERNATIVE_URL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `WEBSITE_ALTERNATIVE_URL` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `website_id` int(10) unsigned NOT NULL,
  `url` varchar(2000) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `website_id` (`website_id`),
  CONSTRAINT `website_alternative_url_ibfk_1` FOREIGN KEY (`website_id`) REFERENCES `websites` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `accounts` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `shared` tinyint(1) NOT NULL DEFAULT '0',
  `lastUpdateDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `reminderIntervalValue` tinyint(4) DEFAULT NULL,
  `reminderIntervalType` varchar(25) DEFAULT NULL,
  `publicKey` text,
  `privateKey` text,
  `mustBeReciphered` tinyint(1) DEFAULT '0',
  `passwordMustBeUpdated` tinyint(1) NOT NULL DEFAULT '0',
  `adminNotified` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15166 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `accountsInformations`
--

DROP TABLE IF EXISTS `accountsInformations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `accountsInformations` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `account_id` int(10) unsigned NOT NULL,
  `information_name` varchar(255) NOT NULL,
  `information_value` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `accountsinformations_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30641 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `admins`
--

DROP TABLE IF EXISTS `admins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admins` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `admins_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `anyApps`
--

DROP TABLE IF EXISTS `anyApps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anyApps` (
  `id` int(10) unsigned NOT NULL,
  `account_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `anyapps_ibfk_1` FOREIGN KEY (`id`) REFERENCES `websiteApps` (`id`),
  CONSTRAINT `anyapps_ibfk_2` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `apps`
--

DROP TABLE IF EXISTS `apps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `apps` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `insert_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `app_info_id` int(10) unsigned NOT NULL,
  `disabled` tinyint(1) NOT NULL DEFAULT '0',
  `profile_id` int(10) unsigned DEFAULT NULL,
  `position` int(10) unsigned DEFAULT NULL,
  `new` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `app_info_id` (`app_info_id`),
  KEY `profile_id` (`profile_id`),
  CONSTRAINT `apps_ibfk_2` FOREIGN KEY (`app_info_id`) REFERENCES `appsInformations` (`id`),
  CONSTRAINT `apps_ibfk_3` FOREIGN KEY (`profile_id`) REFERENCES `profiles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31084 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `appsInformations`
--

DROP TABLE IF EXISTS `appsInformations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `appsInformations` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28360 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `askingIps`
--

DROP TABLE IF EXISTS `askingIps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `askingIps` (
  `ip_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ip` char(39) DEFAULT NULL,
  `attempts` tinyint(3) unsigned NOT NULL,
  `attemptDate` datetime NOT NULL,
  `expirationDate` datetime NOT NULL,
  PRIMARY KEY (`ip_id`),
  UNIQUE KEY `ip` (`ip`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `blacklistedWebsites`
--

DROP TABLE IF EXISTS `blacklistedWebsites`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `blacklistedWebsites` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `url` varchar(255) NOT NULL,
  `count` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `categories` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `position` tinyint(3) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `channelAndTeamUserMap`
--

DROP TABLE IF EXISTS `channelAndTeamUserMap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `channelAndTeamUserMap` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `channel_id` int(10) unsigned NOT NULL,
  `team_user_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `channel_id` (`channel_id`),
  KEY `team_user_id` (`team_user_id`),
  CONSTRAINT `channelAndTeamUserMap_ibfk_1` FOREIGN KEY (`channel_id`) REFERENCES `channels` (`id`),
  CONSTRAINT `channelAndTeamUserMap_ibfk_2` FOREIGN KEY (`team_user_id`) REFERENCES `teamUsers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=801 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `channels`
--

DROP TABLE IF EXISTS `channels`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `channels` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `team_id` int(10) unsigned NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `purpose` varchar(250) DEFAULT NULL,
  `creator_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `team_id` (`team_id`,`name`),
  KEY `channels_ibfk_2` (`creator_id`),
  CONSTRAINT `channels_ibfk_1` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`),
  CONSTRAINT `channels_ibfk_2` FOREIGN KEY (`creator_id`) REFERENCES `teamUsers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=235 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `classicApps`
--

DROP TABLE IF EXISTS `classicApps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `classicApps` (
  `id` int(10) unsigned NOT NULL,
  `account_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `classicApps_ibfk_1` FOREIGN KEY (`id`) REFERENCES `websiteApps` (`id`),
  CONSTRAINT `classicapps_ibfk_2` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customerCredentialsReception`
--

DROP TABLE IF EXISTS `customerCredentialsReception`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customerCredentialsReception` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `sender_email` varchar(255) NOT NULL,
  `url` text NOT NULL,
  `login` text NOT NULL,
  `password` text NOT NULL,
  `serverPublicKey_id` int(10) unsigned NOT NULL,
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `serverPublicKey_id` (`serverPublicKey_id`),
  CONSTRAINT `customerCredentialsReception_ibfk_1` FOREIGN KEY (`serverPublicKey_id`) REFERENCES `serverPublicKeys` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `date_dimension`
--

DROP TABLE IF EXISTS `date_dimension`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ease_metrics`
--

DROP TABLE IF EXISTS `ease_metrics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ease_metrics` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `metric_name` varchar(255) NOT NULL,
  `metric_value` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `metric_name` (`metric_name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `invitations`
--

DROP TABLE IF EXISTS `invitations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invitations` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin NOT NULL,
  `email` varchar(255) COLLATE utf8_bin NOT NULL,
  `linkCode` varchar(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `joinTeamCardRequests`
--

DROP TABLE IF EXISTS `joinTeamCardRequests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `joinTeamCardRequests` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `teamCard_id` int(10) unsigned NOT NULL,
  `teamUser_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `teamCard_id` (`teamCard_id`),
  KEY `teamUser_id` (`teamUser_id`),
  CONSTRAINT `joinTeamCardRequests_ibfk_1` FOREIGN KEY (`teamCard_id`) REFERENCES `teamCards` (`id`),
  CONSTRAINT `joinTeamCardRequests_ibfk_2` FOREIGN KEY (`teamUser_id`) REFERENCES `teamUsers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `joinTeamEnterpriseCardRequests`
--

DROP TABLE IF EXISTS `joinTeamEnterpriseCardRequests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `joinTeamEnterpriseCardRequests` (
  `id` int(10) unsigned NOT NULL,
  `account_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `joinTeamEnterpriseCardRequests_ibfk_1` FOREIGN KEY (`id`) REFERENCES `joinTeamCardRequests` (`id`),
  CONSTRAINT `joinTeamEnterpriseCardRequests_ibfk_2` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `joinTeamSingleCardRequests`
--

DROP TABLE IF EXISTS `joinTeamSingleCardRequests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `joinTeamSingleCardRequests` (
  `id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `joinTeamSingleCardRequests_ibfk_1` FOREIGN KEY (`id`) REFERENCES `joinTeamCardRequests` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jsonWebTokens`
--

DROP TABLE IF EXISTS `jsonWebTokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jsonWebTokens` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `connection_token_hash` text NOT NULL,
  `jwt_ciphered` text NOT NULL,
  `keyUser_cipher` text NOT NULL,
  `salt` varchar(28) NOT NULL,
  `expiration_date` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=721 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `linkAppInformations`
--

DROP TABLE IF EXISTS `linkAppInformations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `linkAppInformations` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `url` varchar(2000) NOT NULL,
  `img_url` varchar(2000) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `linkApps`
--

DROP TABLE IF EXISTS `linkApps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `linkApps` (
  `id` int(10) unsigned NOT NULL,
  `link_app_info_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `link_app_info_id` (`link_app_info_id`),
  CONSTRAINT `linkApps_ibfk_1` FOREIGN KEY (`id`) REFERENCES `apps` (`id`),
  CONSTRAINT `linkapps_ibfk_2` FOREIGN KEY (`link_app_info_id`) REFERENCES `linkAppInformations` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `logWithApps`
--

DROP TABLE IF EXISTS `logWithApps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `logWithApps` (
  `id` int(10) unsigned NOT NULL,
  `logWith_website_app_id` int(10) unsigned DEFAULT NULL,
  `logWithWebsite_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `logWith_website_app_id` (`logWith_website_app_id`),
  KEY `logWithWebsite_id` (`logWithWebsite_id`),
  CONSTRAINT `logWithApps_ibfk_1` FOREIGN KEY (`id`) REFERENCES `websiteApps` (`id`),
  CONSTRAINT `logWithApps_ibfk_2` FOREIGN KEY (`logWith_website_app_id`) REFERENCES `websiteApps` (`id`),
  CONSTRAINT `logWithApps_ibfk_3` FOREIGN KEY (`logWithWebsite_id`) REFERENCES `websites` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `logs`
--

DROP TABLE IF EXISTS `logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `logs` (
  `servlet_name` varchar(255) NOT NULL,
  `code` int(10) unsigned NOT NULL,
  `user_id` int(10) unsigned DEFAULT NULL,
  `args` text NOT NULL,
  `retMsg` text NOT NULL,
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `metricClickOnApp`
--

DROP TABLE IF EXISTS `metricClickOnApp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `metricClickOnApp` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `app_id` int(10) unsigned NOT NULL,
  `year` int(10) unsigned NOT NULL,
  `week_of_year` tinyint(3) unsigned NOT NULL,
  `day_0` smallint(5) unsigned NOT NULL,
  `day_1` smallint(5) unsigned NOT NULL,
  `day_2` smallint(5) unsigned NOT NULL,
  `day_3` smallint(5) unsigned NOT NULL,
  `day_4` smallint(5) unsigned NOT NULL,
  `day_5` smallint(5) unsigned NOT NULL,
  `day_6` smallint(5) unsigned NOT NULL,
  `team_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `metricTeam`
--

DROP TABLE IF EXISTS `metricTeam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `metricTeam` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `team_id` int(10) unsigned NOT NULL,
  `year` int(10) unsigned NOT NULL,
  `week_of_year` tinyint(3) unsigned NOT NULL,
  `people_invited` mediumint(8) unsigned NOT NULL,
  `people_invited_emails` text,
  `people_joined` mediumint(8) unsigned NOT NULL,
  `people_joined_emails` text,
  `people_with_cards` mediumint(8) unsigned NOT NULL,
  `people_with_cards_emails` text,
  `people_click_on_app_once` mediumint(8) unsigned NOT NULL,
  `people_click_on_app_once_emails` text,
  `people_click_on_app_three_times` mediumint(8) unsigned NOT NULL,
  `people_click_on_app_three_times_emails` text,
  `people_click_on_app_five_times` mediumint(8) unsigned NOT NULL,
  `people_click_on_app_five_times_emails` text,
  `room_number` tinyint(4) NOT NULL,
  `room_names` text,
  `cards` mediumint(8) unsigned NOT NULL,
  `cards_with_receiver` mediumint(8) unsigned NOT NULL,
  `cards_with_receiver_and_password_policy` mediumint(8) unsigned NOT NULL,
  `single_cards` mediumint(8) unsigned NOT NULL,
  `enterprise_cards` mediumint(8) unsigned NOT NULL,
  `link_cards` mediumint(8) unsigned NOT NULL,
  `people_with_personnal_apps` mediumint(8) unsigned DEFAULT NULL,
  `people_with_personnal_apps_emails` text,
  `cards_names` text NOT NULL,
  `single_cards_names` text NOT NULL,
  `enterprise_cards_names` text NOT NULL,
  `link_cards_names` text NOT NULL,
  `cards_with_receiver_names` text NOT NULL,
  `cards_with_receiver_and_password_policy_names` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notifications` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL,
  `creation_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `url` varchar(255) NOT NULL,
  `icon` varchar(255) NOT NULL,
  `is_new` tinyint(1) NOT NULL DEFAULT '1',
  `user_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1157 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `options`
--

DROP TABLE IF EXISTS `options`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `options` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `background_picked` tinyint(1) NOT NULL DEFAULT '0',
  `infinite_session` tinyint(1) NOT NULL DEFAULT '0',
  `homepage_state` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3509 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `passwordLost`
--

DROP TABLE IF EXISTS `passwordLost`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `passwordLost` (
  `user_id` int(10) unsigned NOT NULL,
  `linkCode` varchar(255) DEFAULT NULL,
  `dateOfRequest` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY `user_id` (`user_id`),
  CONSTRAINT `passwordlost_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pendingJoinChannelRequests`
--

DROP TABLE IF EXISTS `pendingJoinChannelRequests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pendingJoinChannelRequests` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `channel_id` int(10) unsigned NOT NULL,
  `teamUser_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `channel_id` (`channel_id`),
  KEY `teamUser_id` (`teamUser_id`),
  CONSTRAINT `pendingJoinChannelRequests_ibfk_1` FOREIGN KEY (`channel_id`) REFERENCES `channels` (`id`),
  CONSTRAINT `pendingJoinChannelRequests_ibfk_2` FOREIGN KEY (`teamUser_id`) REFERENCES `teamUsers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pendingJoinTeamRequests`
--

DROP TABLE IF EXISTS `pendingJoinTeamRequests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pendingJoinTeamRequests` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `team_id` int(10) unsigned NOT NULL,
  `user_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `team_id` (`team_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `pendingJoinTeamRequests_ibfk_1` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`),
  CONSTRAINT `pendingJoinTeamRequests_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pendingRegistrations`
--

DROP TABLE IF EXISTS `pendingRegistrations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pendingRegistrations` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `email` varchar(100) NOT NULL,
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pendingTeamCreations`
--

DROP TABLE IF EXISTS `pendingTeamCreations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pendingTeamCreations` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `digits` varchar(6) NOT NULL,
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pendingTeamUserNotifications`
--

DROP TABLE IF EXISTS `pendingTeamUserNotifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pendingTeamUserNotifications` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `teamUser_id` int(10) unsigned NOT NULL,
  `content` text NOT NULL,
  `creation_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `url` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `teamUser_id` (`teamUser_id`),
  CONSTRAINT `pendingTeamUserNotifications_ibfk_1` FOREIGN KEY (`teamUser_id`) REFERENCES `teamUsers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pricingContacts`
--

DROP TABLE IF EXISTS `pricingContacts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pricingContacts` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `email` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL,
  `phoneNumber` varchar(50) NOT NULL,
  `company` varchar(100) NOT NULL,
  `jobPosition` varchar(100) NOT NULL,
  `collaborators` int(10) unsigned NOT NULL,
  `needs` text,
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `profileInfo`
--

DROP TABLE IF EXISTS `profileInfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `profileInfo` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8286 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `profiles`
--

DROP TABLE IF EXISTS `profiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `profiles` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `column_idx` int(10) unsigned NOT NULL,
  `position_idx` int(10) unsigned NOT NULL,
  `profile_info_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `profile_info_id` (`profile_info_id`),
  CONSTRAINT `profiles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `profiles_ibfk_3` FOREIGN KEY (`profile_info_id`) REFERENCES `profileInfo` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9153 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `savedSessions`
--

DROP TABLE IF EXISTS `savedSessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `savedSessions` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `sessionId` char(56) NOT NULL,
  `sessionToken` varchar(255) NOT NULL,
  `keyUser` char(44) NOT NULL,
  `saltUser` char(28) NOT NULL,
  `user_id` int(10) unsigned NOT NULL,
  `datetime` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sessionId` (`sessionId`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `savedsessions_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `serverKeys`
--

DROP TABLE IF EXISTS `serverKeys`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `serverKeys` (
  `login` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `saltPerso` char(28) DEFAULT NULL,
  `keyServer` char(44) DEFAULT NULL,
  PRIMARY KEY (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `serverPublicKeys`
--

DROP TABLE IF EXISTS `serverPublicKeys`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `serverPublicKeys` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `publicKey` text NOT NULL,
  `admin_email` varchar(255) NOT NULL,
  `creation_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `software`
--

DROP TABLE IF EXISTS `software`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `software` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `folder` varchar(255) NOT NULL,
  `logo_url` varchar(2000) DEFAULT NULL,
  `logo_version` tinyint(3) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `softwareApps`
--

DROP TABLE IF EXISTS `softwareApps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `softwareConnectionInformation`
--

DROP TABLE IF EXISTS `softwareConnectionInformation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `softwareConnectionInformation` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `software_id` int(10) unsigned NOT NULL,
  `information_name` varchar(255) NOT NULL,
  `information_type` varchar(255) NOT NULL,
  `priority` tinyint(3) unsigned NOT NULL,
  `placeholder` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `software_id` (`software_id`),
  CONSTRAINT `softwareconnectioninformation_ibfk_1` FOREIGN KEY (`software_id`) REFERENCES `software` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sso`
--

DROP TABLE IF EXISTS `sso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sso` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(40) NOT NULL,
  `img_path` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ssoApps`
--

DROP TABLE IF EXISTS `ssoApps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ssoApps` (
  `id` int(10) unsigned NOT NULL,
  `ssoGroup_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ssoGroup_id` (`ssoGroup_id`),
  CONSTRAINT `ssoApps_ibfk_1` FOREIGN KEY (`id`) REFERENCES `websiteApps` (`id`),
  CONSTRAINT `ssoApps_ibfk_2` FOREIGN KEY (`ssoGroup_id`) REFERENCES `ssoGroups` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ssoGroups`
--

DROP TABLE IF EXISTS `ssoGroups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ssoGroups` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `sso_id` int(10) unsigned NOT NULL,
  `account_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `sso_id` (`sso_id`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `ssoGroups_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `ssoGroups_ibfk_2` FOREIGN KEY (`sso_id`) REFERENCES `sso` (`id`),
  CONSTRAINT `ssoGroups_ibfk_3` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `status` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `first_connection` tinyint(1) NOT NULL,
  `chrome_scrapping` tinyint(1) NOT NULL,
  `apps_manually_added` tinyint(1) NOT NULL,
  `tuto_done` tinyint(1) NOT NULL,
  `last_connection` datetime DEFAULT CURRENT_TIMESTAMP,
  `homepage_email_sent` tinyint(1) NOT NULL,
  `terms_reviewed` tinyint(1) NOT NULL DEFAULT '0',
  `team_tuto_done` tinyint(1) NOT NULL DEFAULT '0',
  `edit_email_code` varchar(6) DEFAULT NULL,
  `email_requested` varchar(100) DEFAULT NULL,
  `new_feature_seen` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3509 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `teamAndEmailInvitationsMap`
--

DROP TABLE IF EXISTS `teamAndEmailInvitationsMap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teamAndEmailInvitationsMap` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `team_id` int(10) unsigned NOT NULL,
  `email_1` varchar(255) NOT NULL,
  `email_2` varchar(255) NOT NULL,
  `email_3` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `team_id` (`team_id`),
  CONSTRAINT `teamAndEmailInvitationsMap_ibfk_1` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `teamAndWebsiteMap`
--

DROP TABLE IF EXISTS `teamAndWebsiteMap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teamAndWebsiteMap` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `team_id` int(10) unsigned NOT NULL,
  `website_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `team_id` (`team_id`),
  KEY `website_id` (`website_id`),
  CONSTRAINT `teamAndWebsiteMap_ibfk_1` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`),
  CONSTRAINT `teamAndWebsiteMap_ibfk_2` FOREIGN KEY (`website_id`) REFERENCES `websites` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `teamCardReceivers`
--

DROP TABLE IF EXISTS `teamCardReceivers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teamCardReceivers` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `teamCard_id` int(10) unsigned NOT NULL,
  `teamUser_id` int(10) unsigned NOT NULL,
  `app_id` int(10) unsigned DEFAULT NULL,
  `sharing_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `teamCard_id` (`teamCard_id`),
  KEY `teamUser_id` (`teamUser_id`),
  KEY `app_id` (`app_id`),
  CONSTRAINT `teamCardReceivers_ibfk_1` FOREIGN KEY (`teamCard_id`) REFERENCES `teamCards` (`id`),
  CONSTRAINT `teamCardReceivers_ibfk_2` FOREIGN KEY (`teamUser_id`) REFERENCES `teamUsers` (`id`),
  CONSTRAINT `teamCardReceivers_ibfk_3` FOREIGN KEY (`app_id`) REFERENCES `apps` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=108 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `teamCards`
--

DROP TABLE IF EXISTS `teamCards`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teamCards` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `team_id` int(10) unsigned NOT NULL,
  `channel_id` int(10) unsigned NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `creation_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `team_id` (`team_id`),
  KEY `channel_id` (`channel_id`),
  CONSTRAINT `teamCards_ibfk_1` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`),
  CONSTRAINT `teamCards_ibfk_2` FOREIGN KEY (`channel_id`) REFERENCES `channels` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `teamCredit`
--

DROP TABLE IF EXISTS `teamCredit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teamCredit` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `credit` int(11) NOT NULL DEFAULT '0',
  `team_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `team_id` (`team_id`),
  CONSTRAINT `teamCredit_ibfk_1` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `teamEnterpriseCardReceivers`
--

DROP TABLE IF EXISTS `teamEnterpriseCardReceivers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teamEnterpriseCardReceivers` (
  `id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `teamEnterpriseCardReceivers_ibfk_1` FOREIGN KEY (`id`) REFERENCES `teamCardReceivers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `teamEnterpriseCards`
--

DROP TABLE IF EXISTS `teamEnterpriseCards`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teamEnterpriseCards` (
  `id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `teamEnterpriseCards_ibfk_1` FOREIGN KEY (`id`) REFERENCES `teamWebsiteCards` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `teamEnterpriseSoftwareCards`
--

DROP TABLE IF EXISTS `teamEnterpriseSoftwareCards`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teamEnterpriseSoftwareCards` (
  `id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `teamenterprisesoftwarecards_ibfk_1` FOREIGN KEY (`id`) REFERENCES `teamSoftwareCards` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `teamLinkCardReceivers`
--

DROP TABLE IF EXISTS `teamLinkCardReceivers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teamLinkCardReceivers` (
  `id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `teamLinkCardReceivers_ibfk_1` FOREIGN KEY (`id`) REFERENCES `teamCardReceivers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `teamLinkCards`
--

DROP TABLE IF EXISTS `teamLinkCards`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teamLinkCards` (
  `id` int(10) unsigned NOT NULL,
  `url` varchar(2000) DEFAULT NULL,
  `img_url` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `teamLinkCards_ibfk_1` FOREIGN KEY (`id`) REFERENCES `teamCards` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `teamSingleCardReceivers`
--

DROP TABLE IF EXISTS `teamSingleCardReceivers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teamSingleCardReceivers` (
  `id` int(10) unsigned NOT NULL,
  `allowed_to_see_password` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `teamSingleCardReceivers_ibfk_1` FOREIGN KEY (`id`) REFERENCES `teamCardReceivers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `teamSingleCards`
--

DROP TABLE IF EXISTS `teamSingleCards`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teamSingleCards` (
  `id` int(10) unsigned NOT NULL,
  `account_id` int(10) unsigned DEFAULT NULL,
  `teamUser_filler_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `account_id` (`account_id`),
  KEY `teamUser_filler_id` (`teamUser_filler_id`),
  CONSTRAINT `teamSingleCards_ibfk_1` FOREIGN KEY (`id`) REFERENCES `teamWebsiteCards` (`id`),
  CONSTRAINT `teamSingleCards_ibfk_2` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`),
  CONSTRAINT `teamSingleCards_ibfk_3` FOREIGN KEY (`teamUser_filler_id`) REFERENCES `teamUsers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `teamSingleSoftwareCards`
--

DROP TABLE IF EXISTS `teamSingleSoftwareCards`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teamSingleSoftwareCards` (
  `id` int(10) unsigned NOT NULL,
  `account_id` int(10) unsigned DEFAULT NULL,
  `teamUser_filler_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `account_id` (`account_id`),
  KEY `teamUser_filler_id` (`teamUser_filler_id`),
  CONSTRAINT `teamsinglesoftwarecards_ibfk_1` FOREIGN KEY (`id`) REFERENCES `teamSoftwareCards` (`id`),
  CONSTRAINT `teamsinglesoftwarecards_ibfk_2` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`),
  CONSTRAINT `teamsinglesoftwarecards_ibfk_3` FOREIGN KEY (`teamUser_filler_id`) REFERENCES `teamUsers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `teamSoftwareCards`
--

DROP TABLE IF EXISTS `teamSoftwareCards`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teamSoftwareCards` (
  `id` int(10) unsigned NOT NULL,
  `software_id` int(10) unsigned NOT NULL,
  `password_reminder_interval` tinyint(3) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `software_id` (`software_id`),
  CONSTRAINT `teamsoftwarecards_ibfk_1` FOREIGN KEY (`id`) REFERENCES `teamCards` (`id`),
  CONSTRAINT `teamsoftwarecards_ibfk_2` FOREIGN KEY (`software_id`) REFERENCES `software` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `teamUserRoles`
--

DROP TABLE IF EXISTS `teamUserRoles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teamUserRoles` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `role` bit(8) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=437 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `teamUserStatus`
--

DROP TABLE IF EXISTS `teamUserStatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teamUserStatus` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `reminder_three_days_sended` tinyint(1) NOT NULL DEFAULT '0',
  `tuto_done` tinyint(1) NOT NULL DEFAULT '0',
  `first_app_received` tinyint(1) NOT NULL DEFAULT '0',
  `invitation_sent` tinyint(1) NOT NULL,
  `profile_created` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=437 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `teamUsers`
--

DROP TABLE IF EXISTS `teamUsers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teamUsers` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned DEFAULT NULL,
  `team_id` int(10) unsigned NOT NULL,
  `firstName` varchar(100) DEFAULT NULL,
  `lastName` varchar(100) DEFAULT NULL,
  `username` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `teamUserRole_id` int(10) unsigned NOT NULL,
  `departureDate` datetime DEFAULT NULL,
  `arrivalDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `teamKey` text,
  `jobTitle` varchar(100) DEFAULT NULL,
  `active` tinyint(1) DEFAULT '0',
  `disabled` tinyint(1) NOT NULL DEFAULT '0',
  `status_id` int(10) unsigned NOT NULL,
  `phone_number` varchar(50) DEFAULT NULL,
  `admin_id` int(10) unsigned DEFAULT NULL,
  `disabled_date` datetime DEFAULT NULL,
  `state` tinyint(4) NOT NULL,
  `invitation_code` varchar(255) DEFAULT NULL,
  `profile_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `team_id` (`team_id`,`email`,`username`),
  KEY `user_id` (`user_id`),
  KEY `teamUserRole_id` (`teamUserRole_id`),
  KEY `teamusers_ibfk_4` (`status_id`),
  KEY `teamusers_ibfk_5` (`admin_id`),
  CONSTRAINT `teamUsers_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `teamUsers_ibfk_2` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`),
  CONSTRAINT `teamUsers_ibfk_3` FOREIGN KEY (`teamUserRole_id`) REFERENCES `teamUserRoles` (`id`),
  CONSTRAINT `teamusers_ibfk_4` FOREIGN KEY (`status_id`) REFERENCES `teamUserStatus` (`id`),
  CONSTRAINT `teamusers_ibfk_5` FOREIGN KEY (`admin_id`) REFERENCES `teamUsers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=437 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `teamWebsiteCards`
--

DROP TABLE IF EXISTS `teamWebsiteCards`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teamWebsiteCards` (
  `id` int(10) unsigned NOT NULL,
  `website_id` int(10) unsigned NOT NULL,
  `password_reminder_interval` tinyint(3) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `website_id` (`website_id`),
  CONSTRAINT `teamWebsiteCards_ibfk_1` FOREIGN KEY (`id`) REFERENCES `teamCards` (`id`),
  CONSTRAINT `teamWebsiteCards_ibfk_2` FOREIGN KEY (`website_id`) REFERENCES `websites` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `teams`
--

DROP TABLE IF EXISTS `teams`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teams` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `customer_id` varchar(30) DEFAULT NULL,
  `subscription_id` varchar(30) DEFAULT NULL,
  `subscription_date` datetime DEFAULT NULL,
  `card_entered` tinyint(1) NOT NULL DEFAULT '0',
  `invite_people` tinyint(1) NOT NULL DEFAULT '0',
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=114 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `testingEmails`
--

DROP TABLE IF EXISTS `testingEmails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `testingEmails` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `email` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `userKeys`
--

DROP TABLE IF EXISTS `userKeys`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userKeys` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `password` varchar(255) NOT NULL,
  `saltEase` char(28) DEFAULT NULL,
  `saltPerso` char(28) DEFAULT NULL,
  `keyUser` char(44) DEFAULT NULL,
  `publicKey` text,
  `privateKey` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3509 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `userPendingRegistrations`
--

DROP TABLE IF EXISTS `userPendingRegistrations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userPendingRegistrations` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `digits` varchar(6) NOT NULL,
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=282 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `firstName` varchar(30) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `key_id` int(10) unsigned DEFAULT NULL,
  `option_id` int(10) unsigned DEFAULT NULL,
  `registration_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status_id` int(10) unsigned NOT NULL,
  `jwt_id` int(10) unsigned DEFAULT NULL,
  `post_registration_emails_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `key_id` (`key_id`),
  KEY `option_id` (`option_id`),
  KEY `status_id` (`status_id`),
  KEY `jwt_id` (`jwt_id`),
  KEY `post_registration_emails_id` (`post_registration_emails_id`),
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`key_id`) REFERENCES `userKeys` (`id`),
  CONSTRAINT `users_ibfk_2` FOREIGN KEY (`option_id`) REFERENCES `options` (`id`),
  CONSTRAINT `users_ibfk_3` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`),
  CONSTRAINT `users_ibfk_4` FOREIGN KEY (`jwt_id`) REFERENCES `jsonWebTokens` (`id`),
  CONSTRAINT `users_ibfk_5` FOREIGN KEY (`post_registration_emails_id`) REFERENCES `USER_POST_REGISTRATION_EMAILS` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3019 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usersEmails`
--

DROP TABLE IF EXISTS `usersEmails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usersEmails` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `email` varchar(100) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `verified` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`email`),
  CONSTRAINT `usersemails_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5034 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usersEmailsPending`
--

DROP TABLE IF EXISTS `usersEmailsPending`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usersEmailsPending` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `userEmail_id` int(10) unsigned NOT NULL,
  `verificationCode` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `userEmail_id` (`userEmail_id`),
  CONSTRAINT `usersemailspending_ibfk_1` FOREIGN KEY (`userEmail_id`) REFERENCES `usersEmails` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `waitingCredits`
--

DROP TABLE IF EXISTS `waitingCredits`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `waitingCredits` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `email` varchar(100) NOT NULL,
  `credit` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `websiteAndSignInWebsiteMap`
--

DROP TABLE IF EXISTS `websiteAndSignInWebsiteMap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `websiteAndSignInWebsiteMap` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `website_id` int(10) unsigned NOT NULL,
  `signIn_website_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `website_id` (`website_id`),
  KEY `signIn_website_id` (`signIn_website_id`),
  CONSTRAINT `websiteAndSignInWebsiteMap_ibfk_1` FOREIGN KEY (`website_id`) REFERENCES `websites` (`id`),
  CONSTRAINT `websiteAndSignInWebsiteMap_ibfk_2` FOREIGN KEY (`signIn_website_id`) REFERENCES `websites` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=448 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `websiteApps`
--

DROP TABLE IF EXISTS `websiteApps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `websiteApps` (
  `id` int(10) unsigned NOT NULL,
  `website_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `website_id` (`website_id`),
  CONSTRAINT `websiteApps_ibfk_1` FOREIGN KEY (`id`) REFERENCES `apps` (`id`),
  CONSTRAINT `websiteapps_ibfk_2` FOREIGN KEY (`website_id`) REFERENCES `websites` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `websiteAttributes`
--

DROP TABLE IF EXISTS `websiteAttributes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `websiteAttributes` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `locked` tinyint(1) unsigned DEFAULT '0',
  `lockedExpiration` datetime DEFAULT NULL,
  `addedDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `new` tinyint(1) NOT NULL DEFAULT '1',
  `public` tinyint(1) NOT NULL DEFAULT '1',
  `visits` int(10) unsigned NOT NULL DEFAULT '0',
  `blacklisted` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `noScrap` tinyint(1) DEFAULT '0',
  `integrated` tinyint(1) NOT NULL DEFAULT '1',
  `logo_url` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=657 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `websiteCredentials`
--

DROP TABLE IF EXISTS `websiteCredentials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `websiteCredentials` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `login` text COLLATE utf8_unicode_ci NOT NULL,
  `password` text COLLATE utf8_unicode_ci NOT NULL,
  `website_id` int(10) unsigned NOT NULL,
  `serverPublicKey_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `website_id` (`website_id`),
  KEY `serverPublicKey_id` (`serverPublicKey_id`),
  CONSTRAINT `websiteCredentials_ibfk_1` FOREIGN KEY (`website_id`) REFERENCES `websites` (`id`),
  CONSTRAINT `websiteCredentials_ibfk_2` FOREIGN KEY (`serverPublicKey_id`) REFERENCES `serverPublicKeys` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `websiteFailures`
--

DROP TABLE IF EXISTS `websiteFailures`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `websiteFailures` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `url` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `count` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `websiteRequests`
--

DROP TABLE IF EXISTS `websiteRequests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `websiteRequests` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `url` varchar(2000) COLLATE utf8_unicode_ci NOT NULL,
  `website_id` int(10) unsigned NOT NULL,
  `email` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `website_id` (`website_id`),
  CONSTRAINT `websiteRequests_ibfk_1` FOREIGN KEY (`website_id`) REFERENCES `websites` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `websites`
--

DROP TABLE IF EXISTS `websites`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `websites` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `login_url` text NOT NULL,
  `website_name` varchar(255) NOT NULL,
  `folder` varchar(50) NOT NULL,
  `sso` int(10) unsigned DEFAULT NULL,
  `noLogin` tinyint(1) DEFAULT '0',
  `website_homepage` text NOT NULL,
  `ratio` int(10) unsigned DEFAULT '0',
  `position` int(10) unsigned DEFAULT '1',
  `website_attributes_id` int(10) unsigned DEFAULT NULL,
  `category_id` int(10) unsigned DEFAULT NULL,
  `logo_version` tinyint(3) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `sso` (`sso`),
  KEY `website_attributes_id` (`website_attributes_id`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `websites_ibfk_1` FOREIGN KEY (`sso`) REFERENCES `sso` (`id`),
  CONSTRAINT `websites_ibfk_2` FOREIGN KEY (`website_attributes_id`) REFERENCES `websiteAttributes` (`id`),
  CONSTRAINT `websites_ibfk_3` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=614 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `websitesInformations`
--

DROP TABLE IF EXISTS `websitesInformations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `websitesInformations` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `website_id` int(10) unsigned NOT NULL,
  `information_name` varchar(255) NOT NULL,
  `information_type` varchar(255) NOT NULL,
  `priority` tinyint(4) NOT NULL,
  `placeholder` varchar(25) NOT NULL,
  `placeholder_icon` varchar(25) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `website_id` (`website_id`),
  CONSTRAINT `websitesinformations_ibfk_1` FOREIGN KEY (`website_id`) REFERENCES `websites` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1320 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `websitesVisited`
--

DROP TABLE IF EXISTS `websitesVisited`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `websitesVisited` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `url` varchar(255) NOT NULL,
  `count` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-01-15 11:32:26
-- MySQL dump 10.13  Distrib 5.7.17, for macos10.12 (x86_64)
--
-- Host: 127.0.0.1    Database: easeLogs
-- ------------------------------------------------------
-- Server version	5.7.14

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `logs`
--

DROP TABLE IF EXISTS `logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `logs` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `servlet_name` varchar(255) NOT NULL,
  `code` int(10) unsigned NOT NULL,
  `user_id` int(10) unsigned DEFAULT NULL,
  `args` text NOT NULL,
  `retMsg` text NOT NULL,
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=117181 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-01-15 11:32:27
