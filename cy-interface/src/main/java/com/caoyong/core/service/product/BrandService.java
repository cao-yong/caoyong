package com.caoyong.core.service.product;

import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.product.Brand;
import com.caoyong.core.bean.product.BrandQuery;
import com.caoyong.exception.BizException;

public interface BrandService {
	/**
	 * 查询分页对象
	 * @param query
	 * @return
	 */
	Page<Brand> selectPageByQuery(BrandQuery query)throws BizException;
	
	/**
	 * 根据id查询品牌
	 * @param id
	 * @return
	 * @throws BizException
	 */
	Brand selectBrandById(Long id)throws BizException;
}
