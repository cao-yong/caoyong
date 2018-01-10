package com.caoyong.common.enums;

import java.util.stream.Stream;

/**
 * 访客统计时间枚举
 * 
 * @author caoyong
 * @time 2018年1月10日 下午3:33:56
 */
public enum VisitsStatisticsTimeEnum {
    TODAY("today", "今日"),
    TOMORROW("tomorrow", "昨日"),
    WEEK("week", "本周"),
    MONTH("month", "本月"),
    ALL("all", "累计 "),
    NONE("", "");
    /**
     * 编号
     */
    private String code;
    /**
     * 名称
     */
    private String name;

    private VisitsStatisticsTimeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    /**
     * 根据code获取名称
     * 
     * @param code 编码
     * @return 名称
     */
    public static String getNameByCode(String code) {
        return Stream.of(VisitsStatisticsTimeEnum.values()).filter(statistics -> statistics.code.equals(code))
                .findFirst().orElse(NONE).name;
    }
}
