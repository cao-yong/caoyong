package com.caoyong.core.bean.product;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductIsShowVO implements Serializable {

    private static final long serialVersionUID = -4253781910315395673L;

    private Long[]            ids;

    private Integer           showType;
}
