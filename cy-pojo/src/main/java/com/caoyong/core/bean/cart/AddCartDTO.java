package com.caoyong.core.bean.cart;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 添加购物车DTO
 * 
 * @author yong.cao
 * @time 2017年7月22日上午10:52:32
 */

@Setter
@Getter
@ToString
public class AddCartDTO {
    /**
     * sku id
     */
    private Long    skuId;

    /**
     * 购买数量
     */
    private Integer amount;
}
