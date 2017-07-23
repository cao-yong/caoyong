package com.caoyong.core.service.order;

import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.cart.BuyerCart;
import com.caoyong.core.bean.order.Order;
import com.caoyong.core.bean.product.Sku;
import com.caoyong.exception.BizException;

/**
 * 订单
 * @author yong.cao
 * @time 2017年7月23日下午5:06:27
 */
public interface OrderService {
	/**
	 * 从redis中取出用户的购物车
	 * @param username
	 * @return
	 * @throws BizException
	 */
	ResultBase<BuyerCart> selectBuyerCartFromRedis(String username) throws BizException;
	/**
	 * 根据id查询sku
	 * @param skuId
	 * @return
	 * @throws BizException
	 */
	ResultBase<Sku> selectSkuById(Long skuId) throws BizException;
	/**
	 * 保存订单
	 * @param order
	 * @return
	 * @throws BizException
	 */
	ResultBase<Integer> insertOrder(Order order) throws BizException;

}
