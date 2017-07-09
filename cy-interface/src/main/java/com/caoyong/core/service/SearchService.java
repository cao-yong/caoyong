package com.caoyong.core.service;

import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.product.Product;
import com.caoyong.core.bean.product.ProductQueryDTO;
import com.caoyong.exception.BizException;

public interface SearchService {
	/**
	 * 全文检索
	 * @param query
	 * @return
	 * @throws BizException
	 */
	Page<Product> selectPageByQuery(ProductQueryDTO query) throws BizException;
}
