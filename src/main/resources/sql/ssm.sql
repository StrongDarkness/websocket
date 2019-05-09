/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 50720
 Source Host           : localhost:3306
 Source Schema         : ssm

 Target Server Type    : MySQL
 Target Server Version : 50720
 File Encoding         : 65001

 Date: 09/05/2019 17:32:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for interfacedoc
-- ----------------------------
DROP TABLE IF EXISTS `interfacedoc`;
CREATE TABLE `interfacedoc`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `iid` int(11) NULL DEFAULT NULL,
  `date` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of interfacedoc
-- ----------------------------
INSERT INTO `interfacedoc` VALUES (1, '标题', 1, '2018-07-13 16:52:13');

-- ----------------------------
-- Table structure for interfacelist
-- ----------------------------
DROP TABLE IF EXISTS `interfacelist`;
CREATE TABLE `interfacelist`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `iid` int(11) NULL DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `address` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `type` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `paramsId` int(11) NULL DEFAULT NULL,
  `returnId` int(11) NULL DEFAULT NULL,
  `info` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of interfacelist
-- ----------------------------
INSERT INTO `interfacelist` VALUES (1, 1, '查询接口', 'http://localhost:8080/api/Doc', 'get', 1, 1, '备注');

-- ----------------------------
-- Table structure for paramslist
-- ----------------------------
DROP TABLE IF EXISTS `paramslist`;
CREATE TABLE `paramslist`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pid` int(11) NULL DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `value` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `note` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of paramslist
-- ----------------------------
INSERT INTO `paramslist` VALUES (1, 1, 'id', '1', 'id');
INSERT INTO `paramslist` VALUES (2, 1, 'aa', '2', 'aa');

-- ----------------------------
-- Table structure for returnlist
-- ----------------------------
DROP TABLE IF EXISTS `returnlist`;
CREATE TABLE `returnlist`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rid` int(11) NULL DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `value` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `note` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of returnlist
-- ----------------------------
INSERT INTO `returnlist` VALUES (1, 1, 'bb', '3', 'bb');
INSERT INTO `returnlist` VALUES (2, 1, 'bb', '3', 'bb');
INSERT INTO `returnlist` VALUES (3, 1, 'dd', '4', 'dd');

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `Uid` binary(36) NOT NULL COMMENT 'Uid',
  `Name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `Age` int(3) NOT NULL,
  `ClassId` int(3) NOT NULL,
  PRIMARY KEY (`Uid`) USING BTREE,
  INDEX `StudentClass`(`ClassId`) USING BTREE,
  CONSTRAINT `StudentClass` FOREIGN KEY (`ClassId`) REFERENCES `studentclass` (`ClassId`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES (0x310000000000000000000000000000000000000000000000000000000000000000000000, '张三', 15, 1);

-- ----------------------------
-- Table structure for studentclass
-- ----------------------------
DROP TABLE IF EXISTS `studentclass`;
CREATE TABLE `studentclass`  (
  `ClassId` int(3) NOT NULL AUTO_INCREMENT,
  `ClassName` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ClassId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of studentclass
-- ----------------------------
INSERT INTO `studentclass` VALUES (1, '高一(1)班');

-- ----------------------------
-- Table structure for t_admin_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_admin_menu`;
CREATE TABLE `t_admin_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '管理员菜单关联表',
  `admin_role_id` bigint(11) NOT NULL COMMENT '管理员角色id',
  `admin_role_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '管理员角色名',
  `menu_id` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单id用，分割 ',
  `create_user` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `update_user` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_in_use` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '0:逻辑数据删除 1:逻辑数据存在',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '管理员菜单关联表，管理员（即登陆帐号）能访问的菜单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_admin_menu
-- ----------------------------
INSERT INTO `t_admin_menu` VALUES (1, 1, '超级管理员', '1', 'admin', NULL, '2019-02-15 17:21:13', NULL, '1');
INSERT INTO `t_admin_menu` VALUES (2, 2, '普通管理员', '1', 'admin', NULL, '2019-02-15 17:21:13', NULL, '1');
INSERT INTO `t_admin_menu` VALUES (3, 3, '客服', '1', 'admin', NULL, '2019-02-15 17:21:13', NULL, '1');

-- ----------------------------
-- Table structure for t_admin_user
-- ----------------------------
DROP TABLE IF EXISTS `t_admin_user`;
CREATE TABLE `t_admin_user`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '系统管理员用户表',
  `admin_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '管理员用户名',
  `admin_password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '管理员密码',
  `role_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色id',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱地址',
  `phone_num` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话号码',
  `login_flag` int(255) NOT NULL DEFAULT 0 COMMENT '是否允许登录 0-允许 1-不允许',
  `create_user` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `update_user` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_in_use` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '0:逻辑数据删除 1:逻辑数据存在',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统管理员用户表，代理后台用于登陆的帐号信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_admin_user
-- ----------------------------
INSERT INTO `t_admin_user` VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '1', NULL, NULL, 0, '1', NULL, '2019-01-18 10:29:50', NULL, '1');
INSERT INTO `t_admin_user` VALUES (2, 'admin2', 'e10adc3949ba59abbe56e057f20f883e', '2', NULL, NULL, 0, '1', NULL, '2019-01-18 10:29:50', NULL, '1');

-- ----------------------------
-- Table structure for t_sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_menu`;
CREATE TABLE `t_sys_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '后台栏目表,总后台和代理后台通用',
  `parnet_id` int(11) NOT NULL DEFAULT 0 COMMENT '上级id',
  `menu_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '栏目名称',
  `menu_icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '小图标',
  `menu_link` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '跳转页面',
  `menu_sort` int(255) NOT NULL DEFAULT 0 COMMENT '排序',
  `create_user` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `update_user` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_in_use` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '0:逻辑数据删除 1:逻辑数据存在',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '后台栏目表,总后台和代理后台通用' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_permission`;
CREATE TABLE `t_sys_permission`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '用户权限表，用于区分管理员权限，类似（客服，财务，管理员）',
  `permission_id` int(11) NOT NULL COMMENT '权限id',
  `permission_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限名',
  `permission_info` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限描述',
  `ver` int(11) NOT NULL COMMENT '版本',
  `create_user` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `update_user` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_in_use` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '0:逻辑数据删除 1:逻辑数据存在',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户权限表，用于区分管理员权限，类似（客服，财务，管理员）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_permission
-- ----------------------------
INSERT INTO `t_sys_permission` VALUES (1, 1, 'delete', '删除', 1, 'admin', NULL, '2019-02-18 16:40:29', NULL, '1');

-- ----------------------------
-- Table structure for t_sys_role
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_role`;
CREATE TABLE `t_sys_role`  (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '系统角色表',
  `role_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名',
  `role_desc` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色描述',
  `create_user` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `update_user` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_in_use` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '0:逻辑数据删除 1:逻辑数据存在',
  `permission_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限id用，分割',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_role
-- ----------------------------
INSERT INTO `t_sys_role` VALUES (1, 'admin', '管理员', '小邓', NULL, '2019-01-17 10:11:26', NULL, '1', '1');
INSERT INTO `t_sys_role` VALUES (2, 'user', '普通用户', '小邓', NULL, '2019-01-17 10:13:09', NULL, '1', '2');
INSERT INTO `t_sys_role` VALUES (3, '代理', '客服开', '小邓', NULL, '2019-01-17 10:14:13', NULL, '1', '3');
INSERT INTO `t_sys_role` VALUES (4, '业务员', '代理开', '小邓', NULL, '2019-01-17 10:14:42', NULL, '1', '4');

-- ----------------------------
-- Table structure for t_user_info
-- ----------------------------
DROP TABLE IF EXISTS `t_user_info`;
CREATE TABLE `t_user_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户基本信息表',
  `user_id` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户ID',
  `user_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户姓名',
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户密码',
  `pay_password` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付密码',
  `nick_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户昵称',
  `head_img` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  `phone` char(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户手机',
  `phone_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户手机唯一标识',
  `id_card_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `real_name_state` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '实名状态：\n0、未实名;\n1、已实名;',
  `roles` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户拥有的角色，关联角色表ID,可多个，格式如：1==2==3',
  `create_user` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `update_user` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_in_use` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '0:逻辑数据删除 1:逻辑数据存在',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户基本信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user_info
-- ----------------------------
INSERT INTO `t_user_info` VALUES (1, 'M20190122163422120', '15970446645', '123456', NULL, '张三', NULL, '15970446645', '123456789', NULL, '0', NULL, '自己', NULL, '2019-01-22 16:45:31', NULL, '1');
INSERT INTO `t_user_info` VALUES (2, 'M20190122163422121', '15970446644', '123456', NULL, '李四', NULL, '15970446644', NULL, NULL, '0', NULL, '别人', NULL, '2019-01-23 09:46:12', NULL, '1');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `password` varchar(25) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `email` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `phoneNum` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `gg` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (2, 'lisi', '123456', '456@qq.com', '15970448855', '1');
INSERT INTO `user` VALUES (3, 'wangwu', '123456', '425@qq.com', '15975545555', '1');
INSERT INTO `user` VALUES (4, 'wanger', '123456', '425@qq.com', '15945246525', '1');
INSERT INTO `user` VALUES (5, 'wanger', '123456', '666@qq.com', '15945246525', '1');
INSERT INTO `user` VALUES (6, 'wanger', '123456', '425@qq.com', '15945246525', '1');
INSERT INTO `user` VALUES (7, 'wanger', '123456', '425@qq.com', '15945246525', '1');
INSERT INTO `user` VALUES (8, 'wanger', '123456', '425@qq.com', '15945246525', '1');
INSERT INTO `user` VALUES (9, 'aa', '123456', '1345@google.com', '239838434848', '1');
INSERT INTO `user` VALUES (10, '张三', '123456', '1345@google.com', '239838434848', '1');

SET FOREIGN_KEY_CHECKS = 1;
