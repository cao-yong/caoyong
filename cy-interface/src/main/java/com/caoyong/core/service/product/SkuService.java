package com.caoyong.core.service.product;

import java.util.List;

import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.cart.BuyerCart;
import com.caoyong.core.bean.product.Sku;
import com.caoyong.core.bean.product.SkuDTO;
import com.caoyong.core.bean.product.SkuQueryDTO;
import com.caoyong.exception.BizException;

/**
 * sku 服务
 * 
 * @author yong.cao
 * @time 2017年7月22日下午8:48:42
 */

public interface SkuService {
    /**
     * 查询库存
     * 
     * @param productId
     * @return
     * @throws BizException
     */
    ResultBase<List<Sku>> selectSkuByProductId(Long productId) throws BizException;

    /**
     * 根据查询查询sku分页
     * 
     * @param query
     * @return
     * @throws BizException
     */
    Page<Sku> selectPageByQuery(SkuQueryDTO query) throws BizException;

    /**
     * 保存库存
     * 
     * @param sku
     * @return
     * @throws BizException
     */
    ResultBase<Integer> addSku(Sku sku) throws BizException;

    /**
     * 根据productId删除sku信息
     * 
     * @param productId
     * @return
     * @throws BizException
     */
    ResultBase<Integer> deleteSkuByProductId(Long productId) throws BizException;

    /**
     * 根据id删除sku信息
     * 
     * @param id
     * @return
     * @throws BizException
     */
    ResultBase<Integer> deleteSkuById(Long id) throws BizException;

    /**
     * 根据skuDTO更新sku信息
     * 
     * @param skuDTO
     * @return
     * @throws BizException
     */
    ResultBase<Integer> updateSkuBySkuDTO(SkuDTO skuDTO) throws BizException;

    /**
     * 根据id查询sku
     * 
     * @param skuId
     * @return
     * @throws BizException
     */
    ResultBase<Sku> selectSkuById(Long skuId) throws BizException;

    /**
     * 保存购物车到redis
     * 
     * @param buyerCart
     * @return
     * @throws BizException
     */
    ResultBase<Integer> insertBuyerCartToRedis(BuyerCart buyerCart, String username) throws BizException;

    /**
     * 从redis中取出用户的购物车
     * 
     * @param username
     * @return
     * @throws BizException
     */
    ResultBase<BuyerCart> selectBuyerCartFromRedis(String username) throws BizException;

    /**
     * 根据skuDTO中的oper更新sku信息
     * 
     * @param skuDTO
     * @return
     * @throws BizException
     */
    ResultBase<Integer> operatingSkuBySkuDTO(SkuDTO skuDTO) throws BizException;

}
