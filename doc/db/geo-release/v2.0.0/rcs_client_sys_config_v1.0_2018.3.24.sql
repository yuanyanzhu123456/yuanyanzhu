# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.6.38)
# Database: rcs
# Generation Time: 2018-04-08 10:26:43 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table sys_customer
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_customer`;

CREATE TABLE `sys_customer` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `server` varchar(255) DEFAULT NULL COMMENT '加密类型和加密秘钥向GEO索取 AES(秘钥长度不固定)、AES2(秘钥长度16)、DES(秘钥长度8)、DESede(秘钥长度24)、XOR(秘钥只能是数字)',
  `encrypted` int(11) DEFAULT NULL COMMENT '是否加密传输  1是0否',
  `encryption_type` varchar(50) DEFAULT NULL COMMENT '加密类型',
  `encryption_key` varchar(255) DEFAULT NULL COMMENT '加密秘钥',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `password` varchar(50) DEFAULT NULL COMMENT '密码',
  `uno` varchar(255) DEFAULT NULL COMMENT '合同号',
  `etype` varchar(11) DEFAULT NULL COMMENT '只能填写""!',
  `dsign` int(11) DEFAULT NULL COMMENT '是否进行数字签名 1是0否',
  `user_id` bigint(20) DEFAULT NULL COMMENT '客户编号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `sys_customer` WRITE;
/*!40000 ALTER TABLE `sys_customer` DISABLE KEYS */;

INSERT INTO `sys_customer` (`id`, `server`, `encrypted`, `encryption_type`, `encryption_key`, `username`, `password`, `uno`, `etype`, `dsign`, `user_id`)
VALUES
	(1,'https://yz.geotmt.com',1,'AES2','Geotmt@123asdfgh','geotmt','Geotmt@123','200959',NULL,1,1);

/*!40000 ALTER TABLE `sys_customer` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sys_login_log
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_login_log`;

CREATE TABLE `sys_login_log` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `logname` varchar(255) DEFAULT NULL,
  `userid` bigint(11) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `succeed` varchar(255) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


# Dump of table sys_menu
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_menu`;

CREATE TABLE `sys_menu` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单id',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父菜单id',
  `parent_name` varchar(20) DEFAULT NULL COMMENT '父菜单名称',
  `name` varchar(20) DEFAULT NULL COMMENT '名称',
  `url` varchar(255) DEFAULT NULL COMMENT '菜单url',
  `perms` varchar(255) DEFAULT NULL COMMENT '授权',
  `type` int(11) DEFAULT '1' COMMENT '类型',
  `open` tinyint(1) DEFAULT NULL COMMENT 'ztree属性',
  `front_icon` varchar(255) DEFAULT NULL COMMENT '前面的图标',
  `back_icon` varchar(255) DEFAULT NULL COMMENT '后面的图标',
  `order_id` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;

INSERT INTO `sys_menu` (`id`, `menu_id`, `parent_id`, `parent_name`, `name`, `url`, `perms`, `type`, `open`, `front_icon`, `back_icon`, `order_id`)
VALUES
	(1,1,0,'主页','主页','./index.html',NULL,1,1,'li-home',NULL,1),
	(2,2,148,'规则管理','规则管理','#','rule:list',1,1,'fa fa-qrcode','arrow fa fa-angle-right',4),
	(3,3,2,'规则管理','场景管理','./scene_management.html','rule:scene:list',1,1,NULL,NULL,NULL),
	(4,4,2,'规则管理','字段管理','./fields_market.html','rule:field:list',1,1,NULL,NULL,NULL),
	(5,5,2,'规则集管理','规则集管理','./rule_set.html','rule:ruleset:list',1,1,NULL,NULL,NULL),
	(6,6,0,'规则测试','规则测试','#',NULL,1,0,'fa fa-edit','arrow fa fa-angle-right',5),
	(7,7,6,'规则测试','标签管理','./label_management.html',NULL,1,0,NULL,NULL,NULL),
	(8,8,0,'名单管理','名单管理','#',NULL,1,0,'fa fa-file-text','arrow fa fa-angle-right',6),
	(9,9,8,'名单管理','黑名单管理','./black_list.html','ros:bow:list',1,0,NULL,NULL,NULL),
	(10,10,8,'名单管理','白名单管理','./white_list.html','ros:whe:list',1,0,NULL,NULL,NULL),
	(11,11,148,'审批管理','审批管理','#',NULL,1,1,'fa fa-history','arrow fa fa-angle-right',7),
	(12,12,11,'审批管理','待审批','./approval_pending.html','api:appr:list',1,1,NULL,NULL,NULL),
	(13,13,11,'审批管理','已审批','./approved.html','api:appr:list',1,1,NULL,NULL,NULL),
	(14,14,146,'进件管理','进件管理','#',NULL,1,1,'fa fa-inbox','arrow fa fa-angle-right',8),
	(15,15,14,'进件管理','通过事件','./pass_event.html',NULL,1,1,NULL,NULL,NULL),
	(16,16,14,'进件管理','拒绝事件','./reject_event.html',NULL,1,1,NULL,NULL,NULL),
	(17,17,14,'进件管理','人工审核事件','./human_review.html',NULL,1,1,NULL,NULL,NULL),
	(18,18,0,'事件统计','事件统计','./event_statistics.html','api:customerEvestat:list',0,1,'fa fa-bar-chart-o',NULL,1),
	(19,19,149,'系统管理','系统管理','#',NULL,1,1,'fa fa-cog','arrow fa fa-angle-right	',9),
	(20,20,19,'系统管理','用户管理','./staff_management.html','emp:list',1,1,NULL,NULL,NULL),
	(21,21,87,'客户管理','客户管理','./customer_account.html','sys:user:list',0,1,NULL,NULL,NULL),
	(22,22,19,'系统管理','角色管理','./role_management.html','sys:role:list',1,1,NULL,NULL,NULL),
	(23,23,3,'场景管理','添加场景',NULL,'rule:scene:save',1,1,NULL,NULL,NULL),
	(24,24,3,'场景管理','修改场景',NULL,'rule:scene:update',1,1,NULL,NULL,NULL),
	(25,25,3,'场景管理','删除场景',NULL,'rule:scene:delete',1,1,NULL,NULL,NULL),
	(26,26,72,'字段管理','添加字段',NULL,'rule:field:save',0,1,NULL,NULL,NULL),
	(27,27,72,'字段管理','修改字段',NULL,'rule:field:update',0,1,NULL,NULL,NULL),
	(28,28,72,'字段管理','删除字段',NULL,'rule:field:delete',0,1,NULL,NULL,NULL),
	(29,29,5,'规则集管理','添加规则集',NULL,'rule:ruleset:save',1,1,NULL,NULL,NULL),
	(30,30,5,'规则集管理','修改规则集',NULL,'rule:ruleset:update',1,1,NULL,NULL,NULL),
	(31,31,5,'规则集管理','删除规则集',NULL,'rule:ruleset:delete',1,1,NULL,NULL,NULL),
	(32,32,5,'规则管理','添加规则',NULL,'rule:save',1,1,NULL,NULL,NULL),
	(33,33,5,'规则管理','修改规则',NULL,'rule:update',1,1,NULL,NULL,NULL),
	(34,34,5,'规则管理','删除规则',NULL,'rule:delete',1,1,NULL,NULL,NULL),
	(35,35,5,'规则管理','条件管理',NULL,'rule:condition:list',1,1,NULL,NULL,NULL),
	(36,36,35,'条件管理','添加条件',NULL,'rule:condition:save',1,1,NULL,NULL,NULL),
	(37,37,35,'条件管理','修改条件',NULL,'rule:condition:update',1,1,NULL,NULL,NULL),
	(38,38,35,'条件管理','删除条件',NULL,'rule:condition:delete',1,1,NULL,NULL,NULL),
	(39,39,7,'标签管理','删除标签',NULL,NULL,1,0,NULL,NULL,NULL),
	(40,40,7,'标签管理','导入',NULL,NULL,1,0,NULL,NULL,NULL),
	(41,41,59,'规则测试','删除',NULL,NULL,1,0,NULL,NULL,NULL),
	(42,42,9,'名单库管理','添加',NULL,'ros:bow:save',1,0,NULL,NULL,NULL),
	(43,43,9,'名单库管理','删除',NULL,'ros:bow:delete',1,0,NULL,NULL,NULL),
	(44,44,9,'名单库管理','导入',NULL,'ros:bow:import',1,0,NULL,NULL,NULL),
	(45,45,9,'名单库管理','导出',NULL,'ros:bow:export',1,0,NULL,NULL,NULL),
	(46,46,12,'待审批管理','审批',NULL,'api:appr:accraditation',1,1,NULL,NULL,NULL),
	(47,47,12,'已审批管理','查看',NULL,'api:appr:view',1,1,NULL,NULL,NULL),
	(48,48,15,'进件管理','查看报告',NULL,NULL,1,1,NULL,NULL,NULL),
	(49,49,15,'进件管理','下载报告',NULL,NULL,1,1,NULL,NULL,NULL),
	(50,50,17,'人工审核事件','审批',NULL,NULL,1,1,NULL,NULL,NULL),
	(51,51,20,'用户管理','添加',NULL,'emp:save',1,1,NULL,NULL,NULL),
	(52,52,20,'用户管理','修改',NULL,'emp:update',1,1,NULL,NULL,NULL),
	(53,53,20,'用户管理','启用/禁用',NULL,'emp:update',1,1,NULL,NULL,NULL),
	(54,54,22,'角色管理','添加角色',NULL,'sys:role:save',1,1,NULL,NULL,NULL),
	(55,55,22,'角色管理','修改角色',NULL,'sys:role:update',1,1,NULL,NULL,NULL),
	(56,56,22,'角色管理','启用/禁用',NULL,'sys:role:update',1,1,NULL,NULL,NULL),
	(57,57,22,'角色管理','配置权限',NULL,'sys:role:updatePermission',1,1,NULL,NULL,NULL),
	(59,59,6,'规则测试','规则测试','./rule_test.html',NULL,1,0,NULL,NULL,NULL),
	(60,60,71,'数据管理','接口管理','./interface_management.html','rule:inter:list',0,1,NULL,NULL,NULL),
	(61,61,21,'客户账号管理','添加客户权限',NULL,'sys:user:save',0,1,NULL,NULL,NULL),
	(62,62,21,'客户账号管理','删除客户权限',NULL,'sys:user:delete',0,1,NULL,NULL,NULL),
	(63,63,21,'客户账号管理','修改客户权限',NULL,'sys:user:update',0,1,NULL,NULL,NULL),
	(64,64,60,'接口管理','添加接口权限',NULL,'rule:inter:save',0,1,NULL,NULL,NULL),
	(65,65,60,'接口管理','修改接口权限',NULL,'rule:inter:update',0,1,NULL,NULL,NULL),
	(66,66,0,'系统管理','系统管理','#',NULL,0,1,'fa fa-cog','arrow fa fa-angle-right',4),
	(67,67,66,'系统管理','用户管理','./staff_management.html','emp:list',0,1,NULL,NULL,NULL),
	(68,68,66,'系统管理','角色管理','./role_management.html','sys:role:list',0,1,NULL,NULL,NULL),
	(71,71,0,'数据管理','数据管理','#','rule:list',0,1,'fa fa-qrcode','arrow fa fa-angle-right',3),
	(72,72,71,'数据管理','字段管理','./fields_market.html','rule:field:list',0,1,NULL,NULL,NULL),
	(73,73,67,'用户管理','添加用户',NULL,'emp:save',0,1,NULL,NULL,NULL),
	(74,74,67,'用户管理','修改用户',NULL,'emp:update',0,1,NULL,NULL,NULL),
	(75,75,68,'权限管理','添加角色',NULL,'sys:role:save',0,1,NULL,NULL,NULL),
	(76,76,68,'权限管理','添加角色',NULL,'sys:role:update',0,1,NULL,NULL,NULL),
	(77,77,68,'权限管理','启用/禁用',NULL,'sys:role:update',0,1,NULL,NULL,NULL),
	(78,78,68,'权限管理','配置权限',NULL,'sys:role:updatePermission',0,1,NULL,NULL,NULL),
	(79,79,10,'白名单管理','添加',NULL,'ros:bow:save',1,0,NULL,NULL,NULL),
	(80,80,10,'白名单管理','删除',NULL,'ros:bow:delete',1,0,NULL,NULL,NULL),
	(81,81,10,'白名单管理','导入',NULL,'ros:bow:import',1,0,NULL,NULL,NULL),
	(82,82,10,'白名单管理','导出',NULL,'ros:bow:export',1,0,NULL,NULL,NULL),
	(83,83,2,'规则管理','规则模版','./rule_set_template.html','rule:template:list',1,1,NULL,NULL,NULL),
	(84,84,71,'数据管理','规则集管理','./rules_management.html','rule:ruleset:list',0,1,NULL,NULL,NULL),
	(85,85,71,'数据管理','模板管理','./template_management.html','rule:template:list',0,1,NULL,NULL,NULL),
	(86,86,146,'进件统计','进件统计','./event_statistics.html','event:stat:save,event:stat:list',1,1,'fa fa-bar-chart-o',NULL,3),
	(87,87,0,'客户管理','客户管理','#',NULL,0,1,'fa fa-user','arrow fa fa-angle-right',2),
	(89,89,87,'客户管理','客户统计','./customer_statistics.html',NULL,0,1,NULL,NULL,NULL),
	(91,91,71,'数据管理','目录管理','./menu_management.html','operate:menu:list',0,1,NULL,NULL,NULL),
	(93,93,84,'规则集管理','添加规则集',NULL,'rule:ruleset:save',0,1,NULL,NULL,NULL),
	(94,94,84,'规则集管理','修改规则集',NULL,'rule:ruleset:update',0,1,NULL,NULL,NULL),
	(95,95,84,'规则集管理','删除规则集',NULL,'rule:ruleset:delete',0,1,NULL,NULL,NULL),
	(96,96,84,'规则集管理','添加规则',NULL,'rule:save',0,1,NULL,NULL,NULL),
	(97,97,84,'规则集管理','修改规则',NULL,'rule:update',0,1,NULL,NULL,NULL),
	(98,98,84,'规则集管理','删除规则',NULL,'rule:delete',0,1,NULL,NULL,NULL),
	(99,99,84,'规则集管理','条件管理',NULL,'rule:condition:list',0,1,NULL,NULL,NULL),
	(100,100,84,'条件管理','添加条件',NULL,'rule:condition:save',0,1,NULL,NULL,NULL),
	(101,101,84,'条件管理','修改条件',NULL,'rule:condition:update',0,1,NULL,NULL,NULL),
	(102,102,84,'条件管理','删除条件',NULL,'rule:condition:delete',0,1,NULL,NULL,NULL),
	(105,105,84,'规则集管理','删除规则集',NULL,'rule:ruleset:delete',0,1,NULL,NULL,NULL),
	(106,106,84,'规则集管理','添加规则',NULL,'rule:save',0,1,NULL,NULL,NULL),
	(107,107,84,'规则集管理','修改规则',NULL,'rule:update',0,1,NULL,NULL,NULL),
	(108,108,84,'规则集管理','删除规则',NULL,'rule:delete',0,1,NULL,NULL,NULL),
	(109,109,84,'规则集管理','条件管理',NULL,'rule:condition:list',0,1,NULL,NULL,NULL),
	(110,110,109,'条件管理','添加条件',NULL,'rule:condition:save',0,1,NULL,NULL,NULL),
	(111,111,109,'条件管理','修改条件',NULL,'rule:condition:update',0,1,NULL,NULL,NULL),
	(112,112,109,'条件管理','删除条件',NULL,'rule:condition:delete',0,1,NULL,NULL,NULL),
	(140,140,91,'目录管理','添加',NULL,'operate:menu:save',0,1,NULL,NULL,NULL),
	(141,141,91,'目录管理','查询',NULL,'operate:menu:list',0,1,NULL,NULL,NULL),
	(142,142,91,'目录管理','修改',NULL,'operate:menu:update',0,1,NULL,NULL,NULL),
	(143,143,91,'目录管理','删除',NULL,'operate:menu:delete',0,1,NULL,NULL,NULL),
	(144,144,146,'风险识别','在线识别','./event_online.html',NULL,1,1,'fa fa-wpexplorer',NULL,2),
	(145,145,1,'主页','主页','./index.html','index:list',1,1,'fa fa-home',NULL,1),
	(146,146,0,'风险识别','风险识别','./event_online.html',NULL,1,1,'li-recognize',NULL,2),
	(147,147,0,'决策引擎','决策引擎','sbshiqiang',NULL,1,0,'li-flow',NULL,2),
	(148,148,0,'规则引擎','规则引擎','./rule_set.html','rule:ruleset:list',1,1,'li-models',NULL,4),
	(149,149,0,'系统管理','系统管理','./staff_management.html','emp:list',1,1,'li-settings',NULL,5),
	(150,150,146,'风险识别','个人识别','./event_online_single.html','',1,1,'fa fa-crosshairs','',NULL);

/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;




# Dump of table sys_role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creater` varchar(255) DEFAULT NULL COMMENT '创建人',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `encode` varchar(255) DEFAULT NULL COMMENT '角色编码',
  `role_name` varchar(255) DEFAULT NULL COMMENT '角色名称',
  `pid` int(11) DEFAULT NULL COMMENT '父级ID',
  `status` int(11) DEFAULT '1' COMMENT '状态',
  `type` int(11) DEFAULT NULL COMMENT '类型',
  `user_id` bigint(20) DEFAULT NULL COMMENT '客户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;

INSERT INTO `sys_role` (`id`, `create_time`, `creater`, `description`, `encode`, `role_name`, `pid`, `status`, `type`, `user_id`)
VALUES
	(1,'2018-01-01 00:00:00','北京集奥聚合','运营超级管理员','Administrator','运营超级管理员',0,1,0,1),
	(2,'2018-01-01 00:00:00','默认','超级管理员','Root','超级管理员',2,1,1,0),
	(3,'2018-01-01 00:00:00','默认','管理员','Admin','管理员',2,1,1,0),
	(4,'2018-01-01 00:00:00','默认','用户','User','用户',2,1,1,0),
	(5,'2018-01-01 00:00:00','北京集奥聚合','超级管理员','Root','超级管理员',5,1,0,1),
	(6,'2018-01-01 00:00:00','北京集奥聚合','管理员','Admin','管理员',6,1,0,1),
	(7,'2018-01-01 00:00:00','北京集奥聚合','用户','User','用户',5,1,0,1),
	(8,'2018-03-13 18:47:48','北京集奥聚合','游客','游客','游客',7,1,0,1),
	(11,'2018-03-26 15:33:10','北京集奥聚合','asfdasfd','test','测试',4,0,1,1),
	(12,'2018-03-28 16:45:16','李世强','游客','user001','游客',4,1,1,18);

/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sys_role_menu
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_role_menu`;

CREATE TABLE `sys_role_menu` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) DEFAULT NULL,
  `menu_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;

INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `user_id`)
VALUES
	(87,4,1,0),
	(88,4,2,0),
	(89,4,3,0),
	(90,4,23,0),
	(91,4,24,0),
	(92,4,25,0),
	(93,4,4,0),
	(94,4,5,0),
	(95,4,29,0),
	(96,4,30,0),
	(97,4,31,0),
	(98,4,32,0),
	(99,4,33,0),
	(100,4,34,0),
	(101,4,35,0),
	(102,4,36,0),
	(103,4,37,0),
	(104,4,38,0),
	(105,4,83,0),
	(106,4,14,0),
	(107,4,15,0),
	(108,4,48,0),
	(109,4,49,0),
	(110,4,16,0),
	(111,4,17,0),
	(112,4,50,0),
	(113,4,86,0),
	(114,4,144,0),
	(125,4,145,0),
	(126,4,146,0),
	(127,4,147,0),
	(128,4,148,0),
	(129,4,149,0),
	(130,2,1,0),
	(131,2,145,0),
	(132,2,146,0),
	(133,2,14,0),
	(134,2,15,0),
	(135,2,48,0),
	(136,2,49,0),
	(137,2,16,0),
	(138,2,17,0),
	(139,2,50,0),
	(140,2,86,0),
	(141,2,144,0),
	(142,2,150,0),
	(143,2,148,0),
	(144,2,2,0),
	(145,2,3,0),
	(146,2,23,0),
	(147,2,24,0),
	(148,2,25,0),
	(149,2,4,0),
	(150,2,5,0),
	(151,2,29,0),
	(152,2,30,0),
	(153,2,31,0),
	(154,2,32,0),
	(155,2,33,0),
	(156,2,34,0),
	(157,2,35,0),
	(158,2,36,0),
	(159,2,37,0),
	(160,2,38,0),
	(161,2,83,0),
	(162,2,11,0),
	(163,2,12,0),
	(164,2,46,0),
	(165,2,47,0),
	(166,2,13,0),
	(167,2,149,0),
	(168,2,19,0),
	(169,2,20,0),
	(170,2,51,0),
	(171,2,52,0),
	(172,2,53,0),
	(173,2,22,0),
	(174,2,54,0),
	(175,2,55,0),
	(176,2,56,0),
	(177,2,57,0),
	(178,3,1,0),
	(179,3,145,0),
	(180,3,146,0),
	(181,3,14,0),
	(182,3,15,0),
	(183,3,48,0),
	(184,3,49,0),
	(185,3,16,0),
	(186,3,17,0),
	(187,3,50,0),
	(188,3,86,0),
	(189,3,144,0),
	(190,3,150,0),
	(191,3,148,0),
	(192,3,2,0),
	(193,3,3,0),
	(194,3,23,0),
	(195,3,24,0),
	(196,3,25,0),
	(197,3,4,0),
	(198,3,5,0),
	(199,3,29,0),
	(200,3,30,0),
	(201,3,31,0),
	(202,3,32,0),
	(203,3,33,0),
	(204,3,34,0),
	(205,3,35,0),
	(206,3,36,0),
	(207,3,37,0),
	(208,3,38,0),
	(209,3,83,0),
	(210,3,11,0),
	(211,3,12,0),
	(212,3,46,0),
	(213,3,47,0),
	(214,3,13,0),
	(215,3,149,0),
	(216,3,19,0),
	(217,3,20,0),
	(218,3,51,0),
	(219,3,52,0),
	(220,3,53,0),
	(221,3,22,0),
	(222,3,54,0),
	(223,3,55,0),
	(224,3,56,0),
	(225,3,57,0),
	(226,4,150,0);

/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;






# Dump of table sys_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '客户id',
  `contact` varchar(255) DEFAULT NULL COMMENT '联系人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creater` varchar(255) DEFAULT NULL COMMENT '创建人',
  `email` varchar(255) DEFAULT NULL COMMENT '电子邮箱',
  `mobilephone` varchar(255) DEFAULT NULL COMMENT '手机号',
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `password` varchar(255) DEFAULT NULL COMMENT '登录密码',
  `role_id` int(11) DEFAULT NULL COMMENT '角色id',
  `salt` varchar(255) DEFAULT NULL COMMENT '盐值',
  `status` int(11) DEFAULT '1' COMMENT '状态',
  `username` varchar(255) DEFAULT NULL COMMENT '登录用户名',
  `login_ip` varchar(50) DEFAULT NULL COMMENT '登录ip',
  `login_date` datetime DEFAULT NULL COMMENT '登录时间',
  `unique_code` bigint(20) DEFAULT NULL COMMENT '客户标识',
  `company` varchar(50) DEFAULT NULL COMMENT '公司名',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;

INSERT INTO `sys_user` (`id`, `contact`, `create_time`, `creater`, `email`, `mobilephone`, `name`, `password`, `role_id`, `salt`, `status`, `username`, `login_ip`, `login_date`, `unique_code`, `company`)
VALUES
	(1,'北京集奥聚合','2017-12-22 00:00:00','北京集奥聚合','www.geotmt.com','199999999','北京集奥集合','cefbbd3cbb3e0f3c8a126a67b3553601',2,NULL,1,'geotmt','0:0:0:0:0:0:0:1','2017-12-25 00:00:00',1,'北京集奥聚合');

/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sys_user_role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `u_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `id` bigint(100) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `FKt7u9ggroj0hseyo20nexvep86` (`u_id`),
  KEY `FKhh52n8vd4ny9ff4x9fb8v65qx` (`role_id`),
  CONSTRAINT `FKhh52n8vd4ny9ff4x9fb8v65qx` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`),
  CONSTRAINT `FKt7u9ggroj0hseyo20nexvep86` FOREIGN KEY (`u_id`) REFERENCES `sys_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;

INSERT INTO `sys_user_role` (`role_id`, `u_id`, `id`)
VALUES
	(2,1,1);

/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sys_user_token
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_user_token`;

CREATE TABLE `sys_user_token` (
  `user_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `token` varchar(255) DEFAULT NULL,
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `sys_user_token` WRITE;
/*!40000 ALTER TABLE `sys_user_token` DISABLE KEYS */;

INSERT INTO `sys_user_token` (`user_id`, `token`, `expire_time`, `update_time`, `id`)
VALUES
	(1,'2417602a08880bcf1dc7b46761d37048','2018-04-09 18:01:36','2018-04-08 18:01:36',1);

/*!40000 ALTER TABLE `sys_user_token` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
