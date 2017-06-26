package com.caoyong.core.service.product;

import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.product.Product;
import com.caoyong.core.bean.product.ProductQueryDTO;
import com.caoyong.exception.BizException;

public interface ProductService {
	/**
	 * 查询产品分页
	 * @param query
	 * @return
	 * @throws BizException
	 */
	Page<Product> selectPageByQuery(ProductQueryDTO query)throws BizException;
}
