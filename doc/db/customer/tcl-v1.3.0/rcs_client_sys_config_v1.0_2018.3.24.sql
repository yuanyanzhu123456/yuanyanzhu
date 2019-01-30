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
	(1,1,0,'主页','主页','./index.html','index:list',1,1,'fa fa-home',NULL,1),
	(2,2,0,'规则管理','规则管理','#','rule:list',1,1,'fa fa-qrcode','arrow fa fa-angle-right',4),
	(3,3,2,'规则管理','场景管理','./scene_management.html','rule:scene:list',1,1,NULL,NULL,NULL),
	(4,4,2,'规则管理','字段管理','./fields_market.html','rule:field:list',1,1,NULL,NULL,NULL),
	(5,5,2,'规则管理','规则集管理','./rule_set.html','rule:ruleset:list',1,1,NULL,NULL,NULL),
	(6,6,0,'规则测试','规则测试','#',NULL,1,0,'fa fa-edit','arrow fa fa-angle-right',5),
	(7,7,6,'规则测试','标签管理','./label_management.html',NULL,1,0,'','',NULL),
	(8,8,0,'名单管理','名单管理','#',NULL,1,0,'fa fa-file-text','arrow fa fa-angle-right',6),
	(9,9,8,'名单管理','黑名单管理','./black_list.html','ros:bow:list',1,0,NULL,NULL,NULL),
	(10,10,8,'名单管理','白名单管理','./white_list.html','ros:whe:list',1,0,NULL,NULL,NULL),
	(11,11,0,'审批管理','审批管理','#',NULL,1,1,'fa fa-history','arrow fa fa-angle-right',7),
	(12,12,11,'审批管理','待审批','./approval_pending.html','api:appr:list',1,1,NULL,NULL,NULL),
	(13,13,11,'审批管理','已审批','./approved.html','api:appr:list',1,1,NULL,NULL,NULL),
	(14,14,0,'进件管理','进件管理','#',NULL,1,1,'fa fa-inbox','arrow fa fa-angle-right',8),
	(15,15,14,'进件管理','通过事件','./pass_event.html',NULL,1,1,NULL,NULL,NULL),
	(16,16,14,'进件管理','拒绝事件','./reject_event.html',NULL,1,1,NULL,NULL,NULL),
	(17,17,14,'进件管理','人工审核事件','./human_review.html',NULL,1,1,NULL,NULL,NULL),
	(18,18,0,'事件统计','事件统计','./event_statistics.html','api:customerEvestat:list',0,1,'fa fa-bar-chart-o',NULL,1),
	(19,19,0,'系统管理','系统管理','#','',1,1,'fa fa-cog','arrow fa fa-angle-right	',9),
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
	(86,86,0,'进件统计','进件统计','./event_statistics.html','event:stat:save,event:stat:list',1,1,'fa fa-bar-chart-o',NULL,3),
	(87,87,0,'客户管理','客户管理','#','',0,1,'fa fa-user','arrow fa fa-angle-right',2),
	(89,89,87,'客户管理','客户统计','./customer_statistics.html','',0,1,NULL,NULL,NULL),
	(91,91,71,'数据管理','目录管理','./menu_management.html','operate:menu:list',0,1,'','',NULL),
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
	(140,140,91,'目录管理','添加',' ','operate:menu:save',0,1,'','',NULL),
	(141,141,91,'目录管理','查询',' ','operate:menu:list',0,1,'','',NULL),
	(142,142,91,'目录管理','修改',' ','operate:menu:update',0,1,'','',NULL),
	(143,143,91,'目录管理','删除',' ','operate:menu:delete',0,1,'','',NULL),
	(144,144,0,'在线识别','在线识别','./event_online.html','',1,1,'fa fa-wpexplorer','',2);

/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
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
	(1,2,1,0),
	(2,2,2,0),
	(3,2,3,0),
	(4,2,23,0),
	(5,2,24,0),
	(6,2,25,0),
	(7,2,4,0),
	(8,2,5,0),
	(9,2,29,0),
	(10,2,30,0),
	(11,2,31,0),
	(12,2,32,0),
	(13,2,33,0),
	(14,2,34,0),
	(15,2,35,0),
	(16,2,36,0),
	(17,2,37,0),
	(18,2,38,0),
	(19,2,83,0),
	(20,2,11,0),
	(21,2,12,0),
	(22,2,46,0),
	(23,2,47,0),
	(24,2,13,0),
	(25,2,14,0),
	(26,2,15,0),
	(27,2,48,0),
	(28,2,49,0),
	(29,2,16,0),
	(30,2,17,0),
	(31,2,50,0),
	(32,2,19,0),
	(33,2,20,0),
	(34,2,51,0),
	(35,2,52,0),
	(36,2,53,0),
	(37,2,22,0),
	(38,2,54,0),
	(39,2,55,0),
	(40,2,56,0),
	(41,2,57,0),
	(42,2,86,0),
	(43,2,144,0),
	(44,3,1,0),
	(45,3,2,0),
	(46,3,3,0),
	(47,3,23,0),
	(48,3,24,0),
	(49,3,25,0),
	(50,3,4,0),
	(51,3,5,0),
	(52,3,29,0),
	(53,3,30,0),
	(54,3,31,0),
	(55,3,32,0),
	(56,3,33,0),
	(57,3,34,0),
	(58,3,35,0),
	(59,3,36,0),
	(60,3,37,0),
	(61,3,38,0),
	(62,3,83,0),
	(63,3,11,0),
	(64,3,12,0),
	(65,3,46,0),
	(66,3,47,0),
	(67,3,13,0),
	(68,3,14,0),
	(69,3,15,0),
	(70,3,48,0),
	(71,3,49,0),
	(72,3,16,0),
	(73,3,17,0),
	(74,3,50,0),
	(75,3,19,0),
	(76,3,20,0),
	(77,3,51,0),
	(78,3,52,0),
	(79,3,53,0),
	(80,3,22,0),
	(81,3,54,0),
	(82,3,55,0),
	(83,3,56,0),
	(84,3,57,0),
	(85,3,86,0),
	(86,3,144,0),
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
	(114,4,144,0);

/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

# Dump of table sys_role_permission
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_role_permission`;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
