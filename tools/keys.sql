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
-- Dumping data for table `serverKeys`
--

LOCK TABLES `serverKeys` WRITE;
/*!40000 ALTER TABLE `serverKeys` DISABLE KEYS */;
INSERT INTO `serverKeys` VALUES ('felix','$2a$10$jEW9Ytb0ciuSh5X/72zy3OjC2l4Ja37IP5WL6w2gFi4Kz8zMhW8qy','s4qw2K/ZZAp7pTAYBaWvy5W42oA=','1eo/qbe+6Ag+MVBt0M+MRMPpf6w/5NfWRt2dNd+FOAA='),('pierre','$2a$10$siiJbuLzfJUR6UMR8tzeW..Ym.aswgw2vXDTQPoOdmaVdWHW.Tot.','fls3xnm5sEtkdGz+hmBZhrSEoG8=','xUSYefiyzjBq6keXzf+C1bE3stMmeDnxotm32v4uCOs='),('sergii','$2a$10$egRItAvljs.cB6iBMtEXG.BDmGDiKbSyZoU5cgUnT81SoVwSxKlo.','MIACClcexUr4j5Nb/8BoUcX9uKc=','Q8wMh8yV1ltNA6dLY7EiQormbuPNCZc1bUQMP97XYxE='),('thomas','$2a$10$JY1iX2PgYWfSYUmtduLedO8NsuWViDXjw7jc9xQqsUum/F7gaiEDK','81h2Hqzn2o7E8vCU/y1xh7UZgKA=','FeSdsczPNzInjf//V3VXwgPnPxhXWv3dV0EEMImxTt4=');
/*!40000 ALTER TABLE `serverKeys` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-02-15 17:47:42
