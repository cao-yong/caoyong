package com.caoyong.common.web;

/**
 * 业务常量
 * 
 * @author yong.cao
 * @time 2017年6月14日下午11:36:42
 */
public interface Constants {
    //图片服务器
    String  IMG_URL              = "http://192.168.128.128/";
    //N常量
    String  CONSTANTS_N          = "N";
    //Y常量
    String  CONSTANTS_Y          = "Y";
    //SYSTEM常量
    String  SYSTEM               = "SYSTEM";
    //默认价格
    String  DEAFAULT_PRICE       = "0.00";
    //默认运费
    String  DEAFAULT_DELIVE_FEE  = "10.00";
    //默认购买上限
    Integer DEAFAULT_UPPER_LIMIT = 200;
    //默认库存
    Integer DEAFAULT_STOCK       = 0;
    //执行成功
    String  SUCCESS              = "执行成功";
    //MD5
    String  MD5                  = "MD5";
    //用户名
    String  USER_NAME            = "USER_NAME";
    //CSESSIONID
    String  CSESSIONID           = "CSESSIONID";
    //保存在cookie中购物车的名字
    String  BUYER_CART           = "BUYER_CART";
    //保存在redis中购物车的名字
    String  BUYER_CART_REDIS     = "buyerCart:";
    String  ONE                  = "1";
}
