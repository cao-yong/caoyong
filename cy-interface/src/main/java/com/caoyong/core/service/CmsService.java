package com.caoyong.core.service;

import java.util.List;

import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.product.Product;
import com.caoyong.core.bean.product.Sku;
import com.caoyong.exception.BizException;

/**
 * cms服务
 * 
 * @author yong.cao
 * @time 2017年7月11日下午10:05:11
 */

public interface CmsService {
    /**
     * 根据商品id获取商品信息
     * 
     * @param productId
     * @return
     * @throws BizException
     */
    ResultBase<Product> selectProductById(Long productId) throws BizException;

    /**
     * 根据商品id获取sku结果集
     * 
     * @param productId
     * @return
     */
    ResultBase<List<Sku>> selectSkuListByProductId(Long productId);

}
