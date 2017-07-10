package com.caoyong.core.service;

import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.product.Product;
import com.caoyong.core.bean.product.ProductQueryDTO;
import com.caoyong.exception.BizException;
/**
 * 全文检索服务
 * @author yong.cao
 * @time 2017年7月10日下午10:49:07
 */
public interface SearchService {
	/**
	 * 全文检索
	 * @param query
	 * @return
	 * @throws BizException
	 */
	Page<Product> selectPageByQuery(ProductQueryDTO query) throws BizException;
	
	/**
	 * 保存商品信息到solr服务器
	 * @param id
	 * @throws BizException
	 */
	void insertProductToSolr(Long id) throws BizException;
}
