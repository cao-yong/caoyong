package com.caoyong.common.enums;

import java.util.stream.Stream;

/**
 * 货到付款方式枚举
 * 
 * @author yong.cao
 * @ 2017年7月29日 下午2:54:35
 */
public enum PaymentCashEnum {
    CASH(1, "现金"),
    POS(2, "POS刷卡");

    private Integer code;
    private String  name;

    PaymentCashEnum(Integer code, String name) {
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
    public static PaymentCashEnum getEnum(Integer code) {
        return Stream.of(PaymentCashEnum.values()).filter(e -> e.code.equals(code)).findFirst().orElse(null);
    }
}
