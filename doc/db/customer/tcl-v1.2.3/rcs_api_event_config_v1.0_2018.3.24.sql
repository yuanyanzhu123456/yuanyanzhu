# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.6.38)
# Database: rcs
# Generation Time: 2018-03-24 10:12:59 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table api_event_entry
# ------------------------------------------------------------

DROP TABLE IF EXISTS `api_event_entry`;

CREATE TABLE `api_event_entry` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '进件id（主键）',
  `channel` varchar(50) DEFAULT NULL COMMENT '访问渠道（Web、Android、ios...）',
  `business_id` bigint(20) DEFAULT NULL COMMENT '业务类型id',
  `sence_id` bigint(20) DEFAULT NULL COMMENT '场景id',
  `rules_id` bigint(20) DEFAULT NULL COMMENT '规则集id',
  `rules_name` varchar(100) DEFAULT NULL COMMENT '规则集name',
  `user_id` bigint(20) DEFAULT NULL COMMENT '账户id',
  `nickname` varchar(50) DEFAULT NULL COMMENT '账户name(昵称)',
  `user_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `id_card` varchar(50) DEFAULT NULL COMMENT '身份证号',
  `acct_no` varchar(50) DEFAULT NULL COMMENT '银行卡号',
  `mobile` varchar(50) DEFAULT NULL COMMENT '手机号',
  `approver_id` bigint(20) DEFAULT NULL COMMENT '审核人id',
  `approver_name` varchar(50) DEFAULT NULL COMMENT '审核人name',
  `sys_approval_time` datetime DEFAULT NULL COMMENT '系统审核日期',
  `man_approval_time` datetime DEFAULT NULL COMMENT '人工审核日期',
  `sys_status` tinyint(4) DEFAULT '0' COMMENT '系统审核结果(0:"-",1:"通过",2:"人工审核",3:"拒绝",4:"失效")',
  `man_status` tinyint(4) DEFAULT '0' COMMENT '人工审核结果(0:"-",1:"通过",2:"拒绝")',
  `status` tinyint(4) DEFAULT '0' COMMENT '审核状态(0:"未审核",1:"已审核")',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `result_map` text COMMENT '结果数据',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `province` varchar(10) DEFAULT NULL COMMENT '省份',
  `type` int(11) DEFAULT '0' COMMENT '1测试or0正常进件',
  `city` varchar(20) DEFAULT NULL COMMENT '市区',
  `isp` varchar(10) DEFAULT NULL COMMENT '运营商',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




# Dump of table api_user_token
# ------------------------------------------------------------

DROP TABLE IF EXISTS `api_user_token`;

CREATE TABLE `api_user_token` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(11) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `expire_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
