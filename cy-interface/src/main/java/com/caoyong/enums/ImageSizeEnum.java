package com.caoyong.enums;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 图片大小规则
 * 
 * @author yong.cao
 * @time 2018年7月13日 下午10:39:30
 */
public enum ImageSizeEnum {
    IMG_SIZE_1200(1200, "_1200_480"),
    IMG_SIZE_540(540, "_720_540"),
    IMG_SIZE_320(320, "_480_320"),
    IMG_SIZE_270(270, "_360_270"),
    IMG_SIZE_200(200, "_300_200"),
    IMG_SIZE_150(150, "_200_150"),
    IMG_SIZE_80(80, "_200_80"),
    IMG_SIZE_120(120, "_180_120"),
    IMG_SIZE_91(91, "_121_91"),
    IMG_SIZE_1280(1280, "_1280"),
    IMG_SIZE_960(960, "_960"),
    IMG_SIZE_720(720, "_720"),
    IMG_SIZE_480(480, "_480");

    /**
     * 编码
     */
    private Integer code;
    /**
     * 名称
     */
    private String  name;

    ImageSizeEnum(Integer code, String name) {
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
     * @return 编码对应的枚举
     */
    public static ImageSizeEnum getEnum(Integer code) {
        return Stream.of(ImageSizeEnum.values()).filter(en -> en.getCode().equals(code)).findFirst().orElse(null);
    }

    /**
     * 获取枚举中所有的name值
     * 
     * @return name值的集合
     */
    public static List<String> getAllNames() {
        return Stream.of(ImageSizeEnum.values()).map(ImageSizeEnum::getName).collect(Collectors.toList());
    }
}
