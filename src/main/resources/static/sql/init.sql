CREATE DATABASE /*!32312 IF NOT EXISTS*/`rcs` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `rcs`;


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
	(1,1,2,1),
	(3,1,4,1),
	(5,1,18,1),
	(6,1,19,1),
	(7,1,20,1),
	(8,1,21,1),
	(9,1,22,1),
	(24,1,60,1),
	(25,1,61,1),
	(26,1,62,1),
	(27,1,63,1),
	(28,1,51,1),
	(29,1,52,1),
	(30,1,53,1);

/*!40000 ALTER TABLE `geo_role_menu` ENABLE KEYS */;
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
	(2,4,2);

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
	(1,1,'9499061489476835d86b62c215c4a6f7','2018-03-05 10:04:01','2018-03-06 10:04:01'),
	(2,4,'99c92b026bcb8eb6b4c105abfdf54830','2018-03-02 19:11:08','2018-03-03 19:11:08');

/*!40000 ALTER TABLE `geo_user_token` ENABLE KEYS */;
UNLOCK TABLES;


alter table sys_role add column user_id bigint(20);


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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;

INSERT INTO `sys_menu` (`id`, `menu_id`, `parent_id`, `parent_name`, `name`, `url`, `perms`, `type`, `open`, `front_icon`, `back_icon`)
VALUES
	(1,1,0,'事件统计','事件统计','./index.html','event:stat:save,event:stat:list',0,1,'fa fa-bar-chart-o',NULL),
	(2,2,0,'规则管理','规则管理','#','rule:list',0,1,'fa fa-qrcode','arrow fa fa-angle-right'),
	(3,3,2,'规则管理','场景管理','./scene_management.html','rule:scene:list',1,1,NULL,NULL),
	(4,4,2,'规则管理','字段管理','./column_management.html','rule:field:list',1,1,NULL,NULL),
	(5,5,2,'规则管理','规则集管理','./rules_management.html','rule:ruleset:list',1,1,NULL,NULL),
	(6,6,0,'规则测试','规则测试','#',NULL,1,1,'fa fa-edit','arrow fa fa-angle-right'),
	(7,7,6,'规则测试','标签管理','./label_management.html',NULL,1,1,'',''),
	(8,8,0,'名单库管理','名单库管理','#',NULL,0,1,'fa fa-file-text','arrow fa fa-angle-right'),
	(9,9,8,'名单库管理','黑名单管理','./black_list.html','ros:bow:list',1,1,NULL,NULL),
	(10,10,8,'名单库管理','白名单管理','./white_list.html','ros:whe:list',1,NULL,NULL,NULL),
	(11,11,0,'审批管理','审批管理','#',NULL,0,NULL,'fa fa-history','arrow fa fa-angle-right'),
	(12,12,11,'审批管理','待审批','./approval_pending.html','api:appr:list',1,NULL,NULL,NULL),
	(13,13,11,'审批管理','已审批','./approved.html','api:appr:list',1,NULL,NULL,NULL),
	(14,14,0,'进件管理','进件管理','#',NULL,0,NULL,'fa fa-inbox','arrow fa fa-angle-right'),
	(15,15,14,'进件管理','通过事件','./pass_event.html',NULL,1,NULL,NULL,NULL),
	(16,16,14,'进件管理','拒绝事件','./reject_event.html',NULL,1,NULL,NULL,NULL),
	(17,17,14,'进件管理','人工审核事件','./human_review.html',NULL,1,NULL,NULL,NULL),
	(18,18,0,'客户事件统计','客户事件统计','./customer_event_statistics.html',NULL,0,NULL,'fa fa-user',NULL),
	(19,19,0,'系统管理','系统管理','#',NULL,0,NULL,'fa fa-cog','arrow fa fa-angle-right'),
	(20,20,19,'系统管理','用户管理','./staff_management.html','emp:list',1,NULL,NULL,NULL),
	(21,21,19,'系统管理','客户账号管理','./customer_account.html','sys:user:list',1,NULL,NULL,NULL),
	(22,22,19,'系统管理','角色管理','./role_management.html','sys:role:list',1,NULL,NULL,NULL),
	(23,23,3,'场景管理','添加场景',NULL,'rule:scene:save',1,NULL,NULL,NULL),
	(24,24,3,'场景管理','修改场景',NULL,'rule:scene:update',1,NULL,NULL,NULL),
	(25,25,3,'场景管理','删除场景',NULL,'rule:scene:delete',1,NULL,NULL,NULL),
	(26,26,4,'字段管理','添加字段',NULL,'rule:field:save',1,NULL,NULL,NULL),
	(27,27,4,'字段管理','修改字段',NULL,'rule:field:update',1,NULL,NULL,NULL),
	(28,28,4,'字段管理','删除字段',NULL,'rule:field:delete',1,NULL,NULL,NULL),
	(29,29,5,'规则集管理','添加规则集',NULL,'rule:ruleset:save',1,NULL,NULL,NULL),
	(30,30,5,'规则集管理','修改规则集',NULL,'rule:ruleset:update',1,NULL,NULL,NULL),
	(31,31,5,'规则集管理','删除规则集',NULL,'rule:ruleset:delete',1,NULL,NULL,NULL),
	(32,32,58,'规则管理','添加规则',NULL,'rule:save',1,NULL,NULL,NULL),
	(33,33,58,'规则管理','修改规则',NULL,'rule:update',1,NULL,NULL,NULL),
	(34,34,58,'规则管理','删除规则',NULL,'rule:delete',1,NULL,NULL,NULL),
	(35,35,58,'规则管理','条件管理',NULL,'rule:condition:list',1,NULL,NULL,NULL),
	(36,36,35,'条件管理','添加条件',NULL,'rule:condition:save',1,NULL,NULL,NULL),
	(37,37,35,'条件管理','修改条件',NULL,'rule:condition:update',1,NULL,NULL,NULL),
	(38,38,35,'条件管理','删除条件',NULL,'rule:condition:delete',1,NULL,NULL,NULL),
	(39,39,7,'标签管理','删除标签',NULL,NULL,1,NULL,NULL,NULL),
	(40,40,7,'标签管理','导入',NULL,NULL,1,NULL,NULL,NULL),
	(41,41,7,'规则测试','删除',NULL,NULL,1,NULL,NULL,NULL),
	(42,42,9,'名单库管理','添加',NULL,'ros:bow:list',1,NULL,NULL,NULL),
	(43,43,9,'名单库管理','删除',NULL,'ros:bow:delete',1,NULL,NULL,NULL),
	(44,44,9,'名单库管理','导入',NULL,'ros:bow:import',1,NULL,NULL,NULL),
	(45,45,9,'名单库管理','导出',NULL,'ros:bow:export',1,NULL,NULL,NULL),
	(46,46,12,'待审批管理','审批',NULL,'api:appr:accraditation',1,NULL,NULL,NULL),
	(47,47,12,'已审批管理','查看',NULL,'api:appr:view',1,NULL,NULL,NULL),
	(48,48,15,'进件管理','查看报告',NULL,NULL,1,NULL,NULL,NULL),
	(49,49,15,'进件管理','下载报告',NULL,NULL,1,NULL,NULL,NULL),
	(50,50,17,'人工审核事件','审批',NULL,NULL,1,NULL,NULL,NULL),
	(51,51,20,'用户管理','添加',NULL,'emp:save',1,NULL,NULL,NULL),
	(52,52,20,'用户管理','修改',NULL,'emp:update',1,NULL,NULL,NULL),
	(53,53,20,'用户管理','启用/禁用',NULL,'emp:update',1,NULL,NULL,NULL),
	(54,54,22,'角色管理','添加角色',NULL,'sys:role:save',1,NULL,NULL,NULL),
	(55,55,22,'角色管理','修改角色',NULL,'sys:role:update',1,NULL,NULL,NULL),
	(56,56,22,'角色管理','启用/禁用',NULL,'sys:role:update',1,NULL,NULL,NULL),
	(57,57,22,'角色管理','配置权限',NULL,'sys:role:updatePermission',1,NULL,NULL,NULL),
	(58,58,2,'规则管理','规则管理','./rule_management.html',NULL,1,NULL,NULL,NULL),
	(59,59,6,'规则测试','规则测试','./rule_test.html',NULL,1,NULL,NULL,NULL),
	(60,60,2,'规则管理','接口管理','./interface_management.html',NULL,1,NULL,NULL,NULL),
	(61,61,21,'客户账号管理','添加客户权限',NULL,'sys:user:save',1,NULL,NULL,NULL),
	(62,62,21,'客户账号管理','删除客户权限',NULL,'sys:user:delete',1,NULL,NULL,NULL),
	(63,63,21,'客户账号管理','修改客户权限',NULL,'sys:user:update',1,NULL,NULL,NULL);

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
	(1,2,1,1),
	(2,2,2,1),
	(3,2,3,1),
	(4,2,4,1),
	(5,2,5,1),
	(6,2,6,1),
	(7,2,7,1),
	(8,2,8,1),
	(9,2,9,1),
	(10,2,10,1),
	(11,2,11,1),
	(12,2,12,1),
	(13,2,13,1),
	(14,2,14,1),
	(15,2,15,1),
	(16,2,16,1),
	(17,2,17,1),
	(18,2,19,1),
	(19,2,20,1),
	(20,2,22,1),
	(21,2,23,1),
	(22,2,24,1),
	(23,2,25,1),
	(24,2,29,1),
	(25,2,30,1),
	(26,2,31,1),
	(27,2,32,1),
	(28,2,33,1),
	(29,2,34,1),
	(30,2,35,1),
	(31,2,36,1),
	(32,2,37,1),
	(33,2,38,1),
	(34,2,39,1),
	(35,2,40,1),
	(36,2,41,1),
	(37,2,42,1),
	(38,2,43,1),
	(39,2,44,1),
	(40,2,45,1),
	(41,2,46,1),
	(42,2,47,1),
	(43,2,48,1),
	(44,2,49,1),
	(45,2,50,1),
	(46,2,51,1),
	(47,2,52,1),
	(48,2,53,1),
	(49,2,54,1),
	(50,2,55,1),
	(51,2,56,1),
	(52,2,57,1),
	(53,2,58,1),
	(54,2,59,1),
	(58,9,1,1),
	(59,9,2,1),
	(60,9,3,1),
	(61,9,23,1),
	(62,9,24,1),
	(63,9,25,1),
	(64,9,4,1),
	(65,9,5,1),
	(66,9,29,1),
	(67,9,30,1),
	(68,9,31,1),
	(69,9,58,1),
	(70,9,32,1),
	(71,9,33,1),
	(72,9,34,1),
	(73,9,35,1),
	(74,9,36,1),
	(75,9,37,1),
	(76,9,38,1),
	(77,9,6,1),
	(78,9,7,1),
	(79,9,39,1),
	(80,9,40,1),
	(81,9,41,1),
	(82,9,59,1),
	(109,10,1,1),
	(110,10,2,1),
	(111,10,3,1),
	(112,10,23,1),
	(113,10,24,1),
	(114,10,25,1),
	(115,10,4,1),
	(116,10,5,1),
	(117,10,29,1),
	(118,10,30,1),
	(119,10,31,1),
	(120,10,58,1),
	(121,10,32,1),
	(122,10,33,1),
	(123,10,34,1),
	(124,10,35,1),
	(125,10,36,1),
	(126,10,37,1),
	(127,10,38,1),
	(128,10,14,1),
	(129,10,15,1),
	(130,10,48,1),
	(131,10,49,1),
	(132,10,16,1),
	(133,10,17,1),
	(134,10,50,1);

/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `customer`;

CREATE TABLE `customer` (
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

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;

INSERT INTO `customer` (`id`, `server`, `encrypted`, `encryption_type`, `encryption_key`, `username`, `password`, `uno`, `etype`, `dsign`, `user_id`)
VALUES
	(1, 'https://yz.geotmt.com', 0, 'AES2', '', 'zhangyongming', 'Zhang@123', '200842', '', 0, 1);

/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;
