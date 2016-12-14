-- MySQL dump 10.13  Distrib 5.7.14, for osx10.11 (x86_64)
--
-- Host: localhost    Database: ease
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
  `password` varchar(255) NOT NULL,
  `shared` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` VALUES (6,'ozHYltuq5p70CwA+HyqRNg==',0),(7,'jXYDpBhvhemalAjEaOwTHA==',0),(8,'j0y8ea76Kg5ik56MWAnK3Q==',0);
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accountsInformations`
--

LOCK TABLES `accountsInformations` WRITE;
/*!40000 ALTER TABLE `accountsInformations` DISABLE KEYS */;
INSERT INTO `accountsInformations` VALUES (11,6,'password','aazazaza'),(12,6,'login','azazaz'),(13,7,'password','aedfazdad'),(14,7,'login','adazdazd'),(15,8,'password','qsf'),(16,8,'login','dzqefrgdt');
/*!40000 ALTER TABLE `accountsInformations` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admins`
--

LOCK TABLES `admins` WRITE;
/*!40000 ALTER TABLE `admins` DISABLE KEYS */;
/*!40000 ALTER TABLE `admins` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `appPermissions`
--

DROP TABLE IF EXISTS `appPermissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `appPermissions` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `group_id` int(10) unsigned NOT NULL,
  `permission` bit(20) NOT NULL DEFAULT b'11111111111111111111',
  PRIMARY KEY (`id`),
  KEY `group_id` (`group_id`),
  CONSTRAINT `apppermissions_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appPermissions`
--

LOCK TABLES `appPermissions` WRITE;
/*!40000 ALTER TABLE `appPermissions` DISABLE KEYS */;
/*!40000 ALTER TABLE `appPermissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `apps`
--

DROP TABLE IF EXISTS `apps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `apps` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `profile_id` int(10) unsigned NOT NULL,
  `position` tinyint(3) unsigned NOT NULL,
  `insert_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `type` varchar(255) NOT NULL,
  `app_info_id` int(10) unsigned NOT NULL,
  `group_app_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `profile_id` (`profile_id`),
  KEY `app_info_id` (`app_info_id`),
  KEY `group_app_id` (`group_app_id`),
  CONSTRAINT `apps_ibfk_1` FOREIGN KEY (`profile_id`) REFERENCES `profiles` (`id`),
  CONSTRAINT `apps_ibfk_2` FOREIGN KEY (`app_info_id`) REFERENCES `appsInformations` (`id`),
  CONSTRAINT `apps_ibfk_3` FOREIGN KEY (`group_app_id`) REFERENCES `groupApps` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `apps`
--

LOCK TABLES `apps` WRITE;
/*!40000 ALTER TABLE `apps` DISABLE KEYS */;
INSERT INTO `apps` VALUES (8,2,0,'2016-12-14 11:43:59','websiteApp',12,NULL),(9,10,0,'2016-12-14 12:10:45','websiteApp',13,NULL),(10,7,0,'2016-12-14 16:52:20','websiteApp',14,NULL);
/*!40000 ALTER TABLE `apps` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `appsInformations`
--

DROP TABLE IF EXISTS `appsInformations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `appsInformations` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(14) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appsInformations`
--

LOCK TABLES `appsInformations` WRITE;
/*!40000 ALTER TABLE `appsInformations` DISABLE KEYS */;
INSERT INTO `appsInformations` VALUES (12,'Facebook'),(13,'Facebook'),(14,'Facebook');
/*!40000 ALTER TABLE `appsInformations` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `askingIps`
--

LOCK TABLES `askingIps` WRITE;
/*!40000 ALTER TABLE `askingIps` DISABLE KEYS */;
/*!40000 ALTER TABLE `askingIps` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `classicApps`
--

DROP TABLE IF EXISTS `classicApps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `classicApps` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `website_app_id` int(10) unsigned NOT NULL,
  `account_id` int(10) unsigned NOT NULL,
  `group_classic_app_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `website_app_id` (`website_app_id`),
  KEY `account_id` (`account_id`),
  KEY `group_classic_app_id` (`group_classic_app_id`),
  CONSTRAINT `classicapps_ibfk_1` FOREIGN KEY (`website_app_id`) REFERENCES `websiteApps` (`id`),
  CONSTRAINT `classicapps_ibfk_2` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`),
  CONSTRAINT `classicapps_ibfk_3` FOREIGN KEY (`group_classic_app_id`) REFERENCES `groupClassicApps` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `classicApps`
--

LOCK TABLES `classicApps` WRITE;
/*!40000 ALTER TABLE `classicApps` DISABLE KEYS */;
INSERT INTO `classicApps` VALUES (6,6,6,NULL),(7,7,7,NULL),(8,8,8,NULL);
/*!40000 ALTER TABLE `classicApps` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `classicUpdates`
--

DROP TABLE IF EXISTS `classicUpdates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `classicUpdates` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `update_id` int(10) unsigned NOT NULL,
  `cryptedPassword` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `update_id` (`update_id`),
  CONSTRAINT `classicupdates_ibfk_1` FOREIGN KEY (`update_id`) REFERENCES `updates` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `classicUpdates`
--

LOCK TABLES `classicUpdates` WRITE;
/*!40000 ALTER TABLE `classicUpdates` DISABLE KEYS */;
/*!40000 ALTER TABLE `classicUpdates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customAccounts`
--

DROP TABLE IF EXISTS `customAccounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customAccounts` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `custom_classic_app_id` int(10) unsigned NOT NULL,
  `information_name` varchar(255) DEFAULT NULL,
  `information_value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `custom_classic_app_id` (`custom_classic_app_id`),
  CONSTRAINT `customaccounts_ibfk_1` FOREIGN KEY (`custom_classic_app_id`) REFERENCES `customClassicApps` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customAccounts`
--

LOCK TABLES `customAccounts` WRITE;
/*!40000 ALTER TABLE `customAccounts` DISABLE KEYS */;
/*!40000 ALTER TABLE `customAccounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customApps`
--

DROP TABLE IF EXISTS `customApps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customApps` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(14) NOT NULL,
  `infrastructure_id` int(10) unsigned NOT NULL,
  `permisson_id` int(10) unsigned NOT NULL,
  `type` varchar(25) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `infrastructure_id` (`infrastructure_id`),
  KEY `permisson_id` (`permisson_id`),
  CONSTRAINT `customapps_ibfk_1` FOREIGN KEY (`infrastructure_id`) REFERENCES `infrastructures` (`id`),
  CONSTRAINT `customapps_ibfk_2` FOREIGN KEY (`permisson_id`) REFERENCES `appPermissions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customApps`
--

LOCK TABLES `customApps` WRITE;
/*!40000 ALTER TABLE `customApps` DISABLE KEYS */;
/*!40000 ALTER TABLE `customApps` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customAppsAndGroupsMap`
--

DROP TABLE IF EXISTS `customAppsAndGroupsMap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customAppsAndGroupsMap` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `custom_app_id` int(10) unsigned NOT NULL,
  `group_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `custom_app_id` (`custom_app_id`),
  KEY `group_id` (`group_id`),
  CONSTRAINT `customappsandgroupsmap_ibfk_1` FOREIGN KEY (`custom_app_id`) REFERENCES `customApps` (`id`),
  CONSTRAINT `customappsandgroupsmap_ibfk_2` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customAppsAndGroupsMap`
--

LOCK TABLES `customAppsAndGroupsMap` WRITE;
/*!40000 ALTER TABLE `customAppsAndGroupsMap` DISABLE KEYS */;
/*!40000 ALTER TABLE `customAppsAndGroupsMap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customClassicApps`
--

DROP TABLE IF EXISTS `customClassicApps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customClassicApps` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `custom_website_app_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `custom_website_app_id` (`custom_website_app_id`),
  CONSTRAINT `customclassicapps_ibfk_1` FOREIGN KEY (`custom_website_app_id`) REFERENCES `customWebsiteApps` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customClassicApps`
--

LOCK TABLES `customClassicApps` WRITE;
/*!40000 ALTER TABLE `customClassicApps` DISABLE KEYS */;
/*!40000 ALTER TABLE `customClassicApps` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customLinkApps`
--

DROP TABLE IF EXISTS `customLinkApps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customLinkApps` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `custom_app_id` int(10) unsigned NOT NULL,
  `link` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `custom_app_id` (`custom_app_id`),
  CONSTRAINT `customlinkapps_ibfk_1` FOREIGN KEY (`custom_app_id`) REFERENCES `customApps` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customLinkApps`
--

LOCK TABLES `customLinkApps` WRITE;
/*!40000 ALTER TABLE `customLinkApps` DISABLE KEYS */;
/*!40000 ALTER TABLE `customLinkApps` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customLogWithApps`
--

DROP TABLE IF EXISTS `customLogWithApps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customLogWithApps` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `custom_website_app_id` int(10) unsigned NOT NULL,
  `custom_logWith_website_app_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `custom_website_app_id` (`custom_website_app_id`),
  KEY `custom_logWith_website_app_id` (`custom_logWith_website_app_id`),
  CONSTRAINT `customlogwithapps_ibfk_1` FOREIGN KEY (`custom_website_app_id`) REFERENCES `customWebsiteApps` (`id`),
  CONSTRAINT `customlogwithapps_ibfk_2` FOREIGN KEY (`custom_logWith_website_app_id`) REFERENCES `customWebsiteApps` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customLogWithApps`
--

LOCK TABLES `customLogWithApps` WRITE;
/*!40000 ALTER TABLE `customLogWithApps` DISABLE KEYS */;
/*!40000 ALTER TABLE `customLogWithApps` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customProfiles`
--

DROP TABLE IF EXISTS `customProfiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customProfiles` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `color` char(7) NOT NULL,
  `group_id` int(10) unsigned NOT NULL,
  `permisson_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `group_id` (`group_id`),
  KEY `permisson_id` (`permisson_id`),
  CONSTRAINT `customprofiles_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`),
  CONSTRAINT `customprofiles_ibfk_2` FOREIGN KEY (`permisson_id`) REFERENCES `profilePermissions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customProfiles`
--

LOCK TABLES `customProfiles` WRITE;
/*!40000 ALTER TABLE `customProfiles` DISABLE KEYS */;
/*!40000 ALTER TABLE `customProfiles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customWebsiteApps`
--

DROP TABLE IF EXISTS `customWebsiteApps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customWebsiteApps` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `custom_app_id` int(10) unsigned NOT NULL,
  `website_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `custom_app_id` (`custom_app_id`),
  KEY `website_id` (`website_id`),
  CONSTRAINT `customwebsiteapps_ibfk_1` FOREIGN KEY (`custom_app_id`) REFERENCES `customApps` (`id`),
  CONSTRAINT `customwebsiteapps_ibfk_2` FOREIGN KEY (`website_id`) REFERENCES `websites` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customWebsiteApps`
--

LOCK TABLES `customWebsiteApps` WRITE;
/*!40000 ALTER TABLE `customWebsiteApps` DISABLE KEYS */;
/*!40000 ALTER TABLE `customWebsiteApps` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `date_dimension`
--

LOCK TABLES `date_dimension` WRITE;
/*!40000 ALTER TABLE `date_dimension` DISABLE KEYS */;
/*!40000 ALTER TABLE `date_dimension` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groupApps`
--

DROP TABLE IF EXISTS `groupApps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groupApps` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `group_profile_id` int(10) unsigned NOT NULL,
  `group_id` int(10) unsigned NOT NULL,
  `permisson_id` int(10) unsigned NOT NULL,
  `position` int(10) unsigned NOT NULL,
  `type` varchar(255) NOT NULL,
  `app_info_id` int(10) unsigned NOT NULL,
  `common` tinyint(1) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `group_profile_id` (`group_profile_id`),
  KEY `group_id` (`group_id`),
  KEY `permisson_id` (`permisson_id`),
  KEY `app_info_id` (`app_info_id`),
  CONSTRAINT `groupapps_ibfk_1` FOREIGN KEY (`group_profile_id`) REFERENCES `groupProfiles` (`id`),
  CONSTRAINT `groupapps_ibfk_2` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`),
  CONSTRAINT `groupapps_ibfk_3` FOREIGN KEY (`permisson_id`) REFERENCES `appPermissions` (`id`),
  CONSTRAINT `groupapps_ibfk_4` FOREIGN KEY (`app_info_id`) REFERENCES `appsInformations` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groupApps`
--

LOCK TABLES `groupApps` WRITE;
/*!40000 ALTER TABLE `groupApps` DISABLE KEYS */;
/*!40000 ALTER TABLE `groupApps` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groupClassicApps`
--

DROP TABLE IF EXISTS `groupClassicApps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groupClassicApps` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `group_website_app_id` int(10) unsigned NOT NULL,
  `account_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `group_website_app_id` (`group_website_app_id`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `groupclassicapps_ibfk_1` FOREIGN KEY (`group_website_app_id`) REFERENCES `groupWebsiteApps` (`id`),
  CONSTRAINT `groupclassicapps_ibfk_2` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groupClassicApps`
--

LOCK TABLES `groupClassicApps` WRITE;
/*!40000 ALTER TABLE `groupClassicApps` DISABLE KEYS */;
/*!40000 ALTER TABLE `groupClassicApps` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groupLinkApps`
--

DROP TABLE IF EXISTS `groupLinkApps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groupLinkApps` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `link_app_info_id` int(10) unsigned NOT NULL,
  `group_app_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `link_app_info_id` (`link_app_info_id`),
  KEY `group_app_id` (`group_app_id`),
  CONSTRAINT `grouplinkapps_ibfk_1` FOREIGN KEY (`link_app_info_id`) REFERENCES `linkAppInformations` (`id`),
  CONSTRAINT `grouplinkapps_ibfk_2` FOREIGN KEY (`group_app_id`) REFERENCES `groupApps` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groupLinkApps`
--

LOCK TABLES `groupLinkApps` WRITE;
/*!40000 ALTER TABLE `groupLinkApps` DISABLE KEYS */;
/*!40000 ALTER TABLE `groupLinkApps` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groupLogWithApps`
--

DROP TABLE IF EXISTS `groupLogWithApps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groupLogWithApps` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `group_website_app_id` int(10) unsigned NOT NULL,
  `logWith_group_website_app_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `group_website_app_id` (`group_website_app_id`),
  KEY `logWith_group_website_app_id` (`logWith_group_website_app_id`),
  CONSTRAINT `grouplogwithapps_ibfk_1` FOREIGN KEY (`group_website_app_id`) REFERENCES `groupWebsiteApps` (`id`),
  CONSTRAINT `grouplogwithapps_ibfk_2` FOREIGN KEY (`logWith_group_website_app_id`) REFERENCES `groupWebsiteApps` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groupLogWithApps`
--

LOCK TABLES `groupLogWithApps` WRITE;
/*!40000 ALTER TABLE `groupLogWithApps` DISABLE KEYS */;
/*!40000 ALTER TABLE `groupLogWithApps` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groupProfiles`
--

DROP TABLE IF EXISTS `groupProfiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groupProfiles` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `group_id` int(10) unsigned NOT NULL,
  `permission_id` int(10) unsigned NOT NULL,
  `profile_info_id` int(10) unsigned NOT NULL,
  `common` tinyint(1) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `group_id` (`group_id`),
  KEY `permission_id` (`permission_id`),
  KEY `profile_info_id` (`profile_info_id`),
  CONSTRAINT `groupprofiles_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`),
  CONSTRAINT `groupprofiles_ibfk_2` FOREIGN KEY (`permission_id`) REFERENCES `profilePermissions` (`id`),
  CONSTRAINT `groupprofiles_ibfk_3` FOREIGN KEY (`profile_info_id`) REFERENCES `profileInfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groupProfiles`
--

LOCK TABLES `groupProfiles` WRITE;
/*!40000 ALTER TABLE `groupProfiles` DISABLE KEYS */;
/*!40000 ALTER TABLE `groupProfiles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groupWebsiteApps`
--

DROP TABLE IF EXISTS `groupWebsiteApps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groupWebsiteApps` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `group_app_id` int(10) unsigned NOT NULL,
  `website_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `group_app_id` (`group_app_id`),
  KEY `website_id` (`website_id`),
  CONSTRAINT `groupwebsiteapps_ibfk_1` FOREIGN KEY (`group_app_id`) REFERENCES `groupApps` (`id`),
  CONSTRAINT `groupwebsiteapps_ibfk_2` FOREIGN KEY (`website_id`) REFERENCES `websites` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groupWebsiteApps`
--

LOCK TABLES `groupWebsiteApps` WRITE;
/*!40000 ALTER TABLE `groupWebsiteApps` DISABLE KEYS */;
/*!40000 ALTER TABLE `groupWebsiteApps` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groups`
--

DROP TABLE IF EXISTS `groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groups` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(25) NOT NULL,
  `parent` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `parent` (`parent`),
  CONSTRAINT `groups_ibfk_1` FOREIGN KEY (`parent`) REFERENCES `groups` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groups`
--

LOCK TABLES `groups` WRITE;
/*!40000 ALTER TABLE `groups` DISABLE KEYS */;
INSERT INTO `groups` VALUES (1,'Ease',NULL),(2,'Test',NULL);
/*!40000 ALTER TABLE `groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groupsAndUsersMap`
--

DROP TABLE IF EXISTS `groupsAndUsersMap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groupsAndUsersMap` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `group_id` int(10) unsigned NOT NULL,
  `user_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `group_id` (`group_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `groupsandusersmap_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`),
  CONSTRAINT `groupsandusersmap_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groupsAndUsersMap`
--

LOCK TABLES `groupsAndUsersMap` WRITE;
/*!40000 ALTER TABLE `groupsAndUsersMap` DISABLE KEYS */;
/*!40000 ALTER TABLE `groupsAndUsersMap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `infrastructures`
--

DROP TABLE IF EXISTS `infrastructures`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `infrastructures` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(25) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `infrastructures`
--

LOCK TABLES `infrastructures` WRITE;
/*!40000 ALTER TABLE `infrastructures` DISABLE KEYS */;
/*!40000 ALTER TABLE `infrastructures` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `infrastructuresAdminsMap`
--

DROP TABLE IF EXISTS `infrastructuresAdminsMap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `infrastructuresAdminsMap` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `infrastructure_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `infrastructure_id` (`infrastructure_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `infrastructuresadminsmap_ibfk_1` FOREIGN KEY (`infrastructure_id`) REFERENCES `infrastructures` (`id`),
  CONSTRAINT `infrastructuresadminsmap_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `infrastructuresAdminsMap`
--

LOCK TABLES `infrastructuresAdminsMap` WRITE;
/*!40000 ALTER TABLE `infrastructuresAdminsMap` DISABLE KEYS */;
/*!40000 ALTER TABLE `infrastructuresAdminsMap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invitations`
--

DROP TABLE IF EXISTS `invitations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invitations` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `linkCode` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invitations`
--

LOCK TABLES `invitations` WRITE;
/*!40000 ALTER TABLE `invitations` DISABLE KEYS */;
INSERT INTO `invitations` VALUES (1,'titomheniart@gmail.com',NULL);
/*!40000 ALTER TABLE `invitations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invitationsAndGroupsMap`
--

DROP TABLE IF EXISTS `invitationsAndGroupsMap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invitationsAndGroupsMap` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `invitation_id` int(10) unsigned NOT NULL,
  `group_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `invitation_id` (`invitation_id`),
  KEY `group_id` (`group_id`),
  CONSTRAINT `invitationsandgroupsmap_ibfk_1` FOREIGN KEY (`invitation_id`) REFERENCES `invitations` (`id`),
  CONSTRAINT `invitationsandgroupsmap_ibfk_2` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invitationsAndGroupsMap`
--

LOCK TABLES `invitationsAndGroupsMap` WRITE;
/*!40000 ALTER TABLE `invitationsAndGroupsMap` DISABLE KEYS */;
INSERT INTO `invitationsAndGroupsMap` VALUES (1,1,1);
/*!40000 ALTER TABLE `invitationsAndGroupsMap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `linkAppInformations`
--

DROP TABLE IF EXISTS `linkAppInformations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `linkAppInformations` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `url` varchar(2000) NOT NULL,
  `img_url` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `linkAppInformations`
--

LOCK TABLES `linkAppInformations` WRITE;
/*!40000 ALTER TABLE `linkAppInformations` DISABLE KEYS */;
/*!40000 ALTER TABLE `linkAppInformations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `linkApps`
--

DROP TABLE IF EXISTS `linkApps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `linkApps` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `app_id` int(10) unsigned NOT NULL,
  `link_app_info_id` int(10) unsigned NOT NULL,
  `group_link_app_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `app_id` (`app_id`),
  KEY `link_app_info_id` (`link_app_info_id`),
  KEY `group_link_app_id` (`group_link_app_id`),
  CONSTRAINT `linkapps_ibfk_1` FOREIGN KEY (`app_id`) REFERENCES `apps` (`id`),
  CONSTRAINT `linkapps_ibfk_2` FOREIGN KEY (`link_app_info_id`) REFERENCES `linkAppInformations` (`id`),
  CONSTRAINT `linkapps_ibfk_3` FOREIGN KEY (`group_link_app_id`) REFERENCES `groupLinkApps` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `linkApps`
--

LOCK TABLES `linkApps` WRITE;
/*!40000 ALTER TABLE `linkApps` DISABLE KEYS */;
/*!40000 ALTER TABLE `linkApps` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `logWithApps`
--

DROP TABLE IF EXISTS `logWithApps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `logWithApps` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `website_app_id` int(10) unsigned NOT NULL,
  `logWith_website_app_id` int(10) unsigned NOT NULL,
  `group_logWith_app_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `website_app_id` (`website_app_id`),
  KEY `logWith_website_app_id` (`logWith_website_app_id`),
  KEY `group_logWith_app_id` (`group_logWith_app_id`),
  CONSTRAINT `logwithapps_ibfk_1` FOREIGN KEY (`website_app_id`) REFERENCES `websiteApps` (`id`),
  CONSTRAINT `logwithapps_ibfk_2` FOREIGN KEY (`logWith_website_app_id`) REFERENCES `websiteApps` (`id`),
  CONSTRAINT `logwithapps_ibfk_3` FOREIGN KEY (`group_logWith_app_id`) REFERENCES `groupLogWithApps` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `logWithApps`
--

LOCK TABLES `logWithApps` WRITE;
/*!40000 ALTER TABLE `logWithApps` DISABLE KEYS */;
/*!40000 ALTER TABLE `logWithApps` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `logWithUpdates`
--

DROP TABLE IF EXISTS `logWithUpdates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `logWithUpdates` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `update_id` int(10) unsigned NOT NULL,
  `website_app_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `update_id` (`update_id`),
  KEY `website_app_id` (`website_app_id`),
  CONSTRAINT `logwithupdates_ibfk_1` FOREIGN KEY (`update_id`) REFERENCES `updates` (`id`),
  CONSTRAINT `logwithupdates_ibfk_2` FOREIGN KEY (`website_app_id`) REFERENCES `websiteApps` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `logWithUpdates`
--

LOCK TABLES `logWithUpdates` WRITE;
/*!40000 ALTER TABLE `logWithUpdates` DISABLE KEYS */;
/*!40000 ALTER TABLE `logWithUpdates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `loginWithWebsites`
--

DROP TABLE IF EXISTS `loginWithWebsites`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `loginWithWebsites` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `website_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `website_id` (`website_id`),
  CONSTRAINT `loginwithwebsites_ibfk_1` FOREIGN KEY (`website_id`) REFERENCES `websites` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `loginWithWebsites`
--

LOCK TABLES `loginWithWebsites` WRITE;
/*!40000 ALTER TABLE `loginWithWebsites` DISABLE KEYS */;
INSERT INTO `loginWithWebsites` VALUES (1,7),(2,28),(3,92);
/*!40000 ALTER TABLE `loginWithWebsites` ENABLE KEYS */;
UNLOCK TABLES;

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
  `date` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `logs`
--

LOCK TABLES `logs` WRITE;
/*!40000 ALTER TABLE `logs` DISABLE KEYS */;
INSERT INTO `logs` VALUES ('com.Ease.servlet.CheckInvitation',0,NULL,'<email:heniart.thomas@gmail.com>','No message','2016-12-01 12:51:10'),('com.Ease.servlet.CheckInvitation',0,NULL,'<email:heniart.thomas@gmail.com>','No message','2016-12-01 12:51:34'),('com.Ease.servlet.CheckInvitation',0,NULL,'<email:heniart.thomas@gmail.com>','No message','2016-12-01 12:52:51'),('com.Ease.servlet.CheckInvitation',0,NULL,'<email:heniart.thomas@gmail.com>','No message','2016-12-01 14:13:58'),('com.Ease.servlet.CheckInvitation',0,NULL,'<email:heniart.thomas@gmail.com>','No message','2016-12-01 14:14:47'),('com.Ease.servlet.CheckInvitation',0,NULL,'<email:heniart.thomas@gmail.com>','No message','2016-12-01 14:18:29'),('com.Ease.servlet.CheckInvitation',0,NULL,'<email:heniart.thomas@gmail.com>','No message','2016-12-01 14:20:32'),('com.Ease.servlet.CheckInvitation',200,NULL,'<email:heniart.thomas@gmail.com>','1 You receveid an email','2016-12-01 14:33:42'),('com.Ease.servlet.CheckInvitation',0,NULL,'<email:titomheniart@gmail.com>','No message','2016-12-01 14:34:53'),('com.Ease.servlet.CheckInvitation',200,NULL,'<email:titomheniart@gmail.com>','2 Go to registration','2016-12-01 14:37:25'),('com.Ease.servlet.Registration',200,NULL,'<fname:toto><email:heniart.thomas@gmail.com>','Registered successfully','2016-12-01 14:40:20'),('com.Ease.servlet.ConnectionServlet',1,NULL,'<email:heniart.thomas@gmail.com>','java.sql.SQLException: Before start of result set.\nStackTrace :\ncom.mysql.jdbc.SQLError.createSQLException(SQLError.java:963)\ncom.mysql.jdbc.SQLError.createSQLException(SQLError.java:896)\ncom.mysql.jdbc.SQLError.createSQLException(SQLError.java:885)\ncom.mysql.jdbc.SQLError.createSQLException(SQLError.java:860)\ncom.mysql.jdbc.ResultSetImpl.checkRowPos(ResultSetImpl.java:790)\ncom.mysql.jdbc.ResultSetImpl.getStringInternal(ResultSetImpl.java:5240)\ncom.mysql.jdbc.ResultSetImpl.getString(ResultSetImpl.java:5163)\norg.apache.tomcat.dbcp.dbcp.DelegatingResultSet.getString(DelegatingResultSet.java:213)\norg.apache.tomcat.dbcp.dbcp.DelegatingResultSet.getString(DelegatingResultSet.java:213)\ncom.Ease.dashboard.Keys.loadKeys(Keys.java:25)\ncom.Ease.dashboard.User.loadUser(User.java:42)\ncom.Ease.servlet.ConnectionServlet.doPost(ConnectionServlet.java:70)\njavax.servlet.http.HttpServlet.service(HttpServlet.java:650)\njavax.servlet.http.HttpServlet.service(HttpServlet.java:731)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:303)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:208)\norg.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:52)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:241)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:208)\norg.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:218)\norg.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:122)\norg.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:505)\norg.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:169)\norg.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:103)\norg.apache.catalina.valves.AccessLogValve.invoke(AccessLogValve.java:956)\norg.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:116)\norg.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:442)\norg.apache.coyote.http11.AbstractHttp11Processor.process(AbstractHttp11Processor.java:1082)\norg.apache.coyote.AbstractProtocol$AbstractConnectionHandler.process(AbstractProtocol.java:623)\norg.apache.tomcat.util.net.JIoEndpoint$SocketProcessor.run(JIoEndpoint.java:318)\njava.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)\njava.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)\norg.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)\njava.lang.Thread.run(Thread.java:745)','2016-12-01 14:41:36'),('com.Ease.servlet.ConnectionServlet',200,NULL,'<email:heniart.thomas@gmail.com>','Successfully connected','2016-12-01 14:42:34'),('com.Ease.servlet.CheckInvitation',200,NULL,'<email:heniart.thomas@gmail.com>','2 Go to registration','2016-12-01 14:50:18'),('com.Ease.servlet.CheckInvitation',200,NULL,'<email:pierre0debruyne@gmail.com>','2 Go to registration','2016-12-01 14:57:59'),('com.Ease.servlet.Registration',3,NULL,'<fname:null><email:pierre0debruyne@gmail.com>','Your name is too short.','2016-12-01 14:59:38'),('com.Ease.servlet.Registration',200,NULL,'<fname:Pierre><email:pierre0debruyne@gmail.com>','Registered successfully','2016-12-01 15:00:25'),('com.Ease.Servlet.AddProfile',3,NULL,'','You need to be connected to do that.','2016-12-05 09:52:50'),('com.Ease.Servlet.CheckInvitation',200,NULL,'<name:toto><email:heniart.thomas@gmail.com>','2 Go to registration','2016-12-07 17:04:36'),('com.Ease.Servlet.CheckInvitation',200,NULL,'<name:toto><email:Michel@test.com>','2 Go to registration','2016-12-07 17:05:32'),('com.Ease.Servlet.Registration',200,NULL,'<fname:toto><email:Michel@test.com>','Registered successfully','2016-12-07 17:05:37'),('com.Ease.Servlet.CheckInvitation',2,NULL,'<name:toto><email:titomheniart@gmail.com>','This group does not exist.','2016-12-07 17:19:42'),('com.Ease.Servlet.ConnectionServlet',4,NULL,'<email:heniart.thomas@gmail.com>','Wrong email or password.','2016-12-08 16:10:12'),('com.Ease.Servlet.ConnectionServlet',200,NULL,'<email:heniart.thomas@gmail.com>','Successfully connected','2016-12-08 16:10:21'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-08 16:10:54'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-08 16:10:55'),('com.Ease.Servlet.ConnectionServlet',200,NULL,'<email:heniart.thomas@gmail.com>','Successfully connected','2016-12-08 16:11:28'),('com.Ease.Servlet.ConnectionServlet',200,1,'<email:heniart.thomas@gmail.com>','Successfully connected','2016-12-08 16:11:44'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-08 16:19:53'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-08 16:19:54'),('com.Ease.Servlet.ConnectionServlet',200,1,'<email:heniart.thomas@gmail.com>','Successfully connected','2016-12-08 16:20:11'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-08 16:21:23'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-08 16:21:24'),('com.Ease.Servlet.ConnectionServlet',200,1,'<email:heniart.thomas@gmail.com>','Successfully connected','2016-12-08 16:21:41'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-08 16:25:19'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-08 16:25:20'),('com.Ease.Servlet.ConnectionServlet',4,1,'<email:heniart.thomas@gmail.com>','Wrong email or password.','2016-12-08 16:25:43'),('com.Ease.Servlet.ConnectionServlet',200,1,'<email:heniart.thomas@gmail.com>','Successfully connected','2016-12-08 16:25:54'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-08 16:31:32'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-08 16:31:33'),('com.Ease.Servlet.ConnectionServlet',200,1,'<email:heniart.thomas@gmail.com>','Successfully connected','2016-12-08 16:31:52'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-08 16:38:09'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-08 16:38:10'),('com.Ease.Servlet.ConnectionServlet',200,1,'<email:heniart.thomas@gmail.com>','Successfully connected','2016-12-08 16:38:22'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-08 16:40:41'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-08 16:40:41'),('com.Ease.Servlet.ConnectionServlet',200,1,'<email:heniart.thomas@gmail.com>','Successfully connected','2016-12-08 16:40:53'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-08 17:58:19'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-08 17:58:21'),('com.Ease.Servlet.ConnectionServlet',3,1,'<socketId:null><email:heniart.thomas@gmail.com>','No websocket','2016-12-08 17:58:38'),('com.Ease.Servlet.ConnectionServlet',3,NULL,'<socketId:null><email:heniart.thomas@gmail.com>','No websocket','2016-12-08 18:00:46'),('com.Ease.Servlet.ConnectionServlet',3,NULL,'<socketId:null><email:heniart.thomas@gmail.com>','No websocket','2016-12-08 18:02:28'),('com.Ease.Servlet.ConnectionServlet',3,NULL,'<socketId:null><email:heniart.thomas@gmail.com>','No websocket','2016-12-08 18:05:27'),('com.Ease.Servlet.ConnectionServlet',3,NULL,'<socketId:null><email:heniart.thomas@gmail.com>','No websocket','2016-12-08 18:08:11'),('com.Ease.Servlet.ConnectionServlet',3,NULL,'<socketId:null><email:heniart.thomas@gmail.com>','No websocket','2016-12-08 18:09:04'),('com.Ease.Servlet.ConnectionServlet',200,NULL,'<socketId:3><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-11 14:58:44'),('com.Ease.Servlet.ConnectionServlet',200,NULL,'<socketId:2><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-11 15:05:32'),('com.Ease.Servlet.ConnectionServlet',200,NULL,'<socketId:2><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-11 15:10:23'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-11 15:12:48'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-11 15:12:50'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-11 15:12:50'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:3><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-11 15:12:58'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-11 15:17:08'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-11 15:17:10'),('com.Ease.Servlet.ConnectionServlet',200,NULL,'<socketId:2><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-11 19:10:24'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-11 19:11:59'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-11 19:12:00'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-11 19:12:07'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-11 19:13:11'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-11 19:13:12'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:3><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-11 19:13:22'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-11 19:14:58'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-11 19:14:59'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:2><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-11 19:15:20'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-11 19:17:34'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-11 19:17:35'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-11 19:18:12'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-11 19:20:47'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-11 19:20:48'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-11 19:21:17'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-11 19:23:04'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-11 19:23:05'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-11 19:23:06'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:4><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-11 19:23:20'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-11 19:34:27'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-11 19:34:28'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-11 19:34:48'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-11 19:37:27'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-11 19:37:28'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-11 19:37:39'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-11 19:54:15'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-11 19:54:16'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:2><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-11 19:54:27'),('com.Ease.Servlet.ConnectionServlet',200,NULL,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-12 09:47:42'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-12 09:47:58'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-12 09:47:58'),('com.Ease.Servlet.ConnectionServlet',200,NULL,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-12 09:49:20'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-12 09:55:56'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-12 09:55:57'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-12 10:01:19'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-12 10:43:54'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-12 10:43:55'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:0><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-12 10:44:03'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-12 10:45:51'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-12 10:45:52'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-12 10:45:59'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-12 11:18:19'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-12 11:18:20'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-12 11:18:52'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-12 11:51:36'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-12 11:51:37'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-12 11:51:48'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-12 11:52:38'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-12 11:52:39'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-12 11:52:47'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-12 11:53:49'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-12 11:53:49'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-12 11:53:55'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-12 11:54:35'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-12 11:54:35'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-12 11:54:54'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-12 11:57:04'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-12 11:57:05'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-12 11:57:11'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-12 11:59:11'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-12 11:59:12'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-12 11:59:19'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-12 12:01:01'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-12 12:01:02'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-12 12:01:07'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-12 12:02:20'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-12 12:02:20'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-12 12:02:25'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-12 12:32:52'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-12 12:32:54'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:0><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-12 12:33:00'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-12 12:35:35'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-12 12:35:36'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-12 12:35:46'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-12 12:35:51'),('com.Ease.Servlet.ConnectionServlet',200,NULL,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-12 14:09:28'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-12 14:09:34'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-12 14:09:34'),('com.Ease.Servlet.ConnectionServlet',200,NULL,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-12 14:11:49'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-12 14:12:06'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-12 14:12:07'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:3><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-12 14:12:44'),('com.Ease.Servlet.ConnectionServlet',200,NULL,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-12 14:17:23'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-12 14:17:31'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-12 14:17:32'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:3><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-12 14:17:50'),('com.Ease.Servlet.ConnectionServlet',200,NULL,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-12 14:24:31'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-12 14:29:50'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-12 14:29:51'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:2><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-12 14:30:10'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-12 14:35:20'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-12 14:35:21'),('com.Ease.Servlet.ConnectionServlet',200,NULL,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-12 14:40:08'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-12 14:40:31'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-12 14:40:31'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:3><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-12 14:40:34'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-12 14:46:11'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-12 14:46:12'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-12 14:46:20'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-12 14:47:29'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-12 14:47:29'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-12 14:47:38'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-12 14:48:18'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-12 14:48:18'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-12 14:48:23'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-12 15:17:44'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-12 15:17:45'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:3><email:heniart.thomas@gmail.com>','Successfully connected','2016-12-12 15:17:52'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-12 16:52:17'),('com.Ease.Servlet.ConnectionWithCookies',2,1,'','An user is already connected.','2016-12-12 16:52:18'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-12 16:52:34'),('com.Ease.Servlet.ConnectionWithCookies',200,NULL,'','Connected with cookies.','2016-12-12 17:13:22'),('com.Ease.Servlet.ConnectionWithCookies',2,NULL,'','Wrong session id.','2016-12-12 17:13:23'),('com.Ease.Servlet.ConnectionServlet',4,NULL,'<socketId:1><email:heniart.thomas@gmail.com>','Wrong email or password.','2016-12-12 17:13:33'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-12 17:13:41'),('com.Ease.Servlet.ConnectionWithCookies',200,NULL,'','Connected with cookies.','2016-12-12 17:15:23'),('com.Ease.Servlet.ConnectionWithCookies',2,NULL,'','Wrong session id.','2016-12-12 17:15:23'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:0><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-12 17:15:38'),('com.Ease.Servlet.AddProfile',200,1,'<name:Pouette>','{\"user_id\":\"1\",\"column\":2,\"id\":\"7\",\"position\":0,\"group_profile_id\":\"null\"}','2016-12-12 17:19:38'),('com.Ease.Servlet.ConnectionServlet',200,NULL,'<socketId:0><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-12 17:23:34'),('com.Ease.Servlet.ConnectionWithCookies',200,NULL,'','Connected with cookies.','2016-12-12 17:23:48'),('com.Ease.Servlet.ConnectionWithCookies',2,NULL,'','Wrong session id.','2016-12-12 17:23:49'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:2><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-12 17:24:01'),('com.Ease.Servlet.ConnectionWithCookies',200,NULL,'','Connected with cookies.','2016-12-12 17:24:25'),('com.Ease.Servlet.ConnectionWithCookies',2,NULL,'','Wrong session id.','2016-12-12 17:24:26'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:6><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-12 17:24:40'),('com.Ease.Servlet.ConnectionServlet',200,NULL,'<socketId:0><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-12 17:50:42'),('com.Ease.Servlet.ConnectionWithCookies',200,NULL,'','Connected with cookies.','2016-12-12 17:51:07'),('com.Ease.Servlet.ConnectionWithCookies',2,NULL,'','Wrong session id.','2016-12-12 17:51:07'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:2><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-12 17:51:17'),('com.Ease.Servlet.AddProfile',200,1,'<name:Pouette>','{\"user_id\":\"1\",\"column\":3,\"id\":\"8\",\"position\":0,\"group_profile_id\":\"null\"}','2016-12-12 17:52:38'),('com.Ease.Servlet.ConnectionWithCookies',200,NULL,'','Connected with cookies.','2016-12-12 17:56:53'),('com.Ease.Servlet.ConnectionWithCookies',2,NULL,'','Wrong session id.','2016-12-12 17:56:54'),('com.Ease.Servlet.ConnectionWithCookies',200,NULL,'','Connected with cookies.','2016-12-12 17:57:02'),('com.Ease.Servlet.ConnectionWithCookies',2,NULL,'','Wrong session id.','2016-12-12 17:57:02'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-12 17:57:14'),('com.Ease.Servlet.AddProfile',200,1,'<name:Yolo>','{\"user_id\":\"1\",\"column\":4,\"id\":\"9\",\"position\":0,\"group_profile_id\":\"null\"}','2016-12-12 17:58:22'),('com.Ease.Servlet.ConnectionWithCookies',200,NULL,'','Connected with cookies.','2016-12-12 18:03:04'),('com.Ease.Servlet.ConnectionWithCookies',2,NULL,'','Wrong session id.','2016-12-12 18:03:05'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-12 18:03:13'),('com.Ease.Servlet.AddProfile',2,1,'','Wrong socketId.','2016-12-12 18:03:43'),('com.Ease.Servlet.AddProfile',200,1,'<name:Yolo2>','{\"user_id\":\"1\",\"column\":1,\"id\":\"10\",\"position\":1,\"group_profile_id\":\"null\"}','2016-12-12 18:04:00'),('com.Ease.Servlet.AddProfile',200,1,'<name:Yolo2>','{\"user_id\":\"1\",\"column\":2,\"id\":\"11\",\"position\":1,\"group_profile_id\":\"null\"}','2016-12-12 18:04:51'),('com.Ease.Servlet.AddProfile',200,1,'<name:Yolo2>','{\"user_id\":\"1\",\"column\":3,\"id\":\"12\",\"position\":1,\"group_profile_id\":\"null\"}','2016-12-12 18:05:05'),('com.Ease.Servlet.ConnectionServlet',200,NULL,'<socketId:2><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-12 18:13:46'),('com.Ease.Servlet.ConnectionWithCookies',200,NULL,'','Connected with cookies.','2016-12-12 18:14:01'),('com.Ease.Servlet.ConnectionWithCookies',2,NULL,'','Wrong session id.','2016-12-12 18:14:01'),('com.Ease.Servlet.ConnectionWithCookies',200,NULL,'','Connected with cookies.','2016-12-12 18:14:06'),('com.Ease.Servlet.ConnectionWithCookies',2,NULL,'','Wrong session id.','2016-12-12 18:14:07'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:8><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-12 18:14:15'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:2><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-13 08:59:45'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-13 09:05:05'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:2><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-13 09:05:43'),('com.Ease.Servlet.AddProfile',200,1,'<name:Yolo2>','{\"user_id\":\"1\",\"column\":4,\"id\":\"13\",\"position\":1,\"group_profile_id\":\"null\"}','2016-12-13 09:06:25'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-13 10:02:49'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-13 10:03:15'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-13 10:10:38'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Connected with cookies.','2016-12-13 10:19:31'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-13 10:19:44'),('com.Ease.Servlet.AddProfile',200,1,'<name:Pute>','{\"user_id\":\"1\",\"column\":1,\"id\":\"14\",\"position\":2,\"group_profile_id\":\"null\"}','2016-12-13 10:20:25'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-13 11:47:32'),('com.Ease.Servlet.CheckInvitation',3,1,'<name:Prout><email:prout@gmail.com>','You are logged on Ease.','2016-12-13 11:48:30'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-13 11:48:42'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-13 11:50:14'),('com.Ease.Servlet.CheckInvitation',3,1,'<name:apeifbd><email:aezmcn@zpeofn.fr>','You are logged on Ease.','2016-12-13 11:50:40'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-13 11:50:54'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-13 12:08:57'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-13 12:10:14'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-13 12:13:51'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-13 12:14:04'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:6><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-13 12:54:51'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-13 12:56:05'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-13 12:56:23'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:3><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-13 12:56:30'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Redirected to index.jsp','2016-12-13 12:57:13'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-13 12:57:20'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-13 14:29:09'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Redirected to index.jsp','2016-12-13 14:29:49'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-13 14:30:07'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:2><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-13 14:30:40'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-13 14:30:59'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:4><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-13 14:31:12'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Redirected to index.jsp','2016-12-13 14:43:39'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-13 14:43:50'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:3><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-13 14:44:17'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-13 14:47:08'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-13 14:47:20'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:3><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-13 14:47:34'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Redirected to index.jsp','2016-12-13 14:51:30'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-13 14:51:37'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:2><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-13 14:51:44'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Redirected to index.jsp','2016-12-13 14:54:39'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-13 14:54:51'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:2><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-13 14:55:04'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Redirected to index.jsp','2016-12-13 15:10:46'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Redirected to index.jsp','2016-12-13 15:20:40'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Redirected to index.jsp','2016-12-13 15:22:04'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Redirected to index.jsp','2016-12-13 15:32:37'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Redirected to index.jsp','2016-12-13 16:59:17'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Redirected to index.jsp','2016-12-13 17:07:50'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Redirected to index.jsp','2016-12-13 17:15:34'),('com.Ease.Servlet.RemoveProfile',200,1,'<profileId:10>','Profile removed.','2016-12-13 17:15:49'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Redirected to index.jsp','2016-12-13 17:18:03'),('com.Ease.Servlet.RemoveProfile',200,1,'<profileId:9>','Profile removed.','2016-12-13 17:18:24'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'','Redirected to index.jsp','2016-12-13 17:23:26'),('com.Ease.Servlet.RemoveProfile',200,1,'<profileId:8>','Profile removed.','2016-12-13 17:23:43'),('com.Ease.Servlet.RemoveProfile',200,1,'<profileId:7>','Profile removed.','2016-12-13 17:23:55'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-13 18:46:41'),('com.Ease.Servlet.Profile.MoveProfile',2,1,'<columnIdxDest:null><profileId:6><positionDest:null>','Wrong columnIdx.','2016-12-13 18:47:17'),('com.Ease.Servlet.Profile.MoveProfile',2,1,'<columnIdxDest:null><profileId:6><positionDest:null>','Wrong columnIdx.','2016-12-13 18:48:52'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-13 18:50:36'),('com.Ease.Servlet.Profile.MoveProfile',200,1,'<columnIdxDest:3><profileId:6><positionDest:1>','Profile moved.','2016-12-13 18:50:49'),('com.Ease.Servlet.Profile.MoveProfile',200,1,'<columnIdxDest:1><profileId:4><positionDest:1>','Profile moved.','2016-12-13 18:51:08'),('com.Ease.Servlet.Profile.MoveProfile',200,1,'<columnIdxDest:4><profileId:2><positionDest:1>','Profile moved.','2016-12-13 18:51:16'),('com.Ease.Servlet.Profile.MoveProfile',200,1,'<columnIdxDest:1><profileId:5><positionDest:0>','Profile moved.','2016-12-13 18:51:23'),('com.Ease.Servlet.Profile.MoveProfile',200,1,'<columnIdxDest:4><profileId:6><positionDest:1>','Profile moved.','2016-12-13 18:51:29'),('com.Ease.Servlet.Profile.MoveProfile',200,1,'<columnIdxDest:1><profileId:3><positionDest:2>','Profile moved.','2016-12-13 18:51:32'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-13 18:53:31'),('com.Ease.Servlet.Profile.MoveProfile',200,1,'<columnIdxDest:3><profileId:2><positionDest:0>','Profile moved.','2016-12-13 18:53:48'),('com.Ease.Servlet.Profile.MoveProfile',200,1,'<columnIdxDest:1><profileId:3><positionDest:0>','Profile moved.','2016-12-13 18:54:07'),('com.Ease.Servlet.Profile.MoveProfile',200,1,'<columnIdxDest:2><profileId:5><positionDest:0>','Profile moved.','2016-12-13 18:54:14'),('com.Ease.Servlet.Profile.MoveProfile',200,1,'<columnIdxDest:2><profileId:3><positionDest:0>','Profile moved.','2016-12-13 18:54:31'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-13 18:56:55'),('com.Ease.Servlet.Profile.MoveProfile',200,1,'<columnIdxDest:3><profileId:3><positionDest:0>','Profile moved.','2016-12-13 18:57:11'),('com.Ease.Servlet.Profile.MoveProfile',200,1,'<columnIdxDest:3><profileId:3><positionDest:0>','Profile moved.','2016-12-13 18:57:36'),('com.Ease.Servlet.Profile.MoveProfile',200,1,'<columnIdxDest:2><profileId:3><positionDest:1>','Profile moved.','2016-12-13 18:57:58'),('com.Ease.Servlet.Profile.MoveProfile',200,1,'<columnIdxDest:2><profileId:3><positionDest:1>','Profile moved.','2016-12-13 18:58:07'),('com.Ease.Servlet.Profile.MoveProfile',200,1,'<columnIdxDest:1><profileId:3><positionDest:1>','Profile moved.','2016-12-13 18:58:19'),('com.Ease.Servlet.Profile.MoveProfile',200,1,'<columnIdxDest:3><profileId:3><positionDest:1>','Profile moved.','2016-12-13 18:58:29'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-13 19:00:38'),('com.Ease.Servlet.Profile.MoveProfile',200,1,'<columnIdxDest:2><profileId:3><positionDest:1>','Profile moved.','2016-12-13 19:00:54'),('com.Ease.Servlet.Profile.MoveProfile',200,1,'<columnIdxDest:4><profileId:2><positionDest:0>','Profile moved.','2016-12-13 19:01:13'),('com.Ease.Servlet.Profile.MoveProfile',200,1,'<columnIdxDest:3><profileId:3><positionDest:0>','Profile moved.','2016-12-13 19:01:54'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-13 19:05:14'),('com.Ease.Servlet.Profile.MoveProfile',200,1,'<columnIdxDest:3><profileId:6><positionDest:0>','Profile moved.','2016-12-13 19:05:38'),('com.Ease.Servlet.Profile.MoveProfile',200,1,'<columnIdxDest:4><profileId:6><positionDest:1>','Profile moved.','2016-12-13 19:05:49'),('com.Ease.Servlet.Profile.MoveProfile',200,1,'<columnIdxDest:3><profileId:6><positionDest:0>','Profile moved.','2016-12-13 19:06:06'),('com.Ease.Servlet.Profile.MoveProfile',200,1,'<columnIdxDest:2><profileId:6><positionDest:0>','Profile moved.','2016-12-13 19:06:24'),('com.Ease.Servlet.Profile.MoveProfile',200,1,'<columnIdxDest:3><profileId:6><positionDest:0>','Profile moved.','2016-12-13 19:06:34'),('com.Ease.Servlet.Profile.MoveProfile',200,1,'<columnIdxDest:3><profileId:6><positionDest:0>','Profile moved.','2016-12-13 19:06:41'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-13 19:10:59'),('com.Ease.Servlet.Profile.MoveProfile',200,1,'<columnIdxDest:2><profileId:5><positionDest:0>','Profile moved.','2016-12-13 19:11:07'),('com.Ease.Servlet.Profile.MoveProfile',200,1,'<columnIdxDest:2><profileId:5><positionDest:0>','Profile moved.','2016-12-13 19:11:25'),('com.Ease.Servlet.Profile.MoveProfile',200,1,'<columnIdxDest:2><profileId:5><positionDest:0>','Profile moved.','2016-12-13 19:11:33'),('com.Ease.Servlet.Profile.AddProfile',3,1,'<color:#373B603><name:Pute>','Wrong color.','2016-12-13 19:12:55'),('com.Ease.Servlet.Profile.AddProfile',200,1,'<color:#373B60><name:Pute>','{\"user_id\":\"1\",\"column\":1,\"id\":\"15\",\"position\":1,\"group_profile_id\":\"null\"}','2016-12-13 19:13:23'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-13 19:17:56'),('com.Ease.Servlet.Profile.AddProfile',200,1,'<color:#373B60><name:Pute>','{\"user_id\":\"1\",\"column\":3,\"id\":\"16\",\"position\":1,\"group_profile_id\":\"null\"}','2016-12-13 19:18:06'),('com.Ease.Servlet.Profile.AddProfile',200,1,'<color:#373B60><name:Pute>','{\"user_id\":\"1\",\"column\":4,\"id\":\"17\",\"position\":1,\"group_profile_id\":\"null\"}','2016-12-13 19:18:26'),('com.Ease.Servlet.Profile.AddProfile',200,1,'<color:#373B60><name:Pute>','{\"user_id\":\"1\",\"column\":1,\"id\":\"18\",\"position\":2,\"group_profile_id\":\"null\"}','2016-12-13 19:18:36'),('com.Ease.Servlet.Profile.AddProfile',200,1,'<color:#373B60><name:Pute>','{\"user_id\":\"1\",\"column\":2,\"id\":\"19\",\"position\":2,\"group_profile_id\":\"null\"}','2016-12-13 19:18:44'),('com.Ease.Servlet.Profile.AddProfile',200,1,'<color:#373B60><name:Pute>','{\"user_id\":\"1\",\"column\":3,\"id\":\"20\",\"position\":2,\"group_profile_id\":\"null\"}','2016-12-13 19:20:03'),('com.Ease.Servlet.Profile.AddProfile',200,1,'<color:#373B60><name:Pute>','{\"user_id\":\"1\",\"column\":4,\"id\":\"21\",\"position\":2,\"group_profile_id\":\"null\"}','2016-12-13 19:20:29'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-13 22:47:33'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-13 22:49:29'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-13 22:51:18'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-13 22:54:02'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-13 22:56:51'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-13 22:58:35'),('com.Ease.Servlet.App.AddClassicApp',200,1,'<websiteId:2><password:a><profileId:2><name:Facebook><login:a>','ClassicApp added.','2016-12-13 22:58:47'),('com.Ease.Servlet.ConnectionWithCookies',2,NULL,'<socketId:null>','Wrong session id.','2016-12-13 23:16:23'),('com.Ease.Servlet.ConnectionServlet',1,NULL,'<socketId:1><email:heniart.thomas@gmail.com>','java.sql.SQLException: Illegal operation on empty result set..\nStackTrace :\ncom.mysql.jdbc.SQLError.createSQLException(SQLError.java:963)\ncom.mysql.jdbc.SQLError.createSQLException(SQLError.java:896)\ncom.mysql.jdbc.SQLError.createSQLException(SQLError.java:885)\ncom.mysql.jdbc.SQLError.createSQLException(SQLError.java:860)\ncom.mysql.jdbc.ResultSetImpl.checkRowPos(ResultSetImpl.java:790)\ncom.mysql.jdbc.ResultSetImpl.getStringInternal(ResultSetImpl.java:5240)\ncom.mysql.jdbc.ResultSetImpl.getString(ResultSetImpl.java:5163)\norg.apache.tomcat.dbcp.dbcp.DelegatingResultSet.getString(DelegatingResultSet.java:213)\norg.apache.tomcat.dbcp.dbcp.DelegatingResultSet.getString(DelegatingResultSet.java:213)\ncom.Ease.Dashboard.App.AppInformation.loadAppInformation(AppInformation.java:34)\ncom.Ease.Dashboard.App.App.loadApps(App.java:53)\ncom.Ease.Dashboard.Profile.Profile.loadProfiles(Profile.java:63)\ncom.Ease.Dashboard.User.User.loadProfiles(User.java:265)\ncom.Ease.Dashboard.User.User.loadUser(User.java:60)\ncom.Ease.Servlet.ConnectionServlet.doPost(ConnectionServlet.java:76)\njavax.servlet.http.HttpServlet.service(HttpServlet.java:650)\njavax.servlet.http.HttpServlet.service(HttpServlet.java:731)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:303)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:208)\norg.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:52)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:241)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:208)\norg.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:218)\norg.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:122)\norg.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:505)\norg.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:169)\norg.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:103)\norg.apache.catalina.valves.AccessLogValve.invoke(AccessLogValve.java:958)\norg.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:116)\norg.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:452)\norg.apache.coyote.http11.AbstractHttp11Processor.process(AbstractHttp11Processor.java:1087)\norg.apache.coyote.AbstractProtocol$AbstractConnectionHandler.process(AbstractProtocol.java:637)\norg.apache.tomcat.util.net.JIoEndpoint$SocketProcessor.run(JIoEndpoint.java:318)\njava.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)\njava.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)\norg.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)\njava.lang.Thread.run(Thread.java:745)','2016-12-13 23:17:46'),('com.Ease.Servlet.ConnectionServlet',1,NULL,'<socketId:1><email:heniart.thomas@gmail.com>','java.sql.SQLException: Illegal operation on empty result set..\nStackTrace :\ncom.mysql.jdbc.SQLError.createSQLException(SQLError.java:963)\ncom.mysql.jdbc.SQLError.createSQLException(SQLError.java:896)\ncom.mysql.jdbc.SQLError.createSQLException(SQLError.java:885)\ncom.mysql.jdbc.SQLError.createSQLException(SQLError.java:860)\ncom.mysql.jdbc.ResultSetImpl.checkRowPos(ResultSetImpl.java:790)\ncom.mysql.jdbc.ResultSetImpl.getStringInternal(ResultSetImpl.java:5240)\ncom.mysql.jdbc.ResultSetImpl.getString(ResultSetImpl.java:5163)\norg.apache.tomcat.dbcp.dbcp.DelegatingResultSet.getString(DelegatingResultSet.java:213)\norg.apache.tomcat.dbcp.dbcp.DelegatingResultSet.getString(DelegatingResultSet.java:213)\ncom.Ease.Dashboard.App.AppInformation.loadAppInformation(AppInformation.java:35)\ncom.Ease.Dashboard.App.App.loadApps(App.java:53)\ncom.Ease.Dashboard.Profile.Profile.loadProfiles(Profile.java:63)\ncom.Ease.Dashboard.User.User.loadProfiles(User.java:265)\ncom.Ease.Dashboard.User.User.loadUser(User.java:60)\ncom.Ease.Servlet.ConnectionServlet.doPost(ConnectionServlet.java:76)\njavax.servlet.http.HttpServlet.service(HttpServlet.java:650)\njavax.servlet.http.HttpServlet.service(HttpServlet.java:731)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:303)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:208)\norg.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:52)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:241)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:208)\norg.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:218)\norg.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:122)\norg.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:505)\norg.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:169)\norg.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:103)\norg.apache.catalina.valves.AccessLogValve.invoke(AccessLogValve.java:958)\norg.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:116)\norg.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:452)\norg.apache.coyote.http11.AbstractHttp11Processor.process(AbstractHttp11Processor.java:1087)\norg.apache.coyote.AbstractProtocol$AbstractConnectionHandler.process(AbstractProtocol.java:637)\norg.apache.tomcat.util.net.JIoEndpoint$SocketProcessor.run(JIoEndpoint.java:318)\njava.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)\njava.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)\norg.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)\njava.lang.Thread.run(Thread.java:745)','2016-12-13 23:19:26'),('com.Ease.Servlet.ConnectionServlet',1,NULL,'<socketId:1><email:heniart.thomas@gmail.com>','No app information','2016-12-13 23:23:30'),('com.Ease.Servlet.ConnectionServlet',1,NULL,'<socketId:1><email:heniart.thomas@gmail.com>','java.sql.SQLException: Column Index out of range, 5 > 4. .\nStackTrace :\ncom.mysql.jdbc.SQLError.createSQLException(SQLError.java:963)\ncom.mysql.jdbc.SQLError.createSQLException(SQLError.java:896)\ncom.mysql.jdbc.SQLError.createSQLException(SQLError.java:885)\ncom.mysql.jdbc.SQLError.createSQLException(SQLError.java:860)\ncom.mysql.jdbc.ResultSetImpl.checkColumnBounds(ResultSetImpl.java:767)\ncom.mysql.jdbc.ResultSetImpl.getStringInternal(ResultSetImpl.java:5241)\ncom.mysql.jdbc.ResultSetImpl.getString(ResultSetImpl.java:5163)\norg.apache.tomcat.dbcp.dbcp.DelegatingResultSet.getString(DelegatingResultSet.java:213)\norg.apache.tomcat.dbcp.dbcp.DelegatingResultSet.getString(DelegatingResultSet.java:213)\ncom.Ease.Dashboard.App.WebsiteApp.WebsiteApp.loadWebsiteApp(WebsiteApp.java:50)\ncom.Ease.Dashboard.App.App.loadApps(App.java:63)\ncom.Ease.Dashboard.Profile.Profile.loadProfiles(Profile.java:63)\ncom.Ease.Dashboard.User.User.loadProfiles(User.java:265)\ncom.Ease.Dashboard.User.User.loadUser(User.java:60)\ncom.Ease.Servlet.ConnectionServlet.doPost(ConnectionServlet.java:76)\njavax.servlet.http.HttpServlet.service(HttpServlet.java:650)\njavax.servlet.http.HttpServlet.service(HttpServlet.java:731)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:303)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:208)\norg.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:52)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:241)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:208)\norg.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:218)\norg.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:122)\norg.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:505)\norg.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:169)\norg.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:103)\norg.apache.catalina.valves.AccessLogValve.invoke(AccessLogValve.java:958)\norg.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:116)\norg.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:452)\norg.apache.coyote.http11.AbstractHttp11Processor.process(AbstractHttp11Processor.java:1087)\norg.apache.coyote.AbstractProtocol$AbstractConnectionHandler.process(AbstractProtocol.java:637)\norg.apache.tomcat.util.net.JIoEndpoint$SocketProcessor.run(JIoEndpoint.java:318)\njava.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)\njava.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)\norg.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)\njava.lang.Thread.run(Thread.java:745)','2016-12-13 23:34:41'),('com.Ease.Servlet.ConnectionServlet',1,NULL,'<socketId:1><email:heniart.thomas@gmail.com>','Website app not complete in db.','2016-12-14 08:33:15'),('com.Ease.Servlet.ConnectionServlet',1,NULL,'<socketId:1><email:heniart.thomas@gmail.com>','Website app not complete in db.','2016-12-14 08:35:18'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 08:38:12'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-14 08:40:20'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-14 08:41:33'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 08:41:45'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:2><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 08:41:53'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-14 08:43:18'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-14 08:53:22'),('com.Ease.Servlet.App.AddClassicApp',200,1,'<websiteId:2><password:aoezibn><profileId:2><name:Facebook><login:aodib>','92','2016-12-14 08:53:40'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 08:53:49'),('com.Ease.Servlet.ConnectionServlet',1,NULL,'<socketId:2><email:heniart.thomas@gmail.com>','com.Ease.Utils.GeneralException.\nStackTrace :\ncom.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp.loadClassicApp(ClassicApp.java:46)\ncom.Ease.Dashboard.App.WebsiteApp.WebsiteApp.loadWebsiteApp(WebsiteApp.java:57)\ncom.Ease.Dashboard.App.App.loadApps(App.java:66)\ncom.Ease.Dashboard.Profile.Profile.loadProfiles(Profile.java:63)\ncom.Ease.Dashboard.User.User.loadProfiles(User.java:265)\ncom.Ease.Dashboard.User.User.loadUser(User.java:60)\ncom.Ease.Servlet.ConnectionServlet.doPost(ConnectionServlet.java:76)\njavax.servlet.http.HttpServlet.service(HttpServlet.java:650)\njavax.servlet.http.HttpServlet.service(HttpServlet.java:731)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:303)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:208)\norg.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:52)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:241)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:208)\norg.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:218)\norg.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:122)\norg.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:505)\norg.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:169)\norg.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:103)\norg.apache.catalina.valves.AccessLogValve.invoke(AccessLogValve.java:958)\norg.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:116)\norg.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:452)\norg.apache.coyote.http11.AbstractHttp11Processor.process(AbstractHttp11Processor.java:1087)\norg.apache.coyote.AbstractProtocol$AbstractConnectionHandler.process(AbstractProtocol.java:637)\norg.apache.tomcat.util.net.JIoEndpoint$SocketProcessor.run(JIoEndpoint.java:318)\njava.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)\njava.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)\norg.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)\njava.lang.Thread.run(Thread.java:745)','2016-12-14 08:54:04'),('com.Ease.Servlet.ConnectionServlet',1,NULL,'<socketId:1><email:heniart.thomas@gmail.com>','com.Ease.Utils.GeneralException.\nStackTrace :\ncom.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp.loadClassicApp(ClassicApp.java:47)\ncom.Ease.Dashboard.App.WebsiteApp.WebsiteApp.loadWebsiteApp(WebsiteApp.java:57)\ncom.Ease.Dashboard.App.App.loadApps(App.java:66)\ncom.Ease.Dashboard.Profile.Profile.loadProfiles(Profile.java:63)\ncom.Ease.Dashboard.User.User.loadProfiles(User.java:265)\ncom.Ease.Dashboard.User.User.loadUser(User.java:60)\ncom.Ease.Servlet.ConnectionServlet.doPost(ConnectionServlet.java:76)\njavax.servlet.http.HttpServlet.service(HttpServlet.java:650)\njavax.servlet.http.HttpServlet.service(HttpServlet.java:731)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:303)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:208)\norg.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:52)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:241)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:208)\norg.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:218)\norg.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:122)\norg.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:505)\norg.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:169)\norg.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:103)\norg.apache.catalina.valves.AccessLogValve.invoke(AccessLogValve.java:958)\norg.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:116)\norg.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:452)\norg.apache.coyote.http11.AbstractHttp11Processor.process(AbstractHttp11Processor.java:1087)\norg.apache.coyote.AbstractProtocol$AbstractConnectionHandler.process(AbstractProtocol.java:637)\norg.apache.tomcat.util.net.JIoEndpoint$SocketProcessor.run(JIoEndpoint.java:318)\njava.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)\njava.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)\norg.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)\njava.lang.Thread.run(Thread.java:745)','2016-12-14 08:58:43'),('com.Ease.Servlet.ConnectionServlet',1,NULL,'<socketId:1><email:heniart.thomas@gmail.com>','com.Ease.Utils.GeneralException.\nStackTrace :\ncom.Ease.Dashboard.App.WebsiteApp.ClassicApp.Account.loadAccount(Account.java:40)\ncom.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp.loadClassicApp(ClassicApp.java:42)\ncom.Ease.Dashboard.App.WebsiteApp.WebsiteApp.loadWebsiteApp(WebsiteApp.java:57)\ncom.Ease.Dashboard.App.App.loadApps(App.java:66)\ncom.Ease.Dashboard.Profile.Profile.loadProfiles(Profile.java:63)\ncom.Ease.Dashboard.User.User.loadProfiles(User.java:265)\ncom.Ease.Dashboard.User.User.loadUser(User.java:60)\ncom.Ease.Servlet.ConnectionServlet.doPost(ConnectionServlet.java:76)\njavax.servlet.http.HttpServlet.service(HttpServlet.java:650)\njavax.servlet.http.HttpServlet.service(HttpServlet.java:731)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:303)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:208)\norg.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:52)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:241)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:208)\norg.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:218)\norg.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:122)\norg.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:505)\norg.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:169)\norg.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:103)\norg.apache.catalina.valves.AccessLogValve.invoke(AccessLogValve.java:958)\norg.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:116)\norg.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:452)\norg.apache.coyote.http11.AbstractHttp11Processor.process(AbstractHttp11Processor.java:1087)\norg.apache.coyote.AbstractProtocol$AbstractConnectionHandler.process(AbstractProtocol.java:637)\norg.apache.tomcat.util.net.JIoEndpoint$SocketProcessor.run(JIoEndpoint.java:318)\njava.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)\njava.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)\norg.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)\njava.lang.Thread.run(Thread.java:745)','2016-12-14 09:01:10'),('com.Ease.Servlet.ConnectionServlet',1,NULL,'<socketId:1><email:heniart.thomas@gmail.com>','com.Ease.Utils.GeneralException.\nStackTrace :\ncom.Ease.Dashboard.App.WebsiteApp.ClassicApp.Account.loadAccount(Account.java:40)\ncom.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp.loadClassicApp(ClassicApp.java:42)\ncom.Ease.Dashboard.App.WebsiteApp.WebsiteApp.loadWebsiteApp(WebsiteApp.java:57)\ncom.Ease.Dashboard.App.App.loadApps(App.java:66)\ncom.Ease.Dashboard.Profile.Profile.loadProfiles(Profile.java:63)\ncom.Ease.Dashboard.User.User.loadProfiles(User.java:265)\ncom.Ease.Dashboard.User.User.loadUser(User.java:60)\ncom.Ease.Servlet.ConnectionServlet.doPost(ConnectionServlet.java:76)\njavax.servlet.http.HttpServlet.service(HttpServlet.java:650)\njavax.servlet.http.HttpServlet.service(HttpServlet.java:731)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:303)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:208)\norg.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:52)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:241)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:208)\norg.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:218)\norg.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:122)\norg.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:505)\norg.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:169)\norg.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:103)\norg.apache.catalina.valves.AccessLogValve.invoke(AccessLogValve.java:958)\norg.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:116)\norg.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:452)\norg.apache.coyote.http11.AbstractHttp11Processor.process(AbstractHttp11Processor.java:1087)\norg.apache.coyote.AbstractProtocol$AbstractConnectionHandler.process(AbstractProtocol.java:637)\norg.apache.tomcat.util.net.JIoEndpoint$SocketProcessor.run(JIoEndpoint.java:318)\njava.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)\njava.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)\norg.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)\njava.lang.Thread.run(Thread.java:745)','2016-12-14 09:03:39'),('com.Ease.Servlet.ConnectionServlet',1,NULL,'<socketId:1><email:heniart.thomas@gmail.com>','com.Ease.Utils.GeneralException.\nStackTrace :\ncom.Ease.Dashboard.App.WebsiteApp.ClassicApp.Account.loadAccount(Account.java:41)\ncom.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp.loadClassicApp(ClassicApp.java:41)\ncom.Ease.Dashboard.App.WebsiteApp.WebsiteApp.loadWebsiteApp(WebsiteApp.java:57)\ncom.Ease.Dashboard.App.App.loadApps(App.java:66)\ncom.Ease.Dashboard.Profile.Profile.loadProfiles(Profile.java:63)\ncom.Ease.Dashboard.User.User.loadProfiles(User.java:265)\ncom.Ease.Dashboard.User.User.loadUser(User.java:60)\ncom.Ease.Servlet.ConnectionServlet.doPost(ConnectionServlet.java:76)\njavax.servlet.http.HttpServlet.service(HttpServlet.java:650)\njavax.servlet.http.HttpServlet.service(HttpServlet.java:731)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:303)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:208)\norg.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:52)\norg.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:241)\norg.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:208)\norg.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:218)\norg.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:122)\norg.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:505)\norg.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:169)\norg.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:103)\norg.apache.catalina.valves.AccessLogValve.invoke(AccessLogValve.java:958)\norg.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:116)\norg.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:452)\norg.apache.coyote.http11.AbstractHttp11Processor.process(AbstractHttp11Processor.java:1087)\norg.apache.coyote.AbstractProtocol$AbstractConnectionHandler.process(AbstractProtocol.java:637)\norg.apache.tomcat.util.net.JIoEndpoint$SocketProcessor.run(JIoEndpoint.java:318)\njava.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)\njava.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)\norg.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)\njava.lang.Thread.run(Thread.java:745)','2016-12-14 09:05:45'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 09:08:49'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-14 09:48:33'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-14 09:51:58'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 09:52:26'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:2><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 09:52:33'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-14 09:55:53'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 09:56:03'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:2><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 09:56:11'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:4><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 10:07:58'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-14 10:24:30'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 10:25:04'),('com.Ease.Servlet.ConnectionServlet',3,NULL,'<socketId:7><email:>','Wrong email.','2016-12-14 10:25:09'),('com.Ease.Servlet.ConnectionWithCookies',2,NULL,'<socketId:null>','Wrong session id.','2016-12-14 10:29:31'),('com.Ease.Servlet.ConnectionServlet',3,NULL,'<socketId:1><email:heniart.thomas@gmail.com>','Wrong password.','2016-12-14 10:30:18'),('com.Ease.Servlet.ConnectionServlet',3,NULL,'<socketId:1><email:>','Wrong email.','2016-12-14 10:30:20'),('com.Ease.Servlet.ConnectionServlet',3,NULL,'<socketId:1><email:>','Wrong email.','2016-12-14 10:30:23'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 10:30:31'),('com.Ease.Servlet.App.AddClassicApp',200,1,'<websiteId:2><password:zaeada><profileId:2><name:Facebook><login:aedead>','92','2016-12-14 10:30:49'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-14 10:33:28'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-14 10:35:20'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-14 10:37:42'),('com.Ease.Servlet.App.AddClassicApp',200,1,'<websiteId:2><password:azdazdza><profileId:6><name:Facebook><login:azdzad>','93','2016-12-14 10:40:21'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-14 10:57:31'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-14 10:59:17'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-14 11:01:55'),('com.Ease.Servlet.App.AddClassicApp',200,1,'<websiteId:2><password:zefzee><profileId:2><name:Facebook><login:aazdazd>','92','2016-12-14 11:02:04'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 11:26:44'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 11:39:34'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-14 11:43:41'),('com.Ease.Servlet.App.AddClassicApp',200,1,'<websiteId:2><password:aazazaza><profileId:2><name:Facebook><login:azazaz>','92','2016-12-14 11:43:59'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 11:44:09'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:null><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 11:44:39'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 11:49:32'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:null><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 11:49:38'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 11:49:59'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:null><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 11:53:49'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 11:54:10'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:null><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 11:57:20'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 11:57:46'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:null><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 12:07:16'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 12:07:28'),('com.Ease.Servlet.ConnectionWithCookies',2,NULL,'<socketId:null>','Wrong session id.','2016-12-14 12:09:29'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:null><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 12:09:47'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 12:10:03'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:null><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 12:10:23'),('com.Ease.Servlet.App.AddClassicApp',200,1,'<websiteId:2><password:aedfazdad><profileId:6><name:Facebook><login:adazdazd>','94','2016-12-14 12:10:45'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 12:11:10'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:null><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 12:11:31'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-14 12:25:14'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 12:25:27'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:null><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 12:25:37'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 12:26:41'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:null><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 12:27:00'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-14 12:29:19'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:5><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 12:32:29'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 12:33:11'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 12:33:46'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:null><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 12:33:58'),('com.Ease.Servlet.ConnectionWithCookies',2,NULL,'<socketId:null>','Wrong session id.','2016-12-14 12:39:26'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:null><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 12:40:09'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:null><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 12:40:17'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 12:40:35'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:null><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 12:41:03'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 12:41:30'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:null><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 12:41:48'),('com.Ease.Servlet.ConnectionWithCookies',2,NULL,'<socketId:null>','Wrong session id.','2016-12-14 15:00:35'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:null><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 15:02:08'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 15:03:14'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:null><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 15:03:26'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-14 15:08:52'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 15:09:14'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:null><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 15:09:34'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 15:09:55'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:null><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 15:10:03'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 15:10:11'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:null><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 15:10:16'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-14 15:16:29'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 15:17:00'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:null><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 15:17:21'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 15:17:50'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:null><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 15:18:18'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 15:18:46'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:null><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 15:19:23'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-14 15:23:03'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 15:23:38'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:null><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 15:24:39'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 15:25:35'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:null><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 15:25:49'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 15:26:12'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:2><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 15:28:44'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 15:28:59'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:a><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 15:29:26'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 15:29:58'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:13><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 15:30:48'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 15:31:22'),('com.Ease.Servlet.ConnectionServlet',4,NULL,'<socketId:21><email:heniart.thomas@gmail.com>','Wrong email or password.','2016-12-14 15:33:08'),('com.Ease.Servlet.ConnectionServlet',4,NULL,'<socketId:1><email:heniart.thomas@gmail.com>','Wrong email or password.','2016-12-14 15:38:27'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:1><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 15:38:30'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 15:38:49'),('com.Ease.Servlet.ConnectionServlet',4,NULL,'<socketId:6><email:heniart.thomas@gmail.com>','Wrong email or password.','2016-12-14 15:38:55'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:6><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 15:38:59'),('com.Ease.Servlet.ConnectionWithCookies',2,NULL,'<socketId:null>','Wrong session id.','2016-12-14 15:42:29'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:3><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 15:44:14'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 15:44:24'),('com.Ease.Servlet.ConnectionServlet',4,NULL,'<socketId:10><email:heniart.thomas@gmail.com>','Wrong email or password.','2016-12-14 15:44:37'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:10><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 15:44:45'),('com.Ease.Servlet.ConnectionWithCookies',200,1,'<socketId:null>','Redirected to index.jsp','2016-12-14 16:50:18'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 16:50:52'),('com.Ease.Servlet.ConnectionServlet',4,NULL,'<socketId:9><email:heniart.thomas@gmail.com>','Wrong email or password.','2016-12-14 16:51:13'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:d><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 16:51:21'),('com.Ease.Servlet.App.AddClassicApp',200,1,'<websiteId:2><password:qsf><profileId:3><name:Facebook><login:dzqefrgdt>','96','2016-12-14 16:52:20'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 16:52:44'),('com.Ease.Servlet.ConnectionServlet',4,NULL,'<socketId:1b><email:heniart.thomas@gmail.com>','Wrong email or password.','2016-12-14 16:52:52'),('com.Ease.Servlet.ConnectionServlet',4,NULL,'<socketId:1><email:heniart.thomas@gmail.com>','Wrong email or password.','2016-12-14 16:56:45'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:3><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 16:56:50'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 16:57:24'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:a><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 16:57:31'),('com.Ease.Servlet.Logout',200,1,'','Logged out.','2016-12-14 16:57:42'),('com.Ease.Servlet.ConnectionServlet',4,NULL,'<socketId:16><email:heniart.thomas@gmail.com>','Wrong email or password.','2016-12-14 16:57:54'),('com.Ease.Servlet.ConnectionServlet',200,1,'<socketId:17><email:heniart.thomas@gmail.com>','Successfully connected.','2016-12-14 16:58:02');
/*!40000 ALTER TABLE `logs` ENABLE KEYS */;
UNLOCK TABLES;

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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `options`
--

LOCK TABLES `options` WRITE;
/*!40000 ALTER TABLE `options` DISABLE KEYS */;
INSERT INTO `options` VALUES (1,1,0),(2,1,0),(4,1,0);
/*!40000 ALTER TABLE `options` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `passwordLost`
--

DROP TABLE IF EXISTS `passwordLost`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `passwordLost` (
  `user_id` int(10) unsigned NOT NULL,
  `linkCode` varchar(255) DEFAULT NULL,
  UNIQUE KEY `user_id` (`user_id`),
  CONSTRAINT `passwordlost_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `passwordLost`
--

LOCK TABLES `passwordLost` WRITE;
/*!40000 ALTER TABLE `passwordLost` DISABLE KEYS */;
/*!40000 ALTER TABLE `passwordLost` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `profileInfo`
--

DROP TABLE IF EXISTS `profileInfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `profileInfo` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `color` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `profileInfo`
--

LOCK TABLES `profileInfo` WRITE;
/*!40000 ALTER TABLE `profileInfo` DISABLE KEYS */;
INSERT INTO `profileInfo` VALUES (1,'Side','#000000'),(2,'Perso','#000000'),(3,'Side','#000000'),(4,'Perso','#000000'),(5,'Side','#000000'),(6,'Perso','#000000'),(7,'Pouette','#000000'),(8,'Pouette','#000000'),(9,'Yolo','#000000'),(10,'Yolo2','#000000'),(15,'Pute','#373B60'),(16,'Pute','#373B60'),(17,'Pute','#373B60'),(18,'Pute','#373B60'),(19,'Pute','#373B60'),(20,'Pute','#373B60'),(21,'Pute','#373B60');
/*!40000 ALTER TABLE `profileInfo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `profilePermissions`
--

DROP TABLE IF EXISTS `profilePermissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `profilePermissions` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `group_id` int(10) unsigned NOT NULL,
  `permission` bit(20) NOT NULL DEFAULT b'11111111111111111111',
  PRIMARY KEY (`id`),
  KEY `group_id` (`group_id`),
  CONSTRAINT `profilepermissions_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `profilePermissions`
--

LOCK TABLES `profilePermissions` WRITE;
/*!40000 ALTER TABLE `profilePermissions` DISABLE KEYS */;
/*!40000 ALTER TABLE `profilePermissions` ENABLE KEYS */;
UNLOCK TABLES;

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
  `group_profile_id` int(10) unsigned DEFAULT NULL,
  `profile_info_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `group_profile_id` (`group_profile_id`),
  KEY `profile_info_id` (`profile_info_id`),
  CONSTRAINT `profiles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `profiles_ibfk_2` FOREIGN KEY (`group_profile_id`) REFERENCES `groupProfiles` (`id`),
  CONSTRAINT `profiles_ibfk_3` FOREIGN KEY (`profile_info_id`) REFERENCES `profileInfo` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `profiles`
--

LOCK TABLES `profiles` WRITE;
/*!40000 ALTER TABLE `profiles` DISABLE KEYS */;
INSERT INTO `profiles` VALUES (1,1,0,0,NULL,1),(2,1,4,0,NULL,2),(3,2,0,0,NULL,3),(4,2,1,0,NULL,4),(5,4,0,0,NULL,5),(6,4,1,0,NULL,6),(7,1,2,0,NULL,7),(8,1,1,0,NULL,8),(9,1,2,1,NULL,9),(10,1,3,0,NULL,10),(15,1,1,1,NULL,15),(16,1,3,1,NULL,16),(17,1,4,1,NULL,17),(18,1,1,2,NULL,18),(19,1,2,2,NULL,19),(20,1,3,2,NULL,20),(21,1,4,2,NULL,21);
/*!40000 ALTER TABLE `profiles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `removedUpdates`
--

DROP TABLE IF EXISTS `removedUpdates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `removedUpdates` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `website_id` int(10) unsigned NOT NULL,
  `login` varchar(255) NOT NULL,
  `loginWithWebsites_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `website_id` (`website_id`),
  KEY `loginWithWebsites_id` (`loginWithWebsites_id`),
  CONSTRAINT `removedupdates_ibfk_1` FOREIGN KEY (`website_id`) REFERENCES `websites` (`id`),
  CONSTRAINT `removedupdates_ibfk_2` FOREIGN KEY (`loginWithWebsites_id`) REFERENCES `loginWithWebsites` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `removedUpdates`
--

LOCK TABLES `removedUpdates` WRITE;
/*!40000 ALTER TABLE `removedUpdates` DISABLE KEYS */;
/*!40000 ALTER TABLE `removedUpdates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `requestedWebsites`
--

DROP TABLE IF EXISTS `requestedWebsites`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `requestedWebsites` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `site` text,
  `date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `requestedwebsites_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `requestedWebsites`
--

LOCK TABLES `requestedWebsites` WRITE;
/*!40000 ALTER TABLE `requestedWebsites` DISABLE KEYS */;
/*!40000 ALTER TABLE `requestedWebsites` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `savedSessions`
--

DROP TABLE IF EXISTS `savedSessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
  KEY `user_id` (`user_id`),
  CONSTRAINT `savedsessions_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=360 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `savedSessions`
--

LOCK TABLES `savedSessions` WRITE;
/*!40000 ALTER TABLE `savedSessions` DISABLE KEYS */;
INSERT INTO `savedSessions` VALUES (1,'MSP7G4u5B2obMbNZnyAbdtAOe2xw1/V97d3eP4TQ3dt3HL262Z4zh1LD','/eIqJ4zFSdi+VNspZvoNPQbrQrxRVMgZcnQv2sBfg8c=','WKZbPiZmi2wTEBvJ1RDgBHgXs4A=','IjG8KpfJUYIHtq+2iSQMWIPTt/gpp9HBtlHAueugOJI=','+qklQ9JoiNONbM5xqfWmRVay20k=',1,'2016-12-08 16:03:41'),(2,'GBHznvT8y1VBGvp7naPY2ojDP7Ml4VrDiREZi2/3ohFiDeq1MCPg0M6m','RDWryc1dpmMzeqzoVJaRfGcs97mXGKZM/iN9AEybBHA=','uWA6mILH0pFoUx6e7KqA8ucdRcQ=','l7EVO/3XhJfLl9QqVQSMm+tBqY0qt8B/Rtgz6/yupC8=','onEWE7sT4KqNhIBbKRCEwgOG7SI=',1,'2016-12-08 16:04:34'),(6,'Nhd0r/n8KFdvnZ7kAeP4mzLC1B/hpRRCodei/x9kODlqOn0Tk/K40mye','mE58+s/45eUCQWNVWsw3IsTZu3zDlCPCv6lEOq0zS/s=','WVCl6FPsv76Gm6r1Ht0lprYSfKY=','ynPG5QQzPUeEcL83MZchHvpUVU320zfAGTnE7qh3FtE=','38pOs5WiLbt640exdH1FLYW8zvk=',1,'2016-12-08 16:11:29'),(7,'trxXWfejSAXBUrpitg//CRsX1Qe02RWZv7rA1ngqJOQq4SdlD40e6mA3','TEGJjp0eL4g8qbStDwnGI2VN3tcuCcSfZ08u6SkxsuM=','zZKN+b+Vigg/eo1UYhUM7gGd7wc=','6YG/y913LnteT1PoqgZtJDTHlaHxv75hIk3s60dt4t4=','HwOUQzuiplPhMUmFSJPPsxEeZ9E=',1,'2016-12-08 16:11:45'),(10,'nC+2fEW0jGnM6zZdJZijZEI2LQkxSuT2OtVDcUF0iHfugTv81XzFgBXe','rc64ScZpYwbpv9XJ7jJXfDhBsC9ZN2vuZcB482/EZ/4=','05FNRGtBJku4d8WAIm9HmGqL+GU=','EzVoVpWE0qAhxjLPo6ZU+K7Ro+sLvCEtqa8oD5DNNKY=','ZVGVpoktlxTJo4ys/Rohp9hvDIM=',1,'2016-12-08 16:20:12'),(12,'VJiJDV13ASZze9dMDad3vY5PPBQTmrVFrxTsAJaKTzcIOZ+TjPn5gwR3','ZF8EvE6+Mv+Jkk+9lAtBavAJzFXwxIsNOhpiC2wDCCU=','EeiAY1dAFziDKoNW5WNYOoielGA=','B8EyObNz4T6z1PUUK96hlS/kjwMkNnZ02BNeOD4TaGw=','wwC8YgNkigERRmyxu+4QDq0VHak=',1,'2016-12-08 16:21:24'),(16,'L1GMbg+PGNYB08vEkspqqaNjcUykrCMYBKDvu51FjKDUbuK6FaFn8+mY','k5NXfScH5l3YI5qFSPUycYMdqIe/eZcEOMBNS9nRwF8=','d5t7THX8F6JtSCKVgkjY5K1x72U=','vY4tp+mGD6DQVfJ9knaxVEN1e9aF/s3d+5vqTVkDkPI=','UTwLAcZ3f37CSEMGa9+upOzMmsw=',1,'2016-12-08 16:25:54'),(19,'U2m738zQVxCJAzs1zxT0ZjGSyGCTMLRRJFykwE7Kqq55Ismpuy6v+bLZ','Wvcypaa5XLSTHRgtUGelDHXKVyJkPaqIZorShzccCl8=','rLchrzFe/Qbns+cf7dOtldtP/M0=','C0JR1eW+scxJ1uW8GwfHXvxTTDKsAU0C2+1yGHeOGKY=','8w2Lq1Ta3n0euROTgDYp8s3E0v8=',1,'2016-12-08 16:31:52'),(22,'HVuNRmlTR4r5BK8QQ9bhssSmzX6N4rgbt92AdcWU0RFATcqYdSanYdNt','EQXA3Bj8YfDm9/UPsDR2f4rovvjgkravpCVuzIHCxoQ=','pxgShfR2Vv5NYE938zjcheAubok=','2ePbDL3wgVGRRpDLasYiE/Qd88HyK7ch1D3zXIm7u7o=','AsNeXPVJAxoTt5Zgo6BMPzJUDOA=',1,'2016-12-08 16:38:22'),(25,'+Mu5geN6nV9Bf+hpZ5gToEtnrVz2wXd7zVbobErcPLRt5EZO6OPslNWO','vDMJ/+l5EvX3Czh2NcfGVYgkk/Nm77K9t/wk7Q8sZts=','rPq++nttwyF/4Otsr0kgO3Lq13M=','CJktqyFmLQgDERWAV0FcN79yRLLXwPKKRVjKnIVeFgw=','kkHKn5jL8CsX+IG9efeXP4BozPU=',1,'2016-12-08 16:40:54'),(27,'lskPatHjCHaZ/mWKsxsp2UIe2fXAcfENaJXvNmQ4MmzoEdK4VIALtkWG','c0KqR+ORDvJGy6SGeb0WWSMSxIGw+yzf9IR3HkKg95M=','VXyDSIXIMztGM5GNwEb21qL/U6Y=','8+4DHY7wBeMXY8VokOeczMWc1D5aUg4uWmZ284UVHFs=','dF904sAknfcTaj5aEUZBp2/KI2w=',1,'2016-12-08 17:58:21'),(28,'YmaAqYK7IUrhUOO/9h/NUWSK122k3BcMSBhbpmkQaaoaeo7GTQ7208CU','QmRO0os9o+H+Y7n9SDplvTCQv0i5mPPabm82rXqQPco=','3C8uUloHi0InGZ176trIiIi2ius=','jA9D/8T/enZzOXzVkgppAha96Ilc9Xeny7hF7bwqhSM=','uoWEgxeggOkfo8G2C0KXTk1nu6g=',1,'2016-12-11 14:58:45'),(29,'RWQHr6+afcPorxMtxL7g5l+xj/7dK/yQhqDoPp+W1UeC/yisVDMJk5BK','9J/fhTKTOLYvlrK+VgWijJsxCK2UzfCR29YCd0qWDco=','+R1VWA1pnLUFXLfSvY3Xo3xi4/E=','nIPDq8k+hDvgrfFLAFPlfbt8JBFQn6oipO95F0KIlJo=','JOT4WALjni3YLZvs0gfXb91SSLA=',1,'2016-12-11 15:05:33'),(32,'Si/hgEzbLx9aADfK/5DZMMcXosYSIyMmJiTiRGBdtpbcaUw1N8Ic5142','zcYm9izUehfBzybJtgSVbS/d7LORsMTCk3eZTOFF6W0=','hmbdR33OZZBK/JeqRF9w3mZtKN8=','OQ0ARgcm1IdSsNkzarngtvU12mqk/Uh+lTEuFPv3Xt0=','SUIafqG1X8coWPUV3jYQLqkfk6k=',1,'2016-12-11 15:12:50'),(35,'Z21fg4+BsAsnSWRWz3DF15NeAlrvQEDNSTjD0X02UPXlxMgFqhSyQk9E','OV5XNIdCiuhoP4AMOc7N2jbNAj6F5KfCXYxcMRSXK6s=','maxnrflABxycKO5AnoNQZkW1RyY=','6BTpM1/m88WrNlBNpwmBiwTK6yvFxhrRSiivlJ/LUiQ=','GVOaWH05sS4wo6lh7J2V1Y5Y1rA=',1,'2016-12-11 15:17:09'),(38,'OeoIaRcNEHGZhqxfdjLLEjVaFlSIJlxfxfzLQ3jvyL+nu7VqyurjdpVg','qJzTLCYLvM9JnkcgiqSIvD7rYxdojq0VO0AWE5rtJyU=','hkIZw2D5oKDGFMWWbUsOUV/OgFU=','izpgMWNfJCP//9THr5REVTAmimBYEyUBtLVxw8tB99I=','Qb+uw+XHCVpKaRiyS+Zw6QuLtMc=',1,'2016-12-11 19:12:00'),(41,'KJj9mCuEERn9XdO25prwjYdqJBEYy4WfDUqFFAmu6Si9ss+JUqHIwI/B','QnS0QVr0RLbezDsMzxgzITleHV8ggZG1VwiKxl/Ghew=','Bt1P+towy2W+2CPR67BmWo71t5Y=','SeqvsO+tRI0n4HlUE7+sGixLzu6OFiB3BXDy3K+bTbg=','VNlPlnSct4cZ+m7VMwuMKHryZZo=',1,'2016-12-11 19:13:12'),(44,'8ZMDeFbAiauEyxiG8uUWVFh+mrrEInuQYMiTcklOMXjxP/E19lyVsKSP','PaQMugkd/5/Z/39yT2Gv3z7D7n7klmdn3HM3VnQ6A6s=','Fqkw6rzE2MNFwDVFrBAvov+i34I=','/0VtJvn0EuEZCA/mL2oKiOM9CQQUf+fx3g8JT0JVHug=','CET0S7pKGizbdFyu0+2qa3Xxq34=',1,'2016-12-11 19:14:59'),(47,'tuxM2bbLGHQVTIbJfl3uxXi3ZuLlN3XI/xJAlQtCuqhpg8eUZI381FhX','TC2YswvM4C6UGurpHpL6cGC5MfWl2KujIp+gO5M2VXA=','6s1/C9Sa87JeDQHejnxgWJi6uXM=','JNQzt+SPUiJi1qX6SBK3MoRVoCSiqMYI4uiRQo89ZRs=','x4vK4indkJBRuZJen181oF+ojJY=',1,'2016-12-11 19:17:35'),(51,'ObWe3FPgpD6I0FmicVsMQFihBaVeuIGTMoOJj61EHErSDuzYRvtN1NPH','orvMmI/79oZitd5/eOCm+OsIt1GHC/t4V5lL04jJq5Y=','tBZcRcKyR6JPSCb7MjYND/JPaGQ=','pdBeynYYJu0uaEGLT7QqSTlGBsgUCUaFgL6pYib8Jw0=','CxsSD9U+4vo8nVnSPKLjWPdseeg=',1,'2016-12-11 19:21:18'),(54,'cOYk4HCXd0MOjUyn940zc4UVLlbx9UhGGvE/8xP+AqKsgD9qSKbHcKBt','Ag2u/KN9OftoigiiWBw/JxSCOe+0Be5Ywo9WWWF/lGA=','vhXuf03/pXMdz/0hFCMv5/nJqmg=','tOUQWfB0iD91ErgJyHDsuK/CnnFfPn5dGVD87d0QJhs=','PdpKOitRsNl8TrVMuVlhv74saJc=',1,'2016-12-11 19:23:20'),(57,'j0oJoznCUKNLsuVlgXd4ycAaw6luYqaCtWAc0p4v3HYE7ECxkFE8f3p1','23q2mAh4fX/7o4JVpc3j4YebyIK6Wl/R/2tNu+WN6UA=','f83uHU9iN/6Q46Ttc8OaBVxEt8s=','VKu1p4OpXyo+5DvPvIrMG6Lb9B8MNt7JbObK54huTfY=','mIENvBMJx+leGAdmVetw//V71VU=',1,'2016-12-11 19:34:49'),(60,'yIq2mblEJXVoS7So2V+sMmakyc6nyIj5xShNcCjv0HcOBoQkpjyCJH08','cWFtbFOr8z9fgSYWw7qnRjsbG866qQn2BYm6I3ifLBQ=','732ceBE98Y44Zs5zvi9pd4hrfXI=','b4+joF1oPr4US304RJ3gk20V511Yx9H0ah1P+ERuezo=','BUtt7JXZ+MITjB7IBwohGqB6LTg=',1,'2016-12-11 19:37:39'),(62,'Q6wdDpn/2WJ49QaarBXeRwI1YGoKzjUS2wucpcVtVT+ISKJFMsF9kpol','jV2HZ2o7ykHhGIfeB9iLjuRryAA2MKXcoPg9DiStojU=','kQ+4KpB5P28W5dglLO5JbHXVVkA=','uVjj0mZSACjz7CpShxNUoFRNAC1VTobMo+XHARNd1jc=','XCUQruugE3MqzZ/sA+LmCJKKWuo=',1,'2016-12-11 19:54:16'),(63,'xll+FcUi3K7+frJW8uvVevdhoQblwfLKwfYiiorCqF9bCsrdkdxnMPce','6ORO20xhgjDl04DSQYyxDlUWsY+ORDj+hB8GBwpg0fk=','IMV5hzVwoZCGW6UVSMb9sbRItVs=','xyXnHP0FePf2N80/rdamRf9+K4CFM/TLZnugov3PeWY=','oQ5/CUIEpW7loaCzpQY3t1+dATM=',1,'2016-12-11 19:54:27'),(66,'7QQzv7Vfi2o8V2jjFx5nTBy0hSi0YyDTK4fq19QKH+lxalzKYmc/ypC4','K6UAEhbaMG1U+V0eOGmut0L5NJKIgHAgeLMXTsn5wz4=','MGV0ruqVOgBK+J6WEkGAxpnC+zc=','Qwp5+dW+/4sjLhQ1bgMNFQdj69d0XK0MOl2OCC2DIAA=','JIKds5GZP60b5XiGC7eOxjPPK0U=',1,'2016-12-12 09:47:58'),(69,'m3hsFqeo2+hS6rZcTkeg6S+DgIbYIbwYptn3M5SqcB62SVCR4/aLLfZA','ugy3lik7OJ5ACerDStBCF6M0MSYhHQ+79P3M2uLtN0I=','lqGoTRlB/mDDGLjn2L48eihmBvk=','AJukTayL0tdU5fLUXB2PjoVvEX1mZI1CmgyK+HUxIRM=','DRwW8tANS8vyBisqUnG3kZbcr7A=',1,'2016-12-12 09:55:57'),(72,'dZPTLUhuRuTJvpDrELddYgxj553IDiblaiYj6N0SQL2XutkClBjgfNSE','wv5KK8Kt92BVAI8OFroXgsHns8aQl1SIGOBrp4bRAGo=','1opxvXT4dlXE/tYfKrPVCRdWtrk=','JszVjNcelBdDJCUcjxe4dFR1UAMf/3v7kdRLXFoS3oY=','achuXYSbkXiWj7hiz7PJCXGXWBc=',1,'2016-12-12 10:43:55'),(75,'29QvheNMUVCmQIeCTV9o13NXKgjpRgroGh9pJWOQOeQADVR8uRjenHRd','cfcjX42rd5//4n0tv++06m8/Np0a+3uesDpoyvWTKqM=','oQFBU6Oxm/Wf90KnHGTscsa9yDI=','gE4jmR4jv1nIdRV8qJ6OmL/6fGFFlo7QCNgN2+x97pE=','Bk51sTsXHAT0Cr2H/EpE1g+qavk=',1,'2016-12-12 10:45:52'),(78,'tRnPf3P/t2fIiGixvy1OFjgqDXkPzJNXCANP4OwnQSMJ1+sS8cTRDXpB','AYT8MV5mIw8cSMN2hGzv+PAsh3fuFOMtABzL6aV0/TQ=','evHyX2HhVR5xo8vl/UmGPW+Z+x8=','+jWMXkMbdCAgj+sJciOzuSqpO7BwrU7kQBe9xDPvEv0=','oFUWvMvG1+9Z3aGsiJEyAZWlzQo=',1,'2016-12-12 11:18:20'),(81,'0s8tp2/XlBoyP1MSDOMbWN46mmNMzIneLn40y0chU/WdO2RGGM64TLtf','PoioS49RTWaWEB7O3KnR53XnFv4AQeLR6CqPZRP09sk=','xQbs0OxtAWMLR4EaRUlYgmV0Zj0=','4bvvEDPARI/y00s3RgwdcvvxOJXVmVwu5N5WvvQpRws=','LvWCfDzaYdCKLrC7REWbfG9Oagg=',1,'2016-12-12 11:51:37'),(84,'TT9oulyFamyl2T7G9sdTH9f5SsN+cPu8btC3Mys7uLLzjQUXmdDMTL4/','JgaUu9Po/Uo0D+8odY21SeR6YwXFsZL38bsZYPfgRxI=','u/nhEJnWRjcm0fcSmAjq1RP801w=','0unh1C7JmzMROxdYWDa/pSWqHrwMddWGxRFLQBs0QPE=','jImhhKLVt7pgopIxou7tO4DlOKA=',1,'2016-12-12 11:52:39'),(87,'EkN3eKx8eWC9/6BF/NoKgsZRyjp73drog60zfukb3efvY+7lmgRiydXC','R7EOzy700gT2KWB7WSOX/AIIzYFGnJZUyJ+mQyDKe6w=','XIADRrwqwLXY7wCmVmlo4CRTI5U=','soYtMJIBe2s8pGvs7mR3tFUefohbhxaL54DVpd4/H4I=','4Fbmd2NfSHUJicWI9b0LGap03So=',1,'2016-12-12 11:53:49'),(90,'g0luhKCIEan7ltgBooXShf79OiCBwx8ktp7W/ozRp2XVQNkA8p/P0Swm','3vWJKI74/idFDA9riuAimBVS3QdAXFejsHxPMN7Hu+A=','KpQQ53qFohOokXdzchvzT7HUF/A=','bakgQ6OoJiUGw7NsdZAZ4eUtG9iFT3yKGC+7qMUvaI8=','x9mPfgiJQwvU+7PZJtWmu8wHJ/s=',1,'2016-12-12 11:54:35'),(91,'lWmXwJBGQQ8MptYNQfEiTZSuQji8/owTM+t2hsAi3aDZvSHKspk17SdM','5/BDpwW/UjQwn28YwaFgq+WyPcl2JOaUcpkvirdVpSQ=','cZzy8rKB9Vyi8F2MVQqthuxxUCY=','A9rIWFNHArsGdCSihzdwF8Iu0DKlFZaMyz4KuvfJHK8=','FrNQLi8repG9zZ8nN1lwHdHQrrw=',1,'2016-12-12 11:54:55'),(94,'Ye+7msY9sMRKNUS/+rsMOYLJwehjxbAPwEE82GobBy2oKmqTLDdDT0lb','w/VAM9XAFRU38mV2UVID/I1P7q+GSxM9cLbDebZLIaU=','d2AymVarMMnujYTGfgQDaYqiYWo=','7epEFQ+xcIVb0fDisChRayptsHPzhXAgVjbUIK0lV1s=','0x0dbspb8YBfNHOQGLyylQYBK4k=',1,'2016-12-12 11:57:05'),(97,'ByoT7WOSLp5ou7WT2poUVNnsK7b6HZTTiCifSjCbjWomsd6JTQVTwGyV','gw//WmKdCvGRiztV2mPYW+XgC1RJnLGJGjv4V8ZlmfQ=','nQJUOpdkf/iHYzDcyE2Tovspxq0=','apw4dLZcoHAI7V86Abo8uPMeDoeIY7SYj3wcyxRZkWI=','RhBURAXheibOdtxrlSiduZh5bHg=',1,'2016-12-12 11:59:12'),(100,'4AQqVyYbZWQcvR6yCfis+5bwVFsR8osVQNHEhhHBOL33ufEzCgnB3hdr','XIhTGoixyvEyB3izdBFisUtdESntF2xwu80D0PcKNCc=','8t/MmcO7MXyIQ7zW6UY9kjyumo8=','3+w5OJHb6kkr+5aS+XZBFOqFvLy0pZlwxatjdYamcBU=','i+HT3xfueJgz0RVjTh6CHkOz4uk=',1,'2016-12-12 12:01:02'),(104,'tAoudm7BgegOxETqj9Hod2jUzWFiitDqRlHXaGTo9cGp5po1AKgnUV5C','Kw8XmwhHX7dmEylgdW92XDICxqCtiCEBoEfzs3cpb78=','PDuksZ2WiLFgeyR8z3652Fy63Lo=','rBbNvfJ7Kzf//b2HtrC90mWKzS3+zRG3ftm1ZAOZL0U=','vEcJrsIy9CVKQuUxJSUBNmLak8w=',1,'2016-12-12 12:02:25'),(106,'cEDZYgr58vKOZthHIut4Rin6WmHFC6oB+Kv37hweL4LN0aBNv67iYssB','XJ+n/7YlcZPhL1N70Pf0LJTXtaThLdOwrCR4htf878U=','RX+XN/86bqijtLE1NC5m6QmI00M=','B0MuI5l+B8uRiuaJRbgNHEFCDiQvJ10otHHDxfqqDDU=','cjt/ZMO/WImDh0qe3wrMApmP2K0=',1,'2016-12-12 12:32:54'),(110,'Yi2jHaxK1/A2H0oEmnytFvY/0ULS7ZzxG45juXvy6Cp6hSWf9KwWWIas','ZiIEXOpKNRSiPjhFNqHMj87KQstPrmnO0LnHuCAVw8I=','u7tkeAS6wO8yYyg3NRL5zGy+LHE=','lq4b7ewnuJtJ6JBNSTqFs/rAWy5gGk638fgKjY/BbL4=','iQ1Fui7AkZxUd5ZhK9mjsrCfmDI=',1,'2016-12-12 12:35:47'),(113,'4HObQoBRDSCdoLxfmvN8KsSx1669h2f5pGQZ35b/QiiHbXrjDRX04OrY','u/aobRYdhIFDRGui2uFQ3WJ6iA2zjy0x+PBUk5SQKnc=','m6axEBUjSwtWJaxl5dNEdk+z7oA=','x3HyfcSsEDPeliM/jcC8MvSbymlDReDZPi1xWZPGBCE=','lcG20QPD2mVxNtEwPSCPebdqtBM=',1,'2016-12-12 14:09:34'),(116,'i9lgXhtbtchsN08RtSiv8CNvemgO9FA27vhiPqlIsjdHlqto4n6VhKb7','+knbDq7iYfRcHhW7/K6MTyabb/Dnqm+e40y2ftGGWek=','S3X18L3TsBiYwMbhjt5HIbkLJCY=','NpddofeQWtp5IARxJmPwaUJuLAcqRy9WCa7i6SWSrUE=','vj+dZuToywxEmiRXAd3SkqZuyd4=',1,'2016-12-12 14:12:07'),(117,'cD9CBGvSiArtkBLKS8sGo5fJjt6+PWV7oKeCZTKOdAl3mbDCipBhsbIN','Y+ONbu8YD+XVqsEcR9Md/a9gMrL4bwaiXRXAxFcM37M=','HfgU/ojuLCde4tRIZ890gHjWEpg=','RTyCA/PFBe5U5456ld6jUXo6vpi+KA0Mpncw1+yHRqw=','tU2DCoHpuBdR5QXYYGd58EDPSWc=',1,'2016-12-12 14:12:45'),(120,'5TMRmBTGvfbc68A9k/Lme3woZIWm5EoC2E1zw2rfjv3/dicNSRHQ4iRp','2LzjATcLqsuxcfampj7Zy/QSKrjlzNUNXAgVZuzyO7k=','/Ej+CNelA9jcWWsi+xGHHwv/nmQ=','likPweTuLOW6PN90dWWOJulM4DN7O1Tsehm4vjD0hzk=','gzwbGzIvHfmu4y3NVvjHqto1/5Y=',1,'2016-12-12 14:17:31'),(121,'BpzAbg8zqDdE2tKbivzfnFySBGQOhfbgtJ54EkBlUbBW8mUMZRcm+Fo8','HkqRR9hk2G6wh5qvH1sf7rL/CEXj/aUnMo0Hjir3KWA=','MtTmjxtQsLlhCk76kpAIDDFYwOg=','DVE1ep7S4pkbvj+j2IIq3CBLZS0eGkTl6UrAVgiVuGg=','gBEMIhGKCFrI/pMCtxnywwaOaqM=',1,'2016-12-12 14:17:50'),(125,'kmxlTZ2RFbYX96NDXzUqRxkG5BCNFnuqBJTGUYqZPM4/1QSgrjIsg4YH','xzGmhEOhKUNhWe3Oea5dFsAwk6x0L6AC2F56wEwHztM=','njfqSHnvwP8hpvHxQfGNJLhnoJM=','HIIpGG3svIy2OGYNFLWCON1fqk6H8f8/ukKY0ZksodM=','g9kHLkyrFBYy0ZQmAPkOF+3eEzs=',1,'2016-12-12 14:30:10'),(127,'/phS+Q/voNvVzOVaw4Xni8ZVLV4i2SpYkIkXN5MmOemigEGbEpn6EEg2','qbFmbPQHsEfnqnNcRzN9meUBxYAh9DrrsONGgXGP6CA=','CnulRQnJKJQsyXIsWHjTyINajpA=','hjuE3G+c9yrct5pQF2ECM5qkbUChckw527pWq9HKMoI=','c2HuZ3pleRurIaxABl8qURt+wfI=',1,'2016-12-12 14:35:21'),(130,'8sPkGH/zFio1pKXJM3iugdHqHsO2MzFT6o8gvCEN3rdIsDoKvBZTOJvf','LYvScu1YaPXR9ix0+xnRZNFJtTLXP0tfwwpD6+F1Sa4=','4Yj8fWDAuMQx5mQeaPObPAeSRR4=','o/9rlrjCzbmFSyfpyvXEpDv9HYLmPtcmzrbeZqhL4Lw=','WkEVBq/9An131uUC8ambnB6rsSM=',1,'2016-12-12 14:40:31'),(133,'+ez/82y2B66CU4YLfQgAP+80+O0cCnzCiJcg/BSEMyORkGoegI5zser8','69vzHyTiJ4GtXk+0NrLX6yDbWQc/twyK+TUeNkaflCE=','tpVZAi+9oORiuzeTAby+L7HeUSw=','RsqXM472AR6oD3JGZdf1Sop4KdzMzDHSPImluQQX1Lg=','9Xga3gJ5l4jP8ozaOfTPrCpS6LY=',1,'2016-12-12 14:46:12'),(136,'S4uzWPgA3qjtrQGUBtMQ7BCz+8Ac002gyZFOYGbw0M80QUugpWw/Uh9F','S/GhXyNn3Vl8Ak+4I+70VJIke9vKvDhiV/k/lG3OEGs=','KZjp3TJ7HyyfOlK5kVcoDeWA1V0=','T2ZC33SoTDAi5nCSiHsylUZQOsn+gPi9HDGtY3oguB0=','gP5hiaEN/3vJFPiTMbN0m6PCNbM=',1,'2016-12-12 14:47:29'),(139,'uRUNRrwHHkAOjZQGF0n5b3r2T81/8KOFsd9p8MePFFylRPoVXeKWz2Sd','/WBKBxVxShlErhlAW0QPfcQeikIP5Yh+NVK7mEOlGEM=','egi1ZCZcENAAkB11czG0qDO+x58=','m2qC1VPqxUduwkwX0BNjxl3pDjBKgHIwFAv0sBriD58=','5IBWgSv+3NbrqJIuptW+hAUG0QE=',1,'2016-12-12 14:48:18'),(142,'no0fRoUzTXykw2oojPj708GWoZFAz+Bvn/BFojNXHKjzvyUHU+17S0TZ','eb5h4O+qmxnxXvLl87uM1JdI9GNFahj4Yl9uTBaxf4o=','h3A3W0aOkNsnQUe/+m/WfQSiJRQ=','spRiSpMf9T9EvxFdRrNpwwkLyzoJQMFS5Hshr73QAXA=','54VHH+5bo1k0MfPiVmT7DwD/CZg=',1,'2016-12-12 15:17:45'),(146,'oUOtJFpfYp1a/Bv6gpqAC67IhLGtT/rb7C0ynRCzgv+KIEZudnFzH4aJ','4Pfo8OjSIm42RAdWqL2yfVZEk4TMm55go8Gc7E/hwc4=','OAbNF3j17eE3xzyKot/XZPzzuVw=','sVm5+PGAj9r+Wurl8gQM1NFzuJO7gsMDqqnEUd/UF/A=','C8v14FWv+Fz54CStq7sKZuK4LUc=',1,'2016-12-12 16:52:35'),(148,'7Nv17ndx45I/5KfEQUMIEHrOxOYNqU4h2rlUX64o1LtlxoFJBGkyuryy','yE8DLChLmmyfaMhMlioflsFZ6/HNDlaR8b2wEn7A4JQ=','Vbz5zOm//1+NL2IAxTMLD9vj4uk=','D0BT0v5XpK6znWoQu5dPxxFxBwXs678CqcoXrtYhEIc=','lvOnwsCsFY+gwKNOpl8NwCQByGY=',1,'2016-12-12 17:13:23'),(151,'jvYl+1Uq/cyUuZ2PPHc8BYGceJMFmfBBMLWsHhlv8KxslofuEaE2c/ve','w1OOE/hJM2OdJA+OG7i7vxZo1+jwKQMZ+e7fChHMuaU=','wsYKQPKHGlLlpmMk8kN5CTIH/cY=','kcrQ3WZR4j53Ogt9g4zHJQnrbq5aw+1zqrBiWB5vGGc=','cgI62ezzt4wkZSSkH0G6H+wW4zM=',1,'2016-12-12 17:15:23'),(153,'5Ks3U2Wfm+FBu5IO1xU+O9/LXyT3ALqUdoRZ8h/ILmsa+hq/A1QsaQni','4c9CAMaoP6QMMjVgggkCOV/3vqcllyMUCn7C340R5Zs=','EmeEIS7ulbDYV3kbkkyU3TO6WqA=','PutD+0pBCjNz4dcLzMGbWBPaGajhKv/3jQ9Tk8RE5EA=','s9yXdTxD+Y3Ch9lMzyzlquOOlBQ=',1,'2016-12-12 17:22:41'),(157,'dxbYfvzeGssZJpPc0DzTm6IgS0mTLP/TfHd00/sRvnqRZPOvpSARBSkH','naapxspNAfs2mVFPtNbitzo6ufpnaZP1bqLljYR09U8=','oZ/kPMkUb9et3CQa6dgAUQKWC9U=','0j2i075LZmzWDtmURKETQ+vSM5crf9XUH6HawGxWxjY=','TUiKz9oi8D2VgHnfZ/3MjPyGY9Q=',1,'2016-12-12 17:24:01'),(159,'ruE8LvaPqzKuZWjHnET8vIN8r2+AF4MDOXKA7kB9jnrIJ68J5wKht5U3','2xp6U34gnHqcYWzg/gVtxtinCWGYxZyv35EF31Usqi0=','PKOaOVW13IoZ7gMGyh3T2XWUw8Q=','H87gmI5rEoFFXKKNeUKuUB/NFaYgmQ3DIXtQX6pOcOU=','k9K4sq3hisJ9E04BueC43og9BRI=',1,'2016-12-12 17:24:26'),(160,'HfgjiiJwWXl95Y/SwIxk45JDoa4VtWxyBpp97AXKQ4v9JP//EOTlS9Q7','ONRNxgKdfdn+K4CQ1Sbxsji+IeNlyt4ZWCQrNHfthp4=','Dx0i3qRHZWY3aDDo8RN1WzxFIr4=','TFhl2ixxxDmP/j2VBprVSuuFunHg/4QTw4rJhEsDwj0=','37kAMVzPdG+TED6s8uUFiJ2NiSQ=',1,'2016-12-12 17:24:40'),(164,'2Fs0pzNjK2O8y/ZI8wzkQACkKGN2vtOf7zwkN+svpxP7V4E195RzLdOK','fBFh3rQufRirKMH7zvtAnKPmmaT/c6aRm9JwI5UAL0g=','gprp63IBn9mQ8yMbHsQseODaaQE=','KcO4d//JpcYrtUOaOUGrIQdylzDZ9XJINz6CruT4iHg=','3TIWsyhnircVBghPeDCA8lUTcXc=',1,'2016-12-12 17:51:18'),(166,'gTij0GN+SKGlUSCkcowzQqq1HGtYe6e3+O3mmTO4R02BTJTuK6QYRuaz','9gCPDSb1FwFnu7ceU6BHpYcu2g0DyRz7ytTtmEWhoJE=','MxqerT0104lYXNKVksW5IX2r1Fk=','bYj61JD9y/EIuyGeAv1yqvgUeOGlf42unDUxFRepAms=','FLXrfx1DM24QF5PBxGpmktXVovM=',1,'2016-12-12 17:56:53'),(169,'/mboL9VbMOegtq1olpbVIfXDcXh0ZV1B8VjG+lHXTYIXUXLjp2s+MMpa','7fT40kRmWO1w1nspyUNcWQkoNDKRBoDbAszdknnjSX8=','4fyAqKFvezvKGHxUsg+YAAU6LeU=','SHTnt84g+T0Gll5qvlbRY7yNpHMDhM1XEphUeYJk4KM=','xx7fkZ2KRzQkLWdfC+hqKGL5ksg=',1,'2016-12-12 17:57:14'),(172,'92D7xWo573T09c1OaG6JQr/yzt4v85aPKcs+rMXuc3v35ooMMmz03XVJ','dGfAh9Y++y0RFHsBe48jUTAhYppi16s9wjFqHKlyX88=','yIaqGxlCKUhKWCuakJnZsDYau20=','KgkfnOehfkG73y90wKu2IjS1M4EW09cyElKnBIeD2LY=','MUJMOhMnhZsURgfBirnM1tpsnB0=',1,'2016-12-12 18:03:13'),(175,'QmDFkXgX6/PGdYXmYzW9gnrUV6DUN6fdAjDNpLLRPMx2OzidD/xGZ7A8','y2VUGngIVLXPIc28nqFdRtxld72/VwcX4yKay8+76Jo=','HZ9kztVhV9NTizuY7hG8/7OinaA=','q3D07eQxeXOvEf7FxyH0QAW1Ukv2sX2AvjzClIIBF4w=','B7KJQC6lJp/DyZ+po81ET93ovk8=',1,'2016-12-12 18:14:01'),(177,'MqzKlBsAR5fe1ms+N/+mrUQ2+pTjShQCX4LtCgR59Ymrldla+4FnKB+U','c9KavNAkOc8623DF8Q87juUwG7+bwVOjDzxgGorNF4I=','JZb2Pee+7yoh1DS70OksiQSh9qU=','A6ur3wsX6412KuVP2ueLBzSwuOt6weZECzw54avnA7c=','xzcvMxTQMvMercAb4+IKq55WZwA=',1,'2016-12-12 18:14:07'),(178,'YzAFlkBr4ddxPR+CvTXEgb92IFDSi+ZgpicPK3a6/7D6Kxp0jMvH2D+o','b7JIL0cV7s9jGuxi43bLI6umJH0AAp8I79eF2CY1WAw=','QvHvZ2xri7zx8N1HGVDT4yr8WfE=','OPkEQI9gSpxv5tpQElT7RCYLLkNpsmkZZK9ahJLdgQg=','K/KnU7Le01w0LUY9t8Ht043KHAQ=',1,'2016-12-12 18:14:16'),(188,'iQaRCXrETlUGHbZblP+HN5iffFHu7IYO/Dmd8Sn5ugLJiFi0MYmN1RSg','9BlNYfTgRJqkPQHg0hggFdcLb4ymwwhiBgrxHWJ9NsI=','MjuZp+Yt7gFf22gWbfwNFhTo4tc=','jCmdoVa01HCZPLxrP146eUzbkuBrGMEDlglU/4w+ePI=','b/ALeNfGJBXU0oi+t9ZaN/ESp18=',1,'2016-12-13 10:19:32'),(193,'e94gfdNVudxGE0fAqGssibI8a3dHqgeELV3GmExSZZBGWulbgpYAIgtx','CHblnIkw/MdMaW8imR93e/5AEE+6IQYn2njRQK3pJj4=','zKCoJ7WGC3wirc6he04AFq26yHU=','FScjc8Uf9knFUFd/HEi8ugRaiD1nkD4XDbsCSKfccio=','obI1UORXFBqtfvXoa85/SIP13+g=',1,'2016-12-13 12:54:53'),(205,'Lov8PqlcpOYNNB4PUYrGU1NStw4rhM/QFktsQFjyOLRIdO498tROgxdb','518exxp1eG7HpiLkFAS0kypvJ3MPUBHqAyOrxhsKBdk=','TWBZOgxqlsJ8M9NeuNIdIukBR9w=','ZkUXFSGq/AazwXPkbioBQhH8JUyP4KX0mMB9xIX+pDQ=','TMP4bILzKhbBtqMblCGFVm8qNzo=',1,'2016-12-13 14:44:17'),(261,'zhbNZB0QykChJyDhCXIHeOTckI8pwC/A5NhBD0TFllfehL2DDRjrHRTS','hKsWMQLmQYtuJbBg4q3GbLPPoAxgVvNeOUAVnInjRt4=','Usymav4bkEd+CSh5V5dZkjDMkDs=','2fladAjygtT80gB76sqJU6/kvJVv4FJJkozgFgVuK/8=','UEuSSlbfVeJDHQAA+myDPLAFX6I=',1,'2016-12-13 23:16:23'),(262,'sZhyTFwq6ZrgxPaI+D4+vcMpC+FKDkL6Li7WUfSH8mJiFJYmUufT6qK6','KZX9vVzmLLXnR3XEgdGqvOPmxFSXyim213wrV4OyDfg=','D9BHhpUVYSLI+YbmZpba4Hx4hQg=','2l30095Xv91eIW6ta9kyRcdqSv+Qw5nGQFX9YrpBIa8=','DDpEtQhCqFEIxmgJ5ZCM648j1Fs=',1,'2016-12-13 23:16:43'),(263,'fJobpVCrhFUFMUZRou1o8lO/QfzuhYb7/5/zKgbQ2xkIr5EoyVP1e4vg','38O9uRRKHFh7lDpdgN0w1BFufq3voqA0Iv5tsL32QJg=','y+DPl8uRgELQ1hV3GyZPx6xA/pg=','WqLwWcr2OCj99CpD0RrpgrhqxtawPetT9Fh9krr7STc=','469I4TaJqEJwvCAhoMojN3yhrIs=',1,'2016-12-13 23:17:47'),(264,'YnapI+c4ppNcDB0f5Hz0AIwI/U+HSDEQNETXYDaYQ0bYMkph4yGWEiLe','/9Tp4kWfp4qE3Gpdvc+BmB/hfiMDar9SC+Nuer7zAGg=','U8gL+kynZXk3L4tyTRyUMTR/W6M=','Kz3+8Xok8r0mMJ58c56fKUz+JQA3dhC0OMGOub6OZgg=','2l8Xu0fj/uCl8TJWpvAUnVN4mzU=',1,'2016-12-13 23:19:27'),(265,'Y6aaAozjch3zyjeir9Y/wjnTTYOf2BrHhuU1rtpIJB0jwmYxDA930eth','uZ189bxzk9vJxfiL92vHX5Qcf984RAbr4U+7MFWnWX0=','MDUPLcDor+FOOKBGqRQ9YIo017A=','V++9y2/iJhIWh4nrRiOMyJ6JXvZ8mRRqfTQyAArAmGE=','+x9qBkpn4Pu06TP062Fr3DjqJa0=',1,'2016-12-13 23:23:30'),(266,'rF0H6YeZgUkBkDL2n+3yjkECED3pe3oYhdoAIlcTZpKO0r0338UjIpYS','luWrHXDSSAP3s4IywOHJna+e2cAncvPiwhqjybgq8YE=','MK6fWGsXK7lOX54H2DOKA1AS5co=','DvEha4Rgkza3vkMYUVxDZHwa2ybTnC+4JjH8DGwaC3k=','roQflDYgA6xLsqYZsrjzEPHbqC4=',1,'2016-12-13 23:25:32'),(267,'gAwTrQvHlckfQG0112vTK8vxn65ymNRlXyNCuJEv9YKe+4lOmtZCNsbI','HBC5aWQhRPLKUldhC2d4qTm2JPF5ak7Lr3O4hG0RfpM=','ph2iJeg21VdzC7v7sxIdtSpYeyE=','j/nCFr7vYNpht+6owMCSsC8v1gOUQdA6M0ALpvY8Jfs=','2Ltlg/FpmaVBTsEkda22OnsPOWg=',1,'2016-12-13 23:29:33'),(268,'CAOWDjeYueH2Ht3Dyv/jNjqSatZ/uZbPtGIH9+/S4Rpw4pBuS2twZI2G','T9SrxYOVQeu75K1Q77AY2aGCYuxWqn9dwAV7zDoK4iw=','Y1Gx/4/YEsDJ00n1mh22EKHnQMM=','t+i+xN4XIV7oq5iOBePA24W5oNvKqt9qv5w1NvoRxDY=','F1RhWyp98DTEWXvyQ5fwwPu5s4w=',1,'2016-12-13 23:31:14'),(269,'LYMyMhH/Bmnirzyb3Rr+JddzPwNLctxDX9S5FO5IjRPyMeU9UWgaByF9','t2jVMCLxvmYsLSztlCxuTegiB97DueQs3+gbwx4UpMQ=','Kf3nhIaCg/sR9/7D8lbL6flAPb0=','895Cr6wWs1krNz/DD1vM4Qheqqk+ptxQ4HBnVMU2rPo=','3gDuyU5Tvz8rh5xbZ1uvSV7cSjI=',1,'2016-12-13 23:34:41'),(270,'Zc6rxMbq5foLaJcpjVzldlpUBqsOzUPYeiIxCZ9O7K3fAfw8WIGyB8Mw','WtS58+q7Q7ntAHzyOIQfp6ENl2UsKtw+O2bhfYDDsgM=','jWzy35rM0MiMt9q0mAtpQlUZdz0=','8zYgbbIWoPO+VuhEATlVAaICePciPTtnou9l8mim594=','tWAt3Kg1TmscQUsr+5U4Cb8BhME=',1,'2016-12-14 08:33:16'),(271,'h3nYpaKG9O0aa1ovudpZI2X+aQbJSNNbV1ETOUdSRk2m2gQ8FWJRz1b+','3nh4wifJlv/AZ7OPQDx426UgWI9m09h+YibVLIMmfBg=','vc8YwGvykp7qyBAMArDYLO7UxVA=','skFij5QyIPm8CpuD9TEp0iqSbSRRnkhW4fU+CjnfmYw=','bSdK4iz4dywToEOIJstZFDCLU+4=',1,'2016-12-14 08:35:19'),(282,'bWrvVMYbXjFQXojbeulYLI05uh2jqiKU7LQCAEqCi0OE8cQ/OKwEa4Lw','oriEppriccokIhnwqj2SfeJR31rE/zAeFeeaEgVRjoU=','N8IasJpUrcDk3p26Zq6Rlwm9RNc=','yGZRz63tqGworva3Bv106QMK+4f/UG+uqIDg2ed8tFM=','yn2tQJqArVl+BzGVv2dtxoIVYMY=',1,'2016-12-14 08:54:06'),(283,'+d8rNa4c6NgOXKiq0sDC7DMYzEbPnkFlS0pehuSZp/TUlA1tTdFW1OQV','LgIbY2/6NZZLjYyrjOTcNM/u8KdYKmurSQoet31GYgQ=','gXqfGLTulPLXdcAHK263nOW2kZg=','QmzRJ364penTN1tJvnfHy9HROL6fjXLLIz/aTt5HKaA=','Rt6hZ3xP5VTtqXWJWmsnf4mqG7o=',1,'2016-12-14 08:58:44'),(284,'LvK5evo8tPHnEohqHzoDaNUwa6sgY1BBLVzoKzHqat9HDs1RIhmauFTQ','M/NzalN9I8KEn0L6VplkLHHBtF/l6Js0nrRPBHEy/iY=','VXG2ZXZ17zYNXny/qRNU9UPdjiI=','yZcJdR9+GBtdYbA34rEYrIviI5igKA2MpuoENiIFMto=','LBPhrTTTcxbX7+B+E7lFH07Gs6E=',1,'2016-12-14 09:01:11'),(285,'STMLe0786Ockiu4i6E0n4BX+OTrCBslwdGXZP4uomlud+k8NmpHRv5YL','B553UPe+tApS4+tJ2Ow3K/HBnelyjXAuy7en5CSFB2U=','5o5RXjHhZO1ZNsMQySlGZ96NzzE=','DmEMij5MVRf7Gc1wQZMP+4BiDO9eZk6YGbVBLCYCy0E=','JkMrj8+9/rZDjcRVdm1CdUyHw5o=',1,'2016-12-14 09:03:39'),(286,'xkRUbJGzz5NPU/PMmpVHDg8zAweXzCdzcsNS2Da28/OpmN2OQ6+z5b6I','tGS6fc78QGmBn8nmEDF6tw0ue0a9v2it7oq2cKYb/Ts=','nB43wZexnkvD0QegubVZtkRwocI=','Q1P5R/Pf/Q88aKeaWA3QOFiWhc8ToWRE06rAw7O9rDI=','nkMyGdk+EyVUcNtUupc87ZxjoDM=',1,'2016-12-14 09:05:46'),(310,'Clq5HwzTRhdjwfRRnMsFWgS2fWhVMiebtgXRWtna49APql6JDSvezWzp','VGIhXsvrwu6R53mMV2ihR6lgyTHst0tOYSAD2ySMSAw=','Ey3A6P9uME2lTTPeTDtOwrtsw1o=','DB5w3TXgJjSaGtfGeasl0qstrMzlBpaoj238Fu0ibUQ=','wVTvQMYTnjyGYQizLFDc8IJNVdk=',1,'2016-12-14 11:27:19'),(311,'8EjYL6qa83DV8i+Exmbzv/gepCCea0APHHzWAlBC+CkJ/JZKaDDiTuoC','hBDXat5WSUkSOfS5CTrxe7c07cnP6Jst/7dODd4r9xg=','MB30cJ1i2Emzmy0+MafWgHrkEvc=','ffdF7Utl1eNL1EhnE1KK/QRtRIoFxNmBfgA84A45iSE=','BldVhFpTe3VrLIQeaGtdQmxwTGQ=',1,'2016-12-14 11:27:37'),(359,'1Z23VcMBbRkfLdfd0E7yaOeoQeziMdVLf8q2FtdJBnVO+2fJk3vT4DHV','fJYLPSRwOHymjm+nlhhOQaO8AIoZo1nG+Gi3hVBPSbM=','k7xMlBmWbjz5JhvIllozC6spe+A=','Cp969ldp4/c9AX+UlGb5OEN7wI51blTZgGud1Y+sg6Y=','kn9o+Qcaeo6y8b98WTmT7PSqzF0=',1,'2016-12-14 16:58:02');
/*!40000 ALTER TABLE `savedSessions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `serverKeys`
--

DROP TABLE IF EXISTS `serverKeys`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `serverKeys` (
  `login` varchar(255) NOT NULL,
  `password` varchar(50) NOT NULL,
  `saltEase` char(28) DEFAULT NULL,
  `saltPerso` char(28) DEFAULT NULL,
  `keyServer` char(44) DEFAULT NULL,
  PRIMARY KEY (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `serverKeys`
--

LOCK TABLES `serverKeys` WRITE;
/*!40000 ALTER TABLE `serverKeys` DISABLE KEYS */;
INSERT INTO `serverKeys` VALUES ('ThomasHeniart','NEBmKfVKJTTOrNt6DuJpa4BzmRXYLp98zGihNJ0/SGo=','lMBHo8XyWe9s0+rEGN1IFkCDi9c=','RweIEFl2wlEd69uDEpIqhhRbz3E=','DbBDkUMrLH+19VdtsJRXAxBWScyUG97djugVQFwpaNs=');
/*!40000 ALTER TABLE `serverKeys` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sharedApps`
--

DROP TABLE IF EXISTS `sharedApps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sharedApps` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `app_id` int(10) unsigned NOT NULL,
  `share_app_id` int(10) unsigned NOT NULL,
  `decrypt_key` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `app_id` (`app_id`),
  KEY `share_app_id` (`share_app_id`),
  CONSTRAINT `sharedapps_ibfk_1` FOREIGN KEY (`app_id`) REFERENCES `apps` (`id`),
  CONSTRAINT `sharedapps_ibfk_2` FOREIGN KEY (`share_app_id`) REFERENCES `apps` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sharedApps`
--

LOCK TABLES `sharedApps` WRITE;
/*!40000 ALTER TABLE `sharedApps` DISABLE KEYS */;
/*!40000 ALTER TABLE `sharedApps` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sharedKeys`
--

DROP TABLE IF EXISTS `sharedKeys`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sharedKeys` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `account_id` int(10) unsigned NOT NULL,
  `shared_key` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `sharedkeys_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sharedKeys`
--

LOCK TABLES `sharedKeys` WRITE;
/*!40000 ALTER TABLE `sharedKeys` DISABLE KEYS */;
/*!40000 ALTER TABLE `sharedKeys` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sso`
--

DROP TABLE IF EXISTS `sso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sso` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(40) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sso`
--

LOCK TABLES `sso` WRITE;
/*!40000 ALTER TABLE `sso` DISABLE KEYS */;
INSERT INTO `sso` VALUES (1,'Google');
/*!40000 ALTER TABLE `sso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `status` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `registered` tinyint(1) NOT NULL,
  `tuto` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status`
--

LOCK TABLES `status` WRITE;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
/*!40000 ALTER TABLE `status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tags`
--

DROP TABLE IF EXISTS `tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tags` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(14) NOT NULL,
  `color` char(7) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uc_tag_name` (`tag_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tags`
--

LOCK TABLES `tags` WRITE;
/*!40000 ALTER TABLE `tags` DISABLE KEYS */;
/*!40000 ALTER TABLE `tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tagsAndSitesMap`
--

DROP TABLE IF EXISTS `tagsAndSitesMap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tagsAndSitesMap` (
  `tag_id` int(10) unsigned NOT NULL,
  `website_id` int(10) unsigned NOT NULL,
  UNIQUE KEY `tag_and_website_unique` (`tag_id`,`website_id`),
  KEY `website_id` (`website_id`),
  CONSTRAINT `tagsandsitesmap_ibfk_1` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`),
  CONSTRAINT `tagsandsitesmap_ibfk_2` FOREIGN KEY (`website_id`) REFERENCES `websites` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tagsAndSitesMap`
--

LOCK TABLES `tagsAndSitesMap` WRITE;
/*!40000 ALTER TABLE `tagsAndSitesMap` DISABLE KEYS */;
/*!40000 ALTER TABLE `tagsAndSitesMap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tmpSharedApps`
--

DROP TABLE IF EXISTS `tmpSharedApps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tmpSharedApps` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `shared_app_id` int(10) unsigned NOT NULL,
  `rsa_key` varchar(255) NOT NULL,
  `insertDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `shared_app_id` (`shared_app_id`),
  CONSTRAINT `tmpsharedapps_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `tmpsharedapps_ibfk_2` FOREIGN KEY (`shared_app_id`) REFERENCES `apps` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tmpSharedApps`
--

LOCK TABLES `tmpSharedApps` WRITE;
/*!40000 ALTER TABLE `tmpSharedApps` DISABLE KEYS */;
/*!40000 ALTER TABLE `tmpSharedApps` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `updates`
--

DROP TABLE IF EXISTS `updates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `updates` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `website_id` int(10) unsigned NOT NULL,
  `login` varchar(255) NOT NULL,
  `type` varchar(25) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `website_id` (`website_id`),
  CONSTRAINT `updates_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `updates_ibfk_2` FOREIGN KEY (`website_id`) REFERENCES `websites` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `updates`
--

LOCK TABLES `updates` WRITE;
/*!40000 ALTER TABLE `updates` DISABLE KEYS */;
/*!40000 ALTER TABLE `updates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userKeys`
--

DROP TABLE IF EXISTS `userKeys`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userKeys` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `password` varchar(50) NOT NULL,
  `saltEase` char(28) DEFAULT NULL,
  `saltPerso` char(28) DEFAULT NULL,
  `keyUser` char(44) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userKeys`
--

LOCK TABLES `userKeys` WRITE;
/*!40000 ALTER TABLE `userKeys` DISABLE KEYS */;
INSERT INTO `userKeys` VALUES (1,'R7M/n/trIckquWq6R3nvuiQQrVYHC31LWvzh3dEYtnc=','a1EEsbuyjJU9+/MjWXcs3Vyfw/c=','BJde0s0sQvFHYy2v2IufCa8FGK0=','BUfQdPo4Ebm66TJKU2BU8Pg8PFD/P3DLbs+GmUWfILw='),(2,'NKBC727j6Q4ZPMAprqJmeqbwb98LyB30/BpKXtheBOI=','buqbHncpay2bjgQTfN1kVHR+x+g=','rwt3LGRWT0pdEo1MXth1mbxK9RI=','WrZtEDuvC66l5+3uP80QSBYZCmqZETVMKS0ZoPglidE='),(4,'U/pBNLR0z4Kof7eXmMY/yv/vADF8Yg+Yxo4Xh6TyKo4=','0Icwd2JJW5ySgvfZwc7feywncrs=','Abjklx74KEzsQjflvCSrlmTmwHg=','SNg+CV6UzorQkMrUek5ZclWfP9NjTQIXd0uzYjfXbqA=');
/*!40000 ALTER TABLE `userKeys` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `firstName` varchar(30) DEFAULT NULL,
  `lastName` varchar(30) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `key_id` int(10) unsigned DEFAULT NULL,
  `option_id` int(10) unsigned DEFAULT NULL,
  `registration_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `key_id` (`key_id`),
  KEY `option_id` (`option_id`),
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`key_id`) REFERENCES `userKeys` (`id`),
  CONSTRAINT `users_ibfk_2` FOREIGN KEY (`option_id`) REFERENCES `options` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'toto','','heniart.thomas@gmail.com',1,1,'2016-12-01 14:40:23'),(2,'Pierre','','pierre0debruyne@gmail.com',2,2,'2016-12-01 15:00:26'),(4,'toto','','Michel@test.com',4,4,'2016-12-07 17:05:38');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usersEmails`
--

DROP TABLE IF EXISTS `usersEmails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usersEmails` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `email` varchar(100) NOT NULL,
  `verified` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`email`),
  CONSTRAINT `usersemails_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usersEmails`
--

LOCK TABLES `usersEmails` WRITE;
/*!40000 ALTER TABLE `usersEmails` DISABLE KEYS */;
INSERT INTO `usersEmails` VALUES (1,1,'heniart.thomas@gmail.com',1),(2,2,'pierre0debruyne@gmail.com',0),(3,4,'Michel@test.com',1);
/*!40000 ALTER TABLE `usersEmails` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `usersEmailsPending`
--

LOCK TABLES `usersEmailsPending` WRITE;
/*!40000 ALTER TABLE `usersEmailsPending` DISABLE KEYS */;
/*!40000 ALTER TABLE `usersEmailsPending` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `websiteApps`
--

DROP TABLE IF EXISTS `websiteApps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `websiteApps` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `website_id` int(10) unsigned NOT NULL,
  `app_id` int(10) unsigned NOT NULL,
  `group_website_app_id` int(10) unsigned DEFAULT NULL,
  `type` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `app_id` (`app_id`),
  KEY `website_id` (`website_id`),
  KEY `group_website_app_id` (`group_website_app_id`),
  CONSTRAINT `websiteapps_ibfk_1` FOREIGN KEY (`app_id`) REFERENCES `apps` (`id`),
  CONSTRAINT `websiteapps_ibfk_2` FOREIGN KEY (`website_id`) REFERENCES `websites` (`id`),
  CONSTRAINT `websiteapps_ibfk_3` FOREIGN KEY (`group_website_app_id`) REFERENCES `groupWebsiteApps` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `websiteApps`
--

LOCK TABLES `websiteApps` WRITE;
/*!40000 ALTER TABLE `websiteApps` DISABLE KEYS */;
INSERT INTO `websiteApps` VALUES (6,7,8,NULL,'classicApp'),(7,7,9,NULL,'classicApp'),(8,7,10,NULL,'classicApp');
/*!40000 ALTER TABLE `websiteApps` ENABLE KEYS */;
UNLOCK TABLES;

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
  `hidden` tinyint(1) DEFAULT '0',
  `ratio` int(10) unsigned DEFAULT '0',
  `position` int(10) unsigned DEFAULT '1',
  `insertDate` datetime DEFAULT CURRENT_TIMESTAMP,
  `locked` tinyint(1) unsigned DEFAULT '0',
  `lockedExpiration` datetime DEFAULT NULL,
  `new` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `sso` (`sso`),
  CONSTRAINT `websites_ibfk_1` FOREIGN KEY (`sso`) REFERENCES `sso` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=167 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `websites`
--

LOCK TABLES `websites` WRITE;
/*!40000 ALTER TABLE `websites` DISABLE KEYS */;
INSERT INTO `websites` VALUES (5,'https://www.ieseg-online.com/','IESEG Online','resources/websites/IesegOnline/',NULL,0,'https://www.ieseg-online.com',0,325,2,'2016-11-01 00:00:00',0,NULL,1),(7,'https://www.facebook.com/','Facebook','resources/websites/Facebook/',NULL,0,'https://www.facebook.com',0,224,7,'2016-11-01 00:00:00',0,NULL,1),(28,'https://www.linkedin.com/','Linkedin','resources/websites/Linkedin/',NULL,0,'https://www.linkedin.com',0,92,12,'2016-11-01 00:00:00',0,NULL,1),(35,'https://twitter.com/','Twitter','resources/websites/Twitter/',NULL,0,'https://www.twitter.com',0,59,15,'2016-11-01 00:00:00',0,NULL,1),(46,'https://deliveroo.fr/','Deliveroo','resources/websites/Deliveroo/',NULL,0,'https://www.deliveroo.fr',0,19,25,'2016-11-01 00:00:00',0,NULL,1),(47,'http://www.deezer.com/','Deezer','resources/websites/Deezer/',NULL,0,'http://www.deezer.com',0,18,27,'2016-11-01 00:00:00',0,NULL,1),(49,'http://ieseg.lms.crossknowledge.com/','Crossknowledge','resources/websites/IesegCrossknowledge/',NULL,0,'http://ieseg.lms.crossknowledge.com',0,173,8,'2016-11-01 00:00:00',0,NULL,1),(50,'http://ieseg-network.com','Ieseg Network','resources/websites/IesegNetwork/',NULL,0,'http://ieseg-network.com',0,270,4,'2016-11-01 00:00:00',0,NULL,1),(51,'https://www.captaintrain.com/','Trainline','resources/websites/CaptainTrain/',NULL,0,'https://www.captaintrain.com',0,6,37,'2016-11-01 00:00:00',0,NULL,1),(52,'http://cafet.ieseg.fr/','Cafet Ieseg','resources/websites/CafetIeseg/',NULL,0,'http://cafet.ieseg.fr',0,11,32,'2016-11-01 00:00:00',0,NULL,1),(53,'https://ieseg.jobteaser.com/','Job Teaser','resources/websites/IesegJobTeaser/',NULL,0,'https://ieseg.jobteaser.com',0,259,6,'2016-11-01 00:00:00',0,NULL,1),(54,'https://www.skype.com/','Skype','resources/websites/Skype/',NULL,0,'https://www.skype.com',0,39,18,'2016-11-01 00:00:00',0,NULL,1),(55,'https://www.7speaking.com/','7Speaking','resources/websites/7speaking/',NULL,0,'https://www.7speaking.com',0,117,9,'2016-11-01 00:00:00',0,NULL,1),(56,'http://www.voyages-sncf.com/','Voyages SNCF','resources/websites/VoyageSNCF/',NULL,0,'http://www.voyage-sncf.com',0,42,17,'2016-11-01 00:00:00',0,NULL,1),(57,'https://openclassrooms.com/','OpenClassRoom','resources/websites/OpenClassRoom/',NULL,0,'https://www.openclassrooms.com',0,5,41,'2016-11-01 00:00:00',0,NULL,1),(58,'https://www.qualtrics.com/','Qualtrics','resources/websites/IesegQualtrics/',NULL,0,'https://www.qualtrics.com',0,6,38,'2016-11-01 00:00:00',0,NULL,1),(59,'http://www.mediapluspro.com/mediaplus69/client_net/login.aspx?cfgsite=html5&cfgbdd=ieseg-67','IesegMediaPlus','resources/websites/IesegMediaPlus/',NULL,0,'http://www.mediapluspro.com/mediaplus69/client_net/login.aspx?cfgsite=html5&cfgbdd=ieseg-67',0,106,11,'2016-11-01 00:00:00',0,NULL,1),(60,'https://www.projet-voltaire.fr/','ProjetVoltaire','resources/websites/ProjetVoltaire/',NULL,0,'https://www.projet-voltaire.fr',0,108,10,'2016-11-01 00:00:00',0,NULL,1),(61,'https://www.leboncoin.fr/','LeBonCoin','resources/websites/LeBonCoin/',NULL,0,'https://www.leboncoin.fr',0,16,28,'2016-11-01 00:00:00',0,NULL,1),(62,'https://www.blablacar.fr/','BlaBlaCar','resources/websites/BlaBlaCar/',NULL,0,'https://www.blablacar.fr',0,30,19,'2016-11-01 00:00:00',0,NULL,1),(63,'https://login.microsoftonline.com/fr','Suite Office','resources/websites/Office365/',NULL,0,'https://login.microsoftonline.com',0,27,20,'2016-11-01 00:00:00',0,NULL,1),(64,'https://accounts.google.com','Youtube','resources/websites/Youtube/',1,0,'https://www.youtube.com',0,86,14,'2016-11-01 00:00:00',0,NULL,1),(65,'https://accounts.google.com','Gmail','resources/websites/Gmail/',1,0,'https://www.gmail.com',0,92,13,'2016-11-01 00:00:00',0,NULL,1),(66,'http://fr.vente-privee.com/','Vente Privee','resources/websites/VentePrivee/',NULL,0,'http://fr.vente-privee.com',0,15,31,'2016-11-01 00:00:00',0,NULL,1),(67,'https://www.amazon.fr/','Amazon','resources/websites/Amazon/',NULL,0,'http://www.amazon.com',0,22,22,'2016-11-01 00:00:00',0,NULL,1),(68,'https://accounts.google.com','Google Drive','resources/websites/GoogleDrive/',1,0,'https://drive.google.com',0,49,16,'2016-11-01 00:00:00',0,NULL,1),(69,'https://outlook.office365.com/','Office Mails','resources/websites/Office365Mail/',NULL,0,'https://outlook.office365.com',0,278,3,'2016-11-01 00:00:00',0,NULL,1),(70,'https://www.kickstarter.com/','Kickstarter','resources/websites/Kickstarter/',NULL,0,'https://www.kickstarter.com',0,0,75,'2016-11-01 00:00:00',0,NULL,1),(71,'http://unify.ieseg.fr/','Unify IESEG','resources/websites/UnifyIeseg/',NULL,0,'http://unify.ieseg.fr',0,270,5,'2016-11-01 00:00:00',0,NULL,1),(72,'https://fr.coursera.org/','Coursera','resources/websites/Coursera/',NULL,0,'https://fr.coursera.org',0,4,45,'2016-11-01 00:00:00',0,NULL,1),(73,'https://www.manhattanprep.com/gmat/studentcenter/','Manhattan Prep','resources/websites/ManhattanPrepGmat/',NULL,0,'https://www.manhattanprep.com/gmat/studentcenter',0,1,68,'2016-11-01 00:00:00',0,NULL,1),(74,'https://gmat.magoosh.com/','Magoosh gmat','resources/websites/MagooshGmat/',NULL,0,'https://gmat.magoosh.com',0,1,69,'2016-11-01 00:00:00',0,NULL,1),(75,'https://www.reddit.com/','Reddit','resources/websites/Reddit/',NULL,0,'https://www.reddit.com',0,0,76,'2016-11-01 00:00:00',0,NULL,1),(77,'http://www.economist.com/','The Economist','resources/websites/Economist/',NULL,0,'http://www.economist.com',0,2,57,'2016-11-01 00:00:00',0,NULL,1),(85,'https://bunkrapp.com/','Bunkr','resources/websites/Bunkr/',NULL,0,'https://bunkrapp.com',0,5,42,'2016-11-01 00:00:00',0,NULL,1),(86,'http://www.zonebourse.com/','ZoneBourse','resources/websites/ZoneBourse/',NULL,0,'https://www.zonebourse.com',0,0,77,'2016-11-01 00:00:00',0,NULL,1),(87,'https://play.spotify.com/','Spotify','resources/websites/Spotify/',NULL,0,'https://www.spotify.com',0,23,21,'2016-11-01 00:00:00',0,NULL,1),(89,'https://www.airbnb.fr/Login','Airbnb','resources/websites/AirBnb/',NULL,0,'https://www.airbnb.fr',0,19,26,'2016-11-01 00:00:00',0,NULL,1),(90,'https://www.tumblr.com/','Tumblr','resources/websites/Tumblr/',NULL,0,'https://www.tumblr.com',0,2,58,'2016-11-01 00:00:00',0,NULL,1),(91,'http://www.sushiboutik.com/','SushiBoutik','resources/websites/SushiBoutik/',NULL,0,'http://www.sushiboutik.com',0,1,70,'2016-11-01 00:00:00',0,NULL,1),(92,'https://github.com/','Github','resources/websites/Github/',NULL,0,'https://www.github.com',0,3,51,'2016-11-01 00:00:00',0,NULL,1),(93,'https://www.dropbox.com','Dropbox','resources/websites/Dropbox/',NULL,0,'https://www.dropbox.com',0,16,29,'2016-11-01 00:00:00',0,NULL,1),(94,'https://www.netflix.com','Netflix','resources/websites/Netflix/',NULL,0,'https://www.netflix.com',0,21,23,'2016-11-01 00:00:00',0,NULL,1),(97,'http://lol.univ-catholille.fr/','BUVauban','resources/websites/BUVauban/',NULL,0,'http://lol.univ-catholille.fr',0,21,24,'2016-11-01 00:00:00',0,NULL,1),(98,'http://myieseg.com/mon-compte/','MyIeseg','resources/websites/MyIeseg/',NULL,0,'https://myieseg.com',0,16,30,'2016-11-01 00:00:00',0,NULL,1),(99,'https://www.koudetatondemand.co/','Koudetat','resources/websites/Koudetat/',NULL,0,'https://www.koudetatondemande.co',0,3,52,'2016-11-01 00:00:00',0,NULL,1),(100,'http://www.lemonde.fr','LeMonde','resources/websites/LeMonde/',NULL,0,'http://www.lemonde.fr',0,5,43,'2016-11-01 00:00:00',0,NULL,1),(102,'https://espace-perso.smeno.com/','Smeno','resources/websites/Smeno/',NULL,0,'https://www.smeno.com',0,4,46,'2016-11-01 00:00:00',0,NULL,1),(103,'http://www.tf1.fr/','MyTF1','resources/websites/MyTF1/',NULL,0,'http://www.tf1.fr',0,7,35,'2016-11-01 00:00:00',0,NULL,1),(104,'https://espace-personnel.adecco.fr/','Adecco','resources/websites/Adecco/',NULL,0,'https://www.adecco.fr',0,7,36,'2016-11-01 00:00:00',0,NULL,1),(106,'https://www.viadeo.com','Viadeo','resources/websites/Viadeo/',NULL,0,'https://www.viadeo.com',0,4,47,'2016-11-01 00:00:00',0,NULL,1),(107,'http://fr.fashionjobs.com/','FashionJobs','resources/websites/FashionJobs/',NULL,0,'http://fr.fashionjobs.com',0,2,59,'2016-11-01 00:00:00',0,NULL,1),(110,'https://www.priceminister.com/','PriceMinister','resources/websites/PriceMinister/',NULL,0,'https://www.priceminister.com',0,2,60,'2016-11-01 00:00:00',0,NULL,1),(117,'https://www.cremedelacreme.io/','CremeDeLaCreme','resources/websites/CremeDeLaCreme/',NULL,0,'https://cremedelacreme.io',0,6,39,'2016-11-01 00:00:00',0,NULL,1),(118,'https://secure.meetup.com/login/','Meetup','resources/websites/Meetup/',NULL,0,'https://www.meetup.com',0,3,53,'2016-11-01 00:00:00',0,NULL,1),(119,'htttps://www.babbel.com/','Babbel','resources/websites/Babbel/',NULL,0,'https://www.babbel.com',0,3,54,'2016-11-01 00:00:00',0,NULL,1),(120,'https://www.canva.com','Canva','resources/websites/Canva/',NULL,0,'https://www.canva.com',0,4,48,'2016-11-01 00:00:00',0,NULL,1),(121,'https://www.alloresto.fr/connexion','AlloResto','resources/websites/AlloResto/',NULL,0,'https://www.alloresto.fr',0,1,71,'2016-11-01 00:00:00',0,NULL,1),(122,'https://www.lefigaro.fr','LeFigaro','resources/websites/LeFigaro/',NULL,0,'https://www.lefigaro.fr',0,5,44,'2016-11-01 00:00:00',0,NULL,1),(123,'https://secure.fnac.com','Fnac','resources/websites/Fnac/',NULL,0,'https://www.fnac.com',0,4,49,'2016-11-01 00:00:00',0,NULL,1),(124,'https://www.cdiscount.com/','Cdiscount','resources/websites/Cdiscount/',NULL,0,'https://www.cdiscount.com',0,0,78,'2016-11-01 00:00:00',0,NULL,1),(126,'https://www.zara.com','Zara','resources/websites/Zara/',NULL,0,'https://www.zara.com',0,3,55,'2016-11-01 00:00:00',0,NULL,1),(128,'https://users.wix.com/','Wix','resources/websites/Wix/',NULL,0,'https://www.wix.com',0,1,72,'2016-11-01 00:00:00',0,NULL,1),(129,'https://accounts.google.com','GoogleAnalytic','resources/websites/GoogleAnalytics/',1,0,'https://analytics.google.com',0,3,56,'2016-11-01 00:00:00',0,NULL,1),(130,'https://my.lmde.fr/web/guest/espace-personnel/authentification','LMDE','resources/websites/LMDE/',NULL,0,'https://www.lmde.fr',0,0,79,'2016-11-01 00:00:00',0,NULL,1),(131,'https://www.showroomprive.com/','ShowroomPrive','resources/websites/ShowroomPrive/',NULL,0,'https://www.showroomprive.com',0,4,50,'2016-11-01 00:00:00',0,NULL,1),(132,'http://icampus.univ-catholille.fr/','Icampus','resources/websites/Icampus/',NULL,0,'http://icampus.univ-catholille.fr',0,2,61,'2016-11-01 00:00:00',0,NULL,1),(133,'https://www.t411.ch','T411','resources/websites/T411/',NULL,0,'https://www.t411.ch',0,2,62,'2016-11-01 00:00:00',0,NULL,1),(134,'http://www.netmeet.fr/','NetMeet','resources/websites/NetMeet/',NULL,0,'http://www.netmeet.fr',0,2,63,'2016-11-01 00:00:00',0,NULL,1),(135,'http://www.senscritique.com/','SensCritique','resources/websites/SensCritique/',NULL,0,'http://www.senscritique.com',0,1,73,'2016-11-01 00:00:00',0,NULL,1),(136,'https://www.zalando.com','Zalando','resources/websites/Zalando/',NULL,0,'https://www.zalando.com',0,2,64,'2016-11-01 00:00:00',0,NULL,1),(137,'https://www.betclic.fr','Betclic','resources/websites/Betclic/',NULL,0,'https://www.betclic.fr',0,2,65,'2016-11-01 00:00:00',0,NULL,1),(138,'https://yoopies.fr','Yoopies','resources/websites/Yoopies/',NULL,0,'https://www.yoopies.fr',0,1,74,'2016-11-01 00:00:00',0,NULL,1),(139,'https://my-speaking-agency.com/','SpeakingAgency','resources/websites/SpeakingAgency/',NULL,0,'https://my-speaking-agency.com',0,0,80,'2016-11-01 00:00:00',0,NULL,1),(140,'https://login.yahoo.com/config/mail','YahooMail','resources/websites/YahooMail/',NULL,0,'https://www.yahoo.com',0,2,66,'2016-11-01 00:00:00',0,NULL,1),(142,'https://passport.twitch.tv','Twitch','resources/websites/Twitch/',NULL,0,'https://www.twitch.tv',0,0,81,'2016-11-01 00:00:00',0,NULL,1),(144,'https://soundcloud.com','SoundCloud','resources/websites/Soundcloud/',NULL,1,'https://www.soundcloud.com',0,10,33,'2016-11-01 00:00:00',0,NULL,1),(145,'https://www.intagram.com','Instagram','resources/websites/Instagram/',NULL,1,'https://www.instagram.com',0,8,34,'2016-11-01 00:00:00',0,NULL,1),(146,'https://www.edx.org/','edX','resources/websites/edX/',NULL,0,'',0,0,82,'2016-11-01 00:00:00',0,NULL,1),(149,'https://www.messenger.com/login','Messenger','resources/websites/Messenger/',NULL,0,'https://www.messenger.com',0,0,83,'2016-11-01 00:00:00',0,NULL,1),(150,'https://www.monpetitgazon.com/','MonPetitGazon','resources/websites/MonPetitGazon/',NULL,0,'https://www.monpetitgazon.com',0,0,84,'2016-11-01 00:00:00',0,NULL,1),(153,'https://app.kickofflabs.com/login','KickoffLabs','resources/websites/KickoffLabs/',NULL,0,'https://kickofflabs.com',0,0,85,'2016-11-01 00:00:00',0,NULL,1),(154,'https://srvadfs.ieseg.fr/','ThesisManager','resources/websites/ThesisManager/',NULL,0,'https://srvadfs.ieseg.fr',0,0,86,'2016-11-01 00:00:00',0,NULL,1),(158,'acec','acaea','resources/websites/aaxaz/',NULL,0,'zdczcez',0,0,87,'2016-11-01 00:00:00',0,NULL,1),(159,'https://www.slack.com','Slack','resources/websites/Slack/',NULL,0,'https://www.slack.com',0,6,40,'2016-11-07 18:07:49',0,NULL,1),(161,'https://freedcamp.com/','Freedcamp','resources/websites/Freedcamp/',NULL,0,'https://freedcamp.com/',0,0,88,'2016-11-14 09:53:07',0,NULL,1),(162,'https://www.icloud.com/','iCloud','resources/websites/iCloud/',NULL,0,'https://www.icloud.com/',0,0,89,'2016-11-14 10:15:28',0,NULL,1),(163,'http://www.aplia.com/','Aplia','resources/websites/Aplia/',NULL,0,'http://www.aplia.com/',0,0,90,'2016-11-14 10:58:32',0,NULL,1),(164,'https://www.futurelearn.com/','FutureLearn','resources/websites/FutureLearn/',NULL,0,'https://www.futurelearn.com/',0,0,91,'2016-11-14 12:42:49',0,NULL,1),(165,'https://shop.mheducation.com/store/services/loginview','McGrawHill','resources/websites/McGrawHill/',NULL,0,'http://www.mheducation.com/',0,0,92,'2016-11-14 13:00:14',0,NULL,1),(166,'https://framindmap.org/','Framindmap','resources/websites/Framindmap/',NULL,0,'https://framindmap.org/',0,2,67,'2016-11-14 13:23:37',0,NULL,1);
/*!40000 ALTER TABLE `websites` ENABLE KEYS */;
UNLOCK TABLES;

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
  PRIMARY KEY (`id`),
  KEY `website_id` (`website_id`),
  CONSTRAINT `websitesinformations_ibfk_1` FOREIGN KEY (`website_id`) REFERENCES `websites` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=271 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `websitesInformations`
--

LOCK TABLES `websitesInformations` WRITE;
/*!40000 ALTER TABLE `websitesInformations` DISABLE KEYS */;
INSERT INTO `websitesInformations` VALUES (1,5,'login','text'),(2,7,'login','text'),(3,28,'login','text'),(4,35,'login','text'),(5,46,'login','text'),(6,47,'login','text'),(7,49,'login','text'),(8,50,'login','text'),(9,51,'login','text'),(10,52,'login','text'),(11,53,'login','text'),(12,54,'login','text'),(13,55,'login','text'),(14,56,'login','text'),(15,57,'login','text'),(16,58,'login','text'),(17,59,'login','text'),(18,60,'login','text'),(19,61,'login','text'),(20,62,'login','text'),(21,63,'login','text'),(22,66,'login','text'),(23,67,'login','text'),(24,69,'login','text'),(25,70,'login','text'),(26,71,'login','text'),(27,72,'login','text'),(28,73,'login','text'),(29,74,'login','text'),(30,75,'login','text'),(31,77,'login','text'),(32,85,'login','text'),(33,86,'login','text'),(34,87,'login','text'),(35,89,'login','text'),(36,90,'login','text'),(37,91,'login','text'),(38,92,'login','text'),(39,93,'login','text'),(40,94,'login','text'),(41,97,'login','text'),(42,98,'login','text'),(43,99,'login','text'),(44,100,'login','text'),(45,102,'login','text'),(46,103,'login','text'),(47,104,'login','text'),(48,106,'login','text'),(49,107,'login','text'),(50,110,'login','text'),(51,117,'login','text'),(52,118,'login','text'),(53,119,'login','text'),(54,120,'login','text'),(55,121,'login','text'),(56,122,'login','text'),(57,123,'login','text'),(58,124,'login','text'),(59,126,'login','text'),(60,128,'login','text'),(61,130,'login','text'),(62,131,'login','text'),(63,132,'login','text'),(64,133,'login','text'),(65,134,'login','text'),(66,135,'login','text'),(67,136,'login','text'),(68,137,'login','text'),(69,138,'login','text'),(70,139,'login','text'),(71,140,'login','text'),(72,142,'login','text'),(73,144,'login','text'),(74,145,'login','text'),(75,146,'login','text'),(76,149,'login','text'),(77,150,'login','text'),(78,153,'login','text'),(79,154,'login','text'),(80,158,'login','text'),(81,64,'login','text'),(82,65,'login','text'),(83,68,'login','text'),(84,129,'login','text'),(128,5,'password','password'),(129,7,'password','password'),(130,28,'password','password'),(131,35,'password','password'),(132,46,'password','password'),(133,47,'password','password'),(134,49,'password','password'),(135,50,'password','password'),(136,51,'password','password'),(137,52,'password','password'),(138,53,'password','password'),(139,54,'password','password'),(140,55,'password','password'),(141,56,'password','password'),(142,57,'password','password'),(143,58,'password','password'),(144,59,'password','password'),(145,60,'password','password'),(146,61,'password','password'),(147,62,'password','password'),(148,63,'password','password'),(149,66,'password','password'),(150,67,'password','password'),(151,69,'password','password'),(152,70,'password','password'),(153,71,'password','password'),(154,72,'password','password'),(155,73,'password','password'),(156,74,'password','password'),(157,75,'password','password'),(158,77,'password','password'),(159,85,'password','password'),(160,86,'password','password'),(161,87,'password','password'),(162,89,'password','password'),(163,90,'password','password'),(164,91,'password','password'),(165,92,'password','password'),(166,93,'password','password'),(167,94,'password','password'),(168,97,'password','password'),(169,98,'password','password'),(170,99,'password','password'),(171,100,'password','password'),(172,102,'password','password'),(173,103,'password','password'),(174,104,'password','password'),(175,106,'password','password'),(176,107,'password','password'),(177,110,'password','password'),(178,117,'password','password'),(179,118,'password','password'),(180,119,'password','password'),(181,120,'password','password'),(182,121,'password','password'),(183,122,'password','password'),(184,123,'password','password'),(185,124,'password','password'),(186,126,'password','password'),(187,128,'password','password'),(188,130,'password','password'),(189,131,'password','password'),(190,132,'password','password'),(191,133,'password','password'),(192,134,'password','password'),(193,135,'password','password'),(194,136,'password','password'),(195,137,'password','password'),(196,138,'password','password'),(197,139,'password','password'),(198,140,'password','password'),(199,142,'password','password'),(200,144,'password','password'),(201,145,'password','password'),(202,146,'password','password'),(203,149,'password','password'),(204,150,'password','password'),(205,153,'password','password'),(206,154,'password','password'),(207,158,'password','password'),(208,64,'password','password'),(209,65,'password','password'),(210,68,'password','password'),(211,129,'password','password'),(255,149,'team','text'),(256,159,'login','text'),(257,159,'password','password'),(258,159,'team','text'),(259,161,'login','text'),(260,161,'password','password'),(261,162,'login','text'),(262,162,'password','password'),(263,163,'login','text'),(264,163,'password','password'),(265,164,'login','text'),(266,164,'password','password'),(267,165,'login','text'),(268,165,'password','password'),(269,166,'login','text'),(270,166,'password','password');
/*!40000 ALTER TABLE `websitesInformations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `websitesLogWithMap`
--

DROP TABLE IF EXISTS `websitesLogWithMap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `websitesLogWithMap` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `website_id` int(10) unsigned NOT NULL,
  `website_logwith_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `website_id` (`website_id`),
  KEY `website_logwith_id` (`website_logwith_id`),
  CONSTRAINT `websiteslogwithmap_ibfk_1` FOREIGN KEY (`website_id`) REFERENCES `websites` (`id`),
  CONSTRAINT `websiteslogwithmap_ibfk_2` FOREIGN KEY (`website_logwith_id`) REFERENCES `loginWithWebsites` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `websitesLogWithMap`
--

LOCK TABLES `websitesLogWithMap` WRITE;
/*!40000 ALTER TABLE `websitesLogWithMap` DISABLE KEYS */;
INSERT INTO `websitesLogWithMap` VALUES (1,47,1),(2,50,1),(3,54,1),(4,57,1),(5,62,1),(6,70,1),(7,72,1),(8,85,1),(9,87,1),(10,89,1),(11,94,1),(12,99,1),(13,103,1),(14,118,1),(15,119,1),(16,120,1),(17,121,1),(18,122,1),(19,128,1),(20,131,1),(21,134,1),(22,135,1),(23,138,1),(24,142,1),(25,144,1),(26,145,1),(27,146,1),(28,161,1),(29,164,1),(32,50,2),(33,122,2),(34,161,2);
/*!40000 ALTER TABLE `websitesLogWithMap` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-12-14 17:18:48
