# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.6.38)
# Database: rcs
# Generation Time: 2018-03-24 10:09:21 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table approval_action_type
# ------------------------------------------------------------

DROP TABLE IF EXISTS `approval_action_type`;

CREATE TABLE `approval_action_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `action_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `approval_action_type` WRITE;
/*!40000 ALTER TABLE `approval_action_type` DISABLE KEYS */;

INSERT INTO `approval_action_type` (`id`, `action_name`)
VALUES
	(1,'添加'),
	(2,'修改'),
	(3,'删除'),
	(4,'编辑');

/*!40000 ALTER TABLE `approval_action_type` ENABLE KEYS */;
UNLOCK TABLES;

# Dump of table engine_history_log
# ------------------------------------------------------------

DROP TABLE IF EXISTS `engine_history_log`;

CREATE TABLE `engine_history_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `rules_map` longtext COMMENT '规则集信息',
  `rules_id` bigint(20) DEFAULT NULL COMMENT '规则集编号',
  `unique_code` bigint(20) DEFAULT NULL COMMENT '客户标识',
  `record_time` datetime DEFAULT NULL COMMENT '记录时间',
  `describ` varchar(255) DEFAULT NULL COMMENT '描述',
  `action_type` int(11) DEFAULT '0' COMMENT '操作类型',
  `rules_name` varchar(50) DEFAULT NULL COMMENT '规则集名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


# Dump of table approval_manage
# ------------------------------------------------------------

DROP TABLE IF EXISTS `approval_manage`;

CREATE TABLE `approval_manage` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `action_type` int(11) DEFAULT NULL COMMENT '操作类型',
  `submitter` varchar(50) DEFAULT NULL COMMENT '提交人',
  `sub_time` datetime DEFAULT NULL COMMENT '提交时间',
  `obj_id` int(50) DEFAULT NULL COMMENT '对象名称',
  `app_status` int(11) DEFAULT '0' COMMENT '审批状态（0:未审批，1:正在审批，2:审批通过 3:审批失败）',
  `threshold` int(11) DEFAULT NULL COMMENT '阈值',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `scene` varchar(255) DEFAULT NULL COMMENT '适用场景',
  `activate` int(5) DEFAULT NULL COMMENT '是否激活',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `unique_code` bigint(20) DEFAULT NULL COMMENT '与客户关联',
  `business_id` int(11) DEFAULT NULL COMMENT '业务类型',
  `app_person` varchar(20) DEFAULT NULL COMMENT '审批人',
  `app_time` datetime DEFAULT NULL COMMENT '审批时间',
  `only_id` bigint(20) DEFAULT NULL COMMENT '自身编号',
  `threshold_min` int(11) DEFAULT NULL COMMENT '最小值',
  `threshold_max` int(11) DEFAULT NULL COMMENT '最大值',
  `record_id` bigint(20) DEFAULT NULL COMMENT '记录历史表的编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table approval_object_type
# ------------------------------------------------------------

DROP TABLE IF EXISTS `approval_object_type`;

CREATE TABLE `approval_object_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `obj_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `approval_object_type` WRITE;
/*!40000 ALTER TABLE `approval_object_type` DISABLE KEYS */;

INSERT INTO `approval_object_type` (`id`, `obj_name`)
VALUES
	(1,'场景'),
	(2,'规则集'),
	(3,'规则'),
	(4,'字段');

/*!40000 ALTER TABLE `approval_object_type` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table engine_business_type
# ------------------------------------------------------------

DROP TABLE IF EXISTS `engine_business_type`;

CREATE TABLE `engine_business_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '业务编号',
  `type_name` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `engine_business_type` WRITE;
/*!40000 ALTER TABLE `engine_business_type` DISABLE KEYS */;

INSERT INTO `engine_business_type` (`id`, `type_name`)
VALUES
	(10001,'银行小额信用贷款'),
	(10002,'银行中大额信用贷款\n'),
	(10003,'银行信用卡'),
	(10004,'小额现金贷');

/*!40000 ALTER TABLE `engine_business_type` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table engine_conditions
# ------------------------------------------------------------

DROP TABLE IF EXISTS `engine_conditions`;

CREATE TABLE `engine_conditions` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '条件编号',
  `name` varchar(255) DEFAULT NULL COMMENT '规则名称',
  `rule_id` int(11) DEFAULT NULL COMMENT '所属规则集id',
  `rules_id` int(11) DEFAULT NULL,
  `field_relationship` varchar(255) DEFAULT NULL,
  `describ` varchar(255) DEFAULT NULL COMMENT '描述',
  `active` int(1) DEFAULT '1' COMMENT '激活状态（0：禁用，1：激活）',
  `verify` int(11) DEFAULT '1' COMMENT '审核状态（0：未审核， 1：未审核，2：已通过，3：未通过）',
  `add_user` varchar(255) DEFAULT NULL,
  `add_time` datetime DEFAULT NULL,
  `unique_code` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `engine_conditions` WRITE;
/*!40000 ALTER TABLE `engine_conditions` DISABLE KEYS */;

INSERT INTO `engine_conditions` (`id`, `name`, `rule_id`, `rules_id`, `field_relationship`, `describ`, `active`, `verify`, `add_user`, `add_time`, `unique_code`)
VALUES
	(164,'测试条件1',10250,10067,'10226&&10227','测试条件1',1,1,'北京集奥聚合','2018-03-24 16:46:54',1),
	(165,'测试条件2',10250,10067,'10228&&10229','测试条件2',1,1,'北京集奥聚合','2018-03-24 16:47:48',1),
	(166,'测试条件3',10251,10067,'10230&&10231','测试条件4',1,1,'北京集奥聚合','2018-03-24 16:49:45',1);

/*!40000 ALTER TABLE `engine_conditions` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table engine_field_type
# ------------------------------------------------------------

DROP TABLE IF EXISTS `engine_field_type`;

CREATE TABLE `engine_field_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `type_name` varchar(20) DEFAULT NULL COMMENT '字段类型名称',
  `type` varchar(20) DEFAULT NULL COMMENT '字段类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `engine_field_type` WRITE;
/*!40000 ALTER TABLE `engine_field_type` DISABLE KEYS */;

INSERT INTO `engine_field_type` (`id`, `type_name`, `type`)
VALUES
	(1,'整数','int'),
	(2,'字符串','string'),
	(3,'时间字符串','datetime'),
	(4,'日期字符串','date');

/*!40000 ALTER TABLE `engine_field_type` ENABLE KEYS */;
UNLOCK TABLES;

# Dump of table engine_data_type
# ------------------------------------------------------------

DROP TABLE IF EXISTS `engine_data_type`;

CREATE TABLE `engine_data_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `type_name` varchar(50) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `engine_data_type` WRITE;
/*!40000 ALTER TABLE `engine_data_type` DISABLE KEYS */;

INSERT INTO `engine_data_type` (`id`, `type_name`, `parent_id`)
VALUES
	(1,'全部',0),
	(2,'身份验证',1),
	(3,'位置信息',1),
	(4,'手机号信息查验',1),
	(5,'社交验证',1),
	(6,'手机终端识别',1),
	(7,'多平台借贷识别',1),
	(8,'逾期识别',1),
	(9,'欺诈识别',1),
	(10,'涉诉与失信',1),
	(11,'风险IP识别',1),
	(12,'银行账号风险识别',1),
	(13,'银行卡交易信息查验与评分',1);

/*!40000 ALTER TABLE `engine_data_type` ENABLE KEYS */;
UNLOCK TABLES;



# Dump of table engine_fields
# ------------------------------------------------------------

DROP TABLE IF EXISTS `engine_fields`;

CREATE TABLE `engine_fields` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '字段编号',
  `show_name` varchar(255) DEFAULT NULL COMMENT '显示名',
  `field_id` int(11) DEFAULT NULL COMMENT '字段Id',
  `field_name` varchar(255) DEFAULT NULL COMMENT '字段名称',
  `rules_id` bigint(11) DEFAULT NULL COMMENT '规则集Id',
  `condition_id` bigint(20) DEFAULT NULL COMMENT '条件id',
  `operator` varchar(255) DEFAULT NULL COMMENT '运算符',
  `describ` varchar(255) DEFAULT NULL COMMENT '字段描述',
  `active` int(1) DEFAULT '1' COMMENT '激活状态（0：禁用，1：激活）',
  `verify` int(11) DEFAULT '1' COMMENT '审核状态（0：未审核， 1：未审核，2：已通过，3：未通过）',
  `add_user` varchar(255) DEFAULT NULL,
  `add_time` datetime DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL COMMENT '初始值',
  `value_desc` varchar(255) DEFAULT NULL COMMENT '初始值描述',
  `unique_code` bigint(11) DEFAULT NULL,
  `field_type` varchar(20) DEFAULT NULL COMMENT '字段类型名称',
  `field_type_id` int(11) DEFAULT NULL COMMENT '字段类型编号',
  `parameter` varchar(100) DEFAULT NULL COMMENT '对比值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `engine_fields` WRITE;
/*!40000 ALTER TABLE `engine_fields` DISABLE KEYS */;

INSERT INTO `engine_fields` (`id`, `show_name`, `field_id`, `field_name`, `rules_id`, `condition_id`, `operator`, `describ`, `active`, `verify`, `add_user`, `add_time`, `value`, `value_desc`, `unique_code`, `field_type`, `field_type_id`, `parameter`)
VALUES
	(10238,NULL,10001,'province',10069,170,'==','测试字段1',1,1,'test','2018-03-24 17:13:13',NULL,NULL,35,NULL,2,'山东'),
	(10239,NULL,10002,'city',10069,170,'==','测试字段2',1,1,'test','2018-03-24 17:13:13',NULL,NULL,35,NULL,2,'济南'),
	(10240,NULL,10001,'province',10069,171,'==','测试字段3',1,1,'test','2018-03-24 17:13:13',NULL,NULL,35,NULL,2,'山东'),
	(10241,NULL,10002,'city',10069,171,'==','测试字段4',1,1,'test','2018-03-24 17:13:13',NULL,NULL,35,NULL,2,'济南'),
	(10242,NULL,10001,'province',10069,172,'==','测试字段5',1,1,'test','2018-03-24 17:13:13',NULL,NULL,35,NULL,2,'山东'),
	(10243,NULL,10002,'city',10069,172,'==','测试字段6',1,1,'test','2018-03-24 17:13:13',NULL,NULL,35,NULL,2,'济南');

/*!40000 ALTER TABLE `engine_fields` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table engine_patch_data
# ------------------------------------------------------------

DROP TABLE IF EXISTS `engine_patch_data`;

CREATE TABLE `engine_patch_data` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '临时表编号',
  `action_id` int(11) DEFAULT NULL COMMENT '操作类型',
  `obj_id` int(11) DEFAULT NULL COMMENT '对象名称',
  `name` varchar(20) DEFAULT NULL COMMENT '名称',
  `field_type` int(11) DEFAULT NULL COMMENT '字段类型',
  `data_type` int(11) DEFAULT NULL COMMENT '数据类型',
  `describ` varchar(20) DEFAULT NULL COMMENT '字段显示名',
  `business_id` int(11) DEFAULT NULL COMMENT '业务类型',
  `rules_id` bigint(20) DEFAULT NULL COMMENT '所属规则集',
  `scene_id` bigint(20) DEFAULT NULL COMMENT '适用场景',
  `active` int(1) DEFAULT NULL COMMENT '是否激活',
  `threshold` int(11) DEFAULT NULL COMMENT '风险系数',
  `level` int(1) DEFAULT NULL COMMENT '风险决策',
  `rule_code` varchar(20) DEFAULT NULL COMMENT '规则编码',
  `black_filter` varchar(20) DEFAULT NULL COMMENT '黑名单过滤',
  `white_filter` varchar(20) DEFAULT NULL COMMENT '白名单过滤',
  `describtion` varchar(255) DEFAULT NULL COMMENT '描述',
  `only_id` bigint(20) DEFAULT NULL COMMENT '原表编号',
  `verify` int(11) DEFAULT NULL COMMENT '审核状态',
  `match_type` int(11) DEFAULT NULL COMMENT '匹配模式',
  `threshold_min` int(11) DEFAULT NULL COMMENT '最小值',
  `threshold_max` int(11) DEFAULT NULL COMMENT '最大值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `engine_patch_data` WRITE;
/*!40000 ALTER TABLE `engine_patch_data` DISABLE KEYS */;

INSERT INTO `engine_patch_data` (`id`, `action_id`, `obj_id`, `name`, `field_type`, `data_type`, `describ`, `business_id`, `rules_id`, `scene_id`, `active`, `threshold`, `level`, `rule_code`, `black_filter`, `white_filter`, `describtion`, `only_id`, `verify`, `match_type`, `threshold_min`, `threshold_max`)
VALUES
	(1,2,2,'规则集一',NULL,NULL,NULL,10001,NULL,10001,1,NULL,NULL,NULL,NULL,NULL,'测试修改',10006,NULL,1,300,600),
	(2,2,2,'规则集一',NULL,NULL,NULL,10001,NULL,10001,1,NULL,NULL,NULL,NULL,NULL,'测试修改',10006,NULL,1,300,600),
	(4,2,2,'规则集一',NULL,NULL,NULL,10001,NULL,10001,1,NULL,NULL,NULL,NULL,NULL,'测试修改',10006,NULL,1,100,300),
	(5,2,2,'规则集一',NULL,NULL,NULL,10001,NULL,10001,1,NULL,NULL,NULL,NULL,NULL,'测试修改',10006,NULL,1,100,400);

/*!40000 ALTER TABLE `engine_patch_data` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table engine_raw_fields
# ------------------------------------------------------------

DROP TABLE IF EXISTS `engine_raw_fields`;

CREATE TABLE `engine_raw_fields` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '字段编号',
  `name` varchar(255) DEFAULT NULL COMMENT '字段名称',
  `inter_id` bigint(20) DEFAULT NULL COMMENT '接口id',
  `field_type` int(255) DEFAULT NULL COMMENT '字段类型',
  `data_type` int(255) DEFAULT NULL COMMENT '数据分类',
  `describ` varchar(255) DEFAULT NULL COMMENT '显示名',
  `active` int(1) DEFAULT '0' COMMENT '激活状态（0：禁用，1：激活）',
  `verify` int(11) DEFAULT '0' COMMENT '审核状态（0：未审核， 1：未审核，2：已通过，3：未通过）',
  `add_user` varchar(255) DEFAULT NULL COMMENT '添加人',
  `add_time` datetime DEFAULT NULL COMMENT '添加时间',
  `describtion` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `engine_raw_fields` WRITE;
/*!40000 ALTER TABLE `engine_raw_fields` DISABLE KEYS */;

INSERT INTO `engine_raw_fields` (`id`, `name`, `inter_id`, `field_type`, `data_type`, `describ`, `active`, `verify`, `add_user`, `add_time`, `describtion`)
VALUES
	(10001,'province',10001,2,4,'省份',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10002,'city',10001,2,4,'城市',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10003,'isp',10001,2,4,'运营商',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10004,'onlineTime',10001,2,4,'在网时长',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10005,'onlineStatus',10002,2,4,'在网状态',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10006,'IdPhoneNameValidate',10003,2,4,'手机身份证号姓名验证',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10007,'outOfServiceTimes',10004,1,4,'三月内停机次数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10008,'consumerGrade',10005,2,2,'手机实名验证',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10009,'phoneModel',10006,2,6,'手机型号',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10010,'IdPhoneValidate',10007,2,2,'手机身份证号验证',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10011,'phoneGroupValidate',10008,2,2,'手机集团客户验证',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10012,'onlineTimePlus',10009,2,4,'在网时长详细',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10013,'consumerGradePlus',10010,2,4,'消费等级详细',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10014,'frequentContactsValidate',10011,2,5,'常用联系人验证',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10015,'workPlaceValidate',10012,2,3,'工作地经纬度验证',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10016,'residenceValidate',10013,2,3,'居住地经纬度验证',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10017,'currentCityValidate',10014,2,3,'实时城市验证',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10018,'CallTimes',10015,2,5,'手机联系人通话次数查询',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10019,'result_YQ_ZZSJ',10016,4,8,'逾期最早出现时间',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10020,'result_YQ_ZDSC',10016,2,8,'历史最大逾期时长',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10021,'result_SX_LJCS',10016,2,10,'失信累计出现次数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10022,'result_QZ_LJCS',10016,2,10,'欺诈累计出现次数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10023,'result_YQ_LJCS',10016,2,10,'逾期累计出现次数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10024,'result_SX_ZZSJ',10016,4,10,'失信最早出现时间',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10025,'result_QZ_ZJSJ',10016,4,10,'欺诈最近出现时间',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10026,'result_QZ_ZZSJ',10016,4,10,'欺诈最早出现时间',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10027,'result_YQ_ZJSJ',10016,4,8,'逾期最近出现时间',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10028,'result_SX_ZJSJ',10016,4,10,'失信最近出现时间',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10029,'result_YQ_DQSC',10016,2,8,'当前逾期时长',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10030,'result_YQ_ZDJE',10016,2,8,'历史最大逾期金额',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10031,'result_YQ_DQJE',10016,2,8,'当前逾期金额',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10032,'birthday',10017,4,2,'出生日期',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10033,'gender',10017,2,2,'性别',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10034,'originalAdress',10017,2,2,'出生地址',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10035,'identityResult',10017,2,2,'姓名身份证号验证',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10036,'age',10017,1,2,'年龄',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10037,'lasttime',10018,4,2,'信息获取时间',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10038,'identify_result',10018,2,2,'是否有房',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10039,'IdPhoneNameCardValidate',10019,2,12,'手机身份证号姓名验证银行卡验证',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10040,'IdNameCardValidate',10020,2,12,'身份证号姓名验证银行卡验证',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10041,'account_no',10021,2,12,'银行卡号',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10042,'score',10021,1,12,'缺失',0,0,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10043,'CSSP001',10021,2,12,'缺失',0,0,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10044,'firstTime',10022,4,2,'工作单位最早出现时间',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10045,'lastTime',10022,4,2,'工作单位最近出现时间',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10046,'score',10022,1,2,'工作单位匹配分数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10047,'identify_result',10022,2,2,'工作单位验证',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10048,'isMachdSEO',10023,2,11,'命中SEO',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10049,'isMachdProxy',10023,2,11,'命中代理服务器 ',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10050,'rskScore',10023,1,11,'IP风险得分',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10051,'isMachdDNS',10023,2,11,'命中DNS服务器 ',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10052,'iUpdateDate',10023,4,11,'IP风险时间',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10053,'isMachdMailServ',10023,2,11,'命中邮件服务器',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10054,'isMachdForce',10023,2,11,'命中暴力破解 ',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10055,'isMachdBlacklist',10023,2,11,'命中第三方标注黑名单',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10056,'isMachdWebServ',10023,2,11,'命中web服务器',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10057,'isMachdOrg',10023,2,11,'命中组织出口',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10058,'isMachdCrawler',10023,2,11,'命中爬虫 ',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10059,'isMachdVPN',10023,2,11,'命中vpn服务器 ',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10060,'TJXX_30d_regtimes',10024,1,7,'30天内-多平台注册次数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10061,'TJXX_30d_regplatforms',10024,1,7,'30天内-多平台注册平台数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10062,'TJXX_30d_reglasttime',10024,3,7,'30天内-多平台最近注册时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10063,'TJXX_30d_regfirsttime',10024,3,7,'30天内-多平台最早注册时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10064,'TJXX_30d_apptimes',10024,1,7,'30天内-多平台申请次数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10065,'TJXX_30d_appplatforms',10024,1,7,'30天内-多平台申请平台数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10066,'TJXX_30d_appmoney',10024,2,7,'30天内-多平台最大申请金额区间',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10067,'TJXX_30d_applasttime',10024,3,7,'30天内-多平台最近申请时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10068,'TJXX_30d_appfirsttime',10024,3,7,'30天内-多平台最早申请时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10069,'TJXX_30d_loantimes',10024,1,7,'30天内-多平台放款次数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10070,'TJXX_30d_loanplatforms',10024,1,7,'30天内-多平台放款平台数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10071,'TJXX_30d_loanmoney',10024,2,7,'30天内-多平台最大放款金额区间',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10072,'TJXX_30d_loanfirsttime',10024,3,7,'30天内-多平台最早放款时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10073,'TJXX_30d_loanlasttime',10024,3,7,'30天内-多平台最近放款时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10074,'TJXX_30d_rejtimes',10024,1,7,'30天内-多平台驳回次数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10075,'TJXX_30d_rejplatforms',10024,1,7,'30天内-多平台驳回平台数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10076,'TJXX_30d_rejfirsttime',10024,3,7,'30天内-多平台最早驳回时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10077,'TJXX_30d_rejlasttime',10024,3,7,'30天内-多平台最近驳回时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10078,'TJXX_30d_regtimes_bank',10024,1,7,'30天内-多平台注册次数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10079,'TJXX_30d_regplatforms_bank',10024,1,7,'30天内-多平台注册平台数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10080,'TJXX_30d_reglasttime_bank',10024,3,7,'30天内-多平台最近注册时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10081,'TJXX_30d_regfirsttime_bank',10024,3,7,'30天内-多平台最早注册时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10082,'TJXX_30d_apptimes_bank',10024,1,7,'30天内-多平台申请次数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10083,'TJXX_30d_appplatforms_bank',10024,1,7,'30天内-多平台申请平台数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10084,'TJXX_30d_appmoney_bank',10024,2,7,'30天内-多平台最大申请金额区间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10085,'TJXX_30d_applasttime_bank',10024,3,7,'30天内-多平台最近申请时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10086,'TJXX_30d_appfirsttime_bank',10024,3,7,'30天内-多平台最早申请时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10087,'TJXX_30d_loantimes_bank',10024,1,7,'30天内-多平台放款次数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10088,'TJXX_30d_loanplatforms_bank',10024,1,7,'30天内-多平台放款平台数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10089,'TJXX_30d_loanmoney_bank',10024,2,7,'30天内-多平台最大放款金额区间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10090,'TJXX_30d_loanfirsttime_bank',10024,3,7,'30天内-多平台最早放款时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10091,'TJXX_30d_loanlasttime_bank',10024,3,7,'30天内-多平台最近放款时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10092,'TJXX_30d_rejtimes_bank',10024,1,7,'30天内-多平台驳回次数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10093,'TJXX_30d_rejplatforms_bank',10024,1,7,'30天内-多平台驳回平台数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10094,'TJXX_30d_rejfirsttime_bank',10024,3,7,'30天内-多平台最早驳回时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10095,'TJXX_30d_rejlasttime_bank',10024,3,7,'30天内-多平台最近驳回时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10096,'TJXX_30d_regtimes_nonbank',10024,1,7,'30天内-多平台注册次数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10097,'TJXX_30d_regplatforms_nonbank',10024,1,7,'30天内-多平台注册平台数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10098,'TJXX_30d_reglasttime_nonbank',10024,3,7,'30天内-多平台最近注册时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10099,'TJXX_30d_regfirsttime_nonbank',10024,3,7,'30天内-多平台最早注册时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10100,'TJXX_30d_apptimes_nonbank',10024,1,7,'30天内-多平台申请次数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10101,'TJXX_30d_appplatforms_nonbank',10024,1,7,'30天内-多平台申请平台数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10102,'TJXX_30d_appmoney_nonbank',10024,2,7,'30天内-多平台最大申请金额区间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10103,'TJXX_30d_applasttime_nonbank',10024,3,7,'30天内-多平台最近申请时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10104,'TJXX_30d_appfirsttime_nonbank',10024,3,7,'30天内-多平台最早申请时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10105,'TJXX_30d_loantimes_nonbank',10024,1,7,'30天内-多平台放款次数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10106,'TJXX_30d_loanplatforms_nonbank',10024,1,7,'30天内-多平台放款平台数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10107,'TJXX_30d_loanmoney_nonbank',10024,2,7,'30天内-多平台最大放款金额区间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10108,'TJXX_30d_loanfirsttime_nonbank',10024,3,7,'30天内-多平台最早放款时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10109,'TJXX_30d_loanlasttime_nonbank',10024,3,7,'30天内-多平台最近放款时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10110,'TJXX_30d_rejtimes_nonbank',10024,1,7,'30天内-多平台驳回次数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10111,'TJXX_30d_rejplatforms_nonbank',10024,1,7,'30天内-多平台驳回平台数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10112,'TJXX_30d_rejfirsttime_nonbank',10024,3,7,'30天内-多平台最早驳回时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10113,'TJXX_30d_rejlasttime_nonbank',10024,3,7,'30天内-多平台最近驳回时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10114,'TJXX_7d_regtimes',10024,1,7,'7天内-多平台注册次数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10115,'TJXX_7d_regplatforms',10024,1,7,'7天内-多平台注册平台数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10116,'TJXX_7d_reglasttime',10024,3,7,'7天内-多平台最近注册时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10117,'TJXX_7d_regfirsttime',10024,3,7,'7天内-多平台最早注册时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10118,'TJXX_7d_apptimes',10024,1,7,'7天内-多平台申请次数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10119,'TJXX_7d_appplatforms',10024,1,7,'7天内-多平台申请平台数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10120,'TJXX_7d_appmoney',10024,2,7,'7天内-多平台最大申请金额区间',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10121,'TJXX_7d_applasttime',10024,3,7,'7天内-多平台最近申请时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10122,'TJXX_7d_appfirsttime',10024,3,7,'7天内-多平台最早申请时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10123,'TJXX_7d_loantimes',10024,1,7,'7天内-多平台放款次数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10124,'TJXX_7d_loanplatforms',10024,1,7,'7天内-多平台放款平台数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10125,'TJXX_7d_loanmoney',10024,2,7,'7天内-多平台最大放款金额区间',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10126,'TJXX_7d_loanfirsttime',10024,3,7,'7天内-多平台最早放款时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10127,'TJXX_7d_loanlasttime',10024,3,7,'7天内-多平台最近放款时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10128,'TJXX_7d_rejtimes',10024,1,7,'7天内-多平台驳回次数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10129,'TJXX_7d_rejplatforms',10024,1,7,'7天内-多平台驳回平台数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10130,'TJXX_7d_rejfirsttime',10024,3,7,'7天内-多平台最早驳回时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10131,'TJXX_7d_rejlasttime',10024,3,7,'7天内-多平台最近驳回时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10132,'TJXX_7d_regtimes_bank',10024,1,7,'7天内-多平台注册次数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10133,'TJXX_7d_regplatforms_bank',10024,1,7,'7天内-多平台注册平台数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10134,'TJXX_7d_reglasttime_bank',10024,3,7,'7天内-多平台最近注册时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10135,'TJXX_7d_regfirsttime_bank',10024,3,7,'7天内-多平台最早注册时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10136,'TJXX_7d_apptimes_bank',10024,1,7,'7天内-多平台申请次数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10137,'TJXX_7d_appplatforms_bank',10024,1,7,'7天内-多平台申请平台数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10138,'TJXX_7d_appmoney_bank',10024,2,7,'7天内-多平台最大申请金额区间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10139,'TJXX_7d_applasttime_bank',10024,3,7,'7天内-多平台最近申请时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10140,'TJXX_7d_appfirsttime_bank',10024,3,7,'7天内-多平台最早申请时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10141,'TJXX_7d_loantimes_bank',10024,1,7,'7天内-多平台放款次数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10142,'TJXX_7d_loanplatforms_bank',10024,1,7,'7天内-多平台放款平台数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10143,'TJXX_7d_loanmoney_bank',10024,2,7,'7天内-多平台最大放款金额区间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10144,'TJXX_7d_loanfirsttime_bank',10024,3,7,'7天内-多平台最早放款时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10145,'TJXX_7d_loanlasttime_bank',10024,3,7,'7天内-多平台最近放款时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10146,'TJXX_7d_rejtimes_bank',10024,1,7,'7天内-多平台驳回次数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10147,'TJXX_7d_rejplatforms_bank',10024,1,7,'7天内-多平台驳回平台数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10148,'TJXX_7d_rejfirsttime_bank',10024,3,7,'7天内-多平台最早驳回时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10149,'TJXX_7d_rejlasttime_bank',10024,3,7,'7天内-多平台最近驳回时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10150,'TJXX_7d_regtimes_nonbank',10024,1,7,'7天内-多平台注册次数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10151,'TJXX_7d_regplatforms_nonbank',10024,1,7,'7天内-多平台注册平台数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10152,'TJXX_7d_reglasttime_nonbank',10024,3,7,'7天内-多平台最近注册时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10153,'TJXX_7d_regfirsttime_nonbank',10024,3,7,'7天内-多平台最早注册时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10154,'TJXX_7d_apptimes_nonbank',10024,1,7,'7天内-多平台申请次数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10155,'TJXX_7d_appplatforms_nonbank',10024,1,7,'7天内-多平台申请平台数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10156,'TJXX_7d_appmoney_nonbank',10024,2,7,'7天内-多平台最大申请金额区间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10157,'TJXX_7d_applasttime_nonbank',10024,3,7,'7天内-多平台最近申请时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10158,'TJXX_7d_appfirsttime_nonbank',10024,3,7,'7天内-多平台最早申请时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10159,'TJXX_7d_loantimes_nonbank',10024,1,7,'7天内-多平台放款次数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10160,'TJXX_7d_loanplatforms_nonbank',10024,1,7,'7天内-多平台放款平台数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10161,'TJXX_7d_loanmoney_nonbank',10024,2,7,'7天内-多平台最大放款金额区间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10162,'TJXX_7d_loanfirsttime_nonbank',10024,3,7,'7天内-多平台最早放款时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10163,'TJXX_7d_loanlasttime_nonbank',10024,3,7,'7天内-多平台最近放款时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10164,'TJXX_7d_rejtimes_nonbank',10024,1,7,'7天内-多平台驳回次数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10165,'TJXX_7d_rejplatforms_nonbank',10024,1,7,'7天内-多平台驳回平台数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10166,'TJXX_7d_rejfirsttime_nonbank',10024,3,7,'7天内-多平台最早驳回时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10167,'TJXX_7d_rejlasttime_nonbank',10024,3,7,'7天内-多平台最近驳回时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10168,'TJXX_180d_regtimes',10024,1,7,'180天内-多平台注册次数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10169,'TJXX_180d_regplatforms',10024,1,7,'180天内-多平台注册平台数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10170,'TJXX_180d_reglasttime',10024,3,7,'180天内-多平台最近注册时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10171,'TJXX_180d_regfirsttime',10024,3,7,'180天内-多平台最早注册时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10172,'TJXX_180d_apptimes',10024,1,7,'180天内-多平台申请次数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10173,'TJXX_180d_appplatforms',10024,1,7,'180天内-多平台申请平台数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10174,'TJXX_180d_appmoney',10024,2,7,'180天内-多平台最大申请金额区间',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10175,'TJXX_180d_applasttime',10024,3,7,'180天内-多平台最近申请时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10176,'TJXX_180d_appfirsttime',10024,3,7,'180天内-多平台最早申请时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10177,'TJXX_180d_loantimes',10024,1,7,'180天内-多平台放款次数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10178,'TJXX_180d_loanplatforms',10024,1,7,'180天内-多平台放款平台数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10179,'TJXX_180d_loanmoney',10024,2,7,'180天内-多平台最大放款金额区间',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10180,'TJXX_180d_loanfirsttime',10024,3,7,'180天内-多平台最早放款时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10181,'TJXX_180d_loanlasttime',10024,3,7,'180天内-多平台最近放款时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10182,'TJXX_180d_rejtimes',10024,1,7,'180天内-多平台驳回次数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10183,'TJXX_180d_rejplatforms',10024,1,7,'180天内-多平台驳回平台数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10184,'TJXX_180d_rejfirsttime',10024,3,7,'180天内-多平台最早驳回时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10185,'TJXX_180d_rejlasttime',10024,3,7,'180天内-多平台最近驳回时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10186,'TJXX_180d_regtimes_bank',10024,1,7,'180天内-多平台注册次数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10187,'TJXX_180d_regplatforms_bank',10024,1,7,'180天内-多平台注册平台数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10188,'TJXX_180d_reglasttime_bank',10024,3,7,'180天内-多平台最近注册时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10189,'TJXX_180d_regfirsttime_bank',10024,3,7,'180天内-多平台最早注册时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10190,'TJXX_180d_apptimes_bank',10024,1,7,'180天内-多平台申请次数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10191,'TJXX_180d_appplatforms_bank',10024,1,7,'180天内-多平台申请平台数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10192,'TJXX_180d_appmoney_bank',10024,2,7,'180天内-多平台最大申请金额区间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10193,'TJXX_180d_applasttime_bank',10024,3,7,'180天内-多平台最近申请时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10194,'TJXX_180d_appfirsttime_bank',10024,3,7,'180天内-多平台最早申请时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10195,'TJXX_180d_loantimes_bank',10024,1,7,'180天内-多平台放款次数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10196,'TJXX_180d_loanplatforms_bank',10024,1,7,'180天内-多平台放款平台数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10197,'TJXX_180d_loanmoney_bank',10024,2,7,'180天内-多平台最大放款金额区间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10198,'TJXX_180d_loanfirsttime_bank',10024,3,7,'180天内-多平台最早放款时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10199,'TJXX_180d_loanlasttime_bank',10024,3,7,'180天内-多平台最近放款时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10200,'TJXX_180d_rejtimes_bank',10024,1,7,'180天内-多平台驳回次数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10201,'TJXX_180d_rejplatforms_bank',10024,1,7,'180天内-多平台驳回平台数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10202,'TJXX_180d_rejfirsttime_bank',10024,3,7,'180天内-多平台最早驳回时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10203,'TJXX_180d_rejlasttime_bank',10024,3,7,'180天内-多平台最近驳回时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10204,'TJXX_180d_regtimes_nonbank',10024,1,7,'180天内-多平台注册次数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10205,'TJXX_180d_regplatforms_nonbank',10024,1,7,'180天内-多平台注册平台数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10206,'TJXX_180d_reglasttime_nonbank',10024,3,7,'180天内-多平台最近注册时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10207,'TJXX_180d_regfirsttime_nonbank',10024,3,7,'180天内-多平台最早注册时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10208,'TJXX_180d_apptimes_nonbank',10024,1,7,'180天内-多平台申请次数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10209,'TJXX_180d_appplatforms_nonbank',10024,1,7,'180天内-多平台申请平台数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10210,'TJXX_180d_appmoney_nonbank',10024,2,7,'180天内-多平台最大申请金额区间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10211,'TJXX_180d_applasttime_nonbank',10024,3,7,'180天内-多平台最近申请时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10212,'TJXX_180d_appfirsttime_nonbank',10024,3,7,'180天内-多平台最早申请时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10213,'TJXX_180d_loantimes_nonbank',10024,1,7,'180天内-多平台放款次数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10214,'TJXX_180d_loanplatforms_nonbank',10024,1,7,'180天内-多平台放款平台数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10215,'TJXX_180d_loanmoney_nonbank',10024,2,7,'180天内-多平台最大放款金额区间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10216,'TJXX_180d_loanfirsttime_nonbank',10024,3,7,'180天内-多平台最早放款时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10217,'TJXX_180d_loanlasttime_nonbank',10024,3,7,'180天内-多平台最近放款时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10218,'TJXX_180d_rejtimes_nonbank',10024,1,7,'180天内-多平台驳回次数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10219,'TJXX_180d_rejplatforms_nonbank',10024,1,7,'180天内-多平台驳回平台数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10220,'TJXX_180d_rejfirsttime_nonbank',10024,3,7,'180天内-多平台最早驳回时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10221,'TJXX_180d_rejlasttime_nonbank',10024,3,7,'180天内-多平台最近驳回时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10222,'TJXX_3d_regtimes',10024,1,7,'3天内-多平台注册次数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10223,'TJXX_3d_regplatforms',10024,1,7,'3天内-多平台注册平台数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10224,'TJXX_3d_reglasttime',10024,3,7,'3天内-多平台最近注册时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10225,'TJXX_3d_regfirsttime',10024,3,7,'3天内-多平台最早注册时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10226,'TJXX_3d_apptimes',10024,1,7,'3天内-多平台申请次数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10227,'TJXX_3d_appplatforms',10024,1,7,'3天内-多平台申请平台数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10228,'TJXX_3d_appmoney',10024,2,7,'3天内-多平台最大申请金额区间',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10229,'TJXX_3d_applasttime',10024,3,7,'3天内-多平台最近申请时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10230,'TJXX_3d_appfirsttime',10024,3,7,'3天内-多平台最早申请时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10231,'TJXX_3d_loantimes',10024,1,7,'3天内-多平台放款次数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10232,'TJXX_3d_loanplatforms',10024,1,7,'3天内-多平台放款平台数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10233,'TJXX_3d_loanmoney',10024,2,7,'3天内-多平台最大放款金额区间',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10234,'TJXX_3d_loanfirsttime',10024,3,7,'3天内-多平台最早放款时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10235,'TJXX_3d_loanlasttime',10024,3,7,'3天内-多平台最近放款时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10236,'TJXX_3d_rejtimes',10024,1,7,'3天内-多平台驳回次数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10237,'TJXX_3d_rejplatforms',10024,1,7,'3天内-多平台驳回平台数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10238,'TJXX_3d_rejfirsttime',10024,3,7,'3天内-多平台最早驳回时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10239,'TJXX_3d_rejlasttime',10024,3,7,'3天内-多平台最近驳回时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10240,'TJXX_3d_regtimes_bank',10024,1,7,'3天内-多平台注册次数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10241,'TJXX_3d_regplatforms_bank',10024,1,7,'3天内-多平台注册平台数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10242,'TJXX_3d_reglasttime_bank',10024,3,7,'3天内-多平台最近注册时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10243,'TJXX_3d_regfirsttime_bank',10024,3,7,'3天内-多平台最早注册时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10244,'TJXX_3d_apptimes_bank',10024,1,7,'3天内-多平台申请次数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10245,'TJXX_3d_appplatforms_bank',10024,1,7,'3天内-多平台申请平台数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10246,'TJXX_3d_appmoney_bank',10024,2,7,'3天内-多平台最大申请金额区间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10247,'TJXX_3d_applasttime_bank',10024,3,7,'3天内-多平台最近申请时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10248,'TJXX_3d_appfirsttime_bank',10024,3,7,'3天内-多平台最早申请时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10249,'TJXX_3d_loantimes_bank',10024,1,7,'3天内-多平台放款次数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10250,'TJXX_3d_loanplatforms_bank',10024,1,7,'3天内-多平台放款平台数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10251,'TJXX_3d_loanmoney_bank',10024,2,7,'3天内-多平台最大放款金额区间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10252,'TJXX_3d_loanfirsttime_bank',10024,3,7,'3天内-多平台最早放款时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10253,'TJXX_3d_loanlasttime_bank',10024,3,7,'3天内-多平台最近放款时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10254,'TJXX_3d_rejtimes_bank',10024,1,7,'3天内-多平台驳回次数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10255,'TJXX_3d_rejplatforms_bank',10024,1,7,'3天内-多平台驳回平台数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10256,'TJXX_3d_rejfirsttime_bank',10024,3,7,'3天内-多平台最早驳回时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10257,'TJXX_3d_rejlasttime_bank',10024,3,7,'3天内-多平台最近驳回时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10258,'TJXX_3d_regtimes_nonbank',10024,1,7,'3天内-多平台注册次数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10259,'TJXX_3d_regplatforms_nonbank',10024,1,7,'3天内-多平台注册平台数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10260,'TJXX_3d_reglasttime_nonbank',10024,3,7,'3天内-多平台最近注册时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10261,'TJXX_3d_regfirsttime_nonbank',10024,3,7,'3天内-多平台最早注册时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10262,'TJXX_3d_apptimes_nonbank',10024,1,7,'3天内-多平台申请次数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10263,'TJXX_3d_appplatforms_nonbank',10024,1,7,'3天内-多平台申请平台数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10264,'TJXX_3d_appmoney_nonbank',10024,2,7,'3天内-多平台最大申请金额区间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10265,'TJXX_3d_applasttime_nonbank',10024,3,7,'3天内-多平台最近申请时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10266,'TJXX_3d_appfirsttime_nonbank',10024,3,7,'3天内-多平台最早申请时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10267,'TJXX_3d_loantimes_nonbank',10024,1,7,'3天内-多平台放款次数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10268,'TJXX_3d_loanplatforms_nonbank',10024,1,7,'3天内-多平台放款平台数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10269,'TJXX_3d_loanmoney_nonbank',10024,2,7,'3天内-多平台最大放款金额区间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10270,'TJXX_3d_loanfirsttime_nonbank',10024,3,7,'3天内-多平台最早放款时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10271,'TJXX_3d_loanlasttime_nonbank',10024,3,7,'3天内-多平台最近放款时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10272,'TJXX_3d_rejtimes_nonbank',10024,1,7,'3天内-多平台驳回次数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10273,'TJXX_3d_rejplatforms_nonbank',10024,1,7,'3天内-多平台驳回平台数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10274,'TJXX_3d_rejfirsttime_nonbank',10024,3,7,'3天内-多平台最早驳回时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10275,'TJXX_3d_rejlasttime_nonbank',10024,3,7,'3天内-多平台最近驳回时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10276,'TJXX_regtimes',10024,1,7,'多平台注册次数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10277,'TJXX_regplatforms',10024,1,7,'多平台注册平台数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10278,'TJXX_reglasttime',10024,3,7,'多平台最近注册时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10279,'TJXX_regfirsttime',10024,3,7,'多平台最早注册时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10280,'TJXX_apptimes',10024,1,7,'多平台申请次数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10281,'TJXX_appplatforms',10024,1,7,'多平台申请平台数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10282,'TJXX_appmoney',10024,2,7,'多平台最大申请金额区间',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10283,'TJXX_applasttime',10024,3,7,'多平台最近申请时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10284,'TJXX_appfirsttime',10024,3,7,'多平台最早申请时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10285,'TJXX_loantimes',10024,1,7,'多平台放款次数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10286,'TJXX_loanplatforms',10024,1,7,'多平台放款平台数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10287,'TJXX_loanmoney',10024,2,7,'多平台最大放款金额区间',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10288,'TJXX_loanfirsttime',10024,3,7,'多平台最早放款时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10289,'TJXX_loanlasttime',10024,3,7,'多平台最近放款时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10290,'TJXX_rejtimes',10024,1,7,'多平台驳回次数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10291,'TJXX_rejplatforms',10024,1,7,'多平台驳回平台数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10292,'TJXX_rejfirsttime',10024,3,7,'多平台最早驳回时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10293,'TJXX_rejlasttime',10024,3,7,'多平台最近驳回时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10294,'TJXX_regtimes_bank',10024,1,7,'多平台注册次数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10295,'TJXX_regplatforms_bank',10024,1,7,'多平台注册平台数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10296,'TJXX_reglasttime_bank',10024,3,7,'多平台最近注册时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10297,'TJXX_regfirsttime_bank',10024,3,7,'多平台最早注册时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10298,'TJXX_apptimes_bank',10024,1,7,'多平台申请次数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10299,'TJXX_appplatforms_bank',10024,1,7,'多平台申请平台数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10300,'TJXX_appmoney_bank',10024,2,7,'多平台最大申请金额区间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10301,'TJXX_applasttime_bank',10024,3,7,'多平台最近申请时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10302,'TJXX_appfirsttime_bank',10024,3,7,'多平台最早申请时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10303,'TJXX_loantimes_bank',10024,1,7,'多平台放款次数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10304,'TJXX_loanplatforms_bank',10024,1,7,'多平台放款平台数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10305,'TJXX_loanmoney_bank',10024,2,7,'多平台最大放款金额区间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10306,'TJXX_loanfirsttime_bank',10024,3,7,'多平台最早放款时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10307,'TJXX_loanlasttime_bank',10024,3,7,'多平台最近放款时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10308,'TJXX_rejtimes_bank',10024,1,7,'多平台驳回次数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10309,'TJXX_rejplatforms_bank',10024,1,7,'多平台驳回平台数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10310,'TJXX_rejfirsttime_bank',10024,3,7,'多平台最早驳回时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10311,'TJXX_rejlasttime_bank',10024,3,7,'多平台最近驳回时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10312,'TJXX_regtimes_nonbank',10024,1,7,'多平台注册次数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10313,'TJXX_regplatforms_nonbank',10024,1,7,'多平台注册平台数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10314,'TJXX_reglasttime_nonbank',10024,3,7,'多平台最近注册时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10315,'TJXX_regfirsttime_nonbank',10024,3,7,'多平台最早注册时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10316,'TJXX_apptimes_nonbank',10024,1,7,'多平台申请次数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10317,'TJXX_appplatforms_nonbank',10024,1,7,'多平台申请平台数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10318,'TJXX_appmoney_nonbank',10024,2,7,'多平台最大申请金额区间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10319,'TJXX_applasttime_nonbank',10024,3,7,'多平台最近申请时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10320,'TJXX_appfirsttime_nonbank',10024,3,7,'多平台最早申请时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10321,'TJXX_loantimes_nonbank',10024,1,7,'多平台放款次数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10322,'TJXX_loanplatforms_nonbank',10024,1,7,'多平台放款平台数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10323,'TJXX_loanmoney_nonbank',10024,2,7,'多平台最大放款金额区间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10324,'TJXX_loanfirsttime_nonbank',10024,3,7,'多平台最早放款时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10325,'TJXX_loanlasttime_nonbank',10024,3,7,'多平台最近放款时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10326,'TJXX_rejtimes_nonbank',10024,1,7,'多平台驳回次数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10327,'TJXX_rejplatforms_nonbank',10024,1,7,'多平台驳回平台数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10328,'TJXX_rejfirsttime_nonbank',10024,3,7,'多平台最早驳回时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10329,'TJXX_rejlasttime_nonbank',10024,3,7,'多平台最近驳回时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10330,'TJXX_90d_regtimes',10024,1,7,'90天内-多平台注册次数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10331,'TJXX_90d_regplatforms',10024,1,7,'90天内-多平台注册平台数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10332,'TJXX_90d_reglasttime',10024,3,7,'90天内-多平台最近注册时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10333,'TJXX_90d_regfirsttime',10024,3,7,'90天内-多平台最早注册时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10334,'TJXX_90d_apptimes',10024,1,7,'90天内-多平台申请次数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10335,'TJXX_90d_appplatforms',10024,1,7,'90天内-多平台申请平台数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10336,'TJXX_90d_appmoney',10024,2,7,'90天内-多平台最大申请金额区间',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10337,'TJXX_90d_applasttime',10024,3,7,'90天内-多平台最近申请时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10338,'TJXX_90d_appfirsttime',10024,3,7,'90天内-多平台最早申请时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10339,'TJXX_90d_loantimes',10024,1,7,'90天内-多平台放款次数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10340,'TJXX_90d_loanplatforms',10024,1,7,'90天内-多平台放款平台数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10341,'TJXX_90d_loanmoney',10024,2,7,'90天内-多平台最大放款金额区间',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10342,'TJXX_90d_loanfirsttime',10024,3,7,'90天内-多平台最早放款时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10343,'TJXX_90d_loanlasttime',10024,3,7,'90天内-多平台最近放款时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10344,'TJXX_90d_rejtimes',10024,1,7,'90天内-多平台驳回次数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10345,'TJXX_90d_rejplatforms',10024,1,7,'90天内-多平台驳回平台数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10346,'TJXX_90d_rejfirsttime',10024,3,7,'90天内-多平台最早驳回时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10347,'TJXX_90d_rejlasttime',10024,3,7,'90天内-多平台最近驳回时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10348,'TJXX_90d_regtimes_bank',10024,1,7,'90天内-多平台注册次数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10349,'TJXX_90d_regplatforms_bank',10024,1,7,'90天内-多平台注册平台数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10350,'TJXX_90d_reglasttime_bank',10024,3,7,'90天内-多平台最近注册时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10351,'TJXX_90d_regfirsttime_bank',10024,3,7,'90天内-多平台最早注册时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10352,'TJXX_90d_apptimes_bank',10024,1,7,'90天内-多平台申请次数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10353,'TJXX_90d_appplatforms_bank',10024,1,7,'90天内-多平台申请平台数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10354,'TJXX_90d_appmoney_bank',10024,2,7,'90天内-多平台最大申请金额区间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10355,'TJXX_90d_applasttime_bank',10024,3,7,'90天内-多平台最近申请时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10356,'TJXX_90d_appfirsttime_bank',10024,3,7,'90天内-多平台最早申请时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10357,'TJXX_90d_loantimes_bank',10024,1,7,'90天内-多平台放款次数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10358,'TJXX_90d_loanplatforms_bank',10024,1,7,'90天内-多平台放款平台数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10359,'TJXX_90d_loanmoney_bank',10024,2,7,'90天内-多平台最大放款金额区间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10360,'TJXX_90d_loanfirsttime_bank',10024,3,7,'90天内-多平台最早放款时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10361,'TJXX_90d_loanlasttime_bank',10024,3,7,'90天内-多平台最近放款时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10362,'TJXX_90d_rejtimes_bank',10024,1,7,'90天内-多平台驳回次数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10363,'TJXX_90d_rejplatforms_bank',10024,1,7,'90天内-多平台驳回平台数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10364,'TJXX_90d_rejfirsttime_bank',10024,3,7,'90天内-多平台最早驳回时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10365,'TJXX_90d_rejlasttime_bank',10024,3,7,'90天内-多平台最近驳回时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10366,'TJXX_90d_regtimes_nonbank',10024,1,7,'90天内-多平台注册次数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10367,'TJXX_90d_regplatforms_nonbank',10024,1,7,'90天内-多平台注册平台数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10368,'TJXX_90d_reglasttime_nonbank',10024,3,7,'90天内-多平台最近注册时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10369,'TJXX_90d_regfirsttime_nonbank',10024,3,7,'90天内-多平台最早注册时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10370,'TJXX_90d_apptimes_nonbank',10024,1,7,'90天内-多平台申请次数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10371,'TJXX_90d_appplatforms_nonbank',10024,1,7,'90天内-多平台申请平台数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10372,'TJXX_90d_appmoney_nonbank',10024,2,7,'90天内-多平台最大申请金额区间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10373,'TJXX_90d_applasttime_nonbank',10024,3,7,'90天内-多平台最近申请时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10374,'TJXX_90d_appfirsttime_nonbank',10024,3,7,'90天内-多平台最早申请时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10375,'TJXX_90d_loantimes_nonbank',10024,1,7,'90天内-多平台放款次数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10376,'TJXX_90d_loanplatforms_nonbank',10024,1,7,'90天内-多平台放款平台数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10377,'TJXX_90d_loanmoney_nonbank',10024,2,7,'90天内-多平台最大放款金额区间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10378,'TJXX_90d_loanfirsttime_nonbank',10024,3,7,'90天内-多平台最早放款时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10379,'TJXX_90d_loanlasttime_nonbank',10024,3,7,'90天内-多平台最近放款时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10380,'TJXX_90d_rejtimes_nonbank',10024,1,7,'90天内-多平台驳回次数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10381,'TJXX_90d_rejplatforms_nonbank',10024,1,7,'90天内-多平台驳回平台数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10382,'TJXX_90d_rejfirsttime_nonbank',10024,3,7,'90天内-多平台最早驳回时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10383,'TJXX_90d_rejlasttime_nonbank',10024,3,7,'90天内-多平台最近驳回时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10384,'TJXX_60d_regtimes',10024,1,7,'60天内-多平台注册次数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10385,'TJXX_60d_regplatforms',10024,1,7,'60天内-多平台注册平台数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10386,'TJXX_60d_reglasttime',10024,3,7,'60天内-多平台最近注册时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10387,'TJXX_60d_regfirsttime',10024,3,7,'60天内-多平台最早注册时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10388,'TJXX_60d_apptimes',10024,1,7,'60天内-多平台申请次数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10389,'TJXX_60d_appplatforms',10024,1,7,'60天内-多平台申请平台数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10390,'TJXX_60d_appmoney',10024,2,7,'60天内-多平台最大申请金额区间',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10391,'TJXX_60d_applasttime',10024,3,7,'60天内-多平台最近申请时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10392,'TJXX_60d_appfirsttime',10024,3,7,'60天内-多平台最早申请时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10393,'TJXX_60d_loantimes',10024,1,7,'60天内-多平台放款次数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10394,'TJXX_60d_loanplatforms',10024,1,7,'60天内-多平台放款平台数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10395,'TJXX_60d_loanmoney',10024,2,7,'60天内-多平台最大放款金额区间',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10396,'TJXX_60d_loanfirsttime',10024,3,7,'60天内-多平台最早放款时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10397,'TJXX_60d_loanlasttime',10024,3,7,'60天内-多平台最近放款时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10398,'TJXX_60d_rejtimes',10024,1,7,'60天内-多平台驳回次数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10399,'TJXX_60d_rejplatforms',10024,1,7,'60天内-多平台驳回平台数',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10400,'TJXX_60d_rejfirsttime',10024,3,7,'60天内-多平台最早驳回时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10401,'TJXX_60d_rejlasttime',10024,3,7,'60天内-多平台最近驳回时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10402,'TJXX_60d_regtimes_bank',10024,1,7,'60天内-多平台注册次数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10403,'TJXX_60d_regplatforms_bank',10024,1,7,'60天内-多平台注册平台数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10404,'TJXX_60d_reglasttime_bank',10024,3,7,'60天内-多平台最近注册时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10405,'TJXX_60d_regfirsttime_bank',10024,3,7,'60天内-多平台最早注册时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10406,'TJXX_60d_apptimes_bank',10024,1,7,'60天内-多平台申请次数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10407,'TJXX_60d_appplatforms_bank',10024,1,7,'60天内-多平台申请平台数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10408,'TJXX_60d_appmoney_bank',10024,2,7,'60天内-多平台最大申请金额区间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10409,'TJXX_60d_applasttime_bank',10024,3,7,'60天内-多平台最近申请时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10410,'TJXX_60d_appfirsttime_bank',10024,3,7,'60天内-多平台最早申请时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10411,'TJXX_60d_loantimes_bank',10024,1,7,'60天内-多平台放款次数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10412,'TJXX_60d_loanplatforms_bank',10024,1,7,'60天内-多平台放款平台数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10413,'TJXX_60d_loanmoney_bank',10024,2,7,'60天内-多平台最大放款金额区间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10414,'TJXX_60d_loanfirsttime_bank',10024,3,7,'60天内-多平台最早放款时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10415,'TJXX_60d_loanlasttime_bank',10024,3,7,'60天内-多平台最近放款时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10416,'TJXX_60d_rejtimes_bank',10024,1,7,'60天内-多平台驳回次数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10417,'TJXX_60d_rejplatforms_bank',10024,1,7,'60天内-多平台驳回平台数-银行',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10418,'TJXX_60d_rejfirsttime_bank',10024,3,7,'60天内-多平台最早驳回时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10419,'TJXX_60d_rejlasttime_bank',10024,3,7,'60天内-多平台最近驳回时间-银行',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10420,'TJXX_60d_regtimes_nonbank',10024,1,7,'60天内-多平台注册次数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10421,'TJXX_60d_regplatforms_nonbank',10024,1,7,'60天内-多平台注册平台数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10422,'TJXX_60d_reglasttime_nonbank',10024,3,7,'60天内-多平台最近注册时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10423,'TJXX_60d_regfirsttime_nonbank',10024,3,7,'60天内-多平台最早注册时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10424,'TJXX_60d_apptimes_nonbank',10024,1,7,'60天内-多平台申请次数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10425,'TJXX_60d_appplatforms_nonbank',10024,1,7,'60天内-多平台申请平台数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10426,'TJXX_60d_appmoney_nonbank',10024,2,7,'60天内-多平台最大申请金额区间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10427,'TJXX_60d_applasttime_nonbank',10024,3,7,'60天内-多平台最近申请时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10428,'TJXX_60d_appfirsttime_nonbank',10024,3,7,'60天内-多平台最早申请时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10429,'TJXX_60d_loantimes_nonbank',10024,1,7,'60天内-多平台放款次数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10430,'TJXX_60d_loanplatforms_nonbank',10024,1,7,'60天内-多平台放款平台数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10431,'TJXX_60d_loanmoney_nonbank',10024,2,7,'60天内-多平台最大放款金额区间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10432,'TJXX_60d_loanfirsttime_nonbank',10024,3,7,'60天内-多平台最早放款时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10433,'TJXX_60d_loanlasttime_nonbank',10024,3,7,'60天内-多平台最近放款时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10434,'TJXX_60d_rejtimes_nonbank',10024,1,7,'60天内-多平台驳回次数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10435,'TJXX_60d_rejplatforms_nonbank',10024,1,7,'60天内-多平台驳回平台数-非银',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10436,'TJXX_60d_rejfirsttime_nonbank',10024,3,7,'60天内-多平台最早驳回时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10437,'TJXX_60d_rejlasttime_nonbank',10024,3,7,'60天内-多平台最近驳回时间-非银',1,1,'集奥聚合','2018-01-01 00:00:00','2017/3/24 00:00:00  特殊'),
	(10438,'telloc',10025,2,9,'号码标签-归属地',1,1,'集奥聚合','2018-01-01 00:00:00','河北石家庄联通'),
	(10439,'itag_ids ',10025,2,9,'号码标签-金融标签组',1,1,'集奥聚合','2018-01-01 00:00:00','标签组，如[]'),
	(10440,'name ',10025,2,9,'号码标签-商户名称',1,1,'集奥聚合','2018-01-01 00:00:00','商户名称  例：招商银行 '),
	(10441,'catnames ',10025,2,9,'号码标签-号码分类信息',0,1,'集奥聚合','2018-01-01 00:00:00','号码分类信息   例：银行热线 '),
	(10442,'flag_fid',10025,2,9,'号码标签-标记ID',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10443,'flag_type',10025,2,9,'号码标签-标记说明',1,1,'集奥聚合','2018-01-01 00:00:00','标记说明，如骚扰电话'),
	(10444,'flag_num',10025,1,9,'号码标签-标记数量',1,1,'集奥聚合','2018-01-01 00:00:00','标记用户数'),
	(10445,'status ',10025,2,9,'号码标签-标记状态',1,1,'集奥聚合','2018-01-01 00:00:00','状态   0为正常，非0为错误 '),
	(10446,'telnum',10025,2,9,'号码标签-号码',1,1,'集奥聚合','2018-01-01 00:00:00',' 电话号码'),
	(10447,'realName',10026,2,9,'号码风险标签-真实姓名',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10448,'callertag_tagType',10026,2,9,'号码风险标签-标签类型',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10449,'callertag_updateTime',10026,3,9,'号码风险标签-更新时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017-03-24 00:00:00  特殊'),
	(10450,'black_updateTime',10026,3,9,'号码风险标签-虚假手机号更新时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017-03-24 00:00:00  特殊'),
	(10451,'alt_updateTime',10026,3,9,'号码风险标签-小号更新时间',1,1,'集奥聚合','2018-01-01 00:00:00','2017-03-24 00:00:00  特殊'),
	(10452,'idNumber',10026,2,9,'号码风险标签-身份证号码',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10453,'phone1',10026,2,9,'号码风险标签-手机号',1,1,'集奥聚合','2018-01-01 00:00:00','暂无返回值描述'),
	(10454,'ownPhoneNumber',10027,1,4,'自然人拥有手机号码个数',1,1,'集奥聚合','2018-01-01 00:00:00','0：不大于2个，1：大于2个，99：手机号已离网'),
	(10455,'phoneDepartStatus',10028,1,3,'手机号码出境状态查询',1,1,'集奥聚合','2018-01-01 00:00:00','1：在境外，2：在境内'),
	(10456,'phoneDataLevel',10029,2,4,'手机号码数据流量档次查询',0,0,'集奥聚合','2018-01-01 00:00:00','01：[0,100)，02：[100,400)，03：[400,1000)，04：[1000,2000)，05：[2000,+)'),
	(10457,'consumerGrade',10030,2,4,'消费等级查询',1,1,'集奥聚合','2018-01-01 00:00:00','01：[0,50]，02：(50,100)，011：(20,50]，010：[0,20]，004：(160,+)，003：(80,160]，002：(40,80], 001：[0,40], 4：(200,+), 3：(100,200],  99:手机号已经离网'),
	(10458,'cpws_sortTime',10031,4,10,'裁判文书—审结时间',1,1,'北京集奥聚合','2018-04-01 00:00:00','1495123200000'),
	(10459,'cpws_body',10031,2,10,'裁判文书—内容',1,1,'北京集奥聚合','2018-04-01 00:00:00','内容字符串'),
	(10460,'cpws_caseType',10031,2,10,'裁判文书—案由',1,1,'北京集奥聚合','2018-04-01 00:00:00','内容字符串'),
	(10461,'cpws_sortTimeString',10031,4,10,'裁判文书—审结时间日期',1,1,'北京集奥聚合','2018-04-01 00:00:00','2017年05月19日'),
	(10462,'cpws_trialProcedure',10031,2,10,'裁判文书—审理程序',1,1,'北京集奥聚合','2018-04-01 00:00:00','二审'),
	(10463,'cpws_court',10031,2,10,'裁判文书—法院名称',1,1,'北京集奥聚合','2018-04-01 00:00:00','法院名称'),
	(10464,'cpws_entryId',10031,2,10,'裁判文书—裁判文书ID',1,1,'北京集奥聚合','2018-04-01 00:00:00','案件流程唯一标识'),
	(10465,'cpws_dataType',10031,2,10,'裁判文书—维度',1,1,'北京集奥聚合','2018-04-01 00:00:00','cpws'),
	(10466,'cpws_caseCause',10031,2,10,'裁判文书—案由',1,1,'北京集奥聚合','2018-04-01 00:00:00','内容字符串'),
	(10467,'cpws_title',10031,2,10,'裁判文书—标题',1,1,'北京集奥聚合','2018-04-01 00:00:00','标题字符串'),
	(10468,'cpws_partyId',10031,2,10,'裁判文书—当事人ID',1,1,'北京集奥聚合','2018-04-01 00:00:00','c20173704minxiazhong115_t20171016_pzhaoyubo_rt_111'),
	(10469,'cpws_judge',10031,2,10,'裁判文书—审判员',1,1,'北京集奥聚合','2018-04-01 00:00:00','审判员'),
	(10470,'cpws_matchRatio',10031,1,10,'裁判文书—匹配度',1,1,'北京集奥聚合','2018-04-01 00:00:00','0.99'),
	(10471,'cpws_pname',10031,2,10,'裁判文书—当事人',1,1,'北京集奥聚合','2018-04-01 00:00:00','详细请参照当事人数据字典详细说明'),
	(10472,'cpws_caseNo',10031,2,10,'裁判文书—案号',1,1,'北京集奥聚合','2018-04-01 00:00:00','（2017）鲁04民辖终115号'),
	(10473,'cpws_judgeResult',10031,2,10,'裁判文书—判决结果',1,1,'北京集奥聚合','2018-04-01 00:00:00','结果字符串'),
	(10474,'cpws_yiju',10031,2,10,'裁判文书—依据',1,1,'北京集奥聚合','2018-04-01 00:00:00','依据字符串'),
	(10475,'cpws_courtRank',10031,2,10,'裁判文书—法院等级',1,1,'北京集奥聚合','2018-04-01 00:00:00','1: 最高院2: 高院3: 中院4:其他'),
	(10477,'zxgg_sortTime',10031,4,10,'执行公告-立案时间',1,1,'北京集奥聚合','2018-04-01 00:00:00','1495123200000'),
	(10478,'zxgg_body',10031,2,10,'执行公告-内容',1,1,'北京集奥聚合','2018-04-01 00:00:00','概要'),
	(10479,'zxgg_sortTimeString',10031,2,10,'执行公告-立案时间',1,1,'北京集奥聚合','2018-04-01 00:00:00','2017年05月19日'),
	(10480,'zxgg_court',10031,2,10,'执行公告-执行法院名称',1,1,'北京集奥聚合','2018-04-01 00:00:00','法院名称'),
	(10481,'zxgg_idcardNo',10031,2,10,'执行公告-身份证/组织机构代码',1,1,'北京集奥聚合','2018-04-01 00:00:00','2305231993****231X'),
	(10482,'zxgg_entryId',10031,2,10,'执行公告-执行公告ID',1,1,'北京集奥聚合','2018-04-01 00:00:00','执行公告唯一的标识'),
	(10483,'zxgg_title',10031,2,10,'执行公告-标题',1,1,'北京集奥聚合','2018-04-01 00:00:00','标题字符串'),
	(10484,'zxgg_dataType',10031,2,10,'执行公告-维度',1,1,'北京集奥聚合','2018-04-01 00:00:00','zxgg'),
	(10485,'zxgg_matchRatio',10031,2,10,'执行公告-匹配度',1,1,'北京集奥聚合','2018-04-01 00:00:00','0.99'),
	(10486,'zxgg_pname',10031,2,10,'执行公告-当事人',1,1,'北京集奥聚合','2018-04-01 00:00:00','董庆彬'),
	(10487,'zxgg_caseNo',10031,2,10,'执行公告-案号',1,1,'北京集奥聚合','2018-04-01 00:00:00','（2017）鲁04民辖终115号'),
	(10488,'zxgg_caseState',10031,2,10,'执行公告-案件状态',1,1,'北京集奥聚合','2018-04-01 00:00:00','内容字符串'),
	(10489,'zxgg_execMoney',10031,1,10,'执行公告-执行标的',1,1,'北京集奥聚合','2018-04-01 00:00:00','9000.0'),
	(10491,'shixin_sortTime',10031,4,10,'失信公告-立案时间',1,1,'北京集奥聚合','2018-04-01 00:00:00','1495123200000'),
	(10492,'shixin_body',10031,2,10,'失信公告-内容',1,1,'北京集奥聚合','2018-04-01 00:00:00','概要'),
	(10493,'shixin_sortTimeString',10031,4,10,'失信公告-立案时间',1,1,'北京集奥聚合','2018-04-01 00:00:00','2017年05月19日'),
	(10494,'shixin_sex',10031,2,10,'失信公告-性别',1,1,'北京集奥聚合','2018-04-01 00:00:00','男'),
	(10495,'shixin_lxqk',10031,2,10,'失信公告-被执行人的履行情况',1,1,'北京集奥聚合','2018-04-01 00:00:00','“全部未履行”，“部分未履行”，“全部已履行”，等等描述文字.'),
	(10496,'shixin_yjCode',10031,2,10,'失信公告-执行依据文号',1,1,'北京集奥聚合','2018-04-01 00:00:00','（2017）黑0523执367号'),
	(10497,'shixin_court',10031,2,10,'失信公告-执行法院名称',1,1,'北京集奥聚合','2018-04-01 00:00:00','法院名称'),
	(10498,'shixin_idcardNo',10031,2,10,'失信公告-身份证/组织机构代码',1,1,'北京集奥聚合','2018-04-01 00:00:00','2305231993****231X'),
	(10499,'shixin_entryId',10031,2,10,'失信公告-失信公告ID',1,1,'北京集奥聚合','2018-04-01 00:00:00','失信公告唯一的标识'),
	(10500,'shixin_yjdw',10031,2,10,'失信公告-做出执行依据单位',1,1,'北京集奥聚合','2018-04-01 00:00:00','内容字符串'),
	(10501,'shixin_dataType',10031,2,10,'失信公告-维度',1,1,'北京集奥聚合','2018-04-01 00:00:00','shixin'),
	(10502,'shixin_jtqx',10031,2,10,'失信公告-失信被执行人行为具体情形',1,1,'北京集奥聚合','2018-04-01 00:00:00','有履行能力而拒不履行生效法律文书确定义务的'),
	(10503,'shixin_yiwu',10031,2,10,'失信公告-生效法律文书确定的义务',1,1,'北京集奥聚合','2018-04-01 00:00:00','内容字符串'),
	(10504,'shixin_age',10031,1,10,'失信公告-年龄',1,1,'北京集奥聚合','2018-04-01 00:00:00','30'),
	(10505,'shixin_matchRatio',10031,1,10,'失信公告-匹配度',1,1,'北京集奥聚合','2018-04-01 00:00:00',' 0.99'),
	(10506,'shixin_province',10031,2,10,'失信公告-省份',1,1,'北京集奥聚合','2018-04-01 00:00:00','省份'),
	(10507,'shixin_pname',10031,2,10,'失信公告-被执行人姓名',1,1,'北京集奥聚合','2018-04-01 00:00:00','姓名'),
	(10508,'shixin_caseNo',10031,2,10,'失信公告-案号',1,1,'北京集奥聚合','2018-04-01 00:00:00','（2017）黑0523执367号'),
	(10509,'shixin_postTime',10031,4,10,'失信公告-发布时间',1,1,'北京集奥聚合','2018-04-01 00:00:00','1495641600000'),
	(10511,'bgt_sortTime',10031,4,10,'曝光台-立案日期',1,1,'北京集奥聚合','2018-04-01 00:00:00','2017年05月19日'),
	(10512,'bgt_body',10031,2,10,'曝光台-内容',1,1,'北京集奥聚合','2018-04-01 00:00:00','概要'),
	(10513,'bgt_sortTimeString',10031,4,10,'曝光台-立案日期',1,1,'北京集奥聚合','2018-04-01 00:00:00','2017年05月19日'),
	(10514,'bgt_bgDate',10031,4,10,'曝光台-曝光日期',1,1,'北京集奥聚合','2018-04-01 00:00:00','暂无返回值描述'),
	(10515,'bgt_partyType',10031,2,10,'曝光台-当事人类型',1,1,'北京集奥聚合','2018-04-01 00:00:00','内容字符串'),
	(10516,'bgt_court',10031,2,10,'曝光台-法院名称',1,1,'北京集奥聚合','2018-04-01 00:00:00','内容字符串'),
	(10517,'bgt_proposer',10031,2,10,'曝光台-申请人',1,1,'北京集奥聚合','2018-04-01 00:00:00','内容字符串'),
	(10518,'bgt_idcardNo',10031,2,10,'曝光台-身份证/组织机构代码',1,1,'北京集奥聚合','2018-04-01 00:00:00','2305231993****231X'),
	(10519,'bgt_entryId',10031,2,10,'曝光台-曝光台ID',1,1,'北京集奥聚合','2018-04-01 00:00:00','曝光台唯一标识'),
	(10520,'bgt_dataType',10031,2,10,'曝光台-维度',1,1,'北京集奥聚合','2018-04-01 00:00:00','bgt'),
	(10521,'bgt_caseCause',10031,2,10,'曝光台-案由',1,1,'北京集奥聚合','2018-04-01 00:00:00','内容字符串'),
	(10522,'bgt_unnexeMoney',10031,1,10,'曝光台-未执行金额',1,1,'北京集奥聚合','2018-04-01 00:00:00','9000.0'),
	(10523,'bgt_address',10031,2,10,'曝光台-地址',1,1,'北京集奥聚合','2018-04-01 00:00:00','地址'),
	(10524,'bgt_matchRatio',10031,1,10,'曝光台-匹配度',1,1,'北京集奥聚合','2018-04-01 00:00:00','0.99'),
	(10525,'bgt_pname',10031,2,10,'曝光台-当事人',1,1,'北京集奥聚合','2018-04-01 00:00:00','姓名'),
	(10526,'bgt_caseNo',10031,2,10,'曝光台-案号',1,1,'北京集奥聚合','2018-04-01 00:00:00','（2017）黑0523执367号'),
	(10527,'bgt_yiju',10031,2,10,'曝光台-依据',1,1,'北京集奥聚合','2018-04-01 00:00:00','内容字符串'),
	(10528,'bgt_execMoney',10031,1,10,'曝光台-标的金额',1,1,'北京集奥聚合','2018-04-01 00:00:00','9000.0');

/*!40000 ALTER TABLE `engine_raw_fields` ENABLE KEYS */;
UNLOCK TABLES;

# Dump of table engine_raw_inter
# ------------------------------------------------------------

DROP TABLE IF EXISTS `engine_raw_inter`;

CREATE TABLE `engine_raw_inter` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '字段编号',
  `name` varchar(255) DEFAULT NULL COMMENT '接口名称',
  `describ` varchar(255) DEFAULT NULL COMMENT '描述',
  `active` int(1) DEFAULT '0' COMMENT '激活状态（0：禁用，1：激活）',
  `verify` int(11) DEFAULT '0' COMMENT '审核状态（0：未审核， 1：未审核，2：已通过，3：未通过）',
  `add_user` varchar(255) DEFAULT NULL,
  `add_time` datetime DEFAULT NULL,
  `parser_type` varchar(255) DEFAULT '硬编码解析' COMMENT '解析方式',
  `parameters` varchar(255) DEFAULT NULL COMMENT '接口参数',
  `request_type` varchar(50) DEFAULT NULL COMMENT '请求类型',
  `parameters_desc` varchar(50) DEFAULT NULL COMMENT '参数描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `engine_raw_inter` WRITE;
/*!40000 ALTER TABLE `engine_raw_inter` DISABLE KEYS */;

INSERT INTO `engine_raw_inter` (`id`, `name`, `describ`, `active`, `verify`, `add_user`, `add_time`, `parser_type`, `parameters`, `request_type`, `parameters_desc`)
VALUES
	(10001,'A3','手机号在网时长',1,1,'集奥聚合','2018-01-01 00:00:00','singleField_1','{\"idNumber\":\"身份证号\",\"realName\":\"姓名\",\"cid\":\"手机号\"}','geo','身份证，姓名，手机号'),
	(10002,'A4','手机在网状态',1,1,'集奥聚合','2018-01-01 00:00:00','singleField_1','{\"idNumber\":\"身份证号\",\"realName\":\"姓名\",\"cid\":\"手机号\"}','geo','身份证，姓名，手机号'),
	(10003,'B7','手机号身份证姓名验证',1,1,'集奥聚合','2018-01-01 00:00:00','singleField_1','{\"idNumber\":\"身份证号\",\"realName\":\"姓名\",\"cid\":\"手机号\"}','geo','身份证，姓名，手机号'),
	(10004,'B13','手机号三月内停机次数',1,1,'集奥聚合','2018-01-01 00:00:00','singleField_1','{\"idNumber\":\"身份证号\",\"realName\":\"姓名\",\"cid\":\"手机号\"}','geo','身份证，姓名，手机号'),
	(10005,'A2','手机实名验证',1,1,'集奥聚合','2018-01-01 00:00:00','singleField_1','{\"idNumber\":\"身份证号\",\"realName\":\"姓名\",\"cid\":\"手机号\"}','geo','身份证，姓名，手机号'),
	(10006,'C6','手机终端型号查询',1,1,'集奥聚合','2018-01-01 00:00:00','singleField_2','{\"idNumber\":\"身份证号\",\"realName\":\"姓名\",\"cid\":\"手机号\"}','geo','身份证，姓名，手机号'),
	(10007,'C8','手机身份证号验证',1,1,'集奥聚合','2018-01-01 00:00:00','singleField_1','{\"idNumber\":\"身份证号\",\"realName\":\"姓名\",\"cid\":\"手机号\"}','geo','身份证，姓名，手机号'),
	(10008,'C9','手机号集团客户验证',1,1,'集奥聚合','2018-01-01 00:00:00','singleField_1','{\"idNumber\":\"身份证号\",\"realName\":\"姓名\",\"cid\":\"手机号\"}','geo','身份证，姓名，手机号'),
	(10009,'D1','在网时长详版',1,1,'集奥聚合','2018-01-01 00:00:00','singleField_1','{\"idNumber\":\"身份证号\",\"realName\":\"姓名\",\"cid\":\"手机号\"}','geo','身份证，姓名，手机号'),
	(10010,'D3','手机号消费档次查询详版',1,1,'集奥聚合','2018-01-01 00:00:00','singleField_1','{\"idNumber\":\"身份证号\",\"realName\":\"姓名\",\"cid\":\"手机号\"}','geo','身份证，姓名，手机号'),
	(10011,'B11','常用联系人验证',1,1,'集奥聚合','2018-01-01 00:00:00','singleField_1','{\"idNumber\":\"身份证号\",\"realName\":\"姓名\",\"cid\":\"手机号\",\"cid2\":\"联系人手机号\"}','geo','身份证，姓名，手机号，联系人手机号'),
	(10012,'B18','手机号工作时段地址偏移量查询',1,1,'集奥聚合','2018-01-01 00:00:00','singleField_1','{\"idNumber\":\"身份证号\",\"realName\":\"姓名\",\"cid\":\"手机号\",\"workLongitude\":\"工作地地址（精确到门牌号）\"}','geo','身份证，姓名，手机号，工作地地址（精确到门牌号）'),
	(10013,'B19','手机号休息时段地址偏移量查询',1,1,'集奥聚合','2018-01-01 00:00:00','singleField_1','{\"idNumber\":\"身份证号\",\"realName\":\"姓名\",\"cid\":\"手机号\",\"liveLongitude\":\"居住地地址（精确到门牌号）\"}','geo','身份证，姓名，手机号，居住地地址（精确到门牌号）'),
	(10014,'C1','手机号实时城市核验',1,1,'集奥聚合','2018-01-01 00:00:00','singleField_1','{\"idNumber\":\"身份证号\",\"realName\":\"姓名\",\"cid\":\"手机号\",\"addressCode\":\"城市编码\"}','geo','身份证，姓名，手机号，城市编码'),
	(10015,'C7','手机号联系人通话次数查询',1,1,'集奥聚合','2018-01-01 00:00:00','singleField_1','{\"idNumber\":\"身份证号\",\"realName\":\"姓名\",\"cid\":\"手机号\",\"cid2\":\"联系人手机号\"}','geo','身份证，姓名，手机号，联系人手机号'),
	(10016,'T20103','关注在名单-专业版',1,1,'集奥聚合','2018-01-01 00:00:00','multiField_1','{\"idNumber\":\"身份证号\",\"realName\":\"姓名\",\"cid\":\"手机号\"}','geo','身份证，姓名，手机号'),
	(10017,'Y1','姓名身份证验证',1,1,'集奥聚合','2018-01-01 00:00:00','multiField_1','{\"idNumber\":\"身份证号\",\"realName\":\"姓名\",\"cid\":\"手机号\"}','geo','身份证，姓名，手机号'),
	(10018,'Y4','房产验证',1,1,'集奥聚合','2018-01-01 00:00:00','multiField_1','{\"idNumber\":\"身份证号\",\"realName\":\"姓名\",\"cid\":\"手机号\"}','geo','身份证，姓名，手机号'),
	(10019,'Z4','手机号身份证姓名银行卡验证',1,1,'集奥聚合','2018-01-01 00:00:00','singleField_1','{\"idNumber\":\"身份证号\",\"realName\":\"姓名\",\"cid\":\"手机号\",\"cardNo\":\"银行卡号\"}','geo','身份证，姓名，手机号，银行卡号'),
	(10020,'Z5','身份证姓名银行卡验证',1,1,'集奥聚合','2018-01-01 00:00:00','singleField_1','{\"idNumber\":\"身份证号\",\"realName\":\"姓名\",\"cardNo\":\"银行卡号\"}','geo','身份证，姓名，银行卡号'),
	(10021,'Z7','银行卡钱包位置查询',1,1,'集奥聚合','2018-01-01 00:00:00','multiField_2','{\"idNumber\":\"身份证号\",\"realName\":\"姓名\",\"cid\":\"手机号\",\"cardNo\":\"银行卡号\"}','geo','身份证，姓名，手机号，银行卡号'),
	(10022,'Y3','工作单位验证',1,1,'集奥聚合','2018-01-01 00:00:00','multiField_1','{\"idNumber\":\"身份证号\",\"realName\":\"姓名\",\"cid\":\"手机号\",\"company\":\"单位名称\"}','geo','身份证，姓名，手机号，单位名称'),
	(10023,'T20107','异常IP识别',1,1,'集奥聚合','2018-01-01 00:00:00','multiField_1','{\"idNumber\":\"身份证号\",\"realName\":\"姓名\",\"IP\":\"IP\"}','geo','身份证，姓名，手机号，IP'),
	(10024,'T40301','多平台借贷信息查询',1,1,'集奥聚合','2018-01-01 00:00:00','multiField_3','{\"idNumber\":\"身份证号\",\"realName\":\"姓名\",\"cid\":\"手机号\",\"cycle\":\"统计周期（单位天，可选，默认720天）\"}','geo','身份证，姓名，手机号，统计周期（单位天，可选，默认720天）'),
	(10025,'T40302','号码标签查询',1,1,'集奥聚合','2018-01-01 00:00:00','multiField_3','{\"idNumber\":\"身份证号\",\"realName\":\"姓名\",\"cid\":\"手机号\"}','geo','身份证，姓名，手机号'),
	(10026,'T40303','号码风险标签查询',1,1,'集奥聚合','2018-01-01 00:00:00','multiField_3','{\"idNumber\":\"身份证号\",\"realName\":\"姓名\",\"cid\":\"手机号\"}','geo','身份证，姓名，手机号'),
	(10027,'A5','自然人手机号码个数查询',1,1,'北京集奥聚合','2018-01-01 00:00:00','singleField_1','{\"idNumber\":\"身份证号\",\"realName\":\"姓名\",\"cid\":\"手机号\"}','geo','身份证，姓名，手机号'),
	(10028,'C2','手机号出境查询',1,1,'北京集奥聚合','2018-01-01 00:00:00','singleField_1','{\"idNumber\":\"身份证号\",\"realName\":\"姓名\",\"cid\":\"手机号\"}','geo','身份证，姓名，手机号'),
	(10029,'B21','手机号码月流量档次查询',0,0,'北京集奥聚合','2018-01-01 00:00:00','singleField_1','{\"idNumber\":\"身份证号\",\"realName\":\"姓名\",\"cid\":\"手机号\"}','geo','身份证，姓名，手机号'),
	(10030,'B1','手机号消费档次查询',1,1,'北京集奥聚合','2018-01-01 00:00:00','singleField_1','{\"idNumber\":\"身份证号\",\"realName\":\"姓名\",\"cid\":\"手机号\"}','geo','身份证，姓名，手机号'),
	(10031,'T20105','个人涉诉信息查询',1,1,'北京集奥聚合','2018-01-01 00:00:00','multiField_4','{\"idNumber\":\"身份证号\",\"realName\":\"姓名\",\"cid\":\"手机号\"}','geo','身份证，姓名，手机号');

/*!40000 ALTER TABLE `engine_raw_inter` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table engine_ros_black
# ------------------------------------------------------------

DROP TABLE IF EXISTS `engine_ros_black`;

CREATE TABLE `engine_ros_black` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '黑名单编号',
  `name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `id_card` varchar(20) DEFAULT NULL COMMENT '身份证号',
  `mobilephone` varchar(20) DEFAULT '' COMMENT '手机号',
  `card_no` varchar(20) DEFAULT NULL COMMENT '银行卡号',
  `admin` varchar(50) DEFAULT NULL COMMENT '添加人',
  `add_time` date DEFAULT NULL COMMENT '添加时间',
  `status` int(11) DEFAULT '1' COMMENT '名单状态',
  `unique_code` bigint(20) DEFAULT NULL COMMENT '用户关联',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `engine_ros_black` WRITE;
/*!40000 ALTER TABLE `engine_ros_black` DISABLE KEYS */;

INSERT INTO `engine_ros_black` (`id`, `name`, `id_card`, `mobilephone`, `card_no`, `admin`, `add_time`, `status`, `unique_code`)
VALUES
	(1,'詹庚','1212131','123123123','123123123','郭郭杰','2017-12-29',1,1);

/*!40000 ALTER TABLE `engine_ros_black` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table engine_ros_white
# ------------------------------------------------------------

DROP TABLE IF EXISTS `engine_ros_white`;

CREATE TABLE `engine_ros_white` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '黑名单编号',
  `name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `id_card` varchar(20) DEFAULT NULL COMMENT '身份证号',
  `mobilephone` varchar(20) DEFAULT '' COMMENT '手机号',
  `card_no` varchar(20) DEFAULT NULL COMMENT '银行卡号',
  `admin` varchar(50) DEFAULT NULL COMMENT '添加人',
  `add_time` date DEFAULT NULL COMMENT '添加时间',
  `status` int(11) DEFAULT '1' COMMENT '名单状态（1，正常 默认正常）',
  `unique_code` bigint(20) DEFAULT NULL COMMENT '关联用户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `engine_ros_white` WRITE;
/*!40000 ALTER TABLE `engine_ros_white` DISABLE KEYS */;

INSERT INTO `engine_ros_white` (`id`, `name`, `id_card`, `mobilephone`, `card_no`, `admin`, `add_time`, `status`, `unique_code`)
VALUES
	(1,'郭瑜杰','8888888888','8888888888','88888888','8888','2088-08-08',1,1);

/*!40000 ALTER TABLE `engine_ros_white` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table engine_rule
# ------------------------------------------------------------

DROP TABLE IF EXISTS `engine_rule`;

CREATE TABLE `engine_rule` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '规则编号',
  `name` varchar(255) DEFAULT NULL COMMENT '规则名称',
  `rules_id` int(11) DEFAULT NULL COMMENT '所属规则集id',
  `decision` varchar(255) DEFAULT NULL COMMENT '描述',
  `level` int(11) DEFAULT NULL COMMENT '风险等级(1:无风险2:低风险3:高风险)',
  `threshold` int(11) DEFAULT NULL COMMENT '总分（风险系数）',
  `condition_number` int(11) DEFAULT '0' COMMENT '条件数',
  `describ` varchar(255) DEFAULT NULL COMMENT '描述',
  `active` int(1) DEFAULT '1' COMMENT '激活状态（0：禁用，1：激活）',
  `verify` int(11) DEFAULT '0' COMMENT '审核状态（0：未审核， 1：未审核，2：已通过，3：未通过）',
  `add_user` varchar(255) DEFAULT NULL COMMENT '添加人',
  `add_time` datetime DEFAULT NULL COMMENT '添加时间',
  `condition_relationship` varchar(255) DEFAULT NULL COMMENT '条件逻辑关系',
  `unique_code` bigint(11) DEFAULT NULL COMMENT '客户标识',
  `rule_code` varchar(50) DEFAULT NULL COMMENT '规则编码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `engine_rule` WRITE;
/*!40000 ALTER TABLE `engine_rule` DISABLE KEYS */;

INSERT INTO `engine_rule` (`id`, `name`, `rules_id`, `decision`, `level`, `threshold`, `condition_number`, `describ`, `active`, `verify`, `add_user`, `add_time`, `condition_relationship`, `unique_code`, `rule_code`)
VALUES
	(10051,'test2',10025,'teset deee',3,50,1,NULL,1,0,'阿里巴巴','2018-03-20 18:54:10','45',29,'test2'),
	(10250,'测试规则1',10067,'测试规则1',1,100,2,NULL,1,0,'北京集奥聚合','2018-03-24 16:46:16','165&&164',1,'测试规则1'),
	(10251,'测试规则2',10067,'测试规则2',1,200,1,NULL,1,0,'北京集奥聚合','2018-03-24 16:46:38','166',1,'测试规则2');

/*!40000 ALTER TABLE `engine_rule` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table engine_rules
# ------------------------------------------------------------

DROP TABLE IF EXISTS `engine_rules`;

CREATE TABLE `engine_rules` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '规则集id',
  `name` varchar(255) DEFAULT NULL COMMENT '规则集名称',
  `business_id` int(11) DEFAULT NULL COMMENT '业务类型id',
  `sence_id` bigint(11) DEFAULT NULL COMMENT '场景类型id',
  `match_type` int(1) DEFAULT '0' COMMENT '匹配模式（0：均值匹配，1：最优匹配）',
  `threshold` int(11) DEFAULT NULL COMMENT '阈值',
  `threshold_min` int(11) DEFAULT NULL COMMENT '最小值',
  `threshold_max` int(11) DEFAULT NULL COMMENT '最大值',
  `describ` varchar(255) DEFAULT NULL COMMENT '规则集描述',
  `verify` int(11) DEFAULT '0' COMMENT '审核状态（0：未审核， 1：正在审核，2：已通过，3：未通过）',
  `active` int(1) DEFAULT '0' COMMENT '激活状态（0：禁用，1：激活）',
  `add_user` varchar(255) DEFAULT NULL COMMENT '添加人',
  `add_time` datetime DEFAULT NULL COMMENT '添加时间',
  `unique_code` bigint(20) DEFAULT NULL COMMENT '客户标识',
  `parameters` varchar(255) DEFAULT NULL COMMENT '规则集参数',
  `white_filter` varchar(255) DEFAULT NULL COMMENT '白名单过滤',
  `black_filter` varchar(255) DEFAULT NULL COMMENT '黑名单过滤',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `engine_rules` WRITE;
/*!40000 ALTER TABLE `engine_rules` DISABLE KEYS */;

INSERT INTO `engine_rules` (`id`, `name`, `business_id`, `sence_id`, `match_type`, `threshold`, `threshold_min`, `threshold_max`, `describ`, `verify`, `active`, `add_user`, `add_time`, `unique_code`, `parameters`, `white_filter`, `black_filter`)
VALUES
	(10069,'测试导入导出功能',10001,10001,0,NULL,100,300,'测试导入导出',0,0,'test','2018-03-24 17:13:13',35,'{\"realName\":\"\",\"idNumber\":\"\",\"cid\":\"\"}',NULL,NULL);

/*!40000 ALTER TABLE `engine_rules` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table engine_scenes
# ------------------------------------------------------------

DROP TABLE IF EXISTS `engine_scenes`;

CREATE TABLE `engine_scenes` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '场景编号',
  `business_id` int(11) DEFAULT NULL COMMENT '业务类型id',
  `name` varchar(255) DEFAULT NULL COMMENT '场景名称',
  `describ` varchar(255) DEFAULT NULL COMMENT '场景描述',
  `verify` int(11) DEFAULT '1' COMMENT '审核状态（0：未审核， 1：未审核，2：已通过，3：未通过）',
  `active` int(1) DEFAULT '1' COMMENT '激活状态（0：禁用，1：激活）',
  `add_user` varchar(255) DEFAULT NULL COMMENT '添加人',
  `add_time` datetime DEFAULT NULL COMMENT '添加时间',
  `unique_code` bigint(20) DEFAULT NULL COMMENT '唯一id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `engine_scenes` WRITE;
/*!40000 ALTER TABLE `engine_scenes` DISABLE KEYS */;

INSERT INTO `engine_scenes` (`id`, `business_id`, `name`, `describ`, `verify`, `active`, `add_user`, `add_time`, `unique_code`)
VALUES
	(10001,10001,'登录场景','用于登录验证',1,1,'北京集奥聚合','2018-01-01 00:00:00',1),
	(10002,10003,'注册场景','用于注册验证',1,1,'北京集奥聚合','2018-01-23 14:34:37',1);

/*!40000 ALTER TABLE `engine_scenes` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
