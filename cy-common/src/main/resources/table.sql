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
  `modifier` varchar(10) NOT NULL DEFAULT 'SYSTEM' COMMENT '修改者',
  `extra_info` varchar(128) DEFAULT NULL COMMENT '扩展字段',
  `is_deleted` char(1) NOT NULL DEFAULT 'N' COMMENT '是否删除（Y：是，N：否）',
  PRIMARY KEY (`id`,`is_deleted`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COMMENT='品牌';

/*Data for the table `cy_brand` */

insert  into `cy_brand`(`id`,`name`,`description`,`img_url`,`web_site`,`sort`,`is_display`,`create_time`,`update_time`,`creator`,`modifier`,`extra_info`,`is_deleted`) values (1,'依琦莲9','212','http://192.168.128.128/group1/M00/00/00/wKiAgFlKid2AFectAAFaL_xdW_s975.jpg',NULL,99,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'Y'),(2,'凯速（KANSOON）','红色好看的','http://192.168.128.128/group1/M00/00/00/wKiAgFlKj4KAL4LjAAGVD2i3LAI402.jpg',NULL,98,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'Y'),(3,'梵歌纳（vangona）','蓝色经典','http://192.168.128.128/group1/M00/00/00/wKiAgFlNI8eACYbCAAGaUioeBjg175.jpg',NULL,97,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'N'),(4,'伊朵莲',NULL,'http://192.168.128.128/group1/M00/00/00/wKiAgFlNI8eACYbCAAGaUioeBjg175.jpg',NULL,96,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'N'),(5,'菩媞',NULL,'http://192.168.128.128/group1/M00/00/00/wKiAgFlKj4KAL4LjAAGVD2i3LAI402.jpg',NULL,95,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'N'),(6,'丹璐斯',NULL,'http://192.168.128.128/group1/M00/00/00/wKiAgFlKj4KAL4LjAAGVD2i3LAI402.jpg',NULL,94,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'N'),(7,'喜悦瑜伽',NULL,'http://192.168.128.128/group1/M00/00/00/wKiAgFlNI8eACYbCAAGaUioeBjg175.jpg',NULL,93,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'N'),(8,'皮尔（pieryoga）',NULL,'http://192.168.128.128/group1/M00/00/00/wKiAgFlNI8eACYbCAAGaUioeBjg175.jpg',NULL,92,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'N'),(9,'路伊梵（LEFAN）',NULL,'http://192.168.128.128/group1/M00/00/00/wKiAgFlNI8eACYbCAAGaUioeBjg175.jpg',NULL,91,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'N'),(10,'金啦啦',NULL,'http://192.168.128.128/group1/M00/00/00/wKiAgFlNI8eACYbCAAGaUioeBjg175.jpg',NULL,90,0,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'N'),(11,'来尔瑜伽（LaiErYoGA）',NULL,'http://192.168.128.128/group1/M00/00/00/wKiAgFlNI8eACYbCAAGaUioeBjg175.jpg',NULL,89,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'N'),(12,'艾米达（aimida）','','http://192.168.128.128/group1/M00/00/00/wKiAgFlU_ZCANFVoAABkep5Lr6M450.jpg',NULL,88,1,'2017-06-25 16:37:08','2017-06-29 21:15:47','SYSTEM','SYSTEM',NULL,'N'),(13,'斯泊恩',NULL,'http://192.168.128.128/group1/M00/00/00/wKiAgFlNI8eACYbCAAGaUioeBjg175.jpg',NULL,87,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'N'),(14,'康愫雅KSUA',NULL,'http://192.168.128.128/group1/M00/00/00/wKiAgFlNI8eACYbCAAGaUioeBjg175.jpg',NULL,86,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'N'),(15,'格宁',NULL,'http://192.168.128.128/group1/M00/00/00/wKiAgFlNI8eACYbCAAGaUioeBjg175.jpg',NULL,85,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'N'),(16,'E奈尔（Enaier）',NULL,'http://192.168.128.128/group1/M00/00/00/wKiAgFlNI8eACYbCAAGaUioeBjg175.jpg',NULL,84,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'N'),(17,'哈他',NULL,'http://192.168.128.128/group1/M00/00/00/wKiAgFlNI8eACYbCAAGaUioeBjg175.jpg',NULL,83,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'N'),(18,'伽美斯（Jamars）',NULL,'http://192.168.128.128/group1/M00/00/00/wKiAgFlNI8eACYbCAAGaUioeBjg175.jpg',NULL,82,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'N'),(19,'瑜伽树（yogatree）',NULL,'http://192.168.128.128/group1/M00/00/00/wKiAgFlNI8eACYbCAAGaUioeBjg175.jpg',NULL,81,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'N'),(20,'兰博伊人（lanboyiren）',NULL,'http://192.168.128.128/group1/M00/00/00/wKiAgFlNI8eACYbCAAGaUioeBjg175.jpg',NULL,80,1,'2017-06-25 16:37:08','2017-06-25 16:37:08','SYSTEM','SYSTEM',NULL,'N');

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
  `modifier` varchar(10) NOT NULL DEFAULT 'SYSTEM' COMMENT '修改者',
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
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID或商品编号',
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
) ENGINE=InnoDB AUTO_INCREMENT=1002 DEFAULT CHARSET=utf8 COMMENT='商品';

/*Data for the table `cy_product` */

insert  into `cy_product`(`id`,`brand_id`,`name`,`weight`,`is_new`,`is_hot`,`is_commend`,`is_show`,`img_url`,`description`,`package_list`,`colors`,`sizes`,`creator`,`modifier`,`create_time`,`update_time`,`extra_info`,`is_deleted`) values (1,11111,'test','1',1,1,1,1,'/images/t/t3/e2.jpg','test','test','test','test','SYSTEM','SYSTEM',NULL,NULL,NULL,'N'),(2,22222,'testing','2',1,1,1,1,'/images/t/t3/e1.jpg','testing','testing','testing','testing','SYSTEM','SYSTEM',NULL,NULL,NULL,'N'),(3,3,'潮流假期瑜伽服健身服女士三件套四件套跑步运动套装女春秋宽松健身房速干上衣套装 粉红色-短袖四件套 M','0.6',1,1,1,1,'http://192.168.128.128/group1/M00/00/00/wKiAgFlVIveAAnajAAAIUYMv6ZM393.jpg,http://192.168.128.128/group1/M00/00/00/wKiAgFlVIveADCljAAAIm8yzLwM555.jpg,http://192.168.128.128/group1/M00/00/00/wKiAgFlVIveAOUHmAAAHnbsfCZk605.jpg,http://192.168.128.128/group1/M00/00/00/wKiAgFlVIveABKKJAAAItM1cFQ8733.jpg,http://192.168.128.128/group1/M00/00/00/wKiAgFlVIveAFRSWAAAInaZAoSk925.jpg','<img src=\"http://192.168.128.128/group1/M00/00/00/wKiAgFlVIwuAb6LsAAQZThCyCH8136.jpg\" alt=\"\" />','衣服*1，裤子*1','9,10,12,14,16,23,25','S,M,L,XL,XXL','SYSTEM','SYSTEM','2017-06-29 23:55:59','2017-06-29 23:55:59',NULL,'N'),(1001,5,'测试数据生成商品id','0.6',NULL,NULL,1,0,'http://192.168.128.128/group1/M00/00/00/wKiAgFlWhr6AKi1SAAFaL_xdW_s782.jpg','<img src=\"http://192.168.128.128/group1/M00/00/00/wKiAgFlWhs-AR6_2AAD-yBA5sAQ409.jpg\" alt=\"\" />','衣服*2，裤子*1','9,11,12',NULL,'SYSTEM','SYSTEM','2017-07-01 01:14:08','2017-07-01 01:14:08',NULL,'N');

/*Table structure for table `cy_sku` */

DROP TABLE IF EXISTS `cy_sku`;

CREATE TABLE `cy_sku` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
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
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8 COMMENT='最小销售单元';

/*Data for the table `cy_sku` */

insert  into `cy_sku`(`id`,`product_id`,`color_id`,`size`,`market_price`,`price`,`delive_fee`,`stock`,`upper_limit`,`creator`,`modifier`,`create_time`,`update_time`,`extra_info`,`is_deleted`) values (1,3,9,'S','89.50','129.00','10.00',221,20076,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N'),(2,3,9,'M','0.00','43.00','10.00',0,200,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N'),(3,3,9,'L','0.00','0.00','10.00',0,200,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N'),(4,3,9,'XL','0.00','0.00','10.00',0,200,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N'),(5,3,9,'XXL','0.00','3.00','10.00',0,200,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N'),(6,3,10,'S','0.00','0.00','10.00',0,200,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N'),(7,3,10,'M','0.00','3.20','10.00',0,200,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N'),(8,3,10,'L','0.00','0.00','10.00',0,200,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N'),(9,3,10,'XL','0.00','0.00','10.00',0,200,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N'),(10,3,10,'XXL','0.00','343','10.00',0,200,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N'),(11,3,12,'S','0.00','0.00','10.00',0,200,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N'),(12,3,12,'M','0.00','34','10.00',0,200,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N'),(13,3,12,'L','0.00','0.00','10.00',0,200,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N'),(14,3,12,'XL','0.00','0.00','10.00',0,200,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N'),(15,3,12,'XXL','0.00','0.00','10.00',0,200,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N'),(16,3,14,'S','0.00','34','10.00',0,200,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N'),(17,3,14,'M','0.00','0.00','10.00',0,200,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N'),(18,3,14,'L','0.00','0.00','10.00',0,200,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N'),(19,3,14,'XL','0.00','10','10.00',0,200,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N'),(20,3,14,'XXL','0.00','0.00','10.00',0,200,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N'),(21,3,16,'S','0.00','7.23','10.00',0,200,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N'),(22,3,16,'M','0.00','67','10.00',0,200,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N'),(23,3,16,'L','0.00','0.00','10.00',0,200,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N'),(24,3,16,'XL','0.00','0.00','10.00',0,200,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N'),(25,3,16,'XXL','0.00','0.00','10.00',0,200,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N'),(26,3,23,'S','0.00','0.00','10.00',0,200,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N'),(27,3,23,'M','0.00','32.23','10.00',0,200,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N'),(28,3,23,'L','0.00','0.00','10.00',0,200,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N'),(29,3,23,'XL','0.00','0.00','10.00',0,200,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N'),(30,3,23,'XXL','0.00','0.99','10.00',0,200,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N'),(31,3,25,'S','0.00','0.00','10.00',0,200,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N'),(32,3,25,'M','0.00','0.00','10.00',0,200,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N'),(33,3,25,'L','0.00','0.00','10.00',0,200,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N'),(34,3,25,'XL','0.00','0.00','10.00',0,200,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N'),(35,3,25,'XXL','0.00','32.32','10.00',0,200,'SYSTEM','SYSTEM','2017-06-29 23:56:00','2017-06-29 23:56:00',NULL,'N');

/*Table structure for table `test_tb` */

DROP TABLE IF EXISTS `test_tb`;

CREATE TABLE `test_tb` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(24) DEFAULT NULL,
  `birthday` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;

/*Data for the table `test_tb` */

insert  into `test_tb`(`id`,`name`,`birthday`) values (1,'刘一手','2017-06-01 00:23:44'),(2,'张一国','2017-06-01 00:29:39'),(3,'卡星球','2017-06-01 21:41:38'),(5,'范冰冰','2017-06-01 21:59:28'),(6,'范冰冰','2017-06-01 22:02:30'),(34,'李冰冰','2017-06-05 23:08:40'),(35,'李冰冰','2017-06-05 23:08:40'),(36,'李冰冰','2017-06-05 23:15:01');

