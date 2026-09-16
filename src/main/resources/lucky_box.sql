/*
 Navicat Premium Data Transfer

 Source Server         : vitual
 Source Server Type    : MySQL
 Source Server Version : 260700
 Source Host           : 192.168.88.128:3306
 Source Schema         : mystery_box

 Target Server Type    : MySQL
 Target Server Version : 260700
 File Encoding         : 65001

 Date: 16/09/2026 10:37:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for address
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '地址ID',
  `created_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `update_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `creator_id` bigint UNSIGNED NOT NULL COMMENT '创建人ID',
  `update_id` bigint UNSIGNED NOT NULL COMMENT '更新人ID',
  `user_id` bigint UNSIGNED NOT NULL COMMENT '所属用户ID',
  `latitude` double NOT NULL COMMENT '纬度',
  `longitude` double NOT NULL COMMENT '经度',
  `province` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '省',
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '市',
  `district` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '区',
  `details` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '详细地址',
  `house_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门牌号',
  `phone_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号',
  `real_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '真实姓名',
  `is_top` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否置顶',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '地址表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of address
-- ----------------------------

-- ----------------------------
-- Table structure for base_order
-- ----------------------------
DROP TABLE IF EXISTS `base_order`;
CREATE TABLE `base_order`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `created_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `update_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `creator_id` bigint UNSIGNED NOT NULL COMMENT '创建人ID（用户ID）',
  `update_id` bigint UNSIGNED NOT NULL,
  `payment_id` bigint UNSIGNED NOT NULL COMMENT '支付ID',
  `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单类型：PRODUCT-商品, MYSTERY_BOX-盲盒',
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'PENDING' COMMENT '订单状态：PENDING-待支付, PAID-已支付, SHIPPED-已发货, COMPLETED-已完成, CANCELLED-已取消, REFUNDED-已退款',
  `address` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地址快照',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `tracking_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流单号',
  `coupon_user_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '用户优惠券ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_creator_id`(`creator_id` ASC) USING BTREE,
  INDEX `idx_payment_id`(`payment_id` ASC) USING BTREE,
  INDEX `idx_type`(`type` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '基础订单（父表）' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of base_order
-- ----------------------------

-- ----------------------------
-- Table structure for card
-- ----------------------------
DROP TABLE IF EXISTS `card`;
CREATE TABLE `card`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '卡片ID',
  `created_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `update_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `creator_id` bigint UNSIGNED NOT NULL,
  `update_id` bigint UNSIGNED NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '卡片名称',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '卡片描述',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '卡片图标',
  `card_type_id` bigint UNSIGNED NOT NULL COMMENT '卡片类型ID',
  `probability` decimal(10, 6) NOT NULL COMMENT '基础抽中概率（0~1）',
  `is_limited` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否为限定卡片',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '卡片状态：0-启用, 1-禁用',
  `stock` int NULL DEFAULT NULL COMMENT '总发行量（NULL表示无限）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_card_type_id`(`card_type_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '卡片表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of card
-- ----------------------------

-- ----------------------------
-- Table structure for card_type
-- ----------------------------
DROP TABLE IF EXISTS `card_type`;
CREATE TABLE `card_type`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '卡片类型ID',
  `created_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `update_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `creator_id` bigint UNSIGNED NOT NULL,
  `update_id` bigint UNSIGNED NOT NULL,
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型编码（唯一），如 WESTERN_JOURNEY',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型名称，如 西游卡',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '类型描述',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_code`(`code` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '卡片类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of card_type
-- ----------------------------

-- ----------------------------
-- Table structure for coupon
-- ----------------------------
DROP TABLE IF EXISTS `coupon`;
CREATE TABLE `coupon`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '优惠券ID',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `creator_id` bigint UNSIGNED NOT NULL,
  `update_id` bigint UNSIGNED NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '优惠券名称',
  `sub_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '副标题',
  `rules` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '使用规则',
  `discount_type` tinyint UNSIGNED NOT NULL DEFAULT 1 COMMENT '优惠类型：1-满减, 2-折扣',
  `discount_value` bigint NOT NULL COMMENT '优惠数值（满减填金额(分)，折扣填系数如80=8折）',
  `threshold_price` bigint NOT NULL DEFAULT 0 COMMENT '使用门槛（单位：分），0表示无门槛',
  `sale_price` bigint NOT NULL DEFAULT 0 COMMENT '购买售价（单位：分），0表示免费',
  `scope_type` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '适用范围：0-全场, 1-仅盲盒, 2-仅商品',
  `type` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '券类型：0-普通券, 1-秒杀券',
  `status` tinyint UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态：1-上架, 2-下架, 3-过期',
  `total_quantity` int UNSIGNED NULL DEFAULT NULL COMMENT '发行总量（NULL表示无限）',
  `used_quantity` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '已使用数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '优惠券定义表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of coupon
-- ----------------------------

-- ----------------------------
-- Table structure for coupon_seckill
-- ----------------------------
DROP TABLE IF EXISTS `coupon_seckill`;
CREATE TABLE `coupon_seckill`  (
  `coupon_id` bigint UNSIGNED NOT NULL COMMENT '优惠券ID（主键，一对一关联）',
  `stock` int NOT NULL COMMENT '秒杀剩余库存',
  `begin_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '秒杀开始时间',
  `end_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '秒杀结束时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`coupon_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '秒杀优惠券扩展表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of coupon_seckill
-- ----------------------------

-- ----------------------------
-- Table structure for lucky_box
-- ----------------------------
DROP TABLE IF EXISTS `lucky_box`;
CREATE TABLE `lucky_box`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '盲盒ID',
  `created_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `update_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `creator_id` bigint UNSIGNED NOT NULL,
  `update_id` bigint UNSIGNED NOT NULL,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '盲盒名称',
  `details` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '盲盒详情',
  `tips` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '购买提示',
  `price` bigint NOT NULL COMMENT '价格（单位：分）',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '封面',
  `category_id` bigint UNSIGNED NOT NULL COMMENT '类别ID',
  `box_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'PRODUCT' COMMENT '盲盒类型：PRODUCT-商品盲盒, CARD-抽卡盲盒',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '状态：0-启用, 1-禁用',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category_id`(`category_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '盲盒' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lucky_box
-- ----------------------------

-- ----------------------------
-- Table structure for lucky_box_card
-- ----------------------------
DROP TABLE IF EXISTS `lucky_box_card`;
CREATE TABLE `lucky_box_card`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `created_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `update_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `creator_id` bigint UNSIGNED NOT NULL,
  `update_id` bigint UNSIGNED NOT NULL,
  `lucky_box_id` bigint UNSIGNED NOT NULL COMMENT '盲盒ID',
  `card_id` bigint UNSIGNED NOT NULL COMMENT '卡片ID',
  `probability_override` decimal(10, 6) NULL DEFAULT NULL COMMENT '覆盖卡片默认概率',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_mystery_box_id`(`lucky_box_id` ASC) USING BTREE,
  INDEX `idx_card_id`(`card_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '盲盒-卡片关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lucky_box_card
-- ----------------------------

-- ----------------------------
-- Table structure for lucky_box_category
-- ----------------------------
DROP TABLE IF EXISTS `lucky_box_category`;
CREATE TABLE `lucky_box_category`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '类别ID',
  `created_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `update_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `creator_id` bigint UNSIGNED NOT NULL,
  `update_id` bigint UNSIGNED NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类别名称',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类别图标',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '描述',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '盲盒类别' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lucky_box_category
-- ----------------------------

-- ----------------------------
-- Table structure for lucky_box_order
-- ----------------------------
DROP TABLE IF EXISTS `lucky_box_order`;
CREATE TABLE `lucky_box_order`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `created_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `update_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `creator_id` bigint UNSIGNED NOT NULL,
  `update_id` bigint UNSIGNED NOT NULL,
  `base_order_id` bigint UNSIGNED NOT NULL COMMENT '盲盒订单ID',
  `lucky_box_id` bigint UNSIGNED NOT NULL COMMENT '盲盒ID',
  `lucky_box_snapshot` json NOT NULL COMMENT '盲盒信息快照',
  `lucky_box_count` int NOT NULL COMMENT '盲盒数量',
  `winning_product_sku_id` bigint UNSIGNED NOT NULL COMMENT '抽中的商品SKU ID',
  `winning_product_snapshot` json NOT NULL COMMENT '中奖商品快照',
  `product_order_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '关联的商品订单ID（发货单）',
  `cards_drawn` json NULL COMMENT '附赠的卡片列表快照',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_mystery_box_order_id`(`base_order_id` ASC) USING BTREE,
  INDEX `idx_product_order_id`(`product_order_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '盲盒订单项' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lucky_box_order
-- ----------------------------

-- ----------------------------
-- Table structure for lucky_box_product
-- ----------------------------
DROP TABLE IF EXISTS `lucky_box_product`;
CREATE TABLE `lucky_box_product`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `created_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `update_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `creator_id` bigint UNSIGNED NOT NULL,
  `update_id` bigint UNSIGNED NOT NULL,
  `lucky_box_id` bigint UNSIGNED NOT NULL COMMENT '盲盒ID',
  `product_sku_id` bigint UNSIGNED NOT NULL COMMENT '商品SKU ID',
  `probability` decimal(10, 6) NOT NULL COMMENT '抽中概率（0~1）',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序权重',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_mystery_box_id`(`lucky_box_id` ASC) USING BTREE,
  INDEX `idx_product_sku_id`(`product_sku_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '盲盒-商品奖池关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lucky_box_product
-- ----------------------------

-- ----------------------------
-- Table structure for payment
-- ----------------------------
DROP TABLE IF EXISTS `payment`;
CREATE TABLE `payment`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '支付ID',
  `created_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `update_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `creator_id` bigint UNSIGNED NOT NULL,
  `update_id` bigint UNSIGNED NOT NULL,
  `pay_type` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付方式',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `pay_amount` bigint NOT NULL COMMENT '实付金额（单位：分）',
  `coupon_amount` bigint NOT NULL DEFAULT 0 COMMENT '优惠券优惠金额（单位：分）',
  `product_amount` bigint NOT NULL COMMENT '商品总价（单位：分）',
  `trade_no` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方交易号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '支付表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of payment
-- ----------------------------

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `created_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `update_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `creator_id` bigint UNSIGNED NOT NULL,
  `update_id` bigint UNSIGNED NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `price` bigint NOT NULL COMMENT '价格（单位：分）',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面',
  `brand` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '品牌',
  `category_id` bigint UNSIGNED NOT NULL COMMENT '类别ID',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '描述',
  `tags` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `specifications` json NULL COMMENT '规格',
  `attributes` json NULL COMMENT '属性',
  `quality_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '品质类型',
  `sale` bigint NULL DEFAULT NULL COMMENT '销量',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category_id`(`category_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (4, '2026-09-01 15:29:24.652637', '2026-09-01 15:29:24.653637', 2, 2, 'iPhone 15 Pro Max', 899900, NULL, 'Apple', 2, '最新款旗舰手机，搭载A17芯片，钛金属边框', '热销,新品,5G,旗舰', '{\"存储\": \"256GB\", \"网络\": \"5G\", \"颜色\": \"原色钛金属\", \"屏幕尺寸\": \"6.7英寸\"}', '{\"产地\": \"中国\", \"保修期\": \"12个月\", \"包装清单\": \"手机、数据线、卡针\"}', 'QUALITY_A', NULL);
INSERT INTO `product` VALUES (5, '2026-09-15 20:19:28.715044', '2026-09-15 20:19:28.717074', 2, 2, 'OPPO A7 Pro Max', 299900, 'https://example.com/images/oppo_a7_pro_max.jpg', 'OPPO', 2, '机身尺寸：长162.98mm，宽77.97mm，厚8.57mm。机身重量：226g。入网型号：PYC110，上市日期：2026-08-07。', '5G,快充,高像素,OLED屏', '{\"屏幕刷新率\": \"120Hz\"}', '{\"上市日期\": \"2026-08-07\", \"入网型号\": \"PYC110\", \"机身尺寸\": \"长162.98mm,宽77.97mm,厚8.57mm\", \"机身重量\": \"226g\"}', 'QUALITY_A', NULL);

-- ----------------------------
-- Table structure for product_category
-- ----------------------------
DROP TABLE IF EXISTS `product_category`;
CREATE TABLE `product_category`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '类别ID',
  `created_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `update_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `creator_id` bigint UNSIGNED NOT NULL,
  `update_id` bigint UNSIGNED NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类别名称',
  `parent_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '父类别ID',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类别图标',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '描述',
  `sort_order` int NOT NULL DEFAULT 0 COMMENT '排序号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品类别' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of product_category
-- ----------------------------
INSERT INTO `product_category` VALUES (1, '2026-08-20 14:45:40.180808', '2026-08-20 14:45:40.183814', 2, 2, '电子产品', NULL, NULL, '手机、电脑、平板等数码产品', 1);
INSERT INTO `product_category` VALUES (2, '2026-08-20 14:48:44.063989', '2026-08-20 14:48:44.066512', 2, 2, '手机', 1, NULL, '手机产品', 1);
INSERT INTO `product_category` VALUES (3, '2026-08-20 14:49:59.103116', '2026-08-20 14:49:59.104115', 2, 2, '电脑', 1, NULL, '电脑产品', 2);
INSERT INTO `product_category` VALUES (4, '2026-08-20 14:50:59.081733', '2026-08-20 14:50:59.081733', 2, 2, '服装', NULL, NULL, '上衣，裤子，裙子等', 2);
INSERT INTO `product_category` VALUES (5, '2026-08-20 14:51:40.421823', '2026-08-20 14:51:40.421823', 2, 2, '裤子', 4, NULL, '裤子', 1);
INSERT INTO `product_category` VALUES (6, '2026-08-20 14:51:51.251795', '2026-08-20 14:51:51.251795', 2, 2, '裙子', 4, NULL, '裙子', 2);
INSERT INTO `product_category` VALUES (7, '2026-08-20 14:56:52.177356', '2026-08-20 14:56:52.177356', 2, 2, '短袖', 4, NULL, '短袖', 3);
INSERT INTO `product_category` VALUES (8, '2026-08-20 14:57:11.390148', '2026-08-20 16:02:36.257951', 2, 2, '棉袄', 4, NULL, '棉袄', 4);
INSERT INTO `product_category` VALUES (12, '2026-08-20 19:16:37.649334', '2026-08-20 19:16:37.651865', 2, 2, '礼服', 4, NULL, '礼服', 3);

-- ----------------------------
-- Table structure for product_order
-- ----------------------------
DROP TABLE IF EXISTS `product_order`;
CREATE TABLE `product_order`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `created_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `update_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `creator_id` bigint UNSIGNED NOT NULL,
  `update_id` bigint UNSIGNED NOT NULL,
  `base_order_id` bigint UNSIGNED NOT NULL COMMENT '商品订单ID',
  `product_sku_id` bigint UNSIGNED NOT NULL COMMENT '商品SKU ID',
  `count` int NOT NULL COMMENT '数量',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_product_order_id`(`base_order_id` ASC) USING BTREE,
  INDEX `idx_product_sku_id`(`product_sku_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品订单项' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of product_order
-- ----------------------------

-- ----------------------------
-- Table structure for product_sku
-- ----------------------------
DROP TABLE IF EXISTS `product_sku`;
CREATE TABLE `product_sku`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'SKU ID',
  `created_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `update_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `creator_id` bigint UNSIGNED NOT NULL,
  `update_id` bigint UNSIGNED NOT NULL,
  `product_id` bigint UNSIGNED NOT NULL COMMENT '商品ID',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格封面',
  `price` bigint NULL DEFAULT NULL COMMENT '价格（单位：分）',
  `stock` int NULL DEFAULT NULL COMMENT '库存',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `specification` json NULL COMMENT '规格',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_product_id`(`product_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品SKU' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of product_sku
-- ----------------------------
INSERT INTO `product_sku` VALUES (1, '2026-09-15 20:29:40.421318', '2026-09-15 20:29:40.425538', 2, 2, 5, 'https://example.com/images/oppo_orange_12_256.jpg', 229415, 100, '前橙似锦 12GB+256GB 版', '{\"存储容量\": \"256GB\", \"机身颜色\": \"前橙似锦\", \"运行内存\": \"12GB\"}');
INSERT INTO `product_sku` VALUES (2, '2026-09-15 20:30:01.620888', '2026-09-15 20:30:01.620888', 2, 2, 5, 'https://example.com/images/oppo_blue_16_512.jpg', 259900, 50, '深海蓝 16GB+512GB 版', '{\"存储容量\": \"512GB\", \"机身颜色\": \"深海蓝\", \"运行内存\": \"16GB\"}');
INSERT INTO `product_sku` VALUES (3, '2026-09-15 20:30:10.261663', '2026-09-15 20:30:10.261663', 2, 2, 4, 'https://example.com/images/iphone15_titanium_256.jpg', 899900, 100, '原色钛金属 256GB 版', '{\"存储\": \"256GB\", \"网络\": \"5G\", \"颜色\": \"原色钛金属\", \"屏幕尺寸\": \"6.7英寸\"}');
INSERT INTO `product_sku` VALUES (4, '2026-09-15 20:30:22.238726', '2026-09-15 20:30:22.238726', 2, 2, 4, 'https://example.com/images/iphone15_black_512.jpg', 1099900, 50, '黑色钛金属 512GB 版', '{\"存储\": \"512GB\", \"网络\": \"5G\", \"颜色\": \"黑色钛金属\", \"屏幕尺寸\": \"6.7英寸\"}');

-- ----------------------------
-- Table structure for refund_record
-- ----------------------------
DROP TABLE IF EXISTS `refund_record`;
CREATE TABLE `refund_record`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `created_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `update_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `creator_id` bigint UNSIGNED NOT NULL,
  `update_id` bigint UNSIGNED NOT NULL,
  `order_id` bigint UNSIGNED NOT NULL COMMENT '关联的基础订单ID',
  `reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '退款理由',
  `amount` bigint NOT NULL COMMENT '退款金额（单位：分）',
  `status` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '退款状态',
  `refund_application_details` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '退款申请详情',
  `refund_notify_details` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '退款回调详情',
  `refund_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方退款单号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '退款记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of refund_record
-- ----------------------------

-- ----------------------------
-- Table structure for seckill_order
-- ----------------------------
DROP TABLE IF EXISTS `seckill_order`;
CREATE TABLE `seckill_order`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `created_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `update_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `creator_id` bigint UNSIGNED NOT NULL COMMENT '下单用户ID',
  `update_id` bigint UNSIGNED NOT NULL,
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单编号（唯一）',
  `coupon_id` bigint UNSIGNED NOT NULL COMMENT '购买的秒杀券ID',
  `quantity` int NOT NULL DEFAULT 1 COMMENT '购买数量',
  `unit_price` bigint NOT NULL COMMENT '单价（单位：分）',
  `total_price` bigint NOT NULL COMMENT '总价（单位：分）',
  `pay_type` tinyint UNSIGNED NOT NULL DEFAULT 1 COMMENT '支付方式：1-微信, 2-支付宝, 3-余额',
  `status` tinyint UNSIGNED NOT NULL DEFAULT 1 COMMENT '订单状态：1-待支付, 2-已支付, 3-已取消, 4-已退款',
  `pay_time` timestamp NULL DEFAULT NULL COMMENT '支付时间',
  `cancel_time` timestamp NULL DEFAULT NULL COMMENT '取消时间',
  `refund_time` timestamp NULL DEFAULT NULL COMMENT '退款时间',
  `trade_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方支付交易号',
  `refund_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方退款单号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_creator_id`(`creator_id` ASC) USING BTREE,
  INDEX `idx_coupon_id`(`coupon_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '秒杀订单表（独立）' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of seckill_order
-- ----------------------------

-- ----------------------------
-- Table structure for synthesis_record
-- ----------------------------
DROP TABLE IF EXISTS `synthesis_record`;
CREATE TABLE `synthesis_record`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `created_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `update_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `creator_id` bigint UNSIGNED NOT NULL,
  `update_id` bigint UNSIGNED NOT NULL,
  `user_id` bigint UNSIGNED NOT NULL COMMENT '用户ID',
  `rule_id` bigint UNSIGNED NOT NULL COMMENT '合成规则ID',
  `consumed_card_ids` json NOT NULL COMMENT '消耗的卡片ID列表',
  `result_product_sku_id` bigint UNSIGNED NOT NULL COMMENT '获得的商品ID',
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'SUCCESS' COMMENT '合成状态',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_rule_id`(`rule_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '合成记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of synthesis_record
-- ----------------------------

-- ----------------------------
-- Table structure for synthesis_rule
-- ----------------------------
DROP TABLE IF EXISTS `synthesis_rule`;
CREATE TABLE `synthesis_rule`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `created_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `update_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `creator_id` bigint UNSIGNED NOT NULL,
  `update_id` bigint UNSIGNED NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '合成配方名称',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '配方描述',
  `condition_rule` json NOT NULL COMMENT '合成条件规则（JSON）',
  `result_product_sku_id` bigint UNSIGNED NOT NULL COMMENT '合成获得的商品ID',
  `result_product_snapshot` json NULL COMMENT '商品快照',
  `enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用',
  `total_quota` int NULL DEFAULT NULL COMMENT '总合成配额（NULL表示无限）',
  `used_quota` int NOT NULL DEFAULT 0 COMMENT '已合成次数',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_result_product_id`(`result_product_sku_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '合成规则表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of synthesis_rule
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `created_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `update_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `gender` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '性别',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `status` tinyint NOT NULL COMMENT '用户状态',
  `role` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'USER' COMMENT '角色：USER-普通用户, ADMIN-管理员',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_phone`(`phone` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (2, '2026-08-19 16:59:48.204380', '2026-08-19 16:59:48.205379', 'admin', NULL, NULL, '15789788978', '$2a$10$XX6iLtmtgVWZIokElbcZ/OsIuXrKQGkU8jOncagEi/eprSw0zG0bq', 0, 'ADMIN');
INSERT INTO `user` VALUES (5, '2026-08-19 18:23:57.504650', '2026-08-19 18:23:57.506649', 'user_eRGZceg5zO', NULL, NULL, '15844570908', NULL, 0, 'USER');

-- ----------------------------
-- Table structure for user_card
-- ----------------------------
DROP TABLE IF EXISTS `user_card`;
CREATE TABLE `user_card`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `created_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `update_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `creator_id` bigint UNSIGNED NOT NULL,
  `update_id` bigint UNSIGNED NOT NULL,
  `user_id` bigint UNSIGNED NOT NULL COMMENT '用户ID',
  `card_id` bigint UNSIGNED NOT NULL COMMENT '卡片ID',
  `count` int NOT NULL DEFAULT 0 COMMENT '拥有数量',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_card`(`user_id` ASC, `card_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户卡片' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_card
-- ----------------------------

-- ----------------------------
-- Table structure for user_coupon
-- ----------------------------
DROP TABLE IF EXISTS `user_coupon`;
CREATE TABLE `user_coupon`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint UNSIGNED NOT NULL COMMENT '用户ID',
  `coupon_id` bigint UNSIGNED NOT NULL COMMENT '优惠券ID',
  `status` tinyint UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态：1-未使用, 2-已使用, 3-已过期, 4-已退款',
  `acquired_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '领取时间',
  `used_time` timestamp NULL DEFAULT NULL COMMENT '使用时间',
  `expire_time` timestamp NULL DEFAULT NULL COMMENT '过期时间',
  `base_order_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '使用的订单ID（关联base_order）',
  `source_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'PURCHASE' COMMENT '来源：PURCHASE-付费购买, SECKILL-秒杀, ADMIN-后台赠送',
  `source_order_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '来源订单ID（购买时关联seckill_order.id）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_coupon`(`user_id` ASC, `coupon_id` ASC, `status` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_base_order_id`(`base_order_id` ASC) USING BTREE,
  INDEX `idx_source_order_id`(`source_order_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户优惠券表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_coupon
-- ----------------------------

-- ----------------------------
-- Table structure for user_we_chat
-- ----------------------------
DROP TABLE IF EXISTS `user_we_chat`;
CREATE TABLE `user_we_chat`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `created_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `update_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `user_id` bigint UNSIGNED NOT NULL COMMENT '用户ID',
  `open_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '微信open_id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_open_id`(`open_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '微信用户关联' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_we_chat
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
