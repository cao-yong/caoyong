package com.caoyong.core.bean.product;

import com.caoyong.core.bean.base.BaseQuery;

import lombok.Data;
import lombok.EqualsAndHashCode;
@EqualsAndHashCode(callSuper=false)
public @Data class BrandQuery extends BaseQuery {
	private static final long serialVersionUID = -7067563747954314332L;
	//品牌ID  bigint
	private Long id;
	//品牌名称
	private String name;
	//描述
	private String description;
	//图片URL
	private String imgUrl;
	//排序  越大越靠前   
	private Integer sort;
	//是否可用   0 不可用 1 可用
	private Integer isDisplay;
}
