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
) ENGINE=InnoDB AUTO_INCREMENT=14985 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=30270 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=30844 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=28142 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=23452 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=775 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=226 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=1024 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=708 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=1986 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=1016 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=3494 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=509 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=8258 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=9125 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=51744 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=3494 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=30751 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=30618 DEFAULT CHARSET=utf8;
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
-- Table structure for table `teamUserRoles`
--

DROP TABLE IF EXISTS `teamUserRoles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teamUserRoles` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `role` bit(8) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=422 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=422 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=422 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=108 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=3494 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=272 DEFAULT CHARSET=utf8;
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
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `key_id` (`key_id`),
  KEY `option_id` (`option_id`),
  KEY `status_id` (`status_id`),
  KEY `jwt_id` (`jwt_id`),
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`key_id`) REFERENCES `userKeys` (`id`),
  CONSTRAINT `users_ibfk_2` FOREIGN KEY (`option_id`) REFERENCES `options` (`id`),
  CONSTRAINT `users_ibfk_3` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`),
  CONSTRAINT `users_ibfk_4` FOREIGN KEY (`jwt_id`) REFERENCES `jsonWebTokens` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3004 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=5019 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=767 DEFAULT CHARSET=latin1;
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=601 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
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
) ENGINE=InnoDB AUTO_INCREMENT=359 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
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
) ENGINE=InnoDB AUTO_INCREMENT=91 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
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
  PRIMARY KEY (`id`),
  KEY `sso` (`sso`),
  KEY `website_attributes_id` (`website_attributes_id`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `websites_ibfk_1` FOREIGN KEY (`sso`) REFERENCES `sso` (`id`),
  CONSTRAINT `websites_ibfk_2` FOREIGN KEY (`website_attributes_id`) REFERENCES `websiteAttributes` (`id`),
  CONSTRAINT `websites_ibfk_3` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=558 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=1241 DEFAULT CHARSET=latin1;
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

-- Dump completed on 2017-12-05 11:50:11
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
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'Analytics',10),(2,'Administrative',20),(3,'Communication',30),(4,'Developer Tools',40),(5,'Design',50),(6,'E-Learning',60),(7,'File Management',70),(8,'Food',80),(9,'Health & Medical',90),(10,'HR & Jobs',100),(11,'News',110),(12,'Payment & Accounting',120),(13,'Productivity',130),(14,'Project Management',140),(15,'Sales & Marketing',150),(16,'Shopping',160),(17,'Social & Fun',170),(18,'Travel',180);
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ease_metrics`
--

LOCK TABLES `ease_metrics` WRITE;
/*!40000 ALTER TABLE `ease_metrics` DISABLE KEYS */;
INSERT INTO `ease_metrics` VALUES (1,'app_connections','256514');
/*!40000 ALTER TABLE `ease_metrics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `serverKeys`
--

LOCK TABLES `serverKeys` WRITE;
/*!40000 ALTER TABLE `serverKeys` DISABLE KEYS */;
INSERT INTO `serverKeys` VALUES ('felix','$2a$10$jEW9Ytb0ciuSh5X/72zy3OjC2l4Ja37IP5WL6w2gFi4Kz8zMhW8qy','s4qw2K/ZZAp7pTAYBaWvy5W42oA=','1eo/qbe+6Ag+MVBt0M+MRMPpf6w/5NfWRt2dNd+FOAA='),('pierre','$2a$10$siiJbuLzfJUR6UMR8tzeW..Ym.aswgw2vXDTQPoOdmaVdWHW.Tot.','fls3xnm5sEtkdGz+hmBZhrSEoG8=','xUSYefiyzjBq6keXzf+C1bE3stMmeDnxotm32v4uCOs='),('root','$2a$10$UrMZ52PJXYrHFYiy6asDQe6ve55S2pAsx76Dq4ogsaA0FQiatwLru','WXOgJfaHTmeK177wHTN9moDMIQE=','gtYx/DFUXnheHW6Ooo0GTff37XWUGk8bV9MnNHH0KUU='),('sergii','$2a$10$egRItAvljs.cB6iBMtEXG.BDmGDiKbSyZoU5cgUnT81SoVwSxKlo.','MIACClcexUr4j5Nb/8BoUcX9uKc=','Q8wMh8yV1ltNA6dLY7EiQormbuPNCZc1bUQMP97XYxE='),('thomas','$2a$10$JY1iX2PgYWfSYUmtduLedO8NsuWViDXjw7jc9xQqsUum/F7gaiEDK','81h2Hqzn2o7E8vCU/y1xh7UZgKA=','FeSdsczPNzInjf//V3VXwgPnPxhXWv3dV0EEMImxTt4=');
/*!40000 ALTER TABLE `serverKeys` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `serverPublicKeys`
--

LOCK TABLES `serverPublicKeys` WRITE;
/*!40000 ALTER TABLE `serverPublicKeys` DISABLE KEYS */;
INSERT INTO `serverPublicKeys` VALUES (1,'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCMUiKvEsoQJHHHP0eRMNRemwZEJXORxwZG4Z/+KtllKPQNc9k2KyZ1pB/y2T9qCFaPaCb8zdgsI+H8BPTNF9x5z4pbZcs00cwcoEFj4i2HJEeIHB5n+npmRRbCE7KfRRvww3pVJ8RNVsfIcZx3u8RPXlMvGsHgeuF+WILNY7r/nQIDAQAB','thomas@ease.space','2017-09-14 03:05:46');
/*!40000 ALTER TABLE `serverPublicKeys` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `websiteAndSignInWebsiteMap`
--

LOCK TABLES `websiteAndSignInWebsiteMap` WRITE;
/*!40000 ALTER TABLE `websiteAndSignInWebsiteMap` DISABLE KEYS */;
INSERT INTO `websiteAndSignInWebsiteMap` VALUES (2,50,7),(27,155,7),(34,180,7),(62,50,28),(124,404,7),(128,417,7),(256,145,7),(257,87,7),(258,144,7),(259,175,7),(260,47,7),(261,94,7),(262,118,7),(264,194,7),(265,290,7),(266,290,28),(267,142,7),(268,257,7),(269,230,7),(270,283,7),(271,268,7),(273,239,7),(274,224,7),(275,156,7),(276,243,7),(277,231,7),(278,383,7),(279,161,7),(280,121,7),(281,207,7),(282,232,7),(283,57,7),(284,72,7),(285,211,7),(287,187,7),(288,119,7),(289,99,7),(290,146,7),(291,184,7),(292,264,7),(293,247,7),(294,173,7),(295,295,7),(296,227,7),(297,227,28),(298,321,7),(299,215,7),(300,128,7),(303,204,7),(306,190,7),(307,70,7),(309,366,7),(310,451,7),(311,412,7),(312,371,7),(313,371,28),(314,130,7),(315,92,7),(316,389,7),(317,331,7),(318,279,7),(319,279,28),(320,240,7),(321,240,28),(324,179,7),(325,179,28),(327,181,28),(328,249,7),(329,249,28),(330,307,7),(331,386,7),(332,386,28),(333,333,7),(334,449,28),(335,248,7),(336,238,7),(337,365,7),(338,138,7),(339,398,7),(340,398,28),(341,369,7),(342,369,28),(343,392,28),(344,387,7),(345,387,28),(346,120,7),(347,171,7),(348,171,28),(349,201,7),(350,183,7),(351,209,7),(352,209,28),(353,315,7),(354,376,7),(355,353,7),(357,414,7),(358,265,7),(359,103,7),(360,122,7),(361,122,28),(362,131,7),(363,135,7),(364,193,7),(365,124,7),(366,285,7),(367,244,7),(368,228,7),(369,206,7),(370,221,7),(371,197,7),(372,312,7),(373,431,7),(374,226,7),(375,338,7),(377,253,7),(379,62,7),(380,89,7),(381,217,7),(382,51,7),(383,188,7),(384,198,7),(385,186,7),(386,199,7),(387,362,7),(388,225,7),(389,284,7),(390,465,7),(391,407,7),(392,241,7),(393,223,7),(407,480,28),(408,480,28),(409,480,7),(418,499,7),(421,494,7),(422,291,7),(423,291,7),(426,522,7),(427,526,7),(428,526,7),(435,541,7),(436,544,7),(437,544,7),(438,547,7),(439,547,28),(440,547,7),(441,547,28),(446,548,7),(447,548,28);
/*!40000 ALTER TABLE `websiteAndSignInWebsiteMap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `websiteAttributes`
--

LOCK TABLES `websiteAttributes` WRITE;
/*!40000 ALTER TABLE `websiteAttributes` DISABLE KEYS */;
INSERT INTO `websiteAttributes` VALUES (2,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(3,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(4,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(5,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(6,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(9,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(12,0,NULL,'2016-12-07 15:09:02',0,1,0,0,0,1),(14,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(15,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(19,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(20,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(21,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(22,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(23,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(24,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(25,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(26,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(27,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(28,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(30,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(31,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(32,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(33,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(34,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(36,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(37,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(38,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(39,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(40,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(41,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(42,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(43,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(46,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(47,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(48,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(49,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(50,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(51,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(52,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(53,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(54,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(55,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(56,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(57,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(58,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(59,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(60,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(61,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(62,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(63,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(64,0,NULL,'2017-01-17 15:09:41',0,1,0,0,0,1),(65,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(66,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(70,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(71,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(72,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(73,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(74,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(75,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(76,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(77,0,NULL,'2017-01-17 15:08:12',0,1,0,0,0,1),(78,0,NULL,'2016-12-07 15:09:02',0,1,0,0,0,1),(79,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(80,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(81,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(82,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(84,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(85,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(86,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(87,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(88,0,NULL,'2016-11-01 00:00:00',0,1,0,0,0,1),(89,0,NULL,'2016-11-09 18:00:00',0,1,0,0,0,1),(90,0,NULL,'2016-11-11 10:53:32',0,1,0,0,0,1),(91,0,NULL,'2016-11-11 10:53:56',0,1,0,0,0,1),(92,0,NULL,'2016-11-11 10:55:02',0,1,0,0,0,1),(93,0,NULL,'2016-11-11 10:55:18',0,1,0,0,0,1),(94,0,NULL,'2016-11-11 10:56:48',0,1,0,0,0,1),(95,0,NULL,'2016-11-11 10:57:07',0,1,0,0,0,1),(96,0,NULL,'2016-11-11 10:57:25',0,1,0,0,0,1),(97,0,NULL,'2016-11-11 10:57:37',0,1,0,0,0,1),(98,0,NULL,'2016-11-11 16:16:12',0,1,0,0,0,1),(99,0,NULL,'2016-11-14 14:54:25',0,1,0,0,0,1),(100,0,NULL,'2016-11-14 14:55:52',0,1,0,0,0,1),(101,0,NULL,'2016-11-14 14:57:04',0,1,0,0,0,1),(102,0,NULL,'2016-11-14 14:58:21',0,1,0,0,0,1),(103,0,NULL,'2017-01-17 15:07:35',0,1,0,0,0,1),(104,0,NULL,'2016-11-14 15:00:56',0,1,0,0,0,1),(105,0,NULL,'2016-11-14 15:02:00',0,1,0,0,0,1),(106,0,NULL,'2016-11-14 15:03:35',0,1,0,0,0,1),(107,0,NULL,'2016-11-14 15:04:50',0,1,0,0,0,1),(108,0,NULL,'2016-11-24 16:40:49',0,0,0,0,0,1),(109,0,NULL,'2016-11-24 18:14:00',0,1,0,0,0,1),(110,0,NULL,'2016-11-29 17:56:38',0,1,0,0,0,1),(111,0,NULL,'2016-11-30 09:06:35',0,1,0,0,0,1),(112,0,NULL,'2016-11-30 09:57:57',0,1,0,0,0,1),(113,0,NULL,'2016-11-30 10:09:36',0,1,0,0,0,1),(114,0,NULL,'2016-11-30 11:06:20',0,1,0,0,0,1),(115,0,NULL,'2016-11-30 11:24:17',0,1,0,0,0,1),(116,0,NULL,'2016-11-30 11:37:54',0,1,0,0,0,1),(117,0,NULL,'2016-11-30 12:05:08',0,1,0,0,0,1),(118,0,NULL,'2016-11-30 15:33:34',0,1,0,0,0,1),(119,0,NULL,'2016-11-30 15:49:05',0,1,0,0,0,1),(120,0,NULL,'2017-01-17 15:06:40',0,1,0,0,0,1),(121,0,NULL,'2016-11-30 16:34:31',0,1,0,0,0,1),(122,0,NULL,'2016-11-30 17:07:51',0,1,0,0,0,1),(123,0,NULL,'2016-12-05 15:07:19',0,1,0,0,0,1),(124,0,NULL,'2016-12-05 15:08:37',0,1,0,0,0,1),(125,0,NULL,'2016-12-05 15:09:37',0,1,0,0,0,1),(126,0,NULL,'2016-12-07 14:51:04',0,1,0,0,0,1),(127,0,NULL,'2016-12-08 10:04:43',0,1,0,0,0,1),(128,0,NULL,'2016-12-09 11:37:44',0,1,0,0,0,1),(129,0,NULL,'2016-12-09 13:33:43',0,1,0,0,0,1),(130,0,NULL,'2016-12-09 14:44:10',0,1,0,0,0,1),(131,0,NULL,'2016-12-09 15:06:52',0,1,0,0,0,1),(132,0,NULL,'2016-12-14 18:32:01',0,1,0,0,0,1),(133,0,NULL,'2017-01-03 10:37:19',0,1,0,0,0,1),(134,0,NULL,'2017-01-03 11:38:37',0,1,0,0,0,1),(135,0,NULL,'2017-01-03 12:00:24',0,1,0,0,0,1),(136,0,NULL,'2017-01-03 12:12:06',0,1,0,0,0,1),(137,0,NULL,'2017-01-03 12:13:07',0,1,0,0,0,1),(138,0,NULL,'2017-01-03 12:22:39',0,1,0,0,0,1),(139,0,NULL,'2017-01-03 14:19:44',0,0,0,0,0,1),(140,0,NULL,'2017-01-03 14:21:24',0,1,0,0,0,1),(141,0,NULL,'2017-01-03 14:25:18',0,1,0,0,0,1),(142,0,NULL,'2017-01-03 14:58:38',0,1,0,0,0,1),(143,0,NULL,'2017-01-03 15:15:23',0,1,0,0,0,1),(144,0,NULL,'2017-01-03 15:58:55',0,1,0,0,0,1),(145,0,NULL,'2017-01-10 09:44:49',0,1,0,0,0,1),(146,0,NULL,'2017-01-10 09:58:42',0,1,0,0,0,1),(147,0,NULL,'2017-01-10 10:04:20',0,1,0,0,0,1),(148,0,NULL,'2017-01-10 10:18:41',0,1,0,0,0,1),(149,0,NULL,'2017-01-10 10:26:26',0,1,0,0,0,1),(150,0,NULL,'2017-01-10 10:47:23',0,1,0,0,0,1),(151,0,NULL,'2017-01-10 11:06:25',0,1,0,0,0,1),(152,0,NULL,'2017-01-10 11:21:36',0,1,0,0,0,1),(153,0,NULL,'2017-01-10 11:40:51',0,1,0,0,0,1),(154,0,NULL,'2017-01-10 12:04:45',0,1,0,0,0,1),(155,0,NULL,'2017-01-10 12:10:25',0,1,0,0,0,1),(156,0,NULL,'2017-01-10 12:47:44',0,1,0,0,0,1),(157,0,NULL,'2017-01-10 13:06:53',0,1,0,0,0,1),(256,0,NULL,'2017-01-17 15:02:55',0,1,0,0,0,1),(257,0,NULL,'2017-01-17 15:04:41',0,1,0,0,0,1),(258,0,NULL,'2017-01-17 15:11:32',0,1,0,0,0,1),(259,0,NULL,'2017-01-17 15:39:54',0,1,0,0,0,1),(260,0,NULL,'2017-01-17 17:36:11',0,1,0,0,0,1),(261,0,NULL,'2017-01-24 11:28:26',0,1,0,0,0,1),(262,0,NULL,'2017-01-24 12:19:35',0,1,0,0,0,1),(264,0,NULL,'2017-01-24 13:06:59',0,1,0,0,0,1),(265,0,NULL,'2017-01-24 15:16:28',0,1,0,0,0,1),(266,0,NULL,'2017-01-24 15:17:19',0,1,0,0,0,1),(267,0,NULL,'2017-01-24 15:38:06',0,1,0,0,0,1),(268,0,NULL,'2017-01-24 16:04:52',0,1,0,0,0,1),(269,0,NULL,'2017-02-01 10:57:22',0,1,0,0,0,1),(270,0,NULL,'2017-02-01 11:10:25',0,1,0,0,0,1),(271,0,NULL,'2017-02-01 11:25:29',0,1,0,0,0,1),(272,0,NULL,'2017-02-01 11:43:53',0,1,0,0,0,1),(273,0,NULL,'2017-02-01 13:33:04',0,1,0,0,0,1),(274,0,NULL,'2017-02-01 13:53:48',0,1,0,0,0,1),(275,0,NULL,'2017-02-01 14:44:23',0,1,0,0,0,1),(276,0,NULL,'2017-02-01 15:03:52',0,1,0,0,0,1),(277,0,NULL,'2017-02-07 10:16:56',0,1,0,0,0,1),(278,0,NULL,'2017-02-07 10:49:55',0,1,0,0,0,1),(282,0,NULL,'2017-02-17 18:14:47',0,1,0,0,0,1),(283,0,NULL,'2017-02-17 18:16:12',0,1,0,0,0,1),(284,0,NULL,'2017-02-17 18:18:03',0,1,0,0,0,1),(285,0,NULL,'2017-02-17 18:18:46',0,1,0,0,0,1),(286,0,NULL,'2017-02-17 18:19:27',0,1,0,0,0,1),(287,0,NULL,'2017-02-17 18:20:06',0,1,0,0,0,1),(288,0,NULL,'2017-02-17 18:21:49',0,1,0,0,0,1),(289,0,NULL,'2017-02-17 18:22:38',0,1,0,0,0,1),(290,0,NULL,'2017-02-17 18:23:37',0,1,0,0,0,1),(291,0,NULL,'2017-02-17 18:26:27',0,1,0,0,0,1),(292,0,NULL,'2017-02-17 18:27:57',0,1,0,0,0,1),(293,0,NULL,'2017-02-17 18:32:35',0,1,0,0,0,1),(294,0,NULL,'2017-02-21 17:28:06',0,1,0,0,0,1),(295,0,NULL,'2017-02-21 17:28:34',0,1,0,0,0,1),(296,0,NULL,'2017-02-21 17:29:13',0,1,0,0,0,1),(297,0,NULL,'2017-02-21 17:29:47',0,1,0,0,0,1),(298,0,NULL,'2017-02-21 17:30:12',0,1,0,0,0,1),(299,0,NULL,'2017-02-21 17:30:46',0,1,0,0,0,1),(300,0,NULL,'2017-02-21 17:32:06',0,1,0,0,0,1),(301,0,NULL,'2017-02-21 17:35:55',0,1,0,0,0,1),(302,0,NULL,'2017-02-21 17:36:56',0,1,0,0,0,1),(303,0,NULL,'2017-02-21 17:39:48',0,1,0,0,0,1),(304,0,NULL,'2017-02-21 17:40:59',0,1,0,0,0,1),(305,0,NULL,'2017-02-21 17:42:18',0,1,0,0,0,1),(306,NULL,NULL,'2017-02-22 15:11:38',0,0,0,0,0,1),(307,0,NULL,'2017-02-28 10:07:54',0,1,0,0,0,1),(308,0,NULL,'2017-02-28 10:11:59',0,1,0,0,0,1),(309,0,NULL,'2017-02-28 10:16:53',0,1,0,0,0,1),(310,0,NULL,'2017-02-28 10:18:59',0,1,0,0,0,1),(311,0,NULL,'2017-02-28 10:20:57',0,1,0,0,0,1),(312,0,NULL,'2017-02-28 10:24:15',0,1,0,0,0,1),(313,0,NULL,'2017-02-28 10:48:08',0,1,0,0,0,1),(315,0,NULL,'2017-03-01 19:12:48',0,1,0,0,0,1),(317,0,NULL,'2017-03-01 19:21:03',0,1,0,0,0,1),(318,0,NULL,'2017-03-01 19:22:42',0,1,0,0,0,1),(320,0,NULL,'2017-03-08 14:16:32',0,0,0,0,0,1),(321,0,NULL,'2017-03-08 14:16:53',0,0,0,0,0,1),(322,0,NULL,'2017-03-08 14:17:22',0,0,0,0,0,1),(323,0,NULL,'2017-03-08 14:18:02',0,0,0,0,0,1),(324,0,NULL,'2017-03-08 14:18:34',0,0,0,0,0,1),(325,0,NULL,'2017-03-08 14:19:07',0,1,0,0,0,1),(326,0,NULL,'2017-03-08 14:19:34',0,1,0,0,0,1),(327,0,NULL,'2017-03-09 12:07:52',0,1,0,0,0,1),(328,0,NULL,'2017-03-09 12:13:01',0,1,0,0,0,1),(329,0,NULL,'2017-03-09 12:18:21',0,1,0,0,0,1),(330,0,NULL,'2017-03-09 12:22:29',0,1,0,0,0,1),(331,0,NULL,'2017-03-09 12:26:16',0,1,0,0,0,1),(332,0,NULL,'2017-03-09 12:38:39',0,1,0,0,0,1),(333,0,NULL,'2017-03-14 15:59:44',0,1,0,0,0,1),(334,0,NULL,'2017-03-14 16:00:48',0,1,0,0,0,1),(335,0,NULL,'2017-03-14 16:01:38',0,1,0,0,0,1),(336,0,NULL,'2017-03-14 16:02:28',0,1,0,0,0,1),(337,0,NULL,'2017-03-14 16:03:35',0,1,0,0,0,1),(338,0,NULL,'2017-03-14 16:04:47',0,1,0,0,0,1),(339,0,NULL,'2017-03-15 09:39:49',0,1,0,0,0,1),(340,0,NULL,'2017-03-15 09:41:34',0,1,0,0,0,1),(341,0,NULL,'2017-03-15 09:43:09',0,1,0,0,0,1),(342,0,NULL,'2017-01-01 00:00:00',0,0,0,0,0,1),(343,0,NULL,'2017-01-01 00:00:00',0,0,0,0,0,1),(344,0,NULL,'2017-01-01 00:00:00',0,0,0,0,0,1),(345,0,NULL,'2017-01-01 00:00:00',0,0,0,0,0,1),(346,0,NULL,'2017-01-01 00:00:00',0,0,0,0,0,1),(347,0,NULL,'2017-01-01 00:00:00',0,1,0,0,0,1),(348,0,NULL,'2017-01-01 00:00:00',0,1,0,0,0,1),(349,0,NULL,'2017-01-01 00:00:00',0,0,0,0,0,1),(350,0,NULL,'2017-01-01 00:00:00',0,1,0,0,0,1),(351,0,NULL,'2017-01-01 00:00:00',0,0,0,0,0,1),(352,0,NULL,'2017-01-01 00:00:00',0,0,0,0,0,1),(353,0,NULL,'2017-01-01 00:00:00',0,0,0,0,0,1),(354,0,NULL,'2017-01-01 00:00:00',0,0,0,0,0,1),(355,0,NULL,'2017-01-01 00:00:00',0,0,0,0,0,1),(356,0,NULL,'2017-01-01 00:00:00',0,0,0,0,0,1),(357,0,NULL,'2017-01-01 00:00:00',0,0,0,0,0,1),(358,0,NULL,'2017-01-01 00:00:00',0,0,0,0,0,1),(359,0,NULL,'2017-03-28 21:41:38',0,0,0,0,0,1),(360,0,NULL,'2017-03-29 09:05:13',0,1,0,0,0,1),(361,0,NULL,'2017-03-29 09:06:06',0,1,0,0,0,1),(362,0,NULL,'2017-03-29 09:06:55',0,1,0,0,0,1),(363,0,NULL,'2017-03-29 09:07:51',0,1,0,0,0,1),(364,0,NULL,'2017-03-29 09:08:59',0,1,0,0,0,1),(365,0,NULL,'2017-03-29 09:09:44',0,1,0,0,0,1),(366,0,NULL,'2017-03-29 09:12:18',0,1,0,0,0,1),(367,0,NULL,'2017-04-05 11:52:31',0,1,0,0,0,1),(368,0,NULL,'2017-04-05 11:52:56',0,1,0,0,0,1),(369,0,NULL,'2017-04-05 11:53:17',0,1,0,0,0,1),(370,0,NULL,'2017-04-05 11:53:43',0,1,0,0,0,1),(371,0,NULL,'2017-04-05 15:48:37',0,1,0,0,0,1),(372,0,NULL,'2017-04-11 10:31:39',0,1,0,0,0,1),(373,0,NULL,'2017-04-11 10:52:36',0,1,0,0,0,1),(374,0,NULL,'2017-04-11 11:15:41',0,1,0,0,0,1),(375,0,NULL,'2017-04-11 11:39:11',0,1,0,0,0,1),(376,0,NULL,'2017-04-11 11:40:06',0,1,0,0,0,1),(377,0,NULL,'2017-04-11 11:57:18',0,1,0,0,0,1),(378,0,NULL,'2017-04-11 12:13:35',0,1,0,0,0,1),(379,0,NULL,'2017-04-11 12:25:11',0,1,0,0,0,1),(380,0,NULL,'2017-04-20 13:54:07',0,1,0,0,0,1),(381,0,NULL,'2017-04-20 13:57:28',0,1,0,0,0,1),(382,0,NULL,'2017-04-20 14:00:00',0,1,0,0,0,1),(383,0,NULL,'2017-04-20 14:01:30',0,1,0,0,0,1),(384,0,NULL,'2017-04-20 14:22:17',0,1,0,0,0,1),(385,0,NULL,'2017-04-20 14:22:43',0,1,0,0,0,1),(386,0,NULL,'2017-04-26 10:42:46',0,1,0,0,0,1),(387,0,NULL,'2017-04-26 10:44:16',0,1,0,0,0,1),(388,0,NULL,'2017-04-26 11:47:24',0,1,0,0,0,1),(389,0,NULL,'2017-04-26 12:16:07',0,1,0,0,0,1),(390,0,NULL,'2017-04-26 12:35:55',0,1,0,0,0,1),(391,0,NULL,'2017-05-02 11:59:46',0,1,0,0,0,1),(392,0,NULL,'2017-05-02 12:00:53',0,1,0,0,0,1),(393,0,NULL,'2017-05-02 12:01:28',0,1,0,0,0,1),(394,0,NULL,'2017-05-02 12:02:16',0,1,0,0,0,1),(395,0,NULL,'2017-05-02 12:02:52',0,1,0,0,0,1),(396,0,NULL,'2017-05-02 12:03:37',0,1,0,0,0,1),(397,0,NULL,'2017-05-02 12:04:15',0,1,0,0,0,1),(398,0,NULL,'2017-05-02 12:04:47',0,1,0,0,0,1),(399,0,NULL,'2017-05-02 12:07:27',0,1,0,0,0,1),(400,0,NULL,'2017-05-02 12:09:30',0,1,0,0,0,1),(401,0,NULL,'2017-05-10 16:57:24',0,1,0,0,0,1),(402,0,NULL,'2017-05-10 17:20:47',0,1,0,0,0,1),(403,0,NULL,'2017-05-16 09:23:29',0,1,0,0,0,1),(404,0,NULL,'2017-05-16 09:24:54',0,1,0,0,0,1),(405,0,NULL,'2017-05-16 09:26:30',0,1,0,0,0,1),(406,0,NULL,'2017-05-16 09:27:54',0,1,0,0,0,1),(407,0,NULL,'2017-05-16 09:29:00',0,1,0,0,0,1),(408,0,NULL,'2017-05-16 10:05:17',0,1,0,0,0,1),(409,0,NULL,'2017-05-16 10:05:32',0,1,0,0,0,1),(410,0,NULL,'2017-05-23 13:05:48',0,1,0,0,0,1),(411,0,NULL,'2017-05-23 13:06:53',0,1,0,0,0,1),(412,0,NULL,'2017-05-23 13:07:51',0,1,0,0,0,1),(413,0,NULL,'2017-05-23 13:09:59',0,1,0,0,0,1),(414,0,NULL,'2017-05-23 13:11:02',0,1,0,0,0,1),(415,0,NULL,'2017-05-31 10:25:33',0,1,0,0,0,1),(416,0,NULL,'2017-05-31 11:03:48',0,1,0,0,0,1),(417,0,NULL,'2017-05-31 11:04:32',0,1,0,0,1,1),(418,0,NULL,'2017-05-31 11:05:05',0,1,0,0,1,1),(419,0,NULL,'2017-05-31 11:05:37',0,1,0,0,1,1),(420,0,NULL,'2017-05-31 11:06:36',0,1,0,0,1,1),(421,0,NULL,'2017-05-31 11:08:14',0,1,0,0,0,1),(422,0,NULL,'2017-06-06 10:24:42',0,1,0,0,0,1),(423,0,NULL,'2017-06-06 11:09:32',0,1,0,0,0,1),(424,0,NULL,'2017-06-06 11:09:47',0,1,0,0,0,1),(425,0,NULL,'2017-06-06 11:11:19',0,1,0,0,0,1),(426,0,NULL,'2017-06-06 11:11:46',0,1,0,0,0,1),(427,0,NULL,'2017-06-06 11:14:32',0,1,0,0,0,1),(428,0,NULL,'2017-06-06 11:16:35',0,1,0,0,0,1),(429,0,NULL,'2017-06-16 09:36:37',0,1,0,0,0,1),(430,0,NULL,'2017-06-16 09:36:59',0,1,0,0,0,1),(431,0,NULL,'2017-06-30 09:47:23',0,1,0,0,0,1),(432,0,NULL,'2017-06-30 09:48:25',0,1,0,0,0,1),(433,0,NULL,'2017-06-30 09:49:28',0,1,0,0,0,1),(434,0,NULL,'2017-06-30 09:50:47',0,1,0,0,0,1),(435,0,NULL,'2017-06-30 09:51:45',0,1,0,0,0,1),(436,0,NULL,'2017-06-30 09:52:42',0,1,0,0,0,1),(437,0,NULL,'2017-09-06 20:40:09',0,1,0,0,0,1),(438,0,NULL,'2017-09-06 20:40:57',0,1,0,0,0,1),(439,0,NULL,'2017-09-07 07:57:57',0,0,0,0,0,1),(440,0,NULL,'2017-09-07 07:59:24',0,0,0,0,0,1),(441,0,NULL,'2017-09-07 08:00:44',0,0,0,0,0,1),(442,0,NULL,'2017-09-07 08:11:00',0,0,0,0,0,1),(443,0,NULL,'2017-09-07 08:12:26',0,0,0,0,0,1),(444,0,NULL,'2017-09-07 08:13:30',0,1,0,0,0,1),(445,0,NULL,'2017-09-20 10:25:26',0,1,0,0,0,1),(446,0,NULL,'2017-09-20 10:33:03',0,1,0,0,0,1),(451,0,NULL,'2017-09-27 02:30:11',0,1,0,0,0,0),(452,0,NULL,'2017-09-27 10:07:25',0,1,0,0,0,0),(453,0,NULL,'2017-10-11 17:43:00',0,1,0,0,0,1),(454,0,NULL,'2017-10-04 15:57:04',0,1,0,0,0,1),(455,0,NULL,'2017-10-04 15:57:44',0,1,0,0,0,1),(456,0,NULL,'2017-10-04 15:58:16',0,1,0,0,0,1),(457,0,NULL,'2017-10-04 15:59:03',0,1,0,0,0,1),(458,0,NULL,'2017-10-09 12:19:04',0,1,0,0,0,1),(459,0,NULL,'2017-10-05 16:02:46',0,1,0,0,0,0),(460,0,NULL,'2017-10-06 16:32:02',0,1,0,0,0,1),(462,0,NULL,'2017-10-06 16:34:25',0,1,0,0,0,1),(463,0,NULL,'2017-10-06 16:36:05',0,1,0,0,0,1),(464,0,NULL,'2017-10-06 16:39:42',0,1,0,0,0,1),(465,0,NULL,'2017-10-09 15:10:13',0,1,0,0,0,1),(466,0,NULL,'2017-10-06 17:13:10',0,1,0,0,0,0),(467,0,NULL,'2017-10-09 15:11:24',0,1,0,0,0,1),(468,0,NULL,'2017-10-09 15:08:46',0,1,0,0,0,1),(469,0,NULL,'2017-10-09 14:12:16',0,1,0,0,0,1),(470,0,NULL,'2017-10-09 14:14:01',0,1,0,0,0,1),(471,0,NULL,'2017-10-09 14:15:32',0,1,0,0,0,1),(472,0,NULL,'2017-10-09 14:35:36',0,1,0,0,0,1),(473,0,NULL,'2017-10-09 14:37:29',0,1,0,0,0,1),(474,0,NULL,'2017-10-09 14:39:46',0,1,0,0,0,1),(475,0,NULL,'2017-10-09 14:41:37',0,1,0,0,0,1),(476,0,NULL,'2017-10-09 16:52:23',0,1,0,0,0,1),(477,0,NULL,'2017-10-10 16:34:05',0,1,0,0,0,1),(478,0,NULL,'2017-10-10 16:35:04',0,1,0,0,0,1),(479,0,NULL,'2017-10-10 16:36:08',0,1,0,0,0,1),(480,0,NULL,'2017-10-10 16:36:49',0,1,0,0,0,1),(481,0,NULL,'2017-10-10 16:37:36',0,1,0,0,0,1),(482,0,NULL,'2017-10-12 12:25:56',0,0,0,0,1,1),(483,0,NULL,'2017-10-12 12:29:42',0,0,0,0,1,1),(484,0,NULL,'2017-10-12 12:27:19',0,0,0,0,1,1),(485,0,NULL,'2017-10-12 12:26:35',0,0,0,0,1,1),(486,0,NULL,'2017-10-12 18:04:59',0,0,0,0,1,1),(487,0,NULL,'2017-10-12 18:05:20',0,0,0,0,1,1),(488,0,NULL,'2017-10-12 18:19:44',0,1,0,0,0,1),(489,0,NULL,'2017-10-19 14:00:14',0,1,0,0,0,0),(490,0,NULL,'2017-10-19 18:54:35',0,1,0,0,0,1),(491,0,NULL,'2017-10-19 18:57:53',0,1,0,0,0,1),(492,0,NULL,'2017-10-19 18:58:25',0,1,0,0,0,1),(493,0,NULL,'2017-10-19 18:58:49',0,1,0,0,0,1),(494,0,NULL,'2017-10-19 18:59:19',0,1,0,0,0,1),(495,0,NULL,'2017-10-19 19:00:54',0,1,0,0,0,1),(496,0,NULL,'2017-10-19 19:02:21',0,1,0,0,0,1),(497,0,NULL,'2017-10-20 13:01:19',0,0,0,0,1,1),(498,0,NULL,'2017-10-20 13:11:45',0,1,0,0,0,1),(499,0,NULL,'2017-10-20 18:38:02',0,1,0,0,0,0),(500,0,NULL,'2017-10-23 11:21:37',0,1,0,0,0,1),(501,0,NULL,'2017-10-23 11:19:50',0,1,0,0,0,1),(502,0,NULL,'2017-10-23 11:18:29',0,1,0,0,0,1),(503,0,NULL,'2017-10-22 15:06:33',0,1,0,0,0,1),(504,0,NULL,'2017-10-22 16:48:13',0,1,0,0,0,1),(505,0,NULL,'2017-10-23 11:26:57',0,1,0,0,0,1),(506,0,NULL,'2017-10-23 11:57:43',0,1,0,0,0,0),(507,0,NULL,'2017-10-23 18:49:37',0,1,0,0,0,0),(508,0,NULL,'2017-10-24 15:42:19',0,1,0,0,0,1),(509,0,NULL,'2017-10-24 15:43:44',0,1,0,0,0,0),(510,0,NULL,'2017-10-24 16:25:51',0,1,0,0,0,1),(517,0,NULL,'2017-10-28 12:32:26',0,1,0,0,0,1),(518,0,NULL,'2017-10-28 13:58:43',0,1,0,0,0,0),(520,0,NULL,'2017-10-29 14:49:46',0,1,0,0,0,1),(522,0,NULL,'2017-10-30 09:34:45',0,1,0,0,0,1),(523,0,NULL,'2017-10-30 09:56:56',0,1,0,0,0,1),(525,0,NULL,'2017-10-30 10:59:26',0,1,0,0,0,1),(526,0,NULL,'2017-10-30 11:44:43',0,1,0,0,0,1),(527,0,NULL,'2017-10-30 11:44:54',0,1,0,0,0,1),(529,0,NULL,'2017-10-30 11:45:09',0,1,0,0,0,1),(530,0,NULL,'2017-10-30 11:48:18',0,1,0,0,0,1),(531,0,NULL,'2017-10-31 08:52:03',0,1,0,0,0,1),(532,0,NULL,'2017-10-31 08:56:11',0,1,0,0,0,1),(535,0,NULL,'2017-10-31 10:36:26',0,1,0,0,0,0),(536,0,NULL,'2017-10-31 10:36:36',0,1,0,0,0,0),(537,0,NULL,'2017-10-31 15:24:21',0,1,0,0,0,1),(538,0,NULL,'2017-10-31 15:24:52',0,1,0,0,0,0),(539,0,NULL,'2017-10-31 15:25:08',0,1,0,0,0,0),(540,0,NULL,'2017-11-01 09:22:28',0,1,0,0,0,1),(541,0,NULL,'2017-11-03 11:21:30',0,1,0,0,0,0),(542,0,NULL,'2017-11-03 14:18:27',0,1,0,0,0,1),(543,0,NULL,'2017-11-07 17:53:19',0,1,0,0,0,0),(544,0,NULL,'2017-11-07 18:04:54',0,1,0,0,0,0),(545,0,NULL,'2017-11-07 18:06:38',0,1,0,0,0,1),(546,0,NULL,'2017-11-07 18:08:03',0,1,0,0,0,0),(548,0,NULL,'2017-11-08 15:05:56',0,1,0,0,0,1),(549,0,NULL,'2017-11-08 15:06:11',0,1,0,0,0,1),(550,0,NULL,'2017-11-08 15:06:38',0,1,0,0,0,1),(552,0,NULL,'2017-11-08 16:34:09',0,1,0,0,0,1),(553,0,NULL,'2017-11-10 09:49:50',0,1,0,0,0,1),(554,0,NULL,'2017-11-10 10:33:08',0,1,0,0,0,1),(556,0,NULL,'2017-11-12 11:04:57',0,1,0,0,0,1),(557,0,NULL,'2017-11-13 14:20:01',0,1,0,0,0,0),(558,0,NULL,'2017-11-13 14:22:11',0,1,0,0,0,1),(559,0,NULL,'2017-11-13 18:21:55',0,1,0,0,0,1),(560,0,NULL,'2017-11-13 22:28:24',0,1,0,0,0,0),(562,0,NULL,'2017-11-14 08:40:40',0,1,0,0,0,0),(564,0,NULL,'2017-11-14 09:56:23',0,1,0,0,0,1),(565,0,NULL,'2017-11-14 12:35:02',0,1,0,0,0,1),(566,0,NULL,'2017-11-14 14:40:17',0,1,0,0,0,1),(567,0,NULL,'2017-11-14 14:42:41',0,1,0,0,0,1),(569,0,NULL,'2017-11-15 19:16:42',0,1,0,0,0,1),(570,0,NULL,'2017-11-16 20:53:08',0,1,0,0,0,1),(571,0,NULL,'2017-11-19 08:38:03',0,1,0,0,0,0),(572,0,NULL,'2017-11-20 09:50:22',0,1,0,0,0,1),(573,0,NULL,'2017-11-20 11:25:15',0,0,0,0,0,0),(574,0,NULL,'2017-11-20 22:29:28',0,1,0,0,0,0),(575,0,NULL,'2017-11-21 16:31:12',0,0,0,0,0,0),(576,0,NULL,'2017-11-22 09:58:23',0,1,0,0,0,1),(577,0,NULL,'2017-11-22 16:15:08',0,1,0,0,0,0),(578,0,NULL,'2017-11-22 17:54:39',0,1,0,0,0,1),(579,0,NULL,'2017-11-23 12:17:26',0,1,0,0,0,1),(580,0,NULL,'2017-11-24 16:32:17',0,1,0,0,0,0),(581,0,NULL,'2017-11-25 16:00:28',0,1,0,0,0,0),(583,0,NULL,'2017-11-26 19:28:23',0,1,0,0,0,1),(584,0,NULL,'2017-11-26 19:28:58',0,1,0,0,0,1),(585,0,NULL,'2017-11-26 20:06:02',0,1,0,0,0,1),(586,0,NULL,'2017-11-27 12:27:05',0,1,0,0,0,1),(587,0,NULL,'2017-11-28 14:31:01',0,1,0,0,0,1),(588,0,NULL,'2017-11-28 23:14:24',0,1,0,0,0,0),(589,0,NULL,'2017-11-29 10:00:48',0,1,0,0,0,0),(590,0,NULL,'2017-11-29 20:34:49',0,1,0,0,0,1),(591,0,NULL,'2017-11-29 21:50:29',0,1,0,0,0,1),(592,0,NULL,'2017-11-30 09:44:05',0,1,0,0,0,1),(593,0,NULL,'2017-11-30 10:24:53',0,1,0,0,0,0),(594,0,NULL,'2017-11-30 17:40:19',1,0,0,0,0,0),(595,0,NULL,'2017-11-30 23:48:59',1,1,0,0,0,0),(596,0,NULL,'2017-11-30 23:54:29',1,1,0,0,0,0),(597,0,NULL,'2017-12-01 00:02:58',1,1,0,0,0,0),(598,0,NULL,'2017-12-02 21:40:08',1,1,0,0,0,0),(599,0,NULL,'2017-12-02 21:40:28',1,1,0,0,0,0),(600,0,NULL,'2017-12-02 23:30:20',1,1,0,0,0,0);
/*!40000 ALTER TABLE `websiteAttributes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `websites`
--

LOCK TABLES `websites` WRITE;
/*!40000 ALTER TABLE `websites` DISABLE KEYS */;
INSERT INTO `websites` VALUES (5,'https://www.ieseg-online.com/','IESEG Online','IesegOnline',NULL,0,'https://www.ieseg-online.com',26,47,342,NULL),(7,'https://www.facebook.com/','Facebook','Facebook',NULL,0,'https://www.facebook.com',931,4,2,17),(28,'https://www.linkedin.com/','LinkedIn','LinkedIn',NULL,0,'https://www.linkedin.com',550,5,3,17),(35,'https://twitter.com/','Twitter','Twitter',NULL,0,'https://www.twitter.com',315,9,4,17),(46,'https://deliveroo.fr/','Deliveroo','Deliveroo',NULL,0,'https://www.deliveroo.fr',56,24,5,8),(47,'http://www.deezer.com/','Deezer','Deezer',NULL,0,'http://www.deezer.com',148,25,6,17),(49,'http://ieseg.lms.crossknowledge.com/','Crossknowledge','IesegCrossknowledge',NULL,0,'http://ieseg.lms.crossknowledge.com',11,84,343,NULL),(50,'http://ieseg-network.com','Ieseg Network','IesegNetwork',NULL,0,'http://ieseg-network.com',4,63,344,NULL),(51,'https://www.trainline.eu/','Trainline','Trainline',NULL,0,'https://www.trainline.eu/',44,27,9,18),(52,'http://cafet.ieseg.fr/','Cafet Ieseg','CafetIeseg',NULL,0,'http://cafet.ieseg.fr',14,114,345,NULL),(53,'https://ieseg.jobteaser.com/','JobTeaser Ieseg','IesegJobTeaser',NULL,0,'https://ieseg.jobteaser.com',16,55,346,NULL),(54,'https://login.live.com','Skype','Skype',NULL,0,'https://www.skype.com',120,22,12,3),(55,'https://www.7speaking.com/','7Speaking','7speaking',NULL,0,'https://www.7speaking.com',6,103,347,6),(56,'http://www.voyages-sncf.com/','Voyages SNCF','VoyageSNCF',NULL,0,'http://www.voyage-sncf.com',179,12,14,18),(57,'https://openclassrooms.com/','OpenClassRoom','OpenClassRoom',NULL,0,'https://www.openclassrooms.com',43,48,15,6),(58,'https://www.qualtrics.com/','Qualtrics','IesegQualtrics',NULL,0,'https://www.qualtrics.com',11,74,348,1),(59,'http://www.mediapluspro.com/mediaplus69/client_net/login.aspx?cfgsite=html5&cfgbdd=ieseg-67','Iéseg Media Plus','IesegMediaPlus',NULL,0,'http://www.mediapluspro.com/mediaplus69/client_net/login.aspx?cfgsite=html5&cfgbdd=ieseg-67',6,115,349,NULL),(60,'https://www.projet-voltaire.fr/','Projet Voltaire','ProjetVoltaire',NULL,0,'https://www.projet-voltaire.fr',7,104,350,6),(61,'https://www.leboncoin.fr/','Leboncoin','LeBonCoin',NULL,0,'https://www.leboncoin.fr',65,26,19,16),(62,'https://www.blablacar.fr/','BlaBlaCar','BlaBlaCar',NULL,0,'https://www.blablacar.fr',209,14,20,18),(63,'https://login.microsoftonline.com/fr','Suite Office','Office365',NULL,0,'https://login.microsoftonline.com',113,23,21,7),(64,'https://accounts.google.com','Youtube','Youtube',1,0,'https://www.youtube.com',397,7,22,17),(65,'https://accounts.google.com','Gmail','Gmail',1,0,'https://www.gmail.com',536,6,23,3),(66,'http://fr.vente-privee.com/','Vente-privée','VentePrivee',NULL,0,'http://fr.vente-privee.com',96,20,24,16),(67,'https://www.amazon.fr/','Amazon','Amazon',NULL,0,'http://www.amazon.com',126,17,25,16),(68,'https://accounts.google.com','Google Drive','GoogleDrive',1,0,'https://drive.google.com',381,8,26,7),(69,'https://outlook.office365.com/','Office365 Mails','Office365Mail',NULL,0,'https://outlook.office365.com',1049,3,27,3),(70,'https://www.kickstarter.com/','Kickstarter','Kickstarter',NULL,0,'https://www.kickstarter.com',10,94,28,15),(71,'http://unify.ieseg.fr/','Unify Iéseg','UnifyIeseg',NULL,0,'http://unify.ieseg.fr',21,52,351,NULL),(72,'https://fr.coursera.org/','Coursera','Coursera',NULL,0,'https://fr.coursera.org',31,37,30,6),(73,'https://www.manhattanprep.com/gmat/studentcenter/','GMAT Prep','ManhattanPrepGmat',NULL,0,'https://www.manhattanprep.com/gmat/studentcenter',3,105,31,6),(74,'https://gmat.magoosh.com/','Magoosh gmat','MagooshGmat',NULL,0,'https://gmat.magoosh.com',6,75,32,6),(75,'https://www.reddit.com/','Reddit','Reddit',NULL,0,'https://www.reddit.com',9,79,33,17),(77,'http://www.economist.com/','The Economist','TheEconomist',NULL,0,'http://www.economist.com',11,64,34,11),(86,'http://www.zonebourse.com/','Zone bourse','ZoneBourse',NULL,0,'https://www.zonebourse.com',1,116,36,12),(87,'https://play.spotify.com/','Spotify','Spotify',NULL,0,'https://www.spotify.com',221,16,37,17),(89,'https://www.airbnb.fr/Login','Airbnb','Airbnb',NULL,0,'https://www.airbnb.fr',219,13,38,18),(90,'https://www.tumblr.com/','Tumblr','Tumblr',NULL,0,'https://www.tumblr.com',19,69,39,17),(91,'http://www.sushiboutik.com/','Sushi Boutik','SushiBoutik',NULL,0,'http://www.sushiboutik.com',9,76,40,8),(92,'https://github.com/','GitHub','Github',NULL,0,'https://www.github.com',11,70,41,4),(93,'https://www.dropbox.com','Dropbox','Dropbox',NULL,0,'https://www.dropbox.com',121,18,42,7),(94,'https://www.netflix.com','Netflix','Netflix',NULL,0,'https://www.netflix.com',132,15,43,17),(97,'http://lol.univ-catholille.fr/','BU Vauban','BUVauban',NULL,0,'http://lol.univ-catholille.fr',70,21,352,NULL),(98,'http://myieseg.com/mon-compte/','MyIéseg','MyIeseg',NULL,0,'https://myieseg.com',19,56,353,NULL),(99,'http://www.koudetatondemand.co','Koudetat','Koudetat',NULL,1,'http://www.koudetatondemand.co',18,57,46,6),(100,'http://www.lemonde.fr','Le Monde','LeMonde',NULL,0,'http://www.lemonde.fr',25,38,47,11),(102,'https://espace-perso.smeno.com/','SMENO','Smeno',NULL,0,'https://www.smeno.com',36,34,48,9),(103,'http://www.tf1.fr/','MYTF1','MyTF1',NULL,0,'http://www.tf1.fr',122,29,49,11),(104,'https://espace-personnel.adecco.fr/','Adecco','Adecco',NULL,0,'http://www.adecco.fr',11,58,50,10),(106,'https://www.viadeo.com','Viadeo','Viadeo',NULL,0,'https://www.viadeo.com',16,77,51,17),(107,'http://fr.fashionjobs.com/','FashionJobs','FashionJobs',NULL,0,'http://fr.fashionjobs.com',12,65,52,10),(110,'https://www.priceminister.com/','PriceMinister','PriceMinister',NULL,0,'https://www.priceminister.com',14,71,53,16),(117,'https://www.cremedelacreme.io/','Crème de la crème','CremeDeLaCreme',NULL,0,'https://cremedelacreme.io',30,30,54,10),(118,'https://secure.meetup.com/login/','Meetup','Meetup',NULL,0,'https://www.meetup.com',41,49,55,17),(119,'htttps://www.babbel.com/','Babbel','Babbel',NULL,0,'https://www.babbel.com',20,85,56,6),(120,'https://www.canva.com','Canva','Canva',NULL,0,'https://www.canva.com',91,39,57,5),(121,'https://www.alloresto.fr/connexion','Allo Resto','AlloResto',NULL,0,'https://www.alloresto.fr',14,80,58,8),(122,'https://www.lefigaro.fr','Le Figaro','LeFigaro',NULL,0,'https://www.lefigaro.fr',18,40,59,11),(123,'https://secure.fnac.com','Fnac','Fnac',NULL,0,'https://www.fnac.com',28,41,60,16),(124,'https://www.cdiscount.com/','Cdiscount','Cdiscount',NULL,0,'https://www.cdiscount.com',15,86,61,16),(126,'https://www.zara.com','Zara','Zara',NULL,0,'https://www.zara.com',13,60,62,16),(128,'https://users.wix.com/','Wix','Wix',NULL,0,'https://www.wix.com',35,61,63,15),(129,'https://accounts.google.com','Google Analytics','GoogleAnalytics',1,0,'https://analytics.google.com',35,59,64,1),(130,'https://my.lmde.fr/web/guest/espace-personnel/authentification','LMDE','LMDE',NULL,0,'https://www.lmde.fr',9,87,65,9),(131,'https://www.showroomprive.com/','Showroomprivé','ShowroomPrive',NULL,0,'https://www.showroomprive.com',29,32,66,16),(132,'http://icampus.univ-catholille.fr/','Icampus','ICampus',NULL,0,'http://icampus.univ-catholille.fr',20,44,354,NULL),(133,'https://www.t411.ch','T411','T411',NULL,0,'https://www.t411.ch',0,117,306,NULL),(135,'http://www.senscritique.com/','SensCritique','SensCritique',NULL,0,'http://www.senscritique.com',14,96,70,11),(136,'https://www.zalando.com','Zalando','Zalando',NULL,0,'https://www.zalando.com',6,81,71,16),(137,'https://www.betclic.fr','Betclic','Betclic',NULL,0,'https://www.betclic.fr',15,53,72,17),(138,'https://yoopies.fr','Yoopies','Yoopies',NULL,0,'https://www.yoopies.fr',5,106,73,10),(139,'https://my-speaking-agency.com/','Speaking Agency','SpeakingAgency',NULL,0,'https://my-speaking-agency.com',1,123,74,10),(140,'https://login.yahoo.com/config/mail','Yahoo Mail','YahooMail',NULL,0,'https://www.yahoo.com',40,33,75,3),(142,'https://passport.twitch.tv','Twitch','Twitch',NULL,0,'https://www.twitch.tv',30,66,76,17),(144,'https://soundcloud.com','SoundCloud','Soundcloud',NULL,0,'https://www.soundcloud.com',183,19,77,17),(145,'https://www.intagram.com','Instagram','Instagram',NULL,0,'https://www.instagram.com',414,10,78,17),(146,'https://www.edx.org/','edX','edX',NULL,0,'https://www.edx.org/',14,67,79,6),(149,'https://www.messenger.com/login','Messenger','Messenger',NULL,0,'https://www.messenger.com',195,11,80,17),(150,'https://www.monpetitgazon.com/','MonPetitGazon','MonPetitGazon',NULL,0,'https://www.monpetitgazon.com',20,62,81,17),(153,'https://app.kickofflabs.com/login','KickoffLabs','KickoffLabs',NULL,0,'https://kickofflabs.com',4,97,82,15),(154,'https://srvadfs.ieseg.fr/','Thesis Manager','ThesisManager',NULL,0,'https://srvadfs.ieseg.fr',27,88,355,NULL),(155,'https://auth.ted.com/session/new','TED','TED',NULL,0,'https://www.ted.com/',66,42,84,NULL),(156,'https://accounts.songkick.com','Songkick','Songkick',NULL,0,'https://www.songkick.com/',3,107,85,17),(157,'https://www.bebee.com','BeBee','BeBee',NULL,0,'https://www.bebee.com',1,124,86,15),(158,'https://www.mendeley.com/','Mendeley','Mendeley',NULL,0,'https://www.mendeley.com/',5,118,87,7),(159,'http://www.profilculture.com/','ProfilCulture','ProfilCulture',NULL,0,'http://www.profilculture.com/',3,108,88,10),(161,'https://www.foodora.fr/','Foodora','Foodora',NULL,0,'https://www.foodora.fr/',35,68,89,8),(162,'https://trello.com/login','Trello','Trello',NULL,0,'https://trello.com/',52,36,90,14),(163,'https://login.mailchimp.com/','Mailchimp','Mailchimp',NULL,0,'https://mailchimp.com/',41,54,91,15),(164,'https://bitly.com/a/sign_in','Bitly','Bitly',NULL,0,'https://bitly.com/',11,89,92,15),(165,'https://www.namecheap.com/','Namecheap','Namecheap',NULL,0,'https://www.namecheap.com/',4,98,93,15),(166,'https://www.ovh.com/auth/','OVH','OVH',NULL,0,'https://www.ovh.com/',31,90,94,4),(167,'https://app.pipedrive.com/auth/login','Pipedrive','Pipedrive',NULL,0,'https://pipedrive.com/',6,99,95,15),(168,'https://calendly.com/login','Calendly','Calendly',NULL,0,'https://calendly.com/',7,119,96,13),(169,'https://wordpress.com/','WordPress','WordPress',NULL,0,'https://wordpress.com/',27,50,97,15),(170,'https://www.slack.com','Slack','Slack',NULL,0,'https://www.slack.com',63,28,98,3),(171,'https://prezi.com','Prezi','Prezi',NULL,0,'https://prezi.com',38,45,99,5),(172,'http://aplia.com/','Aplia','Aplia',NULL,0,'http://aplia.com/',12,46,100,6),(173,'https://www.futurelearn.com/','Future Learn','FutureLearn',NULL,0,'https://www.futurelearn.com/',3,109,101,6),(174,'https://framindmap.org','Framindmap','Framindmap',NULL,0,'https://framindmap.org',8,82,102,14),(175,'https://pinterest.com/','Pinterest','Pinterest',NULL,0,'https://pinterest.com/',153,31,103,17),(176,'https://shop.mheducation.com','Mc Graw Hill','McGrawHill',NULL,0,'http://www.mheducation.com/',8,100,104,6),(177,'https://siders.side.co/','Side','Side',NULL,0,'https://siders.side.co/',6,91,105,10),(178,'https://www.fun-mooc.fr/','Fun MOOC','Fun',NULL,0,'https://www.fun-mooc.fr/',4,120,106,6),(179,'https://freedcamp.com','Freedcamp','Freedcamp',NULL,0,'https://freedcamp.com',12,83,107,13),(180,'https://my.asos.com/','Asos','Asos',NULL,0,'http://www.asos.com/',38,125,108,NULL),(181,'https://www.doctrine.fr/','Doctrine','Doctrine',NULL,0,'https://www.doctrine.fr/',0,138,109,13),(183,'https://magic.piktochart.com','Piktochart','Piktochart',NULL,0,'https://magic.piktochart.com',24,72,110,5),(184,'https://quizlet.com/','Quizlet','Quizlet',NULL,0,'https://quizlet.com/',11,101,111,6),(185,'http://www.justfab.fr/','JustFab','JustFab',NULL,0,'http://www.justfab.fr/',0,139,112,16),(186,'https://shop.flixbus.fr','FlixBus','FlixBus',NULL,0,'https://www.flixbus.fr/',14,110,113,18),(187,'https://www.codecademy.com/','Codeacademy','CodeAcademy',NULL,0,'https://www.codecademy.com/',20,78,114,6),(188,'https://www.couchsurfing.com/','Couchsurfing','Couchsurfing',NULL,0,'https://www.couchsurfing.com/',33,121,115,18),(189,'https://www.evernote.com','Evernote','Evernote',NULL,0,'https://www.evernote.com',27,73,116,13),(190,'https://fr.surveymonkey.com','SurveyMonkey','SurveyMonkey',NULL,0,'https://fr.surveymonkey.com',10,122,117,15),(192,'https://www.lafourchette.com','La Fourchette','LaFourchette',NULL,0,'https://www.lafourchette.com',2,126,118,8),(193,'https://www.groupon.fr','Groupon','Groupon',NULL,0,'https://www.groupon.fr',21,127,119,16),(194,'http://9gag.com/','9gag','9gag',NULL,0,'http://9gag.com/',36,92,120,17),(195,'https://www.sushishop.fr','Sushi Shop','SushiShop',NULL,0,'https://www.sushishop.fr',11,111,121,8),(196,'https://ifttt.com','IFTTT','IFTTT',NULL,0,'https://ifttt.com',18,128,122,13),(197,'http://www.seloger.com/','SeLoger','SeLoger',NULL,0,'http://www.seloger.com/',7,129,123,16),(198,'http://www.booking.com/','Booking','Booking',NULL,0,'http://www.booking.com/',25,112,124,18),(199,'https://www.kayak.fr/','Kayak','Kayak',NULL,0,'https://www.kayak.fr/',5,140,125,18),(200,'https://login.live.com','Outlook','Outlook',NULL,0,'https://outlook.live.com/',99,35,126,NULL),(201,'https://thenounproject.com/','The Noun Project','TheNounProject',NULL,0,'https://thenounproject.com/',26,93,127,5),(203,'https://www.paypal.com/','PayPal','Paypal',NULL,0,'https://www.paypal.com/',56,51,128,12),(204,'http://doodle.com/','Doodle','Doodle',NULL,0,'http://doodle.com/',27,102,129,15),(205,'https://angel.co/','AngelList','AngelList',NULL,0,'https://angel.co/',8,113,130,17),(206,'https://www.producthunt.com','Product Hunt','ProductHunt',NULL,1,'https://www.producthunt.com',8,130,131,11),(207,'http://www.marmiton.org/','Marmiton','Marmiton',NULL,0,'http://www.marmiton.org/',8,131,132,8),(209,'https://www.strikingly.com/','Strikingly','Strikingly',NULL,0,'https://www.strikingly.com/',8,141,133,5),(211,'http://duolingo.com/','Duolingo','Duolingo',NULL,0,'http://duolingo.com/',23,132,134,6),(212,'https://www.zotero.org/','Zotero','Zotero',NULL,0,'https://www.zotero.org/',3,133,135,7),(213,'http://login.uptobox.com/','Uptobox','Uptobox',NULL,0,'http://uptobox.com/',3,134,136,7),(214,'https://1fichier.com/login.pl','1fichier','1fichier',NULL,0,'https://1fichier.com/',3,142,137,7),(215,'https://hootsuite.com/','Hootsuite','Hootsuite',NULL,0,'https://hootsuite.com/',39,135,138,15),(216,'http://www.nobesnap.com/','NobeSnap','NobeSnap',NULL,0,'http://www.nobesnap.com/',0,143,139,16),(217,'https://www.tripadvisor.com/','TripAdvisor','TripAdvisor',NULL,0,'https://www.tripadvisor.com/',66,136,140,18),(218,'https://www.urbanoutfitters.com/','UrbanOutfitter','UrbanOutfitters',NULL,0,'https://www.urbanoutfitters.com/',1,144,141,16),(219,'https://www.eventbrite.fr','Eventbrite','Eventbrite',NULL,0,'https://www.eventbrite.fr',14,145,142,17),(220,'https://mon-espace-tennis.fft.fr/','FFT','FFT',NULL,0,'https://mon-espace-tennis.fft.fr/',0,146,143,2),(221,'https://login.bloomberg.com/','Bloomberg','Bloomberg',NULL,0,'https://www.bloomberg.com/',3,137,144,11),(222,'https://www.vente-du-diable.com/','Vente Du Diable','VenteDuDiable',NULL,0,'https://www.vente-du-diable.com/',4,1,145,16),(223,'https://bazarchic.com/','Bazar Chic','BazarChic',NULL,0,'https://bazarchic.com/',2,1,146,16),(224,'http://8tracks.com/','8tracks','8tracks',NULL,0,'http://8tracks.com/',8,1,147,17),(225,'https://www.hotels.com','Hotels.com','HotelsCom',NULL,0,'https://www.hotels.com',2,1,148,18),(226,'https://www.brandalley.fr/','BrandAlley','BrandAlley',NULL,0,'https://www.brandalley.fr/',4,1,149,16),(227,'http://gmatclub.com/forum','GMAT Club','GMATClub',NULL,0,'http://gmatclub.com/',0,1,150,6),(228,'https://secure.allocine.fr/account','Allo Ciné','AlloCine',NULL,0,'http://allocine.fr',9,1,151,11),(229,'https://insights.hotjar.com/login','Hotjar','Hotjar',NULL,0,'https://www.hotjar.com/',3,1,152,10),(230,'https://www.quora.com/','Quora','Quora',NULL,0,'https://www.quora.com/',22,1,153,17),(231,'https://trakt.tv/auth/signin','Trakt','Trakt',NULL,0,'https://trakt.tv/',1,1,154,17),(232,'https://www.atelierdeschefs.fr','Atelier des Chefs','AtelierDesChefs',NULL,0,'https://www.atelierdeschefs.fr',0,1,155,8),(233,'https://signin.ea.com/p/web/login','EA Sports','EASports',NULL,0,'https://www.easports.com/',0,1,156,11),(234,'https://www.easyjet.com/fr/secure/UpdateAccount.mvc/EditAccount','easyJet','EasyJet',NULL,0,'https://www.easyjet.com/',9,1,157,18),(235,'https://analytics.amplitude.com/login','Amplitude','Amplitude',NULL,0,'https://amplitude.com/',0,1,256,1),(236,'https://www.auchandirect.fr/','Auchan Direct','AuchanDirect',NULL,0,'https://www.auchandirect.fr/',3,1,257,16),(237,'http://www.lequipe.fr/','L\'équipe','LEquipe',NULL,0,'http://www.lequipe.fr/',1,1,258,11),(238,'https://www.glassdoor.fr/','Glassdoor','Glassdoor',NULL,0,'https://www.glassdoor.fr/',7,1,259,10),(239,'https://unsplash.com/','Unsplash','Unsplash',NULL,0,'https://unsplash.com/',9,1,260,17),(240,'https://buffer.com','Buffer','Buffer',NULL,0,'https://buffer.com',24,1,261,13),(241,'https://www.noisli.com/login','Noisli','Noisli',NULL,0,'https://www.noisli.com/',2,1,262,13),(243,'https://fantasy.premierleague.com','PremierLeague','PremierLeague',NULL,0,'https://fantasy.premierleague.com',1,1,264,17),(244,'https://www.etsy.com/','Etsy','Etsy',NULL,0,'https://www.etsy.com/',14,1,265,16),(245,'http://www.hostelworld.com/','HostelWorld','HostelWorld',NULL,0,'http://www.hostelworld.com/',1,1,266,18),(246,'https://accounts.google.com','Google Calendar','GoogleCalendar',1,0,'https://calendar.google.com/',113,1,267,13),(247,'https://www.livementor.com/','Livementor','Livementor',NULL,0,'https://www.livementor.com/',6,1,268,6),(248,'http://www.inoreader.com/','Inoreader','Inoreader',NULL,0,'http://www.inoreader.com/',0,1,269,13),(249,'https://admin.typeform.com/l','Typeform','Typeform',NULL,0,'https://www.typeform.com',19,1,270,13),(250,'https://www.objetrama.fr/','ObjetRama','ObjetRama',NULL,0,'https://www.objetrama.fr/',0,1,271,16),(251,'https://app.asana.com','Asana','Asana',NULL,0,'https://asana.com',9,1,272,14),(252,'https://bitbucket.org','Bitbucket','Bitbucket',NULL,0,'https://bitbucket.org',0,1,273,4),(253,'https://www.makesense.org/','MakeSense','MakeSense',NULL,0,'https://www.makesense.org/',0,1,274,11),(254,'https://sendgrid.com/','SendGrid','SendGrid',NULL,0,'https://sendgrid.com/',2,1,275,15),(255,'https://app.crisp.im','Crisp','Crisp',NULL,0,'https://crisp.im',0,1,276,15),(256,'https://www.laposte.net/','La Poste','LaPoste',NULL,0,'https://www.laposte.net/',4,1,277,3),(257,'https://medium.com/','Medium','Medium',NULL,1,'https://medium.com/',29,1,278,17),(258,'http://accueil.etud-espas.fr/owncloud/','Kwartz','ownCloud',NULL,0,'http://accueil.etud-espas.fr/owncloud/',5,1,356,NULL),(259,'http://entespa.o2switch.net/chamilo/','Chamilo','Chamilo',NULL,0,'http://entespa.o2switch.net/chamilo/',5,1,357,NULL),(260,'https://estice-espas.jobteaser.com/fr/users/sign_in','JobTeaser Estice','EsticeJobTeaser',NULL,0,'https://estice-espas.jobteaser.com/fr/users/sign_in',9,1,358,NULL),(261,'http://www.kill-tilt.fr/','Kill Tilt','KillTilt',NULL,0,'http://www.kill-tilt.fr/',0,1,282,11),(262,'https://www.mindmeister.com','Mindmeister','Mindmeister',NULL,0,'https://www.mindmeister.com',8,1,283,13),(263,'https://my.sendinblue.com','SendinBlue','SendinBlue',NULL,0,'https://my.sendinblue.com',5,1,284,15),(264,'http://www.superprof.fr/','Superprof','Superprof',NULL,0,'http://www.superprof.fr/',8,1,285,6),(265,'https://www.thinglink.com','ThingLink','ThingLink',NULL,0,'https://www.thinglink.com',0,1,286,5),(266,'https://news.ycombinator.com','Y Combinator News','YCombinatorNews',NULL,0,'https://news.ycombinator.com',1,1,287,11),(267,'https://store.steampowered.com/','Steam','Steam',NULL,0,'http://store.steampowered.com/',7,1,288,16),(268,'https://eu.battle.net/','Battle.net','BattleNet',NULL,0,'https://eu.battle.net/',16,1,289,17),(269,'https://core.futuresimple.com/','Base CRM','BaseCRM',NULL,0,'https://getbase.com/',3,1,290,15),(270,'https://auth.riotgames.com','League of Legends','LeagueOfLegend',NULL,0,'http://euw.leagueoflegends.com/',4,1,291,17),(271,'https://mega.nz/','Mega','Mega',NULL,0,'https://mega.nz/',1,1,292,7),(272,'https://accounts.google.com/','Google Play','GooglePlay',1,0,'https://play.google.com/',7,1,293,4),(273,'https://payfit.com/','PayFit','PayFit',NULL,0,'https://payfit.com/',1,1,294,12),(274,'https://mon-expert-en-gestion.fr/','Mon expert en gestion','MonExpertEnGestion',NULL,0,'https://mon-expert-en-gestion.fr/',0,1,295,12),(275,'https://flat.io/','Flat','Flat',NULL,0,'https://flat.io/',0,1,296,6),(276,'https://www.nestorparis.com/','Nestor','Nestor',NULL,0,'https://www.nestorparis.com/',3,1,297,8),(277,'https://getpocket.com/','Pocket','Pocket',NULL,0,'https://getpocket.com/',8,1,298,13),(278,'https://my.freshbooks.com','FreshBooks','FreshBooks',NULL,0,'https://www.freshbooks.com',0,1,299,12),(279,'https://accounts.zoho.com/','Zoho Mail','ZohoMail',NULL,0,'https://mail.zoho.com/zm/',1,1,300,3),(280,'https://app.azendoo.com/','Azendoo','Azendoo',NULL,0,'https://www.azendoo.com/',1,1,301,14),(281,'https://app.getresponse.com/','GetResponse','GetResponse',NULL,0,'https://www.getresponse.fr/',0,1,302,15),(282,'https://accounts.google.com/','Google AdWords','GoogleAdwords',1,0,'https://adwords.google.com/',14,1,303,15),(283,'https://www.freeletics.com/','Freeletics','Freeletics',NULL,0,'https://www.freeletics.com/',21,1,304,17),(284,'https://www.ticketswap.fr/','TicketSwap','TicketSwap',NULL,1,'https://www.ticketswap.fr/',12,1,305,16),(285,'http://www.6play.fr/','6 Play','6Play',NULL,0,'http://www.6play.fr/',13,1,307,11),(286,'https://bookshelf.vitalsource.com/','Bookshelf','Bookshelf',NULL,0,'https://bookshelf.vitalsource.com/',2,1,308,NULL),(287,'https://accounts.google.com/','Google Developers','GoogleDevelopers',NULL,0,'https://developers.google.com/',75,1,309,4),(288,'https://my.cushionapp.com','Cushion','CushionApp',NULL,0,'https://cushionapp.com/',1,1,310,14),(289,'https://www.instapaper.com','Instapaper','Instapaper',NULL,0,'https://www.instapaper.com',0,1,311,13),(290,'https://slideshare.net/','SlideShare','SlideShare',NULL,0,'https://slideshare.net/',34,1,312,17),(291,'https://www.udemy.com/','Udemy','Udemy',NULL,0,'https://www.udemy.com/',8,1,313,6),(293,'https://app.plany.io/login','Plany','Plany',NULL,0,'https://www.plany.io/',1,1,315,15),(295,'https://codecombat.com/','CodeCombat','CodeCombat',NULL,0,'https://codecombat.com/',0,1,317,6),(296,'https://rocketreach.co/login','RocketReach','RocketReach',NULL,0,'https://rocketreach.co/',3,1,318,15),(298,'http://lentremets-express.fr/','L Entremets','LentremetsExpress',NULL,0,'http://lentremets-express.fr/',0,1,320,NULL),(299,'http://www.netedhec.com/','Blackboard','Blackboard',NULL,0,'http://www.netedhec.com/',6,1,321,NULL),(300,'http://lille-eprinting.edhec.edu/','Everyon Print','EveryonePrintLille',NULL,0,'http://lille-eprinting.edhec.edu/',15,1,322,NULL),(301,'http://aurion.edhec.edu/','Aurion','WebAurion',NULL,0,'http://aurion.edhec.edu/',10,1,323,NULL),(302,'https://cas.edhec.edu/','JobTeaser Edhec','EdhecJobTeaser',NULL,0,'https://edhec.jobteaser.com/fr/',13,1,324,NULL),(303,'https://edhec.facebook.com/','Workplace','EDHECWorkplace',NULL,0,'https://edhec.facebook.com/',41,1,325,17),(304,'https://cb.hbsp.harvard.edu/','Harvard Publishing','HarvardPublishing',NULL,0,'https://cb.hbsp.harvard.edu/cbmp/pages/home',1,1,326,13),(305,'https://web.whatsapp.com','WhatsApp','WhatsApp',NULL,1,'https://web.whatsapp.com',0,1,327,17),(306,'https://www.shopify.com','Shopify','Shopify',NULL,0,'https://www.shopify.com',1,1,328,15),(307,'https://www.wunderlist.com/','Wunderlist','Wunderlist',NULL,0,'https://www.wunderlist.com/',11,1,329,13),(308,'https://www.researchgate.net','ResearchGate','ResearchGate',NULL,0,'https://www.researchgate.net',0,1,330,17),(309,'https://www.gamefaqs.com','GameFaqs','GameFaqs',NULL,0,'https://www.gamefaqs.com',0,1,331,11),(310,'https://clients.o2switch.fr/','o2switch','O2Switch',NULL,0,'http://www.o2switch.fr/',0,1,332,4),(311,'https://dapulse.com/','Dapulse','Dapulse',NULL,0,'https://dapulse.com/',1,1,333,14),(312,'https://birchbox.fr/','Birchbox','Birchbox',NULL,0,'https://birchbox.fr/',4,1,334,16),(313,'https://app.uxpin.com/','UXPin','UXPin',NULL,0,'https://www.uxpin.com',0,1,335,5),(314,'https://web.ciscospark.com','Cisco Spark','CiscoSpark',NULL,0,'https://www.ciscospark.com/',0,1,336,14),(315,'https://pixabay.com/','Pixabay','Pixabay',NULL,0,'https://pixabay.com/',7,1,337,5),(316,'https://manager.avocode.com/','Avocode','Avocode',NULL,0,'https://avocode.com/',0,1,338,4),(317,'https://app.debitoor.com/','Debitoor','Debitoor',NULL,0,'https://debitoor.com/',0,1,339,12),(318,'https://dribbble.com/session/new','Dribbble','Dribbble',NULL,0,'https://dribbble.com',3,1,340,5),(319,'https://login.xero.com/','Xero','Xero',NULL,0,'https://www.xero.com/',1,1,341,12),(320,'http://www.edhecbbaalumni.com/','Alumni BBA','AlumniBBA',NULL,0,'http://www.edhecbbaalumni.com/',5,1,359,NULL),(321,'https://manager.agorapulse.com/','Agorapulse','Agorapulse',NULL,1,'https://agorapulse.com/',1,1,360,15),(322,'https://app.intercom.io/','Intercom','Intercom',NULL,0,'https://www.intercom.com/',3,1,361,15),(323,'https://my.livechatinc.com/','LiveChat','LiveChat',NULL,0,'https://www.livechatinc.com/',0,1,362,15),(324,'https://projects.invisionapp.com/','InVision','InVision',NULL,0,'https://www.invisionapp.com/',1,1,363,5),(325,'https://dashboard.webydo.com/','Webydo','Webydo',NULL,0,'http://www.webydo.com/',0,1,364,5),(326,'https://mail.ovh.net','Roundcube OVH','MailOVH',NULL,0,'https://mail.ovh.net',2,1,365,3),(327,'http://www.tidiochat.com/','TidioChat','TidioChat',NULL,0,'http://www.tidiochat.com/',0,1,366,15),(328,'https://account.ankama.com/','Dofus','Dofus',NULL,0,'http://www.dofus.com/',0,1,367,17),(329,'https://account.ankama.com/','Krosmaga','Krosmaga',NULL,0,'http://www.krosmaga.com/',0,1,368,17),(330,'https://account.ankama.com/','Wakfu','Wakfu',NULL,0,'http://www.wakfu.com/',0,1,369,17),(331,'https://ankama.zendesk.com/','Ankama Support','AnkamaSupport',NULL,0,'https://support.ankama.com/',1,1,370,4),(332,'https://www.crunchyroll.com','Crunchyroll','Crunchyroll',NULL,0,'https://www.crunchyroll.com',1,1,371,17),(333,'https://zenkit.com','Zenkit','Zenkit',NULL,0,'https://zenkit.com',1,1,372,13),(334,'https://paymium.com','Paymium','Paymium',NULL,0,'https://paymium.com',0,1,373,12),(335,'https://www.gandi.net','Gandi.net','GandiNet',NULL,0,'https://www.gandi.net',1,1,374,4),(336,'https://www.prestashop.com','PrestaShop','PrestaShop',NULL,0,'https://www.prestashop.com',1,1,375,15),(337,'https://www.mondocteur.fr/identification.html','MonDocteur','MonDocteur',NULL,0,'https://www.mondocteur.fr/',0,1,376,9),(338,'http://www.investopedia.com/','Investopedia','Investopedia',NULL,0,'http://www.investopedia.com/',0,1,377,11),(339,'https://www.supplay.fr/supplay-espace-interimaire-candidats-connexion','Supplay','Supplay',NULL,0,'https://www.supplay.fr',0,1,378,10),(340,'https://www.doctolib.fr','Doctolib','Doctolib',NULL,0,'https://www.doctolib.fr',0,1,379,9),(341,'https://accounts.google.com','Google+','GooglePlus',1,0,'https://plus.google.com/',13,1,380,17),(342,'https://accounts.google.com','Google Sheets','GoogleSheet',1,0,'https://docs.google.com/',16,1,381,7),(343,'https://www.facebook.com/','Facebook Business','FacebookBusiness',NULL,0,'https://business.facebook.com/',5,1,382,15),(344,'https://www.inpi.fr/','Inpi','Inpi',NULL,0,'https://www.inpi.fr/',0,1,383,2),(345,'https://www.marches-publics.gouv.fr/','Marches publics','MarchesPublics',NULL,0,'https://www.marches-publics.gouv.fr/',0,1,384,2),(346,'https://www.mise-en-scene.be','Mise en scene','MiseEnScene',NULL,0,'https://www.mise-en-scene.be',0,1,385,16),(347,'https://www.payfacile.com/','Payfacile','Payfacile',NULL,0,'https://www.payfacile.com/',0,1,386,12),(348,'https://accounts.shutterstock.com/','Shutterstock','Shutterstock',NULL,0,'https://www.shutterstock.com/',0,1,387,5),(349,'https://developer.vuforia.com/','Vuforia Developer','VuforiaDeveloper',NULL,0,'https://developer.vuforia.com/',0,1,388,4),(350,'https://www.rankwyz.com/','Rankwyz','Rankwyz',NULL,0,'https://www.rankwyz.com/',0,1,389,4),(351,'https://goodbarber.com','GoodBarber','GoodBarber',NULL,0,'https://goodbarber.com',0,1,390,4),(352,'https://recruteurs.apec.fr/','Apec Recruteurs','ApecRecruteurs',NULL,0,'https://recruteurs.apec.fr/',1,1,391,10),(353,'https://gurushots.com/','GuruShots','GuruShots',NULL,0,'https://gurushots.com/',1,1,392,5),(354,'https://zapier.com/','Zapier','Zapier',NULL,0,'https://zapier.com/',2,1,393,13),(355,'https://app.hubspot.com/','Hubspot','Hubspot',NULL,0,'https://www.hubspot.com',1,1,394,15),(356,'https://mail.mailkitchen.com/','MailKitchen','MailKitchen',NULL,0,'http://www.mailkitchen.com/',0,1,395,15),(357,'https://app.instaply.com/','Instaply','Instaply',NULL,0,'https://www.instaply.com/',1,1,396,15),(358,'https://espace-recruteurs.cadremploi.fr/','Cadremploi Recruteurs','CadremploiRecruteurs',NULL,0,'https://espace-recruteurs.cadremploi.fr/',0,1,397,10),(359,'https://www.cadremploi.fr/','Cadremploi Candidats','CadremploiCandidats',NULL,0,'https://www.cadremploi.fr/',1,1,398,10),(360,'https://www.infogreffe.fr/','Infogreffe','Infogreffe',NULL,0,'https://www.infogreffe.fr/',1,1,399,2),(361,'http://www.radio.fr/','Radio.fr','RadioFr',NULL,0,'http://www.radio.fr/',1,1,400,11),(362,'https://www.loisirsencheres.com/','Loisirs Encheres','LoisirsEncheres',NULL,0,'https://www.loisirsencheres.com/',3,1,401,18),(363,'http://slacksocial.com/','Slack Social','SlackSocial',NULL,0,'http://slacksocial.com/',0,1,402,15),(364,'https://www.algolia.com/','Algolia','Algolia',NULL,0,'https://www.algolia.com/',0,1,403,4),(365,'https://www.indeed.fr/','Indeed','Indeed',NULL,0,'https://www.indeed.fr/',5,1,404,10),(366,'https://ulule.com','Ulule','Ulule',NULL,0,'https://ulule.com',1,1,405,15),(367,'https://dashboard.stripe.com/','Stripe','Stripe',NULL,0,'https://stripe.com/fr',4,1,406,12),(368,'https://www.foodcheri.com/','Food Cheri','FoodCheri',NULL,0,'https://www.foodcheri.com/',0,1,407,8),(369,'https://www.misterbilingue.com/','Mister Bilingue Candidat','MisterBilingueCandidat',NULL,0,'https://www.misterbilingue.com/',1,1,408,10),(370,'https://www.misterbilingue.com/','Mister Bilingue Recruteur','MisterBilingueRecruteur',NULL,0,'https://www.misterbilingue.com/',0,1,409,10),(371,'https://crm.zoho.com/','Zoho CRM','ZohoCRM',NULL,0,'https://crm.zoho.com/',0,1,410,15),(372,'https://www.plex.tv/','Plex.tv','PlexTv',NULL,0,'https://www.plex.tv/',0,1,411,11),(373,'https://mailbilly.com/','Mail Billy','MailBilly',NULL,0,'https://mailbilly.com/',1,1,412,15),(374,'https://app.mailjet.com/','Mailjet','Mailjet',NULL,0,'https://www.mailjet.com/',2,1,413,15),(375,'https://app.frontapp.com/','FrontApp','FrontApp',NULL,0,'https://frontapp.com/',0,1,414,15),(376,'http://www.flaticon.com/','Flaticon','Flaticon',NULL,0,'http://www.flaticon.com/',4,1,415,5),(377,'https://freshsales.io','Freshsales','Freshsales',NULL,0,'https://freshsales.io',0,1,416,15),(378,'https://accounts.google.com','Google Photos','GooglePhotos',1,0,'https://photos.google.com',6,1,417,7),(379,'https://accounts.google.com','Google Keep','GoogleKeep',1,0,'https://keep.google.com',5,1,418,13),(380,'https://accounts.google.com','Google Hangouts','GoogleHangouts',1,0,'https://hangouts.google.com',3,1,419,3),(381,'https://accounts.google.com','GSuite','GSuite',1,0,'https://gsuite.google.com',4,1,420,14),(382,'https://login.freeagent.com','FreeAgent','FreeAgent',NULL,0,'https://www.freeagent.com',0,1,421,12),(383,'https://vk.com/','VK','VK',NULL,0,'https://vk.com/',0,1,422,17),(384,'https://cloud.digitalocean.com/','DigitalOcean','DigitalOcean',NULL,0,'https://www.digitalocean.com/',0,1,423,2),(385,'https://todoist.com/','Todoist','Todoist',NULL,0,'https://todoist.com/',2,1,424,14),(386,'https://papaly.com/','Papaly','Papaly',NULL,0,'https://papaly.com/',1,1,425,13),(387,'https://infogr.am/','Infogram','Infogram',NULL,0,'https://infogr.am/',1,1,426,1),(388,'https://www.raja.fr/','Raja','Raja',NULL,0,'https://www.raja.fr/',1,1,427,16),(389,'https://stackoverflow.com/','Stackoverflow','Stackoverflow',NULL,0,'https://stackoverflow.com/',0,1,428,4),(390,'https://app.craft.io/','Craft','Craft',NULL,0,'https://craft.io/',0,1,429,14),(391,'https://www.freecodecamp.com/','Free Code Camp','FreeCodeCamp',NULL,0,'https://www.freecodecamp.com/',3,1,430,6),(392,'https://www.workable.com/','Workable','Workable',NULL,0,'https://www.workable.com/',0,1,431,10),(393,'https://www.wrike.com/','Wrike','Wrike',NULL,0,'https://www.wrike.com/',1,1,432,3),(394,'https://app.close.io/','Close.io','CloseIo',NULL,0,'https://close.io/',0,1,433,15),(395,'https://app.moqups.com/','Moqups','Moqups',NULL,0,'https://moqups.com/',0,1,434,5),(396,'https://my.appcues.com/','Appcues','Appcues',NULL,0,'https://www.appcues.com/',0,1,435,15),(397,'https://fly2.customer.io/','Customer.io','CustomerIo',NULL,0,'https://customer.io/',0,1,436,15),(398,'https://www.talentoday.com/','Talentoday','Talentoday',NULL,0,'https://www.talentoday.com/',1,1,437,10),(399,'https://www.scholarvox.com/','Scholarvox','Scholarvox',NULL,0,'https://www.scholarvox.com/',0,1,438,6),(400,'https://myisc.iscparis.com/','My ISC','MyISC',NULL,0,'https://myisc.iscparis.com/',0,1,439,NULL),(401,'https://moodle.iscparis.com/','Moodle','MoodleISCParis',NULL,0,'https://moodle.iscparis.com/',0,1,440,NULL),(402,'https://www.jobteaser.com/','JobTeaser ISC','JobTeaserISC',NULL,0,'https://www.jobteaser.com/',0,1,441,NULL),(403,'https://iscparis.p.iagora.com/','Iagora','IagoraISCParis',NULL,0,'https://iscparis.p.iagora.com/',0,1,442,NULL),(404,'https://iscparis.studapart.com/','Housing Center','ISCParisStudapart',NULL,0,'https://iscparis.studapart.com/',0,1,443,NULL),(405,'https://app.centraltest.com/','CentralTest','CentralTestISCParis',NULL,0,'https://app.centraltest.com/',0,1,444,NULL),(406,'https://www.welcomekit.co/users/sign_in','WelcomeKit','WelcomeKit',NULL,0,'https://www.welcomekit.co',1,1,445,10),(407,'https://www.lalalab.com/','LALALAB.','Lalalab',NULL,0,'https://www.lalalab.com/',0,1,446,16),(408,'azdazdazdzdzd','azdazdazdzdzd','undefined',NULL,0,'azdazdazdzdzd',0,1,451,NULL),(409,'dddddjdj','dddddjdj','undefined',NULL,0,'dddddjdj',0,1,452,NULL),(410,'https://companies.intra.42.fr','42 Entreprises','CompaniesIntra42',NULL,0,'https://companies.intra.42.fr',0,1,453,NULL),(411,'https://www.coinbase.com/signin','Coinbase','Coinbase',NULL,0,'https://www.coinbase.com',0,1,454,12),(412,'https://app.instapage.com/auth/login','Instapage','Instapage',NULL,0,'https://instapage.com/',0,1,455,15),(413,'https://platform.tech/','The Family Platform','TheFamilyPlatform',NULL,0,'https://platform.tech/',0,1,456,14),(414,'https://adobeid-na1.services.adobe.com/','Adobe','Adobe',NULL,0,'https://www.adobe.com/',0,1,457,14),(415,'https://www.incwo.com/','Incwo','Incwo',NULL,0,'https://www.incwo.com/',0,1,458,NULL),(416,'https://webmail.1and1.fr/','https://webmail.1and1.fr/','undefined',NULL,0,'https://webmail.1and1.fr/',0,1,459,NULL),(417,'https://purse.io/','Purse','Purse',NULL,0,'https://purse.io/',0,1,460,NULL),(419,'https://localbitcoins.com/','LocalBitcoins','LocalBitcoins',NULL,0,'https://localbitcoins.com/',0,1,462,12),(420,'https://pro.boutique.laposte.fr/','La Poste Business','LaPosteBusiness',NULL,0,'https://pro.boutique.laposte.fr/',0,1,463,3),(421,'https://accounts.google.com/','Google Docs','GoogleDocs',1,0,'https://docs.google.com/',0,1,464,7),(422,'https://www.bitstamp.net/','Bitstamp','Bitstamp',NULL,0,'https://www.bitstamp.net/',0,1,465,NULL),(423,'www.twitter.com/kantoutacou','www.twitter.com/kantoutacou','undefined',NULL,0,'www.twitter.com/kantoutacou',0,1,466,NULL),(424,'https://live.blockcypher.com/','BlockCypher','Blockcypher',NULL,0,'https://live.blockcypher.com/',0,1,467,NULL),(425,'https://reservation.citiz.fr/','Citiz Lille Arras','CitizLilleArras',NULL,0,'https://citiz.coop/',0,1,468,NULL),(426,'https://www.name.com/account','Name.com','NameCom',NULL,0,'https://www.name.com/',0,1,469,NULL),(427,'https://www.bitstamp.net/account/balance/','Bitstamp','Bitstamp',NULL,0,'https://www.bitstamp.net/',0,1,470,NULL),(428,'https://live.blockcypher.com/login','Blockcypher','Blockcypher',NULL,0,'https://live.blockcypher.com',0,1,471,12),(429,'https://mixpanel.com','Mixpanel','Mixpanel',NULL,0,'https://mixpanel.com',0,1,472,1),(430,'https://www.humblebundle.com/','Humble Bundle','HumbleBundle',NULL,0,'https://www.humblebundle.com/',0,1,473,17),(431,'https://www.sikana.tv/','sikana.tv','SikanaTv',NULL,0,'https://www.sikana.tv/',0,1,474,11),(432,'https://www.solidatech.fr/','SolidaTech','SolidaTech',NULL,0,'https://www.solidatech.fr/',0,1,475,13),(433,'https://app.linklead.io/','LinkLead','LinkLead',NULL,0,'https://linklead.io/',0,1,476,15),(434,'https://paybis.com/','Paybis','Paybis',NULL,0,'https://paybis.com/',0,1,477,12),(435,'https://wallet.advcash.com/','Advanced Cash','AdvCash',NULL,0,'https://advcash.com/',0,1,478,12),(436,'http://espaceperso.cacomptepourmoi.fr/','Ca Compte Pour Moi','CaComptePourMoi',NULL,0,'https://cacomptepourmoi.fr',0,1,479,12),(437,'https://espace-entreprises.humanis.com/','Humanis Entreprises','HumanisEntreprises',NULL,0,'https://espace-entreprises.humanis.com/',0,1,480,2),(438,'https://sageone.bulletinsdepaye.com/','Sage One Paie','SageOnePaie',NULL,0,'https://sage.fr',0,1,481,12),(439,'https://www.noova.co/admin0/','Noova PrestaShop','NoovaPrestaShop',NULL,0,'https://www.noova.co/admin0/',0,1,482,NULL),(440,'http://www.pleinlesyeux.co/wp-admin/','Plein les yeux WordPress','PleinLesYeuxWordPress',NULL,0,'http://www.pleinlesyeux.co/wp-admin/',0,1,483,NULL),(441,'https://blog.noova.co/wp-admin/','Noova WordPress','BlogNoovaWordPress',NULL,0,'https://blog.noova.co/wp-admin/',0,1,484,NULL),(442,'https://www.pleinlesyeux.co/wp-admin/','Plein les yeux WordPress','PleinLesYeuxWordPress',NULL,0,'https://www.pleinlesyeux.co/wp-admin/',0,1,485,NULL),(443,'https://venteprivee.noova.co/admin0','Noova vente privee','NoovaVentePriveePrestaShop',NULL,0,'https://venteprivee.noova.co/admin0',0,1,486,NULL),(444,'https://test.noova.co/admin0/','Noova test','NoovaTestPrestaShop',NULL,0,'https://test.noova.co/admin0/',0,1,487,NULL),(445,'https://sso.godaddy.com/','GoDaddy','GoDaddy',NULL,0,'https://godaddy.com/',0,1,488,4),(446,'www.testt.com','www.testt.com','undefined',NULL,0,'www.testt.com',0,1,489,NULL),(447,'https://tree.taiga.io/','TAIGA','Taiga',NULL,0,'https://taiga.io',0,1,490,14),(448,'https://www.chronopost.fr/','Chronopost','Chronopost',NULL,0,'https://www.chronopost.fr/',0,1,491,2),(449,'https://www.appvizer.fr/','appvizer','Appvizer',NULL,0,'https://www.appvizer.fr/',0,1,492,13),(450,'https://www.yooda.com/','Yooda','Yooda',NULL,0,'https://www.yooda.com/',0,1,493,13),(451,'https://www.daycause.com/','Daycause','Daycause',NULL,0,'https://www.daycause.com/',0,1,494,15),(452,'https://accounts.google.com/','Google Partners','GooglePartners',1,0,'https://www.google.com/partners',0,1,495,14),(453,'https://www.ebay.fr/','Ebay','Ebay',NULL,0,'https://www.ebay.fr/',0,1,496,16),(454,'https://elocky.com/admin','elocky admin','ElockyAdmin',NULL,0,'https://elocky.com/admin',0,1,497,NULL),(455,'https://app.dougs.fr','dougs','Dougs',NULL,0,'https://dougs.fr',0,1,498,12),(456,'ex3.mail.ovh.net','ex3.mail.ovh.net','undefined',NULL,0,'ex3.mail.ovh.net',0,1,499,NULL),(457,'https://www.smartlook.com/','Smartlook','Smartlook',NULL,0,'https://www.smartlook.com/',0,1,500,1),(458,'https://dashboard.smartsupp.com/','Smartsupp','Smartsupp',NULL,0,'https://dashboard.smartsupp.com/',0,1,501,15),(459,'https://itunesconnect.apple.com/','iTunes connect','iTunesConnect',NULL,0,'https://itunesconnect.apple.com/',0,1,502,4),(460,'https://gitlab.com/users/sign_in','Gitlab','Gitlab',NULL,0,'https://gitlab.com/',0,1,503,4),(461,'https://airtable.com/login','Airtable','Airtable',NULL,0,'https://airtable.com/',0,1,504,13),(462,'https://app.zeplin.io/','Zeplin','Zeplin',NULL,0,'https://zeplin.io/',0,1,505,5),(463,'https://ex3.mail.ovh.net','https://ex3.mail.ovh.net','undefined',NULL,0,'https://ex3.mail.ovh.net',0,1,506,NULL),(464,'https://www.colissimo.entreprise.laposte.fr/fr','https://www.colissimo.entreprise.laposte.fr/fr','undefined',NULL,0,'https://www.colissimo.entreprise.laposte.fr/fr',0,1,507,NULL),(465,'https://controlpanel.amen.fr/','amen.fr','Amen',NULL,0,'https://amen.fr/',0,1,508,4),(466,'https://www.origin.com/fra/fr-fr/store','https://www.origin.com/fra/fr-fr/store','undefined',NULL,0,'https://www.origin.com/fra/fr-fr/store',0,1,509,NULL),(467,'https://login.live.com/','Microsoft','MicrosoftAccount',NULL,0,'https://accounts.microsoft.com/',0,1,510,NULL),(474,'https://app.forestadmin.com','Forest admin','ForestAdmin',NULL,0,'https://www.forestadmin.com/',0,1,517,NULL),(475,'http://www.pole-emploi.fr','In progress','undefined',NULL,0,'http://www.pole-emploi.fr',0,1,518,NULL),(477,'https://a2.alsofrance.fr/','ALSO','AlsoFrance',NULL,0,'https://www.also.com/',0,1,520,NULL),(479,'https://hubic.com/home/','hubiC','HubiC',NULL,0,'https://hubic.com/',0,1,522,NULL),(480,'https://account.synology.com/','Synology','Synology',NULL,0,'https://synology.com/',0,1,523,NULL),(482,'https://store.speedtree.com/','Speedtree','Speedtree',NULL,0,'https://store.speedtree.com/',0,1,525,NULL),(483,'https://3dsky.org/','3dsky','3dsky',NULL,0,'https://3dsky.org/',0,1,526,NULL),(484,'https://www.allegorithmic.com/','allegorithmic','Allegorithmic',NULL,0,'https://www.allegorithmic.com/',0,1,527,NULL),(486,'https://www.materiel.net/','materiel.net','Materiel.net',NULL,0,'https://www.materiel.net/',0,1,529,NULL),(487,'https://store.unity.com/','Unity','Unity',NULL,0,'https://store.unity.com/',0,1,530,NULL),(488,'http://www.bonscadeauxgrenke.fr','Bons cadeaux GRENKE','BonsCadeauxGrenke',NULL,0,'http://www.bonscadeauxgrenke.fr',0,1,531,NULL),(489,'https://client.edenred.fr/','Edenred','ClientEdenred',NULL,0,'https://client.edenred.fr/',0,1,532,NULL),(492,'https://smallpdf.com/','In progress','undefined',NULL,0,'https://smallpdf.com/',0,1,535,NULL),(493,'https://cloudconvert.com/','In progress','undefined',NULL,0,'https://cloudconvert.com/',0,1,536,NULL),(494,'http://www.dailymotion.com/','Dailymotion','Dailymotion',NULL,0,'http://www.dailymotion.com/',0,1,537,NULL),(495,'https://www.betaseries.com/','In progress','undefined',NULL,0,'https://www.betaseries.com/',0,1,538,NULL),(496,'http://www.minecraft.net/','In progress','undefined',NULL,0,'http://www.minecraft.net/',0,1,539,NULL),(497,'https://alan.eu','Alan','Alan',NULL,0,'https://alan.eu',0,1,540,2),(498,'http://blog.kiwanisbeaujolais.com/','In progress','undefined',NULL,0,'http://blog.kiwanisbeaujolais.com/',0,1,541,NULL),(499,'https://web.crowdfireapp.com/','Crowdfire','CrowdfireApp',NULL,0,'https://crowdfireapp.com/',0,1,542,NULL),(500,'https://www.cic.fr/fr/authentification.html','In progress','undefined',NULL,0,'https://www.cic.fr/fr/authentification.html',0,1,543,NULL),(501,'https://www.cic.fr/fr/authentification.html','In progress','undefined',NULL,0,'https://www.cic.fr/fr/authentification.html',0,1,544,NULL),(502,'https://cfspro.impots.gouv.fr/','Impots.gouv Pro','ImpotsGouvPro',NULL,0,'https://www.impots.gouv.fr',0,1,545,NULL),(503,'https://www.cic.fr/fr/authentification.html','In progress','undefined',NULL,0,'https://www.cic.fr/fr/authentification.html',0,1,546,NULL),(505,'https://www.bricodepot.fr/','Brico Dépôt','BricoDepot',NULL,0,'https://www.bricodepot.fr/',0,1,548,NULL),(506,'https://www.sellsy.fr/','Sellsy','Sellsy',NULL,0,'https://www.sellsy.fr/',0,1,549,NULL),(507,'https://gronline.total.fr/','Total GR Online','TotalGROnline',NULL,0,'https://gronline.total.fr/',0,1,550,NULL),(509,'https://startuponly.com/','StartupOnly','StartupOnly',NULL,0,'https://startuponly.com/',0,1,552,NULL),(510,'https://www.malt.fr','malt','Malt',NULL,0,'https://www.malt.fr',0,1,553,NULL),(511,'https://signin.aws.amazon.com/','AWS','AWS',NULL,0,'https://aws.amazon.com',0,1,554,NULL),(513,'https://www.voyages-sncf.com/vpro-entreprises/','SNCF Pro','VoyagesSncfPro',NULL,0,'https://www.voyages-sncf.com/vpro-entreprises/',0,1,556,NULL),(514,'https://www.cadastre.gouv.fr/scpc/accueil.do','In progress','undefined',NULL,0,'https://www.cadastre.gouv.fr/scpc/accueil.do',0,1,557,NULL),(515,'http://professionnels.ign.fr/','IGN','IGNPro',NULL,0,'http://professionnels.ign.fr/',0,1,558,NULL),(516,'https://m2mexpress.fr.orange-business.com/mac/customer/home.do','Orange Business Services','OrangeBusinessServices',NULL,0,'https://m2mexpress.fr.orange-business.com/mac/customer/home.do',0,1,559,NULL),(517,'https://beta.icloud.com','In progress','undefined',NULL,0,'https://beta.icloud.com',0,1,560,NULL),(519,'https://www.courrierinternational.com','In progress','undefined',NULL,0,'https://www.courrierinternational.com',0,1,562,NULL),(521,'https://espaceclientv3.orange.fr/','Orange','OrangePro',NULL,0,'https://pro.orange.fr/espace-client.html',0,1,564,NULL),(522,'https://www.jemepropose.com/','Jemepropose','Jemepropose',NULL,0,'https://www.jemepropose.com/',0,1,565,NULL),(523,'https://jechercheundev.fr','JeChercheUnDev.fr','JeChercheUnDev',NULL,0,'https://jechercheundev.fr',0,1,566,NULL),(524,'https://up.hidden.market/','Hidden.market','HiddenMarket',NULL,0,'https://up.hidden.market/',0,1,567,NULL),(526,'https://onesignal.com/','OneSignal','OneSignal',NULL,0,'https://onesignal.com/',0,1,569,NULL),(527,'https://www.ubereats.com/stores/','UberEats','UberEats',NULL,0,'https://www.ubereats.com/stores/',0,1,570,8),(528,'https://www.pottermore.com/','In progress','undefined',NULL,0,'https://www.pottermore.com/',0,1,571,NULL),(529,'https://clients.equidem-conseil.fr/','Equidem Conseil','EquidemConseil',NULL,0,'https://clients.equidem-conseil.fr/',0,1,572,NULL),(530,'https://actemis1crm.3acrm.info','In progress','undefined',NULL,0,'https://actemis1crm.3acrm.info',0,1,573,NULL),(531,'https://www.bouyguestelecom.fr/mon-compte/dashboard','In progress','undefined',NULL,0,'https://www.bouyguestelecom.fr/mon-compte/dashboard',0,1,574,NULL),(532,'https://admin.thegoodcar.fr/','In progress','undefined',NULL,0,'https://admin.thegoodcar.fr/',0,1,575,NULL),(533,'https://www.exaprint.fr/','Exaprint','Exaprint',NULL,0,'https://www.exaprint.fr/',0,1,576,NULL),(534,'https://www.icloud.com/#notes2','In progress','undefined',NULL,0,'https://www.icloud.com/#notes2',0,1,577,NULL),(535,'https://webapp.yousign.com/#!/','yousign','YouSign',NULL,0,'https://webapp.yousign.com/#!/',0,1,578,NULL),(536,'https://accounts.google.com/','Firebase','Firebase',1,0,'https://console.firebase.google.com/',0,1,579,NULL),(537,'https://www.sfr.fr','In progress','undefined',NULL,0,'https://www.sfr.fr',0,1,580,NULL),(538,'https://www.etoro.com','In progress','undefined',NULL,0,'https://www.etoro.com',0,1,581,NULL),(540,'https://app.flatchr.io/','Flacthr','Flatchr',NULL,0,'https://flatchr.io/',0,1,583,NULL),(541,'https://start.me/','start.me','start.me',NULL,0,'https://start.me/',0,1,584,NULL),(542,'https://anyleads.com/login/','Anyleads','Anyleads',NULL,0,'https://anyleads.com/',0,1,585,NULL),(543,'https://www.sharecg.com','ShareCG','ShareCG',NULL,0,'https://www.sharecg.com',0,1,586,NULL),(544,'https://www.zendesk.com/','Zendesk','Zendesk',NULL,0,'https://www.zendesk.com/',0,1,587,NULL),(545,'https://kwiksurveys.com/','In progress','undefined',NULL,0,'https://kwiksurveys.com/',0,1,588,NULL),(546,'https://www.creditdunord.com','In progress','undefined',NULL,0,'https://www.creditdunord.com',0,1,589,NULL),(547,'http://quizbiz.fr/','QuizBiz','QuizBiz',NULL,0,'http://quizbiz.fr/',0,1,590,NULL),(548,'https://app.surveygizmo.com/login/v1','SurveyGizmo','SurveyGizmo',NULL,0,'https://www.surveygizmo.com/',0,1,591,NULL),(549,'https://app.oneup.com','OneUp','OneUp',NULL,0,'https://app.oneup.com',0,1,592,NULL),(550,'https://www.uber.com/fr/','In progress','undefined',NULL,0,'https://www.uber.com/fr/',0,1,593,NULL),(551,'https://actemis1crm.3acrm.info/login.php','In progress','undefined',NULL,0,'https://actemis1crm.3acrm.info/login.php',0,1,594,NULL),(552,'Feedly','In progress','undefined',NULL,0,'Feedly',0,1,595,NULL),(553,'https://riders.uber.com/trips','In progress','undefined',NULL,0,'https://riders.uber.com/trips',0,1,596,NULL),(554,'https://www.streak.com','In progress','undefined',NULL,0,'https://www.streak.com',0,1,597,NULL),(555,'https://ease.space','In progress','undefined',NULL,0,'https://ease.space',0,1,598,NULL),(556,'https://ease.space','In progress','undefined',NULL,0,'https://ease.space',0,1,599,NULL),(557,'https://ease.space','In progress','undefined',NULL,0,'https://ease.space',0,1,600,NULL);
/*!40000 ALTER TABLE `websites` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `websitesInformations`
--

LOCK TABLES `websitesInformations` WRITE;
/*!40000 ALTER TABLE `websitesInformations` DISABLE KEYS */;
INSERT INTO `websitesInformations` VALUES (1,5,'login','text',1,'Login','fa-user-o'),(2,7,'login','text',1,'Login','fa-user-o'),(3,28,'login','text',1,'Login','fa-user-o'),(4,35,'login','text',1,'Login','fa-user-o'),(5,46,'login','text',1,'Login','fa-user-o'),(6,47,'login','text',1,'Login','fa-user-o'),(7,49,'login','text',1,'Login','fa-user-o'),(8,50,'login','text',1,'Login','fa-user-o'),(9,51,'login','text',1,'Login','fa-user-o'),(10,52,'login','text',1,'Login','fa-user-o'),(11,53,'login','text',1,'Login','fa-user-o'),(12,54,'login','text',1,'Login','fa-user-o'),(13,55,'login','text',1,'Login','fa-user-o'),(14,56,'login','text',1,'Login','fa-user-o'),(15,57,'login','text',1,'Login','fa-user-o'),(16,58,'login','text',1,'Login','fa-user-o'),(17,59,'login','text',1,'Login','fa-user-o'),(18,60,'login','text',1,'Login','fa-user-o'),(19,61,'login','text',1,'Login','fa-user-o'),(20,62,'login','text',1,'Login','fa-user-o'),(21,63,'login','text',1,'Login','fa-user-o'),(22,66,'login','text',1,'Login','fa-user-o'),(23,67,'login','text',1,'Login','fa-user-o'),(24,69,'login','text',1,'Login','fa-user-o'),(25,70,'login','text',1,'Login','fa-user-o'),(26,71,'login','text',1,'Login','fa-user-o'),(27,72,'login','text',1,'Login','fa-user-o'),(28,73,'login','text',1,'Login','fa-user-o'),(29,74,'login','text',1,'Login','fa-user-o'),(30,75,'login','text',1,'Login','fa-user-o'),(31,77,'login','text',1,'Login','fa-user-o'),(33,86,'login','text',1,'Login','fa-user-o'),(34,87,'login','text',1,'Login','fa-user-o'),(35,89,'login','text',1,'Login','fa-user-o'),(36,90,'login','text',1,'Login','fa-user-o'),(37,91,'login','text',1,'Login','fa-user-o'),(38,92,'login','text',1,'Login','fa-user-o'),(39,93,'login','text',1,'Login','fa-user-o'),(40,94,'login','text',1,'Login','fa-user-o'),(41,97,'login','text',1,'Login','fa-user-o'),(42,98,'login','text',1,'Login','fa-user-o'),(44,100,'login','text',1,'Login','fa-user-o'),(45,102,'login','text',1,'Login','fa-user-o'),(46,103,'login','text',1,'Login','fa-user-o'),(47,104,'login','text',1,'Login','fa-user-o'),(48,106,'login','text',1,'Login','fa-user-o'),(49,107,'login','text',1,'Login','fa-user-o'),(50,110,'login','text',1,'Login','fa-user-o'),(51,117,'login','text',1,'Login','fa-user-o'),(52,118,'login','text',1,'Login','fa-user-o'),(53,119,'login','text',1,'Login','fa-user-o'),(54,120,'login','text',1,'Login','fa-user-o'),(55,121,'login','text',1,'Login','fa-user-o'),(56,122,'login','text',1,'Login','fa-user-o'),(57,123,'login','text',1,'Login','fa-user-o'),(58,124,'login','text',1,'Login','fa-user-o'),(59,126,'login','text',1,'Login','fa-user-o'),(60,128,'login','text',1,'Login','fa-user-o'),(61,130,'login','text',1,'Login','fa-user-o'),(62,131,'login','text',1,'Login','fa-user-o'),(63,132,'login','text',1,'Login','fa-user-o'),(64,133,'login','text',1,'Login','fa-user-o'),(66,135,'login','text',1,'Login','fa-user-o'),(67,136,'login','text',1,'Login','fa-user-o'),(68,137,'login','text',1,'Login','fa-user-o'),(69,138,'login','text',1,'Login','fa-user-o'),(70,139,'login','text',1,'Login','fa-user-o'),(71,140,'login','text',1,'Login','fa-user-o'),(72,142,'login','text',1,'Login','fa-user-o'),(73,144,'login','text',1,'Login','fa-user-o'),(74,145,'login','text',1,'Login','fa-user-o'),(75,146,'login','text',1,'Login','fa-user-o'),(76,149,'login','text',1,'Login','fa-user-o'),(77,150,'login','text',1,'Login','fa-user-o'),(78,153,'login','text',1,'Login','fa-user-o'),(79,154,'login','text',1,'Login','fa-user-o'),(80,155,'login','text',1,'Login','fa-user-o'),(81,156,'login','text',1,'Login','fa-user-o'),(82,157,'login','text',1,'Login','fa-user-o'),(83,158,'login','text',1,'Login','fa-user-o'),(84,159,'login','text',1,'Login','fa-user-o'),(85,161,'login','text',1,'Login','fa-user-o'),(86,64,'login','text',1,'Login','fa-user-o'),(87,65,'login','text',1,'Login','fa-user-o'),(88,68,'login','text',1,'Login','fa-user-o'),(89,129,'login','text',1,'Login','fa-user-o'),(255,162,'login','text',1,'Login','fa-user-o'),(257,163,'login','text',1,'Login','fa-user-o'),(259,164,'login','text',1,'Login','fa-user-o'),(261,165,'login','text',1,'Login','fa-user-o'),(263,166,'login','text',1,'Login','fa-user-o'),(265,167,'login','text',1,'Login','fa-user-o'),(267,168,'login','text',1,'Login','fa-user-o'),(269,169,'login','text',1,'Login','fa-user-o'),(271,170,'login','text',1,'Login','fa-user-o'),(273,170,'team','text',0,'Team','fa-slack'),(274,171,'login','text',1,'Login','fa-user-o'),(276,172,'login','text',1,'Login','fa-user-o'),(278,173,'login','text',1,'Login','fa-user-o'),(280,174,'login','text',1,'Login','fa-user-o'),(282,175,'login','text',1,'Login','fa-user-o'),(284,176,'login','text',1,'Login','fa-user-o'),(286,177,'login','text',1,'Login','fa-user-o'),(288,178,'login','text',1,'Login','fa-user-o'),(290,179,'login','text',1,'Login','fa-user-o'),(292,180,'login','text',1,'Login','fa-user-o'),(294,181,'login','text',1,'Login','fa-user-o'),(298,183,'login','text',1,'Login','fa-user-o'),(300,184,'login','text',1,'Login','fa-user-o'),(302,185,'login','text',1,'Login','fa-user-o'),(304,186,'login','text',1,'Login','fa-user-o'),(306,187,'login','text',1,'Login','fa-user-o'),(308,188,'login','text',1,'Login','fa-user-o'),(310,189,'login','text',1,'Login','fa-user-o'),(312,190,'login','text',1,'Login','fa-user-o'),(316,192,'login','text',1,'Login','fa-user-o'),(318,193,'login','text',1,'Login','fa-user-o'),(320,194,'login','text',1,'Login','fa-user-o'),(322,195,'login','text',1,'Login','fa-user-o'),(324,196,'login','text',1,'Login','fa-user-o'),(326,197,'login','text',1,'Login','fa-user-o'),(328,198,'login','text',1,'Login','fa-user-o'),(330,199,'login','text',1,'Login','fa-user-o'),(332,200,'login','text',1,'Login','fa-user-o'),(334,201,'login','text',1,'Login','fa-user-o'),(338,203,'login','text',1,'Login','fa-user-o'),(340,204,'login','text',1,'Login','fa-user-o'),(342,205,'login','text',1,'Login','fa-user-o'),(346,207,'login','text',1,'Login','fa-user-o'),(350,209,'login','text',1,'Login','fa-user-o'),(354,211,'login','text',1,'Login','fa-user-o'),(356,212,'login','text',1,'Login','fa-user-o'),(358,213,'login','text',1,'Login','fa-user-o'),(360,214,'login','text',1,'Login','fa-user-o'),(362,215,'login','text',1,'Login','fa-user-o'),(364,216,'login','text',1,'Login','fa-user-o'),(366,217,'login','text',1,'Login','fa-user-o'),(368,218,'login','text',1,'Login','fa-user-o'),(370,219,'login','text',1,'Login','fa-user-o'),(372,220,'login','text',1,'Login','fa-user-o'),(374,221,'login','text',1,'Login','fa-user-o'),(376,222,'login','text',1,'Login','fa-user-o'),(378,223,'login','text',1,'Login','fa-user-o'),(380,224,'login','text',1,'Login','fa-user-o'),(382,225,'login','text',1,'Login','fa-user-o'),(384,226,'login','text',1,'Login','fa-user-o'),(386,227,'login','text',1,'Login','fa-user-o'),(388,228,'login','text',1,'Login','fa-user-o'),(390,229,'login','text',1,'Login','fa-user-o'),(392,230,'login','text',1,'Login','fa-user-o'),(394,231,'login','text',1,'Login','fa-user-o'),(396,232,'login','text',1,'Login','fa-user-o'),(398,233,'login','text',1,'Login','fa-user-o'),(400,234,'login','text',1,'Login','fa-user-o'),(402,235,'login','text',1,'Login','fa-user-o'),(403,236,'login','text',1,'Login','fa-user-o'),(404,237,'login','text',1,'Login','fa-user-o'),(405,238,'login','text',1,'Login','fa-user-o'),(406,239,'login','text',1,'Login','fa-user-o'),(407,240,'login','text',1,'Login','fa-user-o'),(408,241,'login','text',1,'Login','fa-user-o'),(410,243,'login','text',1,'Login','fa-user-o'),(411,244,'login','text',1,'Login','fa-user-o'),(412,245,'login','text',1,'Login','fa-user-o'),(413,246,'login','text',1,'Login','fa-user-o'),(414,247,'login','text',1,'Login','fa-user-o'),(415,248,'login','text',1,'Login','fa-user-o'),(416,249,'login','text',1,'Login','fa-user-o'),(417,250,'login','text',1,'Login','fa-user-o'),(418,251,'login','text',1,'Login','fa-user-o'),(419,252,'login','text',1,'Login','fa-user-o'),(420,253,'login','text',1,'Login','fa-user-o'),(421,254,'login','text',1,'Login','fa-user-o'),(422,255,'login','text',1,'Login','fa-user-o'),(423,256,'login','text',1,'Login','fa-user-o'),(425,258,'login','text',1,'Login','fa-user-o'),(426,259,'login','text',1,'Login','fa-user-o'),(427,260,'login','text',1,'Login','fa-user-o'),(428,261,'login','text',1,'Login','fa-user-o'),(429,262,'login','text',1,'Login','fa-user-o'),(430,263,'login','text',1,'Login','fa-user-o'),(431,264,'login','text',1,'Login','fa-user-o'),(432,265,'login','text',1,'Login','fa-user-o'),(433,266,'login','text',1,'Login','fa-user-o'),(434,267,'login','text',1,'Login','fa-user-o'),(435,268,'login','text',1,'Login','fa-user-o'),(436,269,'login','text',1,'Login','fa-user-o'),(437,270,'login','text',1,'Login','fa-user-o'),(438,271,'login','text',1,'Login','fa-user-o'),(439,272,'login','text',1,'Login','fa-user-o'),(440,273,'login','text',1,'Login','fa-user-o'),(441,274,'login','text',1,'Login','fa-user-o'),(442,275,'login','text',1,'Login','fa-user-o'),(443,276,'login','text',1,'Login','fa-user-o'),(444,277,'login','text',1,'Login','fa-user-o'),(445,278,'login','text',1,'Login','fa-user-o'),(446,279,'login','text',1,'Login','fa-user-o'),(447,280,'login','text',1,'Login','fa-user-o'),(448,281,'login','text',1,'Login','fa-user-o'),(449,282,'login','text',1,'Login','fa-user-o'),(450,283,'login','text',1,'Login','fa-user-o'),(452,285,'login','text',1,'Login','fa-user-o'),(453,286,'login','text',1,'Login','fa-user-o'),(454,287,'login','text',1,'Login','fa-user-o'),(455,288,'login','text',1,'Login','fa-user-o'),(456,289,'login','text',1,'Login','fa-user-o'),(457,290,'login','text',1,'Login','fa-user-o'),(458,291,'login','text',1,'Login','fa-user-o'),(460,293,'login','text',1,'Login','fa-user-o'),(462,295,'login','text',1,'Login','fa-user-o'),(463,296,'login','text',1,'Login','fa-user-o'),(465,5,'password','password',2,'Password','fa-lock'),(466,7,'password','password',2,'Password','fa-lock'),(467,28,'password','password',2,'Password','fa-lock'),(468,35,'password','password',2,'Password','fa-lock'),(469,46,'password','password',2,'Password','fa-lock'),(470,47,'password','password',2,'Password','fa-lock'),(471,49,'password','password',2,'Password','fa-lock'),(472,50,'password','password',2,'Password','fa-lock'),(473,51,'password','password',2,'Password','fa-lock'),(474,52,'password','password',2,'Password','fa-lock'),(475,53,'password','password',2,'Password','fa-lock'),(476,54,'password','password',2,'Password','fa-lock'),(477,55,'password','password',2,'Password','fa-lock'),(478,56,'password','password',2,'Password','fa-lock'),(479,57,'password','password',2,'Password','fa-lock'),(480,58,'password','password',2,'Password','fa-lock'),(481,59,'password','password',2,'Password','fa-lock'),(482,60,'password','password',2,'Password','fa-lock'),(483,61,'password','password',2,'Password','fa-lock'),(484,62,'password','password',2,'Password','fa-lock'),(485,63,'password','password',2,'Password','fa-lock'),(486,64,'password','password',2,'Password','fa-lock'),(487,65,'password','password',2,'Password','fa-lock'),(488,66,'password','password',2,'Password','fa-lock'),(489,67,'password','password',2,'Password','fa-lock'),(490,68,'password','password',2,'Password','fa-lock'),(491,69,'password','password',2,'Password','fa-lock'),(492,70,'password','password',2,'Password','fa-lock'),(493,71,'password','password',2,'Password','fa-lock'),(494,72,'password','password',2,'Password','fa-lock'),(495,73,'password','password',2,'Password','fa-lock'),(496,74,'password','password',2,'Password','fa-lock'),(497,75,'password','password',2,'Password','fa-lock'),(498,77,'password','password',2,'Password','fa-lock'),(500,86,'password','password',2,'Password','fa-lock'),(501,87,'password','password',2,'Password','fa-lock'),(502,89,'password','password',2,'Password','fa-lock'),(503,90,'password','password',2,'Password','fa-lock'),(504,91,'password','password',2,'Password','fa-lock'),(505,92,'password','password',2,'Password','fa-lock'),(506,93,'password','password',2,'Password','fa-lock'),(507,94,'password','password',2,'Password','fa-lock'),(508,97,'password','password',2,'Password','fa-lock'),(509,98,'password','password',2,'Password','fa-lock'),(510,100,'password','password',2,'Password','fa-lock'),(511,102,'password','password',2,'Password','fa-lock'),(512,103,'password','password',2,'Password','fa-lock'),(513,104,'password','password',2,'Password','fa-lock'),(514,106,'password','password',2,'Password','fa-lock'),(515,107,'password','password',2,'Password','fa-lock'),(516,110,'password','password',2,'Password','fa-lock'),(517,117,'password','password',2,'Password','fa-lock'),(518,118,'password','password',2,'Password','fa-lock'),(519,119,'password','password',2,'Password','fa-lock'),(520,120,'password','password',2,'Password','fa-lock'),(521,121,'password','password',2,'Password','fa-lock'),(522,122,'password','password',2,'Password','fa-lock'),(523,123,'password','password',2,'Password','fa-lock'),(524,124,'password','password',2,'Password','fa-lock'),(525,126,'password','password',2,'Password','fa-lock'),(526,128,'password','password',2,'Password','fa-lock'),(527,129,'password','password',2,'Password','fa-lock'),(528,130,'password','password',2,'Password','fa-lock'),(529,131,'password','password',2,'Password','fa-lock'),(530,132,'password','password',2,'Password','fa-lock'),(531,133,'password','password',2,'Password','fa-lock'),(533,135,'password','password',2,'Password','fa-lock'),(534,136,'password','password',2,'Password','fa-lock'),(535,137,'password','password',2,'Password','fa-lock'),(536,138,'password','password',2,'Password','fa-lock'),(537,139,'password','password',2,'Password','fa-lock'),(538,140,'password','password',2,'Password','fa-lock'),(539,142,'password','password',2,'Password','fa-lock'),(540,144,'password','password',2,'Password','fa-lock'),(541,145,'password','password',2,'Password','fa-lock'),(542,146,'password','password',2,'Password','fa-lock'),(543,149,'password','password',2,'Password','fa-lock'),(544,150,'password','password',2,'Password','fa-lock'),(545,153,'password','password',2,'Password','fa-lock'),(546,154,'password','password',2,'Password','fa-lock'),(547,155,'password','password',2,'Password','fa-lock'),(548,156,'password','password',2,'Password','fa-lock'),(549,157,'password','password',2,'Password','fa-lock'),(550,158,'password','password',2,'Password','fa-lock'),(551,159,'password','password',2,'Password','fa-lock'),(552,161,'password','password',2,'Password','fa-lock'),(553,162,'password','password',2,'Password','fa-lock'),(554,163,'password','password',2,'Password','fa-lock'),(555,164,'password','password',2,'Password','fa-lock'),(556,165,'password','password',2,'Password','fa-lock'),(557,166,'password','password',2,'Password','fa-lock'),(558,167,'password','password',2,'Password','fa-lock'),(559,168,'password','password',2,'Password','fa-lock'),(560,169,'password','password',2,'Password','fa-lock'),(561,170,'password','password',2,'Password','fa-lock'),(562,171,'password','password',2,'Password','fa-lock'),(563,172,'password','password',2,'Password','fa-lock'),(564,173,'password','password',2,'Password','fa-lock'),(565,174,'password','password',2,'Password','fa-lock'),(566,175,'password','password',2,'Password','fa-lock'),(567,176,'password','password',2,'Password','fa-lock'),(568,177,'password','password',2,'Password','fa-lock'),(569,178,'password','password',2,'Password','fa-lock'),(570,179,'password','password',2,'Password','fa-lock'),(571,180,'password','password',2,'Password','fa-lock'),(572,181,'password','password',2,'Password','fa-lock'),(573,183,'password','password',2,'Password','fa-lock'),(574,184,'password','password',2,'Password','fa-lock'),(575,185,'password','password',2,'Password','fa-lock'),(576,186,'password','password',2,'Password','fa-lock'),(577,187,'password','password',2,'Password','fa-lock'),(578,188,'password','password',2,'Password','fa-lock'),(579,189,'password','password',2,'Password','fa-lock'),(580,190,'password','password',2,'Password','fa-lock'),(581,192,'password','password',2,'Password','fa-lock'),(582,193,'password','password',2,'Password','fa-lock'),(583,194,'password','password',2,'Password','fa-lock'),(584,195,'password','password',2,'Password','fa-lock'),(585,196,'password','password',2,'Password','fa-lock'),(586,197,'password','password',2,'Password','fa-lock'),(587,198,'password','password',2,'Password','fa-lock'),(588,199,'password','password',2,'Password','fa-lock'),(589,200,'password','password',2,'Password','fa-lock'),(590,201,'password','password',2,'Password','fa-lock'),(591,203,'password','password',2,'Password','fa-lock'),(592,204,'password','password',2,'Password','fa-lock'),(593,205,'password','password',2,'Password','fa-lock'),(594,207,'password','password',2,'Password','fa-lock'),(595,209,'password','password',2,'Password','fa-lock'),(596,211,'password','password',2,'Password','fa-lock'),(597,212,'password','password',2,'Password','fa-lock'),(598,213,'password','password',2,'Password','fa-lock'),(599,214,'password','password',2,'Password','fa-lock'),(600,215,'password','password',2,'Password','fa-lock'),(601,216,'password','password',2,'Password','fa-lock'),(602,217,'password','password',2,'Password','fa-lock'),(603,218,'password','password',2,'Password','fa-lock'),(604,219,'password','password',2,'Password','fa-lock'),(605,220,'password','password',2,'Password','fa-lock'),(606,221,'password','password',2,'Password','fa-lock'),(607,222,'password','password',2,'Password','fa-lock'),(608,223,'password','password',2,'Password','fa-lock'),(609,224,'password','password',2,'Password','fa-lock'),(610,225,'password','password',2,'Password','fa-lock'),(611,226,'password','password',2,'Password','fa-lock'),(612,227,'password','password',2,'Password','fa-lock'),(613,228,'password','password',2,'Password','fa-lock'),(614,229,'password','password',2,'Password','fa-lock'),(615,230,'password','password',2,'Password','fa-lock'),(616,231,'password','password',2,'Password','fa-lock'),(617,232,'password','password',2,'Password','fa-lock'),(618,233,'password','password',2,'Password','fa-lock'),(619,234,'password','password',2,'Password','fa-lock'),(620,235,'password','password',2,'Password','fa-lock'),(621,236,'password','password',2,'Password','fa-lock'),(622,237,'password','password',2,'Password','fa-lock'),(623,238,'password','password',2,'Password','fa-lock'),(624,239,'password','password',2,'Password','fa-lock'),(625,240,'password','password',2,'Password','fa-lock'),(626,241,'password','password',2,'Password','fa-lock'),(627,243,'password','password',2,'Password','fa-lock'),(628,244,'password','password',2,'Password','fa-lock'),(629,245,'password','password',2,'Password','fa-lock'),(630,246,'password','password',2,'Password','fa-lock'),(631,247,'password','password',2,'Password','fa-lock'),(632,248,'password','password',2,'Password','fa-lock'),(633,249,'password','password',2,'Password','fa-lock'),(634,250,'password','password',2,'Password','fa-lock'),(635,251,'password','password',2,'Password','fa-lock'),(636,252,'password','password',2,'Password','fa-lock'),(637,253,'password','password',2,'Password','fa-lock'),(638,254,'password','password',2,'Password','fa-lock'),(639,255,'password','password',2,'Password','fa-lock'),(640,256,'password','password',2,'Password','fa-lock'),(641,258,'password','password',2,'Password','fa-lock'),(642,259,'password','password',2,'Password','fa-lock'),(643,260,'password','password',2,'Password','fa-lock'),(644,261,'password','password',2,'Password','fa-lock'),(645,262,'password','password',2,'Password','fa-lock'),(646,263,'password','password',2,'Password','fa-lock'),(647,264,'password','password',2,'Password','fa-lock'),(648,265,'password','password',2,'Password','fa-lock'),(649,266,'password','password',2,'Password','fa-lock'),(650,267,'password','password',2,'Password','fa-lock'),(651,268,'password','password',2,'Password','fa-lock'),(652,269,'password','password',2,'Password','fa-lock'),(653,270,'password','password',2,'Password','fa-lock'),(654,271,'password','password',2,'Password','fa-lock'),(655,272,'password','password',2,'Password','fa-lock'),(656,273,'password','password',2,'Password','fa-lock'),(657,274,'password','password',2,'Password','fa-lock'),(658,275,'password','password',2,'Password','fa-lock'),(659,276,'password','password',2,'Password','fa-lock'),(660,277,'password','password',2,'Password','fa-lock'),(661,278,'password','password',2,'Password','fa-lock'),(662,279,'password','password',2,'Password','fa-lock'),(663,280,'password','password',2,'Password','fa-lock'),(664,281,'password','password',2,'Password','fa-lock'),(665,282,'password','password',2,'Password','fa-lock'),(666,283,'password','password',2,'Password','fa-lock'),(667,285,'password','password',2,'Password','fa-lock'),(668,286,'password','password',2,'Password','fa-lock'),(669,287,'password','password',2,'Password','fa-lock'),(670,288,'password','password',2,'Password','fa-lock'),(671,289,'password','password',2,'Password','fa-lock'),(672,290,'password','password',2,'Password','fa-lock'),(673,291,'password','password',2,'Password','fa-lock'),(674,293,'password','password',2,'Password','fa-lock'),(675,295,'password','password',2,'Password','fa-lock'),(676,296,'password','password',2,'Password','fa-lock'),(720,298,'login','text',0,'Login','fa-user-o'),(721,298,'password','password',1,'Password','fa-lock'),(722,299,'login','text',0,'Login','fa-user-o'),(723,299,'password','password',1,'Password','fa-lock'),(724,300,'login','text',0,'Login','fa-user-o'),(725,300,'password','password',1,'Password','fa-lock'),(726,301,'login','text',0,'Login','fa-user-o'),(727,301,'password','password',1,'Password','fa-lock'),(728,302,'login','text',0,'Login','fa-user-o'),(729,302,'password','password',1,'Password','fa-lock'),(730,303,'login','text',0,'Login','fa-user-o'),(731,303,'password','password',1,'Password','fa-lock'),(732,304,'login','text',0,'Login','fa-user-o'),(733,304,'password','password',1,'Password','fa-lock'),(734,306,'login','text',0,'Login','fa-user-o'),(735,306,'password','password',1,'Password','fa-lock'),(736,306,'storeName','text',2,'Store name','fa-shopping-bag'),(737,307,'login','text',0,'Login','fa-user-o'),(738,307,'password','password',1,'Password','fa-lock'),(739,308,'login','text',0,'Login','fa-user-o'),(740,308,'password','password',1,'Password','fa-lock'),(741,309,'login','text',0,'Login','fa-user-o'),(742,309,'password','password',1,'Password','fa-lock'),(743,310,'login','text',0,'Login','fa-user-o'),(744,310,'password','password',1,'Password','fa-lock'),(745,311,'login','text',0,'Login','fa-user-o'),(746,311,'password','password',1,'Password','fa-lock'),(747,311,'team','text',2,'Team','fa-globe'),(748,312,'login','text',0,'Login','fa-user-o'),(749,312,'password','password',1,'Password','fa-lock'),(750,313,'login','text',0,'Login','fa-user-o'),(751,313,'password','password',1,'Password','fa-lock'),(752,314,'login','text',0,'Login','fa-user-o'),(753,314,'password','password',1,'Password','fa-lock'),(754,315,'login','text',0,'Login','fa-user-o'),(755,315,'password','password',1,'Password','fa-lock'),(756,316,'login','text',0,'Login','fa-user-o'),(757,316,'password','password',1,'Password','fa-lock'),(758,317,'login','text',0,'Login','fa-user-o'),(759,317,'password','password',1,'Password','fa-lock'),(760,318,'login','text',0,'Login','fa-user-o'),(761,318,'password','password',1,'Password','fa-lock'),(762,319,'login','text',0,'Login','fa-user-o'),(763,319,'password','password',1,'Password','fa-lock'),(764,320,'login','text',0,'Login','fa-user-o'),(765,320,'password','password',1,'Password','fa-lock'),(766,322,'login','text',0,'Login','fa-user-o'),(767,322,'password','password',1,'Password','fa-lock'),(768,323,'login','text',0,'Login','fa-user-o'),(769,323,'password','password',1,'Password','fa-lock'),(770,324,'login','text',0,'Login','fa-user-o'),(771,324,'password','password',1,'Password','fa-lock'),(772,325,'login','text',0,'Login','fa-user-o'),(773,325,'password','password',1,'Password','fa-lock'),(774,326,'login','text',0,'Login','fa-user-o'),(775,326,'password','password',1,'Password','fa-lock'),(776,327,'login','text',0,'Login','fa-user-o'),(777,327,'password','password',1,'Password','fa-lock'),(778,328,'login','text',0,'Login','fa-user-o'),(779,328,'password','password',1,'Password','fa-lock'),(780,329,'login','text',0,'Login','fa-user-o'),(781,329,'password','password',1,'Password','fa-lock'),(782,330,'login','text',0,'Login','fa-user-o'),(783,330,'password','password',1,'Password','fa-lock'),(784,331,'login','text',0,'Login','fa-user-o'),(785,331,'password','password',1,'Password','fa-lock'),(786,332,'login','text',0,'Login','fa-user-o'),(787,332,'password','password',1,'Password','fa-lock'),(788,333,'login','text',0,'Login','fa-user-o'),(789,333,'password','password',1,'Password','fa-lock'),(790,334,'login','text',0,'Login','fa-user-o'),(791,334,'password','password',1,'Password','fa-lock'),(792,335,'login','text',0,'Login','fa-user-o'),(793,335,'password','password',1,'Password','fa-lock'),(794,336,'login','text',0,'Login','fa-user-o'),(795,336,'password','password',1,'Password','fa-lock'),(796,337,'login','text',0,'Login','fa-user-o'),(797,337,'password','password',1,'Password','fa-lock'),(798,338,'login','text',0,'Login','fa-user-o'),(799,338,'password','password',1,'Password','fa-lock'),(800,339,'login','text',0,'Login','fa-user-o'),(801,339,'password','password',1,'Password','fa-lock'),(802,340,'login','text',0,'Login','fa-user-o'),(803,340,'password','password',1,'Password','fa-lock'),(804,341,'login','text',0,'Login','fa-user-o'),(805,341,'password','password',1,'Password','fa-lock'),(806,342,'login','text',0,'Login','fa-user-o'),(807,342,'password','password',1,'Password','fa-lock'),(808,343,'login','text',0,'Login','fa-user-o'),(809,343,'password','password',1,'Password','fa-lock'),(810,344,'login','text',0,'Login','fa-user-o'),(811,344,'password','password',1,'Password','fa-lock'),(812,345,'login','text',0,'Login','fa-user-o'),(813,345,'password','password',1,'Password','fa-lock'),(814,346,'login','text',0,'Login','fa-user-o'),(815,346,'password','password',1,'Password','fa-lock'),(816,347,'login','text',0,'Login','fa-user-o'),(817,347,'password','password',1,'Password','fa-lock'),(818,348,'login','text',0,'Login','fa-user-o'),(819,348,'password','password',1,'Password','fa-lock'),(820,349,'login','text',0,'Login','fa-user-o'),(821,349,'password','password',1,'Password','fa-lock'),(822,350,'login','text',0,'Login','fa-user-o'),(823,350,'password','password',1,'Password','fa-lock'),(824,351,'login','text',0,'Login','fa-user-o'),(825,351,'password','password',1,'Password','fa-lock'),(826,352,'login','text',0,'Login','fa-user-o'),(827,352,'password','password',1,'Password','fa-lock'),(828,353,'login','text',0,'Login','fa-user-o'),(829,353,'password','password',1,'Password','fa-lock'),(830,354,'login','text',0,'Login','fa-user-o'),(831,354,'password','password',1,'Password','fa-lock'),(832,355,'login','text',0,'Login','fa-user-o'),(833,355,'password','password',1,'Password','fa-lock'),(834,356,'login','text',0,'Login','fa-user-o'),(835,356,'password','password',1,'Password','fa-lock'),(836,357,'login','text',0,'Login','fa-user-o'),(837,357,'password','password',1,'Password','fa-lock'),(838,358,'login','text',0,'Login','fa-user-o'),(839,358,'password','password',1,'Password','fa-lock'),(840,359,'login','text',0,'Login','fa-user-o'),(841,359,'password','password',1,'Password','fa-lock'),(842,360,'login','text',0,'Login','fa-user-o'),(843,360,'password','password',1,'Password','fa-lock'),(844,361,'login','text',0,'Login','fa-user-o'),(845,361,'password','password',1,'Password','fa-lock'),(846,362,'login','text',0,'Login','fa-user-o'),(847,362,'password','password',1,'Password','fa-lock'),(848,363,'login','text',0,'Login','fa-user-o'),(849,363,'password','password',1,'Password','fa-lock'),(850,364,'login','text',0,'Login','fa-user-o'),(851,364,'password','password',1,'Password','fa-lock'),(852,365,'login','text',0,'Login','fa-user-o'),(853,365,'password','password',1,'Password','fa-lock'),(854,366,'login','text',0,'Login','fa-user-o'),(855,366,'password','password',1,'Password','fa-lock'),(856,367,'login','text',0,'Login','fa-user-o'),(857,367,'password','password',1,'Password','fa-lock'),(858,368,'login','text',0,'Login','fa-user-o'),(859,368,'password','password',1,'Password','fa-lock'),(860,369,'login','text',0,'Login','fa-user-o'),(861,369,'password','password',1,'Password','fa-lock'),(862,370,'login','text',0,'Login','fa-user-o'),(863,370,'password','password',1,'Password','fa-lock'),(864,371,'login','text',0,'Login','fa-user-o'),(865,371,'password','password',1,'Password','fa-lock'),(866,372,'login','text',0,'Login','fa-user-o'),(867,372,'password','password',1,'Password','fa-lock'),(868,373,'login','text',0,'Login','fa-user-o'),(869,373,'password','password',1,'Password','fa-lock'),(870,374,'login','text',0,'Login','fa-user-o'),(871,374,'password','password',1,'Password','fa-lock'),(872,375,'login','text',0,'Login','fa-user-o'),(873,375,'password','password',1,'Password','fa-lock'),(874,376,'login','text',0,'Login','fa-user-o'),(875,376,'password','password',1,'Password','fa-lock'),(876,377,'login','text',0,'Login','fa-user-o'),(877,377,'password','password',1,'Password','fa-lock'),(878,377,'subdomain','text',2,'Subdomain','fa-link'),(879,378,'login','text',0,'Login','fa-user-o'),(880,378,'password','password',1,'Password','fa-lock'),(881,379,'login','text',0,'Login','fa-user-o'),(882,379,'password','password',1,'Password','fa-lock'),(883,380,'login','text',0,'Login','fa-user-o'),(884,380,'password','password',1,'Password','fa-lock'),(885,381,'login','text',0,'Login','fa-user-o'),(886,381,'password','password',1,'Password','fa-lock'),(887,382,'login','text',0,'Login','fa-user-o'),(888,382,'password','password',1,'Password','fa-lock'),(889,382,'subdomain','text',2,'Subdomain','fa-link'),(890,383,'login','text',0,'Login','fa-user-o'),(891,383,'password','password',1,'Password','fa-lock'),(892,384,'login','text',0,'Login','fa-user-o'),(893,384,'password','password',1,'Password','fa-lock'),(894,385,'login','text',0,'Login','fa-user-o'),(895,385,'password','password',1,'Password','fa-lock'),(896,386,'login','text',0,'Login','fa-user-o'),(897,386,'password','password',1,'Password','fa-lock'),(898,387,'login','text',0,'Login','fa-user-o'),(899,387,'password','password',1,'Password','fa-lock'),(900,388,'login','text',0,'Login','fa-user-o'),(901,388,'password','password',1,'Password','fa-lock'),(902,389,'login','text',0,'Login','fa-user-o'),(903,389,'password','password',1,'Password','fa-lock'),(904,390,'login','text',0,'Login','fa-user-o'),(905,390,'password','password',1,'Password','fa-lock'),(906,391,'login','text',0,'Login','fa-user-o'),(907,391,'password','password',1,'Password','fa-lock'),(908,392,'login','text',0,'Login','fa-user-o'),(909,392,'password','password',1,'Password','fa-lock'),(910,393,'login','text',0,'Login','fa-user-o'),(911,393,'password','password',1,'Password','fa-lock'),(912,394,'login','text',0,'Login','fa-user-o'),(913,394,'password','password',1,'Password','fa-lock'),(914,395,'login','text',0,'Login','fa-user-o'),(915,395,'password','password',1,'Password','fa-lock'),(916,396,'login','text',0,'Login','fa-user-o'),(917,396,'password','password',1,'Password','fa-lock'),(918,397,'login','text',0,'Login','fa-user-o'),(919,397,'password','password',1,'Password','fa-lock'),(920,398,'login','text',0,'Login','fa-user-o'),(921,398,'password','password',1,'Password','fa-lock'),(922,399,'login','text',0,'Login','fa-user-o'),(923,399,'password','password',1,'Password','fa-lock'),(924,400,'login','text',0,'Login','fa-user-o'),(925,400,'password','password',1,'Password','fa-lock'),(926,401,'login','text',0,'Login','fa-user-o'),(927,401,'password','password',1,'Password','fa-lock'),(928,402,'login','text',0,'Login','fa-user-o'),(929,402,'password','password',1,'Password','fa-lock'),(930,403,'login','text',0,'Login','fa-user-o'),(931,403,'password','password',1,'Password','fa-lock'),(932,404,'login','text',0,'Login','fa-user-o'),(933,404,'password','password',1,'Password','fa-lock'),(934,405,'login','text',0,'Login','fa-user-o'),(935,405,'password','password',1,'Password','fa-lock'),(936,406,'login','text',0,'Login','fa-user-o'),(937,406,'password','password',1,'Password','fa-lock'),(938,407,'login','text',0,'Login','fa-user-o'),(939,407,'password','password',1,'Password','fa-lock'),(940,408,'login','text',0,'Login','fa-user-o'),(941,408,'password','password',1,'Password','fa-lock'),(942,409,'login','text',0,'Login','fa-user-o'),(943,409,'password','password',1,'Password','fa-lock'),(944,410,'login','text',0,'Login','fa-user-o'),(945,410,'password','password',1,'Password','fa-lock'),(946,411,'login','text',0,'Login','fa-user-o'),(947,411,'password','password',1,'Password','fa-lock'),(948,412,'login','text',0,'Login','fa-user-o'),(949,412,'password','password',1,'Password','fa-lock'),(950,413,'login','text',0,'Login','fa-user-o'),(951,413,'password','password',1,'Password','fa-lock'),(952,414,'login','text',0,'Login','fa-user-o'),(953,414,'password','password',1,'Password','fa-lock'),(954,415,'login','text',0,'Login','fa-user-o'),(955,415,'password','password',1,'Password','fa-lock'),(956,416,'login','text',0,'Login','fa-user-o'),(957,416,'password','password',1,'Password','fa-lock'),(958,417,'login','text',0,'Login','fa-user-o'),(959,417,'password','password',1,'Password','fa-lock'),(962,419,'login','text',0,'Login','fa-user-o'),(963,419,'password','password',1,'Password','fa-lock'),(964,420,'login','text',0,'Login','fa-user-o'),(965,420,'password','password',1,'Password','fa-lock'),(966,421,'login','text',0,'Login','fa-user-o'),(967,421,'password','password',1,'Password','fa-lock'),(968,422,'login','text',0,'Login','fa-user-o'),(969,422,'password','password',1,'Password','fa-lock'),(970,423,'login','text',0,'Login','fa-user-o'),(971,423,'password','password',1,'Password','fa-lock'),(972,424,'login','text',0,'Login','fa-user-o'),(973,424,'password','password',1,'Password','fa-lock'),(974,425,'login','text',0,'Login','fa-user-o'),(975,425,'password','password',1,'Password','fa-lock'),(976,426,'login','text',0,'Login','fa-user-o'),(977,426,'password','password',1,'Password','fa-lock'),(978,427,'login','text',0,'Login','fa-user-o'),(979,427,'password','password',1,'Password','fa-lock'),(980,428,'login','text',0,'Login','fa-user-o'),(981,428,'password','password',1,'Password','fa-lock'),(982,429,'login','text',0,'Login','fa-user-o'),(983,429,'password','password',1,'Password','fa-lock'),(984,430,'login','text',0,'Login','fa-user-o'),(985,430,'password','password',1,'Password','fa-lock'),(986,431,'login','text',0,'Login','fa-user-o'),(987,431,'password','password',1,'Password','fa-lock'),(988,432,'login','text',0,'Login','fa-user-o'),(989,432,'password','password',1,'Password','fa-lock'),(990,433,'login','text',0,'Login','fa-user-o'),(991,433,'password','password',1,'Password','fa-lock'),(992,434,'login','text',0,'Login','fa-user-o'),(993,434,'password','password',1,'Password','fa-lock'),(994,435,'login','text',0,'Login','fa-user-o'),(995,435,'password','password',1,'Password','fa-lock'),(996,436,'login','text',0,'Login','fa-user-o'),(997,436,'password','password',1,'Password','fa-lock'),(998,437,'login','text',0,'Login','fa-user-o'),(999,437,'password','password',1,'Password','fa-lock'),(1000,438,'login','text',0,'Login','fa-user-o'),(1001,438,'password','password',1,'Password','fa-lock'),(1002,439,'login','text',0,'Login','fa-user-o'),(1003,439,'password','password',1,'Password','fa-lock'),(1004,440,'login','text',0,'Login','fa-user-o'),(1005,440,'password','password',1,'Password','fa-lock'),(1006,441,'login','text',0,'Login','fa-user-o'),(1007,441,'password','password',1,'Password','fa-lock'),(1008,442,'login','text',0,'Login','fa-user-o'),(1009,442,'password','password',1,'Password','fa-lock'),(1010,443,'login','text',0,'Login','fa-user-o'),(1011,443,'password','password',1,'Password','fa-lock'),(1012,444,'login','text',0,'Login','fa-user-o'),(1013,444,'password','password',1,'Password','fa-lock'),(1014,445,'login','text',0,'Login','fa-user-o'),(1015,445,'password','password',1,'Password','fa-lock'),(1016,446,'login','text',0,'Login','fa-user-o'),(1017,446,'password','password',1,'Password','fa-lock'),(1018,447,'login','text',0,'Login','fa-user-o'),(1019,447,'password','password',1,'Password','fa-lock'),(1020,448,'login','text',0,'Login','fa-user-o'),(1021,448,'password','password',1,'Password','fa-lock'),(1022,449,'login','text',0,'Login','fa-user-o'),(1023,449,'password','password',1,'Password','fa-lock'),(1024,450,'login','text',0,'Login','fa-user-o'),(1025,450,'password','password',1,'Password','fa-lock'),(1026,451,'login','text',0,'Login','fa-user-o'),(1027,451,'password','password',1,'Password','fa-lock'),(1028,452,'login','text',0,'Login','fa-user-o'),(1029,452,'password','password',1,'Password','fa-lock'),(1030,453,'login','text',0,'Login','fa-user-o'),(1031,453,'password','password',1,'Password','fa-lock'),(1032,454,'login','text',0,'Login','fa-user-o'),(1033,454,'password','password',1,'Password','fa-lock'),(1034,455,'login','text',0,'Login','fa-user-o'),(1035,455,'password','password',1,'Password','fa-lock'),(1036,456,'login','text',0,'Login','fa-user-o'),(1037,456,'password','password',1,'Password','fa-lock'),(1038,457,'login','text',0,'Login','fa-user-o'),(1039,457,'password','password',1,'Password','fa-lock'),(1040,458,'login','text',0,'Login','fa-user-o'),(1041,458,'password','password',1,'Password','fa-lock'),(1042,459,'login','text',0,'Login','fa-user-o'),(1043,459,'password','password',1,'Password','fa-lock'),(1044,460,'login','text',0,'Login','fa-user-o'),(1045,460,'password','password',1,'Password','fa-lock'),(1046,461,'login','text',0,'Login','fa-user-o'),(1047,461,'password','password',1,'Password','fa-lock'),(1048,462,'login','text',0,'Login','fa-user-o'),(1049,462,'password','password',1,'Password','fa-lock'),(1050,463,'login','text',0,'Login','fa-user-o'),(1051,463,'password','password',1,'Password','fa-lock'),(1052,464,'login','text',0,'Login','fa-user-o'),(1053,464,'password','password',1,'Password','fa-lock'),(1054,465,'login','text',0,'Login','fa-user-o'),(1055,465,'password','password',1,'Password','fa-lock'),(1056,466,'login','text',0,'Login','fa-user-o'),(1057,466,'password','password',1,'Password','fa-lock'),(1058,467,'login','text',0,'Login','fa-user-o'),(1059,467,'password','password',1,'Password','fa-lock'),(1072,474,'login','text',0,'Login','fa-user-o'),(1073,474,'password','password',1,'Password','fa-lock'),(1074,475,'login','text',0,'Login','fa-user-o'),(1075,475,'password','password',1,'Password','fa-lock'),(1078,477,'login','text',0,'Login','fa-user-o'),(1079,477,'password','password',1,'Password','fa-lock'),(1082,479,'password','password',1,'Password','fa-lock'),(1083,479,'login','text',0,'Login','fa-user-o'),(1084,480,'login','text',0,'Login','fa-user-o'),(1085,480,'password','password',1,'Password','fa-lock'),(1088,482,'password','password',1,'Password','fa-lock'),(1089,482,'login','text',0,'Login','fa-user-o'),(1090,483,'login','text',0,'Login','fa-user-o'),(1091,483,'password','password',1,'Password','fa-lock'),(1092,484,'login','text',0,'Login','fa-user-o'),(1093,484,'password','password',1,'Password','fa-lock'),(1096,486,'password','password',1,'Password','fa-lock'),(1097,486,'login','text',0,'Login','fa-user-o'),(1098,487,'password','password',1,'Password','fa-lock'),(1099,487,'login','text',0,'Login','fa-user-o'),(1100,488,'login','text',0,'Login','fa-user-o'),(1101,488,'password','password',1,'Password','fa-lock'),(1102,489,'login','text',0,'Login','fa-user-o'),(1103,489,'password','password',1,'Password','fa-lock'),(1108,492,'password','password',1,'Password','fa-lock'),(1109,492,'login','text',0,'Login','fa-user-o'),(1110,493,'login','text',0,'Login','fa-user-o'),(1111,493,'password','password',1,'Password','fa-lock'),(1112,494,'password','password',1,'Password','fa-lock'),(1113,494,'login','text',0,'Login','fa-user-o'),(1114,495,'login','text',0,'Login','fa-user-o'),(1115,495,'password','password',1,'Password','fa-lock'),(1116,496,'login','text',0,'Login','fa-user-o'),(1117,496,'password','password',1,'Password','fa-lock'),(1118,497,'login','text',0,'Login','fa-user-o'),(1119,497,'password','password',1,'Password','fa-lock'),(1120,498,'password','password',1,'Password','fa-lock'),(1121,498,'login','text',0,'Login','fa-user-o'),(1124,500,'password','password',1,'Password','fa-lock'),(1125,500,'login','text',0,'Login','fa-user-o'),(1126,501,'password','password',1,'Password','fa-lock'),(1127,501,'login','text',0,'Login','fa-user-o'),(1128,502,'login','text',0,'Login','fa-user-o'),(1129,502,'password','password',1,'Password','fa-lock'),(1130,503,'login','text',0,'Login','fa-user-o'),(1131,503,'password','password',1,'Password','fa-lock'),(1134,505,'password','password',1,'Password','fa-lock'),(1135,505,'login','text',0,'Login','fa-user-o'),(1136,506,'password','password',1,'Password','fa-lock'),(1137,506,'login','text',0,'Login','fa-user-o'),(1138,507,'login','text',0,'Login','fa-user-o'),(1139,507,'password','password',1,'Password','fa-lock'),(1142,509,'login','text',0,'Login','fa-user-o'),(1143,509,'password','password',1,'Password','fa-lock'),(1144,510,'login','text',0,'Login','fa-user-o'),(1145,510,'password','password',1,'Password','fa-lock'),(1146,511,'password','password',1,'Password','fa-lock'),(1147,511,'login','text',0,'Login','fa-user-o'),(1150,513,'login','text',0,'Login','fa-user-o'),(1151,513,'password','password',1,'Password','fa-lock'),(1152,514,'login','text',0,'Login','fa-user-o'),(1153,514,'password','password',1,'Password','fa-lock'),(1154,515,'password','password',1,'Password','fa-lock'),(1155,515,'login','text',0,'Login','fa-user-o'),(1156,516,'login','text',0,'Login','fa-user-o'),(1157,516,'password','password',1,'Password','fa-lock'),(1158,517,'login','text',0,'Login','fa-user-o'),(1159,517,'password','password',1,'Password','fa-lock'),(1162,519,'password','password',1,'Password','fa-lock'),(1163,519,'login','text',0,'Login','fa-user-o'),(1166,521,'password','password',1,'Password','fa-lock'),(1167,521,'login','text',0,'Login','fa-user-o'),(1168,522,'login','text',0,'Login','fa-user-o'),(1169,522,'password','password',1,'Password','fa-lock'),(1170,523,'login','text',0,'Login','fa-user-o'),(1171,523,'password','password',1,'Password','fa-lock'),(1172,524,'login','text',0,'Login','fa-user-o'),(1173,524,'password','password',1,'Password','fa-lock'),(1176,526,'password','password',1,'Password','fa-lock'),(1177,526,'login','text',0,'Login','fa-user-o'),(1178,527,'password','password',1,'Password','fa-lock'),(1179,527,'login','text',0,'Login','fa-user-o'),(1180,528,'login','text',0,'Login','fa-user-o'),(1181,528,'password','password',1,'Password','fa-lock'),(1182,529,'password','password',1,'Password','fa-lock'),(1183,529,'login','text',0,'Login','fa-user-o'),(1184,530,'password','password',1,'Password','fa-lock'),(1185,530,'login','text',0,'Login','fa-user-o'),(1186,531,'password','password',1,'Password','fa-lock'),(1187,531,'login','text',0,'Login','fa-user-o'),(1188,532,'login','text',0,'Login','fa-user-o'),(1189,532,'password','password',1,'Password','fa-lock'),(1190,533,'login','text',0,'Login','fa-user-o'),(1191,533,'password','password',1,'Password','fa-lock'),(1192,534,'password','password',1,'Password','fa-lock'),(1193,534,'login','text',0,'Login','fa-user-o'),(1194,535,'login','text',0,'Login','fa-user-o'),(1195,535,'password','password',1,'Password','fa-lock'),(1196,536,'password','password',1,'Password','fa-lock'),(1197,536,'login','text',0,'Login','fa-user-o'),(1198,537,'login','text',0,'Login','fa-user-o'),(1199,537,'password','password',1,'Password','fa-lock'),(1200,538,'login','text',0,'Login','fa-user-o'),(1201,538,'password','password',1,'Password','fa-lock'),(1204,540,'login','text',0,'Login','fa-user-o'),(1205,540,'password','password',1,'Password','fa-lock'),(1206,541,'login','text',0,'Login','fa-user-o'),(1207,541,'password','password',1,'Password','fa-lock'),(1208,542,'login','text',0,'Login','fa-user-o'),(1209,542,'password','password',1,'Password','fa-lock'),(1210,543,'password','password',1,'Password','fa-lock'),(1211,543,'login','text',0,'Login','fa-user-o'),(1212,544,'login','text',1,'Login','fa-user-o'),(1213,544,'password','password',2,'Password','fa-lock'),(1214,545,'password','password',1,'Password','fa-lock'),(1215,545,'login','text',0,'Login','fa-user-o'),(1216,544,'subdomain','text',0,'subdomain','fa-globe'),(1217,546,'password','password',1,'Password','fa-lock'),(1218,546,'login','text',0,'Login','fa-user-o'),(1219,547,'password','password',1,'Password','fa-lock'),(1220,547,'login','text',0,'Login','fa-user-o'),(1221,548,'login','text',0,'Login','fa-user-o'),(1222,548,'password','password',1,'Password','fa-lock'),(1223,549,'password','password',1,'Password','fa-lock'),(1224,549,'login','text',0,'Login','fa-user-o'),(1225,550,'password','password',1,'Password','fa-lock'),(1226,550,'login','text',0,'Login','fa-user-o'),(1227,551,'login','text',0,'Login','fa-user-o'),(1228,551,'password','password',1,'Password','fa-lock'),(1229,552,'password','password',1,'Password','fa-lock'),(1230,552,'login','text',0,'Login','fa-user-o'),(1231,553,'password','password',1,'Password','fa-lock'),(1232,553,'login','text',0,'Login','fa-user-o'),(1233,554,'password','password',1,'Password','fa-lock'),(1234,554,'login','text',0,'Login','fa-user-o'),(1235,555,'login','text',0,'Login','fa-user-o'),(1236,555,'password','password',1,'Password','fa-lock'),(1237,556,'login','text',0,'Login','fa-user-o'),(1238,556,'password','password',1,'Password','fa-lock'),(1239,557,'password','password',1,'Password','fa-lock'),(1240,557,'login','text',0,'Login','fa-user-o');
/*!40000 ALTER TABLE `websitesInformations` ENABLE KEYS */;
UNLOCK TABLES;
LOCK TABLE `sso`;
/*!40000 ALTER TABLE `sso` DISABLE KEYS */;
INSERT INTO sso VALUES (1, 'Google', 'google.png');
/*!40000 ALTER TABLE `sso` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-12-05 11:51:55
CREATE TABLE metricTeam (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  team_id INT(10) UNSIGNED NOT NULL,
  year INT(10) UNSIGNED NOT NULL,
  week_of_year TINYINT UNSIGNED NOT NULL,
  people_invited MEDIUMINT UNSIGNED NOT NULL,
  people_invited_emails TEXT,
  people_joined MEDIUMINT UNSIGNED NOT NULL,
  people_joined_emails TEXT,
  people_with_cards MEDIUMINT UNSIGNED NOT NULL,
  people_with_cards_emails TEXT,
  people_click_on_app_once MEDIUMINT UNSIGNED NOT NULL,
  people_click_on_app_once_emails TEXT,
  people_click_on_app_three_times MEDIUMINT UNSIGNED NOT NULL,
  people_click_on_app_three_times_emails TEXT,
  people_click_on_app_five_times MEDIUMINT UNSIGNED NOT NULL,
  people_click_on_app_five_times_emails TEXT,
  room_number TINYINT NOT NULL,
  room_names TEXT,
  cards MEDIUMINT UNSIGNED NOT NULL,
  cards_with_receiver MEDIUMINT UNSIGNED NOT NULL,
  cards_with_receiver_and_password_policy MEDIUMINT UNSIGNED NOT NULL,
  single_cards MEDIUMINT UNSIGNED NOT NULL,
  enterprise_cards MEDIUMINT UNSIGNED NOT NULL,
  link_cards MEDIUMINT UNSIGNED NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE metricTeam ADD COLUMN people_with_personnal_apps MEDIUMINT UNSIGNED NOT NULL NULL;
ALTER TABLE metricTeam ADD COLUMN people_with_personnal_apps_emails TEXT;
UPDATE metricTeam SET people_with_personnal_apps = 0;