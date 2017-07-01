package com.caoyong.common.utlis;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.apache.commons.lang3.StringUtils;

/**
 * 金额格式化工具
 * @author yong.cao
 * @time 2017年7月1日下午11:33:51
 */
public class MoneyFormatUtil {

	public static String format(String money){
		if(StringUtils.isNotBlank(money)){
			//初始化金额，四舍五入
			BigDecimal decMoney = new BigDecimal(money).
					setScale(2, BigDecimal.ROUND_HALF_UP);
			//格式化
			DecimalFormat fm = new DecimalFormat("0.00");
			return fm.format(decMoney);
		}
		return "0.00";
	}
}
