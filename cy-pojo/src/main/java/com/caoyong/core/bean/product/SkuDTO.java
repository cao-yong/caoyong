package com.caoyong.core.bean.product;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SkuDTO implements Serializable {

    private static final long serialVersionUID = -319069081731983372L;

    /**
     * ID
     */
    private Long              id;

    /**
     * 市场价
     */
    private String            marketPrice;

    /**
     * 售价
     */
    private String            price;

    /**
     * 运费 默认10元
     */
    private String            deliveFee;

    /**
     * 库存
     */
    private Integer           stock;

    /**
     * 购买限制
     */
    private Integer           upperLimit;

    /**
     * 操作
     */
    private String            oper;

}
