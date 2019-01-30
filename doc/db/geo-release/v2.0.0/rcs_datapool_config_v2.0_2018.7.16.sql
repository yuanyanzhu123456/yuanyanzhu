# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.19)
# Database: rcs2
# Generation Time: 2018-07-16 03:32:11 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table datapool_person
# ------------------------------------------------------------

DROP TABLE IF EXISTS `datapool_person`;

CREATE TABLE `datapool_person` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '存储ID',
  `rid` varchar(32) NOT NULL DEFAULT '' COMMENT '数据全局ID',
  `name` varchar(32) DEFAULT NULL COMMENT '姓名',
  `cid` int(11) DEFAULT NULL COMMENT '手机号',
  `id_number` varchar(32) DEFAULT NULL COMMENT '身份证号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `query_times` int(20) DEFAULT NULL COMMENT '查询次数',
  `update_times` int(20) DEFAULT NULL COMMENT '更新次数',
  `query_status` int(11) DEFAULT NULL COMMENT '查询状态',
  `update_status` int(11) DEFAULT NULL COMMENT '更新状态',
  `interval` int(11) DEFAULT NULL COMMENT '分发间隔',
  `min_interval` int(11) DEFAULT NULL COMMENT '最小分发间隔',
  `max_interval` int(11) DEFAULT NULL COMMENT '最大分发间隔',
  `status` int(11) DEFAULT NULL COMMENT '状态',
  `score` int(11) DEFAULT NULL COMMENT '分数',
  `trend` varchar(255) DEFAULT '' COMMENT '趋势数据',
  `recent_data` varchar(255) DEFAULT NULL COMMENT '最近更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
