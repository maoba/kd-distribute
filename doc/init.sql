DROP TABLE IF EXISTS `kd_order`;
CREATE TABLE `kd_order` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `order_status` int(4) NOT NULL DEFAULT 1 COMMENT '订单状态 1-待支付',
  `receiver_name` varchar(256) NOT NULL DEFAULT '' COMMENT '收货人名称',
  `receiver_mobile` varchar(256) NOT NULL DEFAULT '' COMMENT '收货人联系方式',
  `order_amount` decimal(20,2) NOT NULL DEFAULT 0.00 COMMENT '订单金额',
  `time_created` timestamp NOT NULL DEFAULT current_timestamp(),
  `time_modified` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `create_user` varchar(256) NOT NULL DEFAULT '' COMMENT '创建人',
  `update_user` varchar(256) NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- ----------------------------
-- Table structure for `kd_order_item`
-- ----------------------------
DROP TABLE IF EXISTS `kd_order_item`;
CREATE TABLE `kd_order_item` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL DEFAULT 0 COMMENT '订单id',
  `product_id` int(11) NOT NULL DEFAULT 0 COMMENT '产品id',
  `purchase_price` decimal(20,2) NOT NULL DEFAULT 0.00 COMMENT '购买的金额',
  `purchase_num` int(3) NOT NULL DEFAULT 0 COMMENT '购买的数量',
  `time_created` timestamp NOT NULL DEFAULT current_timestamp(),
  `time_modified` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `create_user` varchar(256) NOT NULL DEFAULT '' COMMENT '创建人',
  `update_user` varchar(256) NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单商品表';

-- ----------------------------
-- Table structure for `product`
-- ----------------------------
DROP TABLE IF EXISTS `kd_product`;
CREATE TABLE `kd_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_name` varchar(256) NOT NULL DEFAULT '' COMMENT '产品名称',
  `price` decimal(20,2) NOT NULL DEFAULT 0.00 COMMENT '价格',
  `count` int(11) NOT NULL DEFAULT 0 COMMENT '库存',
  `product_desc` varchar(256) NOT NULL DEFAULT '' COMMENT '产品描述',
  `time_created` timestamp NOT NULL DEFAULT current_timestamp(),
  `time_modified` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `create_user` varchar(256) NOT NULL DEFAULT '' COMMENT '创建人',
  `update_user` varchar(256) NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100101 DEFAULT CHARSET=utf8mb4 COMMENT='产品表';


DROP TABLE IF EXISTS `distribute_lock`;
CREATE TABLE `distribute_lock` (
  `id` int(11) NOT NULL,
  `business_code` varchar(255) NOT NULL,
  `business_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `kd_product` VALUES ('100100', '测试商品信息', '5.00', '1', '测试商品', '2020-12-24 22:36:43', '2020-12-24 22:36:43', '老猫', '');