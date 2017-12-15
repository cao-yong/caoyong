package com.caoyong.core.bean.cart;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.caoyong.common.utlis.MoneyFormatUtil;
import com.caoyong.common.web.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 购物车
 * 
 * @author yong.cao
 * @time 2017年7月17日下午10:34:08
 */

@Getter
@Setter
@ToString
public class BuyerCart implements Serializable {

    private static final long serialVersionUID = 7818235262862461485L;
    /**
     * 商品结果集
     */
    private List<BuyerItem>   items            = new ArrayList<>();

    /**
     * 添加购物项
     * 
     * @param item
     */
    public void addItem(BuyerItem item) {
        //同款商品合并
        if (items.contains(item)) {
            //累计
            items.stream().filter(it -> it.equals(item)).forEach(it -> it.setAmount(it.getAmount() + item.getAmount()));
        } else {
            items.add(item);
        }
    }

    /**
     * 计算商品数量
     * 
     * @return
     */
    @JsonIgnore
    public Integer getProductAmount() {
        Integer result = 0;
        if (!items.isEmpty()) {
            //计数
            result = items.stream().mapToInt(item -> item.getAmount()).sum();
        }
        return result;
    }

    /**
     * 计算商品金额
     * 
     * @return
     */
    @JsonIgnore
    public String getProductPrice() {
        String result = Constants.DEAFAULT_PRICE;
        if (!items.isEmpty()) {
            //计算金额
            BigDecimal price = new BigDecimal(result);
            for (BuyerItem item : items) {
                price = price.add(new BigDecimal(item.getSku().getPrice()).multiply(new BigDecimal(item.getAmount())));
            }
            result = MoneyFormatUtil.format(price.toString());
        }
        return result;
    }

    /**
     * 计算运费
     * 
     * @return
     */
    @JsonIgnore
    public String getFee() {
        String result = (new BigDecimal(getProductPrice()).compareTo(new BigDecimal("79.00"))) == -1
                ? Constants.DEAFAULT_DELIVE_FEE
                : Constants.DEAFAULT_PRICE;
        return result;
    }

    /**
     * 总金额
     * 
     * @return
     */
    @JsonIgnore
    public String getTotalPrice() {
        String result = new BigDecimal(getProductPrice()).add(new BigDecimal(getFee())).toString();
        return result;
    }
}
