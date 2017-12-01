package com.caoyong.common.enums;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * 支付状态枚举
 * 
 * @author yong.cao
 * @time 2017年7月23日下午6:34:13
 */
public enum ProductIsShowEnum {
    ALL(null, "全部"),
    PUT_OFF(0, "下架"),
    PUT_ON(1, "上架");
    private Integer value;
    private String  showName;

    private ProductIsShowEnum(Integer value, String showName) {
        this.value = value;
        this.showName = showName;
    }

    public Integer getValue() {
        return value;
    }

    public String getShowName() {
        return showName;
    }

    /**
     * 获取枚举
     * 
     * @param value
     * @return
     */
    public static ProductIsShowEnum getEnum(Integer value) {
        Optional<ProductIsShowEnum> en = Stream.of(ProductIsShowEnum.values()).filter(e -> e.value.equals(value))
                .findFirst();
        if (en.isPresent()) {
            return en.get();
        }
        return null;
    }
}
