package com.caoyong.common.enums;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * 订单状态枚举
 * 
 * @author yong.cao
 * @ 2017年7月29日 下午2:54:12
 */
public enum OrderStateEnum {
    SUBMIT_ORDER(0, "提交订单"),
    DISTRIBUTION(1, "仓库配货"),
    OUT_OF_STOCK(2, "商品出库"),
    WAITING_FOR_DELIVERY(3, "等待收货"),
    COMPLETE(4, "完成"),
    WAITING_FOR_REFUNDING(5, "待退货"),
    ALREADY_REFUNDING(6, "已退货");

    private Integer code;
    private String  name;

    OrderStateEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    /**
     * 获取枚举
     * 
     * @param code 编码
     * @return 枚举
     */
    public static OrderStateEnum getEnum(Integer code) {
        return Stream.of(OrderStateEnum.values()).filter(e -> e.code.equals(code))
                .findFirst().orElse(null);
    }
}
