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
	(187,1,18,6),
	(340,1,18,1),
	(341,1,66,1),
	(342,1,67,1),
	(343,1,73,1),
	(344,1,74,1),
	(345,1,68,1),
	(346,1,75,1),
	(347,1,76,1),
	(348,1,77,1),
	(349,1,78,1),
	(350,1,71,1),
	(351,1,60,1),
	(352,1,64,1),
	(353,1,65,1),
	(354,1,72,1),
	(355,1,26,1),
	(356,1,27,1),
	(357,1,28,1),
	(358,1,84,1),
	(359,1,93,1),
	(360,1,94,1),
	(361,1,95,1),
	(362,1,96,1),
	(363,1,97,1),
	(364,1,98,1),
	(365,1,99,1),
	(366,1,100,1),
	(367,1,101,1),
	(368,1,102,1),
	(369,1,105,1),
	(370,1,106,1),
	(371,1,107,1),
	(372,1,108,1),
	(373,1,109,1),
	(374,1,110,1),
	(375,1,111,1),
	(376,1,112,1),
	(377,1,85,1),
	(378,1,91,1),
	(379,1,140,1),
	(380,1,141,1),
	(381,1,142,1),
	(382,1,143,1),
	(383,1,87,1),
	(384,1,21,1),
	(385,1,61,1),
	(386,1,62,1),
	(387,1,63,1),
	(388,1,89,1),
	(389,1,151,1);

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
	(1,'北京集奥聚合','2018-02-27 10:54:00','北京集奥聚合有限公司','www.geotmt.com','','北京集奥聚合','dd945be88c20e3d859babe42eef51bf9',1,NULL,1,'superadmin',NULL,NULL,NULL,1,'北京集奥聚合');

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

INSERT INTO `geo_user_token` (`id`, `geo_id`, `token`, `update_time`, `expire_time`)
VALUES
	(1,1,'28e389f41a4d8bc5db2c6172fd25370d','2018-03-21 15:01:15','2018-03-22 15:01:15'),
	(2,4,'f951ff95f3a110842552ad6062d0d293','2018-03-14 18:27:23','2018-03-15 18:27:23'),
	(3,2,'ea4e4a057b02ecc0cdd62e09be665853','2018-03-13 18:41:17','2018-03-14 18:41:17'),
	(4,3,'f771773149e7a5185cd7c4bf41f1f7e1','2018-03-14 11:42:23','2018-03-15 11:42:23'),
	(5,6,'f887759d46d42492113a04ab1bd50f03','2018-03-20 10:35:29','2018-03-21 10:35:29'),
	(6,7,'f70a1caa137bb4eea690498ac47cb9f2','2018-03-20 10:41:30','2018-03-21 10:41:30'),
	(7,8,'cf167b24aee782e00a02a1a1df0b49e2','2018-03-20 10:42:54','2018-03-21 10:42:54');

/*!40000 ALTER TABLE `geo_user_token` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
