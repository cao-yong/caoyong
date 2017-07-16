package com.caoyong.core.service.user;

import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.user.Buyer;
import com.caoyong.exception.BizException;

/**
 * 用户services
 * @author yong.cao
 * @time 2017年7月16日上午11:19:47
 */
public interface BuyerService {
	/**
	 * 查询用户
	 * @param username
	 * @return
	 * @throws BizException
	 */
	ResultBase<Buyer> selectBuyerByUsername(String username) throws BizException;

}
