# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.6.38)
# Database: rcs
# Generation Time: 2018-03-24 10:07:04 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table geo_role_menu
# ------------------------------------------------------------

DROP TABLE IF EXISTS `geo_role_menu`;

CREATE TABLE `geo_role_menu` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `geo_id` bigint(20) DEFAULT NULL,
  `menu_id` bigint(20) DEFAULT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `geo_role_menu` WRITE;
/*!40000 ALTER TABLE `geo_role_menu` DISABLE KEYS */;

INSERT INTO `geo_role_menu` (`id`, `geo_id`, `menu_id`, `role_id`)
VALUES
	(99,1,18,5),
	(100,1,66,5),
	(101,1,21,5),
	(102,1,61,5),
	(103,1,62,5),
	(104,1,63,5),
	(105,1,67,5),
	(106,1,75,5),
	(107,1,76,5),
	(108,1,77,5),
	(109,1,78,5),
	(110,1,68,5),
	(111,1,73,5),
	(112,1,74,5),
	(113,1,71,5),
	(114,1,60,5),
	(115,1,64,5),
	(116,1,65,5),
	(117,1,72,5),
	(118,1,26,5),
	(119,1,27,5),
	(120,1,28,5),
	(165,1,18,1),
	(166,1,66,1),
	(167,1,21,1),
	(168,1,61,1),
	(169,1,62,1),
	(170,1,63,1),
	(171,1,67,1),
	(172,1,75,1),
	(173,1,76,1),
	(174,1,77,1),
	(175,1,78,1),
	(176,1,68,1),
	(177,1,73,1),
	(178,1,74,1),
	(179,1,71,1),
	(180,1,60,1),
	(181,1,64,1),
	(182,1,65,1),
	(183,1,72,1),
	(184,1,26,1),
	(185,1,27,1),
	(186,1,28,1),
	(187,1,18,6),
	(188,1,85,1),
	(189,1,84,1);
	
/*!40000 ALTER TABLE `geo_role_menu` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table geo_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `geo_user`;

CREATE TABLE `geo_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '员工管理ID',
  `contact` varchar(255) DEFAULT NULL COMMENT '联系人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creater` varchar(255) DEFAULT NULL COMMENT '创建人',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `mobilephone` varchar(255) DEFAULT NULL COMMENT '手机号',
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `role_id` int(11) DEFAULT NULL COMMENT '角色ID',
  `salt` varchar(255) DEFAULT NULL COMMENT '盐',
  `status` int(11) DEFAULT '1' COMMENT '员工状态（1，正常 -默认正常）',
  `username` varchar(255) DEFAULT NULL COMMENT '登录用户名',
  `login_ip` varchar(50) DEFAULT NULL COMMENT '登录IP',
  `login_date` date DEFAULT NULL COMMENT '登录日期',
  `role_type` varchar(50) DEFAULT NULL COMMENT '角色类型',
  `unique_code` bigint(20) DEFAULT NULL COMMENT '客户标识',
  `company` varchar(50) DEFAULT NULL COMMENT '公司名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `geo_user` WRITE;
/*!40000 ALTER TABLE `geo_user` DISABLE KEYS */;

INSERT INTO `geo_user` (`id`, `contact`, `create_time`, `creater`, `email`, `mobilephone`, `name`, `password`, `role_id`, `salt`, `status`, `username`, `login_ip`, `login_date`, `role_type`, `unique_code`, `company`)
VALUES
  (1,'上海TCL金控','2018-04-20 10:54:00','上海TCL金控','www.tcl.com','','上海TCL金控','013267422ded9e24219b83c1ec9b6caf',1,NULL,1,'superuser',NULL,NULL,NULL,1,'上海TCL金控');

/*!40000 ALTER TABLE `geo_user` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table geo_user_role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `geo_user_role`;

CREATE TABLE `geo_user_role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `geo_id` bigint(20) DEFAULT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `geo_user_role` WRITE;
/*!40000 ALTER TABLE `geo_user_role` DISABLE KEYS */;

INSERT INTO `geo_user_role` (`id`, `geo_id`, `role_id`)
VALUES
	(1,1,1),
	(2,2,5),
	(3,3,5),
	(4,4,5),
	(5,5,1),
	(6,6,7),
	(7,7,6),
	(8,8,5),
	(9,9,5);

/*!40000 ALTER TABLE `geo_user_role` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table geo_user_token
# ------------------------------------------------------------

DROP TABLE IF EXISTS `geo_user_token`;

CREATE TABLE `geo_user_token` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `geo_id` bigint(20) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `expire_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `geo_user_token` WRITE;
/*!40000 ALTER TABLE `geo_user_token` DISABLE KEYS */;
/*!40000 ALTER TABLE `geo_user_token` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
