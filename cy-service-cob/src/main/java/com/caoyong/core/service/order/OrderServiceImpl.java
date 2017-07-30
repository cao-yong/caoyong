package com.caoyong.core.service.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.caoyong.common.enums.OrderStateEnum;
import com.caoyong.common.enums.PaymentStateEnum;
import com.caoyong.common.web.Constants;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.cart.BuyerCart;
import com.caoyong.core.bean.cart.BuyerItem;
import com.caoyong.core.bean.order.Detail;
import com.caoyong.core.bean.order.Order;
import com.caoyong.core.bean.product.Sku;
import com.caoyong.core.dao.order.DetailDao;
import com.caoyong.core.dao.order.OrderDao;
import com.caoyong.core.dao.product.ColorDao;
import com.caoyong.core.dao.product.ProductDao;
import com.caoyong.core.dao.product.SkuDao;
import com.caoyong.enums.ErrorCodeEnum;
import com.caoyong.exception.BizException;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * 订单Service
 * 
 * @author yong.cao
 * @time 2017年7月23日下午5:02:16
 */
@Slf4j
@Service("orderService")
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private SkuDao     skuDao;
    @Autowired
    private ColorDao   colorDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private OrderDao   orderDao;
    @Autowired
    private DetailDao  detailDao;
    @Autowired
    private Jedis      jedis;

    /**
     * 保存订单
     * 
     * @param order
     * @return
     */
    @Override
    public ResultBase<Integer> insertOrder(Order order) throws BizException {
        log.info("insertOrder start, order:{}", ToStringBuilder.reflectionToString(order, ToStringStyle.DEFAULT_STYLE));
        ResultBase<Integer> result = new ResultBase<>();
        try {
            //订单id
            Long id = jedis.incr("oid");
            order.setId(id);
            //从redis中查询购物车信息
            ResultBase<BuyerCart> buyerCartResult = selectBuyerCartFromRedis(order.getUsername());
            BuyerCart buyerCart = buyerCartResult.getValue();
            List<BuyerItem> items = buyerCart.getItems();
            //订单详情
            List<Detail> details = new ArrayList<>();
            //数组，保存字段，用来删除redis中提交过订单的购物项
            String[] fileds = null;
            if (!items.isEmpty()) {
                //创建数组
                fileds = new String[items.size()];
                //索引，记录循环次数
                int index = 0;
                for (BuyerItem buyerItem : items) {
                    //详情
                    Detail detail = new Detail();
                    //查询sku详细信息
                    buyerItem.setSku(selectSkuById(buyerItem.getSku().getId()).getValue());
                    //订单id
                    detail.setOrderId(id);
                    //商品id
                    detail.setProductId(buyerItem.getSku().getProductId());
                    //商品名称
                    detail.setProductName(buyerItem.getSku().getProduct().getName());
                    //颜色
                    detail.setColor(buyerItem.getSku().getColor().getName());
                    //尺码
                    detail.setSize(buyerItem.getSku().getSize());
                    //价格
                    detail.setPrice(buyerItem.getSku().getPrice());
                    //数量
                    detail.setAmount(buyerItem.getAmount());
                    //时间
                    detail.setCreateTime(new Date());
                    detail.setUpdateTime(new Date());
                    //detail加到list中
                    details.add(detail);
                    //保存字段到数据
                    fileds[index] = String.valueOf(buyerItem.getSku().getId());
                    //累加索引
                    index++;
                }
            }
            //设置运费
            order.setDeliverFee(buyerCart.getFee());
            //总额
            order.setTotalPrice(buyerCart.getTotalPrice());
            //订单金额
            order.setOrderPrice(buyerCart.getProductPrice());
            //订单状态
            Integer paymentState = order.getPaymentWay() == 1 ? PaymentStateEnum.CASH_ON_DELIVERY.getCode()
                    : PaymentStateEnum.PENDING_PAYMENT.getCode();
            order.setPaymentState(paymentState);
            order.setOrderState(OrderStateEnum.SUBMIT_ORDER.getCode());
            //用户id
            String uid = jedis.get(order.getUsername());
            order.setBuyerId(Long.parseLong(uid));
            order.setIsConfirm(Boolean.FALSE);
            order.setOrderCreateDate(new Date());
            order.setCreateTime(new Date());
            order.setCreator(Constants.SYSTEM);
            order.setModifier(Constants.SYSTEM);
            order.setUpdateTime(new Date());
            //保存订单
            int count = orderDao.insertSelective(order);
            //保存订单详情
            if (null != details && !details.isEmpty()) {
                details.stream().forEach(detail -> detailDao.insertSelective(detail));
            }
            //清空redis中的购物车
            //jedis.del(Constants.BUYER_CART_REDIS + order.getUsername());
            String key = Constants.BUYER_CART_REDIS + order.getUsername();
            //删除hash中指定字段
            if (null != fileds) {
                jedis.hdel(key, fileds);
            }
            result.setSuccess(true);
            result.setValue(count);
        } catch (Exception e) {
            result.setErrorCode(ErrorCodeEnum.UNKOWN_ERROR.getCode());
            result.setErrorMsg(ErrorCodeEnum.UNKOWN_ERROR.getMsg());
            log.error("insertOrder error:{}", e.getMessage(), e);
            throw new BizException(ErrorCodeEnum.UNKOWN_ERROR, e.getMessage(), e);
        }
        log.info("insertOrder end.");
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
}
