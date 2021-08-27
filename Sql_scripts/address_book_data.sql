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
-- Dumping data for table `addresses`
--

LOCK TABLES `addresses` WRITE;
/*!40000 ALTER TABLE `addresses` DISABLE KEYS */;
INSERT INTO `addresses` VALUES (1,5,234,'Tobie','Abel','19094644166','4213  Gordon Street','','Chino','California','91710',1),(2,5,106,'Adri','Hora','04426585493','28, Ebenezer Street, Pudur, Ambattur','','Chennai','Tamil Nadu','600053',0),(3,7,199,'Chan','Yi','068900012','28 Ayer Rajah Cresent #05-01','Ayer Rajah Industrial Estate 1399','Singapore','','139959',0),(4,34,234,'Robert','Martin','703-325-3192','92  Golf Course Drive','','Alexandria','Virginia','22303',0),(5,18,234,'Juanita','Mason','608-827-2230','256  Irish Lane','','Verona','Wisconsin','53593',0),(6,16,199,'Bao','Shao','068601449','30 Tuas Ave 2, 639461','','Singapore','','639461',0),(7,14,106,'Hastimukha','Krishna','02224033183','44 New Nehru Nagar Hsg Society Gr Floor','Feet Road, Opp Santac','Mumbai','Maharashtra','400017',1),(8,14,106,'Varun','Ramkissoon','01126910573','B 9, Lajpat Nagar','',' Bangalore','Karnataka','110024',0),(9,25,242,'Diep','Ngoc Hao','0909102509','56 Cu Lao St., Ward 2','Phu Nhuan District','Ho Chi Minh city','','71011',0),(10,4,39,'Bryan','Rodriquez','905-513-6645','1331  Harvest Moon Dr','','Unionville','Ontario','L3R 0L',0);
/*!40000 ALTER TABLE `addresses` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-06-10 13:02:26
