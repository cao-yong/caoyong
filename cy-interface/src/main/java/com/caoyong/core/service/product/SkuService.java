package com.caoyong.core.service.product;

import java.util.List;

import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.product.Sku;
import com.caoyong.exception.BizException;

public interface SkuService {
	/**
	 * 查询库存
	 * @param productId
	 * @return
	 */
	ResultBase<List<Sku>> selectSkuByProductId(Long productId)throws BizException;

}
