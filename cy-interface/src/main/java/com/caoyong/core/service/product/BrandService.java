package com.caoyong.core.service.product;

import java.util.List;

import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.product.Brand;
import com.caoyong.core.bean.product.BrandQuery;
import com.caoyong.exception.BizException;

public interface BrandService {
	/**
	 * 查询分页对象
	 * @param query
	 * @return
	 * @throws BizException
	 */
	Page<Brand> selectPageByQuery(BrandQuery query)throws BizException;
	
	/**
	 * 根据id查询品牌
	 * @param id
	 * @return
	 * @throws BizException
	 */
	ResultBase<Brand> selectBrandById(Long id)throws BizException;
	
	/**
	 * 根据id修改品牌
	 * @param brand
	 * @return
	 * @throws BizException
	 */
	ResultBase<Integer> updateBrandById(Brand brand)throws BizException;
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 * @throws BizException
	 */
	ResultBase<Integer> deletes(Long[] ids)throws BizException;
	
	/**
	 * 查询结果集
	 * @param isDisplay
	 * @return
	 * @throws BizException
	 */
	List<Brand> selectListByQuery(Integer isDisplay)throws BizException;
	
	/**
	 * 从redis查询品牌结果集
	 * @return
	 * @throws BizException 
	 */
	List<Brand> selectBrandListFromRedis() throws BizException;
}
