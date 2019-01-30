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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `sys_customer` WRITE;
/*!40000 ALTER TABLE `sys_customer` DISABLE KEYS */;

INSERT INTO `sys_customer` (`id`, `server`, `encrypted`, `encryption_type`, `encryption_key`, `username`, `password`, `uno`, `etype`, `dsign`, `user_id`)
VALUES
	(1,'https://yz.geotmt.com',1,'AES2','','','','',NULL,0,1);

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
  PRIMARY KEY (`id`)
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
	(1,1,0,'主页','主页','./index.html','index:list',1,1,'fa fa-home',NULL,1),
	(2,2,0,'规则管理','规则管理','#','rule:list',1,1,'fa fa-qrcode','arrow fa fa-angle-right',3),
	(3,3,2,'规则管理','场景管理','./scene_management.html','rule:scene:list',1,1,NULL,NULL,NULL),
	(4,4,2,'规则管理','字段管理','./fields_market.html','rule:field:list',1,1,NULL,NULL,NULL),
	(5,5,2,'规则管理','规则集管理','./rule_set.html','rule:ruleset:list',1,1,NULL,NULL,NULL),
	(6,6,0,'规则测试','规则测试','#',NULL,1,0,'fa fa-edit','arrow fa fa-angle-right',4),
	(7,7,6,'规则测试','标签管理','./label_management.html',NULL,1,0,'','',NULL),
	(8,8,0,'名单管理','名单管理','#',NULL,1,0,'fa fa-file-text','arrow fa fa-angle-right',5),
	(9,9,8,'名单管理','黑名单管理','./black_list.html','ros:bow:list',1,0,NULL,NULL,NULL),
	(10,10,8,'名单管理','白名单管理','./white_list.html','ros:whe:list',1,0,NULL,NULL,NULL),
	(11,11,0,'审批管理','审批管理','#',NULL,1,1,'fa fa-history','arrow fa fa-angle-right',6),
	(12,12,11,'审批管理','待审批','./approval_pending.html','api:appr:list',1,1,NULL,NULL,NULL),
	(13,13,11,'审批管理','已审批','./approved.html','api:appr:list',1,1,NULL,NULL,NULL),
	(14,14,0,'进件管理','进件管理','#',NULL,1,1,'fa fa-inbox','arrow fa fa-angle-right',7),
	(15,15,14,'进件管理','通过事件','./pass_event.html',NULL,1,1,NULL,NULL,NULL),
	(16,16,14,'进件管理','拒绝事件','./reject_event.html',NULL,1,1,NULL,NULL,NULL),
	(17,17,14,'进件管理','人工审核事件','./human_review.html',NULL,1,1,NULL,NULL,NULL),
	(18,18,0,'客户事件统计','客户事件统计','./customer_event_statistics.html','api:customerEvestat:list',0,1,'fa fa-user',NULL,NULL),
	(19,19,0,'系统管理','系统管理','#',NULL,1,1,'fa fa-cog','arrow fa fa-angle-right',8),
	(20,20,19,'系统管理','用户管理','./staff_management.html','emp:list',1,1,NULL,NULL,NULL),
	(21,21,66,'系统管理','客户账号管理','./customer_account.html','sys:user:list',0,1,NULL,NULL,NULL),
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
	(60,60,71,'规则管理','接口管理','./interface_management.html','rule:inter:list',0,1,NULL,NULL,NULL),
	(61,61,21,'客户账号管理','添加客户权限',NULL,'sys:user:save',0,1,NULL,NULL,NULL),
	(62,62,21,'客户账号管理','删除客户权限',NULL,'sys:user:delete',0,1,NULL,NULL,NULL),
	(63,63,21,'客户账号管理','修改客户权限',NULL,'sys:user:update',0,1,NULL,NULL,NULL),
	(64,64,60,'接口管理','添加接口权限',NULL,'rule:inter:save',0,1,NULL,NULL,NULL),
	(65,65,60,'接口管理','修改接口权限',NULL,'rule:inter:update',0,1,NULL,NULL,NULL),
	(66,66,0,'系统管理','系统管理','#',NULL,0,1,'fa fa-cog','arrow fa fa-angle-right',NULL),
	(67,67,66,'系统管理','角色管理','./role_management.html','sys:role:list',0,1,NULL,NULL,NULL),
	(68,68,66,'系统管理','用户管理','./staff_management.html','emp:list',0,1,NULL,NULL,NULL),
	(71,71,0,'规则管理','规则管理','#','rule:list',0,1,'fa fa-qrcode','arrow fa fa-angle-right',NULL),
	(72,72,71,'规则管理','字段管理','./fields_market.html','rule:field:list',0,1,NULL,NULL,NULL),
	(73,73,68,'用户管理','添加用户',NULL,'emp:save',0,1,NULL,NULL,NULL),
	(74,74,68,'用户管理','修改用户',NULL,'emp:update',0,1,NULL,NULL,NULL),
	(75,75,67,'角色管理','添加角色',NULL,'sys:role:save',0,1,NULL,NULL,NULL),
	(76,76,67,'角色管理','添加角色',NULL,'sys:role:update',0,1,NULL,NULL,NULL),
	(77,77,67,'角色管理','启用/禁用',NULL,'sys:role:update',0,1,NULL,NULL,NULL),
	(78,78,67,'角色管理','配置权限',NULL,'sys:role:updatePermission',0,1,NULL,NULL,NULL),
	(79,79,10,'白名单管理','添加',NULL,'ros:bow:save',1,0,NULL,NULL,NULL),
	(80,80,10,'白名单管理','删除',NULL,'ros:bow:delete',1,0,NULL,NULL,NULL),
	(81,81,10,'白名单管理','导入',NULL,'ros:bow:import',1,0,NULL,NULL,NULL),
	(82,82,10,'白名单管理','导出',NULL,'ros:bow:export',1,0,NULL,NULL,NULL),
	(83,83,2,'规则管理','规则模版','./rule_set_template.html','rule:template:list',1,1,NULL,NULL,NULL),
	(84,84,71,'规则管理','规则集管理','./rules_management.html','rule:ruleset:list',0,1,NULL,NULL,NULL),
	(85,85,71,'规则管理','规则模版','./template_management.html','rule:template:list',0,1,NULL,NULL,NULL),
	(86,86,0,'进件统计','进件统计','./event_statistics.html','event:stat:save,event:stat:list',1,1,'fa fa-bar-chart-o',NULL,2);

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
	(7,'2018-01-01 00:00:00','北京集奥聚合','用户','User','用户',5,1,0,1);

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
	(135,3,1,9),
	(388,2,1,1),
	(389,2,2,1),
	(390,2,3,1),
	(391,2,23,1),
	(392,2,24,1),
	(393,2,25,1),
	(394,2,4,1),
	(395,2,5,1),
	(396,2,29,1),
	(397,2,30,1),
	(398,2,31,1),
	(399,2,32,1),
	(400,2,33,1),
	(401,2,34,1),
	(402,2,35,1),
	(403,2,36,1),
	(404,2,37,1),
	(405,2,38,1),
	(406,2,6,1),
	(407,2,7,1),
	(408,2,39,1),
	(409,2,40,1),
	(410,2,59,1),
	(411,2,41,1),
	(412,2,8,1),
	(413,2,9,1),
	(414,2,42,1),
	(415,2,43,1),
	(416,2,44,1),
	(417,2,45,1),
	(418,2,10,1),
	(419,2,79,1),
	(420,2,80,1),
	(421,2,81,1),
	(422,2,82,1),
	(423,2,11,1),
	(424,2,12,1),
	(425,2,46,1),
	(426,2,47,1),
	(427,2,13,1),
	(428,2,14,1),
	(429,2,15,1),
	(430,2,48,1),
	(431,2,49,1),
	(432,2,16,1),
	(433,2,17,1),
	(434,2,50,1),
	(435,2,19,1),
	(436,2,20,1),
	(437,2,51,1),
	(438,2,52,1),
	(439,2,53,1),
	(440,2,22,1),
	(441,2,54,1),
	(442,2,55,1),
	(443,2,56,1),
	(444,2,57,1),
	(480,3,1,1),
	(481,3,2,1),
	(482,3,3,1),
	(483,3,23,1),
	(484,3,24,1),
	(485,3,25,1),
	(486,3,4,1),
	(487,3,5,1),
	(488,3,29,1),
	(489,3,30,1),
	(490,3,31,1),
	(491,3,32,1),
	(492,3,33,1),
	(493,3,34,1),
	(494,3,35,1),
	(495,3,36,1),
	(496,3,37,1),
	(497,3,38,1),
	(498,3,11,1),
	(499,3,12,1),
	(500,3,46,1),
	(501,3,47,1),
	(502,3,13,1),
	(503,3,14,1),
	(504,3,15,1),
	(505,3,48,1),
	(506,3,49,1),
	(507,3,16,1),
	(508,3,17,1),
	(509,3,50,1),
	(510,3,19,1),
	(511,3,20,1),
	(512,3,51,1),
	(513,3,52,1),
	(514,3,53,1),
	(540,4,1,1),
	(541,4,2,1),
	(542,4,3,1),
	(543,4,23,1),
	(544,4,24,1),
	(545,4,25,1),
	(546,4,4,1),
	(547,4,5,1),
	(548,4,29,1),
	(549,4,30,1),
	(550,4,31,1),
	(551,4,32,1),
	(552,4,33,1),
	(553,4,34,1),
	(554,4,35,1),
	(555,4,36,1),
	(556,4,37,1),
	(557,4,38,1),
	(558,4,14,1),
	(559,4,15,1),
	(560,4,48,1),
	(561,4,49,1),
	(562,4,16,1),
	(563,4,17,1),
	(564,4,50,1),
	(565,2,83,1),
	(566,3,83,1),
	(567,4,83,1),
	(568,12,1,18),
	(569,12,2,18),
	(570,12,3,18),
	(571,12,23,18),
	(572,12,24,18),
	(573,12,25,18),
	(574,12,4,18),
	(575,12,5,18),
	(576,12,29,18),
	(577,12,30,18),
	(578,12,31,18),
	(579,12,32,18),
	(580,12,33,18),
	(581,12,34,18),
	(582,12,35,18),
	(583,12,36,18),
	(584,12,37,18),
	(585,12,38,18),
	(586,12,83,18),
	(587,12,11,18),
	(588,12,12,18),
	(589,12,46,18),
	(590,12,47,18),
	(591,12,13,18),
	(592,12,14,18),
	(593,12,15,18),
	(594,12,48,18),
	(595,12,49,18),
	(596,12,16,18),
	(597,12,17,18),
	(598,12,50,18),
	(599,12,19,18),
	(600,12,20,18),
	(601,12,51,18),
	(602,12,52,18),
	(603,12,53,18),
	(604,12,22,18),
	(605,12,54,18),
	(606,12,55,18),
	(607,12,56,18),
	(608,12,57,18),
	(609,2,86,1),
	(610,3,86,1),
	(611,4,86,1);

/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sys_role_permission
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_role_permission`;

CREATE TABLE `sys_role_permission` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `permission_id` bigint(20) NOT NULL COMMENT '权限ID',
  `id` bigint(100) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `FKomxrs8a388bknvhjokh440waq` (`permission_id`),
  KEY `FK9q28ewrhntqeipl1t04kh1be7` (`role_id`),
  CONSTRAINT `FK9q28ewrhntqeipl1t04kh1be7` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`),
  CONSTRAINT `FKomxrs8a388bknvhjokh440waq` FOREIGN KEY (`permission_id`) REFERENCES `sys_permission` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



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
  PRIMARY KEY (`id`)
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

/*!40000 ALTER TABLE `sys_user_token` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
