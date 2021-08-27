-- MySQL dump 10.13  Distrib 8.0.15, for Win64 (x86_64)
--
-- Host: localhost    Database: shopmedb
-- ------------------------------------------------------
-- Server version	8.0.15

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `shipping_rates`
--

LOCK TABLES `shipping_rates` WRITE;
/*!40000 ALTER TABLE `shipping_rates` DISABLE KEYS */;
INSERT INTO `shipping_rates` VALUES (2,234,'New York',10,7,1),(3,234,'Florida',12,6,0),(5,242,'Hanoi',3.98,2,1),(6,234,'California',11.56,6,0),(7,242,'Hai Phong',3.93,2,0),(8,242,'Bac Giang',4.12,2,0),(9,242,'Phu Tho',4.21,3,0),(10,242,'Thanh Hoa',3.52,1,1),(11,106,'Karnataka',8.22,5,0),(12,106,'Maharashtra',8.69,5,1),(13,106,'Meghalaya',8.1,4,0),(14,106,'Punjab',7.89,3,0),(15,106,'Tamil Nadu',8.25,4,0),(16,106,'Telangana',7.72,4,0),(17,242,'Da Nang',0.5,1,1),(18,234,'Ohio',11.5,8,0),(19,78,'London',9.88,6,1),(20,106,'Delhi',8.45,5,1),(21,106,'West Bengal',8.88,5,1),(22,78,'Barton',7.78,6,1),(23,106,'Andhra Pradesh',8.12,6,1),(24,234,'Tennessee',12,8,1),(25,234,'Massachusetts',11.85,7,1),(26,14,'Queensland',4.99,5,1),(27,199,'Singapore',3.33,3,1),(28,39,'British Columbia',9.88,7,1),(29,14,'New South Wales',4.57,6,1),(30,234,'Illinois',13,9,1);
/*!40000 ALTER TABLE `shipping_rates` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-06-05 22:14:43
