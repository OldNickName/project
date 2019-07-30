-- --------------------------------------------------------
-- Хост:                         127.0.0.1
-- Версия сервера:               8.0.11 - MySQL Community Server - GPL
-- Операционная система:         Win64
-- HeidiSQL Версия:              10.2.0.5610
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Дамп структуры базы данных currencies_spots
CREATE DATABASE IF NOT EXISTS `currencies_spots`;
USE `currencies_spots`;

-- Дамп структуры для таблица currencies_spots.spots
CREATE TABLE IF NOT EXISTS `spots` (
  `Currency` varchar(50) NOT NULL,
  `Spot` double DEFAULT NULL,
  PRIMARY KEY (`Currency`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
