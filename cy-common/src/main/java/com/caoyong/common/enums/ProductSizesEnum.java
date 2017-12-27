package com.caoyong.common.enums;

import java.util.stream.Stream;

/**
 * 商品尺码规则枚举
 * 
 * @author caoyong
 * @since 2017年12月5日 下午2:00:11
 */
public enum ProductSizesEnum {
    S(1, "S"),
    M(2, "M"),
    L(3, "L"),
    XL(4, "XL"),
    XXL(5, "XXL");
    private Integer value;
    private String  showName;

    ProductSizesEnum(Integer value, String showName) {
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
     * @param value 枚举值
     * @return 枚举
     */
    public static ProductSizesEnum getEnum(Integer value) {
        return Stream.of(ProductSizesEnum.values()).filter(e -> e.value.equals(value)).findFirst().orElse(null);
    }
}
