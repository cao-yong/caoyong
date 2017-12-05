package com.caoyong.core.bean.product;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

public @Data class Brand implements Serializable {
    private static final long serialVersionUID = 127735330027307707L;
    //品牌ID  bigint
    private Long              id;
    //品牌名称
    private String            name;
    //描述
    private String            description;
    //图片URL
    private String            imgUrl;
    //排序  越大越靠前   
    private Integer           sort;
    //是否可用   0 不可用 1 可用
    private Integer           isDisplay;                             //is_display
    //修改时间
    private Date              updateTime;
}
