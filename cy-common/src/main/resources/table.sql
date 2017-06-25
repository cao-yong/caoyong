/*
SQLyog 企业版 - MySQL GUI v8.14 
MySQL - 5.1.49-community : Database - caoyong
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`caoyong` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `caoyong`;

/*Table structure for table `cy_brand` */

DROP TABLE IF EXISTS `cy_brand`;

CREATE TABLE `cy_brand` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(40) NOT NULL COMMENT '名称',
  `description` varchar(80) DEFAULT NULL COMMENT '描述',
  `img_url` varchar(80) DEFAULT NULL COMMENT '图片Url',
  `web_site` varchar(80) DEFAULT NULL COMMENT '品牌网址',
  `sort` int(11) DEFAULT NULL COMMENT '排序:最大最排前',
  `is_display` tinyint(1) DEFAULT NULL COMMENT '是否可见 1:可见 0:不可见',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `creator` varchar(10) NOT NULL DEFAULT 'SYSTEM' COMMENT '创建者',
  `modifier` varbinary(10) NOT NULL DEFAULT 'SYSTEM' COMMENT '修改者',
  `extra_info` varchar(128) DEFAULT NULL COMMENT '扩展字段',
  `is_deleted` char(1) NOT NULL DEFAULT 'N' COMMENT '是否删除（Y：是，N：否）',
  PRIMARY KEY (`id`,`is_deleted`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COMMENT='品牌';

/*Data for the table `cy_brand` */

insert  into `cy_brand`(`id`,`name`,`description`,`img_url`,`web_site`,`sort`,`is_display`,`create_time`,`update_time`,`creator`,`modifier`,`extra_info`,`is_deleted`) values (1,'依琦莲9','212','http://192.168.128.128/group1/M00/00/00/wKiAgFlKid2AFectAAFaL_xdW_s975.jpg',NULL,99,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'N'),(2,'凯速（KANSOON）','红色好看的','http://192.168.128.128/group1/M00/00/00/wKiAgFlKj4KAL4LjAAGVD2i3LAI402.jpg',NULL,98,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'N'),(3,'梵歌纳（vangona）','蓝色经典','http://192.168.128.128/group1/M00/00/00/wKiAgFlNI8eACYbCAAGaUioeBjg175.jpg',NULL,97,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'N'),(4,'伊朵莲',NULL,'http://192.168.128.128/group1/M00/00/00/wKiAgFlNI8eACYbCAAGaUioeBjg175.jpg',NULL,96,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'N'),(5,'菩媞',NULL,'http://192.168.128.128/group1/M00/00/00/wKiAgFlKj4KAL4LjAAGVD2i3LAI402.jpg',NULL,95,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'N'),(6,'丹璐斯',NULL,'http://192.168.128.128/group1/M00/00/00/wKiAgFlKj4KAL4LjAAGVD2i3LAI402.jpg',NULL,94,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'N'),(7,'喜悦瑜伽',NULL,'http://192.168.128.128/group1/M00/00/00/wKiAgFlNI8eACYbCAAGaUioeBjg175.jpg',NULL,93,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'N'),(8,'皮尔（pieryoga）',NULL,'http://192.168.128.128/group1/M00/00/00/wKiAgFlNI8eACYbCAAGaUioeBjg175.jpg',NULL,92,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'N'),(9,'路伊梵（LEFAN）',NULL,'http://192.168.128.128/group1/M00/00/00/wKiAgFlNI8eACYbCAAGaUioeBjg175.jpg',NULL,91,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'N'),(10,'金啦啦',NULL,'http://192.168.128.128/group1/M00/00/00/wKiAgFlNI8eACYbCAAGaUioeBjg175.jpg',NULL,90,0,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'N'),(11,'来尔瑜伽（LaiErYoGA）',NULL,'http://192.168.128.128/group1/M00/00/00/wKiAgFlNI8eACYbCAAGaUioeBjg175.jpg',NULL,89,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'N'),(12,'艾米达（aimida）',NULL,'http://192.168.128.128/group1/M00/00/00/wKiAgFlNI8eACYbCAAGaUioeBjg175.jpg',NULL,88,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'Y'),(13,'斯泊恩',NULL,'http://192.168.128.128/group1/M00/00/00/wKiAgFlNI8eACYbCAAGaUioeBjg175.jpg',NULL,87,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'Y'),(14,'康愫雅KSUA',NULL,'http://192.168.128.128/group1/M00/00/00/wKiAgFlNI8eACYbCAAGaUioeBjg175.jpg',NULL,86,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'Y'),(15,'格宁',NULL,'http://192.168.128.128/group1/M00/00/00/wKiAgFlNI8eACYbCAAGaUioeBjg175.jpg',NULL,85,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'Y'),(16,'E奈尔（Enaier）',NULL,'http://192.168.128.128/group1/M00/00/00/wKiAgFlNI8eACYbCAAGaUioeBjg175.jpg',NULL,84,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'Y'),(17,'哈他',NULL,'http://192.168.128.128/group1/M00/00/00/wKiAgFlNI8eACYbCAAGaUioeBjg175.jpg',NULL,83,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'Y'),(18,'伽美斯（Jamars）',NULL,'http://192.168.128.128/group1/M00/00/00/wKiAgFlNI8eACYbCAAGaUioeBjg175.jpg',NULL,82,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'Y'),(19,'瑜伽树（yogatree）',NULL,'http://192.168.128.128/group1/M00/00/00/wKiAgFlNI8eACYbCAAGaUioeBjg175.jpg',NULL,81,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'Y'),(20,'兰博伊人（lanboyiren）',NULL,'http://192.168.128.128/group1/M00/00/00/wKiAgFlNI8eACYbCAAGaUioeBjg175.jpg',NULL,80,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'Y');

/*Table structure for table `cy_buyer` */

DROP TABLE IF EXISTS `cy_buyer`;

CREATE TABLE `cy_buyer` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `username` varchar(24) DEFAULT NULL COMMENT '用户名',
  `password` varchar(24) DEFAULT NULL COMMENT '密码',
  `gender` tinyint(4) DEFAULT NULL COMMENT '性别',
  `email` varchar(32) DEFAULT NULL COMMENT '邮箱',
  `real_name` varchar(24) DEFAULT NULL COMMENT '真实名字',
  `register_time` datetime DEFAULT NULL COMMENT '注册时间',
  `province` varchar(24) DEFAULT NULL COMMENT '省ID',
  `city` varchar(24) DEFAULT NULL COMMENT '市ID',
  `town` varchar(24) DEFAULT NULL COMMENT '县ID',
  `addr` varchar(128) DEFAULT NULL COMMENT '地址',
  `creator` varchar(24) DEFAULT 'SYSTEM' COMMENT '创建者',
  `modifier` varchar(24) DEFAULT 'SYSTEM' COMMENT '修改者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `extra_info` varchar(128) DEFAULT NULL COMMENT '扩展字段',
  `is_deleted` char(1) DEFAULT 'N' COMMENT '是否删除（Y：是，N：否）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户';

/*Data for the table `cy_buyer` */

/*Table structure for table `cy_color` */

DROP TABLE IF EXISTS `cy_color`;

CREATE TABLE `cy_color` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(24) DEFAULT NULL COMMENT '名称',
  `parent_id` int(11) DEFAULT NULL COMMENT '父ID为色系',
  `img_url` varchar(512) DEFAULT NULL COMMENT '颜色对应的衣服小图',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `creator` varchar(10) NOT NULL DEFAULT 'SYSTEM' COMMENT '创建者',
  `modifier` varbinary(10) NOT NULL DEFAULT 'SYSTEM' COMMENT '修改者',
  `extra_info` varchar(128) DEFAULT NULL COMMENT '扩展字段',
  `is_deleted` char(1) NOT NULL DEFAULT 'N' COMMENT '是否删除（Y：是，N：否）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='颜色';

/*Data for the table `cy_color` */

insert  into `cy_color`(`id`,`name`,`parent_id`,`img_url`,`create_time`,`update_time`,`creator`,`modifier`,`extra_info`,`is_deleted`) values (1,'红色系',0,NULL,'2017-06-25 17:35:17','2017-06-25 17:35:17','SYSTEM','SYSTEM',NULL,'N'),(2,'绿色系',0,NULL,'2017-06-25 17:36:02','2017-06-25 17:36:02','SYSTEM','SYSTEM',NULL,'N'),(3,'蓝色系',0,NULL,'2017-06-25 17:36:57','2017-06-25 17:36:57','SYSTEM','SYSTEM',NULL,'N'),(4,'黑色系',0,NULL,'2017-06-25 17:37:24','2017-06-25 17:37:24','SYSTEM','SYSTEM',NULL,'N'),(5,'粉色系',0,NULL,'2017-06-25 17:37:50','2017-06-25 17:37:50','SYSTEM','SYSTEM',NULL,'N'),(6,'紫色系',0,NULL,'2017-06-25 17:38:10','2017-06-25 17:38:10','SYSTEM','SYSTEM',NULL,'N'),(7,'灰色系',0,NULL,'2017-06-25 17:38:30','2017-06-25 17:38:30','SYSTEM','SYSTEM',NULL,'N'),(8,'其它',0,NULL,'2017-06-25 17:38:48','2017-06-25 17:38:48','SYSTEM','SYSTEM',NULL,'N'),(9,'西瓜红',1,NULL,'2017-06-25 17:39:20','2017-06-25 17:39:20','SYSTEM','SYSTEM',NULL,'N'),(10,'大红',1,NULL,'2017-06-25 17:39:35','2017-06-25 17:39:35','SYSTEM','SYSTEM',NULL,'N'),(11,'墨绿',2,NULL,'2017-06-25 17:40:09','2017-06-25 17:40:09','SYSTEM','SYSTEM',NULL,'N'),(12,'草绿',2,NULL,'2017-06-25 17:40:25','2017-06-25 17:40:25','SYSTEM','SYSTEM',NULL,'N'),(13,'青绿',2,NULL,'2017-06-25 17:40:45','2017-06-25 17:40:45','SYSTEM','SYSTEM',NULL,'N'),(14,'海军蓝',3,NULL,'2017-06-25 17:41:52','2017-06-25 17:41:52','SYSTEM','SYSTEM',NULL,'N'),(15,'海军白蓝条',14,NULL,'2017-06-25 17:42:20','2017-06-25 17:42:20','SYSTEM','SYSTEM',NULL,'N'),(16,'紫黑',4,NULL,'2017-06-25 17:42:49','2017-06-25 17:42:49','SYSTEM','SYSTEM',NULL,'N'),(17,'纯黑',4,NULL,'2017-06-25 17:43:19','2017-06-25 17:43:19','SYSTEM','SYSTEM',NULL,'N'),(18,'浅粉',5,NULL,'2017-06-25 17:44:27','2017-06-25 17:44:27','SYSTEM','SYSTEM',NULL,'N'),(19,'浅紫',6,NULL,'2017-06-25 17:45:11','2017-06-25 17:45:11','SYSTEM','SYSTEM',NULL,'N'),(20,'浅灰',7,NULL,'2017-06-25 17:45:45','2017-06-25 17:45:45','SYSTEM','SYSTEM',NULL,'N'),(21,'均色',8,NULL,'2017-06-25 17:46:10','2017-06-25 17:46:10','SYSTEM','SYSTEM',NULL,'N'),(22,'桃粉',5,NULL,'2017-06-25 17:47:15','2017-06-25 17:47:15','SYSTEM','SYSTEM',NULL,'N'),(23,'深紫',6,NULL,'2017-06-25 17:48:02','2017-06-25 17:48:02','SYSTEM','SYSTEM',NULL,'N'),(24,'浅蓝',3,NULL,'2017-06-25 17:48:51','2017-06-25 17:48:51','SYSTEM','SYSTEM',NULL,'N'),(25,'深蓝',3,NULL,'2017-06-25 17:49:20','2017-06-25 17:49:20','SYSTEM','SYSTEM',NULL,'N');

/*Table structure for table `cy_detail` */

DROP TABLE IF EXISTS `cy_detail`;

CREATE TABLE `cy_detail` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `order_id` bigint(20) DEFAULT NULL COMMENT '订单ID',
  `product_id` bigint(20) DEFAULT NULL COMMENT '商品编号',
  `product_name` varchar(128) DEFAULT NULL COMMENT '商品名称',
  `color` varchar(24) DEFAULT NULL COMMENT '颜色名称',
  `size` varchar(24) DEFAULT NULL COMMENT '尺码',
  `price` varchar(64) DEFAULT NULL COMMENT '商品销售价',
  `amount` int(11) DEFAULT NULL COMMENT '购买数量',
  `creator` varchar(24) DEFAULT 'SYSTEM' COMMENT '创建者',
  `modifier` varchar(24) DEFAULT 'SYSTEM' COMMENT '修改者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `extra_info` varchar(128) DEFAULT NULL COMMENT '扩展字段',
  `is_deleted` char(1) DEFAULT 'N' COMMENT '是否删除（Y：是，N：否）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单详情';

/*Data for the table `cy_detail` */

/*Table structure for table `cy_order` */

DROP TABLE IF EXISTS `cy_order`;

CREATE TABLE `cy_order` (
  `id` bigint(20) NOT NULL COMMENT 'ID或订单号',
  `deliver_fee` varchar(64) DEFAULT NULL COMMENT '运费',
  `total_price` varchar(64) DEFAULT NULL COMMENT '应付金额',
  `order_price` varchar(64) DEFAULT NULL COMMENT '订单金额',
  `payment_way` tinyint(4) DEFAULT NULL COMMENT '支付方式 0:到付 1:在线 2:邮局 3:公司转帐',
  `payment_cash` tinyint(4) DEFAULT NULL COMMENT '货到付款方式.1现金,2POS刷卡',
  `delivery` int(11) DEFAULT NULL COMMENT '送货时间',
  `is_confirm` tinyint(1) DEFAULT NULL COMMENT '是否电话确认 1:是  0: 否',
  `payment_state` tinyint(4) DEFAULT NULL COMMENT '支付状态 :0到付1待付款,2已付款,3待退款,4退款成功,5退款失败',
  `order_state` tinyint(4) DEFAULT NULL COMMENT '订单状态 0:提交订单 1:仓库配货 2:商品出库 3:等待收货 4:完成 5待退货 6已退货',
  `order_create_date` datetime DEFAULT NULL COMMENT '订单生成时间',
  `note` varchar(128) DEFAULT NULL COMMENT '附言',
  `buyer_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `creator` varchar(24) DEFAULT 'SYSTEM' COMMENT '创建者',
  `modifier` varchar(24) DEFAULT 'SYSTEM' COMMENT '修改者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `extra_info` varchar(128) DEFAULT NULL COMMENT '扩展字段',
  `is_deleted` char(1) DEFAULT 'N' COMMENT '是否删除（Y：是，N：否）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单';

/*Data for the table `cy_order` */

/*Table structure for table `cy_product` */

DROP TABLE IF EXISTS `cy_product`;

CREATE TABLE `cy_product` (
  `id` bigint(20) NOT NULL COMMENT 'ID或商品编号',
  `brand_id` bigint(20) DEFAULT NULL COMMENT '品牌ID',
  `name` varchar(64) DEFAULT NULL COMMENT '商品名称',
  `weight` varchar(64) DEFAULT NULL COMMENT '重量 单位:克',
  `is_new` tinyint(1) DEFAULT NULL COMMENT '是否新品:0:旧品,1:新品',
  `is_hot` tinyint(1) DEFAULT NULL COMMENT '是否热销:0,否 1:是',
  `is_commend` tinyint(1) DEFAULT NULL COMMENT '推荐 1推荐 0 不推荐',
  `is_show` tinyint(1) DEFAULT NULL COMMENT '上下架:0否 1是',
  `img_url` varchar(1024) DEFAULT NULL COMMENT '商品图片集  img,img1,',
  `description` varchar(512) DEFAULT NULL COMMENT '商品描述',
  `package_list` varchar(512) DEFAULT NULL COMMENT '包装清单',
  `colors` varchar(512) DEFAULT NULL COMMENT '颜色集',
  `sizes` varchar(512) DEFAULT NULL COMMENT '尺寸集',
  `creator` varchar(24) DEFAULT 'SYSTEM' COMMENT '创建者',
  `modifier` varchar(24) DEFAULT 'SYSTEM' COMMENT '修改者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `extra_info` varchar(128) DEFAULT NULL COMMENT '扩展字段',
  `is_deleted` char(1) DEFAULT 'N' COMMENT '是否删除（Y：是，N：否）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品';

/*Data for the table `cy_product` */

insert  into `cy_product`(`id`,`brand_id`,`name`,`weight`,`is_new`,`is_hot`,`is_commend`,`is_show`,`img_url`,`description`,`package_list`,`colors`,`sizes`,`creator`,`modifier`,`create_time`,`update_time`,`extra_info`,`is_deleted`) values (1,11111,'test','1',1,1,1,1,'www.baidu.com','test','test','test','test','SYSTEM','SYSTEM',NULL,NULL,NULL,'N'),(2,22222,'testing','2',1,1,1,1,'www.jd.com','testing','testing','testing','testing','SYSTEM','SYSTEM',NULL,NULL,NULL,'N');

/*Table structure for table `cy_sku` */

DROP TABLE IF EXISTS `cy_sku`;

CREATE TABLE `cy_sku` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `product_id` bigint(20) DEFAULT NULL COMMENT '商品ID',
  `color_id` bigint(20) DEFAULT NULL COMMENT '颜色ID',
  `size` varchar(16) DEFAULT NULL COMMENT '尺码',
  `market_price` varchar(64) DEFAULT NULL COMMENT '市场价',
  `price` varchar(64) DEFAULT NULL COMMENT '售价',
  `delive_fee` varchar(16) DEFAULT '10.00' COMMENT '运费 默认10元',
  `stock` int(11) DEFAULT NULL COMMENT '库存',
  `upper_limit` int(11) DEFAULT NULL COMMENT '购买限制',
  `creator` varchar(24) DEFAULT 'SYSTEM' COMMENT '创建者',
  `modifier` varchar(24) DEFAULT 'SYSTEM' COMMENT '修改者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `extra_info` varchar(128) DEFAULT NULL COMMENT '扩展字段',
  `is_deleted` char(1) DEFAULT 'N' COMMENT '是否删除（Y：是，N：否）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='最小销售单元';

/*Data for the table `cy_sku` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
