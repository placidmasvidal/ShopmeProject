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
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'Electronics','electronics','electronics.png',1,NULL),(2,'Camera & Photo','camera','camera.jpg',1,1),(3,'Computers','computers','computers.png',1,NULL),(4,'Cell Phones & Accessories','cellphones','cellphones.png',1,1),(5,'Desktops','desktop_computers','desktop computers.png',1,3),(6,'Laptops','laptop_computers','laptop computers.png',1,3),(7,'Tablets','tablet_computers','tablets.png',1,3),(8,'Computer Components','computer_components','computer components.png',1,3),(9,'Bags & Cases','camera_bags_cases','bags_cases.png',1,2),(10,'Digital Cameras','digital_cameras','digital cameras.png',1,2),(11,'Flashes','camera_flashes','flashes.png',1,2),(12,'Lenses','camera_lenses','lenses.png',1,2),(13,'Tripods & Monopods','camera_tripods_monopods','tripods_monopods.png',1,2),(14,'Carrier Cell Phones','carrier_cellphones','carrier cellphones.png',1,4),(15,'Unlocked Cell Phones','unlocked_cellphones','unlocked cellphones.png',1,4),(16,'Accessories','cellphone_accessories','cellphone accessories.png',1,4),(17,'Cables & Adapters','cellphone_cables_adapters','cables and adapters.png',1,16),(18,'MicroSD Cards','microsd_cards','microsd cards.png',1,16),(19,'Stands','cellphone_stands','cellphone_stands.png',1,16),(20,'Cases','cellphone_cases','cellphone cases.png',1,16),(21,'Headphones','headphones','headphones.png',1,16),(22,'CPU Processors Unit','computer_processors','computer processors.png',1,8),(23,'Graphic Cards','computer_graphic_cards','graphic cards.png',1,8),(24,'Internal Hard Drives','hard_drive','internal hard drive.png',1,8),(25,'Internal Optical Drives','computer_optical_drives','internal optical drives.png',1,8),(26,'Power Supplies','computer_power_supplies','power supplies.png',1,8),(27,'Solid State Drives','solid_state_drives','solid state drives.png',1,8),(28,'Sound Cards','computer_sound_cards','sound cards.png',1,8),(29,'Memory','computer_memory','computer memory.png',1,8),(30,'Motherboard','computer_motherboard','motherboards.png',1,8),(31,'Network Cards','computer_network_cards','network cards.png',1,8);
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-03-20  8:23:08
