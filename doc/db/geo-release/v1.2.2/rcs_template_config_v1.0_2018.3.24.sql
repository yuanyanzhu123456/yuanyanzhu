# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.6.38)
# Database: rcs
# Generation Time: 2018-03-24 10:27:57 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table templates
# ------------------------------------------------------------

DROP TABLE IF EXISTS `templates`;

CREATE TABLE `templates` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `ruleset_json` longtext COMMENT '规则集json串',
  `export_num` int(11) DEFAULT '0' COMMENT '导出量',
  `user_num` int(11) DEFAULT '0' COMMENT '使用量',
  `add_user` varchar(255) DEFAULT NULL COMMENT '添加人',
  `add_time` datetime DEFAULT NULL COMMENT '添加时间',
  `describ` varchar(255) DEFAULT NULL COMMENT '描述',
  `type` int(11) DEFAULT NULL COMMENT '分类',
  `version` varchar(20) DEFAULT NULL COMMENT '版本号',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `templates` WRITE;
/*!40000 ALTER TABLE `templates` DISABLE KEYS */;

INSERT INTO `templates` (`id`, `ruleset_json`, `export_num`, `user_num`, `add_user`, `add_time`, `describ`, `type`, `version`, `name`)
VALUES
	(10001,'{\"id\":10067,\"name\":\"测试导入导出功能\",\"businessId\":10001,\"senceId\":10001,\"matchType\":0,\"threshold\":null,\"thresholdMin\":100,\"thresholdMax\":300,\"describ\":\"测试导入导出\",\"verify\":0,\"active\":0,\"addUser\":\"北京集奥聚合\",\"addTime\":1521881148000,\"pageSize\":null,\"pageNo\":null,\"uniqueCode\":1,\"parameters\":{\"realName\":\"\",\"idNumber\":\"\",\"cid\":\"\"},\"ruleList\":[{\"id\":10250,\"name\":\"测试规则1\",\"rulesId\":10067,\"decision\":\"测试规则1\",\"threshold\":100,\"level\":1,\"conditionNumber\":2,\"describ\":null,\"active\":1,\"verify\":0,\"addUser\":\"北京集奥聚合\",\"addTime\":1521881176000,\"conditionRelationship\":\"165&&164\",\"uniqueCode\":1,\"pageSize\":null,\"pageNo\":null,\"rulesName\":null,\"ruleCode\":\"测试规则1\",\"conditionsList\":[{\"id\":164,\"name\":\"测试条件1\",\"ruleId\":10250,\"rulesId\":10067,\"fieldRelationship\":\"10226&&10227\",\"describ\":\"测试条件1\",\"active\":1,\"verify\":1,\"addUser\":\"北京集奥聚合\",\"addTime\":1521881214000,\"uniqueCode\":1,\"pageSize\":null,\"pageNo\":null,\"ruleName\":null,\"rulesName\":null,\"fieldList\":[{\"id\":10226,\"showName\":null,\"fieldId\":10001,\"fieldName\":\"province\",\"rulesId\":10067,\"conditionId\":164,\"operator\":\"==\",\"describ\":\"测试字段1\",\"active\":1,\"verify\":1,\"addUser\":\"北京集奥聚合\",\"addTime\":1521881252000,\"value\":null,\"valueDesc\":null,\"uniqueCode\":1,\"fieldType\":\"string\",\"parameter\":\"山东\",\"fieldTypeId\":2,\"pageSize\":null,\"pageNo\":null,\"fieldRawName\":null},{\"id\":10227,\"showName\":null,\"fieldId\":10002,\"fieldName\":\"city\",\"rulesId\":10067,\"conditionId\":164,\"operator\":\"==\",\"describ\":\"测试字段2\",\"active\":1,\"verify\":1,\"addUser\":\"北京集奥聚合\",\"addTime\":1521881252000,\"value\":null,\"valueDesc\":null,\"uniqueCode\":1,\"fieldType\":\"string\",\"parameter\":\"济南\",\"fieldTypeId\":2,\"pageSize\":null,\"pageNo\":null,\"fieldRawName\":null}]},{\"id\":165,\"name\":\"测试条件2\",\"ruleId\":10250,\"rulesId\":10067,\"fieldRelationship\":\"10228&&10229\",\"describ\":\"测试条件2\",\"active\":1,\"verify\":1,\"addUser\":\"北京集奥聚合\",\"addTime\":1521881268000,\"uniqueCode\":1,\"pageSize\":null,\"pageNo\":null,\"ruleName\":null,\"rulesName\":null,\"fieldList\":[{\"id\":10228,\"showName\":null,\"fieldId\":10001,\"fieldName\":\"province\",\"rulesId\":10067,\"conditionId\":165,\"operator\":\"==\",\"describ\":\"测试字段3\",\"active\":1,\"verify\":1,\"addUser\":\"北京集奥聚合\",\"addTime\":1521881326000,\"value\":null,\"valueDesc\":null,\"uniqueCode\":1,\"fieldType\":\"string\",\"parameter\":\"山东\",\"fieldTypeId\":2,\"pageSize\":null,\"pageNo\":null,\"fieldRawName\":null},{\"id\":10229,\"showName\":null,\"fieldId\":10002,\"fieldName\":\"city\",\"rulesId\":10067,\"conditionId\":165,\"operator\":\"==\",\"describ\":\"测试字段4\",\"active\":1,\"verify\":1,\"addUser\":\"北京集奥聚合\",\"addTime\":1521881326000,\"value\":null,\"valueDesc\":null,\"uniqueCode\":1,\"fieldType\":\"string\",\"parameter\":\"济南\",\"fieldTypeId\":2,\"pageSize\":null,\"pageNo\":null,\"fieldRawName\":null}]}]},{\"id\":10251,\"name\":\"测试规则2\",\"rulesId\":10067,\"decision\":\"测试规则2\",\"threshold\":200,\"level\":1,\"conditionNumber\":1,\"describ\":null,\"active\":1,\"verify\":0,\"addUser\":\"北京集奥聚合\",\"addTime\":1521881198000,\"conditionRelationship\":\"166\",\"uniqueCode\":1,\"pageSize\":null,\"pageNo\":null,\"rulesName\":null,\"ruleCode\":\"测试规则2\",\"conditionsList\":[{\"id\":166,\"name\":\"测试条件3\",\"ruleId\":10251,\"rulesId\":10067,\"fieldRelationship\":\"10230&&10231\",\"describ\":\"测试条件4\",\"active\":1,\"verify\":1,\"addUser\":\"北京集奥聚合\",\"addTime\":1521881385000,\"uniqueCode\":1,\"pageSize\":null,\"pageNo\":null,\"ruleName\":null,\"rulesName\":null,\"fieldList\":[{\"id\":10230,\"showName\":null,\"fieldId\":10001,\"fieldName\":\"province\",\"rulesId\":10067,\"conditionId\":166,\"operator\":\"==\",\"describ\":\"测试字段5\",\"active\":1,\"verify\":1,\"addUser\":\"北京集奥聚合\",\"addTime\":1521881423000,\"value\":null,\"valueDesc\":null,\"uniqueCode\":1,\"fieldType\":\"string\",\"parameter\":\"山东\",\"fieldTypeId\":2,\"pageSize\":null,\"pageNo\":null,\"fieldRawName\":null},{\"id\":10231,\"showName\":null,\"fieldId\":10002,\"fieldName\":\"city\",\"rulesId\":10067,\"conditionId\":166,\"operator\":\"==\",\"describ\":\"测试字段6\",\"active\":1,\"verify\":1,\"addUser\":\"北京集奥聚合\",\"addTime\":1521881423000,\"value\":null,\"valueDesc\":null,\"uniqueCode\":1,\"fieldType\":\"string\",\"parameter\":\"济南\",\"fieldTypeId\":2,\"pageSize\":null,\"pageNo\":null,\"fieldRawName\":null}]}]}],\"typeName\":null,\"sceneName\":null,\"whiteFilter\":null,\"blackFilter\":null,\"eventStatEntry\":null}',1,0,'test','2018-03-24 17:01:15',NULL,NULL,NULL,'测试导入导出功能');

/*!40000 ALTER TABLE `templates` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table template_import_log
# ------------------------------------------------------------

DROP TABLE IF EXISTS `template_import_log`;

CREATE TABLE `template_import_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '导入客户编号',
  `model_id` int(11) DEFAULT NULL COMMENT '规则模版编号',
  `import_time` datetime DEFAULT NULL COMMENT '导入时间',
  `import_status` int(11) DEFAULT NULL COMMENT '导入状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `template_import_log` WRITE;
/*!40000 ALTER TABLE `template_import_log` DISABLE KEYS */;

INSERT INTO `template_import_log` (`id`, `user_id`, `model_id`, `import_time`, `import_status`)
VALUES
	(1,1,8,NULL,NULL),
	(2,NULL,NULL,NULL,NULL);

/*!40000 ALTER TABLE `template_import_log` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table template_type
# ------------------------------------------------------------

DROP TABLE IF EXISTS `template_type`;

CREATE TABLE `template_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `template_type` WRITE;
/*!40000 ALTER TABLE `template_type` DISABLE KEYS */;

INSERT INTO `template_type` (`id`, `name`)
VALUES
	(1,'全部');

/*!40000 ALTER TABLE `template_type` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
