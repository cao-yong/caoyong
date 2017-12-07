package com.caoyong.core.service.product;

import java.util.Date;
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

import com.caoyong.common.enums.JqGridOperEnum;
import com.caoyong.common.utlis.MoneyFormatUtil;
import com.caoyong.common.web.Constants;
import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.cart.BuyerCart;
import com.caoyong.core.bean.cart.BuyerItem;
import com.caoyong.core.bean.product.Sku;
import com.caoyong.core.bean.product.SkuDTO;
import com.caoyong.core.bean.product.SkuQuery;
import com.caoyong.core.bean.product.SkuQuery.Criteria;
import com.caoyong.core.bean.product.SkuQueryDTO;
import com.caoyong.core.dao.product.ColorDao;
import com.caoyong.core.dao.product.ProductDao;
import com.caoyong.core.dao.product.SkuDao;
import com.caoyong.enums.ErrorCodeEnum;
import com.caoyong.exception.BizException;
import com.caoyong.utils.BeanConvertionHelp;
import com.caoyong.utils.CheckParamsUtil;

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
@Transactional(rollbackFor = Exception.class)
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

    @Override
    public Page<Sku> selectPageByQuery(SkuQueryDTO query) throws BizException {
        log.info("selectPageByQuery start. query:{}",
                ToStringBuilder.reflectionToString(query, ToStringStyle.DEFAULT_STYLE));
        Page<Sku> page = new Page<>();
        StringBuilder params = new StringBuilder();
        //设置分页查询条件
        SkuQuery example = new SkuQuery();
        example.setPageNo(query.getPageNo());
        example.setPageSize(query.getLimit());
        example.setStartRow(query.getStart());
        //查询条件
        Criteria createCriteria = example.createCriteria();
        //删除的不查询
        createCriteria.andIsDeletedEqualTo(Constants.CONSTANTS_N);
        if (null != query.getProductId()) {
            createCriteria.andProductIdEqualTo(query.getProductId());
            params.append("productId=").append(query.getProductId());
        }
        try {
            Integer count = skuDao.countByExample(example);
            List<Sku> rows = skuDao.selectByExample(example);
            if (null != rows && !rows.isEmpty()) {
                //循环
                rows.stream().forEach(x -> {
                    x.setColor(colorDao.selectByPrimaryKey(x.getColorId()));
                });
            }
            if (null != rows && !rows.isEmpty()) {
                log.info("selectPageByQuery results:{}", count);
                log.info("selectPageByQuery rows:{}",
                        ToStringBuilder.reflectionToString(rows, ToStringStyle.DEFAULT_STYLE));
                page.setStart(query.getStart());
                page.setResults(count);
                page.setLimit(query.getLimit());
                page.setPage(query.getPage());
                page.setPageNo(query.getPageNo());
                page.setRows(rows);
                page.setIsSuccess(true);
            }
        } catch (Exception e) {
            log.error("selectPageByQuery error:{}", e.getMessage(), e);
            page.setError("数据库查询产品分页失败");
            page.setErrorCode(e.getMessage());
            page.setErrorCode(e.getMessage());
            page.setResults(0);
        }
        log.info("selectPageByQuery end.");
        //分页展示
        String url = "/sku/skuList.do";
        page.pageView(url, params.toString());
        return page;
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

    @Override
    public ResultBase<Integer> deleteSkuByProductId(Long productId) throws BizException {
        log.info("deleteSkuByProductId start. productId:{}", productId);
        ResultBase<Integer> result = new ResultBase<>();
        if (productId == null) {
            result.setSuccess(false);
            result.setValue(0);
            throw new BizException(ErrorCodeEnum.PARAMETER_CAN_NOT_BE_NULL);
        }
        try {
            SkuQuery example = new SkuQuery();
            example.createCriteria().andProductIdEqualTo(productId);
            Sku record = new Sku();
            record.setUpdateTime(new Date());
            record.setIsDeleted(Constants.CONSTANTS_Y);
            int count = skuDao.updateByExampleSelective(record, example);
            if (count > 0) {
                result.setSuccess(true);
            }
        } catch (Exception e) {
            log.error("deleteSkuByProductId Exception:{}", e.getMessage(), e);
            result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
            result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
            throw new BizException(ErrorCodeEnum.UNKOWN_ERROR, e.getMessage(), e);
        }
        log.info("deleteSkuByProductId end result:{}",
                ToStringBuilder.reflectionToString(result, ToStringStyle.DEFAULT_STYLE));
        return result;
    }

    @Override
    public ResultBase<Integer> deleteSkuById(Long id) throws BizException {
        log.info("deleteSkuById start. id:{}", id);
        ResultBase<Integer> result = new ResultBase<>();
        if (id == null) {
            result.setSuccess(false);
            result.setValue(0);
            throw new BizException(ErrorCodeEnum.PARAMETER_CAN_NOT_BE_NULL);
        }
        try {
            Sku record = new Sku();
            record.setId(id);
            record.setUpdateTime(new Date());
            record.setIsDeleted(Constants.CONSTANTS_Y);
            int count = skuDao.updateByPrimaryKeySelective(record);
            if (count > 0) {
                result.setSuccess(true);
            }
        } catch (Exception e) {
            log.error("deleteSkuById Exception:{}", e.getMessage(), e);
            result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
            result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
            throw new BizException(ErrorCodeEnum.UNKOWN_ERROR, e.getMessage(), e);
        }
        log.info("deleteSkuById end result:{}",
                ToStringBuilder.reflectionToString(result, ToStringStyle.DEFAULT_STYLE));
        return result;
    }

    @Override
    public ResultBase<Integer> updateSkuBySkuDTO(SkuDTO skuDTO) throws BizException {
        log.info("updateSkuBySkuDTO start. skuDTO:{}",
                ToStringBuilder.reflectionToString(skuDTO, ToStringStyle.DEFAULT_STYLE));
        ResultBase<Integer> result = new ResultBase<Integer>();
        try {
            //运费
            if (StringUtils.isNotBlank(skuDTO.getDeliveFee())) {
                skuDTO.setDeliveFee(MoneyFormatUtil.format(skuDTO.getDeliveFee()));
            }
            //市场价
            if (StringUtils.isNotBlank(skuDTO.getMarketPrice())) {
                skuDTO.setMarketPrice(MoneyFormatUtil.format(skuDTO.getMarketPrice()));
            }
            //售价
            if (StringUtils.isNotBlank(skuDTO.getPrice())) {
                skuDTO.setPrice(MoneyFormatUtil.format(skuDTO.getPrice()));
            }
            //复制对象
            Sku sku = BeanConvertionHelp.copyBeanFieldValue(Sku.class, skuDTO);
            sku.setUpdateTime(new Date());
            int count = skuDao.updateByPrimaryKeySelective(sku);
            result.setValue(count);
            result.setSuccess(true);
            log.info("result:{}", ToStringBuilder.reflectionToString(result, ToStringStyle.DEFAULT_STYLE));
        } catch (Exception e) {
            result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
            result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
            log.error("updateSkuBySkuDTO error:{}", e.getMessage(), e);
            throw new BizException(ErrorCodeEnum.UNKOWN_ERROR, e.getMessage(), e);
        }
        log.info("updateSkuBySkuDTO end.");
        return result;
    }

    @Override
    public ResultBase<Integer> operatingSkuBySkuDTO(SkuDTO skuDTO) throws BizException {
        log.info("operatingSkuBySkuDTO start. skuDTO:{}", skuDTO);
        CheckParamsUtil.check(skuDTO, SkuDTO.class, "id");
        ResultBase<Integer> result = new ResultBase<>();
        try {
            if (JqGridOperEnum.DEL.getCode().equals(skuDTO.getOper())) {
                result = deleteSkuById(skuDTO.getId());
            } else if (JqGridOperEnum.EDIT.getCode().equals(skuDTO.getOper())) {
                result = updateSkuBySkuDTO(skuDTO);
            }
        } catch (Exception e) {
            log.error("operatingSkuBySkuDTO Exception:{}", e.getMessage(), e);
            result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
            result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
            throw new BizException(ErrorCodeEnum.UNKOWN_ERROR, e.getMessage(), e);
        }
        log.info("operatingSkuBySkuDTO end result:{}",
                ToStringBuilder.reflectionToString(result, ToStringStyle.DEFAULT_STYLE));
        return result;
    }
}
