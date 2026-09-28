/*
 Navicat Premium Data Transfer

 Source Server         : vitual
 Source Server Type    : MySQL
 Source Server Version : 260700
 Source Host           : 192.168.88.128:3306
 Source Schema         : lucky_box

 Target Server Type    : MySQL
 Target Server Version : 260700
 File Encoding         : 65001

 Date: 27/09/2026 18:42:20
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
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '地址表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for base_order
-- ----------------------------
DROP TABLE IF EXISTS `base_order`;
CREATE TABLE `base_order`  (
  `id` bigint UNSIGNED NOT NULL COMMENT '订单ID',
  `created_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `update_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `creator_id` bigint UNSIGNED NOT NULL COMMENT '创建人ID（用户ID）',
  `update_id` bigint UNSIGNED NOT NULL,
  `payment_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '支付ID',
  `type` tinyint NOT NULL COMMENT '订单类型：0-商品, 1-盲盒',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '订单状态：0-待支付, 1-已支付, 2-已发货, 3-已完成, 4-已取消, 5-已退款',
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
  `box_type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '盲盒类型：0-商品盲盒, 1-抽卡盲盒',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '状态：0-启用, 1-禁用',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category_id`(`category_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '盲盒' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '盲盒-卡片关联表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '盲盒类别' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for lucky_box_order
-- ----------------------------
DROP TABLE IF EXISTS `lucky_box_order`;
CREATE TABLE `lucky_box_order`  (
  `base_order_id` bigint UNSIGNED NOT NULL,
  `lucky_box_id` bigint UNSIGNED NOT NULL COMMENT '盲盒ID',
  `lucky_box_snapshot` json NOT NULL COMMENT '盲盒信息快照',
  `lucky_box_count` int NOT NULL COMMENT '盲盒数量',
  `winning_product_sku_id` bigint UNSIGNED NOT NULL COMMENT '抽中的商品SKU ID',
  `winning_product_snapshot` json NOT NULL COMMENT '中奖商品快照',
  `product_order_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '关联的商品订单ID（发货单）',
  `cards_drawn` json NULL COMMENT '附赠的卡片列表快照',
  PRIMARY KEY (`base_order_id`) USING BTREE,
  INDEX `idx_product_order_id`(`product_order_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '盲盒订单项' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '盲盒-商品奖池关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for notification
-- ----------------------------
DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '通知ID',
  `created_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `update_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `creator_id` bigint UNSIGNED NOT NULL COMMENT '创建人ID',
  `update_id` bigint UNSIGNED NOT NULL COMMENT '更新人ID',
  `user_id` bigint UNSIGNED NOT NULL COMMENT '接收用户ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
  `type` tinyint NOT NULL COMMENT '类型：0-SYSTEM-系统, 1-BOX-盲盒, 2-ORDER-订单',
  `biz_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '关联业务ID（如盲盒ID）',
  `is_read` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已读：0-未读, 1-已读',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_read`(`user_id` ASC, `is_read` ASC) USING BTREE,
  INDEX `idx_created_time`(`created_time` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '消息通知表' ROW_FORMAT = Dynamic;

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
-- Table structure for product_order
-- ----------------------------
DROP TABLE IF EXISTS `product_order`;
CREATE TABLE `product_order`  (
  `base_order_id` bigint UNSIGNED NOT NULL COMMENT '商品订单ID',
  `product_sku_id` bigint UNSIGNED NOT NULL COMMENT '商品SKU ID',
  `count` int NOT NULL COMMENT '数量',
  PRIMARY KEY (`base_order_id`) USING BTREE,
  INDEX `idx_product_order_id`(`base_order_id` ASC) USING BTREE,
  INDEX `idx_product_sku_id`(`product_sku_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品订单项' ROW_FORMAT = DYNAMIC;

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

SET FOREIGN_KEY_CHECKS = 1;
