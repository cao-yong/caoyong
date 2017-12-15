package com.caoyong.core.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.caoyong.common.web.Constants;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.user.Buyer;
import com.caoyong.core.bean.user.BuyerQuery;
import com.caoyong.core.dao.user.BuyerDao;
import com.caoyong.exception.BizException;

import lombok.extern.slf4j.Slf4j;

/**
 * 用户service实现
 * 
 * @author yong.cao
 * @time 2017年7月16日上午11:20:11
 */

@Slf4j
@Service(version = "1.0.0")
public class BuyerServiceImpl implements BuyerService {
    @Autowired
    private BuyerDao buyerDao;

    /**
     * 查询用户
     * 
     * @param username
     * @return
     * @throws BizException
     */
    @Override
    public ResultBase<Buyer> selectBuyerByUsername(String username) throws BizException {
        log.info("selectBuyerByUsername start. username:{}", username);
        ResultBase<Buyer> result = new ResultBase<Buyer>();
        try {
            //查询
            BuyerQuery example = new BuyerQuery();
            example.createCriteria().andUsernameEqualTo(username).andIsDeletedEqualTo(Constants.CONSTANTS_N);
            List<Buyer> buyers = buyerDao.selectByExample(example);
            if (null != buyers && !buyers.isEmpty()) {
                result.setSuccess(true);
                result.setValue(buyers.get(0));
                return result;
            }
        } catch (Exception e) {
            log.error("selectBuyerByUsername error:", e.getMessage(), e);
        }
        log.info("selectBuyerByUsername end.");
        return result;
    }
}
