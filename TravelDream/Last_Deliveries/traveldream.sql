CREATE DATABASE  IF NOT EXISTS `traveldream` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `traveldream`;
-- MySQL dump 10.13  Distrib 5.6.13, for osx10.6 (i386)
--
-- Host: 127.0.0.1    Database: traveldream
-- ------------------------------------------------------
-- Server version	5.6.14

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
-- Table structure for table `Amico`
--

DROP TABLE IF EXISTS `Amico`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Amico` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Amico` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Amico`
--

LOCK TABLES `Amico` WRITE;
/*!40000 ALTER TABLE `Amico` DISABLE KEYS */;
INSERT INTO `Amico` VALUES (1,'jack11@outlook.it');
/*!40000 ALTER TABLE `Amico` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AmicoGiftList`
--

DROP TABLE IF EXISTS `AmicoGiftList`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AmicoGiftList` (
  `Amico_ID` int(11) NOT NULL,
  `GiftList_ID` int(11) NOT NULL,
  PRIMARY KEY (`Amico_ID`,`GiftList_ID`),
  KEY `fk_Amico_has_Gift List_Gift List1_idx` (`GiftList_ID`),
  KEY `fk_Amico_has_Gift List_Amico1_idx` (`Amico_ID`),
  CONSTRAINT `fk_Amico_has_Gift List_Amico1` FOREIGN KEY (`Amico_ID`) REFERENCES `Amico` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Amico_has_Gift List_Gift List1` FOREIGN KEY (`GiftList_ID`) REFERENCES `GiftList` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AmicoGiftList`
--

LOCK TABLES `AmicoGiftList` WRITE;
/*!40000 ALTER TABLE `AmicoGiftList` DISABLE KEYS */;
INSERT INTO `AmicoGiftList` VALUES (1,1);
/*!40000 ALTER TABLE `AmicoGiftList` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Escursione`
--

DROP TABLE IF EXISTS `Escursione`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Escursione` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Nome` varchar(255) NOT NULL,
  `Data` date NOT NULL,
  `Costo` int(11) NOT NULL,
  `Luogo` varchar(255) NOT NULL,
  `Immagine` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Escursione`
--

LOCK TABLES `Escursione` WRITE;
/*!40000 ALTER TABLE `Escursione` DISABLE KEYS */;
INSERT INTO `Escursione` VALUES (5,'Partita Milan Palermo','2014-02-06',32,'Milano','Edefault.jpg'),(6,'Duomo di Milano','2014-02-13',12,'Milano','Edefault.jpg'),(7,'Toronto Symphony Orchestra','2014-04-15',26,'Toronto','toronto-symphony-orchestra.jpg'),(8,'High Park','2014-04-13',5,'Toronto','high.jpg'),(9,'AGO','2014-04-19',22,'Toronto','ago.jpeg'),(10,'Museo Reale','2014-04-07',13,'Toronto','royal-ontario-museum.jpg'),(11,'Sydney Harbour','2014-03-11',19,'Sydney','photo-of-sydney-harbour.jpg'),(12,'Wildlife Zoo','2014-03-17',38,'Sydney','sydney-wildlife-world.jpg'),(13,'Observatory','2014-03-13',32,'Sydney','sydney-observatory-observatory.jpg');
/*!40000 ALTER TABLE `Escursione` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EscursionePacchetto`
--

DROP TABLE IF EXISTS `EscursionePacchetto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EscursionePacchetto` (
  `Pacchetto_ID` int(11) NOT NULL,
  `Escursione_ID` int(11) NOT NULL,
  PRIMARY KEY (`Pacchetto_ID`,`Escursione_ID`),
  KEY `fk_Pacchetto_has_Escursione_Escursione1_idx` (`Escursione_ID`),
  KEY `fk_Pacchetto_has_Escursione_Pacchetto1_idx` (`Pacchetto_ID`),
  CONSTRAINT `fk_Pacchetto_has_Escursione_Escursione1` FOREIGN KEY (`Escursione_ID`) REFERENCES `Escursione` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Pacchetto_has_Escursione_Pacchetto1` FOREIGN KEY (`Pacchetto_ID`) REFERENCES `Pacchetto` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EscursionePacchetto`
--

LOCK TABLES `EscursionePacchetto` WRITE;
/*!40000 ALTER TABLE `EscursionePacchetto` DISABLE KEYS */;
INSERT INTO `EscursionePacchetto` VALUES (3,5),(3,6),(4,7),(4,8),(4,9),(4,10),(5,11),(5,12),(5,13);
/*!40000 ALTER TABLE `EscursionePacchetto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EscursionePagata`
--

DROP TABLE IF EXISTS `EscursionePagata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EscursionePagata` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Pagata` tinyint(1) NOT NULL,
  `GiftList_ID` int(11) NOT NULL,
  `EscursioneSalvata_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_EscursionePagata_Gift List1_idx` (`GiftList_ID`),
  KEY `fk_EscursionePagata_EscursioneSalvata1_idx` (`EscursioneSalvata_ID`),
  CONSTRAINT `fk_EscursionePagata_EscursioneSalvata1` FOREIGN KEY (`EscursioneSalvata_ID`) REFERENCES `EscursioneSalvata` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_EscursionePagata_Gift List1` FOREIGN KEY (`GiftList_ID`) REFERENCES `GiftList` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EscursionePagata`
--

LOCK TABLES `EscursionePagata` WRITE;
/*!40000 ALTER TABLE `EscursionePagata` DISABLE KEYS */;
INSERT INTO `EscursionePagata` VALUES (1,0,1,22),(2,0,1,23),(3,0,1,24);
/*!40000 ALTER TABLE `EscursionePagata` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EscursioneSalvata`
--

DROP TABLE IF EXISTS `EscursioneSalvata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EscursioneSalvata` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Nome` varchar(255) NOT NULL,
  `Data` date NOT NULL,
  `Costo` int(11) NOT NULL,
  `Luogo` varchar(255) NOT NULL,
  `Immagine` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EscursioneSalvata`
--

LOCK TABLES `EscursioneSalvata` WRITE;
/*!40000 ALTER TABLE `EscursioneSalvata` DISABLE KEYS */;
INSERT INTO `EscursioneSalvata` VALUES (18,'Partita Milan Palermo','2014-02-06',32,'Milano','Edefault.jpg'),(19,'Duomo di Milano','2014-02-13',12,'Milano','Edefault.jpg'),(20,'Toronto Symphony Orchestra','2014-04-15',26,'Toronto','toronto-symphony-orchestra.jpg'),(21,'Museo Reale','2014-04-07',13,'Toronto','royal-ontario-museum.jpg'),(22,'Sydney Harbour','2014-03-11',19,'Sydney','photo-of-sydney-harbour.jpg'),(23,'Wildlife Zoo','2014-03-17',38,'Sydney','sydney-wildlife-world.jpg'),(24,'Observatory','2014-03-13',32,'Sydney','sydney-observatory-observatory.jpg');
/*!40000 ALTER TABLE `EscursioneSalvata` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EscursioneSalvataViaggio`
--

DROP TABLE IF EXISTS `EscursioneSalvataViaggio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EscursioneSalvataViaggio` (
  `Viaggio_ID` int(11) NOT NULL,
  `EscursioneSalvata_ID` int(11) NOT NULL,
  PRIMARY KEY (`Viaggio_ID`,`EscursioneSalvata_ID`),
  KEY `fk_Viaggio_has_EscursioneSalvata_EscursioneSalvata1_idx` (`EscursioneSalvata_ID`),
  KEY `fk_Viaggio_has_EscursioneSalvata_Viaggio1_idx` (`Viaggio_ID`),
  CONSTRAINT `fk_Viaggio_has_EscursioneSalvata_EscursioneSalvata1` FOREIGN KEY (`EscursioneSalvata_ID`) REFERENCES `EscursioneSalvata` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Viaggio_has_EscursioneSalvata_Viaggio1` FOREIGN KEY (`Viaggio_ID`) REFERENCES `Viaggio` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EscursioneSalvataViaggio`
--

LOCK TABLES `EscursioneSalvataViaggio` WRITE;
/*!40000 ALTER TABLE `EscursioneSalvataViaggio` DISABLE KEYS */;
INSERT INTO `EscursioneSalvataViaggio` VALUES (57,18),(55,20),(55,21),(56,22),(56,23),(56,24);
/*!40000 ALTER TABLE `EscursioneSalvataViaggio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GiftList`
--

DROP TABLE IF EXISTS `GiftList`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GiftList` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `VoloAPag` tinyint(1) NOT NULL,
  `VoloRPag` tinyint(1) NOT NULL,
  `HotelPag` tinyint(1) NOT NULL,
  `Utente_Username` varchar(255) NOT NULL,
  `Viaggio_ID` int(11) NOT NULL,
  `Hash` varchar(255) NOT NULL,
  `Npersone` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_Gift List_Utente1_idx` (`Utente_Username`),
  KEY `fk_Gift List_Viaggio1_idx` (`Viaggio_ID`),
  CONSTRAINT `fk_Gift List_Utente1` FOREIGN KEY (`Utente_Username`) REFERENCES `Utente` (`Username`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Gift List_Viaggio1` FOREIGN KEY (`Viaggio_ID`) REFERENCES `Viaggio` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GiftList`
--

LOCK TABLES `GiftList` WRITE;
/*!40000 ALTER TABLE `GiftList` DISABLE KEYS */;
INSERT INTO `GiftList` VALUES (1,0,0,0,'sergioferrari',56,'7b74f89674d9ac90431f68548c55286e',3);
/*!40000 ALTER TABLE `GiftList` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Hotel`
--

DROP TABLE IF EXISTS `Hotel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Hotel` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Nome` varchar(255) NOT NULL,
  `Luogo` varchar(255) NOT NULL,
  `Data inizio` date NOT NULL,
  `Data fine` date NOT NULL,
  `Costo giornaliero` int(11) NOT NULL,
  `Immagine` varchar(255) DEFAULT NULL,
  `Stelle` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Hotel`
--

LOCK TABLES `Hotel` WRITE;
/*!40000 ALTER TABLE `Hotel` DISABLE KEYS */;
INSERT INTO `Hotel` VALUES (24,'The Westin Palace','Milano','2014-02-01','2014-02-28',355,'7794_66_z.jpg',5),(25,'Hotel Terminal','Milano','2014-02-01','2014-02-28',65,'917892_18_z.jpg',3),(26,'Antares Hotel','Milano','2014-02-01','2014-02-28',94,'583511_87_z.jpg',3),(27,'Drake Hotel','Toronto','2014-04-01','2014-04-30',89,'drake.jpeg',4),(28,'Victoria','Toronto','2014-04-05','2014-04-26',82,'victoria.jpeg',3),(29,'Holiday Inn','Toronto','2014-04-01','2014-04-22',62,'inn.jpeg',2),(30,'Chinatown Travelers','Toronto','2014-04-01','2014-04-30',26,'china.jpeg',1),(31,'The Grace Sydney','Sydney','2014-03-01','2014-03-29',154,'grace.jpeg',4),(32,'Novotel','Sydney','2014-03-01','2014-03-29',96,'novo.jpeg',3);
/*!40000 ALTER TABLE `Hotel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `HotelPacchetto`
--

DROP TABLE IF EXISTS `HotelPacchetto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `HotelPacchetto` (
  `Hotel_ID` int(11) NOT NULL,
  `Pacchetto_ID` int(11) NOT NULL,
  PRIMARY KEY (`Hotel_ID`,`Pacchetto_ID`),
  KEY `fk_Hotel_has_Pacchetto_Pacchetto1_idx` (`Pacchetto_ID`),
  KEY `fk_Hotel_has_Pacchetto_Hotel1_idx` (`Hotel_ID`),
  CONSTRAINT `fk_Hotel_has_Pacchetto_Hotel1` FOREIGN KEY (`Hotel_ID`) REFERENCES `Hotel` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Hotel_has_Pacchetto_Pacchetto1` FOREIGN KEY (`Pacchetto_ID`) REFERENCES `Pacchetto` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `HotelPacchetto`
--

LOCK TABLES `HotelPacchetto` WRITE;
/*!40000 ALTER TABLE `HotelPacchetto` DISABLE KEYS */;
INSERT INTO `HotelPacchetto` VALUES (24,3),(25,3),(26,3),(27,4),(30,4),(31,5),(32,5);
/*!40000 ALTER TABLE `HotelPacchetto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `HotelSalvato`
--

DROP TABLE IF EXISTS `HotelSalvato`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `HotelSalvato` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Nome` varchar(255) NOT NULL,
  `Luogo` varchar(255) NOT NULL,
  `Data inizio` date NOT NULL,
  `Data fine` date NOT NULL,
  `Costo giornaliero` int(11) NOT NULL,
  `Immagine` varchar(255) DEFAULT NULL,
  `Stelle` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `HotelSalvato`
--

LOCK TABLES `HotelSalvato` WRITE;
/*!40000 ALTER TABLE `HotelSalvato` DISABLE KEYS */;
INSERT INTO `HotelSalvato` VALUES (23,'The Westin Palace','Milano','2014-02-01','2014-02-28',355,'7794_66_z.jpg',5),(24,'Antares Hotel','Milano','2014-02-01','2014-02-28',94,'583511_87_z.jpg',3),(25,'Drake Hotel','Toronto','2014-04-01','2014-04-30',89,'drake.jpeg',4),(26,'The Grace Sydney','Sydney','2014-03-01','2014-03-29',154,'grace.jpeg',4),(27,'Hotel Terminal','Milano','2014-02-01','2014-02-28',65,'917892_18_z.jpg',3);
/*!40000 ALTER TABLE `HotelSalvato` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Invito`
--

DROP TABLE IF EXISTS `Invito`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Invito` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Status` tinyint(1) NOT NULL,
  `Amico` varchar(255) NOT NULL,
  `Utente` varchar(255) NOT NULL,
  `Viaggio` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `Utente_idx` (`Utente`),
  KEY `Viaggio_idx` (`Viaggio`),
  CONSTRAINT `UtenteInvito` FOREIGN KEY (`Utente`) REFERENCES `Utente` (`Username`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `ViaggioInvito` FOREIGN KEY (`Viaggio`) REFERENCES `Viaggio` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Invito`
--

LOCK TABLES `Invito` WRITE;
/*!40000 ALTER TABLE `Invito` DISABLE KEYS */;
INSERT INTO `Invito` VALUES (1,0,'proca@a.it','sergioferrari',57);
/*!40000 ALTER TABLE `Invito` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Pacchetto`
--

DROP TABLE IF EXISTS `Pacchetto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Pacchetto` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Nome` varchar(255) NOT NULL,
  `Destinazione` varchar(255) NOT NULL,
  `Data inizio` date NOT NULL,
  `Data fine` date NOT NULL,
  `Immagine` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Pacchetto`
--

LOCK TABLES `Pacchetto` WRITE;
/*!40000 ALTER TABLE `Pacchetto` DISABLE KEYS */;
INSERT INTO `Pacchetto` VALUES (3,'Milano da vivere','Milano','2014-02-01','2014-02-28','876MilanoDuomo.JPG'),(4,'Toronto Dream','Toronto','2014-04-01','2014-04-30','toronto.jpeg'),(5,'Sydeny Adventure','Sydney','2014-03-01','2014-03-29','sydney-opera-house-482x298.jpg');
/*!40000 ALTER TABLE `Pacchetto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Prenotazione`
--

DROP TABLE IF EXISTS `Prenotazione`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Prenotazione` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Numero persone` int(11) NOT NULL,
  `Costo` int(11) NOT NULL,
  `Utente` varchar(255) NOT NULL,
  `Viaggio` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `Utente_idx` (`Utente`),
  KEY `Viaggio_idx` (`Viaggio`),
  CONSTRAINT `UtentePrenotazione` FOREIGN KEY (`Utente`) REFERENCES `Utente` (`Username`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `ViaggioPrenotazione` FOREIGN KEY (`Viaggio`) REFERENCES `Viaggio` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Prenotazione`
--

LOCK TABLES `Prenotazione` WRITE;
/*!40000 ALTER TABLE `Prenotazione` DISABLE KEYS */;
INSERT INTO `Prenotazione` VALUES (10,2,20012,'m',51),(11,2,6332,'sergioferrari',55);
/*!40000 ALTER TABLE `Prenotazione` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Utente`
--

DROP TABLE IF EXISTS `Utente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Utente` (
  `Username` varchar(255) NOT NULL,
  `Password` varchar(128) NOT NULL,
  `Nome` varchar(255) NOT NULL,
  `Cognome` varchar(255) NOT NULL,
  `Data di nascita` date NOT NULL,
  `Email` varchar(255) NOT NULL,
  PRIMARY KEY (`Username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Utente`
--

LOCK TABLES `Utente` WRITE;
/*!40000 ALTER TABLE `Utente` DISABLE KEYS */;
INSERT INTO `Utente` VALUES ('admin','c7ad44cbad762a5da0a452f9e854fdc1e0e7a52a38015f23f3eab1d80b931dd472634dfac71cd34ebc35d16ab7fb8a90c81f975113d6c7538dc69dd8de9077ec','Ian','Osterman','1991-11-11','man@g.it'),('carlox','b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86','Carlo','Brambilla','1993-01-20','carlo.bramba@hotmail.it'),('jackv','b42b50eb04d6b13ed148a47c93ca88b524bd194df7a5646e6d500c55809a6a3a043722c75e616a4d7d65feaf5ec7d7f7483d4df8e64aa444a3f2a525ba3873d3','Giacomo','Villa','1986-08-15','jack11@outlook.it'),('lucky89','79e80d55935fd12a1e3a1e821b6c146b680d3bfcfb9e6af61db9d6cdf937d26d13048e3013d83c4820de319b50e27cc92bfb06ce5eda0ffcea75cfa0b09689f9','Luciano','Colombo','1989-04-01','luckyluciano@gmail.com'),('m','f14aae6a0e050b74e4b7b9a5b2ef1a60ceccbbca39b132ae3e8bf88d3a946c6d8687f3266fd2b626419d8b67dcf1d8d7c0fe72d4919d9bd05efbd37070cfb41a','Andrea','Motta','2014-01-14','m@m.it'),('robros','d404559f602eab6fd602ac7680dacbfaadd13630335e951f097af3900e9de176b6db28512f2e000b9d04fba5133e8b1c6e8df59db3a8ab9d60be4b97cc9e81db','Roberto','Rossi','1975-05-16','rr@gmail.com'),('sergioferrari','5061958799f5441d020aeeb652016d7350fea95002e16892bc18f1f6d9a1e1900d6e3e35bc2023ff09496519e6d789ad9bbfac43ef86aab949aab1ec2fa163cc','Sergio','Ferrari','1957-03-03','sergio.ferrari1@alice.it'),('z','5ae625665f3e0bd0a065ed07a41989e4025b79d13930a2a8c57d6b4325226707d956a082d1e91b4d96a793562df98fd03c9dcf743c9c7b4e3055d4f9f09ba015','Diego','DeLaVega','2014-01-14','zdizorro@z.it');
/*!40000 ALTER TABLE `Utente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `UtenteGruppo`
--

DROP TABLE IF EXISTS `UtenteGruppo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UtenteGruppo` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Username` varchar(255) NOT NULL,
  `Gruppo` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `UtenteGruppo_idx` (`Username`),
  CONSTRAINT `UtenteGruppo` FOREIGN KEY (`Username`) REFERENCES `Utente` (`Username`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `UtenteGruppo`
--

LOCK TABLES `UtenteGruppo` WRITE;
/*!40000 ALTER TABLE `UtenteGruppo` DISABLE KEYS */;
INSERT INTO `UtenteGruppo` VALUES (14,'admin','AMMINISTRATORE'),(16,'m','UTENTE'),(17,'z','UTENTE'),(18,'robros','IMPIEGATO'),(19,'carlox','IMPIEGATO'),(20,'jackv','IMPIEGATO'),(21,'lucky89','UTENTE'),(22,'sergioferrari','UTENTE');
/*!40000 ALTER TABLE `UtenteGruppo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Viaggio`
--

DROP TABLE IF EXISTS `Viaggio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Viaggio` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Data inizio` date NOT NULL,
  `Data fine` date NOT NULL,
  `HotelSalvato_ID` int(11) NOT NULL,
  `VoloSalvatoAndata_ID` int(11) NOT NULL,
  `VoloSalvatoRitorno_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_Viaggio_HotelSalvato1_idx` (`HotelSalvato_ID`),
  KEY `fk_Viaggio_VoloSalvato1_idx` (`VoloSalvatoAndata_ID`),
  KEY `fk_Viaggio_VoloSalvato2_idx` (`VoloSalvatoRitorno_ID`),
  CONSTRAINT `fk_Viaggio_HotelSalvato1` FOREIGN KEY (`HotelSalvato_ID`) REFERENCES `HotelSalvato` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Viaggio_VoloSalvato1` FOREIGN KEY (`VoloSalvatoAndata_ID`) REFERENCES `VoloSalvato` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Viaggio_VoloSalvato2` FOREIGN KEY (`VoloSalvatoRitorno_ID`) REFERENCES `VoloSalvato` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Viaggio`
--

LOCK TABLES `Viaggio` WRITE;
/*!40000 ALTER TABLE `Viaggio` DISABLE KEYS */;
INSERT INTO `Viaggio` VALUES (51,'2014-02-01','2014-02-28',23,51,52),(52,'2014-02-01','2014-02-28',23,51,52),(53,'2014-02-01','2014-02-28',23,51,52),(54,'2014-02-01','2014-02-28',24,51,52),(55,'2014-04-01','2014-04-30',25,53,54),(56,'2014-03-01','2014-03-29',26,55,56),(57,'2014-02-01','2014-02-28',27,51,52);
/*!40000 ALTER TABLE `Viaggio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Volo`
--

DROP TABLE IF EXISTS `Volo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Volo` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Luogo partenza` varchar(255) NOT NULL,
  `Luogo arrivo` varchar(255) NOT NULL,
  `Data` date NOT NULL,
  `Costo` int(11) NOT NULL,
  `Compagnia` varchar(255) NOT NULL,
  `Immagine` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Volo`
--

LOCK TABLES `Volo` WRITE;
/*!40000 ALTER TABLE `Volo` DISABLE KEYS */;
INSERT INTO `Volo` VALUES (7,'Palermo','Milano','2014-02-01',16,'Alitalia','Vdefault.jpg'),(8,'Milano','Palermo','2014-02-28',18,'RyanAir','Vdefault.jpg'),(9,'Milano','Toronto','2014-04-01',235,'Emirates','emir.png'),(10,'Bologna','Toronto','2014-04-05',239,'Emirates','emir.png'),(11,'Palermo','Toronto','2014-04-02',349,'Lufthansa','luft.png'),(12,'Toronto','Milano','2014-04-30',222,'Emirates','emir.png'),(13,'Toronto','Bologna','2014-04-26',210,'Lufthansa','luft.png'),(14,'Toronto','Palermo','2014-04-26',475,'Air Canada','cana.png'),(15,'Torino','Sydney','2014-03-01',433,'Turkish Airlines','turk.png'),(16,'Sydney','Torino','2014-03-29',442,'Turkish Airlines','turk.png'),(17,'Verona','Sydney','2014-03-09',232,'Emirates','emir.png'),(18,'Sydney','Verona','2014-03-29',211,'Turkish Airlines','turk.png');
/*!40000 ALTER TABLE `Volo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `VoloPacchetto`
--

DROP TABLE IF EXISTS `VoloPacchetto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `VoloPacchetto` (
  `Voli_ID` int(11) NOT NULL,
  `Pacchetto_ID` int(11) NOT NULL,
  PRIMARY KEY (`Voli_ID`,`Pacchetto_ID`),
  KEY `fk_Voli_has_Pacchetto_Pacchetto1_idx` (`Pacchetto_ID`),
  KEY `fk_Voli_has_Pacchetto_Voli1_idx` (`Voli_ID`),
  CONSTRAINT `fk_Voli_has_Pacchetto_Pacchetto1` FOREIGN KEY (`Pacchetto_ID`) REFERENCES `Pacchetto` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Voli_has_Pacchetto_Voli1` FOREIGN KEY (`Voli_ID`) REFERENCES `Volo` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `VoloPacchetto`
--

LOCK TABLES `VoloPacchetto` WRITE;
/*!40000 ALTER TABLE `VoloPacchetto` DISABLE KEYS */;
INSERT INTO `VoloPacchetto` VALUES (7,3),(8,3),(9,4),(10,4),(11,4),(12,4),(13,4),(14,4),(15,5),(16,5),(17,5),(18,5);
/*!40000 ALTER TABLE `VoloPacchetto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `VoloSalvato`
--

DROP TABLE IF EXISTS `VoloSalvato`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `VoloSalvato` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Luogo partenza` varchar(255) NOT NULL,
  `Luogo arrivo` varchar(255) NOT NULL,
  `Data` date NOT NULL,
  `Costo` int(11) NOT NULL,
  `Compagnia` varchar(255) NOT NULL,
  `Immagine` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `VoloSalvato`
--

LOCK TABLES `VoloSalvato` WRITE;
/*!40000 ALTER TABLE `VoloSalvato` DISABLE KEYS */;
INSERT INTO `VoloSalvato` VALUES (51,'Palermo','Milano','2014-02-01',16,'Alitalia','Vdefault.jpg'),(52,'Milano','Palermo','2014-02-28',18,'RyanAir','Vdefault.jpg'),(53,'Milano','Toronto','2014-04-01',235,'Emirates','emir.png'),(54,'Toronto','Milano','2014-04-30',222,'Emirates','emir.png'),(55,'Torino','Sydney','2014-03-01',433,'Turkish Airlines','turk.png'),(56,'Sydney','Torino','2014-03-29',442,'Turkish Airlines','turk.png');
/*!40000 ALTER TABLE `VoloSalvato` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-01-31 18:25:17
