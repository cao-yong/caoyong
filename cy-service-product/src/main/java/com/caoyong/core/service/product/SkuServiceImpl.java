package com.caoyong.core.service.product;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.caoyong.common.utlis.MoneyFormatUtil;
import com.caoyong.common.web.Constants;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.cart.BuyerCart;
import com.caoyong.core.bean.cart.BuyerItem;
import com.caoyong.core.bean.product.Sku;
import com.caoyong.core.bean.product.SkuQuery;
import com.caoyong.core.dao.product.ColorDao;
import com.caoyong.core.dao.product.ProductDao;
import com.caoyong.core.dao.product.SkuDao;
import com.caoyong.enums.ErrorCodeEnum;
import com.caoyong.exception.BizException;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * 最小销售单元 service
 * 
 * @author yong.cao
 * @time 2017年7月30日 下午9:22:02
 */
@Slf4j
@Service("skuService")
@Transactional
public class SkuServiceImpl implements SkuService {
    @Autowired
    private SkuDao     skuDao;
    @Autowired
    private ColorDao   colorDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private Jedis      jedis;

    /**
     * 根据productId查询sku结果集
     */
    @Override
    public ResultBase<List<Sku>> selectSkuByProductId(Long productId) throws BizException {
        log.info("selectSkuByProductId start. productId:{}", productId);
        ResultBase<List<Sku>> result = new ResultBase<List<Sku>>();
        try {
            SkuQuery example = new SkuQuery();
            example.createCriteria().andIsDeletedEqualTo(Constants.CONSTANTS_N).andProductIdEqualTo(productId);
            List<Sku> skus = skuDao.selectByExample(example);
            if (null != skus && !skus.isEmpty()) {
                //循环
                skus.stream().forEach(x -> {
                    x.setColor(colorDao.selectByPrimaryKey(x.getColorId()));
                });
            }
            if (null != skus && !skus.isEmpty()) {
                result.setSuccess(true);
                result.setValue(skus);
            }
            log.info("result:{}", ToStringBuilder.reflectionToString(result, ToStringStyle.DEFAULT_STYLE));
        } catch (Exception e) {
            log.error("selectSkuByProductId error:", e.getMessage(), e);
            result.setErrorCode(e.getMessage());
            result.setErrorMsg("查询sku失败");
        }
        log.info("selectSkuByProductId end");
        return result;
    }

    /**
     * 保存sku
     * 
     * @param sku
     * @throws BizException
     */
    @Override
    public ResultBase<Integer> addSku(Sku sku) throws BizException {
        log.info("addSku start. sku:{}", ToStringBuilder.reflectionToString(sku, ToStringStyle.DEFAULT_STYLE));
        ResultBase<Integer> result = new ResultBase<Integer>();
        try {
            //运费
            if (StringUtils.isNotBlank(sku.getDeliveFee())) {
                sku.setDeliveFee(MoneyFormatUtil.format(sku.getDeliveFee()));
            }
            //市场价
            if (StringUtils.isNotBlank(sku.getMarketPrice())) {
                sku.setMarketPrice(MoneyFormatUtil.format(sku.getMarketPrice()));
            }
            //售价
            if (StringUtils.isNotBlank(sku.getPrice())) {
                sku.setPrice(MoneyFormatUtil.format(sku.getPrice()));
            }
            int count = skuDao.updateByPrimaryKeySelective(sku);
            result.setValue(count);
            result.setSuccess(true);
            log.info("result:{}", ToStringBuilder.reflectionToString(result, ToStringStyle.DEFAULT_STYLE));
        } catch (Exception e) {
            result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
            result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
            log.error("addSku error:{}", e.getMessage(), e);
            throw new BizException(ErrorCodeEnum.UNKOWN_ERROR, e.getMessage(), e);
        }
        log.info("addSku end.");
        return result;
    }

    /**
     * 根据id查询sku
     * 
     * @param skuId
     * @return
     */
    @Override
    public ResultBase<Sku> selectSkuById(Long skuId) throws BizException {
        log.info("selectSkuById start. skuId:{}",
                ToStringBuilder.reflectionToString(skuId, ToStringStyle.DEFAULT_STYLE));
        ResultBase<Sku> result = new ResultBase<>();
        try {
            Sku sku = skuDao.selectByPrimaryKey(skuId);
            //加载商品对象
            sku.setProduct(productDao.selectByPrimaryKey(sku.getProductId()));
            //加载颜色对象
            sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
            result.setValue(sku);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
            result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
            log.error("selectSkuById error:{}", e.getMessage(), e);
        }

        log.info("selectSkuById end.");
        return result;
    }

    /**
     * 保存购物车到redis
     * 
     * @param buyerCart
     * @return
     */
    @Override
    public ResultBase<Integer> insertBuyerCartToRedis(BuyerCart buyerCart, String username) throws BizException {
        log.info("insertBuyerCartToRedis start. buyerCart:{}",
                ToStringBuilder.reflectionToString(buyerCart, ToStringStyle.DEFAULT_STYLE));
        ResultBase<Integer> result = new ResultBase<>();
        Integer count = 0;
        try {
            List<BuyerItem> items = buyerCart.getItems();
            if (null != buyerCart && !items.isEmpty()) {
                items.stream().forEach(item -> {
                    String key = "buyerCart:" + username;
                    //累计
                    Boolean hexists = jedis.hexists(key, String.valueOf(item.getSku().getId()));
                    if (hexists) {
                        jedis.hincrBy(key, String.valueOf(item.getSku().getId()), item.getAmount());
                    } else {
                        jedis.hset(key, String.valueOf(item.getSku().getId()), String.valueOf(item.getAmount()));
                    }

                });
            }
            count = 1;
            result.setValue(count);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
            result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
            log.error("insertBuyerCartToRedis error:{}", e.getMessage(), e);
            throw new BizException(ErrorCodeEnum.UNKOWN_ERROR, e.getMessage(), e);
        }
        log.info("insertBuyerCartToRedis end.");
        return result;
    }

    /**
     * 从redis中取出用户的购物车
     * 
     * @param username
     * @return
     * @throws BizException
     */
    @Override
    public ResultBase<BuyerCart> selectBuyerCartFromRedis(String username) throws BizException {
        log.info("selectBuyerCartFromRedis start. username:{}", username);
        ResultBase<BuyerCart> result = new ResultBase<>();
        try {
            BuyerCart buyerCart = new BuyerCart();
            Map<String, String> hgetAll = jedis.hgetAll("buyerCart:" + username);
            Set<Entry<String, String>> entrySet = hgetAll.entrySet();
            for (Entry<String, String> entry : entrySet) {
                Sku sku = new Sku();
                sku.setId(Long.parseLong(entry.getKey()));
                //购物项
                BuyerItem buyerItem = new BuyerItem();
                buyerItem.setSku(sku);
                buyerItem.setAmount(Integer.parseInt(entry.getValue()));
                //追加
                buyerCart.addItem(buyerItem);
            }
            result.setValue(buyerCart);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
            result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
            log.error("selectBuyerCartFromRedis error:", e.getMessage(), e);
        }
        log.info("selectBuyerCartFromRedis end.");
        return result;
    }
}
