/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : renren_security

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-10-15 14:48:48
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for renren_sys_config
-- ----------------------------
DROP TABLE IF EXISTS `renren_sys_config`;
CREATE TABLE `renren_sys_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `param_key` varchar(50) DEFAULT NULL COMMENT 'key',
  `param_value` varchar(2000) DEFAULT NULL COMMENT 'value',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态   0：隐藏   1：显示',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `param_key` (`param_key`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='系统配置信息表';

-- ----------------------------
-- Records of renren_sys_config
-- ----------------------------
INSERT INTO `renren_sys_config` VALUES ('1', 'CLOUD_STORAGE_CONFIG_KEY', '{\"aliyunAccessKeyId\":\"\",\"aliyunAccessKeySecret\":\"\",\"aliyunBucketName\":\"\",\"aliyunDomain\":\"\",\"aliyunEndPoint\":\"\",\"aliyunPrefix\":\"\",\"qcloudBucketName\":\"\",\"qcloudDomain\":\"\",\"qcloudPrefix\":\"\",\"qcloudSecretId\":\"\",\"qcloudSecretKey\":\"\",\"qiniuAccessKey\":\"NrgMfABZxWLo5B-YYSjoE8-AZ1EISdi1Z3ubLOeZ\",\"qiniuBucketName\":\"ios-app\",\"qiniuDomain\":\"http://7xqbwh.dl1.z0.glb.clouddn.com\",\"qiniuPrefix\":\"upload\",\"qiniuSecretKey\":\"uIwJHevMRWU0VLxFvgy0tAcOdGqasdtVlJkdy6vV\",\"type\":1}', '0', '云存储配置信息');

-- ----------------------------
-- Table structure for renren_sys_log
-- ----------------------------
DROP TABLE IF EXISTS `renren_sys_log`;
CREATE TABLE `renren_sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) DEFAULT NULL COMMENT '用户操作',
  `method` varchar(200) DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) DEFAULT NULL COMMENT '请求参数',
  `time` bigint(20) NOT NULL COMMENT '执行时长(毫秒)',
  `ip` varchar(64) DEFAULT NULL COMMENT 'IP地址',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='系统日志';

-- ----------------------------
-- Records of renren_sys_log
-- ----------------------------
INSERT INTO `renren_sys_log` VALUES ('1', 'admin', '删除菜单', 'io.renren.modules.sys.controller.SysMenuController.delete()', '40', '44', '127.0.0.1', '2018-10-15 14:12:12');
INSERT INTO `renren_sys_log` VALUES ('2', 'admin', '删除菜单', 'io.renren.modules.sys.controller.SysMenuController.delete()', '39', '43', '127.0.0.1', '2018-10-15 14:12:17');
INSERT INTO `renren_sys_log` VALUES ('3', 'admin', '删除菜单', 'io.renren.modules.sys.controller.SysMenuController.delete()', '38', '26', '127.0.0.1', '2018-10-15 14:12:23');
INSERT INTO `renren_sys_log` VALUES ('4', 'admin', '删除菜单', 'io.renren.modules.sys.controller.SysMenuController.delete()', '37', '41', '127.0.0.1', '2018-10-15 14:12:28');
INSERT INTO `renren_sys_log` VALUES ('5', 'admin', '删除菜单', 'io.renren.modules.sys.controller.SysMenuController.delete()', '36', '27', '127.0.0.1', '2018-10-15 14:12:32');
INSERT INTO `renren_sys_log` VALUES ('6', 'admin', '删除菜单', 'io.renren.modules.sys.controller.SysMenuController.delete()', '35', '23', '127.0.0.1', '2018-10-15 14:12:37');
INSERT INTO `renren_sys_log` VALUES ('7', 'admin', '删除菜单', 'io.renren.modules.sys.controller.SysMenuController.delete()', '34', '36', '127.0.0.1', '2018-10-15 14:12:41');
INSERT INTO `renren_sys_log` VALUES ('8', 'admin', '删除菜单', 'io.renren.modules.sys.controller.SysMenuController.delete()', '33', '38', '127.0.0.1', '2018-10-15 14:12:46');
INSERT INTO `renren_sys_log` VALUES ('9', 'admin', '删除菜单', 'io.renren.modules.sys.controller.SysMenuController.delete()', '32', '40', '127.0.0.1', '2018-10-15 14:12:50');
INSERT INTO `renren_sys_log` VALUES ('10', 'admin', '删除菜单', 'io.renren.modules.sys.controller.SysMenuController.delete()', '31', '0', '127.0.0.1', '2018-10-15 14:12:54');
INSERT INTO `renren_sys_log` VALUES ('11', 'admin', '删除菜单', 'io.renren.modules.sys.controller.SysMenuController.delete()', '31', '0', '127.0.0.1', '2018-10-15 14:12:57');
INSERT INTO `renren_sys_log` VALUES ('12', 'admin', '删除菜单', 'io.renren.modules.sys.controller.SysMenuController.delete()', '7', '0', '127.0.0.1', '2018-10-15 14:13:04');
INSERT INTO `renren_sys_log` VALUES ('13', 'admin', '删除菜单', 'io.renren.modules.sys.controller.SysMenuController.delete()', '30', '0', '127.0.0.1', '2018-10-15 14:13:10');
INSERT INTO `renren_sys_log` VALUES ('14', 'admin', '删除菜单', 'io.renren.modules.sys.controller.SysMenuController.delete()', '31', '0', '127.0.0.1', '2018-10-15 14:13:14');
INSERT INTO `renren_sys_log` VALUES ('15', 'admin', '用户登录', 'io.renren.modules.sys.controller.SysLoginController.login()', '\"admin\"', '17', '127.0.0.1', '2018-10-15 14:31:00');
INSERT INTO `renren_sys_log` VALUES ('16', 'admin', '删除菜单', 'io.renren.modules.sys.controller.SysMenuController.delete()', '31', '0', '127.0.0.1', '2018-10-15 14:31:11');
INSERT INTO `renren_sys_log` VALUES ('17', 'admin', '删除菜单', 'io.renren.modules.sys.controller.SysMenuController.delete()', '7', '0', '127.0.0.1', '2018-10-15 14:31:16');
INSERT INTO `renren_sys_log` VALUES ('18', 'admin', '用户登录', 'io.renren.modules.sys.controller.SysLoginController.login()', '\"admin\"', '168', '127.0.0.1', '2018-10-15 14:36:50');
INSERT INTO `renren_sys_log` VALUES ('19', 'admin', '用户登录', 'io.renren.modules.sys.controller.SysLoginController.login()', '\"admin\"', '142', '127.0.0.1', '2018-10-15 14:45:18');

-- ----------------------------
-- Table structure for renren_sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `renren_sys_menu`;
CREATE TABLE `renren_sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(500) DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int(11) DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8 COMMENT='菜单管理';

-- ----------------------------
-- Records of renren_sys_menu
-- ----------------------------
INSERT INTO `renren_sys_menu` VALUES ('1', '0', '系统管理', null, null, '0', 'fa fa-cog', '0');
INSERT INTO `renren_sys_menu` VALUES ('2', '1', '管理员管理', 'modules/sys/user.html', null, '1', 'fa fa-user', '1');
INSERT INTO `renren_sys_menu` VALUES ('3', '1', '角色管理', 'modules/sys/role.html', null, '1', 'fa fa-user-secret', '2');
INSERT INTO `renren_sys_menu` VALUES ('4', '1', '菜单管理', 'modules/sys/menu.html', null, '1', 'fa fa-th-list', '3');
INSERT INTO `renren_sys_menu` VALUES ('5', '1', 'SQL监控', 'druid/sql.html', null, '1', 'fa fa-bug', '4');
INSERT INTO `renren_sys_menu` VALUES ('15', '2', '查看', null, 'sys:user:list,sys:user:info', '2', null, '0');
INSERT INTO `renren_sys_menu` VALUES ('16', '2', '新增', null, 'sys:user:save,sys:role:select', '2', null, '0');
INSERT INTO `renren_sys_menu` VALUES ('17', '2', '修改', null, 'sys:user:update,sys:role:select', '2', null, '0');
INSERT INTO `renren_sys_menu` VALUES ('18', '2', '删除', null, 'sys:user:delete', '2', null, '0');
INSERT INTO `renren_sys_menu` VALUES ('19', '3', '查看', null, 'sys:role:list,sys:role:info', '2', null, '0');
INSERT INTO `renren_sys_menu` VALUES ('20', '3', '新增', null, 'sys:role:save,sys:menu:perms', '2', null, '0');
INSERT INTO `renren_sys_menu` VALUES ('21', '3', '修改', null, 'sys:role:update,sys:menu:perms', '2', null, '0');
INSERT INTO `renren_sys_menu` VALUES ('22', '3', '删除', null, 'sys:role:delete', '2', null, '0');
INSERT INTO `renren_sys_menu` VALUES ('23', '4', '查看', null, 'sys:menu:list,sys:menu:info', '2', null, '0');
INSERT INTO `renren_sys_menu` VALUES ('24', '4', '新增', null, 'sys:menu:save,sys:menu:select', '2', null, '0');
INSERT INTO `renren_sys_menu` VALUES ('25', '4', '修改', null, 'sys:menu:update,sys:menu:select', '2', null, '0');
INSERT INTO `renren_sys_menu` VALUES ('26', '4', '删除', null, 'sys:menu:delete', '2', null, '0');
INSERT INTO `renren_sys_menu` VALUES ('27', '1', '参数管理', 'modules/sys/config.html', 'sys:config:list,sys:config:info,sys:config:save,sys:config:update,sys:config:delete', '1', 'fa fa-sun-o', '6');
INSERT INTO `renren_sys_menu` VALUES ('29', '1', '系统日志', 'modules/sys/log.html', 'sys:log:list', '1', 'fa fa-file-text-o', '7');

-- ----------------------------
-- Table structure for renren_sys_role
-- ----------------------------
DROP TABLE IF EXISTS `renren_sys_role`;
CREATE TABLE `renren_sys_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色';

-- ----------------------------
-- Records of renren_sys_role
-- ----------------------------

-- ----------------------------
-- Table structure for renren_sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `renren_sys_role_menu`;
CREATE TABLE `renren_sys_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色与菜单对应关系';

-- ----------------------------
-- Records of renren_sys_role_menu
-- ----------------------------

-- ----------------------------
-- Table structure for renren_sys_user
-- ----------------------------
DROP TABLE IF EXISTS `renren_sys_user`;
CREATE TABLE `renren_sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `salt` varchar(20) DEFAULT NULL COMMENT '盐',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) DEFAULT NULL COMMENT '手机号',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态  0：禁用   1：正常',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='系统用户';

-- ----------------------------
-- Records of renren_sys_user
-- ----------------------------
INSERT INTO `renren_sys_user` VALUES ('1', 'admin', 'e1153123d7d180ceeb820d577ff119876678732a68eef4e6ffc0b1f06a01f91b', 'YzcmCZNvbXocrsz9dm8e', 'root@renren.io', '13612345678', '1', '1', '2016-11-11 11:11:11');

-- ----------------------------
-- Table structure for renren_sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `renren_sys_user_role`;
CREATE TABLE `renren_sys_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户与角色对应关系';

-- ----------------------------
-- Records of renren_sys_user_role
-- ----------------------------
