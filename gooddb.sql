/*
SQLyog Ultimate v12.3.1 (64 bit)
MySQL - 5.5.28 : Database - goodsdb
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`goodsdb` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `goodsdb`;

/*Table structure for table `goods` */

DROP TABLE IF EXISTS `goods`;

CREATE TABLE `goods` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `goodsName` varchar(20) NOT NULL COMMENT '商品名称',
  `status` int(11) NOT NULL COMMENT '商品状态',
  `district` int(11) NOT NULL COMMENT '商品区域',
  `counts` int(11) NOT NULL COMMENT '库存数量',
  `price` decimal(3,2) DEFAULT NULL COMMENT '商品单价',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Data for the table `goods` */

insert  into `goods`(`id`,`goodsName`,`status`,`district`,`counts`,`price`) values 
(1,'衣服',2,1,20,1.00),
(2,'食品',1,1,20,2.00),
(3,'裤子',0,1,10,3.00),
(4,'衣服',2,1,20,1.00),
(5,'食品aaaaa',2,2,20,1.00),
(6,'裤子',1,0,10,1.00);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
