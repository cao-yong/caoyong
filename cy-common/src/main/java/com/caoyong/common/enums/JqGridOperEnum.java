package com.caoyong.common.enums;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * 商品尺码规则枚举
 * 
 * @author caoyong
 * @time 2017年12月5日 下午2:00:11
 */
public enum JqGridOperEnum {
    DEL("del", "删除"),
    EDIT("edit", "编辑");
    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    private JqGridOperEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 获取枚举
     * 
     * @param code
     * @return
     */
    public static JqGridOperEnum getEnum(String code) {
        Optional<JqGridOperEnum> en = Stream.of(JqGridOperEnum.values()).filter(e -> e.getCode().equals(code))
                .findFirst();
        if (en.isPresent()) {
            return en.get();
        }
        return null;
    }
}
