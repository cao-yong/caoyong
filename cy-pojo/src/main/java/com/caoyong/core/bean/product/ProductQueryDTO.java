package com.caoyong.core.bean.product;

import com.caoyong.core.bean.base.BaseQuery;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductQueryDTO extends BaseQuery {
	private static final long serialVersionUID = 7557765012813704363L;

	/**
     * ID或商品编号
     */
    private Long id;

    /**
     * 品牌ID
     */
    private Long brandId;

    /**
     * 商品名称
     */
    private String name;


    /**
     * 是否新品:0:旧品,1:新品
     */
    private Boolean isNew;

    /**
     * 是否热销:0,否 1:是
     */
    private Boolean isHot;

    /**
     * 推荐 1推荐 0 不推荐
     */
    private Boolean isCommend;

    /**
     * 上下架:0否 1是
     */
    private Boolean isShow;

    /**
     * 商品描述
     */
    private String description;

}