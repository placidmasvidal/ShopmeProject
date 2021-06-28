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
-- Dumping data for table `brands`
--

LOCK TABLES `brands` WRITE;
/*!40000 ALTER TABLE `brands` DISABLE KEYS */;
INSERT INTO `brands` VALUES (1,'Canon','Canon.png'),(2,'Fujifilm','Fujifilm.png'),(3,'Sony','Sony.png'),(4,'HP','HP.png'),(5,'SanDisk','SanDisk.png'),(6,'Western Digital','Western Digital.png'),(7,'Panasonic','Panasonic.png'),(8,'Pelican','Pelican.png'),(9,'Apple','Apple.png'),(10,'Samsung','Samsung.png'),(11,'Olympus','Olympus.png'),(12,'CADeN','Caden.png'),(13,'AmazonBasics','amazonbasics.png'),(14,'Nikon','Nikon.png'),(15,'Neewer','Neewer.png'),(16,'Sigma','Sigma.png'),(17,'Bosch','Bosch.png'),(18,'Joby','Joby.png'),(19,'GoPro','GoPro.png'),(20,'Manfrotto','Manfrotto.png'),(21,'Google','Google.png'),(22,'LG','LG.png'),(23,'Motorola','Motorola.png'),(24,'Pantech','Pantech.png'),(25,'Huawei','Huawei.png'),(26,'Xiaomi','Xiaomi.png'),(27,'HOVAMP','Hovamp.png'),(28,'Aioneus','Aioneus.png'),(29,'XIAE','XIAE.png'),(30,'Everyworth','Everyworth.png'),(31,'Lexar','Lexar.png'),(32,'Nulaxy','Nulaxy.png'),(33,'Fitfort','Fitfort.png'),(34,'PopSockets','PopSocket.png'),(35,'SHAWE','SHAWE.png'),(36,'Lenovo','Lenovo.png'),(37,'Acer','Acer.png'),(38,'Dell','Dell.png'),(39,'Intel','Intel.png'),(40,'ASUS','ASUS.png'),(41,'Microsoft','Microsoft.png'),(42,'DragonTouch','DragonTouch.png'),(43,'AMD','AMD.png'),(44,'XFX','XFX.png'),(45,'MSI','MSI.png'),(46,'Seagate','Seagate.png'),(47,'Cosair','Corsair.png'),(48,'Thermaltake','Thermaltake.png'),(49,'Kingston','Kingston.png'),(50,'Creative','Creative.png'),(51,'Crucial','Crucial.png'),(52,'HyperX','HyperX.png'),(53,'Gigabyte','Gigabyte.png'),(54,'TP-Link','TP-Link.png');
/*!40000 ALTER TABLE `brands` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `brands_categories`
--

LOCK TABLES `brands_categories` WRITE;
/*!40000 ALTER TABLE `brands_categories` DISABLE KEYS */;
INSERT INTO `brands_categories` VALUES (6,1),(14,10),(1,2),(2,2),(3,2),(4,5),(4,6),(5,24),(5,29),(5,27),(6,24),(6,29),(6,27),(1,10),(3,10),(7,2),(7,10),(8,2),(8,9),(9,5),(9,6),(9,7),(9,4),(9,14),(9,15),(5,31),(10,24),(10,25),(10,29),(10,27),(10,5),(10,6),(10,7),(10,14),(10,15),(11,10),(12,9),(13,9),(14,11),(1,11),(3,11),(15,11),(1,12),(14,12),(16,12),(17,13),(18,13),(19,13),(20,13),(21,14),(22,14),(23,14),(24,14),(21,15),(23,15),(25,15),(26,15),(27,17),(28,17),(29,17),(30,17),(10,18),(5,18),(31,18),(20,19),(32,19),(33,19),(34,19),(35,19),(36,5),(36,6),(36,7),(37,5),(37,6),(37,7),(38,5),(38,6),(38,7),(39,22),(39,5),(40,30),(40,5),(40,6),(40,7),(41,7),(42,7),(43,22),(43,23),(44,23),(45,23),(45,30),(46,24),(40,25),(22,25),(40,26),(47,29),(47,26),(48,26),(49,29),(49,27),(40,28),(50,28),(51,29),(52,29),(53,30),(54,31);
/*!40000 ALTER TABLE `brands_categories` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-03-24 16:35:25
