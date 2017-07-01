package com.caoyong.core.service.product;

import java.util.List;

import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.product.Color;
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
	/**
	 * 查询颜色结果集
	 * @return
	 */
	ResultBase<List<Color>> selectColorList()throws BizException;
	/**
	 * 保存商品信息
	 * @param product
	 * @return
	 */
	ResultBase<Integer> saveProduct(Product product);
	
}
